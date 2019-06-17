package com.spark.coinpay.main.buy.order;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.spark.coinpay.R;
import com.spark.coinpay.adapter.ViewPagerAdapter;
import com.spark.coinpay.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的订单-一键购买
 */

public class MyOrderActivity extends BaseActivity  {

    @BindView(R.id.wait_mycheck)
    RadioButton waitMycheck;
    @BindView(R.id.has_checked)
    RadioButton hasChecked;
    @BindView(R.id.has_cancel)
    RadioButton hasCancel;
    @BindView(R.id.productGroup_check)
    RadioGroup productGroupCheck;
    @BindView(R.id.id_tab_line_iv_check)
    ImageView idTabLineIvCheck;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    /**
     * 屏幕的宽度
     */
    private int screenWidth;
    /**
     * ViewPager的当前选中页
     */
    private int currentIndex;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_my_order;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.str_my_order));
        ivMessage.setVisibility(View.INVISIBLE);
        ivMessage.setImageResource(R.mipmap.icon_select);
        tvGoto.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        super.initData();
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new OrderIngFragment());
        mFragmentList.add(new OrderFinishFragment());
        mFragmentList.add(new OrderCancelFragment());

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mFragmentList));
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /**
             * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
             */
            @Override
            public void onPageScrollStateChanged(int state) {
            }

            /**
             * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
             * offsetPixels:当前页面偏移的像素位置
             */
            @Override
            public void onPageScrolled(int position, float offset, int offsetPixels) {

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) idTabLineIvCheck.getLayoutParams();

                /**
                 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
                 * 设置mTabLineIv的左边距 滑动场景：
                 * 记3个页面,
                 * 从左到右分别为0,1,2
                 * 0->1; 1->2; 2->1; 1->0
                 */

                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));

                } else if (currentIndex == 1 && position == 0) // 1->0
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));

                } else if (currentIndex == 1 && position == 1) // 1->2
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));
                } else if (currentIndex == 2 && position == 1) // 2->1
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));
                }
                idTabLineIvCheck.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        waitMycheck.setChecked(true);
                        break;
                    case 1:
                        hasChecked.setChecked(true);
                        break;
                    case 2:
                        hasCancel.setChecked(true);
                        break;
                }
                currentIndex = position;
            }
        });

        productGroupCheck.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                viewPager.setCurrentItem(group.indexOfChild(group.findViewById(checkedId)));
            }

        });
        initTabLineWidth();
    }

    /**
     * 设置滑动条的宽度为屏幕的1/3(根据Tab的个数而定)
     */
    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) idTabLineIvCheck.getLayoutParams();
        lp.width = screenWidth / 3;
        idTabLineIvCheck.setLayoutParams(lp);
    }

}
