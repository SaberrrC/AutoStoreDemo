package com.shanlin.android.autostore.ui.act;

import android.content.Intent;
import android.view.View;

import com.shanlin.android.autostore.common.base.BaseActivity;
import com.shanlin.android.autostore.common.constants.Constant;
import com.shanlin.android.autostore.common.utils.CommonUtils;
import com.shanlin.android.autostore.common.utils.SpUtils;
import com.shanlin.android.autostore.common.utils.ToastUtils;
import com.shanlin.android.autostore.entity.body.MemberUpdateSendBean;
import com.shanlin.android.autostore.entity.respone.MemberUpdateBean;
import com.shanlin.android.autostore.presenter.Contract.SaveFaceActContract;
import com.shanlin.android.autostore.presenter.SaveFacePresenter;
import com.shanlin.android.autostore.ui.view.ProgressDialog;
import com.shanlin.autostore.R;

import butterknife.OnClick;

public class SaveFaceActivity extends BaseActivity<SaveFacePresenter> implements SaveFaceActContract.View {

    private ProgressDialog mProgressDialog;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_save_face;
    }

    @Override
    public void initData() {
        CommonUtils.initToolbar(this, "人脸资料录入成功", R.color.black, null);
    }

    private void doMemberUpdate() {
        String userDeviceId = SpUtils.getString(this, Constant.DEVICEID, "");
        String imageBase64 = getIntent().getStringExtra(Constant.SaveFaceActivity.IMAGE_BASE64);
        MemberUpdateSendBean memberUpdateSendBean = new MemberUpdateSendBean(userDeviceId);
        memberUpdateSendBean.imageBase64 = imageBase64;
        mPresenter.doMemberUpdate(memberUpdateSendBean);
    }

    private void showLoadingDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.show();
    }

    @OnClick(R.id.tv_save)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_save:
                if (!CommonUtils.checkNet()) {
                    return;
                }
                showLoadingDialog();
                doMemberUpdate();
                break;
        }
    }

    @Override
    public void doMemberUpdateSuccess(String code, MemberUpdateBean data, String msg) {
        mProgressDialog.dismiss();
        ToastUtils.showToast(msg);
        Intent intent = new Intent(SaveFaceActivity.this, MainActivity.class);
        intent.putExtra(Constant.MainActivityArgument.MAIN_ACTIVITY, Constant.FACE_REGESTED_OK);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    public void doMemberUpdateFailed(Throwable ex, String code, String msg) {
        mProgressDialog.dismiss();
        ToastUtils.showToast(msg);
    }
}
