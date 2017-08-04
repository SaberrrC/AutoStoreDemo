package com.shanlin.autostore.activity;

import android.view.View;
import android.widget.TextView;

import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.utils.CommonUtils;

public class RefundExplainActivity extends BaseActivity {

    private TextView mTvComplain;
    public String text = "（1）您可以将欲退的商品放置在退货带进行回收，5个工作日内，将会有专门的工作人员进行验收，确认商品完整后，邻家便利店将退款至账户余额内，用户下载app并完成绑卡后即可提现。\n" + "（2）与客服联系：若因卡注销等异常状态导致退款失败，请联系客服协助为您办理退款事宜。";

    @Override
    public int initLayout() {
        return R.layout.activity_refund_explain;
    }

    @Override
    public void initView() {
        CommonUtils.initToolbar(this, "退款说明", R.color.black, null);
        mTvComplain = (TextView) findViewById(R.id.tv_complain);
        mTvComplain.setText(text);

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {

    }
}
