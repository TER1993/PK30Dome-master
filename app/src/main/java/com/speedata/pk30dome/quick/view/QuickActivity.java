package com.speedata.pk30dome.quick.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.liang.scancode.MsgEvent;
import com.speedata.pk30dome.MyApp;
import com.speedata.pk30dome.R;
import com.speedata.pk30dome.base.BaseActivity;
import com.speedata.pk30dome.database.QuickBean;
import com.speedata.pk30dome.database.QuickDataBean;
import com.speedata.pk30dome.quick.adapter.QuickAdapter;
import com.speedata.pk30dome.quick.model.QuickModel;
import com.speedata.pk30dome.settings.model.SettingsModel;
import com.speedata.pk30dome.utils.Logcat;
import com.speedata.pk30dome.utils.SpUtils;
import com.speedata.pk30dome.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import speedata.com.blelib.utils.DataManageUtils;
import speedata.com.blelib.utils.PK30DataUtils;

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
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
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
                //开始测量
                test();
                break;
            case R.id.quick_clear:
                //清除，list清除
                mListBeans.clear();
                mAdapter.replaceData(mListBeans);
                break;
            case R.id.quick_next:
                //下一步，先检测输入内容全不全
                if (mListBeans.size() > 0) {
                    if ("".equals(mListBeans.get(mListBeans.size() - 1).getActualWeight()) || "".equals(mListBeans.get(mListBeans.size() - 1).getBubbleWeight())
                            || "".equals(mListBeans.get(mListBeans.size() - 1).getCargoSize()) || "".equals(mListBeans.get(mListBeans.size() - 1).getQuickNumber())) {
                        ToastUtils.showShortToastSafe("请先补全货物数据");
                        return;
                    }
                } else {
                    ToastUtils.showShortToastSafe("没有货物数据，请先添加货物信息");
                    return;
                }

                String price = mPrice.getText().toString();
                String mreturn = mReturn.getText().toString();
                String goodType = mGoodsType.getText().toString();
                String packingType = mPackingType.getText().toString();

                if ("".equals(price) || "".equals(mreturn) || "".equals(goodType) || "".equals(packingType)) {
                    ToastUtils.showShortToastSafe("存在空白项，请补全信息");
                    return;
                }
                //检测完毕，进入下一页面（到时保存传递1个实体类数据和一个list）
                QuickDataBean quickDataBean = new QuickDataBean();
                quickDataBean.setMQuotedPrice(price);
                quickDataBean.setMQuickReturn(mreturn);
                quickDataBean.setMTypeOfGoods(goodType);
                quickDataBean.setMPackingType(packingType);

                startActivity(new Intent(MyApp.getInstance(), SenderActivity.class)
                        .putExtra(QuickModel.INTENT_ONE, (Serializable) mListBeans).putExtra(QuickModel.INTENT_TWO, quickDataBean));
                finish();
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

    @SuppressLint("NewApi")
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

        Logcat.d("监听到了点击item的child");
        //判断弹出对话框，并修改mlist的数据即可。
        switch (view.getId()) {
            case R.id.quick_one: {
                DialogButtonOnClickListener dialogButtonOnClickListener = new DialogButtonOnClickListener();
                etTxtInput = new EditText(MyApp.getInstance());
                etTxtInput.setTextColor(getResources().getColor(R.color.black, null));
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
                etTxtInput.setTextColor(getResources().getColor(R.color.black, null));
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
                etTxtInput.setTextColor(getResources().getColor(R.color.black, null));
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
                etTxtInput.setTextColor(getResources().getColor(R.color.black, null));
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MsgEvent mEvent) {
        String type = mEvent.getType();
        Object msg = mEvent.getMsg();
        if ("ServiceConnectedStatus".equals(type)) {
            boolean result = (boolean) msg;
            Logcat.d("First:" + result);
            if (result) {
                Logcat.d("Address：" + MyApp.address + "Name：" + MyApp.name);
            } else {
                Logcat.d("未连接");
            }
            Logcat.d("" + result);
        } else if ("Save6DataErr".equals(type)) {
            Toast.makeText(MyApp.getInstance(), (String) msg, Toast.LENGTH_SHORT).show();
        } else if ("L".equals(type)) {
            String string = (String) msg;
            Logcat.d("L:" + string);
            String s = mListBeans.get(mListBeans.size() - 1).getCargoSize();
            mListBeans.get(mListBeans.size() - 1).setCargoSize(string);
            mAdapter.notifyDataSetChanged();
            doLoop();
        } else if ("W".equals(type)) {
            String string = (String) msg;
            Logcat.d("W:" + string);
            String s = mListBeans.get(mListBeans.size() - 1).getCargoSize();
            mListBeans.get(mListBeans.size() - 1).setCargoSize(s + "-" + string);
            mAdapter.notifyDataSetChanged();
            doLoop();
        } else if ("H".equals(type)) {
            String string = (String) msg;
            Logcat.d("H:" + string);
            String s = mListBeans.get(mListBeans.size() - 1).getCargoSize();
            mListBeans.get(mListBeans.size() - 1).setCargoSize(s + "-" + string);
            mAdapter.notifyDataSetChanged();
            doLoop();
        } else if ("G".equals(type)) {
            String string = (String) msg;
            Logcat.d("G:" + string);
            String s = mListBeans.get(mListBeans.size() - 1).getActualWeight();
            mListBeans.get(mListBeans.size() - 1).setActualWeight(string);
            mAdapter.notifyDataSetChanged();
            doLoop();
        } else if ("SOFT".equals(type)) {
            Logcat.d(msg + "");
        } else if ("HARD".equals(type)) {
            Logcat.d(msg + "");
        } else if (type.equals("codeResult")) {
            Logcat.d(msg + "");
            doLoop();

        } else if ("MODEL".equals(type)) {
            String string = (String) msg;
            switch (string) {
                case "00":
                    string = "模式更改为长度测量";
                    break;
                case "02":
                    string = "模式更改为宽度测量";
                    break;
                case "03":
                    string = "模式更改为高度测量";
                    break;
                case "01":
                    string = "模式更改为重量测量";
                    break;
            }
            Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
        } else if ("SHUTDOWN".equals(type)) {
            String string = (String) msg;
            if ("01".equals(string)) {
                Toast.makeText(MyApp.getInstance(), "关机成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MyApp.getInstance(), "关机失败", Toast.LENGTH_SHORT).show();
            }
        } else if ("FENGMING".equals(type)) {
            String string = (String) msg;
            int toInt = DataManageUtils.HexToInt(string);
            Toast.makeText(MyApp.getInstance(), "蜂鸣器时长设置为" + toInt, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 循环执行测量
     */
    private void doLoop() {
        if (isTest && queue != null) {
            Integer integer = queue.poll();
            if (integer != null) {
                PK30DataUtils.setModel(integer);
            } else {
                test();
            }

        }
    }

    private Queue<Integer> queue;
    //开始测试
    private boolean isTest = false;

    /**
     * 启动测试
     */
    private boolean test() {
        queue = new LinkedList<Integer>();
        isTest = true;
        if ((Integer) SpUtils.get(MyApp.getInstance(), SettingsModel.MODEL, 0) == 0) {
            //长宽高重量
            queue.offer(0);
            queue.offer(1);
            queue.offer(2);
            queue.offer(3);
        } else if ((Integer) SpUtils.get(MyApp.getInstance(), SettingsModel.MODEL, 0) == 1) {
            //重量长宽高
            queue.offer(3);
            queue.offer(0);
            queue.offer(1);
            queue.offer(2);
        } else if ((Integer) SpUtils.get(MyApp.getInstance(), SettingsModel.MODEL, 0) == 2) {
            //长
            queue.offer(0);
        } else if ((Integer) SpUtils.get(MyApp.getInstance(), SettingsModel.MODEL, 0) == 3) {
            //重量
            queue.offer(3);
        }
        if (queue.size() != 0) {
            doLoop();
        } else {
            Toast.makeText(this, "请先勾选需要启动的测量模式", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
