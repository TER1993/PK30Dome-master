package com.speedata.pk30dome.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.speedata.pk30dome.MyApp;
import com.speedata.pk30dome.R;
import com.speedata.pk30dome.settings.view.SettingsActivity;
import com.speedata.pk30dome.utils.AlertUtils;
import com.speedata.pk30dome.utils.ToastUtils;


/**
 * @author :Reginer in  2017/9/18 12:17.
 * 联系方式:QQ:282921012
 * 功能描述:Activity基类，所有Activity父类<p>
 * Activity的生命周期除了{@link #onRestart()}是两两对应的，其中有super，在每个生命周期中执行任务需要注意:</p>
 * 在onCreate、onStart、onResume、onRestart方法中执行，把要执行的方法放到super下一行写，其他的放到super上一行写
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    protected Toolbar mToolBar;
    protected TextView mTitle;
    protected TextView mEnd;
    private Dialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.base_layout);
        FrameLayout viewContent = findViewById(R.id.view_content);
        LayoutInflater.from(BaseActivity.this).inflate(getActLayoutId(), viewContent);
        mToolBar = findViewById(R.id.toolbar);
        mTitle = findViewById(R.id.toolbar_title);
        mEnd = findViewById(R.id.toolbar_end);
        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationOnClickListener(view -> finish());
        initView(savedInstanceState);
        mEnd.setText("");
        mEnd.setOnClickListener(view -> showSettingsAct());
    }

    @Override
    protected void onStart() {
        super.onStart();
        initToolBar();
    }

    /**
     * 在这个方法执行之前，Activity的界面都是不可见的.<p>
     * 建议在这个方法之前不要进行在主线程的耗时操作.<p>
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 这个方法无论会不会执行到{@link #onStop()},{@link #onDestroy()}都会执行,<p>
     * 可以用来保存数据,同时在 super.onSaveInstanceState(outState);上一行操作.
     *
     * @param outState 待操作数据.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 初始化ToolBar. <p>
     * 因为ToolBar的变化很小，所以这里就在这里初始化一下，如果有其他Activity有变化，可以使用{@link #mToolBar}的对应方法来设置<p>
     * 当然，也可以参照{@link #initView(Bundle)}方法来写.
     */
    private void initToolbar() {
        mToolBar = findViewById(R.id.toolbar);
        mTitle = findViewById(R.id.toolbar_title);
        mEnd = findViewById(R.id.toolbar_end);
        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationOnClickListener(view -> finish());

    }

    /**
     * 获取activity布局.
     *
     * @return layout
     */
    @LayoutRes
    protected abstract int getActLayoutId();

    /**
     * 设置ToolBar.
     */
    protected abstract void initToolBar();

    /**
     * 在这个方法中初始化控件.
     *
     * @param savedInstanceState 保存的数据,就是{@link #onSaveInstanceState(Bundle)}中的outState.
     */
    protected abstract void initView(@Nullable Bundle savedInstanceState);


    /**
     * 显示等待框 .
     */
    public void showProgress() {
        if (mDialog == null) {
            mDialog = new Dialog(this);
            //noinspection ConstantConditions
            mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            ProgressBar progressBar = new ProgressBar(this);
            progressBar.setBackgroundResource(android.R.color.transparent);
            mDialog.setContentView(progressBar);
        }
        mDialog.show();
    }

    /**
     * 取消等待框 .
     *
     * @param errorMsg 错误信息
     */
    public void dismissProgress(@Nullable String errorMsg) {
        if (!TextUtils.isEmpty(errorMsg)) {
            ToastUtils.showShortToastSafe(errorMsg);
        }
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * 退出对话框
     */
    public void showExitDialog() {
        AlertUtils.dialog(mContext, "注销",
                "是否注销当前用户？", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                }, (dialog, which) -> dialog.dismiss());
    }


    /**
     * 退出对话框
     */
    public void showFinishDialog() {
        AlertUtils.dialog(mContext, "退出",
                "是否退出程序？", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                }, (dialog, which) -> dialog.dismiss());
    }

    /**
     * 页面跳转
     */
    public void showSettingsAct() {
        startActivity(new Intent(MyApp.getInstance(), SettingsActivity.class));
    }

}
