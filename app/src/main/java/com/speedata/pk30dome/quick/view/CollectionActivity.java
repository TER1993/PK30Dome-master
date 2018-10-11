package com.speedata.pk30dome.quick.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.speedata.pk30dome.R;
import com.speedata.pk30dome.base.BaseActivity;
import com.speedata.pk30dome.database.DaoOptions;
import com.speedata.pk30dome.database.QuickBean;
import com.speedata.pk30dome.database.QuickDataBean;
import com.speedata.pk30dome.quick.model.QuickModel;
import com.speedata.pk30dome.utils.Logcat;
import com.speedata.pk30dome.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

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

    private List<QuickBean> mList;
    private QuickDataBean quickDataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = new ArrayList<>();
        mList = (List<QuickBean>) getIntent().getSerializableExtra(QuickModel.INTENT_ONE);
        quickDataBean = getIntent().getParcelableExtra(QuickModel.INTENT_TWO);
        Logcat.d(mList.toString() + quickDataBean.toString());
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

                //处理并保存数据
                quickDataBean.setMCollectionTheSender(two);
                quickDataBean.setMCollectionPhoneNumber(three);
                quickDataBean.setMCollectionCompany(four);
                quickDataBean.setMCollectionAddress(five);

                for (int i = 0; i < mList.size(); i++) {
                    mList.get(i).setMSenderOddNumber(quickDataBean.getMSenderOddNumber());
                }

                DaoOptions.saveQuickDataBean(quickDataBean);
                DaoOptions.saveQuickBeanData(mList);
                ToastUtils.showShortToastSafe("已保存");
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
