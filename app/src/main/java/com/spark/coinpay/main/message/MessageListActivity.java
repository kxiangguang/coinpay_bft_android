package com.spark.coinpay.main.message;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.coinpay.GlobalConstant;
import com.spark.coinpay.R;
import com.spark.coinpay.adapter.MessageListAdapter;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.overtime_order.OverTimeOrderDetailActivity;
import com.spark.coinpay.view.ReleaseDialog;
import com.spark.library.acp.model.OrderInTransit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 消息中心列表
 */

public class MessageListActivity extends BaseActivity implements MessageListContract.MessageListView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private int pageNo = 1;
    private MessageListPresenterImpl presenter;
    private List<OrderInTransit> mDatas = new ArrayList<>();
    private MessageListAdapter adapter;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_common_listview2;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.str_message));
    }

    @Override
    protected void initRv() {
        super.initRv();
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(manager);
        adapter = new MessageListAdapter(R.layout.item_message, mDatas);
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
//        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                orderInTransit = (OrderInTransit) adapter.getItem(position);
//                showActivity(OverTimeOrderDetailActivity.class, null, 1);
//            }
//        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                refreshLayout.setEnabled(false);
                pageNo = pageNo + 1;
                getList(false);
            }
        }, recyclerView);
//        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                switch (view.getId()) {
//                    case R.id.tvRelease:
//                        orderInTransit = (OrderInTransit) adapter.getItem(position);
//                        showReleaseDialog();
//                        break;
//                    case R.id.ivPayReferCopy:
//                        orderInTransit = (OrderInTransit) adapter.getItem(position);
//                        copyText(orderInTransit.getPayRefer());
//                        break;
//                }
//            }
//        });

    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new MessageListPresenterImpl(this);
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
//        presenter.getOrder(isShow, map);
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

}
