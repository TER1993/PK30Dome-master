package com.speedata.pk30dome.quick.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.speedata.pk30dome.R;
import com.speedata.pk30dome.base.BaseActivity;

/**
 * @author xuyan  收件人信息
 */
public class CollectionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    }
}
