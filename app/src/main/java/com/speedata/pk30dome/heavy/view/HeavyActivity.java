package com.speedata.pk30dome.heavy.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.speedata.pk30dome.R;
import com.speedata.pk30dome.base.BaseActivity;

/**
 * @author xuyan  重量稽查
 */
public class HeavyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_heavy;
    }

    @Override
    protected void initToolBar() {
        mTitle.setText("重量稽查");
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {

    }
}
