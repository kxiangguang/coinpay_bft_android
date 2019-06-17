package com.spark.coinpay.main.buy;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spark.coinpay.GlobalConstant;
import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.entity.PayData;
import com.spark.coinpay.view.AlertIosDialog;
import com.spark.coinpay.view.ConfirmIosDialog;
import com.spark.coinpay.view.ConfirmPayDialog;
import com.spark.coinpay.view.PayCodeDialog;
import com.spark.library.acp.model.MessageResult;
import com.spark.modulebase.utils.AppUtils;
import com.spark.modulebase.utils.DateUtils;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 一键买币-第四步-付款完成
 */

public class BuyPayFinishActivity extends BaseActivity implements BuyPayFinishContract.PayView {


    @BindView(R.id.tvPayMoney)
    TextView tvPayMoney;
    @BindView(R.id.tvPayTag)
    TextView tvPayTag;
    @BindView(R.id.tvJump)
    TextView tvJump;
    @BindView(R.id.tvRec)
    TextView tvRec;
    @BindView(R.id.tvRecCopy)
    ImageView tvRecCopy;
    @BindView(R.id.tvRecAccount)
    TextView tvRecAccount;
    @BindView(R.id.tvRecAccountCopy)
    ImageView tvRecAccountCopy;
    @BindView(R.id.ivRecCode)
    ImageView ivRecCode;
    @BindView(R.id.tvPayRefer)
    TextView tvPayRefer;
    @BindView(R.id.tvPayReferCopy)
    ImageView tvPayReferCopy;
    @BindView(R.id.tvRemarkTag)
    TextView tvRemarkTag;
    @BindView(R.id.tvPayPer)
    TextView tvPayPer;
    @BindView(R.id.tvPayPerCopy)
    ImageView tvPayPerCopy;
    @BindView(R.id.tvOpenBank)
    TextView tvOpenBank;
    @BindView(R.id.tvOpenBankCopy)
    ImageView tvOpenBankCopy;
    @BindView(R.id.tvBranch)
    TextView tvBranch;
    @BindView(R.id.tvBranchCopy)
    ImageView tvBranchCopy;
    @BindView(R.id.tvBankNum)
    TextView tvBankNum;
    @BindView(R.id.tvBankNumCopy)
    ImageView tvBankNumCopy;
    @BindView(R.id.tvPayNum)
    TextView tvPayNum;
    @BindView(R.id.tvPayNumCopy)
    ImageView tvPayNumCopy;
    @BindView(R.id.tvTag)
    TextView tvTag;
    @BindView(R.id.tvPayFinish)
    TextView tvPayFinish;
    @BindView(R.id.llAliPay)
    LinearLayout llAliPay;
    @BindView(R.id.llBankPay)
    LinearLayout llBankPay;
    @BindView(R.id.tvBankRemark)
    TextView tvBankRemark;

    private AlertIosDialog alertIosDialog;
    private PayCodeDialog payCodeDialog;
    private PayData curPayData;
    private int type = -1;//1支付宝、2微信、3银行卡、4PAYPAL
    private String payMoney = "";//交易金额
    private String limitTime = "";//剩余时间
    private String payRefer = "";//付款参考号
    private String actualPayment = "";//实际选择的付款方式
    private String orderNo = "";//订单编号
    private BuyPayFinishPresenterImpl presenter;
    //倒计时
    private long randomTime;//订单剩余时间（毫秒）
    private String qrCodeUrl;
    private String codeAliUrl = "alipays://platformapi/startapp";

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_buy_pay_finish;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            type = bundle.getInt("type", -1);
            curPayData = (PayData) bundle.getSerializable("data");
            if (curPayData == null) {
                ToastUtils.showToast("未找到收款方式");
                return;
            }
            payMoney = bundle.getString("payMoney");
            limitTime = bundle.getString("limitTime");
            payRefer = bundle.getString("payRefer");
            orderNo = bundle.getString("orderNo");
            randomTime = bundle.getLong("randomTime");
            tvPayMoney.setText(payMoney + " CNY");

