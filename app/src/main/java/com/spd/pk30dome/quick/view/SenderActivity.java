package com.spd.pk30dome.quick.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.liang.scancode.CommonScanActivity;
import com.liang.scancode.MsgEvent;
import com.liang.scancode.utils.Constant;
import com.spd.pk30dome.MyApp;
import com.spd.pk30dome.R;
import com.spd.pk30dome.base.BaseActivity;
import com.spd.pk30dome.database.QuickBean;
import com.spd.pk30dome.database.QuickDataBean;
import com.spd.pk30dome.quick.model.QuickModel;
import com.spd.pk30dome.utils.Logcat;
import com.spd.pk30dome.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xuyan  寄件人信息
 */
public class SenderActivity extends BaseActivity implements View.OnClickListener {

    private EditText mOddNumber;
    private EditText mTheSender;
    private EditText mPhoneNumber;
    private EditText mCompany;
    private EditText mAddress;

    private TextView mScan;
    private Button mNext;


    private List<QuickBean> mList;
    private QuickDataBean quickDataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mList = new ArrayList<>();
        mList = (List<QuickBean>) getIntent().getSerializableExtra(QuickModel.INTENT_ONE);
        quickDataBean = getIntent().getParcelableExtra(QuickModel.INTENT_TWO);
        Logcat.d(mList.toString() + quickDataBean.toString());

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_sender;
    }

    @Override
    protected void initToolBar() {
        mTitle.setText("智能录单");
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mOddNumber = findViewById(R.id.sender_odd_number);
        mTheSender = findViewById(R.id.sender_the_sender);
        mPhoneNumber = findViewById(R.id.sender_phone_number);
        mCompany = findViewById(R.id.sender_company);
        mAddress = findViewById(R.id.sender_address);

        mScan = findViewById(R.id.sender_scan);
        mNext = findViewById(R.id.sender_next);

        mScan.setOnClickListener(this);
        mNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                    ToastUtils.showShortToastSafe("存在空白项，请补全信息");
                    return;
                }

                quickDataBean.setMSenderOddNumber(one);
                quickDataBean.setMSenderTheSender(two);
                quickDataBean.setMSenderPhoneNumber(three);
                quickDataBean.setMSenderCompany(four);
                quickDataBean.setMSenderAddress(five);

                startActivity(new Intent(MyApp.getInstance(), CollectionActivity.class)
                        .putExtra(QuickModel.INTENT_ONE, (Serializable) mList).putExtra(QuickModel.INTENT_TWO, quickDataBean));
                finish();

                break;
            default:
                break;

        }
    }

    private void startScanAct() {
        Intent intent = new Intent(this, CommonScanActivity.class);
        intent.putExtra(Constant.REQUEST_SCAN_MODE, Constant.REQUEST_SCAN_MODE_BARCODE_MODE);
        startActivity(intent);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MsgEvent mEvent) {
        String type = mEvent.getType();
        Object msg = mEvent.getMsg();
        if (type.equals("codeResult")) {
            mOddNumber.setText(msg + "");
        }
    }


}
