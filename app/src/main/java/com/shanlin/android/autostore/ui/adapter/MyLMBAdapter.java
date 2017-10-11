package com.shanlin.android.autostore.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.shanlin.android.autostore.common.base.BaseRecycerViewAdapter;
import com.shanlin.android.autostore.entity.respone.RecordBean;
import com.shanlin.android.autostore.entity.respone.RefundMoneyBean;
import com.shanlin.autostore.R;

/**
 * Created by dell、 on 2017/8/17.
 */

public class MyLMBAdapter extends BaseRecycerViewAdapter<RecordBean,BaseRecycerViewAdapter.BaseHolder> {

    public MyLMBAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseHolder getCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseHolder(getItemView(R.layout.layout_item_lemaibao_list,parent));
    }

    @Override
    public void getBindViewHolder(BaseHolder holder, int position) {
//        RecordBean bean = list.get(position);
        holder.setText(R.id.tv_mouth,"")
                .setText(R.id.tv_money,"")
                .setText(R.id.tv_time,"")
                .setText(R.id.stork_thin,"")
                .setText(R.id.stork_fat,"");
    }

}
