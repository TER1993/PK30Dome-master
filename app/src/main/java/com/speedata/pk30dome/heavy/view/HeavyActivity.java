package com.speedata.pk30dome.heavy.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scandecode.ScanDecode;
import com.scandecode.inf.ScanInterface;
import com.speedata.pk30dome.MyApp;
import com.speedata.pk30dome.R;
import com.speedata.pk30dome.base.BaseActivity;
import com.speedata.pk30dome.heavy.adapter.HeavyAdapter;
import com.speedata.pk30dome.heavy.adapter.HeavyLoadAdapter;
import com.speedata.pk30dome.quick.model.QuickBean;
import com.speedata.pk30dome.utils.Logcat;
import com.speedata.pk30dome.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuyan  重量稽查
 */
public class HeavyActivity extends BaseActivity implements View.OnClickListener, BaseQuickAdapter.OnItemChildClickListener {

    private EditText mOddNumber;

    private TextView mPriceLoad;
    private TextView mReturnLoad;
    private TextView mGoodsTypeLoad;
    private TextView mPackingTypeLoad;

    private EditText mPrice;
    private EditText mReturn;
    private EditText mGoodsType;
    private EditText mPackingType;

    private TextView mScan;
    private Button mAdd;
    private Button mClear;
    private Button mUpload;

    private ScanInterface scanDecode;

    private HeavyAdapter mAdapter;
    private HeavyLoadAdapter mLoadAdapter;
    private List<QuickBean> mList;
    private List<QuickBean> mLoadList;

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
        return R.layout.activity_heavy;
    }

    @Override
    protected void initToolBar() {
        mTitle.setText("重量稽查");
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mOddNumber = findViewById(R.id.heavy_odd_number);
        mScan = findViewById(R.id.heavy_scan);

        mPriceLoad = findViewById(R.id.quoted_price);
        mReturnLoad = findViewById(R.id.quick_return);
        mGoodsTypeLoad = findViewById(R.id.type_of_goods);
        mPackingTypeLoad = findViewById(R.id.packing_type);

        mAdd = findViewById(R.id.heavy_add);
        mClear = findViewById(R.id.heavy_clear);
        mUpload = findViewById(R.id.heavy_upload);

        mScan.setOnClickListener(this);
        mAdd.setOnClickListener(this);
        mClear.setOnClickListener(this);
        mUpload.setOnClickListener(this);

        mPrice = findViewById(R.id.heavy_quoted_price);
        mReturn = findViewById(R.id.heavy_return);
        mGoodsType = findViewById(R.id.heavy_type_of_goods);
        mPackingType = findViewById(R.id.heavy_packing_type);


        //初始化只显示的数据
        RecyclerView mRecyclerView = findViewById(R.id.heavy_rv_content);
        mLoadList = new ArrayList<>();
        mLoadAdapter = new HeavyLoadAdapter(mLoadList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mLoadAdapter);

        //初始化可编辑的显示数据
        RecyclerView recyclerView = findViewById(R.id.rv_content);
        mList = new ArrayList<>();
        mAdapter = new HeavyAdapter(mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.heavy_scan:
                //用jar包处理扫描即可
                scanDecode.starScan();
                break;
            case R.id.heavy_add:
                //先判断数据是否为空（空白则不添加）
                if (mLoadList.size() == 0) {
                    ToastUtils.showShortToastSafe("请先添加运单信息");
                    return;
                }

                if (mList.size() > 0) {
                    if ("".equals(mList.get(mList.size() - 1).getActualWeight()) || "".equals(mList.get(mList.size() - 1).getBubbleWeight())
                            || "".equals(mList.get(mList.size() - 1).getCargoSize()) || "".equals(mList.get(mList.size() - 1).getQuickNumber())) {
                        ToastUtils.showShortToastSafe("请先补全上一条内容再添加数据");
                        return;
                    }
                }

                //添加，list长度+1
                QuickBean quickBean = new QuickBean();
                mList.add(quickBean);
                mAdapter.replaceData(mList);
                break;
            case R.id.heavy_clear:
                //清除，list清除
                mList.clear();
                mAdapter.replaceData(mList);
                break;
            case R.id.heavy_upload:
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
                mList.get(mPosition).setActualWeight(txt);
            } else if (mChooseDialog == 2) {
                mList.get(mPosition).setBubbleWeight(txt);
            } else if (mChooseDialog == 3) {
                mList.get(mPosition).setCargoSize(txt);
            } else if (mChooseDialog == 4) {
                mList.get(mPosition).setQuickNumber(txt);
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
                mDialog = new AlertDialog.Builder(HeavyActivity.this)
                        .setTitle("请输入或修改实际重量")
                        .setView(etTxtInput)
                        .setPositiveButton("确定", dialogButtonOnClickListener)
                        .setNegativeButton("取消", dialogButtonOnClickListener)
                        .show();

                String q = mList.get(position).getActualWeight();
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
                mDialog = new AlertDialog.Builder(HeavyActivity.this)
                        .setTitle("请输入或修改泡重")
                        .setView(etTxtInput)
                        .setPositiveButton("确定", dialogButtonOnClickListener)
                        .setNegativeButton("取消", dialogButtonOnClickListener)
                        .show();

                String q = mList.get(position).getBubbleWeight();
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
                mDialog = new AlertDialog.Builder(HeavyActivity.this)
                        .setTitle("请输入或修改货物尺寸")
                        .setView(etTxtInput)
                        .setPositiveButton("确定", dialogButtonOnClickListener)
                        .setNegativeButton("取消", dialogButtonOnClickListener)
                        .show();

                String q = mList.get(position).getCargoSize();
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
                mDialog = new AlertDialog.Builder(HeavyActivity.this)
                        .setTitle("请输入或修改件数")
                        .setView(etTxtInput)
                        .setPositiveButton("确定", dialogButtonOnClickListener)
                        .setNegativeButton("取消", dialogButtonOnClickListener)
                        .show();

                String q = mList.get(position).getQuickNumber();
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
