package com.speedata.pk30dome.quick.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.scandecode.ScanDecode;
import com.scandecode.inf.ScanInterface;
import com.speedata.pk30dome.MyApp;
import com.speedata.pk30dome.R;
import com.speedata.pk30dome.base.BaseActivity;
import com.speedata.pk30dome.utils.ToastUtils;

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

    private ScanInterface scanDecode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化扫描服务
        scanDecode = new ScanDecode(this);
        scanDecode.initService("true");
        scanDecode.getBarCode(s -> mOddNumber.setText(s));
    }

    @Override
    protected void onDestroy() {
        scanDecode.onDestroy();
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
                //用jar包处理扫描即可
                scanDecode.starScan();
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

                startActivity(new Intent(MyApp.getInstance(), CollectionActivity.class));
                finish();

                break;
            default:
                break;

        }
    }


}
