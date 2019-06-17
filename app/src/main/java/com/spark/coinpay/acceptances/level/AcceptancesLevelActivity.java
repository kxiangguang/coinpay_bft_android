package com.spark.coinpay.acceptances.level;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.coinpay.R;
import com.spark.coinpay.acceptances.authentication.AuthenticationActivity;
import com.spark.coinpay.acceptances.detail.ApplySurrenderActivity;
import com.spark.coinpay.acceptances.process.AcceptancesProcessActivity;
import com.spark.coinpay.adapter.LevelAdapter;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.view.AlertIosDialog;
import com.spark.library.acp.model.AcceptMerchantApplyMarginType;
import com.spark.library.acp.model.AcceptMerchantFrontVo;
import com.spark.library.acp.model.Dict;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.entity.HttpErrorEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.moduleassets.AssetsConstants.ACP;

/**
 * 承兑商招募
 */
public class AcceptancesLevelActivity extends BaseActivity implements AcceptancesLevelContract.AcceptancesLevelView {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.llCurrent)
    LinearLayout llCurrent;
    @BindView(R.id.llCd)
    LinearLayout llCd;
    @BindView(R.id.block)
    View block;
    @BindView(R.id.rlCurrentLevel)
    RelativeLayout rlCurrentLevel;
    @BindView(R.id.tvCurrentLevel)
    TextView tvCurrentLevel;
    @BindView(R.id.tvCurrentRea)
    TextView tvCurrentRea;
    @BindView(R.id.tvCurrentLevelInfo)
    TextView tvCurrentLevelInfo;

    private AcceptancesLevelPresenterImpl presenter;
    private List<Dict> dictList;
    private LevelAdapter levelAdapter;
    private boolean isMain = false;//是否首页进入该界面
    private boolean isAuthed = false;//是否已认证
    private String authKey = "";//认证商家key
    private AlertIosDialog alertIosDialog;
    private double sumTrade = 0;//交易账户金额
    private int status;//认证状态
    private Long id;//重新认证id
    private String reason;//申请退保原因

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    if (isMain) {
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        presenter.getSelfLevelInfo();
                    }
                    break;
            }
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.acitivty_acceptances_level;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.str_cd_level));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isMain = bundle.getBoolean("isMain", false);
        }
    }

    @Override
    protected void initRv() {
        super.initRv();
        presenter = new AcceptancesLevelPresenterImpl(this);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(manager);
        dictList = new ArrayList<>();

        levelAdapter = new LevelAdapter(R.layout.view_item_level, dictList);
        recyclerView.setAdapter(levelAdapter);
    }

    @Override
    protected void loadData() {
        super.loadData();
        presenter.getLevelList();
    }

    @OnClick({R.id.llCurrent})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.llCurrent:
                //showActivity(LevelDetailActivity.class, null, 1);
                if (status == 2) {//2：认证-审核成功
                    Bundle bundle = new Bundle();
                    bundle.putString("reason", "");
                    bundle.putInt("status", status);
                    showActivity(ApplySurrenderActivity.class, null, 1);
                } else if (status == 3) {//3：认证-审核失败
                    if (id != 0) {//重新认证
                        Bundle bundle = new Bundle();
                        bundle.putLong("id", id);
                        showActivity(AuthenticationActivity.class, bundle, 1);
                    }
                } else if (status == 6) {//6：退保-审核失败
                    Bundle bundle = new Bundle();
                    bundle.putString("reason", reason);
                    bundle.putInt("status", status);
                    showActivity(ApplySurrenderActivity.class, bundle, 1);
                }
                break;
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        levelAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Dict dict = (Dict) adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString("key", dict.getKey());
                if (isAuthed) {//已认证
                    if (!dict.getKey().equals(authKey)) {
                        showAlerDialog();
                    }
                } else {//未认证
                    bundle.putDouble("buy", sumTrade);
                    showActivity(AcceptancesProcessActivity.class, bundle, 1);
                }

            }
        });
    }

    @Override
    public void getLevelListSuccess(List<Dict> list) {
        if (list != null) {
            this.dictList.clear();
            this.dictList.addAll(list);
            levelAdapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
            //获取个人等级
            presenter.getSelfLevelInfo();
            //查询交易账户-校验交易账户金额
            presenter.getWallet(ACP);
        }
    }

    @Override
    public void getLevelListError(HttpErrorEntity httpErrorEntity) {
        dealError(httpErrorEntity);
    }

    @Override
    public void getSelfLevelInfoSuccess(AcceptMerchantFrontVo acceptMerchantFrontVo) {
        status = acceptMerchantFrontVo.getCertifiedBusinessStatus();
        //承兑商认证状态 0：未认证 1：认证-待审核 2：认证-审核成功 3：认证-审核失败 5：退保-待审核 6：退保-审核失败 7:退保-审核成功 8:退保-已退还保证金
        if (status == 2) {
            tvCurrentRea.setText("认证通过");
        } else if (status == 0) {
            tvCurrentRea.setText("未认证");
        } else if (status == 1) {
            tvCurrentRea.setText("审核中");
        } else if (status == 3) {
            tvCurrentRea.setText("认证不通过");
        } else if (status == 5) {
            tvCurrentRea.setText("退保-审核中");
        } else if (status == 6) {
            tvCurrentRea.setText("退保-审核不通过");
            reason = "";
        } else if (status == 7) {
            tvCurrentRea.setText("退保-审核通过");
        } else if (status == 8) {
            tvCurrentRea.setText("退保-已退还保证金");
        }
        llCurrent.setVisibility(View.VISIBLE);
        block.setVisibility(View.VISIBLE);
        for (Dict dict : dictList) {
            if (acceptMerchantFrontVo.getMerchantType().equals(dict.getKey())) {
                tvCurrentLevel.setText(dict.getValue());
                tvCurrentLevelInfo.setText(dict.getDescript());
                dict.setStatus(99);
                isAuthed = true;
                authKey = dict.getKey();
                levelAdapter.notifyDataSetChanged();
                break;
            }
        }

       int merchantType = Integer.parseInt(acceptMerchantFrontVo.getMerchantType());
        presenter.getAcceptancesProcessInfo(merchantType);
    }

    @Override
    public void getSelfLevelInfoError(HttpErrorEntity httpErrorEntity) {
        if (httpErrorEntity.getCode() == 30548) {
            llCurrent.setVisibility(View.GONE);
            block.setVisibility(View.GONE);
            return;
        }
        dealError(httpErrorEntity);
    }

    /**
     * 确定对话框
     */
    private void showAlerDialog() {
        if (alertIosDialog == null) {
            alertIosDialog = new AlertIosDialog(activity);
            alertIosDialog.withWidthScale(0.8f);
            alertIosDialog.setImg(0).setContent(getString(R.string.str_auth_sure_tag)).setTag("");
            alertIosDialog.setPositiveClickLister(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertIosDialog.dismiss();
                }
            });
        }
        alertIosDialog.show();
    }

    @Override
    public void getWalletSuccess(String type, List<Wallet> list) {
        sumTrade = 0;
        for (Wallet coin : list) {
            sumTrade += coin.getBalance();
        }
    }

    @Override
    public void getAcceptancesProcessInfoSuccess(AcceptMerchantApplyMarginType acceptMerchantApplyMarginType) {
        if (acceptMerchantApplyMarginType != null) {
            id = acceptMerchantApplyMarginType.getId();
        }
    }

}
