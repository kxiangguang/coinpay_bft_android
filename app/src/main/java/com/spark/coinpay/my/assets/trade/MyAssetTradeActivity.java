package com.spark.coinpay.my.assets.trade;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.spark.coinpay.R;
import com.spark.coinpay.adapter.AssetRecordAdapter;
import com.spark.coinpay.adapter.WalletAdapter;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.my.assets.extract.ExtractActivity;
import com.spark.coinpay.my.assets.recharge.RechargeActivity;
import com.spark.library.acp.model.AcceptMerchantFrontVo;
import com.spark.moduleassets.entity.AssetRecord;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.base.BaseConstant;
import com.spark.modulebase.utils.MathUtils;
import com.spark.modulebase.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.spark.moduleassets.AssetsConstants.ACP;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_AHTH;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_SHOW;

/**
 * 交易账户
 */
public class MyAssetTradeActivity extends BaseActivity implements MyAssetTradeContract.MyAssetTradeView {

    @BindView(R.id.ivAssetEyeTrade)
    ImageView ivAssetEyeTrade;
    @BindView(R.id.tvAssetMoneyTrade)
    TextView tvAssetMoneyTrade;
    @BindView(R.id.tvAssetMoneyTradeBail)
    TextView tvAssetMoneyTradeBail;
    @BindView(R.id.recyViewCion)
    RecyclerView recyViewCion;
    @BindView(R.id.ivAssetTrade)
    ImageView ivAssetTrade;
    @BindView(R.id.recyViewRecord)
    RecyclerView recyViewRecord;
    @BindView(R.id.llAssetMoneyTradeRecharge)
    LinearLayout llAssetMoneyTradeRecharge;
    @BindView(R.id.llAssetMoneyTradeExtract)
    LinearLayout llAssetMoneyTradeExtract;

    private MyAssetTradePresenterImpl presenter;
    private ArrayList<Wallet> walletsTrade = new ArrayList<>();
    private double sumTrade = 0;//交易账户金额
    private double sumCny = 0;//保证金金额
    private int number = BaseConstant.MONEY_FORMAT;//小数点保留位数格式化
    private ArrayList<Wallet> wallets = new ArrayList<>();
    private ArrayList<AssetRecord> wallets2 = new ArrayList<>();
    private WalletAdapter adapter;
    private AssetRecordAdapter adapter2;
    private Integer[] types = {null, 1, 2, 3, 4, 5, 6, 7, 8};

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_asset_trade;
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
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new MyAssetTradePresenterImpl(this);
    }

    @Override
    protected void initRv() {
        super.initRv();
        LinearLayoutManager manager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyViewCion.setLayoutManager(manager);
        adapter = new WalletAdapter(R.layout.item_lv_wallet, wallets);
        recyViewCion.setAdapter(adapter);

        LinearLayoutManager manager2 = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyViewRecord.setLayoutManager(manager2);
        adapter2 = new AssetRecordAdapter(R.layout.item_lv_asset_record, wallets2);
        recyViewRecord.setAdapter(adapter2);
    }

    @Override
    protected void loadData() {
        super.loadData();
        //承兑商是否认证
        if (SharedPreferencesUtil.getInstance(activity).getBooleanParam(SP_AHTH)) {
            //获取个人等级---获取保证金
            presenter.getSelfLevelInfo();
        }
        //查询交易账户
        presenter.getWallet(ACP);
        //获取财务记录
        presenter.getRecordList(null, ACP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @OnClick({R.id.ivAssetEyeTrade, R.id.ivAssetTrade, R.id.llAssetMoneyTradeRecharge, R.id.llAssetMoneyTradeExtract})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        Bundle bundle = null;
        switch (v.getId()) {
            case R.id.ivAssetEyeTrade://查看
                switchSee(ACP);
                break;
            case R.id.ivAssetTrade://筛选
                showOrderDialog();
                break;
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
        }
    }

    @Override
    public void getWalletSuccess(String type, List<Wallet> list) {
        this.wallets.clear();
        this.wallets.addAll(list);
        adapter.notifyDataSetChanged();
        this.walletsTrade.clear();
        this.walletsTrade.addAll(list);
        calcuTotal(type, wallets);
    }

    private void calcuTotal(String type, List<Wallet> coins) {
        sumTrade = 0;
        for (Wallet coin : coins) {
            sumTrade += coin.getBalance();
        }
        if (SharedPreferencesUtil.getInstance(activity).getBooleanParam(SP_KEY_SHOW)) {
            tvAssetMoneyTrade.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(sumTrade, number, null)));
            ivAssetEyeTrade.setImageResource(R.mipmap.icon_eye_close);
        } else {
            tvAssetMoneyTrade.setText("********");
            ivAssetEyeTrade.setImageResource(R.mipmap.icon_eye_open);
        }
    }

    /**
     * 控制资产数额可见
     */
    private void switchSee(String type) {
        if (SharedPreferencesUtil.getInstance(activity).getBooleanParam(SP_KEY_SHOW)) {
            SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_SHOW, false);
            tvAssetMoneyTrade.setText("********");
            tvAssetMoneyTradeBail.setText("*****");
            ivAssetEyeTrade.setImageResource(R.mipmap.icon_eye_open);
        } else {
            SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_SHOW, true);
            tvAssetMoneyTrade.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(sumTrade, number, null)));
            tvAssetMoneyTradeBail.setText("保证金 " + MathUtils.subZeroAndDot(MathUtils.getRundNumber(sumCny, BaseConstant.MONEY_FORMAT, null)) + " ");
            ivAssetEyeTrade.setImageResource(R.mipmap.icon_eye_close);
        }
    }


    @Override
    public void getRecordListSuccess(List<AssetRecord> list) {
        this.wallets2.clear();
        this.wallets2.addAll(list);
        adapter2.notifyDataSetChanged();
    }

    @Override
    public void getSelfLevelInfoSuccess(AcceptMerchantFrontVo acceptMerchantFrontVo) {
        if (acceptMerchantFrontVo != null && acceptMerchantFrontVo.getMargin() != null) {
            sumCny = acceptMerchantFrontVo.getMargin().doubleValue();
        }
        tvAssetMoneyTradeBail.setText("保证金 " + MathUtils.subZeroAndDot(MathUtils.getRundNumber(sumCny, BaseConstant.MONEY_FORMAT, null)) + " ");
    }

    /**
     * 选择交易类型
     */
    private void showOrderDialog() {
        final String[] stringItems = getResources().getStringArray(R.array.trade_type);
        final ActionSheetDialog dialog = new ActionSheetDialog(activity, stringItems, null);
        dialog.isTitleShow(false).itemTextColor(getResources().getColor(R.color.font_main_title))
                .cancelText(getResources().getColor(R.color.font_main_content))
                .cancelText(getResources().getString(R.string.str_cancel)).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                Integer takingType = types[position];
                //获取财务记录
                presenter.getRecordList(takingType, ACP);
            }
        });
    }
}
