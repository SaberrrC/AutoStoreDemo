package com.shanlin.android.autostore.ui.act;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanlin.android.autostore.common.base.SimpleActivity;
import com.shanlin.android.autostore.common.utils.CommonUtils;
import com.shanlin.android.autostore.common.utils.ThreadUtils;
import com.shanlin.android.autostore.ui.adapter.MyLMBAdapter;
import com.shanlin.autostore.R;
import com.shanlin.autostore.bean.resultBean.RecordBean;
import com.shanlin.autostore.constants.Constant_LeMaiBao;
import com.shanlin.autostore.view.PulltoRefreshRecyclerView;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell、 on 2017/8/17.
 */

public class MyLMBActivity extends SimpleActivity {

    @BindView(R.id.tv_money)
    TextView mTvMoney;
    @BindView(R.id.tv_credit_used)
    TextView mTvUsed;
    @BindView(R.id.tv_behalf_repayment)
    TextView mTvBehalfRepayment;
    @BindView(R.id.tv_repaid)
    TextView mTvRepaid;
    @BindView(R.id.iv_nolist)
    ImageView mIvNolist;
    @BindView(R.id.tv_nolist)
    TextView mTvNolist;
    @BindView(R.id.fl_nolist)
    AutoRelativeLayout mFlNolist;
    @BindView(R.id.pr_lists)
    PulltoRefreshRecyclerView mPulltoRefreshRecyclerView;
    private RecyclerView mRecyclerView;
    private List<Object> mDatas = new ArrayList<>();
    private static final int REFRESH = 0;
    private static final int LOAD = 1;
    private int currentAction = 0;//记录当前用户手势是下拉刷新还是上拉更多，默认下拉刷新
    private int pageno = 1;
    private static final int CHOOSE_STATE_BEHALF_REPAYMENT = 0;//待还款
    private static final int CHOOSE_STATE_REPAID = 1;//已还款
    private int currentChooseState = 0;

    @Override
    public int initLayout() {
        return R.layout.activity_my_lei_mai_bao;
    }

    @Override
    public void initData() {
        CommonUtils.initToolbar(this, "我的乐买宝", R.color.blcak, null);
        Intent intent = getIntent();
        String creditBalence = intent.getStringExtra(Constant_LeMaiBao.CREDIT_BALANCE);
        String creditUsed = intent.getStringExtra(Constant_LeMaiBao.CREDIT_USED);
        mTvMoney.setText("¥ "+(creditBalence == null ? "0.00" : creditBalence));
        mTvUsed.setText("¥ "+(creditUsed == null ? "0.00" : creditUsed));
        mRecyclerView = mPulltoRefreshRecyclerView.getRecyclerView();

        MyLMBAdapter mAdapter = new MyLMBAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);
        mPulltoRefreshRecyclerView.setRefreshLoadMoreListener(MyRefreshLoadMoreListener);
        initCheck();
    }

    private void initCheck() {
        mTvBehalfRepayment.setTextColor(Color.parseColor("#FFFCBF0D"));
        mTvRepaid.setTextColor(Color.parseColor("#999999"));
        mFlNolist.setVisibility(View.VISIBLE);
        mIvNolist.setImageResource(R.mipmap.image_yhq);
        mTvNolist.setText("账单都已还清");
    }

    @OnClick({R.id.tv_behalf_repayment,R.id.tv_repaid})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_behalf_repayment:
                if (currentChooseState == CHOOSE_STATE_BEHALF_REPAYMENT) {
                    return;
                }
                currentChooseState = CHOOSE_STATE_BEHALF_REPAYMENT;
                mTvBehalfRepayment.setTextColor(Color.parseColor("#FFFCBF0D"));
                mTvRepaid.setTextColor(Color.parseColor("#999999"));
                mFlNolist.setVisibility(View.VISIBLE);
                mIvNolist.setImageResource(R.mipmap.image_yhq);
                mTvNolist.setText("账单都已还清");
                break;
            case R.id.tv_repaid:
                if (currentChooseState == CHOOSE_STATE_REPAID) {
                    return;
                }
                currentChooseState = CHOOSE_STATE_REPAID;
                mTvBehalfRepayment.setTextColor(Color.parseColor("#999999"));
                mTvRepaid.setTextColor(Color.parseColor("#FFFCBF0D"));
                mFlNolist.setVisibility(View.VISIBLE);
                mIvNolist.setImageResource(R.mipmap.image_wzd);
                mTvNolist.setText("没有已还清账单");
                break;
        }
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
                    mPulltoRefreshRecyclerView.setLoadMoreCompleted();
                }
            }, 500);
        }
    };
}
