package speedata.com.blelib.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

import speedata.com.blelib.bean.SampleGattAttributes;
import speedata.com.blelib.utils.ByteUtils;
import speedata.com.blelib.utils.DataManageUtils;
import speedata.com.blelib.utils.PK30DataUtils;

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
@SuppressLint("NewApi")
public class BluetoothLeService extends Service {
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";
    public final static String NOTIFICATION_DATA = "com.example.bluetooth.le.NOTIFICATION_DATA";
    public final static String NOTIFICATION_DATA_ERR = "com.example.bluetooth.le.NOTIFICATION_DATA_ERR";

    public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);

    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
//                mBluetoothGatt.close();
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }

        //discoverServices 搜索连接设备所支持的service。
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        //readCharacteristic 读取指定的characteristic。
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        //setCharacteristicNotification 设置当指定characteristic值变化时，发出通知。
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }

        //写入回调
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Handler handler = new Handler(Looper.getMainLooper());
            if (status == BluetoothGatt.GATT_SUCCESS) {
                setCharacteristicNotification(characteristic, true);
            } else if (status == BluetoothGatt.GATT_FAILURE) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BluetoothLeService.this, "写入失败", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (status == BluetoothGatt.GATT_WRITE_NOT_PERMITTED) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BluetoothLeService.this, "没有权限", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };

    public void wirteCharacteristic(BluetoothGattCharacteristic characteristic) {

        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }

        mBluetoothGatt.writeCharacteristic(characteristic);
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    /**
     * 是否接收1结束
     */
    private boolean over = true;
    /**
     * 1的完整命令
     */
    private byte[] all = new byte[27];
    /**
     * 1的第一段20长度部分
     */
    private byte[] all1 = new byte[20];
    /**
     * 1的第二段7长度部分
     */
    private byte[] all2 = new byte[7];

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        Log.d(TAG, "characteristic:" + characteristic.getUuid());
        // This is special handling for the Heart Rate Measurement profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            int flag = characteristic.getProperties();
            int format;
            if ((flag & 0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16;
                Log.d(TAG, "Heart rate format UINT16.");
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8;
                Log.d(TAG, "Heart rate format UINT8.");
            }
            final int heartRate = characteristic.getIntValue(format, 1);
            Log.d(TAG, String.format("Received heart rate: %d", heartRate));
            intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
            sendBroadcast(intent);
            //就这一个接收
        } else if ("0000b351-d6d8-c7ec-bdf0-eab1bfc6bcbc".equals(characteristic.getUuid().toString())) {
            final byte[] data = characteristic.getValue();
            String bytesToHexString = DataManageUtils.bytesToHexString(data);
            Log.d("ZM", "信道6接收: " + bytesToHexString);
            if (data == null) {
                intent.putExtra(NOTIFICATION_DATA_ERR, "数据为空");
                sendBroadcast(intent);
                return;
            }

            if (data.length == 20 && data[9] == (byte) 0x01 && over) {
                over = false;
                all1 = data;
                Log.d("ZM", "all1: " + DataManageUtils.bytesToHexString(all1));
                return;
            } else if (data.length == 7 && !over) {
                over = true;
                all2 = data;
                Log.d("ZM", "all2: " + DataManageUtils.bytesToHexString(all2));
                all = ByteUtils.concatAll(all1, all2);
                Log.d("ZM", "all: " + DataManageUtils.bytesToHexString(all));
                boolean checkData = PK30DataUtils.checkData2(all);
                Log.d("ZM", "checkData: " + checkData);
                // TODO: 2021/12/1/001 校验好了之后再用它
//                if (!checkData) {
//                    Log.d("ZM", "数据错误");
//                    intent.putExtra(NOTIFICATION_DATA_ERR, "数据错误" + bytesToHexString);
//                    sendBroadcast(intent);
//                    return;
//                }
                if (all[9] == (byte) 0x01) {
                    Log.d("ZM", "readOne");
                    //矿灯主叫测定器数据命令返回，3种气体数据AA，甲烷 氧气 一氧化碳
                    //根据命令，获取显示数据
                    PK30DataUtils.readOne(BluetoothLeService.this, intent, all);
                }
                return;
            }

            boolean checkData = PK30DataUtils.checkData2(data);

            if (!checkData) {
                intent.putExtra(NOTIFICATION_DATA_ERR, "数据错误" + bytesToHexString);
                sendBroadcast(intent);
                return;
            }

            if (data[9] == (byte) 0x02) {
                //高报警值 区分每种气体
                PK30DataUtils.readTwo(BluetoothLeService.this, intent, data);
            } else if (data[9] == (byte) 0x03) {
                //低报警值 区分每种气体
                PK30DataUtils.readThree(BluetoothLeService.this, intent, data);
            } else if (data[9] == (byte) 0x04) {
                //调零 区分每种气体
                PK30DataUtils.readFour(BluetoothLeService.this, intent, data);
            } else if (data[9] == (byte) 0x05) {
                //校准 区分每种气体
                PK30DataUtils.readFive(BluetoothLeService.this, intent, data);
            } else if (data[9] == (byte) 0x06) {
                //标气值 区分每种气体
                PK30DataUtils.readSix(BluetoothLeService.this, intent, data);
            } else if (data[9] == (byte) 0x07) {
                //复位 固定指令 除蓝牙名
                PK30DataUtils.readSeven(BluetoothLeService.this, intent, data);
            } else if (data[9] == (byte) 0x08) {
                //修改蓝牙名称 固定指令 除了新名字
                PK30DataUtils.readEight(BluetoothLeService.this, intent, data);
            } else {
                intent.putExtra(NOTIFICATION_DATA_ERR, "其他" + bytesToHexString);
                sendBroadcast(intent);
            }
        }
    }

    public final static String NOTIFICATION_DATA_ONE1 = "com.example.bluetooth.le.NOTIFICATION_DATA_L";
    public final static String NOTIFICATION_DATA_ONE2 = "com.example.bluetooth.le.NOTIFICATION_DATA_W";
    public final static String NOTIFICATION_DATA_ONE3 = "com.example.bluetooth.le.NOTIFICATION_DATA_H";
    public final static String NOTIFICATION_DATA_TWO = "com.example.bluetooth.le.NOTIFICATION_DATA_G";
    public final static String NOTIFICATION_DATA_THREE = "com.example.bluetooth.le.NOTIFICATION_DATA_MAC";
    public final static String NOTIFICATION_DATA_FOUR = "com.example.bluetooth.le.NOTIFICATION_DATA_SOFT";
    public final static String NOTIFICATION_DATA_FIVE = "com.example.bluetooth.le.NOTIFICATION_DATA_HARD";
    public final static String NOTIFICATION_DATA_SIX = "com.example.bluetooth.le.NOTIFICATION_DATA_MODEL";
    public final static String NOTIFICATION_DATA_SEVEN = "com.example.bluetooth.le.NOTIFICATION_SHUTDOWN";
    public final static String NOTIFICATION_DATA_EIGHT1 = "com.example.bluetooth.le.NOTIFICATION_FENGMING";
    public final static String NOTIFICATION_DATA_EIGHT2 = "com.example.bluetooth.le.NOTIFICATION_DIDIAN";


    public class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     * 连接到在蓝牙LE设备上托管的GATT服务器。
     *
     * @param address The device address of the destination device.目标设备的设备地址。
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.以前连接设备。尝试重新连接。
        if (address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.我们想要直接连接到设备上，所以我们设置了自动连接 参数为false。
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.在使用了一个可使用的设备之后，应用程序必须调用这个方法来确保资源的使用。
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     * 启用或禁用给定特性的通知。
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        boolean isEnableNotification = mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        if (isEnableNotification) {
            List<BluetoothGattDescriptor> descriptorList = characteristic.getDescriptors();
            if (descriptorList != null && descriptorList.size() > 0) {
                for (BluetoothGattDescriptor descriptor : descriptorList) {
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    mBluetoothGatt.writeDescriptor(descriptor);
                }
            }
        }
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     * 检索连接设备上支持的GATT服务的列表
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) {
            return null;
        }

        return mBluetoothGatt.getServices();
    }
}
