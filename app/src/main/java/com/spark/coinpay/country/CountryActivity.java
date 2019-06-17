package com.spark.coinpay.country;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.adapter.CountryAdapter;
import com.spark.moduleuc.entity.CountryEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 国家选择
 */
public class CountryActivity extends BaseActivity implements CountryContract.CountryView {
    public static final int RETURN_COUNTRY = 0;
    private RecyclerView rvCountry;
    private List<CountryEntity> countries = new ArrayList<>();
    private CountryAdapter adapter;
    private CountryPresenterImpl presenter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_global_normal_rv;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        rvCountry = findViewById(R.id.recyclerView);
        ivBack.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new CountryPresenterImpl(this);
        tvTitle.setText(getString(R.string.str_country));
        initRv();
    }

    @Override
    protected void initRv() {
        super.initRv();
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvCountry.setLayoutManager(manager);
        adapter = new CountryAdapter(R.layout.item_country, countries, activity);
        rvCountry.setAdapter(adapter);
    }

    @Override
    protected void loadData() {
        presenter.country();
    }

    @Override
    protected void setListener() {
        super.setListener();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CountryEntity countryEntity = (CountryEntity) adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("objCountry", countryEntity);
                Intent intent = new Intent();
                intent.putExtra("getCountry", countries.get(position));
                CountryActivity.this.setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    @Override
    public void countrySuccess(List<CountryEntity> obj) {
        if (obj == null) return;
        this.countries.clear();
        this.countries.addAll(obj);
        adapter.notifyDataSetChanged();
    }

}
