package com.shanlin.android.autostore.ui.act;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.shanlin.android.autostore.common.base.BaseActivity;
import com.shanlin.android.autostore.common.utils.SpUtils;
import com.shanlin.android.autostore.common.utils.ThreadUtils;
import com.shanlin.android.autostore.common.utils.ToastUtils;
import com.shanlin.android.autostore.presenter.Contract.OrderDetailActContract;
import com.shanlin.android.autostore.presenter.OrderDetailPresenter;
import com.shanlin.autostore.R;
import com.shanlin.autostore.adapter.FinalRecycleAdapter;
import com.shanlin.autostore.bean.orderdetail.OrderDetailBodyHeadBean;
import com.shanlin.autostore.bean.resultBean.OrderDetailBean;
import com.shanlin.autostore.bean.resultBean.OrderHistoryBean;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.DateUtils;
import com.shanlin.autostore.view.PulltoRefreshRecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;


public class OrderDetailActivity extends BaseActivity<OrderDetailPresenter> implements FinalRecycleAdapter.OnViewAttachListener,OrderDetailActContract.View {

    @BindView(R.id.recyclerview_order)
    PulltoRefreshRecyclerView mPulltoRefreshRecyclerView;
    private RecyclerView                       mRecyclerView;
    private FinalRecycleAdapter                mRecycleAdapter;
    private OrderHistoryBean.DataBean.ListBean mBean;
    private OrderDetailBean                    data;
    private List<Object>            mDatas        = new ArrayList<>();
    private OrderDetailBodyHeadBean mBodyHeadBean = new OrderDetailBodyHeadBean();
    private String mToken;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_order_detail;
    }

    /**
     * 下拉和上拉的监听函數
     */
    private PulltoRefreshRecyclerView.RefreshLoadMoreListener MyRefreshLoadMoreListener = new PulltoRefreshRecyclerView.RefreshLoadMoreListener() {
        @Override
        public void onRefresh() {
            ThreadUtils.runMainDelayed(new Runnable() {
                @Override
                public void run() {
                    mPresenter.getOrderDetail(mBean);
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
        CommonUtils.initToolbar(this, "购物详情", R.color.black, null);
        mRecyclerView = mPulltoRefreshRecyclerView.getRecyclerView();
        mBean = (OrderHistoryBean.DataBean.ListBean) getIntent().getSerializableExtra(Constant.ORDER_ITEM);
        Map<Class, Integer> map = new HashMap<>();
        map.put(OrderHistoryBean.DataBean.ListBean.class, R.layout.item_order_detail_head);
        map.put(OrderDetailBodyHeadBean.class, R.layout.item_order_detail_body);
        map.put(OrderDetailBean.DataBean.ItemsBean.class, R.layout.item_order_detail_body_item);
        map.put(OrderDetailBean.class, R.layout.item_order_detail_foot);
        mToken = SpUtils.getString(this, Constant.TOKEN, "");
        mPresenter.getOrderDetail(mBean);
        mRecycleAdapter = new FinalRecycleAdapter(mDatas, map, this);
        mRecyclerView.setAdapter(mRecycleAdapter);
        //禁止加载更多
        mPulltoRefreshRecyclerView.setPullLoadMoreEnable(false);
        mPulltoRefreshRecyclerView.setRefreshLoadMoreListener(MyRefreshLoadMoreListener);
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

    @Override
    public void ongetOrderDetailSuccess(String code, OrderDetailBean data, String msg) {
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
    public void ongetOrderDetailFailed(Throwable ex, String code, String msg) {
        if (TextUtils.equals(code, "401")) {//token未认证
            ToastUtils.showToast("用户已在别处登录，请重新登录");
            toLoginActivity();
            return;
        }
        ToastUtils.showToast(msg);
    }

    private void toLoginActivity() {
        mDatas.clear();
        mDatas.add(mBean);
        mDatas.add(mBodyHeadBean);
        mDatas.add(data);
        mRecycleAdapter.notifyDataSetChanged();
        Intent toLoginActivity = new Intent(this, com.shanlin.autostore.activity.LoginActivity.class);
        startActivity(toLoginActivity);
        finish();
    }
}