package com.spark.coinpay.overtime_order;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.coinpay.GlobalConstant;
import com.spark.coinpay.R;
import com.spark.coinpay.adapter.OverTimeAdapter;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.view.AlertIosDialog;
import com.spark.coinpay.view.ReleaseDialog;
import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.OrderInTransit;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 超时单列表
 */

public class OverTimeOrderListActivity extends BaseActivity implements OverTimeOrderContract.MyOrderView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private int pageNo = 1;
    private OrerTimeOrderPresenterImpl presenter;
    private List<OrderInTransit> mDatas = new ArrayList<>();
    public static OrderInTransit orderInTransit;
    private OverTimeAdapter adapter;
    private ReleaseDialog releaseDialog;
    private AlertIosDialog alertIosDialog;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_common_listview2;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.str_out_time));
    }

    @Override
    protected void initRv() {
        super.initRv();
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(manager);
        adapter = new OverTimeAdapter(R.layout.item_overtime_order, mDatas);
        adapter.bindToRecyclerView(recyclerView);
    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setEnableLoadMore(false);
                pageNo = 1;
                getList(false);
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                orderInTransit = (OrderInTransit) adapter.getItem(position);
                showActivity(OverTimeOrderDetailActivity.class, null, 1);
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                refreshLayout.setEnabled(false);
                pageNo = pageNo + 1;
                getList(false);
            }
        }, recyclerView);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tvRelease:
                        orderInTransit = (OrderInTransit) adapter.getItem(position);
                        showReleaseDialog();
                        break;
                    case R.id.ivPayReferCopy:
                        orderInTransit = (OrderInTransit) adapter.getItem(position);
                        copyText(orderInTransit.getPayRefer());
                        break;
                }
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new OrerTimeOrderPresenterImpl(this);
    }


    @Override
    protected void loadData() {
        getList(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    /**
     * 获取列表
     */
    private void getList(boolean isShow) {
        HashMap<String, String> map = new HashMap<>();
        map.put("pageNo", pageNo + "");
        map.put("pageSize", GlobalConstant.PageSize + "");
        map.put("status", "5");
        presenter.getOrder(isShow, map);
    }


    @Override
    public void getOrderSuccess(List<OrderInTransit> list) {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        adapter.setEnableLoadMore(true);
        adapter.loadMoreComplete();
        if (list == null) return;
        if (pageNo == 1) {
            mDatas.clear();
            if (list.size() == 0) {
                adapter.loadMoreEnd();
                adapter.setEmptyView(R.layout.empty_layout);
                adapter.notifyDataSetChanged();
            } else {
                mDatas.addAll(list);
            }
        } else {
            if (list.size() != 0) mDatas.addAll(list);
            else adapter.loadMoreEnd();
        }
        adapter.notifyDataSetChanged();
        adapter.disableLoadMoreIfNotFullPage();
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
                    presenter.releaseOrder(orderInTransit.getOrderId(), pwd);
                    releaseDialog.dismiss();
                }
            }
        });
        releaseDialog.show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1://刷新数据
                    adapter.setEnableLoadMore(false);
                    pageNo = 1;
                    getList(false);
                    break;
            }
        }
    }


}
