package com.spark.coinpay.my.assets.transfer;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.library.ac.model.AssetTransferDto;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.base.BaseConstant;
import com.spark.modulebase.utils.MathUtils;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;

import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 资金划转
 */

public class TransferActivity extends BaseActivity implements TransferContract.TransferView {
    @BindView(R.id.tvToAccount)
    TextView tvToAccount;
    @BindView(R.id.tvFromAccount)
    TextView tvFromAccount;
    @BindView(R.id.tvCoin)
    TextView tvCoin;
    @BindView(R.id.edtNumber)
    EditText edtNumber;
    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.tvGetUnit)
    TextView tvGetUnit;

    private boolean isFromBaseToTrade;
    private String[] coinArrays;
    private Wallet wallet;
    private TransferPresenterImp presenter;
    private double baseBalance;
    private double tradeBalance;
    private ArrayList<Wallet> wallets;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_transfer;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        presenter = new TransferPresenterImp(this);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.str_assets_transfer));
    }

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isFromBaseToTrade = bundle.getBoolean("isFromBaseToTrade");
            baseBalance = bundle.getDouble("baseBalance");
            tradeBalance = bundle.getDouble("tradeBalance");
            if (isFromBaseToTrade) {
                tvFromAccount.setText(getString(R.string.str_base_account));
                tvToAccount.setText(getString(R.string.str_trade_account));
            } else {
                tvFromAccount.setText(getString(R.string.str_trade_account));
                tvToAccount.setText(getString(R.string.str_base_account));
            }
            wallets = (ArrayList<Wallet>) bundle.getSerializable("wallets");
            if (wallets != null && wallets.size() > 0) {
                setCoinArray();
                tvBalance.setText(getString(R.string.str_transfer_banlance) + MathUtils.subZeroAndDot(MathUtils.getRundNumber(wallet.getBalance(), BaseConstant.MONEY_FORMAT, null)) + wallet.getCoinId());
            } else {
                tvBalance.setText(getString(R.string.str_transfer_banlance) + MathUtils.subZeroAndDot(MathUtils.getRundNumber(0, BaseConstant.MONEY_FORMAT, null)));
            }
        }
    }

    /**
     * 设置可选币种
     */
    private void setCoinArray() {
        coinArrays = new String[wallets.size()];
        for (int i = 0; i < wallets.size(); i++) {
            Wallet w = wallets.get(i);
            coinArrays[i] = w.getCoinId();
        }
        tvCoin.setText(coinArrays[0]);
        tvGetUnit.setText(coinArrays[0]);
        wallet = wallets.get(0);
    }

    @OnClick({R.id.ivSwitch, R.id.tvCoin, R.id.tvAll, R.id.tvSure})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.ivSwitch://切换
                if (isFromBaseToTrade) {
                    isFromBaseToTrade = false;
                    tvFromAccount.setText(getString(R.string.str_trade_account));
                    tvToAccount.setText(getString(R.string.str_base_account));
                    tvBalance.setText(getString(R.string.str_transfer_banlance) + MathUtils.subZeroAndDot(MathUtils.getRundNumber(tradeBalance, BaseConstant.MONEY_FORMAT, null)) + wallet.getCoinId());
                } else {
                    isFromBaseToTrade = true;
                    tvFromAccount.setText(getString(R.string.str_base_account));
                    tvToAccount.setText(getString(R.string.str_trade_account));
                    tvBalance.setText(getString(R.string.str_transfer_banlance) + MathUtils.subZeroAndDot(MathUtils.getRundNumber(baseBalance, BaseConstant.MONEY_FORMAT, null)) + wallet.getCoinId());
                }
                break;
            case R.id.tvCoin://选择币种
                if (coinArrays == null) return;
                showCoinDialog();
                break;
            case R.id.tvAll://全部
                if (wallet != null) {
                    edtNumber.setText(wallet.getBalance() + "");
                }
                break;
            case R.id.tvSure://确定
                checkInput();
                break;
        }
    }


    @Override
    protected void checkInput() {
        super.checkInput();
        String count = StringUtils.getText(edtNumber);
        String tradePassword = StringUtils.getText(etPassword);
        if (StringUtils.isEmpty(count)) {
            ToastUtils.showToast(activity, getString(R.string.str_please_input) + getString(R.string.str_transfer_count));
        } else if (StringUtils.isEmpty(tradePassword)) {
            ToastUtils.showToast(activity, getString(R.string.str_please_input) + getString(R.string.text_money_pwd));
        } else if (Double.parseDouble(count) <= 0) {
            ToastUtils.showToast(activity, getString(R.string.str_min_coin_amount));
        } else {
            BigDecimal bigDecimal = new BigDecimal(count).setScale(8, BigDecimal.ROUND_HALF_UP);
            String coinName = wallet.getCoinId();
            if (isFromBaseToTrade) {
                if (Double.parseDouble(count) > baseBalance) {
                    ToastUtils.showToast(activity, getString(R.string.str_transfer_banlance) + baseBalance);
                } else {
                    presenter.doWithDraw(bigDecimal, coinName, AssetTransferDto.FromEnum.COMMON, AssetTransferDto.ToEnum.ACP, tradePassword);
                }
            } else {
                if (Double.parseDouble(count) > tradeBalance) {
                    ToastUtils.showToast(activity, getString(R.string.str_transfer_banlance) + tradeBalance);
                } else {
                    presenter.doWithDraw(bigDecimal, coinName, AssetTransferDto.FromEnum.ACP, AssetTransferDto.ToEnum.COMMON, tradePassword);
                }

            }
        }
    }

    @Override
    public void doWithDrawSuccess(String response) {
        if (StringUtils.isNotEmpty(response)) {
            ToastUtils.showToast(this, response);
        } else {
            ToastUtils.showToast(this, getString(R.string.str_save_success));
        }
        setResult(RESULT_OK);
        finish();
    }


    /**
     * 展示币种
     */
    private void showCoinDialog() {
        NormalListDialog normalDialog = null;
        normalDialog = new NormalListDialog(activity, coinArrays);
        normalDialog.titleBgColor(getResources().getColor(R.color.bg_btn_normal));
//        normalDialog.widthScale(0.8f);
        normalDialog.title(getString(R.string.str_select_coin));
        final NormalListDialog finalNormalDialog = normalDialog;
        normalDialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                wallet = wallets.get(position);
                tvCoin.setText(wallet.getCoinId());
                tvGetUnit.setText(wallet.getCoinId());
                tvBalance.setText(getString(R.string.str_transfer_banlance) + MathUtils.subZeroAndDot(MathUtils.getRundNumber(wallet.getBalance(), BaseConstant.MONEY_FORMAT, null)) + wallet.getCoinId());
                finalNormalDialog.dismiss();
            }
        });
        normalDialog.show();
    }

}