            switch (type) {//1支付宝、2微信、3银行卡、4PAYPAL
                case 1://1支付宝
                    llAliPay.setVisibility(View.VISIBLE);
                    llBankPay.setVisibility(View.GONE);
                    actualPayment = GlobalConstant.ALIPAY;
                    tvTitle.setText(getString(R.string.str_ali));
                    tvPayTag.setText(R.string.str_ali);
                    Drawable drawableLeft = getResources().getDrawable(R.mipmap.icon_ali);
                    tvPayTag.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
                    tvJump.setText(R.string.str_open_ali);

                    tvRec.setText(curPayData.getRealName());
                    tvRecAccount.setText(curPayData.getPayAddress());
                    //Glide.with(this).load(curPayData.getQrCodeUrl()).into(ivRecCode);
                    tvPayRefer.setText(payRefer);
                    break;
                case 2://2微信
                    llAliPay.setVisibility(View.VISIBLE);
                    llBankPay.setVisibility(View.GONE);
                    actualPayment = GlobalConstant.WECHAT;
                    tvTitle.setText(getString(R.string.str_wechat));
                    tvPayTag.setText(R.string.str_wechat);
                    drawableLeft = getResources().getDrawable(R.mipmap.icon_wechat);
                    tvPayTag.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
                    tvJump.setText(R.string.str_open_wechat);

                    tvRec.setText(curPayData.getRealName());
                    tvRecAccount.setText(curPayData.getPayAddress());
                    //Glide.with(this).load(curPayData.getQrCodeUrl()).into(ivRecCode);
                    tvPayRefer.setText(payRefer);
                    break;
                case 3://3银行卡
                    llAliPay.setVisibility(View.GONE);
                    llBankPay.setVisibility(View.VISIBLE);
                    actualPayment = GlobalConstant.BANK;
                    tvTitle.setText(getString(R.string.str_bank));

                    tvPayPer.setText(curPayData.getRealName());
                    tvOpenBank.setText(curPayData.getBank());
                    tvBranch.setText(curPayData.getBranch());
                    tvBankNum.setText(curPayData.getPayAddress());
                    tvPayNum.setText(payRefer);
                    break;
                case 4://4PAYPAL
                    actualPayment = GlobalConstant.PAYPAL;
                    tvTitle.setText(getString(R.string.str_paypal));
                    break;
            }
            limitTime = DateUtils.getTimeDown(randomTime, DateUtils.DATE_FORMAT_2, "", "");
            //tvlimitTime.setText(limitTime);
            String htm = "请在<font color=#FD5858>" + limitTime + "</font>" + "以内向以上收款方式支付" + payMoney + "CNY";
            tvTag.setText(Html.fromHtml(htm));
            if (randomTime > 0) {
                mTimeHandler.post(runnable);//刷新时间
            } else {
                mTimeHandler.removeCallbacks(runnable);
            }
            tvRemarkTag.setText("---");
            tvBankRemark.setText("---");
        }
        presenter = new BuyPayFinishPresenterImpl(this);
    }

    @OnClick({R.id.tvPayFinish, R.id.ivRecCode, R.id.tvRecCopy, R.id.tvRecAccountCopy, R.id.tvPayReferCopy,
            R.id.tvPayPerCopy, R.id.tvOpenBankCopy, R.id.tvBranchCopy, R.id.tvBankNumCopy, R.id.tvPayNumCopy, R.id.tvJump})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvPayFinish:
                if (curPayData == null) {
                    ToastUtils.showToast("未找到收款方式");
                    return;
                }
                presenter.payComplete(actualPayment, orderNo);
                break;
            case R.id.ivRecCode://二维码
                if (curPayData != null && StringUtils.isNotEmpty(curPayData.getQrCodeUrl())) {
                    qrCodeUrl = curPayData.getQrCodeUrl();
                    showPayCodeDialog();
                }
                break;
            case R.id.tvRecCopy://复制
                copyText(tvRec.getText().toString());
                break;
            case R.id.tvRecAccountCopy://复制
                copyText(tvRecAccount.getText().toString());
                break;
            case R.id.tvPayReferCopy://复制
                copyText(tvPayRefer.getText().toString());
                break;
            case R.id.tvPayPerCopy://复制
                copyText(tvPayPer.getText().toString());
                break;
            case R.id.tvOpenBankCopy://复制
                copyText(tvOpenBank.getText().toString());
                break;
            case R.id.tvBranchCopy://复制
                copyText(tvBranch.getText().toString());
                break;
            case R.id.tvBankNumCopy://复制
                copyText(tvBankNum.getText().toString());
                break;
            case R.id.tvPayNumCopy://复制
                copyText(tvPayNum.getText().toString());
                break;
            case R.id.tvJump://打开支付宝或者微信
                if (type == 1) {//1支付宝
                    if (AppUtils.isInstalled(activity, "com.eg.android.AlipayGphone")) {
                        skipScheme(activity, codeAliUrl);
                    } else {
                        ToastUtils.showToast("未找到支付宝客户端");
                    }
                }
                if (type == 2) {//2微信
                    if (AppUtils.isInstalled(activity, "com.tencent.mm")) {
                        openWeChat();
                    } else {
                        ToastUtils.showToast("未找到微信客户端");
                    }
                }
                break;
        }
    }

    /**
     * 跳转支付宝
     *
     * @param context
     * @param newurl
     * @return
     */
    public static boolean skipScheme(Context context, String newurl) {
        try {
            // 以下固定写法
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(newurl));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            List<ResolveInfo> resolves = context.getPackageManager().queryIntentActivities(intent, 0);
            if (resolves.size() > 0) {
                ((Activity) context).startActivityIfNeeded(intent, -1);
            }
        } catch (Exception e) {
            // 防止没有安装的情况
            ToastUtils.showToast("未找到支付宝客户端");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 跳转微信
     */
    private void openWeChat() {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ToastUtils.showToast("未找到微信客户端");
        }

    }

    /**
     * 提示框
     */
    private void showPayCodeDialog() {
        payCodeDialog = new PayCodeDialog(activity);
        payCodeDialog.setImg(qrCodeUrl, randomTime);
        payCodeDialog.show();
    }

    @Override
    public void payCompleteSuccess(MessageResult response) {
        if (response != null) {
            showAlerDialog();
        }
    }

    /**
     * 支付完成
     */
    private void showAlerDialog() {
        if (alertIosDialog == null) {
            alertIosDialog = new AlertIosDialog(activity);
            alertIosDialog.withWidthScale(0.8f);
            alertIosDialog.setContent(getString(R.string.str_pay_success_wait)).setTag(getString(R.string.str_pay_finish_tag));
            alertIosDialog.setPositiveClickLister(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertIosDialog.dismiss();
                    setResult(RESULT_OK);
                    finish();
                }
            });
            alertIosDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }
        alertIosDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimeHandler.removeCallbacks(runnable);
        presenter.destory();
    }

    @Override
    protected void setListener() {
        super.setListener();
    }

    Handler mTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://刷新剩余时间
                    if (randomTime > 0) {
                        randomTime -= 1000;
                        setTime(randomTime);
                    } else {
                        setTime(0);
                        mTimeHandler.removeCallbacks(runnable);
                    }
                    if (payCodeDialog != null) {
                        payCodeDialog.setImg(qrCodeUrl, randomTime);
                    }
                    break;
            }
        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mTimeHandler.postDelayed(runnable, 1000);
            Message message = new Message();
            message.what = 1;
            mTimeHandler.sendMessage(message);
        }
    };

    /**
     * 设置时间
     *
     * @param randomTime
     */
    private void setTime(long randomTime) {
        limitTime = DateUtils.getTimeDown(randomTime, DateUtils.DATE_FORMAT_2, "", "");
//        if (tvlimitTime != null) {
//            tvlimitTime.setText(limitTime);
//        }
        if (tvTag != null) {
            String htm = "请在<font color=#FD5858>" + limitTime + "</font>" + "以内向以上收款方式支付" + payMoney + "CNY";
            tvTag.setText(Html.fromHtml(htm));
        }
    }
}
