package com.spark.coinpay.my.set;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.google.gson.Gson;
import com.spark.coinpay.MyApplication;
import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.entity.AutoRelease;
import com.spark.coinpay.event.LoginoutEvent;
import com.spark.coinpay.my.keep_alive.KeepAliveActivity;
import com.spark.coinpay.login.LoginActivity;
import com.spark.coinpay.my.invite_code.InviteCodeActivity;
import com.spark.coinpay.my.language.LanguageActivity;
import com.spark.library.acp.model.MessageResult;
import com.spark.modulebase.entity.User;
import com.spark.modulebase.utils.ActivityManage;
import com.spark.modulebase.utils.SharedPreferencesUtil;
import com.spark.modulebase.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_API_ADDRESS;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_AUTO_ACCEPT;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_VOICE;

/**
 * 设置中心
 */
public class SetActivity extends BaseActivity implements SetContract.SetView {

    @BindView(R.id.llLanguage)
    LinearLayout llLanguage;
    @BindView(R.id.ivVoiceOnOff)
    ImageView ivVoiceOnOff;
    @BindView(R.id.tvCode)
    TextView tvCode;
    @BindView(R.id.llWrite)
    LinearLayout llWrite;
    @BindView(R.id.llPremission)
    LinearLayout llPremission;
    @BindView(R.id.ivAutoAcceptOnOff)
    ImageView ivAutoAcceptOnOff;
    @BindView(R.id.tvApiAddress)
    TextView tvApiAddress;
    @BindView(R.id.tvCopy)
    TextView tvCopy;
    @BindView(R.id.tv_logout)
    TextView tvLogout;

