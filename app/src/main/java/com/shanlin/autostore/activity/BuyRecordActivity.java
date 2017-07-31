package com.shanlin.autostore.activity;


import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.shanlin.autostore.AutoStoreApplication;
import com.shanlin.autostore.R;
import com.shanlin.autostore.adapter.FinalRecycleAdapter;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.bean.resultBean.OrderHistoryBean;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.interf.HttpService;
import com.shanlin.autostore.net.CustomCallBack;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.DateUtils;
import com.shanlin.autostore.utils.LogUtils;
import com.shanlin.autostore.utils.SpUtils;
import com.shanlin.autostore.utils.ThreadUtils;
import com.shanlin.autostore.utils.ToastUtils;
import com.shanlin.autostore.view.PulltoRefreshRecyclerView;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by DELL on 2017/7/17 0017.
 */

public class BuyRecordActivity extends BaseActivity implements FinalRecycleAdapter.OnViewAttachListener {

    public static final int PAGE_SIZE = 20;

    private Toolbar                   toolbar;
    private TextView                  title;
    private PulltoRefreshRecyclerView mPulltoRefreshRecyclerView;
    private RecyclerView              mRecyclerView;
    private List<Object> mDatas = new ArrayList<>();
    private FinalRecycleAdapter mFinalRecycleAdapter;
    private static int REFRESH       = 0;
    private static int LOAD          = 1;
    private        int currentAction = 0;//记录当前用户手势是下拉刷新还是上拉更多，默认下拉刷新
    private        int pageno        = 1;
    private        int currentPage   = 0;
    private        int totalPage     = 0;
    private AutoRelativeLayout mFlNoList;


    @Override
    public int initLayout() {
        return R.layout.activity_buyrecord_layout;
    }

    @Override
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = ((TextView) findViewById(R.id.toolbar_title));
        mFlNoList = (AutoRelativeLayout) findViewById(R.id.fl_nolist);
        title.setText("购买记录");
        title.setTextColor(Color.BLACK);
        toolbar.setNavigationIcon(R.mipmap.nav_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mPulltoRefreshRecyclerView = (PulltoRefreshRecyclerView) findViewById(R.id.pr_lists);
        mRecyclerView = mPulltoRefreshRecyclerView.getRecyclerView();
        Map<Class, Integer> map = new HashMap<>();
        //OrderHistoryBean.DataBean.ListBean
        map.put(OrderHistoryBean.DataBean.ListBean.class, R.layout.buy_record_lv_item);
        getOrderData(pageno, PAGE_SIZE);
        mFinalRecycleAdapter = new FinalRecycleAdapter(mDatas, map, this);
        mRecyclerView.setAdapter(mFinalRecycleAdapter);
        mPulltoRefreshRecyclerView.setRefreshLoadMoreListener(MyRefreshLoadMoreListener);
    }

    /**
     * 下拉和上拉的监听函數
     */
    private PulltoRefreshRecyclerView.RefreshLoadMoreListener MyRefreshLoadMoreListener = new PulltoRefreshRecyclerView.RefreshLoadMoreListener() {
        @Override
        public void onRefresh() {
            //            // TODO: 2017-5-31
            currentAction = REFRESH;
            pageno = 1;
            ThreadUtils.runMainDelayed(new Runnable() {
                @Override
                public void run() {
                    getOrderData(pageno, PAGE_SIZE);
                    mPulltoRefreshRecyclerView.stopRefresh();
                }
            }, 500);
        }

        @Override
        public void onLoadMore() {
            //获取数据
            ThreadUtils.runMainDelayed(new Runnable() {
                @Override
                public void run() {
                    currentAction = LOAD;
                    if (currentPage < totalPage) {
                        pageno++;
                        getOrderData(pageno, PAGE_SIZE);
                    } else {
                        ToastUtils.showToast("没有更多数据！");
                    }
                    //拿到数据了
                    mPulltoRefreshRecyclerView.setLoadMoreCompleted();
                }
            }, 500);
        }
    };

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onBindViewHolder(FinalRecycleAdapter.ViewHolder holder, int position, Object itemData) {
        if (itemData instanceof OrderHistoryBean.DataBean.ListBean) {
            final OrderHistoryBean.DataBean.ListBean bean = (OrderHistoryBean.DataBean.ListBean) itemData;
            holder.getRootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(BuyRecordActivity.this, OrderDetailActivity.class);
                    intent.putExtra(Constant.ORDER_ITEM, bean);
                    startActivity(intent);
                }
            });
            TextView tvItemTitle = (TextView) holder.getViewById(R.id.tv_item_title);//邻家无人值守便利店
            TextView tvItemDate = (TextView) holder.getViewById(R.id.tv_item_date);//日期
            TextView tvItemMoney = (TextView) holder.getViewById(R.id.tv_item_money);//500.00
            TextView tvItemPayWay = (TextView) holder.getViewById(R.id.tv_item_pay_way);//微信支付
            tvItemTitle.setText("领家智能Go");
            long timeLong = bean.getCreateTime();
            String time = DateUtils.getDateStringWithFormate(new Date(timeLong), null);
            tvItemDate.setText(time);
            tvItemMoney.setText(bean.getPayAmount() + "");
            tvItemPayWay.setText(bean.getPaymentType());
        }

    }

    public void getOrderData(int pageNumber, int pageSize) {
        HttpService httpService = CommonUtils.doNet();
        Call<OrderHistoryBean> call = httpService.getOrderHistory(SpUtils.getString(this, Constant.TOKEN, ""), pageNumber, pageSize);
        call.enqueue(new CustomCallBack<OrderHistoryBean>() {
            @Override
            public void success(String code, OrderHistoryBean data, String msg) {
                List<OrderHistoryBean.DataBean.ListBean> list = data.getData().getList();
                totalPage = data.getData().getPages();//总页数
                currentPage = data.getData().getPageNum();//当前页码
                if (currentAction == REFRESH) {
                    mDatas.clear();
                }
                if (list != null && list.size() > 0) {
                    mDatas.addAll(list);
                }
                if (mDatas.size() < 1) {
                    mFlNoList.setVisibility(View.VISIBLE);
                    mFinalRecycleAdapter.notifyDataSetChanged();
                    mPulltoRefreshRecyclerView.setPullLoadMoreEnable(false);
                    return;
                }
                mPulltoRefreshRecyclerView.setPullLoadMoreEnable(true);
                mFlNoList.setVisibility(View.GONE);
                mFinalRecycleAdapter.notifyDataSetChanged();
                LogUtils.d("size  " + list.size() + "  " + mDatas.size());
            }

            @Override
            public void error(Throwable ex, String code, String msg) {
                ToastUtils.showToast(msg);
                if (TextUtils.equals(code, "401")) {//token未认证
                    AutoStoreApplication.isLogin = false;
                    toLoginActivity();
                }
            }
        });

    }

    private void toLoginActivity() {
        mDatas.clear();
        mFinalRecycleAdapter.notifyDataSetChanged();
        mFlNoList.setVisibility(View.VISIBLE);
        Intent toLoginActivity = new Intent(this, LoginActivity.class);
        startActivity(toLoginActivity);
        finish();
    }
}
