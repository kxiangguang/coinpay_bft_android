package com.spark.coinpay.my.assets.extract;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.coinpay.R;
import com.spark.coinpay.adapter.ExtractAddressAdapter;
import com.spark.coinpay.base.BaseActivity;
import com.spark.moduleassets.entity.Address;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.entity.HttpErrorEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 提币地址
 */

public class ExtractAddressActivity extends BaseActivity implements ExtractContract.ExtractAddressView {
    @BindView(R.id.rvAddress)
    RecyclerView rvAddress;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private ExtractAddressPresenterImpl presenter;
    private ExtractAddressAdapter adapter;
    private List<Address> addressList = new ArrayList<>();
    private ArrayList<Wallet> wallets;
    private Wallet wallet;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_extract_address;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.str_extract_addr));
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new ExtractAddressPresenterImpl(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            wallets = (ArrayList<Wallet>) bundle.getSerializable("wallets");
            wallet = (Wallet) bundle.getSerializable("wallet");
        }
    }

    @Override
    protected void initRv() {
        super.initRv();
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvAddress.setLayoutManager(manager);
        adapter = new ExtractAddressAdapter(addressList);
        adapter.bindToRecyclerView(rvAddress);
    }

    @Override
    protected void loadData() {
        super.loadData();
        getExtractAddress(true);
    }

    private void getExtractAddress(boolean isShow) {
        if (isShow)
            showLoading();
        if (wallet != null) {
            presenter.getExtractAddress(wallet.getCoinId());
        } else {
            presenter.getExtractAddress("");
        }
    }

    @OnClick({R.id.tvAdd})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvAdd:
                Bundle bundle = new Bundle();
                bundle.putSerializable("wallets", wallets);
                showActivity(ExtractAddAddressActivity.class, bundle, 1);
                break;
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getExtractAddress(false);
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                Address address = (Address) adapter.getItem(position);
                bundle.putSerializable("address", address);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void getExtractAddressSuccess(List<Address> list) {
        if (refreshLayout != null) {
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
        }

        if (list == null) return;

        addressList.clear();
        addressList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void dealError(HttpErrorEntity httpErrorEntity) {
        super.dealError(httpErrorEntity);
        if (refreshLayout != null) {
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void dealError(VolleyError volleyError) {
        super.dealError(volleyError);
        if (refreshLayout != null) {
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1: //划转
                    getExtractAddress(true);
                    break;
            }
        }
    }
}
