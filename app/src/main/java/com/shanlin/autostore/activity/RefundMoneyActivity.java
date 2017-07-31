package com.shanlin.autostore.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.shanlin.autostore.R;
import com.shanlin.autostore.adapter.FinalRecycleAdapter;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.bean.resultBean.RefundMoneyBean;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.net.CustomCallBack;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.SpUtils;
import com.shanlin.autostore.utils.ThreadUtils;
import com.shanlin.autostore.utils.ToastUtils;
import com.shanlin.autostore.view.PulltoRefreshRecyclerView;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefundMoneyActivity extends BaseActivity implements FinalRecycleAdapter.OnViewAttachListener {
    private PulltoRefreshRecyclerView mPulltoRefreshRecyclerView;
    private RecyclerView              mRecyclerView;
    private List<Object> mDatas = new ArrayList<>();
    private FinalRecycleAdapter mFinalRecycleAdapter;
    private static int REFRESH       = 0;
    private static int LOAD          = 1;
    private        int currentAction = 0;//记录当前用户手势是下拉刷新还是上拉更多，默认下拉刷新
    private        int pageno        = 1;
    private TextView           mTvMoney;
    private AutoRelativeLayout mRlWtk;

    @Override
    public int initLayout() {
        return R.layout.activity_refund_money;
    }

    @Override
    public void initView() {
        CommonUtils.initToolbar(this, "退款金额", R.color.black, null);
        findViewById(R.id.tv_explain).setOnClickListener(this);
        mTvMoney = (TextView) findViewById(R.id.tv_money);
        mRlWtk = (AutoRelativeLayout) findViewById(R.id.rl_wtk);
        mPulltoRefreshRecyclerView = (PulltoRefreshRecyclerView) findViewById(R.id.pr_lists);
        mRecyclerView = mPulltoRefreshRecyclerView.getRecyclerView();
        Map<Class, Integer> map = new HashMap<>();
        map.put(RefundMoneyBean.DataBean.class, R.layout.layout_item_refund);
        RefundMoneyBean refundMoneyBean = (RefundMoneyBean) getIntent().getSerializableExtra(Constant.REFUND_MONEY_BEAN);
        setMoneyText(refundMoneyBean);
        mFinalRecycleAdapter = new FinalRecycleAdapter(mDatas, map, this);
        mRecyclerView.setAdapter(mFinalRecycleAdapter);
        mPulltoRefreshRecyclerView.setRefreshLoadMoreListener(MyRefreshLoadMoreListener);
        getRefundMoney();
    }

    private void setMoneyText(RefundMoneyBean refundMoneyBean) {
        List<RefundMoneyBean.DataBean> beanList = refundMoneyBean.getData();
        if (beanList == null || beanList.size() == 0) {
            mTvMoney.setText("¥0.00");
            return;
        }
        double sum = 0.00;
        for (RefundMoneyBean.DataBean dataBean : beanList) {
            String balance = dataBean.getBalance();
            if (TextUtils.isEmpty(balance)) {
                continue;
            }
            double refundMoney = Double.parseDouble(balance);
            sum += refundMoney;
        }
        mTvMoney.setText("¥" + sum);
        mDatas.addAll(beanList);
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
                    getRefundMoney();
                    mPulltoRefreshRecyclerView.stopRefresh();
                }
            }, 500);
        }

        @Override
        public void onLoadMore() {
            currentAction = LOAD;
            //获取数据
            ThreadUtils.runMainDelayed(new Runnable() {
                @Override
                public void run() {
                    //拿到数据了
                    ToastUtils.showToast("没有更多数据");
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
        switch (v.getId()) {
            case R.id.tv_explain:
                startActivity(new Intent(this, RefundExplainActivity.class));
                break;
        }
    }

    @Override
    public void onBindViewHolder(FinalRecycleAdapter.ViewHolder holder, int position, Object itemData) {
        if (itemData instanceof RefundMoneyBean.DataBean) {
            RefundMoneyBean.DataBean bean = (RefundMoneyBean.DataBean) itemData;
            TextView tvMouth = (TextView) findViewById(R.id.tv_mouth);//退款
            TextView tvBanlance = (TextView) findViewById(R.id.tv_banlance);//余额
            TextView tvMoney = (TextView) findViewById(R.id.tv_money);//+500.00元
            TextView tvTime = (TextView) findViewById(R.id.tv_time);//日期
            tvBanlance.setText(bean.getAmount());
            tvMoney.setText(bean.getAmount());
            tvTime.setText(bean.getCreatedTime());
        }
    }

    public void getRefundMoney() {
        CommonUtils.doNet().getRefundMoney(SpUtils.getString(this, Constant.TOKEN, "")).enqueue(new CustomCallBack<RefundMoneyBean>() {
            @Override
            public void success(String code, RefundMoneyBean data, String msg) {
                if (currentAction == REFRESH) {
                    mDatas.clear();
                }
                List<RefundMoneyBean.DataBean> beanList = data.getData();
                if (beanList == null || beanList.size() == 0) {
                    mTvMoney.setText("¥0.00");
                }
                double sum = 0.00;
                for (RefundMoneyBean.DataBean dataBean : beanList) {
                    String balance = dataBean.getBalance();
                    if (TextUtils.isEmpty(balance)) {
                        continue;
                    }
                    double refundMoney = Double.parseDouble(balance);
                    sum += refundMoney;
                }
                mTvMoney.setText("¥" + sum);
                mDatas.addAll(beanList);
                mFinalRecycleAdapter.notifyDataSetChanged();
                if (mDatas.size() > 0) {
                    mRlWtk.setVisibility(View.GONE);
                } else {
                    mRlWtk.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void error(Throwable ex, String code, String msg) {
                if (mDatas.size() > 0) {
                    mRlWtk.setVisibility(View.GONE);
                } else {
                    mRlWtk.setVisibility(View.VISIBLE);
                }
                ToastUtils.showToast(msg);
            }
        });
    }

    private void toLoginActivity() {
        mDatas.clear();
        mFinalRecycleAdapter.notifyDataSetChanged();
        Intent toLoginActivity = new Intent(this, LoginActivity.class);
        startActivity(toLoginActivity);
        finish();
    }

}
