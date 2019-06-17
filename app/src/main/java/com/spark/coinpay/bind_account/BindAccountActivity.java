package com.spark.coinpay.bind_account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.coinpay.GlobalConstant;
import com.spark.coinpay.R;
import com.spark.coinpay.adapter.PayWayAdapter;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.entity.PayWaySetting;
import com.spark.coinpay.event.CheckLoginSuccessEvent;
import com.spark.coinpay.view.DeleteDialog;
import com.spark.library.acp.model.MessageResult;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.modulebase.base.BaseConstant.SUCCESS_CODE;

/**
 * 收款方式
 */
public class BindAccountActivity extends BaseActivity implements BindAccountContract.BindAccountView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private ArrayList<PayWaySetting> mDatas = new ArrayList<>();
    private BindAccountPresenterImpl presenter;
    private PayWayAdapter adapter;
    private boolean isMain = false;//是否首页进入该界面
    private DeleteDialog deleteDialog;
    private static final int ADD_PAY_WAY_REQ = 1;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_bind_account;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (isMain) {
                setResult(RESULT_OK);
                finish();
            } else {
                if (requestCode == ADD_PAY_WAY_REQ) {
                    presenter.queryPayWayList();
                }
            }
        }
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        ivMessage.setVisibility(View.VISIBLE);
        ivMessage.setImageResource(R.mipmap.icon_add);
        tvGoto.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.str_rec_way);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isMain = bundle.getBoolean("isMain", false);
        }
    }

    @Override
    protected void initRv() {
        super.initRv();
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(manager);
        adapter = new PayWayAdapter(R.layout.view_item_setting, mDatas);
        adapter.bindToRecyclerView(recyclerView);
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new BindAccountPresenterImpl(this);
    }

    /**
     * 获取列表
     */
    private void getList(boolean isShow) {
        presenter.queryPayWayList();
    }

    @Override
    protected void loadData() {
        super.loadData();
        getList(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @OnClick({R.id.ivMessage})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        Bundle bundle = null;
        switch (v.getId()) {
            case R.id.ivMessage://筛选
                showOrderDialog();
                break;
        }
    }


    @Override
    public void queryPayWayListSuccess(String response) {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        adapter.setEnableLoadMore(true);
        adapter.loadMoreComplete();
        try {
            JSONObject jsonObject = new JSONObject(response);
            String payWayLists = jsonObject.optString("data");
            List<PayWaySetting> list = new Gson().fromJson(payWayLists, new TypeToken<List<PayWaySetting>>() {
            }.getType());
            if (list == null) return;
            mDatas.clear();
            if (list.size() == 0) {
                adapter.loadMoreEnd();
                adapter.setEmptyView(R.layout.empty_layout);
            } else {
                mDatas.addAll(list);
            }
            adapter.notifyDataSetChanged();
            adapter.disableLoadMoreIfNotFullPage();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 选择交易类型
     */
    private void showOrderDialog() {
        final String[] stringItems = getResources().getStringArray(R.array.pay_type);
        final ActionSheetDialog dialog = new ActionSheetDialog(activity, stringItems, null);
        dialog.isTitleShow(false).itemTextColor(getResources().getColor(R.color.font_main_title))
                .cancelText(getResources().getColor(R.color.font_main_content))
                .cancelText(getResources().getString(R.string.str_cancel)).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                selectType(position);
            }
        });
    }

    private void selectType(int type) {
        Bundle bundle = new Bundle();
        switch (type) {
            case 0:
                bundle = new Bundle();
                bundle.putString("payWay", GlobalConstant.ALIPAY);
                showActivity(BindAliActivity.class, bundle, ADD_PAY_WAY_REQ);
                break;
            case 1:
                bundle = new Bundle();
                bundle.putString("payWay", GlobalConstant.WECHAT);
                showActivity(BindAliActivity.class, bundle, ADD_PAY_WAY_REQ);
                break;
            case 2:
                showActivity(BindBankActivity.class, null, ADD_PAY_WAY_REQ);
                break;
            case 3:
                showActivity(BindPayPalActivity.class, null, ADD_PAY_WAY_REQ);
                break;
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setEnableLoadMore(false);
                getList(false);
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PayWaySetting payWaySetting = (PayWaySetting) adapter.getItem(position);
                String payType = payWaySetting.getPayType();
                Bundle bundle = new Bundle();
                switch (payType) {
                    case GlobalConstant.ALIPAY:
                        bundle = new Bundle();
                        bundle.putSerializable("data", payWaySetting);
                        bundle.putString("payWay", GlobalConstant.ALIPAY);
                        showActivity(BindAliActivity.class, bundle, ADD_PAY_WAY_REQ);
                        break;
                    case GlobalConstant.WECHAT:
                        bundle = new Bundle();
                        bundle.putSerializable("data", payWaySetting);
                        bundle.putString("payWay", GlobalConstant.WECHAT);
                        showActivity(BindAliActivity.class, bundle, ADD_PAY_WAY_REQ);
                        break;
                    case GlobalConstant.BANK:
                        bundle = new Bundle();
                        bundle.putSerializable("data", payWaySetting);
                        showActivity(BindBankActivity.class, bundle, ADD_PAY_WAY_REQ);
                        break;
                    case GlobalConstant.PAYPAL:
                        bundle = new Bundle();
                        bundle.putSerializable("data", payWaySetting);
                        showActivity(BindPayPalActivity.class, bundle, ADD_PAY_WAY_REQ);
                        break;
                }
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ivStatus:
                        PayWaySetting orderInTransit = (PayWaySetting) adapter.getItem(position);
                        if (orderInTransit.getStatus() == 1) {
                            presenter.doUpdateStatusBank(orderInTransit.getId(), 0);
                        } else {
                            presenter.doUpdateStatusBank(orderInTransit.getId(), 1);
                        }
                        break;
                }
            }
        });

        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                PayWaySetting payWaySetting = (PayWaySetting) adapter.getItem(position);
                showReleaseDialog(payWaySetting.getId());
                return true;
            }
        });

    }


    @Override
    public void updateSuccess(MessageResult response) {
        if (response != null) {
            if (response.getCode() == SUCCESS_CODE) {
                presenter.queryPayWayList();
            } else {
                ToastUtils.showToast(activity, response.getMessage());
            }
        }
    }

    /**
     * 提示框-删除
     */
    private void showReleaseDialog(final Long id) {
        deleteDialog = new DeleteDialog(activity);
        deleteDialog.setPositiveOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = StringUtils.getText(deleteDialog.getPwdEditText());
                if (StringUtils.isEmpty(pwd)) {
                    ToastUtils.showToast(activity, getString(R.string.str_please_input_assets_pwd));
                } else if (pwd.length() != 6) {
                    ToastUtils.showToast(activity, getString(R.string.text_money_pwd_tag));
                } else {
                    presenter.doDeleteBank(id, pwd);
                    deleteDialog.dismiss();
                }

            }
        });
        deleteDialog.show();
    }

    /**
     * check uc、ac、acp成功后，通知刷新界面
     */
    public void onEvent(CheckLoginSuccessEvent event) {
        presenter.queryPayWayList();
    }
}
