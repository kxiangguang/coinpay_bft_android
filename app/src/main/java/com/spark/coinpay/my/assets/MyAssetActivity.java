package com.spark.coinpay.my.assets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.coinpay.R;
import com.spark.coinpay.adapter.WalletAdapter;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.event.CheckLoginSuccessEvent;
import com.spark.coinpay.my.assets.extract.ExtractActivity;
import com.spark.coinpay.my.assets.recharge.RechargeActivity;
import com.spark.coinpay.my.assets.record.MyAssetTradeRecordActivity;
import com.spark.coinpay.view.AssetsRewardDescDialog;
import com.spark.library.acp.model.AcceptMerchantFrontVo;
import com.spark.library.acp.model.MessageResult;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.base.BaseConstant;
import com.spark.modulebase.utils.DateUtils;
import com.spark.modulebase.utils.MathUtils;
import com.spark.modulebase.utils.SharedPreferencesUtil;
import com.spark.modulebase.utils.StringUtils;
import com.spark.moduleotc.entity.AcceptMerchantInfoEntity;
import com.spark.moduleotc.entity.AcceptanceMerchantListEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.moduleassets.AssetsConstants.ACP;
import static com.spark.moduleassets.AssetsConstants.COMMON;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_AHTH;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_SHOW;

/**
 * 我的资产-修改版
 */
public class MyAssetActivity extends BaseActivity implements MyAssetContract.MyAssetView {

