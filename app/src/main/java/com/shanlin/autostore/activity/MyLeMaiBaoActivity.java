package com.shanlin.autostore.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.adapter.FinalRecycleAdapter;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.view.PulltoRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2017/7/17 0017.
 */

public class MyLeMaiBaoActivity extends BaseActivity implements FinalRecycleAdapter.OnViewAttachListener {
    private     TextView                  mTvAvailableAmount;
    private     TextView                  mTvMoney;
    private     TextView                  mTvUsed;
    private     TextView                  mTvBehalfRepayment;
    private     TextView                  mTvRepaid;
    private     PulltoRefreshRecyclerView mPulltoRefreshRecyclerView;
    private RecyclerView              mRecyclerView;
    private List<Object> mDatas = new ArrayList<>();
    private FinalRecycleAdapter mFinalRecycleAdapter;
    private static int REFRESH       = 0;
    private static int LOAD          = 1;
    private        int currentAction = 0;//记录当前用户手势是下拉刷新还是上拉更多，默认下拉刷新
    private        int pageno        = 1;

    @Override
    public int initLayout() {
        return R.layout.activity_my_lei_mai_bao;
    }

    @Override
    public void initView() {
        CommonUtils.initToolbar(this,"我的乐买宝", R.color.blcak,MainActivity.class);
        mTvAvailableAmount = (TextView) findViewById(R.id.tv_available_amount);
        mTvMoney = (TextView) findViewById(R.id.tv_money);
        mTvUsed = (TextView) findViewById(R.id.tv_used);
        mTvBehalfRepayment = (TextView) findViewById(R.id.tv_behalf_repayment);
        mTvRepaid = (TextView) findViewById(R.id.tv_repaid);
//        mPulltoRefreshRecyclerView = (PulltoRefreshRecyclerView) findViewById(R.id.pr_lists);
//        mRecyclerView = mPulltoRefreshRecyclerView.getRecyclerView();
//        Map<Class, Integer> map = new HashMap<>();
//        map.put(RecordBean.class, R.layout.buy_record_lv_item);
//        for (int i = 0; i < 10; i++) {
//            mDatas.add(new RecordBean(K
        // ));
//        }
//        mFinalRecycleAdapter = new FinalRecycleAdapter(mDatas, map, this);
//        mRecyclerView.setAdapter(mFinalRecycleAdapter);
//        mPulltoRefreshRecyclerView.setRefreshLoadMoreListener(MyRefreshLoadMoreListener);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBindViewHolder(FinalRecycleAdapter.ViewHolder holder, int position, Object itemData) {

    }
}
