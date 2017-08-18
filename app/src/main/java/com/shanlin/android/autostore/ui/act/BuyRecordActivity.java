package com.shanlin.android.autostore.ui.act;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.shanlin.android.autostore.common.base.BaseActivity;
import com.shanlin.android.autostore.common.base.BaseRecycerViewAdapter;
import com.shanlin.android.autostore.common.constants.Constant;
import com.shanlin.android.autostore.common.utils.CommonUtils;
import com.shanlin.android.autostore.common.utils.ThreadUtils;
import com.shanlin.android.autostore.common.utils.ToastUtils;
import com.shanlin.android.autostore.entity.respone.OrderHistoryBean;
import com.shanlin.android.autostore.presenter.BuyRecordPresenter;
import com.shanlin.android.autostore.presenter.Contract.BuyRecordActContract;
import com.shanlin.android.autostore.ui.adapter.BuyRecordAdapter;
import com.shanlin.android.autostore.ui.view.PulltoRefreshRecyclerView;
import com.shanlin.autostore.R;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by DELL on 2017/7/17 0017.
 */

public class BuyRecordActivity extends BaseActivity<BuyRecordPresenter> implements BuyRecordActContract.View {

    @BindView(R.id.toolbar_title)
    TextView                  title;
    @BindView(R.id.toolbar)
    Toolbar                   toolbar;
    @BindView(R.id.fl_nolist)
    AutoRelativeLayout        mFlNoList;
    @BindView(R.id.pr_lists)
    PulltoRefreshRecyclerView mPulltoRefreshRecyclerView;
    private RecyclerView mRecyclerView;
    public static final int          PAGE_SIZE     = 20;
    private static      int          REFRESH       = 0;
    private static      int          LOAD          = 1;
    private             int          currentAction = 0;//记录当前用户手势是下拉刷新还是上拉更多，默认下拉刷新
    private             int          pageno        = 1;
    private             int          currentPage   = 0;
    private             int          totalPage     = 0;
    private BuyRecordAdapter mAdapter;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_buyrecord_layout;
    }

    @Override
    public void initData() {
        CommonUtils.initToolbar(this,"购买记录",R.color.black,null);
        mPresenter.getOrderData(pageno, PAGE_SIZE);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = mPulltoRefreshRecyclerView.getRecyclerView();
        Map<Class, Integer> map = new HashMap<>();
        map.put(OrderHistoryBean.DataBean.ListBean.class, R.layout.buy_record_lv_item);
        mAdapter = new BuyRecordAdapter(this);
        mAdapter.setOnItemClickListener(new BaseRecycerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, RecyclerView.ViewHolder vh) {
                Intent intent = new Intent(BuyRecordActivity.this, OrderDetailActivity.class);
                intent.putExtra(Constant.ORDER_ITEM,  mAdapter.list.get(position));
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
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
                    mPresenter.getOrderData(pageno, PAGE_SIZE);
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
                        mPresenter.getOrderData(pageno, PAGE_SIZE);
                    } else {
                        ToastUtils.showToast("没有更多数据");
                    }
                    mPulltoRefreshRecyclerView.setLoadMoreCompleted();
                }
            }, 500);
        }
    };

    @Override
    public void onGetOrderDataSuccess(String code, OrderHistoryBean data, String msg) {
        List<OrderHistoryBean.DataBean.ListBean> list = data.getData().getList();
        totalPage = data.getData().getPages();//总页数
        currentPage = data.getData().getPageNum();//当前页码
        if (currentAction == REFRESH) {
            mAdapter.list.clear();
        }
        if (list != null && list.size() > 0) {
            mAdapter.list.addAll(list);
        }
        mAdapter.notifyDataSetChanged();
        if (mAdapter.list.size() < 1) {
            mFlNoList.setVisibility(View.VISIBLE);
            mPulltoRefreshRecyclerView.setPullLoadMoreEnable(false);
            return;
        }
        mPulltoRefreshRecyclerView.setPullLoadMoreEnable(true);
        mFlNoList.setVisibility(View.GONE);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void ongetOrderDataFailed(Throwable ex, String code, String msg) {
        if (TextUtils.equals(code, "401")) {//token未认证
            ToastUtils.showToast("用户已在别处登录，请重新登录");
            toLoginActivity();
            return;
        }
        ToastUtils.showToast(msg);
    }

    private void toLoginActivity() {
        mAdapter.list.clear();
        mAdapter.notifyDataSetChanged();
        mFlNoList.setVisibility(View.VISIBLE);
        Intent toLoginActivity = new Intent(this, LoginActivity.class);
        startActivity(toLoginActivity);
        finish();
    }
}