    @BindView(R.id.tvAssetCommission)
    TextView tvAssetCommission;
    @BindView(R.id.tvAssetCommissionY)
    TextView tvAssetCommissionY;
    @BindView(R.id.tvAssetCommissionUn)
    TextView tvAssetCommissionUn;
    @BindView(R.id.ivAssetEyeTrade)
    ImageView ivAssetEyeTrade;
    @BindView(R.id.tvAssetMoneyTrade)
    TextView tvAssetMoneyTrade;
    @BindView(R.id.tvAssetMoneyTradeBail)
    TextView tvAssetMoneyTradeBail;
    @BindView(R.id.llAssetMoneyTradeRecharge)
    LinearLayout llAssetMoneyTradeRecharge;
    @BindView(R.id.llAssetMoneyTradeExtract)
    LinearLayout llAssetMoneyTradeExtract;
    @BindView(R.id.llTrade)
    LinearLayout llTrade;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvMoneyType)
    TextView tvMoneyType;
    @BindView(R.id.tvMoneyTypeTag)
    TextView tvMoneyTypeTag;
    @BindView(R.id.ivMoneyType)
    ImageView ivMoneyType;
    @BindView(R.id.ivAssetsDesc)
    ImageView ivAssetsDesc;


    private MyAssetPresenterImpl presenter;
    private ArrayList<Wallet> walletsBase = new ArrayList<>();
    private ArrayList<Wallet> walletsTrade = new ArrayList<>();
    private double sumTrade = 0;//交易账户金额
    private double sumCny = 0;//保证金金额

    private ArrayList<Wallet> wallets = new ArrayList<>();
    private WalletAdapter adapter;
    private String coin;
    private AssetsRewardDescDialog assetsRewardDescDialog;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_asset;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1: //划转
                case 2: //充值
                case 3: //提现
                    //查询交易账户
                    presenter.getWallet(ACP);
                    break;
            }
        }
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new MyAssetPresenterImpl(this);
    }

    @Override
    protected void initRv() {
        super.initRv();
        LinearLayoutManager manager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new WalletAdapter(R.layout.item_lv_wallet, wallets);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void loadData() {
        super.loadData();
        //承兑商是否认证
        if (SharedPreferencesUtil.getInstance(activity).getBooleanParam(SP_AHTH)) {
            //查询累计佣金、待结算佣金
            presenter.getTrade();
            //查询昨日佣金
            String tradingDay = DateUtils.getBackDate("yyyyMMdd", -1);
            presenter.getTradeYesterday(tradingDay);
            //获取个人等级---获取保证金
            presenter.getSelfLevelInfo();
        }
        //查询交易账户
        presenter.getWallet(COMMON);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @OnClick({R.id.llAssetMoneyTradeRecharge, R.id.llAssetMoneyTradeExtract, R.id.ivAssetEyeTrade, R.id.llTrade, R.id.tvGoto, R.id.ivAssetsDesc})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        Bundle bundle = null;
        switch (v.getId()) {
            case R.id.llAssetMoneyTradeRecharge://充币
                bundle = new Bundle();
                bundle.putSerializable("wallets", walletsTrade);
                showActivity(RechargeActivity.class, bundle, 2);
                break;
            case R.id.llAssetMoneyTradeExtract://提币
                bundle = new Bundle();
                bundle.putSerializable("wallets", walletsTrade);
                showActivity(ExtractActivity.class, bundle, 3);
                break;
            case R.id.ivAssetEyeTrade://交易账户-查看
                switchSee(ACP);
                break;
            case R.id.llTrade://交易账户-跳转
                //showActivity(MyAssetTradeActivity.class, bundle, 1);
                break;
            case R.id.tvGoto://明细
                showActivity(MyAssetTradeRecordActivity.class, bundle, 1);
                break;
            case R.id.ivAssetsDesc://佣金说明
                showPayCodeDialog();
                break;
        }
    }

    @Override
    public void getWalletSuccess(String type, List<Wallet> list) {
        if (type == COMMON) {
            this.walletsBase.clear();
            this.walletsBase.addAll(list);
            //查询交易账户
            presenter.getWallet(ACP);
        } else if (type == ACP) {
            this.wallets.clear();
            this.wallets.addAll(list);
            adapter.notifyDataSetChanged();

            this.walletsTrade.clear();
            this.walletsTrade.addAll(list);
            calcuTotal(type, walletsTrade);
            for (Wallet wallet : walletsTrade) {
                for (Wallet entity : walletsBase) {
                    if (entity.getCoinId().equals(wallet.getCoinId())) {
                        wallet.setAddress(entity.getAddress());
                    }
                }
            }
        }
    }

    private void calcuTotal(String type, List<Wallet> coins) {
        sumTrade = 0;
        for (Wallet coin : coins) {
            sumTrade += coin.getTotalLegalAssetBalance();
        }
        if (SharedPreferencesUtil.getInstance(activity).contains(SP_KEY_SHOW)) {
            if (SharedPreferencesUtil.getInstance(activity).getBooleanParam(SP_KEY_SHOW)) {
                tvAssetMoneyTrade.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(sumTrade, BaseConstant.MONEY_FORMAT_ASSET, null)));
                ivAssetEyeTrade.setImageResource(R.mipmap.icon_eye_open);
            } else {
                tvAssetMoneyTrade.setText("********");
                ivAssetEyeTrade.setImageResource(R.mipmap.icon_eye_close);
            }
        } else {
            tvAssetMoneyTrade.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(sumTrade, BaseConstant.MONEY_FORMAT_ASSET, null)));
            ivAssetEyeTrade.setImageResource(R.mipmap.icon_eye_open);
        }
    }

    /**
     * 控制资产数额可见
     */
    private void switchSee(String type) {
        if (SharedPreferencesUtil.getInstance(activity).contains(SP_KEY_SHOW)) {
            if (SharedPreferencesUtil.getInstance(activity).getBooleanParam(SP_KEY_SHOW)) {
                SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_SHOW, false);
                tvAssetMoneyTrade.setText("********");
                tvAssetMoneyTradeBail.setText("*****");
                ivAssetEyeTrade.setImageResource(R.mipmap.icon_eye_close);
            } else {
                SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_SHOW, true);
                tvAssetMoneyTrade.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(sumTrade, BaseConstant.MONEY_FORMAT_ASSET, null)));
                tvAssetMoneyTradeBail.setText("保证金 " + MathUtils.subZeroAndDot(MathUtils.getRundNumber(sumCny, BaseConstant.MONEY_FORMAT, null)) + " " + coin);
                ivAssetEyeTrade.setImageResource(R.mipmap.icon_eye_open);
            }
        } else {
            SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_SHOW, false);
            tvAssetMoneyTrade.setText("********");
            tvAssetMoneyTradeBail.setText("*****");
            ivAssetEyeTrade.setImageResource(R.mipmap.icon_eye_close);
        }

    }

    /**
     * 解析昨日佣金数据
     *
     * @param response
     */
    @Override
    public void getTradeYesterdaySuccess(MessageResult response) {
        if (response != null && response.getData() != null) {
            Gson gson = new Gson();
            AcceptMerchantInfoEntity loginEntity = gson.fromJson(gson.toJson(response), new TypeToken<AcceptMerchantInfoEntity>() {
            }.getType());
            //昨日佣金
            String reward = MathUtils.subZeroAndDot(MathUtils.getRundNumber(loginEntity.getData().getDaysBuyReward() + loginEntity.getData().getDaysSellReward(), BaseConstant.MONEY_FORMAT, null));
            tvAssetCommissionY.setText(reward);
            String type = loginEntity.getData().getCoin();
            if (StringUtils.isNotEmpty(type)) {
                tvMoneyType.setText(type);
                tvMoneyTypeTag.setText(type);
                ivMoneyType.setImageResource(getResource(type));
            }
        }
    }

    @Override
    public void getSelfLevelInfoSuccess(AcceptMerchantFrontVo acceptMerchantFrontVo) {
        if (acceptMerchantFrontVo != null) {
            if (acceptMerchantFrontVo.getCertifiedBusinessStatus() == 1) {
                if (acceptMerchantFrontVo.getAmount() != null) {
                    sumCny = acceptMerchantFrontVo.getAmount().doubleValue();
                }
            } else {
                if (acceptMerchantFrontVo.getMargin() != null) {
                    sumCny = acceptMerchantFrontVo.getMargin().doubleValue();
                }
            }
        }
        coin = acceptMerchantFrontVo.getCoin();
        tvAssetMoneyTradeBail.setText("保证金 " + MathUtils.subZeroAndDot(MathUtils.getRundNumber(sumCny, BaseConstant.MONEY_FORMAT, null)) + " " + coin);
    }

    @Override
    public void getTradeSuccess(AcceptanceMerchantListEntity response) {
        if (response != null && response.getData() != null) {
            //累计佣金
            String reward = MathUtils.subZeroAndDot(MathUtils.getRundNumber(response.getData().getTotalBuyReward() + response.getData().getTotalSellReward(), BaseConstant.MONEY_FORMAT, null));
            tvAssetCommission.setText(reward);
            //待结算佣金
            String noSettledReward = MathUtils.subZeroAndDot(MathUtils.getRundNumber(response.getData().getNoSettledReward(), BaseConstant.MONEY_FORMAT, null));
            tvAssetCommissionUn.setText(noSettledReward);
        }
    }

    public int getResource(String imageName) {
        Context ctx = getBaseContext();
        int resId = getResources().getIdentifier(imageName.toLowerCase(), "mipmap", ctx.getPackageName());
        //如果没有在"mipmap"下找到imageName,将会返回0
        return resId;
    }

    /**
     * 提示框
     */
    private void showPayCodeDialog() {
        if (assetsRewardDescDialog == null) {
            assetsRewardDescDialog = new AssetsRewardDescDialog(activity);
            //assetsRewardDescDialog.setText(qrCodeUrl);
        }
        assetsRewardDescDialog.show();
    }

    /**
     * check uc、ac、acp成功后，通知刷新界面
     */
    public void onEvent(CheckLoginSuccessEvent event) {
        //承兑商是否认证
        if (SharedPreferencesUtil.getInstance(activity).getBooleanParam(SP_AHTH)) {
            //查询累计佣金、待结算佣金
            presenter.getTrade();
            //查询昨日佣金
            String tradingDay = DateUtils.getBackDate("yyyyMMdd", -1);
            presenter.getTradeYesterday(tradingDay);
            //获取个人等级---获取保证金
            presenter.getSelfLevelInfo();
        }
        //查询交易账户
        presenter.getWallet(COMMON);
    }

}
