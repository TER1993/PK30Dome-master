package com.speedata.pk30dome.quick.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.speedata.pk30dome.R;
import com.speedata.pk30dome.base.BaseActivity;
import com.speedata.pk30dome.quick.adapter.QuickAdapter;
import com.speedata.pk30dome.quick.model.QuickBean;
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

                //再保存最后一条未保存数据
                QuickBean quickBean1 = mListBeans.get(mListBeans.size() - 1);


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


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        //判断弹出对话框，并修改mlist的数据即可。
        switch (view.getId()) {
            case R.id.quick_one:
                break;
            case R.id.quick_two:
                break;
            case R.id.quick_three:
                break;
            case R.id.quick_four:
                break;
            default:
                break;
        }

    }
}
