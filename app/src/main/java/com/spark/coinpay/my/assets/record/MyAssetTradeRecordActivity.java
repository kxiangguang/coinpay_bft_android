package com.spark.coinpay.my.assets.record;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.spark.coinpay.GlobalConstant;
import com.spark.coinpay.R;
import com.spark.coinpay.adapter.AssetRecordAdapter;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.event.CheckLoginSuccessEvent;
import com.spark.coinpay.my.assets.extract.ExtractActivity;
import com.spark.coinpay.my.assets.recharge.RechargeActivity;
import com.spark.moduleassets.entity.AssetRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.moduleassets.AssetsConstants.ACP;

/**
 * 资产明细
 */
public class MyAssetTradeRecordActivity extends BaseActivity implements MyAssetTradeRecordContract.MyAssetTradeView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private int pageNo = 1;
    private MyAssetTradeRecordPresenterImpl presenter;
    private ArrayList<AssetRecord> mDatas = new ArrayList<>();
    private AssetRecordAdapter adapter;
    private Integer[] subTypes = {null, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private Integer[] types = {null, 5, 6};

    Integer type;
    Integer subType;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_common_listview2;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.str_assets_record_t));
        ivMessage.setVisibility(View.VISIBLE);
        ivMessage.setImageResource(R.mipmap.icon_select);
        tvGoto.setVisibility(View.GONE);
    }

    @Override
    protected void initRv() {
        super.initRv();
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(manager);
        adapter = new AssetRecordAdapter(R.layout.item_lv_asset_record, mDatas);
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
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                refreshLayout.setEnabled(false);
                pageNo = pageNo + 1;
                getList(false);
            }
        }, recyclerView);

    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new MyAssetTradeRecordPresenterImpl(this);
    }


    /**
     * 获取列表
     */
    private void getList(boolean isShow) {
        HashMap<String, String> map = new HashMap<>();
        map.put("pageNo", pageNo + "");
        map.put("pageSize", GlobalConstant.PageSize + "");
        presenter.getRecordList(isShow, type, subType, map, ACP);
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
    public void getRecordListSuccess(List<AssetRecord> list) {
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
                HashMap<String, String> map = new HashMap<>();
                pageNo = 1;
                map.put("pageNo", pageNo + "");
                map.put("pageSize", GlobalConstant.PageSize + "");
                subType = subTypes[position];
                if (subType != null) {
                    if (subType == 1 || subType == 3 || subType == 4 || subType == 7 || subType == 9) {
                        type = 5;
                        if (subType == 7) {
                            type = 2;
                            subType = null;
                        }
                    } else if (subType == 2 || subType == 5 || subType == 6 || subType == 8) {
                        type = 6;
                        if (subType == 6) {
                            type = 1;
                            subType = null;
                        }
                    }
                } else {
                    type = null;
                }

                //获取财务记录
                presenter.getRecordList(true, type, subType, map, ACP);
            }
        });
    }

    //   subType
    //        <item>保证金缴纳</item>1
    //        <item>保证金退还</item>2
    //        <item>保证金赔付</item>3
    //        <item>卖币</item>4
    //        <item>买币</item>5
    //        <item>充币</item>6
    //        <item>提币</item>7
    //        <item>返佣</item>8
    //        <item>交易赔付</item>9

    //   type
    //        <item>充币</item>1
    //        <item>提币</item>2
    //        <item>转入</item>3
    //        <item>转出</item>4
    //        <item>资金交易-扣除</item>5
    //        <item>资金交易-增加</item>6

    /**
     * check uc、ac、acp成功后，通知刷新界面
     */
    public void onEvent(CheckLoginSuccessEvent event) {
        getList(false);
    }
}
