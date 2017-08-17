package com.shanlin.android.autostore.ui.act;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.shanlin.android.autostore.common.base.BaseActivity;
import com.shanlin.android.autostore.common.constants.Constant;
import com.shanlin.android.autostore.common.utils.CommonUtils;
import com.shanlin.android.autostore.common.utils.ThreadUtils;
import com.shanlin.android.autostore.common.utils.ToastUtils;
import com.shanlin.android.autostore.entity.respone.RefundMoneyBean;
import com.shanlin.android.autostore.presenter.Contract.RefundMoneyActContract;
import com.shanlin.android.autostore.presenter.RefundMoneyPresenter;
import com.shanlin.android.autostore.ui.adapter.RefundMoneyAdapter;
import com.shanlin.autostore.R;
import com.shanlin.autostore.view.PulltoRefreshRecyclerView;
import com.zhy.autolayout.AutoRelativeLayout;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell、 on 2017/8/16.
 */

public class RefundMoneyActivity extends BaseActivity<RefundMoneyPresenter> implements RefundMoneyActContract.View {

    @BindView(R.id.tv_money)
    TextView                  mTvMoney;
    @BindView(R.id.rl_wtk)
    AutoRelativeLayout        mRlWtk;
    @BindView(R.id.pr_lists)
    PulltoRefreshRecyclerView mPulltoRefreshRecyclerView;
    private RecyclerView mRecyclerView;
    private static int           REFRESH       = 0;
    private static int           LOAD          = 1;
    private        int           currentAction = 0;//记录当前用户手势是下拉刷新还是上拉更多，默认下拉刷新
    private        int           pageno        = 1;
    private        DecimalFormat df            = new java.text.DecimalFormat("#.00");
    private RefundMoneyAdapter refundMoneyAdapter;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_refund_money;
    }

    @Override
    public void initData() {
        CommonUtils.initToolbar(this, "退款金额", R.color.black, null);
        mRecyclerView = mPulltoRefreshRecyclerView.getRecyclerView();
        RefundMoneyBean refundMoneyBean = (RefundMoneyBean) getIntent().getSerializableExtra(Constant.REFUND_MONEY_BEAN);
        refundMoneyAdapter = new RefundMoneyAdapter(mContext);
        mRecyclerView.setAdapter(refundMoneyAdapter);
        setMoneyText(refundMoneyBean);
        mPulltoRefreshRecyclerView.setRefreshLoadMoreListener(MyRefreshLoadMoreListener);
    }

    /**
     * 下拉和上拉的监听函數
     */
    private PulltoRefreshRecyclerView.RefreshLoadMoreListener MyRefreshLoadMoreListener = new PulltoRefreshRecyclerView.RefreshLoadMoreListener() {
        @Override
        public void onRefresh() {
            currentAction = REFRESH;
            pageno = 1;
            ThreadUtils.runMainDelayed(new Runnable() {
                @Override
                public void run() {
                    mPresenter.getRefoundMoneyInfo();
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

    private void setMoneyText(RefundMoneyBean refundMoneyBean) {
        List<RefundMoneyBean.DataBean> beanList = refundMoneyBean.getData();
        if (beanList == null || beanList.size() == 0) {
            mTvMoney.setText("¥0.00");
            return;
        }
        setMoneyText(beanList);
        refundMoneyAdapter.addAll(beanList);
    }

    @Override
    public void onGetInfoSuccess(String code, RefundMoneyBean data, String msg) {
        if (currentAction == REFRESH) {
            refundMoneyAdapter.clear();
        }
        List<RefundMoneyBean.DataBean> beanList = data.getData();
        if (beanList == null || beanList.size() == 0) {
            mTvMoney.setText("¥0.00");
        } else {
            setMoneyText(beanList);
            refundMoneyAdapter.addAll(beanList);
        }
        if (refundMoneyAdapter.list.size() > 0) {
            mRlWtk.setVisibility(View.GONE);
        } else {
            mRlWtk.setVisibility(View.VISIBLE);
        }
    }

    private void setMoneyText(List<RefundMoneyBean.DataBean> beanList) {
        double sum = 0.00;
        for (RefundMoneyBean.DataBean dataBean : beanList) {
            String balance = dataBean.getBalance();
            if (TextUtils.isEmpty(balance)) {
                continue;
            }
            double refundMoney = Double.parseDouble(balance);
            sum += refundMoney;
        }
        if (sum == 0.00) {
            mTvMoney.setText("¥0" + df.format(sum));
        } else {
            mTvMoney.setText("¥" + df.format(sum));
        }
    }

    @Override
    public void onGetInfoFailed(Throwable ex, String code, String msg) {
        if (refundMoneyAdapter.list.size() > 0) {
            mRlWtk.setVisibility(View.GONE);
        } else {
            mRlWtk.setVisibility(View.VISIBLE);
        }
        ToastUtils.showToast(msg);
    }

    @OnClick(R.id.tv_explain)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_explain:
                startActivity(new Intent(this, RefundExplainActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
        }
    }

}
