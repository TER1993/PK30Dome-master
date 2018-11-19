package com.spd.pk30dome.menu.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.liang.scancode.MsgEvent;
import com.spd.pk30dome.MyApp;
import com.spd.pk30dome.R;
import com.spd.pk30dome.base.BaseActivity;
import com.spd.pk30dome.heavy.view.HeavyActivity;
import com.spd.pk30dome.quick.view.QuickActivity;
import com.spd.pk30dome.utils.SpUtils;
import com.spd.pk30dome.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import static com.spd.pk30dome.quick.model.QuickModel.MENU_ADD;

/**
 * @author xuyan  主页面
 */
public class MenuActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView mImageView;
    private TextView mHistory;
    private TextView mSearch;
    private TextView mQuick;
    private TextView mHeavy;
    private ListView mListView;
    private boolean connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        blueCreate();
    }

    /**
     * 连接蓝牙的创建部分
     */
    private void blueCreate() {
        mHandler = new Handler();

        // 检查当前手机是否支持ble 蓝牙,如果不支持退出程序
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // 初始化 Bluetooth adapter, 通过蓝牙管理器得到一个参考蓝牙适配器(API必须在以上android4.3或以上和版本)
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null) {
            mBluetoothAdapter = bluetoothManager.getAdapter();
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
            }
        }
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();

        // 检查设备上是否支持蓝牙
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        //如果 API level 是大于等于 23(Android 6.0) 时
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //判断是否具有权限
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //判断是否需要向用户解释为什么需要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // showToast("自Android 6.0开始需要打开位置权限才可以搜索到Ble设备");
                }
                //请求权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE_ACCESS_COARSE_LOCATION);
            }
        }

        mListView.setAdapter(mLeDeviceListAdapter);

    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_menu;
    }

    @Override
    protected void initToolBar() {
        mTitle.setText("智能尺连接");
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mImageView = findViewById(R.id.iv_bluetooth);
        mHistory = findViewById(R.id.history);
        mHeavy = findViewById(R.id.heavy);
        mQuick = findViewById(R.id.quick);
        mSearch = findViewById(R.id.search);

        mListView = findViewById(R.id.rv_content);
        mListView.setOnItemClickListener(this);
        mImageView.setOnClickListener(this);
        mHistory.setOnClickListener(this);
        mHeavy.setOnClickListener(this);
        mQuick.setOnClickListener(this);
        mSearch.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_bluetooth:
                //连接上一次的蓝牙设备
                if (connect) {
                    //断开连接
                    MyApp.getInstance().disconnect();
                    ToastUtils.showShortToastSafe("断开连接");
                } else {
                    //尝试连接
                    tryConnect();
                }

                break;
            case R.id.heavy:
                //重量稽查
                startActivity(new Intent(MyApp.getInstance(), HeavyActivity.class));
                break;
            case R.id.quick:
                //快速录单
                startActivity(new Intent(MyApp.getInstance(), QuickActivity.class));
                break;
            case R.id.history: {
                //历史连接
                Drawable navUp = getResources().getDrawable(R.drawable.menu_history_on);
                navUp.setBounds(0, 0, navUp.getMinimumWidth(), navUp.getMinimumHeight());
                mHistory.setCompoundDrawables(null, null, navUp, null);

                navUp = getResources().getDrawable(R.drawable.menu_search_off);
                navUp.setBounds(0, 0, navUp.getMinimumWidth(), navUp.getMinimumHeight());
                mSearch.setCompoundDrawables(navUp, null, null, null);

                //ToastUtils.showShortToastSafe("暂不提供历史连接等功能");
            }
            break;
            case R.id.search: {
                //查找设备
                Drawable navUp = getResources().getDrawable(R.drawable.menu_search_on);
                navUp.setBounds(0, 0, navUp.getMinimumWidth(), navUp.getMinimumHeight());
                mSearch.setCompoundDrawables(navUp, null, null, null);

                navUp = getResources().getDrawable(R.drawable.menu_history_off);
                navUp.setBounds(0, 0, navUp.getMinimumWidth(), navUp.getMinimumHeight());
                mHistory.setCompoundDrawables(null, null, navUp, null);
            }
            break;
            default:
                break;
        }

    }

    private void tryConnect() {
        String add = (String) SpUtils.get(MyApp.getInstance(), MENU_ADD, "");
        for (int i = 0; i < mLeDeviceListAdapter.mLeDevices.size(); i++) {
            if (add.equals(mLeDeviceListAdapter.mLeDevices.get(i).getAddress())) {
                //做类似position点击操作
                System.out.println("==position==" + i);
                final BluetoothDevice device = mLeDeviceListAdapter.getDevice(i);
                if (device == null) {
                    return;
                }
                if (mScanning) {
                    mBluetoothLeScanner.stopScan(mScanCallback);
                    mScanning = false;
                }
                MyApp.getInstance().getDeviceName(device);
            }
        }

    }


    /**
     * 蓝牙连接部分
     */

    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;

    private static final int REQUEST_ENABLE_BT = 1;
    /**
     *  10秒后停止查找搜索.
     */
    private static final long SCAN_PERIOD = 10000;
    private BluetoothLeScanner mBluetoothLeScanner;
    private static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;


    /**
     * Adapter for holding devices found through scanning.
      */
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;

        LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<>();
            mInflator = getLayoutInflater();
        }

        void addDevice(BluetoothDevice device) {
            if (!mLeDevices.contains(device)) {
                mLeDevices.add(device);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = view.findViewById(R.id.device_address);
                viewHolder.deviceName = view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0) {
                viewHolder.deviceName.setText(deviceName);
            } else {
                viewHolder.deviceName.setText(R.string.unknown_device);
            }
            viewHolder.deviceAddress.setText(device.getAddress());

            return view;
        }
    }

    /**
     *  5.0+.返蓝牙信息更新到界面
      */
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            mLeDeviceListAdapter.addDevice(result.getDevice());
            mLeDeviceListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }

    /**
     * 是否扫描蓝牙设备
     */
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(() -> {
                mScanning = false;
                mBluetoothLeScanner.stopScan(mScanCallback);
                invalidateOptionsMenu();
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothLeScanner.startScan(mScanCallback);
        } else {
            mScanning = false;
            mBluetoothLeScanner.stopScan(mScanCallback);
        }
        invalidateOptionsMenu();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 为了确保设备上蓝牙能使用, 如果当前蓝牙设备没启用,弹出对话框向用户要求授予权限来启用
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return;
        }

        if (mBluetoothAdapter.isEnabled()) {
            // Initializes list view adapter.
            mLeDeviceListAdapter = new LeDeviceListAdapter();
            //更新adapter显示数据
            mListView.setAdapter(mLeDeviceListAdapter);
            scanLeDevice(true);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mBluetoothAdapter.isEnabled()) {
            if (mBluetoothAdapter.isEnabled()) {
                scanLeDevice(false);
                mLeDeviceListAdapter.clear();
            }
        }

    }

    /**
     * Device scan callback.返蓝牙信息更新到界面
     */
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(() -> {
                mLeDeviceListAdapter.addDevice(device);
                mLeDeviceListAdapter.notifyDataSetChanged();
            });
        }
    };

    private long mkeyTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.ACTION_DOWN:
                if ((System.currentTimeMillis() - mkeyTime) > 2000) {
                    mkeyTime = System.currentTimeMillis();
                    Toast.makeText(MenuActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
                default:
                    break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 点击蓝牙设备的事件
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("==position==" + position);
        final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
        if (device == null) {
            return;
        }
        if (mScanning) {
            mBluetoothLeScanner.stopScan(mScanCallback);
            mScanning = false;
        }
        MyApp.getInstance().getDeviceName(device);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMain(MsgEvent msgEvent) {
        String type = msgEvent.getType();
        Object msg = msgEvent.getMsg();
        if ("ServiceConnectedStatus".equals(type)) {
            boolean result = (boolean) msg;
            if (result) {
                SpUtils.put(MyApp.getInstance(), MENU_ADD, MyApp.address);
                connect = true;
            } else {
                connect = false;
            }

        }

    }

}
