package com.spark.coinpay.my.assets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.coinpay.GlobalConstant;
import com.spark.coinpay.R;
import com.spark.coinpay.adapter.WalletAdapter;
import com.spark.coinpay.base.BaseFragment;
import com.spark.coinpay.my.assets.extract.ExtractActivity;
import com.spark.coinpay.my.assets.recharge.RechargeActivity;
import com.spark.coinpay.my.assets.transfer.TransferActivity;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.base.BaseConstant;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.utils.MathUtils;
import com.spark.modulebase.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.spark.moduleassets.AssetsConstants.COMMON;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_SHOW;

/**
 * 基础账户
 */

public class BaseAccountFragment extends BaseFragment implements WalletContract.WalletView {
    @BindView(R.id.recyView)
    RecyclerView recyView;
    @BindView(R.id.evSearch)
    EditText evSearch;
    @BindView(R.id.tvCount)
    TextView tvCount;
    @BindView(R.id.tvCny)
    TextView tvCny;
    @BindView(R.id.tvFrozen)
    TextView tvFrozen;
    @BindView(R.id.ivBaseEye)
    ImageView ivBaseEye;
    @BindView(R.id.cbHide)
    CheckBox cbHide;
    private WalletPresenterImpl presenter;
    private ArrayList<Wallet> wallets = new ArrayList<>();
    private ArrayList<Wallet> removeCoins = new ArrayList<>();
    private ArrayList<Wallet> keepCoins = new ArrayList<>();
    private ArrayList<Wallet> zeroCoins = new ArrayList<>();
    private WalletAdapter adapter;
    private double sumUsd = 0;
    private double sumCny = 0;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1: //划转
                case 2: //充值
                case 3: //提现
                    refresh();
                    break;
            }
        }
    }

    @Override
    public void update() {
        super.update();
        presenter.getWallet(COMMON);
    }

    public void refresh() {
        WalletActivity activity = (WalletActivity) getActivity();
        List<BaseFragment> list = activity.getList();
        for (BaseFragment baseFragment : list) {
            baseFragment.update();
        }
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_base_account;
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new WalletPresenterImpl(this);
    }

    @Override
    protected void initRv() {
        super.initRv();
        LinearLayoutManager manager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyView.setLayoutManager(manager);
        adapter = new WalletAdapter(R.layout.item_lv_wallet, wallets);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.isFirstOnly(true);
        recyView.setAdapter(adapter);
    }

    @Override
    protected void loadData() {
        super.loadData();
        presenter.getWallet(COMMON);
    }

    @OnClick({R.id.ivBaseEye, R.id.tvTranferBase, R.id.tvRechargeBase, R.id.tvExtractBase})
    @Override
    protected void setOnClickListener(View v) {
        Bundle bundle = null;
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.ivBaseEye:
                switchSee();
                break;
            case R.id.tvTranferBase:
                bundle = new Bundle();
                bundle.putBoolean("isFromBaseToTrade", true);
                bundle.putDouble("baseBalance", sumUsd);
                bundle.putDouble("tradeBalance", ((WalletActivity) activity).getTradeBalance());
                bundle.putSerializable("wallets", wallets);
                showActivity(TransferActivity.class, bundle, 1);
                break;
            case R.id.tvRechargeBase:
                bundle = new Bundle();
                bundle.putSerializable("wallets", wallets);
                showActivity(RechargeActivity.class, bundle, 2);
                break;
            case R.id.tvExtractBase:
                bundle = new Bundle();
                bundle.putSerializable("wallets", wallets);
                showActivity(ExtractActivity.class, bundle, 3);
                break;
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Wallet wallet = (Wallet) adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("wallet", wallet);
            }
        });

        cbHide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    zeroCoins.clear();
                    for (int i = 0; i < wallets.size(); i++) {
                        if (wallets.get(i).getBalance() == 0) {
                            zeroCoins.add(wallets.get(i));
                        }
                    }
                    wallets.removeAll(zeroCoins);
                    adapter.notifyDataSetChanged();
                } else {
                    wallets.addAll(zeroCoins);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 控制资产数额可见
     */
    private void switchSee() {
        if (SharedPreferencesUtil.getInstance(activity).getBooleanParam(SP_KEY_SHOW)) {
            tvCny.setText("*****");
            tvCount.setText("********");
            SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_SHOW, false);
            ivBaseEye.setImageResource(R.mipmap.icon_eye_close);
        } else {
            tvCount.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(sumUsd, BaseConstant.MONEY_FORMAT, null)));
            tvCny.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(sumCny, BaseConstant.MONEY_FORMAT, null)) + GlobalConstant.CNY);
            SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_SHOW, true);
            ivBaseEye.setImageResource(R.mipmap.icon_eye_open);
        }
    }

    @Override
    public String getmTag() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void getWalletSuccess(String type, List<Wallet> list) {
        if (list == null) return;
        this.wallets.clear();
        this.wallets.addAll(list);
        keepCoins.addAll(list);
        adapter.notifyDataSetChanged();
        evSearch.addTextChangedListener(localChangeWatcher);
        calcuTotal(wallets);
//        if (StringUtils.isNotEmpty(depositRecordAmount, depositRecordCoinId)) {
//            tvBond.setText(getString(R.string.bond_text) + ": " + depositRecordAmount + depositRecordCoinId);
//        }
    }

    private void calcuTotal(List<Wallet> coins) {
        sumUsd = 0;
        sumCny = 0;
        for (Wallet coin : coins) {
            sumUsd += coin.getBalance();
            sumCny += coin.getBalance();
        }
        ((WalletActivity) activity).setBaseBalance(sumUsd);
        if (ivBaseEye.getVisibility() == View.GONE) {
            ivBaseEye.setVisibility(View.VISIBLE);
        }
        if (SharedPreferencesUtil.getInstance(activity).getBooleanParam(SP_KEY_SHOW)) {
            tvCount.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(sumUsd, BaseConstant.MONEY_FORMAT, null)));
            tvCny.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(sumCny, BaseConstant.MONEY_FORMAT, null)) + " CNY");
            ivBaseEye.setImageResource(R.mipmap.icon_eye_open);
        } else {
            tvCount.setText("********");
            tvCny.setText("*****");
            ivBaseEye.setImageResource(R.mipmap.icon_eye_close);
        }
    }

    private TextWatcher localChangeWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            removeCoins.clear();
            localCoinChange();
        }
    };

    private void localCoinChange() {
        String str = evSearch.getText().toString().toUpperCase();
        if (str.isEmpty()) {
            wallets.clear();
            wallets.addAll(keepCoins);
            adapter.notifyDataSetChanged();
        } else {
            for (int i = 0; i < wallets.size(); i++) {
                if (!wallets.get(i).getCoinId().contains(str)) {
                    removeCoins.add(wallets.get(i));
                }
            }
            wallets.removeAll(removeCoins);
            adapter.notifyDataSetChanged();
        }
    }
}
