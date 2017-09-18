package com.shanlin.autostore.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.shanlin.autostore.AutoStoreApplication;
import com.shanlin.autostore.R;
import com.shanlin.autostore.adapter.FinalRecycleAdapter;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.bean.orderdetail.OrderDetailBodyHeadBean;
import com.shanlin.autostore.bean.orderdetail.OrderDetailFootBean;
import com.shanlin.autostore.bean.resultBean.OrderDetailBean;
import com.shanlin.autostore.bean.resultBean.OrderHistoryBean;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.net.CustomCallBack;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.DateUtils;
import com.shanlin.autostore.utils.SpUtils;
import com.shanlin.autostore.utils.ThreadUtils;
import com.shanlin.autostore.utils.ToastUtils;
import com.shanlin.autostore.view.PulltoRefreshRecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OrderDetailActivity extends BaseActivity implements FinalRecycleAdapter.OnViewAttachListener {
    private PulltoRefreshRecyclerView mPulltoRefreshRecyclerView;
    private RecyclerView              mRecyclerView;
    private List<Object> mDatas = new ArrayList<>();
    private FinalRecycleAdapter mRecycleAdapter;
    private OrderDetailBodyHeadBean mBodyHeadBean = new OrderDetailBodyHeadBean();
    private OrderDetailFootBean     mFootBean     = new OrderDetailFootBean();
    private OrderHistoryBean.DataBean.ListBean mBean;
    private static int REFRESH = 0;
    public OrderDetailBean data;

    @Override
    public int initLayout() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initView() {
        CommonUtils.initToolbar(this, "购物详情", R.color.black, null);
        mPulltoRefreshRecyclerView = (PulltoRefreshRecyclerView) findViewById(R.id.recyclerview_order);
        mRecyclerView = mPulltoRefreshRecyclerView.getRecyclerView();
        mBean = (OrderHistoryBean.DataBean.ListBean) getIntent().getSerializableExtra(Constant.ORDER_ITEM);
        Map<Class, Integer> map = new HashMap<>();
        map.put(OrderHistoryBean.DataBean.ListBean.class, R.layout.item_order_detail_head);
        map.put(OrderDetailBodyHeadBean.class, R.layout.item_order_detail_body);
        map.put(OrderDetailBean.DataBean.ItemsBean.class, R.layout.item_order_detail_body_item);
        map.put(OrderDetailBean.class, R.layout.item_order_detail_foot);
        getOrderDetail();
        mRecycleAdapter = new FinalRecycleAdapter(mDatas, map, this);
        mRecyclerView.setAdapter(mRecycleAdapter);
        //禁止加载更多
        mPulltoRefreshRecyclerView.setPullLoadMoreEnable(false);
        mPulltoRefreshRecyclerView.setRefreshLoadMoreListener(MyRefreshLoadMoreListener);

    }

    /**
     * 下拉和上拉的监听函數
     */
    private PulltoRefreshRecyclerView.RefreshLoadMoreListener MyRefreshLoadMoreListener = new PulltoRefreshRecyclerView.RefreshLoadMoreListener() {
        @Override
        public void onRefresh() {
            //            // TODO: 2017-5-31
            ThreadUtils.runMainDelayed(new Runnable() {
                @Override
                public void run() {
                    getOrderDetail();
                    mPulltoRefreshRecyclerView.stopRefresh();
                }
            }, 500);
        }

        @Override
        public void onLoadMore() {
        }
    };

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onBindViewHolder(FinalRecycleAdapter.ViewHolder holder, int position, Object itemData) {
        if (itemData instanceof OrderHistoryBean.DataBean.ListBean) {
            OrderHistoryBean.DataBean.ListBean bean = (OrderHistoryBean.DataBean.ListBean) itemData;
            TextView tvName = (TextView) holder.getViewById(R.id.tv_name);//店铺名字
            TextView tvAddress = (TextView) holder.getViewById(R.id.tv_address);//店铺地址
            TextView tvTime = (TextView) holder.getViewById(R.id.tv_time);//订单创建时间
            TextView sumOfConsumption = (TextView) holder.getViewById(R.id.sum_of_consumption);//支付金额
            TextView stateOfConsumption = (TextView) holder.getViewById(R.id.state_of_consumption);//支付方式
            TextView tvOrderNum = (TextView) holder.getViewById(R.id.tv_order_num);//订单号
            tvName.setText(data.getData().getStoreName());
            tvAddress.setText(data.getData().getStoreAddress());
            long timeLong = bean.getCreateTime();
            String time = DateUtils.getDateStringWithFormate(new Date(timeLong), null);
            tvTime.setText(time);
            sumOfConsumption.setText(bean.getPayAmount() + "");
            stateOfConsumption.setText(bean.getPaymentType());
            tvOrderNum.setText("订单号：" + bean.getOrderNo());
        }
        if (itemData instanceof OrderDetailBodyHeadBean) {
        }
        if (itemData instanceof OrderDetailBean.DataBean.ItemsBean) {
            OrderDetailBean.DataBean.ItemsBean bean = (OrderDetailBean.DataBean.ItemsBean) itemData;
            TextView tvOrderName = (TextView) holder.getViewById(R.id.tv_order_name);//名字
            TextView tvOrderPrice = (TextView) holder.getViewById(R.id.tv_order_price);//单价
            TextView tvOrderNum = (TextView) holder.getViewById(R.id.tv_order_num);//数量
            TextView tvOrderAll = (TextView) holder.getViewById(R.id.tv_order_all);//总价
            tvOrderName.setText(bean.getName());
            tvOrderPrice.setText("¥" + bean.getPrice());
            tvOrderNum.setText(bean.getQuantity() + "");
            tvOrderAll.setText("¥" + bean.getAmount());
        }
        if (itemData instanceof OrderDetailBean) {
            OrderDetailBean bean = (OrderDetailBean) itemData;
            TextView tvAllPrice = (TextView) holder.getViewById(R.id.tv_all_price);
            TextView tvAllGet = (TextView) holder.getViewById(R.id.tv_all_get);
            tvAllPrice.setText("合计：¥" + bean.getData().getTotalAmount());
            tvAllGet.setText("¥" + bean.getData().getPayAmount());
        }
    }

    public void getOrderDetail() {
        CommonUtils.doNet().getOrderDetail(SpUtils.getString(this, Constant.TOKEN, ""), mBean.getOrderNo()).enqueue(new CustomCallBack<OrderDetailBean>() {
            @Override
            public void success(String code, OrderDetailBean data, String msg) {
                List<OrderDetailBean.DataBean.ItemsBean> items = data.getData().getItems();
                mDatas.clear();
                OrderDetailActivity.this.data = data;
                if (items != null && items.size() > 0) {
                    mDatas.add(mBean);
                    mDatas.add(mBodyHeadBean);
                    mDatas.addAll(items);
                    mDatas.add(data);
                } else {
                    mDatas.add(mBean);
                    mDatas.add(mBodyHeadBean);
                    mDatas.add(data);
                    ToastUtils.showToast("无订单列表");
                }
                mRecycleAdapter.notifyDataSetChanged();
            }

            @Override
            public void error(Throwable ex, String code, String msg) {
                if (TextUtils.equals(code, "401")) {//token未认证
                    if (AutoStoreApplication.isLogin) {
                        AutoStoreApplication.isLogin = false;
                        toLoginActivity();
                    }
                }
                ToastUtils.showToast(msg);
            }
        });
    }

    private void toLoginActivity() {
        mDatas.clear();
        mDatas.add(mBean);
        mDatas.add(mBodyHeadBean);
        mDatas.add(data);
        mRecycleAdapter.notifyDataSetChanged();
        Intent toLoginActivity = new Intent(this, LoginActivity.class);
        startActivity(toLoginActivity);
        finish();
    }
}
