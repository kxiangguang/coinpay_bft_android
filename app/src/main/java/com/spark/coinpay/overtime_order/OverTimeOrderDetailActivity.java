package com.spark.coinpay.overtime_order;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.coinpay.GlobalConstant;
import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.entity.PayData;
import com.spark.coinpay.my.order.detail.MyOrderDetailContract;
import com.spark.coinpay.my.order.detail.MyOrderDetailPresenterImpl;
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
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.coinpay.overtime_order.OverTimeOrderListActivity.orderInTransit;


/**
 * 超时单详情
 */

public class OverTimeOrderDetailActivity extends BaseActivity implements MyOrderDetailContract.MyOrderDetailIngView {

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
    private NormalListDialog dialog;
    private List<PayData> payDatas;
    private String[] bankNames;
    private String orderId;//取消订单、放行、支付使用
    private boolean isPay = true;//1确认放行/2去支付
    private PayData curPayData;
    private int type = -1;//1支付宝、2微信、3银行卡、4PAYPAL
    private String payMoney = "";//交易金额
    private String limitTime = "";//剩余时间
    private String payRefer = "";//付款参考号
    private ConfirmCancelDialog confirmDialog;
    private ReleaseDialog releaseDialog;
    private AlertIosDialog alertIosDialog;
    //倒计时
    private long randomTime;//订单剩余时间（毫秒）

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
        if (orderInTransit != null) {
            payDatas = new Gson().fromJson(orderInTransit.getPayData(), new TypeToken<List<PayData>>() {
            }.getType());
            orderId = orderInTransit.getOrderId();
            if (payDatas != null && payDatas.size() > 0) {
                bankNames = new String[payDatas.size()];
                for (int i = 0; i < payDatas.size(); i++) {
                    String payType = payDatas.get(i).getPayType();
                    switch (payType) {
                        case GlobalConstant.ALIPAY:
                            bankNames[i] = "支付宝";
                            break;
                        case GlobalConstant.WECHAT:
                            bankNames[i] = "微信";
                            break;
                        case GlobalConstant.BANK:
                            if (payDatas.get(i).getPayAddress().length() > 4) {
                                bankNames[i] = payDatas.get(i).getBank() + "(" + payDatas.get(i).getPayAddress().substring(payDatas.get(i).getPayAddress().length() - 4) + ")";
                            }
                            break;
                        case GlobalConstant.PAYPAL:
                            bankNames[i] = "PAYPAL";
                            break;
                    }
                }
            }
            if (orderInTransit.getDirection() == 2) {//1确认放行/2去支付
                isPay = true;
            } else {
                isPay = false;
            }
            initContent();
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    private void initContent() {
        int direction = orderInTransit.getDirection();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("m分s秒");

        tvOrderCreateTime.setText(simpleDateFormat.format(orderInTransit.getCreateTime()));
        tvPrice.setText(MathUtils.subZeroAndDot(orderInTransit.getNumber() + ""));

        tvRate.setText(MathUtils.subZeroAndDot(orderInTransit.getPrice() + ""));
        tvPayRefer.setText(orderInTransit.getPayRefer());
        tvOrderNum.setText(orderInTransit.getOrderId());
        tvRemark.setText("请不要备注任何信息");
        if (payDatas != null && payDatas.size() > 0) {
            String bankName = bankNames[0];
            tvBankName.setText(bankName);
            curPayData = payDatas.get(0);
            String payType = curPayData.getPayType();
            switch (payType) {
                case GlobalConstant.ALIPAY:
                    ivPayWay.setImageResource(R.mipmap.icon_ali);
                    type = 1;
                    break;
                case GlobalConstant.WECHAT:
                    ivPayWay.setImageResource(R.mipmap.icon_wechat);
                    type = 2;
                    break;
                case GlobalConstant.BANK:
                    ivPayWay.setImageResource(R.mipmap.icon_bank);
                    type = 3;
                    break;
                case GlobalConstant.PAYPAL:
                    ivPayWay.setImageResource(R.mipmap.icon_paypal);
                    type = 4;
                    break;
            }
        }

        if (direction == 1) {//卖币
            tvSure.setVisibility(View.VISIBLE);
            tvSure.setText(R.string.str_sure_release);
            ivType.setImageResource(R.mipmap.icon_duiru);
            tvDirection.setText(R.string.str_duiru_cny);
            tvAmount.setTextColor(getResources().getColor(R.color.font_green));
            tvTypeTag.setText(String.format(getString(R.string.str_pay_usdt), orderInTransit.getCoinId()));
            tvPayTypeTag.setText(getString(R.string.str_pay_mode));
            llRelease.setVisibility(View.VISIBLE);
            ivDrop.setVisibility(View.GONE);
        } else if (direction == 2) {//买币
            tvSure.setText(R.string.str_pay);
            ivType.setImageResource(R.mipmap.icon_duichu);
            tvDirection.setText(R.string.str_duichu_cny);
            tvAmount.setTextColor(getResources().getColor(R.color.font_red));
            tvTypeTag.setText(String.format(getString(R.string.str_get_usdt), orderInTransit.getCoinId()));
            tvPayTypeTag.setText(getString(R.string.str_pay_way));
            llRelease.setVisibility(View.GONE);
            ivDrop.setVisibility(View.VISIBLE);
        }

        if (StringUtils.isEmpty(orderInTransit.getActualPayment())) {
            tvStatus.setText(getString(R.string.str_wait_pay));
            tvRealease.setText(getString(R.string.str_need_release));
            tvRealeaseTime.setText("");
            if (direction == 2) {
                tvPayTime.setText(simpleDateFormat2.format(orderInTransit.getTimeLimit() * 1000) + "后订单取消");
            } else {
                tvPayTime.setText("");
                llRelease.setVisibility(View.GONE);
            }
        } else {
            if (direction == 2) {
                tvStatus.setText(getString(R.string.str_already_pay_m));
            } else {
                tvStatus.setText(getString(R.string.str_already_pay));
            }
            tvRealease.setText(getString(R.string.str_need_release_out));
            tvRealeaseTime.setText("");
            tvPayTime.setText(simpleDateFormat.format(orderInTransit.getPayTime()));
        }

        limitTime = simpleDateFormat2.format(orderInTransit.getTimeLimit() * 1000);
        payRefer = orderInTransit.getPayRefer();

        if (orderInTransit.getActualAmount() == null) {
            payMoney = MathUtils.subZeroAndDot(orderInTransit.getAmount() + "");
            tvAmount.setText(MathUtils.subZeroAndDot(orderInTransit.getAmount() + ""));
            tvOrderAmount.setText(MathUtils.subZeroAndDot(orderInTransit.getAmount() + ""));
            BigDecimal minuse = orderInTransit.getAmount().subtract(orderInTransit.getAmount());
            tvOrderAmountRandom.setText(String.format(getResources().getString(R.string.str_order_random_cny), MathUtils.subZeroAndDot(minuse + "")));
        } else {
            payMoney = MathUtils.subZeroAndDot(orderInTransit.getActualAmount() + "");
            tvAmount.setText(MathUtils.subZeroAndDot(orderInTransit.getActualAmount() + ""));
            tvOrderAmount.setText(MathUtils.subZeroAndDot(orderInTransit.getAmount() + ""));
            BigDecimal minuse = orderInTransit.getAmount().subtract(orderInTransit.getActualAmount());
            tvOrderAmountRandom.setText(String.format(getResources().getString(R.string.str_order_random_cny), MathUtils.subZeroAndDot(minuse + "")));
        }

    }

    @OnClick({R.id.llPayType, R.id.tvSure, R.id.ivPayReferCopy, R.id.ivOrderNumCopy})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvSure://1确认放行/2去支付
                if (isPay) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", type);
                    bundle.putSerializable("data", curPayData);
                    bundle.putString("payMoney", payMoney);
                    bundle.putString("limitTime", limitTime);
                    bundle.putString("payRefer", payRefer);
                    bundle.putString("orderNo", orderId);
                    bundle.putLong("randomTime", randomTime);
                    showActivity(PayActivity.class, bundle, 1);
                } else {
                    showReleaseDialog();
                }
                break;
            case R.id.llPayType://选择支付方式
                if (orderInTransit.getDirection() == 2) {//买币
                    if (payDatas != null && payDatas.size() > 0) {
                        showDialog();
                    } else {
                        ToastUtils.showToast(activity, getString(R.string.str_no_have) + getString(R.string.str_pay_way));
                    }
                }
                break;
            case R.id.ivPayReferCopy://复制
                copyText(tvPayRefer.getText().toString());
                break;
            case R.id.ivOrderNumCopy://复制
                copyText(tvOrderNum.getText().toString());
                break;
        }
    }

    /**
     * 提示框-取消
     */
    private void showConfirmDialog() {
        confirmDialog = new ConfirmCancelDialog(activity);
        confirmDialog.setPositiveOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = StringUtils.getText(confirmDialog.getPwdEditText());
                if (StringUtils.isEmpty(pwd)) {
                    ToastUtils.showToast(activity, getString(R.string.str_please_input_assets_pwd));
                } else if (pwd.length() != 6) {
                    ToastUtils.showToast(activity, getString(R.string.text_money_pwd_tag));
                } else {
                    presenter.cancelOrder(orderId, pwd);
                    confirmDialog.dismiss();
                }

            }
        });
        confirmDialog.show();
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

    /**
     * 选择开户银行
     */
    private void showDialog() {
        dialog = new NormalListDialog(activity, bankNames);
        dialog.title("请选择支付方式");
        dialog.titleBgColor(getResources().getColor(R.color.sec_font_title));
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                String bankName = bankNames[position];
                tvBankName.setText(bankName);
                curPayData = payDatas.get(position);
                String payType = curPayData.getPayType();
                switch (payType) {
                    case GlobalConstant.ALIPAY:
                        ivPayWay.setImageResource(R.mipmap.icon_ali);
                        type = 1;
                        break;
                    case GlobalConstant.WECHAT:
                        ivPayWay.setImageResource(R.mipmap.icon_wechat);
                        type = 2;
                        break;
                    case GlobalConstant.BANK:
                        ivPayWay.setImageResource(R.mipmap.icon_bank);
                        type = 3;
                        break;
                    case GlobalConstant.PAYPAL:
                        ivPayWay.setImageResource(R.mipmap.icon_paypal);
                        type = 4;
                        break;
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public void cancelOrderSuccess(MessageResult response) {
        if (isPay) {
            ToastUtils.showToast(this, R.string.str_cancel_success);
            setResult(RESULT_OK);
            finish();
        } else {
            if (response != null) {
                showAlerDialog();
            }
        }
    }

    @Override
    public void releaseOrderSuccess(MessageResult response) {
        if (isPay) {
            ToastUtils.showToast(this, R.string.str_cancel_success);
            setResult(RESULT_OK);
            finish();
        } else {
            if (response != null) {
                showAlerDialog();
            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1://刷新首页数据
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
    }
}
