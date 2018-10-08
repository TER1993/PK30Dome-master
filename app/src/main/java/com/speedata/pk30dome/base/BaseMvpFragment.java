package com.speedata.pk30dome.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * @author :Reginer in  2017/9/18 13:13.
 *         联系方式:QQ:282921012
 *         功能描述:mvp fragment基类
 */
public abstract class BaseMvpFragment<P extends BasePresenter> extends BaseFragment {
    protected P mPresenter;

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.attachView(this);
    }


    @Override
    public void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    /**
     * 创建presenter
     * @return presenter
     */
    protected abstract P createPresenter();
}