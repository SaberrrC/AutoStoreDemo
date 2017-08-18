package com.shanlin.android.autostore.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shanlin.android.autostore.common.base.BaseRecycerViewAdapter;
import com.shanlin.autostore.R;
import com.shanlin.android.autostore.entity.respone.OrderHistoryBean;
import com.shanlin.android.autostore.common.utils.DateUtils;

import java.util.Date;

/**
 * Created by dell、 on 2017/8/17.
 */

public class BuyRecordAdapter extends BaseRecycerViewAdapter<OrderHistoryBean.DataBean.ListBean,BaseRecycerViewAdapter.BaseHolder>{

    public BuyRecordAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseHolder getCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseHolder(getItemView(R.layout.buy_record_lv_item,parent));
    }

    @Override
    public void getBindViewHolder(BaseHolder holder, int position) {
        OrderHistoryBean.DataBean.ListBean bean = list.get(position);
        TextView tvItemTitle = (TextView) holder.getView(R.id.tv_item_title);//邻家无人值守便利店
        TextView tvItemDate = (TextView) holder.getView(R.id.tv_item_date);//日期
        TextView tvItemMoney = (TextView) holder.getView(R.id.tv_item_money);//500.00
        TextView tvItemPayWay = (TextView) holder.getView(R.id.tv_item_pay_way);//微信支付
        tvItemTitle.setText("领家智能Go");
        long timeLong = bean.getCreateTime();
        String time = DateUtils.getDateStringWithFormate(new Date(timeLong), null);
        tvItemDate.setText(time);
        tvItemMoney.setText(bean.getPayAmount() + "");
        tvItemPayWay.setText(bean.getPaymentType());
    }
}
