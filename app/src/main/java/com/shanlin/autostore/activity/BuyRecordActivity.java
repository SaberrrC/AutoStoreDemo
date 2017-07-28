package com.shanlin.autostore.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.shanlin.autostore.R;
import com.shanlin.autostore.adapter.FinalRecycleAdapter;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.bean.LoginBean;
import com.shanlin.autostore.bean.RecordBean;
import com.shanlin.autostore.bean.sendbean.MemberUpdateSendBean;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.interf.HttpService;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.ThreadUtils;
import com.shanlin.autostore.view.PulltoRefreshRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2017/7/17 0017.
 */

public class BuyRecordActivity extends BaseActivity implements FinalRecycleAdapter.OnViewAttachListener {

    private ListView                  buyRecordLV;
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
    private LoginBean mLoginBean;


    @Override
    public int initLayout() {
        return R.layout.activity_buyrecord_layout;
    }

    @Override
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = ((TextView) findViewById(R.id.toolbar_title));
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
        map.put(RecordBean.class, R.layout.buy_record_lv_item);
        for (int i = 0; i < 10; i++) {
            mDatas.add(new RecordBean());
        }
        getOrderData();
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
        Intent intent = getIntent();
        mLoginBean = (LoginBean) intent.getSerializableExtra(Constant.USER_INFO);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onBindViewHolder(FinalRecycleAdapter.ViewHolder holder, int position, Object itemData) {
        if (itemData instanceof RecordBean) {
            holder.getRootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(BuyRecordActivity.this, OrderDetailActivity.class);
                    //                    intent.putExtra()
                    startActivity(intent);
                }
            });
            RecordBean recordBean = (RecordBean) itemData;
            TextView tvItemTitle = (TextView) findViewById(R.id.tv_item_title);//邻家无人值守便利店
            TextView tvItemDate = (TextView) findViewById(R.id.tv_item_date);//日期
            TextView tvItemMoney = (TextView) findViewById(R.id.tv_item_money);//500.00
            TextView tvItemPayWay = (TextView) findViewById(R.id.tv_item_pay_way);//微信支付
        }

    }

    public void getOrderData() {
        String userDeviceId = mLoginBean.getData().getUserDeviceId();
        HttpService httpService = CommonUtils.doNet();
        MemberUpdateSendBean memberUpdateSendBean = new MemberUpdateSendBean(userDeviceId);
        // TODO: 2017-7-28



    }
}
