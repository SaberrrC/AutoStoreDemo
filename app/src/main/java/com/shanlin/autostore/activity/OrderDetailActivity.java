package com.shanlin.autostore.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shanlin.autostore.R;
import com.shanlin.autostore.adapter.FinalRecycleAdapter;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.bean.orderdetail.OrderDetailBodyHeadBean;
import com.shanlin.autostore.bean.orderdetail.OrderDetailFootBean;
import com.shanlin.autostore.bean.orderdetail.OrderDetailHeadBean;
import com.shanlin.autostore.bean.orderdetail.OrderDetailitemBean;
import com.shanlin.autostore.utils.ThreadUtils;
import com.shanlin.autostore.view.PulltoRefreshRecyclerView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailActivity extends BaseActivity implements FinalRecycleAdapter.OnViewAttachListener {
    private PulltoRefreshRecyclerView mPulltoRefreshRecyclerView;
    private RecyclerView              mRecyclerView;
    private List<Object> mDatas = new ArrayList<>();
    private FinalRecycleAdapter mRecycleAdapter;

    @Override
    public int initLayout() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initView() {
        mPulltoRefreshRecyclerView = (PulltoRefreshRecyclerView) findViewById(R.id.recyclerview_order);
        mRecyclerView = mPulltoRefreshRecyclerView.getRecyclerView();
        mDatas.add(new OrderDetailHeadBean());
        mDatas.add(new OrderDetailBodyHeadBean());
        for (int i = 0; i < 10; i++) {
            mDatas.add(new OrderDetailitemBean());
        }
        mDatas.add(new OrderDetailFootBean());
        Map<Class, Integer> map = new HashMap<>();
        map.put(OrderDetailHeadBean.class, R.layout.item_order_detail_head);
        map.put(OrderDetailBodyHeadBean.class, R.layout.item_order_detail_body);
        map.put(OrderDetailitemBean.class, R.layout.item_order_detail_body_item);
        map.put(OrderDetailFootBean.class, R.layout.item_order_detail_foot);
        mRecycleAdapter = new FinalRecycleAdapter(mDatas, map, this);
        mRecyclerView.setAdapter(mRecycleAdapter);
        //禁止加载更多
        mPulltoRefreshRecyclerView.setPullLoadMoreEnable(false);
        mPulltoRefreshRecyclerView.setRefreshLoadMoreListener(new PulltoRefreshRecyclerView.RefreshLoadMoreListener() {
            @Override
            public void onRefresh() {
                ThreadUtils.runMainDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPulltoRefreshRecyclerView.stopRefresh();
                    }
                }, 500);

            }

            @Override
            public void onLoadMore() {

            }
        });

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onBindViewHolder(FinalRecycleAdapter.ViewHolder holder, int position, Object itemData) {
        AutoUtils.autoSize(holder.getRootView());
    }
}
