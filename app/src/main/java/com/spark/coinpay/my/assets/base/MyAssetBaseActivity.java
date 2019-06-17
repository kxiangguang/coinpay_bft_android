package com.spark.coinpay.my.assets.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.coinpay.R;
import com.spark.coinpay.adapter.AssetRecordAdapter;
import com.spark.coinpay.adapter.WalletAdapter;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.my.assets.extract.ExtractActivity;
import com.spark.coinpay.my.assets.recharge.RechargeActivity;
import com.spark.coinpay.my.assets.transfer.TransferActivity;
import com.spark.library.ac.model.MessageResultPageMemberTransactionVo;
import com.spark.library.acp.model.AcceptMerchantFrontVo;
import com.spark.library.acp.model.AcceptMerchantTrade;
import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.MessageResultAcceptMerchantTrade;
import com.spark.moduleassets.entity.AssetRecord;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.base.BaseConstant;
import com.spark.modulebase.utils.DateUtils;
import com.spark.modulebase.utils.LogUtils;
import com.spark.modulebase.utils.MathUtils;
import com.spark.modulebase.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.spark.moduleassets.AssetsConstants.ACP;
import static com.spark.moduleassets.AssetsConstants.COMMON;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_SHOW;

/**
 * 基本账户
 */
public class MyAssetBaseActivity extends BaseActivity implements MyAssetBaseContract.MyAssetBaseView {

    @BindView(R.id.ivAssetEyeBase)
    ImageView ivAssetEyeBase;
    @BindView(R.id.tvAssetMoneyBase)
    TextView tvAssetMoneyBase;
    @BindView(R.id.llAssetMoneyBaseRecharge)
    LinearLayout llAssetMoneyBaseRecharge;
    @BindView(R.id.llAssetMoneyBaseExtract)
    LinearLayout llAssetMoneyBaseExtract;
    @BindView(R.id.llAssetMoneyBaseTransfer)
    LinearLayout llAssetMoneyBaseTransfer;
    @BindView(R.id.recyViewCion)
    RecyclerView recyViewCion;
    @BindView(R.id.ivAssetEyeTrade)
    ImageView ivAssetEyeTrade;
    @BindView(R.id.recyViewRecord)
    RecyclerView recyViewRecord;

    private MyAssetBasePresenterImpl presenter;
    private ArrayList<Wallet> walletsBase = new ArrayList<>();
    private double sumBase = 0;//基础账户金额
    private double sumTrade = 0;//交易账户金额
    private int number = BaseConstant.MONEY_FORMAT;//小数点保留位数格式化
    private ArrayList<Wallet> wallets = new ArrayList<>();
    private ArrayList<AssetRecord> wallets2 = new ArrayList<>();
    private WalletAdapter adapter;
    private AssetRecordAdapter adapter2;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_asset_base;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1: //划转
                case 2: //充值
                case 3: //提现
                    //查询基本账户
                    presenter.getWallet(COMMON);
                    //查询交易账户
                    presenter.getWallet(ACP);
                    break;
            }
        }
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new MyAssetBasePresenterImpl(this);
    }

    @Override
    protected void initRv() {
        super.initRv();
        LinearLayoutManager manager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyViewCion.setLayoutManager(manager);
        adapter = new WalletAdapter(R.layout.item_lv_wallet, wallets);
//        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
//        adapter.isFirstOnly(true);
        recyViewCion.setAdapter(adapter);

        LinearLayoutManager manager2 = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyViewRecord.setLayoutManager(manager2);
        adapter2 = new AssetRecordAdapter(R.layout.item_lv_asset_record, wallets2);
//        adapter2.openLoadAnimation(BaseQuickAdapter.SCALEIN);
//        adapter2.isFirstOnly(true);
        recyViewRecord.setAdapter(adapter2);
    }

    @Override
    protected void loadData() {
        super.loadData();
        //查询基本账户
        presenter.getWallet(COMMON);
        //查询交易账户
        presenter.getWallet(ACP);
        //获取财务记录
        presenter.getRecordList(null, COMMON);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @OnClick({R.id.llAssetMoneyBaseRecharge, R.id.llAssetMoneyBaseExtract, R.id.llAssetMoneyBaseTransfer,
            R.id.ivAssetEyeBase, R.id.ivAssetEyeTrade})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        Bundle bundle = null;
        switch (v.getId()) {
            case R.id.llAssetMoneyBaseRecharge://基础账户-充币
                bundle = new Bundle();
                bundle.putSerializable("wallets", walletsBase);
                showActivity(RechargeActivity.class, bundle, 2);
                break;
            case R.id.llAssetMoneyBaseExtract://基础账户-提币
                bundle = new Bundle();
                bundle.putSerializable("wallets", walletsBase);
                showActivity(ExtractActivity.class, bundle, 3);
                break;
            case R.id.llAssetMoneyBaseTransfer://基础账户-划转
                bundle = new Bundle();
                bundle.putBoolean("isFromBaseToTrade", true);
                bundle.putDouble("baseBalance", sumBase);
                bundle.putDouble("tradeBalance", sumTrade);
                bundle.putSerializable("wallets", walletsBase);
                showActivity(TransferActivity.class, bundle, 1);
                break;
            case R.id.ivAssetEyeBase://基础账户-查看
                switchSee(COMMON);
                break;
            case R.id.ivAssetEyeTrade://筛选

                break;
        }
    }

    @Override
    public void getWalletSuccess(String type, List<Wallet> list) {
        if (type.equals(COMMON)) {
            this.wallets.clear();
            this.wallets.addAll(list);
            adapter.notifyDataSetChanged();
            this.walletsBase.clear();
            this.walletsBase.addAll(list);
            calcuTotal(type, wallets);
        } else if (type.equals(ACP)) {
            calcuTotal(type, list);
        }
    }

    private void calcuTotal(String type, List<Wallet> coins) {
        if (type.equals(COMMON)) {
            sumBase = 0;
            for (Wallet coin : coins) {
                sumBase += coin.getBalance();
            }
            if (SharedPreferencesUtil.getInstance(activity).getBooleanParam(SP_KEY_SHOW)) {
                tvAssetMoneyBase.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(sumBase, number, null)));
                ivAssetEyeBase.setImageResource(R.mipmap.icon_eye_close);
            } else {
                tvAssetMoneyBase.setText("********");
                ivAssetEyeBase.setImageResource(R.mipmap.icon_eye_open);
            }
        } else if (type.equals(ACP)) {
            sumTrade = 0;
            for (Wallet coin : coins) {
                sumTrade += coin.getBalance();
            }
        }
    }

    /**
     * 控制资产数额可见
     */
    private void switchSee(String type) {
        if (type.equals(COMMON)) {
            if (SharedPreferencesUtil.getInstance(activity).getBooleanParam(SP_KEY_SHOW)) {
                SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_SHOW, false);
                tvAssetMoneyBase.setText("********");
                ivAssetEyeBase.setImageResource(R.mipmap.icon_eye_open);

                ivAssetEyeTrade.setImageResource(R.mipmap.icon_eye_open);
            } else {
                SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_SHOW, true);
                tvAssetMoneyBase.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(sumBase, number, null)));
                ivAssetEyeBase.setImageResource(R.mipmap.icon_eye_close);

                ivAssetEyeTrade.setImageResource(R.mipmap.icon_eye_close);
            }
        }

    }


    @Override
    public void getRecordListSuccess(List<AssetRecord> list) {
        this.wallets2.clear();
        this.wallets2.addAll(list);
        adapter2.notifyDataSetChanged();
    }

}
