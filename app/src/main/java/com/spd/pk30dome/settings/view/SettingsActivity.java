package com.spd.pk30dome.settings.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.liang.scancode.MsgEvent;
import com.spd.pk30dome.MyApp;
import com.spd.pk30dome.R;
import com.spd.pk30dome.base.BaseActivity;
import com.spd.pk30dome.utils.SpUtils;
import com.spd.pk30dome.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import speedata.com.blelib.base.BaseBleApplication;
import speedata.com.blelib.utils.DataManageUtils;
import speedata.com.blelib.utils.PK30DataUtils;

import static com.spd.pk30dome.settings.model.SettingsModel.MODEL;

/**
 * @author xuyan  右上角设置页面
 */
public class SettingsActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener {

    private SeekBar searchBar;
    private int currentBell;
    private TextView mTextView2;

    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void initToolBar() {
        mTitle.setText("设置页");
        mEnd.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {

        searchBar = findViewById(R.id.seekBar);
        currentBell = 50;
        mTextView2 = findViewById(R.id.textView2);
        mTextView2.setText(String.valueOf(currentBell));
        searchBar.setProgress(0);
        int max = 150;
        searchBar.setMax(max);
        searchBar.setOnSeekBarChangeListener(this);

        //radio部分
        radioGroup = findViewById(R.id.radioGroup);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);

        radioGroup.setOnCheckedChangeListener(this);
        //sp保存状态
        seButtonChecked();

    }

    private void seButtonChecked() {
        if (BaseBleApplication.mNotifyCharacteristic3 != null) {
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
        } else {
            ToastUtils.showShortToastSafe("请先连接PK30设备");
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        currentBell = progress + 50;
        mTextView2.setText(String.valueOf(currentBell));
        if (BaseBleApplication.mNotifyCharacteristic3 != null) {
            PK30DataUtils.fengMing(currentBell);
        } else {
            ToastUtils.showShortToastSafe("请先连接PK30设备");
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (BaseBleApplication.mNotifyCharacteristic3 != null) {
            switch (checkedId) {
                case R.id.radioButton1:
                    //sp保存结果
                    SpUtils.put(MyApp.getInstance(), MODEL, 0);
                    PK30DataUtils.setModel(0);
                    break;
                case R.id.radioButton2:
                    //sp保存结果
                    SpUtils.put(MyApp.getInstance(), MODEL, 1);
                    PK30DataUtils.setModel(3);
                    break;
                case R.id.radioButton3:
                    //sp保存结果
                    SpUtils.put(MyApp.getInstance(), MODEL, 2);
                    PK30DataUtils.setModel(0);
                    break;
                case R.id.radioButton4:
                    //sp保存结果
                    SpUtils.put(MyApp.getInstance(), MODEL, 3);
                    PK30DataUtils.setModel(3);
                    break;
                default:
                    break;
            }
        } else {
            ToastUtils.showShortToastSafe("请先连接PK30设备");
        }

    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MsgEvent mEvent) {
        String type = mEvent.getType();
        Object msg = mEvent.getMsg();

        if ("MODEL".equals(type)) {
            String string = (String) msg;
            switch (string) {
                case "00":
                    string = "模式更改为长度测量";
                    break;
                case "02":
                    string = "模式更改为宽度测量";
                    break;
                case "03":
                    string = "模式更改为高度测量";
                    break;
                case "01":
                    string = "模式更改为重量测量";
                    break;
                default:
                    break;
            }
            Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
        } else if ("FENGMING".equals(type)) {
            String string = (String) msg;
            int toInt = DataManageUtils.HexToInt(string);
            Toast.makeText(this, "蜂鸣器时长设置为" + toInt, Toast.LENGTH_SHORT).show();
        }
    }


}
