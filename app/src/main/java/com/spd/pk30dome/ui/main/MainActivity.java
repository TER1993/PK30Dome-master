package com.spd.pk30dome.ui.main;


import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
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
import com.spd.pk30dome.database.DaoOptions;
import com.spd.pk30dome.database.OldBean;
import com.spd.pk30dome.mvp.MVPBaseActivity;
import com.spd.pk30dome.utils.AlertUtils;
import com.spd.pk30dome.utils.SpUtils;
import com.spd.pk30dome.utils.ToastUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

import speedata.com.blelib.base.BaseBleApplication;
import speedata.com.blelib.utils.DataManageUtils;
import speedata.com.blelib.utils.PK30DataUtils;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 *
 * @author xuyan
 */

public class MainActivity extends MVPBaseActivity<MainContract.View, MainPresenter> implements MainContract.View, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private Button mBtnTest;

    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;


    private TextView mTvSoftware;
    private TextView mTvHardware;
    private Button mBtnFengming;
    private SeekBar mProgress;
    private TextView mTvSeekbarValue;
    private Button mBtnClose;
    private LinearLayout mLl;
    private TextView mDeviceName;
    private TextView mDeviceAddress;
    private LinearLayout mIvOn;
    private KProgressHUD kProgressHUD;
    private Queue<Integer> queue;
    //开始测试
    private boolean isTest = false;
    private Button mBtnTestClose;
    private Button mBtnSoftware;
    private Button mBtnHardware;
    private TextView mTvVersion;


    /**
     * 新的页面，取自其他页面
     *
     */
    /**
     * 取自寄件人页面
     */
    private EditText mOddNumber;
    private EditText mTheSender;
    private EditText mPhoneNumber;
    private EditText mCompany;
    private EditText mAddress;

    private TextView mScan;
    private Button mNext;

    /**
     * 取自收件人页面
     */
    private EditText mTheCollection;
    private EditText mPhoneNumber2;
    private EditText mCompany2;
    private EditText mAddress2;
    private OldBean quickDataBean;

    /**
     * 取自货物信息相关部分
     */
    private EditText mGoodsType;
    private EditText mPackingType;

    private EditText mQuickOne;
    private EditText mQuickTwo;
    private EditText mQuickThree;
    private EditText mQuickFour;


    public static final int XISHU_XU = 5000;

    public static final String MAIN_NUMBER = "MAIN_NUMBER";

    public static final String MODEL = "MODEL";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        permission();
        EventBus.getDefault().register(this);

        // 初始化 Bluetooth adapter, 通过蓝牙管理器得到一个参考蓝牙适配器(API必须在以上android4.3或以上和版本)
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        @SuppressLint({"NewApi", "LocalSuppress"}) BluetoothAdapter mBluetoothAdapter = Objects.requireNonNull(bluetoothManager).getAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }

        initView();
        mBtnTestClose.setEnabled(false);

        quickDataBean = new OldBean();
        quickDataBean = DaoOptions.queryOldBean((String) SpUtils.get(MyApp.getInstance(), MAIN_NUMBER, "8888888888"));
        if (quickDataBean != null) {
            updtUi();
        }
    }

    private void updtUi() {
        mOddNumber.setText(quickDataBean.getMSenderOddNumber());
        mTheSender.setText(quickDataBean.getMSenderTheSender());
        mPhoneNumber.setText(quickDataBean.getMSenderPhoneNumber());
        mCompany.setText(quickDataBean.getMSenderCompany());
        mAddress.setText(quickDataBean.getMSenderAddress());

        mTheCollection.setText(quickDataBean.getMCollectionTheSender());
        mPhoneNumber2.setText(quickDataBean.getMCollectionPhoneNumber());
        mCompany2.setText(quickDataBean.getMCollectionCompany());
        mAddress2.setText(quickDataBean.getMCollectionAddress());

        mGoodsType.setText(quickDataBean.getMTypeOfGoods());
        mPackingType.setText(quickDataBean.getMPackingType());
        mQuickFour.setText(quickDataBean.getQuickNumber());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @SuppressLint("SetTextI18n")
    private void initView() {

        /*
         *  取自寄件人的页面初始化
         */
        mOddNumber = findViewById(R.id.sender_odd_number);
        mTheSender = findViewById(R.id.sender_the_sender);
        mPhoneNumber = findViewById(R.id.sender_phone_number);
        mCompany = findViewById(R.id.sender_company);
        mAddress = findViewById(R.id.sender_address);
        mScan = findViewById(R.id.sender_scan);
        mNext = findViewById(R.id.sender_next);
        mScan.setOnClickListener(this);
        mNext.setOnClickListener(this);

        /*
         *  取自收件人的页面初始化
         */
        mTheCollection = findViewById(R.id.collection_the_collection);
        mPhoneNumber2 = findViewById(R.id.collection_phone_number);
        mCompany2 = findViewById(R.id.collection_company);
        mAddress2 = findViewById(R.id.collection_address);

        /*
         *  取自货物信息
         */
        mGoodsType = findViewById(R.id.type_of_goods);
        mPackingType = findViewById(R.id.packing_type);
        mQuickOne = findViewById(R.id.quick_one);
        mQuickTwo = findViewById(R.id.quick_two);
        mQuickThree = findViewById(R.id.quick_three);
        mQuickFour = findViewById(R.id.quick_four);

        mBtnTest = findViewById(R.id.btn_test);
        mBtnTest.setOnClickListener(this);


        //radio部分
        radioGroup = findViewById(R.id.radioGroup);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);

        radioGroup.setOnCheckedChangeListener(this);
        //sp保存状态
        seButtonChecked();

        mTvSoftware = findViewById(R.id.tv_software);
        mTvHardware = findViewById(R.id.tv_hardware);
        mBtnFengming = findViewById(R.id.btn_fengming);
        mBtnFengming.setOnClickListener(this);

        mProgress = findViewById(R.id.progress);
        mProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTvSeekbarValue.setText((progress + 50) + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mTvSeekbarValue = findViewById(R.id.tv_seekbar_value);
        mBtnClose = findViewById(R.id.btn_close);
        mBtnClose.setOnClickListener(this);
        mLl = findViewById(R.id.ll);
        mDeviceName = findViewById(R.id.device_name);
        mDeviceAddress = findViewById(R.id.device_address);
        mIvOn = findViewById(R.id.iv_on);
        mIvOn.setOnClickListener(this);

        boolean cn = "CN".equals(getApplicationContext().getResources().getConfiguration().locale.getCountry());
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

        mBtnTestClose = findViewById(R.id.btn_test_close);
        mBtnTestClose.setOnClickListener(this);
        mBtnSoftware = findViewById(R.id.btn_software);
        mBtnSoftware.setOnClickListener(this);
        mBtnHardware = findViewById(R.id.btn_hardware);
        mBtnHardware.setOnClickListener(this);
        mTvVersion = findViewById(R.id.tv_version);
        mTvVersion.setText("V" + getVerName(getApplicationContext()));
    }


    private void seButtonChecked() {

        switch ((Integer) SpUtils.get(MyApp.getInstance(), MODEL, 0)) {
            case 0:
                radioButton1.setChecked(true);
                break;
            case 1:
                radioButton2.setChecked(true);
                break;
            case 2:
                radioButton3.setChecked(true);
                break;
            case 3:
                radioButton4.setChecked(true);
                break;
            default:
                break;

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
                testClose();
                mLl.setVisibility(View.GONE);
                Log.d("ZM_connect", "隐藏连接按键");
                mIvOn.setVisibility(View.GONE);
            }
            Log.d("ZM_connect", "" + result);

        } else if ("Save6DataErr".equals(type)) {
            Toast.makeText(MainActivity.this, (String) msg, Toast.LENGTH_SHORT).show();
        } else if ("L".equals(type)) {
            String string = (String) msg;
            mQuickThree.setText(string);
            doLoop();
        } else if ("W".equals(type)) {
            String string = (String) msg;
            String s = mQuickThree.getText().toString();
            mQuickThree.setText(s + "*" + string);
            doLoop();
        } else if ("H".equals(type)) {
            String string = (String) msg;
            String s = mQuickThree.getText().toString();
            mQuickThree.setText(s + "*" + string);

            String four = mQuickFour.getText().toString();

            if ("".equals(four)) {
                four = "1";
            }

            if ((Integer) SpUtils.get(MyApp.getInstance(), MODEL, 0) == 1) {
                //重量长宽高
                String[] x = mQuickThree.getText().toString().split("\\*");
                if (!(AlertUtils.isNumeric(x[0]) && AlertUtils.isNumeric(x[1]) && AlertUtils.isNumeric(x[2]))) {
                    ToastUtils.showShortToastSafe(R.string.Please_note);
                    return;
                }
                if (x.length == 3) {
                    double a = Double.parseDouble(x[0]);
                    double b = Double.parseDouble(x[1]);
                    double c = Double.parseDouble(x[2]);
                    int d = Integer.parseInt(four);
                    //不足1位,会以0补足.
                    DecimalFormat format = new DecimalFormat("0.00");
                    String y = format.format((a * b * c * d) / XISHU_XU);
                    mQuickTwo.setText(y);
                }
            } else if ((Integer) SpUtils.get(MyApp.getInstance(), MODEL, 0) == 0) {
                //重量长宽高
                String[] x = mQuickThree.getText().toString().split("\\*");
                if (!(AlertUtils.isNumeric(x[0]) && AlertUtils.isNumeric(x[1]) && AlertUtils.isNumeric(x[2]))) {
                    ToastUtils.showShortToastSafe(R.string.Please_note);
                    return;
                }
                if (x.length == 3) {
                    double a = Double.parseDouble(x[0]);
                    double b = Double.parseDouble(x[1]);
                    double c = Double.parseDouble(x[2]);
                    int d = Integer.parseInt(four);
                    //不足1位,会以0补足.
                    DecimalFormat format = new DecimalFormat("0.00");
                    String y = format.format((a * b * c * d) / XISHU_XU);
                    mQuickTwo.setText(y);
                }
            }

            doLoop();
        } else if ("G".equals(type)) {
            String string = (String) msg;
            mQuickOne.setText(string);

            doLoop();
        } else if ("SOFT".equals(type)) {
            mTvSoftware.setText(msg + "");
        } else if ("HARD".equals(type)) {
            mTvHardware.setText(msg + "");
        } else if ("codeResult".equals(type)) {

            mOddNumber.setText(msg + "");
            //doLoop();

        } else if ("MODEL".equals(type)) {
            String string = (String) msg;
            switch (string) {
                case "00":
                    string = getString(R.string.Mode_change);
                    break;
                case "02":
                    string = getString(R.string.Mode_change_to_w);
                    break;
                case "03":
                    string = getString(R.string.Mode_change_to_h);
                    break;
                case "01":
                    string = getString(R.string.Change_mode_to_wei);
                    break;
                default:
                    break;
            }
            Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
        } else if ("SHUTDOWN".equals(type)) {
            String string = (String) msg;
            if ("01".equals(string)) {
                Toast.makeText(MainActivity.this, getString(R.string.Shut_down_successfully), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.Shutdown_failed), Toast.LENGTH_SHORT).show();
            }
        } else if ("FENGMING".equals(type)) {
            String string = (String) msg;
            int toInt = DataManageUtils.HexToInt(string);
            Toast.makeText(MainActivity.this, getString(R.string.The_buzzer) + toInt, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 循环执行测量
     */
    private void doLoop() {
        if (isTest && queue != null) {
            Integer integer = queue.poll();
            if (integer != null) {
                PK30DataUtils.setModel(integer);
            } else {
                test();
            }

        }
    }

    private void initBluetooth() {
        Handler handler = new Handler();
        //1秒后执行Runnable中的run方法,否则初始化失败
        /*
         *要执行的操作
         */
        handler.postDelayed(this::closeBle, 1000);
    }

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

                case R.id.btn_software:
                    PK30DataUtils.getSoftware();
                    break;
                case R.id.btn_hardware:
                    PK30DataUtils.getHardware();
                    break;

                case R.id.btn_length:
                    PK30DataUtils.setModel(0);
                    break;
                case R.id.btn_width:
                    PK30DataUtils.setModel(1);
                    break;
                case R.id.btn_height:
                    PK30DataUtils.setModel(2);
                    break;
                case R.id.btn_weight:
                    PK30DataUtils.setModel(3);
                    break;

                case R.id.btn_test:
                    boolean b = test();
                    if (b) {
                        radioButton1.setEnabled(false);
                        radioButton2.setEnabled(false);
                        radioButton3.setEnabled(false);
                        radioButton4.setEnabled(false);
                        mBtnSoftware.setEnabled(false);
                        mBtnHardware.setEnabled(false);
                        mBtnFengming.setEnabled(false);
                        mBtnClose.setEnabled(false);
                        mBtnTest.setEnabled(false);
                        mBtnTestClose.setEnabled(true);
                    }

                    break;

                case R.id.btn_test_close:
                    testClose();
                    break;

                case R.id.btn_close:
                    PK30DataUtils.shutdown();
                    initBluetooth();
                    break;

                case R.id.btn_fengming:
                    int time = Integer.parseInt(mTvSeekbarValue.getText().toString());
                    PK30DataUtils.fengMing(time);
                    break;
                case R.id.sender_scan:
                    startScanAct();
                    break;
                case R.id.sender_next:
                    //下一步,先检测填写的信息
                    String one = mOddNumber.getText().toString();
                    String two = mTheSender.getText().toString();
                    String three = mPhoneNumber.getText().toString();
                    String four = mCompany.getText().toString();
                    String five = mAddress.getText().toString();

                    if ("".equals(one) || "".equals(two) || "".equals(three) || "".equals(four) || "".equals(five)) {
                        ToastUtils.showShortToastSafe(R.string.There_is_a_blank);
                        return;
                    }

                    //下一步,先检测填写的信息
                    String two2 = mTheCollection.getText().toString();
                    String three2 = mPhoneNumber2.getText().toString();
                    String four2 = mCompany2.getText().toString();
                    String five2 = mAddress2.getText().toString();

                    if ("".equals(two2) || "".equals(three2) || "".equals(four2) || "".equals(five2)) {
                        ToastUtils.showShortToastSafe(R.string.There_is_a_blank);
                        return;
                    }

                    //检测上面的输入内容
                    String goodType = mGoodsType.getText().toString();
                    String packingType = mPackingType.getText().toString();

                    if ("".equals(goodType) || "".equals(packingType)) {
                        ToastUtils.showShortToastSafe(R.string.There_is_a_blank);
                        return;
                    }

                    String qone = mQuickOne.getText().toString();
                    String qtwo = mQuickTwo.getText().toString();
                    String qthree = mQuickThree.getText().toString();
                    String qfour = mQuickFour.getText().toString();

                    if ("".equals(qone) || "".equals(qtwo) || "".equals(qthree) || "".equals(qfour)) {
                        ToastUtils.showShortToastSafe(R.string.There_is_a_blank);
                        return;
                    }

                    //检测完毕
                    quickDataBean = new OldBean();
                    quickDataBean.setMTypeOfGoods(goodType);
                    quickDataBean.setMPackingType(packingType);

                    quickDataBean.setMSenderOddNumber(one);
                    quickDataBean.setMSenderTheSender(two);
                    quickDataBean.setMSenderPhoneNumber(three);
                    quickDataBean.setMSenderCompany(four);
                    quickDataBean.setMSenderAddress(five);

                    quickDataBean.setMCollectionTheSender(two2);
                    quickDataBean.setMCollectionPhoneNumber(three2);
                    quickDataBean.setMCollectionCompany(four2);
                    quickDataBean.setMCollectionAddress(five2);

                    quickDataBean.setActualWeight(qone);
                    quickDataBean.setBubbleWeight(qtwo);
                    quickDataBean.setCargoSize(qthree);
                    quickDataBean.setQuickNumber(qfour);

                    DaoOptions.saveOldBean(quickDataBean);
                    SpUtils.put(MyApp.getInstance(), MAIN_NUMBER, "8888888888");
                    Toast.makeText(this, getString(R.string.Successfully_saved), Toast.LENGTH_SHORT).show();
                    chushihua();
                    break;

                default:
                    break;
            }
        } else {
            Toast.makeText(this, getString(R.string.Please_connect_the_PK), Toast.LENGTH_SHORT).show();
        }

    }

    private void chushihua() {
        mOddNumber.setText("8888888888");
        mQuickOne.setText("");
        mQuickTwo.setText("");
        mQuickThree.setText("");
        mQuickFour.setText("1");
        mPackingType.setText(R.string.carton);
        mGoodsType.setText(R.string.clothing);
        mTheSender.setText(R.string.Aaron);
        mPhoneNumber.setText("400-040-5565");
        mCompany.setText(R.string.Beijing_Speedata);
        mAddress.setText(R.string._26_1_03);

        mTheCollection.setText(R.string.Speedatagroup);
        mPhoneNumber2.setText("86-0755-82797385");
        mCompany2.setText(R.string.Beijing_Speedatagroup);
        mAddress2.setText(R.string._26_1_04);

    }

    private void testClose() {
        isTest = false;
        radioButton1.setEnabled(true);
        radioButton2.setEnabled(true);
        radioButton3.setEnabled(true);
        radioButton4.setEnabled(true);
        mBtnSoftware.setEnabled(true);
        mBtnHardware.setEnabled(true);
        mBtnFengming.setEnabled(true);
        mBtnClose.setEnabled(true);
        mBtnTest.setEnabled(true);
        mBtnTestClose.setEnabled(false);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radioButton1:
                //sp保存结果
                SpUtils.put(MyApp.getInstance(), MODEL, 0);
                break;
            case R.id.radioButton2:
                //sp保存结果
                SpUtils.put(MyApp.getInstance(), MODEL, 1);
                break;
            case R.id.radioButton3:
                //sp保存结果
                SpUtils.put(MyApp.getInstance(), MODEL, 2);
                break;
            case R.id.radioButton4:
                //sp保存结果
                SpUtils.put(MyApp.getInstance(), MODEL, 3);
                break;
            default:
                break;
        }
    }

    /**
     * 启动测试
     */
    private boolean test() {
        queue = new LinkedList<Integer>();
        isTest = true;

        boolean ckgzChecked = radioButton1.isChecked();
        boolean zckghChecked = radioButton2.isChecked();
        boolean changChecked = radioButton3.isChecked();
        boolean zhongChecked = radioButton4.isChecked();
        if (ckgzChecked) {
            //长宽高重量
            queue.offer(0);
            queue.offer(1);
            queue.offer(2);
            queue.offer(3);
        } else if (zckghChecked) {
            //重量长宽高
            queue.offer(3);
            queue.offer(0);
            queue.offer(1);
            queue.offer(2);
        } else if (changChecked) {
            //长
            queue.offer(0);
        } else if (zhongChecked) {
            //重量
            queue.offer(3);
        }

//        boolean checked = mCbScan.isChecked();
//        if (checked) {
//            startScanAct();
//        } else {
        if (queue.size() != 0) {
            doLoop();
        } else {
            Toast.makeText(this, getString(R.string.Please_first_check), Toast.LENGTH_SHORT).show();
            return false;
        }

        //      }
        return true;
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
            AndPermission.with(MainActivity.this)
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
                    .rationale((requestCode, rationale) -> AndPermission.rationaleDialog(MainActivity.this, rationale).show()).start();
        } else {
            AndPermission.with(MainActivity.this)
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
                    .rationale((requestCode, rationale) -> AndPermission.rationaleDialog(MainActivity.this, rationale).show()).start();

        }
    }

    PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, deniedPermissions)) {
                AndPermission.defaultSettingDialog(MainActivity.this, 300).show();
            }
        }
    };


    /**
     * 得到软件显示版本信息
     *
     * @param context 上下文
     * @return 当前版本信息
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            String packageName = context.getPackageName();
            verName = context.getPackageManager()
                    .getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }


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
