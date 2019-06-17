package com.spark.coinpay.my.order.detail;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.spark.coinpay.GlobalConstant;
import com.spark.coinpay.MyApplication;
import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.pay.PayActivity;
import com.spark.coinpay.view.AlertIosDialog;
import com.spark.coinpay.view.ConfirmCancelDialog;
import com.spark.coinpay.view.ReleaseDialog;
import com.spark.library.acp.model.MessageResult;
import com.spark.modulebase.utils.MathUtils;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.coinpay.my.order.OrderCancelFragment.orderAchive;


/**
 * 我的订单详情-已取消
 */

public class MyOrderDetailCancelActivity extends BaseActivity implements MyOrderDetailContract.MyOrderDetailIngView {

    @BindView(R.id.ivType)
    ImageView ivType;
    @BindView(R.id.tvDirection)
    TextView tvDirection;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvOrderCreateTimeTag)
    TextView tvOrderCreateTimeTag;
    @BindView(R.id.tvOrderCreateTime)
    TextView tvOrderCreateTime;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvPayTime)
    TextView tvPayTime;
    @BindView(R.id.tvTypeTag)
    TextView tvTypeTag;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvRateTag)
    TextView tvRateTag;
    @BindView(R.id.tvRate)
    TextView tvRate;
    @BindView(R.id.tvPayReferTag)
    TextView tvPayReferTag;
    @BindView(R.id.tvPayRefer)
    TextView tvPayRefer;
    @BindView(R.id.tvOrderNumTag)
    TextView tvOrderNumTag;
    @BindView(R.id.tvOrderNum)
    TextView tvOrderNum;
    @BindView(R.id.tvRemarkTag)
    TextView tvRemarkTag;
    @BindView(R.id.tvRemark)
    TextView tvRemark;
    @BindView(R.id.tvPayTypeTag)
    TextView tvPayTypeTag;
    @BindView(R.id.tvBankName)
    TextView tvBankName;
    @BindView(R.id.ivDrop)
    ImageView ivDrop;
    @BindView(R.id.ivPayWay)
    ImageView ivPayWay;
    @BindView(R.id.tvSure)
    TextView tvSure;
    @BindView(R.id.llPayType)
    LinearLayout llPayType;
    @BindView(R.id.llRelease)
    LinearLayout llRelease;
    @BindView(R.id.tvRealease)
    TextView tvRealease;
    @BindView(R.id.tvRealeaseTime)
    TextView tvRealeaseTime;
    @BindView(R.id.ivPayReferCopy)
    ImageView ivPayReferCopy;
    @BindView(R.id.ivOrderNumCopy)
    ImageView ivOrderNumCopy;
    @BindView(R.id.tvOrderAmount)
    TextView tvOrderAmount;
    @BindView(R.id.tvOrderAmountRandom)
    TextView tvOrderAmountRandom;

    private MyOrderDetailPresenterImpl presenter;
    private ReleaseDialog releaseDialog;
    private AlertIosDialog alertIosDialog;
    private String orderId;//取消订单、放行、支付使用

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.str_order_detail);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new MyOrderDetailPresenterImpl(this);
        if (orderAchive != null) {
            initContent();
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
    }

    private void initContent() {
        orderId = orderAchive.getOrderId();
        int direction = orderAchive.getDirection();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        tvOrderCreateTime.setText(simpleDateFormat.format(orderAchive.getCreateTime()));
        tvPrice.setText(MathUtils.subZeroAndDot(orderAchive.getNumber() + ""));

        tvRate.setText(MathUtils.subZeroAndDot(orderAchive.getPrice() + ""));
        tvPayRefer.setText(orderAchive.getPayRefer());
        tvOrderNum.setText(orderAchive.getOrderId());
        tvRemark.setText("请不要备注任何信息");
        tvSure.setVisibility(View.GONE);
        ivDrop.setVisibility(View.GONE);

        if (direction == 1) {//卖币
            if (orderAchive.getStatus() != null) {
                if (orderAchive.getStatus() == 6) {//卖币订单超时取消6
                    tvSure.setVisibility(View.GONE);
                    tvSure.setText(R.string.str_sure_release);
                }
            }
            ivType.setImageResource(R.mipmap.icon_duiru);
            tvDirection.setText(R.string.str_duiru_cny);
            tvAmount.setTextColor(getResources().getColor(R.color.font_green));
            tvTypeTag.setText(String.format(getString(R.string.str_pay_usdt), orderAchive.getCoinId()));
            tvPayTypeTag.setText(getString(R.string.str_pay_mode));
            llRelease.setVisibility(View.VISIBLE);
        } else if (direction == 2) {//买币
            tvSure.setText(R.string.str_pay);
            ivType.setImageResource(R.mipmap.icon_duichu);
            tvDirection.setText(R.string.str_duichu_cny);
            tvAmount.setTextColor(getResources().getColor(R.color.font_red));
            tvTypeTag.setText(String.format(getString(R.string.str_get_usdt), orderAchive.getCoinId()));
            tvPayTypeTag.setText(getString(R.string.str_pay_way));
            llRelease.setVisibility(View.GONE);
        }

        if (StringUtils.isEmpty(orderAchive.getActualPayment())) {
            tvStatus.setText(getString(R.string.str_wait_pay));
            tvPayTime.setText(MyApplication.getAppContext().getString(R.string.str_already_cancel));
            llRelease.setVisibility(View.GONE);
        } else {
            if (direction == 2) {
                tvStatus.setText(MyApplication.getAppContext().getString(R.string.str_already_pay_m));
            } else {
                tvStatus.setText(MyApplication.getAppContext().getString(R.string.str_already_pay));
            }
            tvPayTime.setText(simpleDateFormat.format(orderAchive.getPayTime()));
            llRelease.setVisibility(View.VISIBLE);
        }

        if (orderAchive.getReleaseTime() != null) {
            tvRealease.setText(getString(R.string.str_already_release));
            tvRealeaseTime.setText(simpleDateFormat.format(orderAchive.getReleaseTime()));
        } else {
            tvRealease.setText(getString(R.string.str_need_release));
            tvRealeaseTime.setText(MyApplication.getAppContext().getString(R.string.str_already_cancel));

        }

        if (orderAchive.getActualAmount() == null) {
            tvAmount.setText(MathUtils.subZeroAndDot(orderAchive.getAmount() + ""));
            tvOrderAmount.setText(MathUtils.subZeroAndDot(orderAchive.getAmount() + ""));
            BigDecimal minuse = orderAchive.getAmount().subtract(orderAchive.getAmount());
            tvOrderAmountRandom.setText(String.format(getResources().getString(R.string.str_order_random_cny), MathUtils.subZeroAndDot(minuse + "")));
        } else {
            tvAmount.setText(MathUtils.subZeroAndDot(orderAchive.getActualAmount() + ""));
            tvOrderAmount.setText(MathUtils.subZeroAndDot(orderAchive.getAmount() + ""));
            BigDecimal minuse = orderAchive.getAmount().subtract(orderAchive.getActualAmount());
            tvOrderAmountRandom.setText(String.format(getResources().getString(R.string.str_order_random_cny), MathUtils.subZeroAndDot(minuse + "")));
        }

    }

    @OnClick({R.id.ivPayReferCopy, R.id.ivOrderNumCopy, R.id.tvSure})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.ivPayReferCopy://复制
                copyText(tvPayRefer.getText().toString());
                break;
            case R.id.ivOrderNumCopy://复制
                copyText(tvOrderNum.getText().toString());
                break;
            case R.id.tvSure://1确认放行
                showReleaseDialog();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    /**
     * 提示框-放行
     */
    private void showReleaseDialog() {
        releaseDialog = new ReleaseDialog(activity);
        releaseDialog.setPositiveOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = releaseDialog.getCheckBox();
                if (!checkBox.isChecked()) {
                    ToastUtils.showToast(activity, getString(R.string.str_confirm_tag));
                    return;
                }
                String pwd = StringUtils.getText(releaseDialog.getPwdEditText());
                if (StringUtils.isEmpty(pwd)) {
                    ToastUtils.showToast(activity, getString(R.string.str_please_input_assets_pwd));
                } else if (pwd.length() != 6) {
                    ToastUtils.showToast(activity, getString(R.string.text_money_pwd_tag));
                } else {
                    presenter.releaseOrder(orderId, pwd);
                    releaseDialog.dismiss();
                }

            }
        });
        releaseDialog.show();
    }


    @Override
    public void cancelOrderSuccess(MessageResult response) {
    }

    @Override
    public void releaseOrderSuccess(MessageResult response) {
        if (response != null) {
            showAlerDialog();
        }
    }

    /**
     * 放行完成
     */
    private void showAlerDialog() {
        if (alertIosDialog == null) {
            alertIosDialog = new AlertIosDialog(activity);
            alertIosDialog.withWidthScale(0.8f);
            alertIosDialog.setContent(getString(R.string.str_release_sure_finish_tag)).setTag(getString(R.string.str_pay_finish_tag));
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

}
