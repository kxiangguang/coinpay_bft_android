package com.spark.coinpay.my;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.spark.coinpay.MyApplication;
import com.spark.coinpay.R;
import com.spark.coinpay.acceptances.level.AcceptancesLevelActivity;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.bind_account.BindAccountActivity;
import com.spark.coinpay.event.CheckLoginSuccessEvent;
import com.spark.coinpay.login.LoginActivity;
import com.spark.coinpay.my.about.AboutActivity;
import com.spark.coinpay.my.appeal.AppealActivity;
import com.spark.coinpay.my.assets.MyAssetActivity;
import com.spark.coinpay.my.assets.WalletActivity;
import com.spark.coinpay.my.myinfo.MyInfoActivity;
import com.spark.coinpay.my.order.MyOrderActivity;
import com.spark.coinpay.my.safe.SafeActivity;
import com.spark.coinpay.my.set.SetActivity;
import com.spark.library.acp.model.AcceptMerchantFrontVo;
import com.spark.library.acp.model.Dict;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.entity.User;
import com.spark.modulebase.utils.AppUtils;
import com.spark.modulebase.utils.SharedPreferencesUtil;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;
import com.spark.modulebase.widget.CircleImageView;
import com.spark.moduleotc.entity.AuthenticationStatusEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.spark.modulebase.base.BaseConstant.SUCCESS_CODE;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_AHTH;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_PAY_PASS;

/**
 * 我的
 */
public class MyActivity extends BaseActivity implements MyContract.MyView {
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvLevel)
    TextView tvLevel;
    @BindView(R.id.ivHead)
    CircleImageView ivHead;
    private MyPresenterImpl presenter;
    private List<Dict> dictList;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_my;
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new MyPresenterImpl(this);
        dictList = new ArrayList<>();
        User user = MyApplication.getAppContext().getCurrentUser();
        if (user != null && StringUtils.isNotEmpty(user.getRealName())) {
            tvName.setText(user.getRealName());
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
        //承兑商是否认证
        if (SharedPreferencesUtil.getInstance(activity).getBooleanParam(SP_AHTH)) {
            presenter.getLevelList();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @OnClick({R.id.ivHead, R.id.ivEdit, R.id.llMyAssets, R.id.llMyOrder, R.id.llAppeal, R.id.llCdDetify, R.id.llRec, R.id.llSafe, R.id.llSetting, R.id.llAbout})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        if (!MyApplication.getAppContext().getCurrentUser().isLogin()) {
            showActivity(LoginActivity.class, null);
            return;
        }
        switch (v.getId()) {
            case R.id.ivHead://个人中心
                showActivity(MyInfoActivity.class, null);
                break;
            case R.id.ivEdit://个人中心
                showActivity(MyInfoActivity.class, null);
                break;
            case R.id.llMyAssets://我的资产
                showActivity(MyAssetActivity.class, null);
                break;
            case R.id.llMyOrder://我的订单
                showActivity(MyOrderActivity.class, null);
                break;
            case R.id.llAppeal://纠纷管理
                showActivity(AppealActivity.class, null);
                break;
            case R.id.llCdDetify://承兑商招募
                showActivity(AcceptancesLevelActivity.class, null);
                break;
            case R.id.llRec://收款方式
                //交易密码是否设置
                if (SharedPreferencesUtil.getInstance(activity).getBooleanParam(SP_PAY_PASS)) {
                    showActivity(BindAccountActivity.class, null);
                } else {
                    ToastUtils.showToast(activity, getString(R.string.set_money_pwd_first));
                }
                break;

            case R.id.llSafe://安全中心
                showActivity(SafeActivity.class, null);
                break;
            case R.id.llSetting://设置中心
                showActivity(SetActivity.class, null);
                break;
            case R.id.llAbout://关于我们
                showActivity(AboutActivity.class, null);
                break;
        }

    }

    @Override
    public void getLevelListSuccess(List<Dict> list) {
        if (list != null) {
            dictList = new ArrayList<>();
            this.dictList.clear();
            this.dictList.addAll(list);
            //获取个人等级
            presenter.getSelfLevelInfo();
        }
    }

    @Override
    public void getSelfLevelInfoSuccess(AcceptMerchantFrontVo acceptMerchantFrontVo) {
        int certifiedBusinessStatus = acceptMerchantFrontVo.getCertifiedBusinessStatus();
        //承兑商认证状态 0：未认证 1：认证-待审核 2：认证-审核成功 3：认证-审核失败 5：退保-待审核 6：退保-审核失败 7:退保-审核成功 8:退保-已退还保证金
        for (Dict dict : dictList) {
            if (acceptMerchantFrontVo.getMerchantType().equals(dict.getKey())) {
                tvLevel.setText(dict.getValue());
                break;
            }
        }
    }

    @Override
    public void getLevelListError(HttpErrorEntity httpErrorEntity) {
        dealError(httpErrorEntity);
    }

    @Override
    public void getSelfLevelInfoError(HttpErrorEntity httpErrorEntity) {
        dealError(httpErrorEntity);
    }

    @Override
    public void findAuthenticationStatusSuccess(String response) {
        //解析认证状态数据
        AuthenticationStatusEntity entity = new Gson().fromJson(response, AuthenticationStatusEntity.class);
        //实名认证状态:0-未认证 1待审核 2-审核不通过  3-已认证
        //承兑商认证状态 0：未认证 1：认证-待审核 2：认证-审核成功 3：认证-审核失败 5：退保-待审核 6：退保-审核失败 7:退保-审核成功 8:退保-已退还保证金
        if (entity != null && entity.getCode() == SUCCESS_CODE) {
            if (entity.getData().getIsCertified().getStatus() == 2) {
                presenter.getLevelList();
            }
        }
    }

    /**
     * 获取用户信息成功
     *
     * @param user
     */
    @Override
    public void getUserInfoSuccess(User user) {
        if (user != null) {
            if (StringUtils.isNotEmpty(user.getRealName())) {
                tvName.setText(user.getRealName());
            } else {
                String phone = user.getMobilePhone();
                if (StringUtils.isNotEmpty(phone)) {
                    if (phone.startsWith("86")) {
                        phone = phone.substring(2, phone.length());
                    }
                    phone = AppUtils.addStar(phone);
                    tvName.setText(phone);
                }
            }
            if (StringUtils.isNotEmpty(user.getAvatar())) {
                Glide.with(this).load(user.getAvatar()).into(ivHead);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取用户信息
        presenter.getUserInfo();
    }

    /**
     * check uc、ac、acp成功后，通知刷新界面
     */
    public void onEvent(CheckLoginSuccessEvent event) {
        //获取用户信息
        presenter.getUserInfo();
    }
}
