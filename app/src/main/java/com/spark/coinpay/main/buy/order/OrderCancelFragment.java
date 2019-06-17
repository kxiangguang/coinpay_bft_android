package com.spark.coinpay.main.buy.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.coinpay.GlobalConstant;
import com.spark.coinpay.R;
import com.spark.coinpay.adapter.BuyOrderIngAdapter;
import com.spark.coinpay.main.buy.PayEntity;
import com.spark.library.acp.model.MessageResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;


/**
 * 订单-已取消
 */
public class OrderCancelFragment extends BuyOrderFragment implements MyOrderContract.MyOrderView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private int pageNo = 1;
    private MyOrderPresenterImpl presenter;
    private List<BuyOrderEntity.RecordsBean> mDatas = new ArrayList<>();
    private BuyOrderIngAdapter adapter;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        adapter = new BuyOrderIngAdapter(R.layout.item_buy_order_ing, mDatas);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        presenter = new MyOrderPresenterImpl(this);
    }

    @Override
    protected void loadData() {
        pageNo = 1;
        getList(false);
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
                buyOrderEntity = (BuyOrderEntity.RecordsBean) adapter.getItem(position);
                PayEntity payEntity = new Gson().fromJson(new Gson().toJson(buyOrderEntity), new TypeToken<PayEntity>() {
                }.getType());
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", payEntity);
                String actualPayment = payEntity.getActualPayment() == null ? "" : payEntity.getActualPayment().toString();
                bundle.putString("actualPayment", actualPayment);
                showActivity(BuyOrderDetailIngActivity.class, bundle, 1);
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
        map.put("queryType", "2");
        presenter.getOrder(isShow, map);
    }


    @Override
    public void getOrderSuccess(MessageResult response) {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        adapter.setEnableLoadMore(true);
        adapter.loadMoreComplete();
        String gson = new Gson().toJson(response.getData());
        BuyOrderEntity payEntity = new Gson().fromJson(gson, BuyOrderEntity.class);
        List<BuyOrderEntity.RecordsBean> list = payEntity.getRecords();
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
