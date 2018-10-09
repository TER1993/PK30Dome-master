package com.speedata.pk30dome.settings.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.speedata.pk30dome.R;
import com.speedata.pk30dome.base.BaseActivity;

/**
 * @author xuyan  右上角设置页面
 */
public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    }
}
