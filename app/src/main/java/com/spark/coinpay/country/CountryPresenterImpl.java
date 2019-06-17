package com.spark.coinpay.country;

import com.android.volley.VolleyError;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.moduleuc.entity.CountryEntity;
import com.spark.moduleuc.RegisterControllerModel;

import java.util.List;

/**
 * 国家
 */

public class CountryPresenterImpl implements CountryContract.CountryPresenter {
    private CountryContract.CountryView countryView;
    private RegisterControllerModel registerControllerModel;

    public CountryPresenterImpl(CountryContract.CountryView countryView) {
        this.countryView = countryView;
        registerControllerModel = new RegisterControllerModel();
    }

    @Override
    public void country() {
        showLoading();
        registerControllerModel.getCountry(new ResponseCallBack.SuccessListener<List<CountryEntity>>() {
            @Override
            public void onResponse(List<CountryEntity> response) {
                hideLoading();
                if (countryView != null)
                    countryView.countrySuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (countryView != null)
                    countryView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (countryView != null)
                    countryView.dealError(volleyError);
            }
        });
    }

    @Override
    public void showLoading() {
        if (countryView != null)
            countryView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (countryView != null)
            countryView.hideLoading();
    }

    @Override
    public void destory() {
        countryView = null;
    }
}
