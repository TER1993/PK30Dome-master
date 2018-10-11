package com.speedata.pk30dome.quick.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.speedata.pk30dome.R;
import com.speedata.pk30dome.database.QuickBean;

import java.util.List;

/**
 * @author xuyan
 */
public class QuickAdapter extends BaseQuickAdapter<QuickBean, BaseViewHolder> {

    public QuickAdapter(@Nullable List<QuickBean> data) {
        super(R.layout.view_quick_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, QuickBean item) {
        helper.setText(R.id.quick_one, item.getActualWeight());
        helper.setText(R.id.quick_two, item.getBubbleWeight());
        helper.setText(R.id.quick_three, item.getCargoSize());
        helper.setText(R.id.quick_four, item.getQuickNumber());

        helper.addOnClickListener(R.id.quick_one).addOnClickListener(R.id.quick_two)
                .addOnClickListener(R.id.quick_three).addOnClickListener(R.id.quick_four);
    }
}
