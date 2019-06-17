package com.spark.coinpay.country;


import com.spark.modulebase.base.BaseContract;
import com.spark.moduleuc.entity.CountryEntity;

import java.util.List;

/**
 * 国家
 */

public interface CountryContract {
    interface CountryView extends BaseContract.BaseView {

        void countrySuccess(List<CountryEntity> obj);
    }

    interface CountryPresenter extends BaseContract.BasePresenter {

        void country();
    }
}