    private SetPresenterImpl presenter;
    private boolean isAutoRelease = false;
    private User curUser;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    //获取用户信息
                    presenter.getUserInfo();
                    break;
            }
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_my_set;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.str_my_setting));
        if (SharedPreferencesUtil.getInstance(activity).contains(SP_KEY_VOICE)) {
            if (SharedPreferencesUtil.getInstance(activity).getBooleanParam(SP_KEY_VOICE)) {
                ivVoiceOnOff.setImageResource(R.mipmap.icon_on);
            } else {
                ivVoiceOnOff.setImageResource(R.mipmap.icon_off);
            }
        } else {
            ivVoiceOnOff.setImageResource(R.mipmap.icon_on);
        }

    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new SetPresenterImpl(this);
    }

    @Override
    protected void loadData() {
        super.loadData();
//        String apiAddress = SharedPreferencesUtil.getInstance(activity).getStringParam(SP_KEY_API_ADDRESS);
        String apiAddress = "1688up.net";
        if (StringUtils.isNotEmpty(apiAddress)) {
            tvApiAddress.setText(apiAddress);
        }
        //获取自动放行配置
        presenter.getAutoRelease();
        //获取用户信息
        presenter.getUserInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @OnClick({R.id.llLanguage, R.id.tv_logout, R.id.ivVoiceOnOff, R.id.ivAutoAcceptOnOff, R.id.tvCopy, R.id.llWrite, R.id.llPremission})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tv_logout:
                showCofirmDialog();
                break;
            case R.id.ivVoiceOnOff:
                if (SharedPreferencesUtil.getInstance(activity).contains(SP_KEY_VOICE)) {
                    if (SharedPreferencesUtil.getInstance(activity).getBooleanParam(SP_KEY_VOICE)) {
                        ivVoiceOnOff.setImageResource(R.mipmap.icon_off);
                        SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_VOICE, false);
                    } else {
                        ivVoiceOnOff.setImageResource(R.mipmap.icon_on);
                        SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_VOICE, true);
                    }
                } else {
                    ivVoiceOnOff.setImageResource(R.mipmap.icon_off);
                    SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_VOICE, false);
                }
                break;
            case R.id.llLanguage://语言设置
                showActivity(LanguageActivity.class, null);
                break;
            case R.id.ivAutoAcceptOnOff:
                if (SharedPreferencesUtil.getInstance(activity).contains(SP_KEY_AUTO_ACCEPT)) {
                    if (SharedPreferencesUtil.getInstance(activity).getBooleanParam(SP_KEY_AUTO_ACCEPT)) {
                        ivAutoAcceptOnOff.setImageResource(R.mipmap.icon_off);
                        SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_AUTO_ACCEPT, false);
                        presenter.updateAutoRelease(0);
                    } else {
                        ivAutoAcceptOnOff.setImageResource(R.mipmap.icon_on);
                        SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_AUTO_ACCEPT, true);
                        presenter.updateAutoRelease(1);
                    }
                } else {
                    ivAutoAcceptOnOff.setImageResource(R.mipmap.icon_on);
                    SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_AUTO_ACCEPT, true);
                    presenter.updateAutoRelease(1);
                }
                break;
            case R.id.tvCopy://复制
                copyText(tvApiAddress.getText().toString().trim());
                break;
            case R.id.llWrite://填写邀请码
                if (curUser == null || StringUtils.isEmpty(curUser.getPromotionCode())) {
                    showActivity(InviteCodeActivity.class, null, 1);
                }
                break;
            case R.id.llPremission://保活权限设置
                showActivity(KeepAliveActivity.class, null);
                break;
        }
    }

    /**
     * 确认是否退出登录
     */
    private void showCofirmDialog() {
        final NormalDialog dialog = new NormalDialog(activity);
        dialog.isTitleShow(false).bgColor(Color.parseColor("#FFFFFF"))
                .content(getString(R.string.str_login_out))
                .contentGravity(Gravity.CENTER)
                .contentTextColor(Color.parseColor("#313131"))
                .btnTextColor(Color.parseColor("#313131"), Color.parseColor("#313131"))
                .btnText(getString(R.string.str_cancel), getString(R.string.str_sure))
                .show();
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();
            }
        }, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                presenter.loginOut();
                dialog.superDismiss();
            }
        });
    }


    @Override
    public void loginOutSuccess(String response) {
        if (response != null) {
            //主动断开长连接
            EventBus.getDefault().post(new LoginoutEvent());

            Bundle bundle = new Bundle();
            User currentUser = MyApplication.getAppContext().getCurrentUser();
            String mobilePhone = currentUser.getMobilePhone();
            String userName = "";
            if (StringUtils.isNotEmpty(mobilePhone) && mobilePhone.startsWith("86")) {
                userName = mobilePhone.substring(2, mobilePhone.length());
            }
            bundle.putString("username", userName);
            MyApplication.getAppContext().deleteCurrentUser();
            MyApplication.getAppContext().getCookieManager().getCookieStore().removeAll();
            ActivityManage.finishAll();
            showActivity(LoginActivity.class, bundle);
        }
    }

    @Override
    public void getAutoReleaseSuccess(String response) {
        if (response != null) {
            AutoRelease entity = new Gson().fromJson(response, AutoRelease.class);
            if (entity != null && entity.getData() != null) {
                if (entity.getData().getAutoReleaseSwitch() == 0) {
                    ivAutoAcceptOnOff.setImageResource(R.mipmap.icon_off);
                    isAutoRelease = false;
                    SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_AUTO_ACCEPT, false);
                } else {
                    ivAutoAcceptOnOff.setImageResource(R.mipmap.icon_on);
                    isAutoRelease = true;
                    SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_AUTO_ACCEPT, true);
                }
            }
        }
    }

    @Override
    public void updateAutoReleaseSuccess(MessageResult response) {
        if (response != null && response.getCode() == 200) {
            isAutoRelease = !isAutoRelease;
            if (isAutoRelease) {
                ivAutoAcceptOnOff.setImageResource(R.mipmap.icon_on);
            } else {
                ivAutoAcceptOnOff.setImageResource(R.mipmap.icon_off);
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
        curUser = user;
        if (user != null && StringUtils.isNotEmpty(user.getPromotionCode())) {
            tvCode.setText(user.getPromotionCode());
            tvCode.setTextColor(getResources().getColor(R.color.font_grey_a5a5a5));
        } else {
            tvCode.setText(getResources().getString(R.string.str_my_set_write_code));
            tvCode.setTextColor(getResources().getColor(R.color.bg_btn_normal));
        }
    }
}
