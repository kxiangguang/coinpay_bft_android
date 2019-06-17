package com.spark.coinpay.main.buy.order;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.spark.coinpay.event.TimeDownFinishEvent;
import com.spark.coinpay.main.buy.BuyUnPayActivity;
import com.spark.coinpay.main.buy.PayEntity;
import com.spark.library.acp.model.MessageResult;
import com.spark.moduleassets.entity.ExtractInfo;
import com.spark.modulebase.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;


/**
 * 订单-进行中
 */
public class OrderIngFragment extends BuyOrderFragment implements MyOrderContract.MyOrderView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private int pageNo = 1;
    private MyOrderPresenterImpl presenter;
    private List<BuyOrderEntity.RecordsBean> mDatas = new ArrayList<>();
    private BuyOrderIngAdapter adapter;
    private int tick = 1;//刷新列表间隔

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1://刷新数据
                    startFlush();
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
        stopFlush();
    }

    @Override
    protected void setListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startFlush();
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
        map.put("queryType", "0");
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
        if (mDatas.size() > 0) {
            handler_timeCurrent.sendEmptyMessageDelayed(0, tick * 1000);
        } else {
            stopFlush();
        }
    }


    //这里很重要，使用Handler的延时效果，每隔一秒刷新一下适配器，以此产生倒计时效果
    private Handler handler_timeCurrent = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            adapter.notifyDataSetChanged();
            handler_timeCurrent.sendEmptyMessageDelayed(0, tick * 1000);
        }
    };

    /**
     * 有倒计时结束订单，刷新第一页
     *
     * @param event
     */
    public void onEvent(TimeDownFinishEvent event) {
        if (adapter != null) {
            if (event.randomTime < 0) {//倒计时为负数时，每隔10秒刷新一次数据
                tick = 10;
                handler_timeCurrent.removeCallbacksAndMessages(null);
                handler_timeCurrent.sendEmptyMessageDelayed(0, tick * 1000);
            } else {
                startFlush();
            }
        }
    }

    /**
     * 停止刷新
     */
    private void stopFlush() {
        handler_timeCurrent.removeCallbacksAndMessages(null);
    }

    /**
     * 开始刷新
     */
    private void startFlush() {
        adapter.setEnableLoadMore(false);
        pageNo = 1;
        getList(false);
        stopFlush();
    }


}
