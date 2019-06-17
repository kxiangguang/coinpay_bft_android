package com.spark.coinpay.base;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/19.
 */

public abstract class BaseFragmentActivity extends BaseActivity {
    protected List<BaseFragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null)
            getFragment();
        super.onCreate(savedInstanceState);
    }

    /**
     * 添加fragment
     *
     * @param fragment
     */
    public void addFragment(BaseFragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(R.id.flContainer, fragment, fragment.getmTag()).commit();
    }

    /**
     * 隐藏fragment
     *
     * @param fragments
     */
    protected void hideFragments(List<BaseFragment> fragments) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            if (!fragments.get(i).isHidden()) {
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.hide(fragments.get(i));
            }
        }
        transaction.commit();
    }

    /**
     * 移除fragment
     *
     * @param fragments
     * @param fragment
     */
    protected void removeFragment(List<BaseFragment> fragments, BaseFragment fragment) {
        fragments.remove(fragment);
        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    /**
     * 显示fragment
     */
    protected void showFragment(BaseFragment fragment) {
        if (!fragment.isAdded()) {
            addFragment(fragment);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.show(fragment).commit();
    }

    /**
     * 切换fragment
     *
     * @param fragment
     */
    protected void switchFragment(BaseFragment fragment) {
        hideFragments(fragments);
        showFragment(fragment);
    }

    /**
     * 添加需要显示的fragmemt
     */
    protected abstract void getFragment();

}
