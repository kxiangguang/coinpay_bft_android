package com.spark.coinpay.my.assets.extract;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.moduleassets.entity.ExtractInfo;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.utils.KeyboardUtils;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加地址
 */

public class ExtractAddAddressActivity extends BaseActivity implements ExtractContract.ExtractAddAddressView {

    @BindView(R.id.tvCoin)
    TextView tvCoin;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etRemark)
    EditText etRemark;
    @BindView(R.id.tvAdd)
    TextView tvAdd;

    private ExtractAddAddressPresenterImpl presenter;
    private ArrayList<Wallet> wallets;
    private String[] coinArrays;
    private Wallet wallet;
    private HashMap<String, ExtractInfo> map;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_extract_add_address;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.str_extract_add_address));
    }

    @Override
    protected void initData() {
        super.initData();
        map = new HashMap<>();
        presenter = new ExtractAddAddressPresenterImpl(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            wallets = (ArrayList<Wallet>) bundle.getSerializable("wallets");
            if (wallets != null && wallets.size() > 0) {
                coinArrays = new String[wallets.size()];
                for (int i = 0; i < wallets.size(); i++) {
                    Wallet w = wallets.get(i);
                    coinArrays[i] = w.getCoinId();
                }
                tvCoin.setText(coinArrays[0]);
                wallet = wallets.get(0);
            }
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
    }

    @OnClick({R.id.tvCoin, R.id.tvAdd})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvCoin://选择币种
                if (coinArrays == null) return;
                showCoinDialog();
                break;
            case R.id.tvAdd://添加
                checkInput();
                break;
        }

    }

    protected void checkInput() {
        super.checkInput();
        String address = StringUtils.getText(etAddress);
        String remark = StringUtils.getText(etRemark);
        if (StringUtils.isEmpty(address)) {
            ToastUtils.showToast(activity, getString(R.string.str_please_input) + getString(R.string.str_extract_addr));
        } else {
            KeyboardUtils.hideSoftInput(activity);
            presenter.addExtractAddress(address, wallet.getCoinId(), remark);
        }
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
                finalNormalDialog.dismiss();
            }
        });
        normalDialog.show();
    }


    @Override
    public void addExtractAddressSuccess(String response) {
        if (StringUtils.isNotEmpty(response)) {
            ToastUtils.showToast(this, response);
        } else {
            ToastUtils.showToast(this, getString(R.string.str_save_success));
        }
        setResult(RESULT_OK);
        finish();
    }

}