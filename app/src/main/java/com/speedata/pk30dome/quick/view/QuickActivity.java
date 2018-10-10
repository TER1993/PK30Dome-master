package com.speedata.pk30dome.quick.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.speedata.pk30dome.MyApp;
import com.speedata.pk30dome.R;
import com.speedata.pk30dome.base.BaseActivity;
import com.speedata.pk30dome.quick.adapter.QuickAdapter;
import com.speedata.pk30dome.quick.model.QuickBean;
import com.speedata.pk30dome.utils.Logcat;
import com.speedata.pk30dome.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuyan  快速录单
 */
public class QuickActivity extends BaseActivity implements View.OnClickListener, BaseQuickAdapter.OnItemChildClickListener {

    private Button mAdd;
    private Button mClear;
    private Button mNext;

    private EditText mPrice;
    private EditText mReturn;
    private EditText mGoodsType;
    private EditText mPackingType;

    private QuickAdapter mAdapter;
    private List<QuickBean> mListBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_quick;
    }

    @Override
    protected void initToolBar() {
        mTitle.setText("快速录单");
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {

        mPrice = findViewById(R.id.quoted_price);
        mReturn = findViewById(R.id.quick_return);
        mGoodsType = findViewById(R.id.type_of_goods);
        mPackingType = findViewById(R.id.packing_type);

        mAdd = findViewById(R.id.quick_add);
        mClear = findViewById(R.id.quick_clear);
        mNext = findViewById(R.id.quick_next);

        mAdd.setOnClickListener(this);
        mClear.setOnClickListener(this);
        mNext.setOnClickListener(this);

        //初始化显示数据
        RecyclerView recyclerView = findViewById(R.id.rv_content);
        mListBeans = new ArrayList<>();
        mAdapter = new QuickAdapter(mListBeans);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quick_add:
                //先判断数据是否为空（空白则不添加）
                if (mListBeans.size() > 0) {
                    if ("".equals(mListBeans.get(mListBeans.size() - 1).getActualWeight()) || "".equals(mListBeans.get(mListBeans.size() - 1).getBubbleWeight())
                            || "".equals(mListBeans.get(mListBeans.size() - 1).getCargoSize()) || "".equals(mListBeans.get(mListBeans.size() - 1).getQuickNumber())) {
                        ToastUtils.showShortToastSafe("请先补全上一条内容再添加数据");
                        return;
                    }
                }


                //添加，list长度+1
                QuickBean quickBean = new QuickBean();
                mListBeans.add(quickBean);
                mAdapter.replaceData(mListBeans);
                break;
            case R.id.quick_clear:
                //清除，list清除
                mListBeans.clear();
                mAdapter.replaceData(mListBeans);
                break;
            case R.id.quick_next:
                //下一步，先检测输入内容全不全
                break;
            default:
                break;
        }

    }


    /**
     * item控件点击显示
     */
    private AlertDialog mDialog;
    /**
     * 控件弹出对话框上的输入框
     */
    private EditText etTxtInput;
    private int mChooseDialog;
    private int mPosition;

    /**
     * 问题退出时的对话框的按钮点击事件
     */
    private class DialogButtonOnClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    // 确定
                    String txt = etTxtInput.getText().toString();
                    mDialog.dismiss();
                    updateList(txt);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    // 取消显示对话框
                    mDialog.dismiss();
                    break;
                default:
                    break;
            }
        }

        //更新数组以及控件的显示
        private void updateList(String txt) {

            if (mChooseDialog == 1) {
                mListBeans.get(mPosition).setActualWeight(txt);
            } else if (mChooseDialog == 2) {
                mListBeans.get(mPosition).setBubbleWeight(txt);
            } else if (mChooseDialog == 3) {
                mListBeans.get(mPosition).setCargoSize(txt);
            } else if (mChooseDialog == 4) {
                mListBeans.get(mPosition).setQuickNumber(txt);
            }
            mAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

        Logcat.d("监听到了点击item的child");
        //判断弹出对话框，并修改mlist的数据即可。
        switch (view.getId()) {
            case R.id.quick_one: {
                DialogButtonOnClickListener dialogButtonOnClickListener = new DialogButtonOnClickListener();
                etTxtInput = new EditText(MyApp.getInstance());
                mDialog = new AlertDialog.Builder(QuickActivity.this)
                        .setTitle("请输入或修改实际重量")
                        .setView(etTxtInput)
                        .setPositiveButton("确定", dialogButtonOnClickListener)
                        .setNegativeButton("取消", dialogButtonOnClickListener)
                        .show();

                String q = mListBeans.get(position).getActualWeight();
                if (q == null) {
                    q = "";
                }
                etTxtInput.append(q);
                mPosition = position;
                mChooseDialog = 1;
            }
            break;
            case R.id.quick_two: {
                DialogButtonOnClickListener dialogButtonOnClickListener = new DialogButtonOnClickListener();
                etTxtInput = new EditText(MyApp.getInstance());
                mDialog = new AlertDialog.Builder(QuickActivity.this)
                        .setTitle("请输入或修改泡重")
                        .setView(etTxtInput)
                        .setPositiveButton("确定", dialogButtonOnClickListener)
                        .setNegativeButton("取消", dialogButtonOnClickListener)
                        .show();

                String q = mListBeans.get(position).getBubbleWeight();
                if (q == null) {
                    q = "";
                }
                etTxtInput.append(q);
                mPosition = position;
                mChooseDialog = 2;
            }
            break;
            case R.id.quick_three: {
                DialogButtonOnClickListener dialogButtonOnClickListener = new DialogButtonOnClickListener();
                etTxtInput = new EditText(MyApp.getInstance());
                mDialog = new AlertDialog.Builder(QuickActivity.this)
                        .setTitle("请输入或修改货物尺寸")
                        .setView(etTxtInput)
                        .setPositiveButton("确定", dialogButtonOnClickListener)
                        .setNegativeButton("取消", dialogButtonOnClickListener)
                        .show();

                String q = mListBeans.get(position).getCargoSize();
                if (q == null) {
                    q = "";
                }
                etTxtInput.append(q);
                mPosition = position;
                mChooseDialog = 3;
            }
            break;
            case R.id.quick_four: {
                DialogButtonOnClickListener dialogButtonOnClickListener = new DialogButtonOnClickListener();
                etTxtInput = new EditText(MyApp.getInstance());
                mDialog = new AlertDialog.Builder(QuickActivity.this)
                        .setTitle("请输入或修改件数")
                        .setView(etTxtInput)
                        .setPositiveButton("确定", dialogButtonOnClickListener)
                        .setNegativeButton("取消", dialogButtonOnClickListener)
                        .show();

                String q = mListBeans.get(position).getQuickNumber();
                if (q == null) {
                    q = "";
                }
                etTxtInput.append(q);
                mPosition = position;
                mChooseDialog = 4;
            }
            break;
            default:
                break;
        }

    }
}
