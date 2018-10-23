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
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.liang.scancode.MsgEvent;
import com.scandecode.ScanDecode;
import com.scandecode.inf.ScanInterface;
import com.speedata.pk30dome.MyApp;
import com.speedata.pk30dome.R;
import com.speedata.pk30dome.base.BaseActivity;
import com.speedata.pk30dome.database.DaoOptions;
import com.speedata.pk30dome.database.HeavyBean;
import com.speedata.pk30dome.database.HeavyDataBean;
import com.speedata.pk30dome.database.QuickBean;
import com.speedata.pk30dome.database.QuickDataBean;
import com.speedata.pk30dome.heavy.adapter.HeavyAdapter;
import com.speedata.pk30dome.heavy.adapter.HeavyLoadAdapter;
import com.speedata.pk30dome.settings.model.SettingsModel;
import com.speedata.pk30dome.utils.Logcat;
import com.speedata.pk30dome.utils.SpUtils;
import com.speedata.pk30dome.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import speedata.com.blelib.utils.DataManageUtils;
import speedata.com.blelib.utils.PK30DataUtils;

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
    private List<HeavyBean> mList;
    private List<QuickBean> mLoadList;

    private QuickDataBean quickDataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化扫描服务
        scanDecode = new ScanDecode(this);
        scanDecode.initService("true");
        scanDecode.getBarCode(s -> {
            mOddNumber.setText(s);
            if (!"".equals(s)) {
                quickDataBean = DaoOptions.queryQuickDataBean(s);
                mLoadList = DaoOptions.queryQuickBean(s);

                mPriceLoad.setText(quickDataBean.getMQuotedPrice());
                mReturnLoad.setText(quickDataBean.getMQuickReturn());
                mGoodsTypeLoad.setText(quickDataBean.getMTypeOfGoods());
                mPackingTypeLoad.setText(quickDataBean.getMPackingType());

                mLoadAdapter.replaceData(mLoadList);
            }
        });
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
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
        RecyclerView mRecyclerView = findViewById(R.id.rv_content);
        mLoadList = new ArrayList<>();
        mLoadAdapter = new HeavyLoadAdapter(mLoadList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mLoadAdapter);

        //初始化可编辑的显示数据
        RecyclerView recyclerView = findViewById(R.id.heavy_rv_content);
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
                HeavyBean heavyBean = new HeavyBean();
                mList.add(heavyBean);
                mAdapter.replaceData(mList);
                break;
            case R.id.heavy_clear:
                //清除，list清除
                mList.clear();
                mAdapter.replaceData(mList);
                break;
            case R.id.heavy_upload:
                //保存mList到一个列表中，保存其他位置数据到一个表中。

                //下一步，先检测输入内容全不全
                if (mList.size() > 0) {
                    if ("".equals(mList.get(mList.size() - 1).getActualWeight()) || "".equals(mList.get(mList.size() - 1).getBubbleWeight())
                            || "".equals(mList.get(mList.size() - 1).getCargoSize()) || "".equals(mList.get(mList.size() - 1).getQuickNumber())) {
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
                HeavyDataBean heavyDataBean = new HeavyDataBean();
                heavyDataBean.setMQuotedPrice(price);
                heavyDataBean.setMQuickReturn(mreturn);
                heavyDataBean.setMTypeOfGoods(goodType);
                heavyDataBean.setMPackingType(packingType);

                for (int i = 0; i < mList.size(); i++) {
                    mList.get(i).setMSenderOddNumber(heavyDataBean.getMSenderOddNumber());
                }

                DaoOptions.saveHeavyDataBean(heavyDataBean);
                DaoOptions.saveHeavyBeanData(mList);
                ToastUtils.showShortToastSafe("已保存");

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
            String s = mList.get(mList.size() - 1).getCargoSize();
            mList.get(mList.size() - 1).setCargoSize(string);
            doLoop();
        } else if ("W".equals(type)) {
            String string = (String) msg;
            Logcat.d("W:" + string);
            String s = mList.get(mList.size() - 1).getCargoSize();
            mList.get(mList.size() - 1).setCargoSize(s + "-" + string);
            doLoop();
        } else if ("H".equals(type)) {
            String string = (String) msg;
            Logcat.d("H:" + string);
            String s = mList.get(mList.size() - 1).getCargoSize();
            mList.get(mList.size() - 1).setCargoSize(s + "-" + string);
            doLoop();
        } else if ("G".equals(type)) {
            String string = (String) msg;
            Logcat.d("G:" + string);
            String s = mList.get(mList.size() - 1).getActualWeight();
            mList.get(mList.size() - 1).setActualWeight(string);
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
        } else if ((Integer) SpUtils.get(MyApp.getInstance(), SettingsModel.MODEL, 1) == 1) {
            //重量长宽高
            queue.offer(3);
            queue.offer(0);
            queue.offer(1);
            queue.offer(2);
        } else if ((Integer) SpUtils.get(MyApp.getInstance(), SettingsModel.MODEL, 2) == 2) {
            //长
            queue.offer(0);
        } else if ((Integer) SpUtils.get(MyApp.getInstance(), SettingsModel.MODEL, 3) == 3) {
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
