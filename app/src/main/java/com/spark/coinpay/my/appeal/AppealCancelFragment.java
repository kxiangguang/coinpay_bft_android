package com.spark.coinpay.my.appeal;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.coinpay.GlobalConstant;
import com.spark.coinpay.R;
import com.spark.coinpay.adapter.AppealCancelAdapter;
import com.spark.coinpay.base.BaseFragment;
import com.spark.coinpay.event.PsListFrushEvent;
import com.spark.coinpay.my.appeal.detail.AppealDetailCancelActivity;
import com.spark.library.acp.model.AppealApplyAchiveVo;
import com.spark.modulebase.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;


/**
 * 纠纷管理-已取消
 */
public class AppealCancelFragment extends BaseFragment implements AppealContract.AppealFinishView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private int pageNo = 1;
    private AppealFinishPresenterImpl presenter;
    private List<AppealApplyAchiveVo> mDatas = new ArrayList<>();
    private AppealCancelAdapter adapter;
    public static AppealApplyAchiveVo appealApplyAchiveVo;


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
        adapter = new AppealCancelAdapter(R.layout.item_appeal, mDatas);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        presenter = new AppealFinishPresenterImpl(this);
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
                appealApplyAchiveVo = (AppealApplyAchiveVo) adapter.getItem(position);
                showActivity(AppealDetailCancelActivity.class, null, 1);
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
        map.put("status", "2");
        presenter.getOrder(isShow, map);
    }


    @Override
    public void getOrderSuccess(List<AppealApplyAchiveVo> list) {
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

    public void onEvent(PsListFrushEvent event) {
        LogUtils.e("====确认======回调刷新啦");
        if (AppealActivity.isSaleOrder) {
            reflush();
        } else {
            reflush();
        }
    }

    private void reflush() {
        adapter.setEnableLoadMore(false);
        pageNo = 1;
        getList(false);
    }
}
