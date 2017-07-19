package com.shanlin.autostore.activity;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.adapter.FinalRecycleAdapter;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.bean.RecordBean;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.ThreadUtils;
import com.shanlin.autostore.view.PulltoRefreshRecyclerView;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.shanlin.autostore.R.id.tv_money;

/**
 * Created by DELL on 2017/7/17 0017.
 */

public class MyLeMaiBaoActivity extends BaseActivity implements FinalRecycleAdapter.OnViewAttachListener {
    private TextView                  mTvAvailableAmount;
    private TextView                  mTvMoney;
    private TextView                  mTvUsed;
    private TextView                  mTvBehalfRepayment;
    private TextView                  mTvRepaid;
    private PulltoRefreshRecyclerView mPulltoRefreshRecyclerView;
    private RecyclerView              mRecyclerView;
    private FinalRecycleAdapter       mFinalRecycleAdapter;
    private              List<Object> mDatas                        = new ArrayList<>();
    private static final int          REFRESH                       = 0;
    private static final int          LOAD                          = 1;
    private              int          currentAction                 = 0;//记录当前用户手势是下拉刷新还是上拉更多，默认下拉刷新
    private              int          pageno                        = 1;
    private static final int          CHOOSE_STATE_BEHALF_REPAYMENT = 0;//待还款
    private static final int          CHOOSE_STATE_REPAID           = 1;//已还款
    private              int          currentChooseState            = 0;
    private ImageView mIvNolist;
    private TextView  mTvNolist;

    @Override
    public int initLayout() {
        return R.layout.activity_my_lei_mai_bao;
    }

    @Override
    public void initView() {
        CommonUtils.initToolbar(this, "我的乐买宝", R.color.blcak, MainActivity.class);
        mTvAvailableAmount = (TextView) findViewById(R.id.tv_available_amount);
        mTvMoney = (TextView) findViewById(tv_money);
        mTvUsed = (TextView) findViewById(R.id.tv_used);
        mIvNolist = (ImageView) findViewById(R.id.iv_nolist);
        mTvNolist = (TextView) findViewById(R.id.tv_nolist);
        mTvBehalfRepayment = (TextView) findViewById(R.id.tv_behalf_repayment);
        mTvRepaid = (TextView) findViewById(R.id.tv_repaid);
        mTvBehalfRepayment.setOnClickListener(this);
        mTvRepaid.setOnClickListener(this);
        mPulltoRefreshRecyclerView = (PulltoRefreshRecyclerView) findViewById(R.id.pr_lists);
        mRecyclerView = mPulltoRefreshRecyclerView.getRecyclerView();
        Map<Class, Integer> map = new HashMap<>();
        map.put(RecordBean.class, R.layout.layout_item_lemaibao_list);
        for (int i = 0; i < 10; i++) {
            mDatas.add(new RecordBean());
        }
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
                    // if (SLJRApplication.isLogin) {
                    //     getShoppingList();
                    // } else {
                    //     toLoginActivity();
                    // }
                    mPulltoRefreshRecyclerView.stopRefresh();
                }
            }, 500);
        }

        @Override
        public void onLoadMore() {
            currentAction = LOAD;
            //            LogUtils.d(currentPage + "--------------" + "totalPage--------------------" + +totalPage);
            //            if (currentPage < totalPage) {
            //                pageno++;
            //                getShoppingList();
            //            } else {
            //                T.showShort("没有更多数据！");
            //            }
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


    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_behalf_repayment:
                if (currentChooseState == CHOOSE_STATE_BEHALF_REPAYMENT) {
                    return;
                }
                currentChooseState = CHOOSE_STATE_BEHALF_REPAYMENT;
                mTvBehalfRepayment.setTextColor(Color.parseColor("#FFFCBF0D"));
                mTvRepaid.setTextColor(Color.parseColor("#999999"));
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
                mIvNolist.setImageResource(R.mipmap.image_wzd);
                mTvNolist.setText("没有已还清账单");
                break;
        }
    }

    @Override
    public void onBindViewHolder(FinalRecycleAdapter.ViewHolder holder, int position, Object itemData) {
        if (itemData instanceof RecordBean) {
            AutoRelativeLayout rlItem = (AutoRelativeLayout) holder.getViewById(R.id.rl_item);
            TextView tvMouth = (TextView) holder.getViewById(R.id.tv_mouth);
            TextView tvMoney = (TextView) holder.getViewById(R.id.tv_money);
            TextView tvTime = (TextView) holder.getViewById(R.id.tv_time);
            View storkThin = holder.getViewById(R.id.stork_thin);
            View storkFat = holder.getViewById(R.id.stork_fat);
        }
    }
}
