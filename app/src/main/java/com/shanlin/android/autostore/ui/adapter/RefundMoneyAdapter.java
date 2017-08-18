package com.shanlin.android.autostore.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.shanlin.android.autostore.common.base.BaseRecycerViewAdapter;
import com.shanlin.android.autostore.entity.respone.RefundMoneyBean;
import com.shanlin.autostore.R;
import com.shanlin.android.autostore.common.utils.DateUtils;

import java.util.Date;

/**
 * Created by cuieney on 17/08/2017.
 */

public class RefundMoneyAdapter extends BaseRecycerViewAdapter<RefundMoneyBean.DataBean,BaseRecycerViewAdapter.BaseHolder>{


    public RefundMoneyAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseHolder getCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseHolder(getItemView(R.layout.layout_item_refund,parent));
    }

    @Override
    public void getBindViewHolder(BaseHolder holder, int position) {
        RefundMoneyBean.DataBean bean = list.get(position);
        long timeLong = Long.parseLong(bean.getCreatedTime());
        String time = DateUtils.getDateStringWithFormate(new Date(timeLong), null);
        holder.setText(R.id.tv_banlance,"余额：¥" + bean.getBalance())
                .setText(R.id.tv_money,"+" + bean.getAmount())
                .setText(R.id.tv_time,time);
//        image load like this
//        ImageLoader.getInstance().displayImage(context,"url", ((ImageView) holder.getView(R.id.img)));

    }
}
