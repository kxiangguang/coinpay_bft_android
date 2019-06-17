package com.spark.coinpay.my.order;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.coinpay.GlobalConstant;
import com.spark.coinpay.R;
import com.spark.coinpay.adapter.MyOrderIngAdapter;
import com.spark.coinpay.base.BaseFragment;
import com.spark.coinpay.event.OrderListFrushEvent;
import com.spark.coinpay.event.TimeDownFinishEvent;
import com.spark.coinpay.my.order.detail.MyOrderDetailIngActivity;
import com.spark.library.acp.model.OrderInTransit;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;


/**
 * 订单-进行中
 */
public class OrderIngFragment extends BaseFragment implements MyOrderContract.MyOrderView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private int pageNo = 1;
    private MyOrderPresenterImpl presenter;
    private List<OrderInTransit> mDatas = new ArrayList<>();
    private MyOrderIngAdapter adapter;
    public static OrderInTransit orderInTransit;
    private int direction;
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
        adapter = new MyOrderIngAdapter(R.layout.item_my_order_ing, mDatas);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        presenter = new MyOrderPresenterImpl(this);
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
                orderInTransit = (OrderInTransit) adapter.getItem(position);
                showActivity(MyOrderDetailIngActivity.class, null, 1);
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
        map.put("status", "1,2,5");//支付中1,支付完成2,订单超时5,
        map.put("direction", direction + "");
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
        if (mDatas.size() > 0) {
            //订单状态 0-已取消 1-未付款 2-已付款 3-已完成 4-申诉中 5.超时单
            //判断订单状态不为5.超时单，要进行倒计时
//            boolean flag = false;
//            for (OrderInTransit orderInTransit : mDatas) {
//                if (orderInTransit.getStatus() != null && orderInTransit.getStatus() != 5) {
//                    flag = true;
//                }
//            }
//            if (flag) {
//                handler_timeCurrent.sendEmptyMessageDelayed(0, tick * 1000);
//            }

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

    /**
     * 筛选，刷新第一页
     *
     * @param event
     */
    public void onEvent(OrderListFrushEvent event) {
        if (adapter != null) {
            direction = event.type;
            startFlush();
        }
    }


}
