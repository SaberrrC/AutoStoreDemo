package com.shanlin.autostore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shanlin.autostore.R;

import java.util.List;

/**
 * Created by DELL on 2017/7/17 0017.
 */

public class BuyRecordAdapter<T> extends BaseAdapter {

    List<T> list;

    public BuyRecordAdapter(List<T> datas) {
        this.list = datas;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHold hold;
        if (convertView == null) {
             convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_record_lv_item,
                     null);
             hold = new MyViewHold(convertView);
             convertView.setTag(hold);
        } else {
            hold = (MyViewHold) convertView.getTag();
        }

        // TODO: 2017/7/17 0017 dai
//        hold.title.setText(list.get(position));

        return null;
    }

    static class MyViewHold {

        private final TextView title;
        private final TextView date;
        private final TextView money;
        private final TextView way;

        public MyViewHold(View convertView) {
            title = ((TextView) convertView.findViewById(R.id.lv_item_title));
            date = ((TextView) convertView.findViewById(R.id.lv_item_date));
            money = ((TextView) convertView.findViewById(R.id.lv_item_money));
            way = ((TextView) convertView.findViewById(R.id.lv_item_pay_way));
        }
    }
}
