package com.speedata.pk30dome.quick.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.speedata.pk30dome.R;
import com.speedata.pk30dome.base.BaseActivity;
import com.speedata.pk30dome.utils.ToastUtils;

/**
 * @author xuyan  收件人信息
 */
public class CollectionActivity extends BaseActivity implements View.OnClickListener {

    private EditText mTheCollection;
    private EditText mPhoneNumber;
    private EditText mCompany;
    private EditText mAddress;

    private TextView mScan;
    private Button mPrint;
    private Button mUpload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_collection;
    }

    @Override
    protected void initToolBar() {
        mTitle.setText("智能录单");
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {

        mTheCollection = findViewById(R.id.collection_the_collection);
        mPhoneNumber = findViewById(R.id.collection_phone_number);
        mCompany = findViewById(R.id.collection_company);
        mAddress = findViewById(R.id.collection_address);

        mScan = findViewById(R.id.collection_scan);
        mUpload = findViewById(R.id.collection_upload);
        mPrint = findViewById(R.id.collection_print);

        mScan.setOnClickListener(this);
        mUpload.setOnClickListener(this);
        mPrint.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.collection_upload: {
                //下一步,先检测填写的信息
                String two = mTheCollection.getText().toString();
                String three = mPhoneNumber.getText().toString();
                String four = mCompany.getText().toString();
                String five = mAddress.getText().toString();

                if ("".equals(two) || "".equals(three) || "".equals(four) || "".equals(five)) {
                    ToastUtils.showShortToastSafe("存在空白项，请补全信息");
                    return;
                }
            }
            break;
            case R.id.collection_print: {
                //下一步,先检测填写的信息
                String two = mTheCollection.getText().toString();
                String three = mPhoneNumber.getText().toString();
                String four = mCompany.getText().toString();
                String five = mAddress.getText().toString();

                if ("".equals(two) || "".equals(three) || "".equals(four) || "".equals(five)) {
                    ToastUtils.showShortToastSafe("存在空白项，请补全信息");
                    return;
                }
            }
            break;
            default:
                break;
        }
    }
}
