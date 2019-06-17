package com.spark.coinpay.my.order;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.coinpay.GlobalConstant;
import com.spark.coinpay.R;
import com.spark.coinpay.adapter.MyOrderFinishAdapter;
import com.spark.coinpay.base.BaseFragment;
import com.spark.coinpay.event.OrderListFrushEvent;
import com.spark.coinpay.my.order.detail.MyOrderDetailFinishActivity;
import com.spark.coinpay.my.order.detail.MyOrderDetailIngActivity;
import com.spark.library.acp.model.OrderAchive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;


/**
 * 订单-已完成
 */
public class OrderFinishFragment extends BaseFragment implements MyOrderContract.MyOrderFinishView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private int pageNo = 1;
    private MyOrderFinishPresenterImpl presenter;
    private List<OrderAchive> mDatas = new ArrayList<>();
    private MyOrderFinishAdapter adapter;
    public static OrderAchive orderAchive;
    private int direction;

    @Override
    public int getFragmentLayoutId() {
        return R.layout.activity_common_listview;
    }

    @Override
    public String getmTag() {
        return this.getClass().getSimpleName();
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyOrderFinishAdapter(R.layout.item_my_order_ing, mDatas);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        presenter = new MyOrderFinishPresenterImpl(this);
    }

    @Override
    protected void loadData() {
        pageNo = 1;
        getList(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @Override
    protected void setListener() {
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
                orderAchive = (OrderAchive) adapter.getItem(position);
                showActivity(MyOrderDetailFinishActivity.class, null, 1);
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
    }


    /**
     * 获取列表
     */
    private void getList(boolean isShow) {
        HashMap<String, String> map = new HashMap<>();
        map.put("pageNo", pageNo + "");
        map.put("pageSize", GlobalConstant.PageSize + "");
        map.put("status", "3,4,7,10");//订单完成3,胜诉赔付4,卖币订单超时放行7,平台强制放行10
        map.put("direction", direction + "");
        presenter.getOrder(isShow, map);
    }


    @Override
    public void getOrderSuccess(List<OrderAchive> list) {
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
     * 筛选，刷新第一页
     *
     * @param event
     */
    public void onEvent(OrderListFrushEvent event) {
        if (adapter != null) {
            direction = event.type;
            adapter.setEnableLoadMore(false);
            pageNo = 1;
            getList(false);
        }
    }

}
