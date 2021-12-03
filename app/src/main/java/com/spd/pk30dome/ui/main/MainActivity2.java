package com.spd.pk30dome.ui.main;


import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.liang.scancode.CommonScanActivity;
import com.liang.scancode.MsgEvent;
import com.liang.scancode.utils.Constant;
import com.spd.pk30dome.MyApp;
import com.spd.pk30dome.R;
import com.spd.pk30dome.mvp.MVPBaseActivity;
import com.spd.pk30dome.utils.ToastUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Objects;

import speedata.com.blelib.base.BaseBleApplication;
import speedata.com.blelib.utils.PK30DataUtils;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 *
 * @author xuyan
 */

public class MainActivity2 extends MVPBaseActivity<MainContract.View, MainPresenter> implements MainContract.View, View.OnClickListener {

    private LinearLayout mLl;
    private TextView mDeviceName;
    private TextView mDeviceAddress;
    private LinearLayout mIvOn;

    private TextView mOne;
    private TextView mTwo;
    private TextView mThree;

    private EditText mE1;
    private EditText mE2;
    private EditText mE3;
    private EditText mE22;
    private EditText mE61;
    private EditText mE62;
    private EditText mE63;
    private EditText mName;

    private TextView mTypeShow;
    private TextView mReceive;

    private String mType = "";
    private int type = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main2);
        permission();
        EventBus.getDefault().register(this);

        // 初始化 Bluetooth adapter, 通过蓝牙管理器得到一个参考蓝牙适配器(API必须在以上android4.3或以上和版本)
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        @SuppressLint({"NewApi", "LocalSuppress"}) BluetoothAdapter mBluetoothAdapter = Objects.requireNonNull(bluetoothManager).getAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }

        initView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @SuppressLint("SetTextI18n")
    private void initView() {

        //显示气体结果
        mOne = findViewById(R.id.tv_show_one);
        mTwo = findViewById(R.id.tv_show_two);
        mThree = findViewById(R.id.tv_show_three);

        Button mb1 = findViewById(R.id.btn_one);
        mb1.setOnClickListener(this);
        Button mb2 = findViewById(R.id.btn_two);
        mb2.setOnClickListener(this);
        Button mb3 = findViewById(R.id.btn_three);
        mb3.setOnClickListener(this);

        Button mq1 = findViewById(R.id.btn_ch4);
        mq1.setOnClickListener(this);
        Button mq2 = findViewById(R.id.btn_o2);
        mq2.setOnClickListener(this);
        Button mq3 = findViewById(R.id.btn_co);
        mq3.setOnClickListener(this);

        mE1 = findViewById(R.id.et_two_one);
        mE2 = findViewById(R.id.et_two_two);
        mE3 = findViewById(R.id.et_two_three);
        mE22 = findViewById(R.id.et_three_two);
        mE61 = findViewById(R.id.et_six_one);
        mE62 = findViewById(R.id.et_six_two);
        mE63 = findViewById(R.id.et_six_three);

        mTypeShow = findViewById(R.id.tv_type);
        mReceive = findViewById(R.id.tv_receive);

        Button mFour = findViewById(R.id.btn_four);
        mFour.setOnClickListener(this);

        Button mFive = findViewById(R.id.btn_five);
        mFive.setOnClickListener(this);

        Button mSix = findViewById(R.id.btn_six);
        mSix.setOnClickListener(this);

        Button mSeven = findViewById(R.id.btn_seven);
        mSeven.setOnClickListener(this);

        mName = findViewById(R.id.et_name);

        Button mEight = findViewById(R.id.btn_eight);
        mEight.setOnClickListener(this);

        mLl = findViewById(R.id.ll);
        mDeviceName = findViewById(R.id.device_name);
        mDeviceAddress = findViewById(R.id.device_address);
        mIvOn = findViewById(R.id.iv_on);
        mIvOn.setOnClickListener(this);

        boolean cn = "CN".equals(getApplicationContext().getResources().getConfiguration().locale.getCountry());
        KProgressHUD kProgressHUD;
        if (cn) {
            kProgressHUD = KProgressHUD.create(getApplicationContext())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("重连中...")
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f);
        } else {
            kProgressHUD = KProgressHUD.create(getApplicationContext())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Reconnection...")
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f);
        }

    }


    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MsgEvent mEvent) {
        String type = mEvent.getType();
        Object msg = mEvent.getMsg();
        if ("ServiceConnectedStatus".equals(type)) {
            boolean result = (boolean) msg;
            Log.d("ZM_connect", "First:" + result);
            if (result) {
                mLl.setVisibility(View.VISIBLE);
                Log.d("ZM_connect", "显示连接按键");
                mDeviceAddress.setText("Address：" + MyApp.address);
                mDeviceName.setText("Name：" + MyApp.name);
                mIvOn.setVisibility(View.VISIBLE);
            } else {
                mLl.setVisibility(View.GONE);
                Log.d("ZM_connect", "隐藏连接按键");
                mIvOn.setVisibility(View.GONE);
            }
            Log.d("ZM_connect", "" + result);
        } else if ("Save6DataErr".equals(type)) {
            Toast.makeText(MainActivity2.this, (String) msg, Toast.LENGTH_SHORT).show();
        } else if ("ONE1".equals(type)) {
            String string = (String) msg;
            mOne.setText(string);
        } else if ("ONE2".equals(type)) {
            String string = (String) msg;
            mTwo.setText(string);
        } else if ("ONE3".equals(type)) {
            String string = (String) msg;
            mThree.setText(string);
        } else if ("TWO".equals(type)) {
            String string = (String) msg;
            if ("A1".equals(string)) {
                string = "甲烷高报警值已接收";
            } else if ("A4".equals(string)) {
                string = "氧气高报警值已接收";
            } else if ("A2".equals(string)) {
                string = "一氧化碳高报警值已接收";
            }
            mReceive.setText(string);
        } else if ("THREE".equals(type)) {
            String string = (String) msg;
            if ("A1".equals(string)) {
                string = "甲烷低报警值已接收";
            } else if ("A4".equals(string)) {
                string = "氧气低报警值已接收";
            } else if ("A2".equals(string)) {
                string = "一氧化碳低报警值已接收";
            }
            mReceive.setText(string);
        } else if ("FOUR".equals(type)) {
            String string = (String) msg;
            if ("A1".equals(string)) {
                string = "甲烷调零已接收";
            } else if ("A4".equals(string)) {
                string = "氧气调零已接收";
            } else if ("A2".equals(string)) {
                string = "一氧化碳调零已接收";
            }
            mReceive.setText(string);
        } else if ("FIVE".equals(type)) {
            String string = (String) msg;
            if ("A1".equals(string)) {
                string = "甲烷校准已接收";
            } else if ("A4".equals(string)) {
                string = "氧气校准已接收";
            } else if ("A2".equals(string)) {
                string = "一氧化碳校准已接收";
            }
            mReceive.setText(string);
        } else if ("SIX".equals(type)) {
            String string = (String) msg;
            if ("A1".equals(string)) {
                string = "甲烷标气值已接收";
            } else if ("A4".equals(string)) {
                string = "氧气标气值已接收";
            } else if ("A2".equals(string)) {
                string = "一氧化碳标气值已接收";
            }
            mReceive.setText(string);
        } else if ("SEVEN".equals(type)) {
            String string = (String) msg;
            if ("AA".equals(string)) {
                string = "复位指令已接收";
            }
            mReceive.setText(string);
        } else if ("EIGHT1".equals(type)) {
            String string = (String) msg;
            mReceive.setText("重命名指令已接收:" + string);
            MyApp.name = string;
        } else if ("EIGHT2".equals(type)) {
            String string = (String) msg;
            if ("AA".equals(string)) {
                string = "重命名指令已接收";
            }
            ToastUtils.showShortToastSafe(string);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (BaseBleApplication.mNotifyCharacteristic3 != null) {
            switch (v.getId()) {
                case R.id.btn_scan:
                    startScanAct();
                    break;
                case R.id.iv_on:
                    closeBle();
                    break;
                case R.id.btn_ch4:
                    type = 1;
                    mType = "A1";
                    mTypeShow.setText("甲烷");
                    break;
                case R.id.btn_o2:
                    type = 2;
                    mType = "A4";
                    mTypeShow.setText("氧气");
                    break;
                case R.id.btn_co:
                    type = 3;
                    mType = "A2";
                    mTypeShow.setText("一氧化碳");
                    break;
                case R.id.btn_one:
                    //01命令。
                    PK30DataUtils.setOne(MyApp.name);
                    break;
                case R.id.btn_two:
                    String data;
                    if (type == 1) {
                        data = mE1.getText().toString();
                    } else if (type == 2) {
                        data = mE2.getText().toString();
                    } else if (type == 3) {
                        data = mE3.getText().toString();
                    } else {
                        ToastUtils.showShortToastSafe("请选择气体类型");
                        return;
                    }
                    if (data.length() != 4) {
                        ToastUtils.showShortToastSafe("请输入正确的数据");
                        return;
                    }
                    PK30DataUtils.setTwo(MyApp.name, mType, data);
                    break;
                case R.id.btn_three:
                    String data3;
                    data3 = mE22.getText().toString();
                    if (data3.length() != 4) {
                        ToastUtils.showShortToastSafe("请输入正确的数据");
                        return;
                    }
                    PK30DataUtils.setThree(MyApp.name, "A4", data3);
                    break;
                case R.id.btn_four:
                    //04命令。
                    if (type == 1 || type == 2 || type == 3) {

                    } else {
                        ToastUtils.showShortToastSafe("请选择气体类型");
                        return;
                    }
                    PK30DataUtils.setFour(MyApp.name, mType);
                    break;
                case R.id.btn_five:
                    //04命令。
                    if (type == 1 || type == 2 || type == 3) {

                    } else {
                        ToastUtils.showShortToastSafe("请选择气体类型");
                        return;
                    }
                    PK30DataUtils.setFive(MyApp.name, mType);
                    break;
                case R.id.btn_six:
                    String data6;
                    if (type == 1) {
                        data6 = mE61.getText().toString();
                    } else if (type == 2) {
                        data6 = mE62.getText().toString();
                    } else if (type == 3) {
                        data6 = mE63.getText().toString();
                    } else {
                        ToastUtils.showShortToastSafe("请选择气体类型");
                        return;
                    }
                    if (data6.length() != 4) {
                        ToastUtils.showShortToastSafe("请输入正确的数据");
                        return;
                    }
                    PK30DataUtils.setSix(MyApp.name, mType, data6);
                    break;
                case R.id.btn_seven:
                    //07命令。
                    PK30DataUtils.setSeven(MyApp.name);
                    break;
                case R.id.btn_eight:
                    String data8;
                    data8 = mName.getText().toString();
                    if (data8.length() != 8) {
                        ToastUtils.showShortToastSafe("请输入正确的数据");
                        return;
                    }
                    PK30DataUtils.setEight(data8);
                    break;
                default:
                    break;
            }
        } else {
            Toast.makeText(this, getString(R.string.Please_connect_the_PK), Toast.LENGTH_SHORT).show();
        }

    }


    private void startScanAct() {
        Intent intent = new Intent(this, CommonScanActivity.class);
        intent.putExtra(Constant.REQUEST_SCAN_MODE, Constant.REQUEST_SCAN_MODE_BARCODE_MODE);
        startActivity(intent);
    }


    private void closeBle() {
        MyApp.getInstance().wantDisconnectBle();
        MyApp.getInstance().disconnect();
        mLl.setVisibility(View.GONE);
        Log.d("ZM_connect", "点击了断开");
        mIvOn.setVisibility(View.GONE);
    }


    /**
     * 权限申请
     */
    private void permission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AndPermission.with(MainActivity2.this)
                    .permission(Manifest.permission.ACCESS_COARSE_LOCATION
                            , Manifest.permission.ACCESS_FINE_LOCATION
                            , Manifest.permission.ACCESS_BACKGROUND_LOCATION
                            , Manifest.permission.VIBRATE
                            , Manifest.permission.FLASHLIGHT
                            , Manifest.permission.MODIFY_AUDIO_SETTINGS
                            , Manifest.permission.READ_HISTORY_BOOKMARKS
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE
                            , Manifest.permission.BLUETOOTH
                            , Manifest.permission.BLUETOOTH_ADMIN
                            , Manifest.permission.CAMERA)
                    .callback(listener)
                    .rationale((requestCode, rationale) -> AndPermission.rationaleDialog(MainActivity2.this, rationale).show()).start();
        } else {
            AndPermission.with(MainActivity2.this)
                    .permission(Manifest.permission.ACCESS_COARSE_LOCATION
                            , Manifest.permission.ACCESS_FINE_LOCATION
                            , Manifest.permission.VIBRATE
                            , Manifest.permission.FLASHLIGHT
                            , Manifest.permission.MODIFY_AUDIO_SETTINGS
                            , Manifest.permission.READ_HISTORY_BOOKMARKS
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE
                            , Manifest.permission.BLUETOOTH
                            , Manifest.permission.BLUETOOTH_ADMIN
                            , Manifest.permission.CAMERA)
                    .callback(listener)
                    .rationale((requestCode, rationale) -> AndPermission.rationaleDialog(MainActivity2.this, rationale).show()).start();

        }
    }

    PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(MainActivity2.this, deniedPermissions)) {
                AndPermission.defaultSettingDialog(MainActivity2.this, 300).show();
            }
        }
    };


    //返回键监听
    private long mkeyTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.ACTION_DOWN:
                if ((System.currentTimeMillis() - mkeyTime) > 2000) {
                    mkeyTime = System.currentTimeMillis();
                    boolean cn = "CN".equals(getApplicationContext().getResources().getConfiguration().locale.getCountry());
                    if (cn) {
                        Toast.makeText(getApplicationContext(), "再次点击返回退出", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Press the exit again", Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        closeBle();
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
}
