package com.shanlin.autostore.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shanlin.autostore.R;
import com.shanlin.autostore.adapter.FinalRecycleAdapter;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.bean.resultBean.RecordBean;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.android.autostore.common.utils.ThreadUtils;
import com.shanlin.autostore.view.PulltoRefreshRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账户余额 (弃用)
 * Created by DELL on 2017/7/17 0017.
 */

public class BalanceActivity extends BaseActivity implements FinalRecycleAdapter.OnViewAttachListener {


    private PulltoRefreshRecyclerView mPulltoRefreshRecyclerView;
    private RecyclerView              mRecyclerView;
    private List<Object> mDatas = new ArrayList<>();
    private FinalRecycleAdapter mFinalRecycleAdapter;
    private static int REFRESH       = 0;
    private static int LOAD          = 1;
    private        int currentAction = 0;//记录当前用户手势是下拉刷新还是上拉更多，默认下拉刷新
    private        int pageno        = 1;

    @Override
    public int initLayout() {
        return R.layout.activity_yu_e;
    }


    @Override
    public void initView() {
        CommonUtils.initToolbar(this, "账户余额", R.color.black, null);
        mPulltoRefreshRecyclerView = (PulltoRefreshRecyclerView) findViewById(R.id.pr_lists);
        mRecyclerView = mPulltoRefreshRecyclerView.getRecyclerView();
        findViewById(R.id.tv_explain).setOnClickListener(this);

        Map<Class, Integer> map = new HashMap<>();
        map.put(RecordBean.class, R.layout.layout_item_banlance);
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
                    //                    if (SLJRApplication.isLogin) {
                    //                        getShoppingList();
                    //                    } else {
                    //                        toLoginActivity();
                    //                    }
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
            case R.id.tv_explain:
                startActivity(new Intent(this, RefundExplainActivity.class));
                break;
        }
    }

    @Override
    public void onBindViewHolder(FinalRecycleAdapter.ViewHolder holder, int position, Object itemData) {

    }
}
