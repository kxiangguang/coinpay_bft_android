package com.spark.coinpay.login;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.coinpay.GlobalConstant;
import com.spark.coinpay.MyApplication;
import com.spark.coinpay.R;
import com.spark.coinpay.adapter.ApiAddressAdapter;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.entity.ApiAddressEntity;
import com.spark.moduleassets.AcUrls;
import com.spark.modulebase.utils.AppUtils;
import com.spark.modulebase.utils.SharedPreferencesUtil;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;
import com.spark.modulelogin.LoginUrls;
import com.spark.moduleotc.OtcUrls;
import com.spark.moduleuc.AgentUrls;
import com.spark.moduleuc.UcUrls;
import com.spark.netty_library.GuardService;
import com.spark.netty_library.WebSocketService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_API_ADDRESS;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_APP_NAME;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_WRITE_API;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_WRITE_API_AC;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_WRITE_API_ACP;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_WRITE_API_UC;

/**
 * API地址
 */

public class ApiActivity extends BaseActivity {

    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.llSelect)
    LinearLayout llSelect;
    @BindView(R.id.etUc)
    EditText etUc;
    @BindView(R.id.etAc)
    EditText etAc;
    @BindView(R.id.etAcceptance)
    EditText etAcceptance;
    @BindView(R.id.llWrite)
    LinearLayout llWrite;
    @BindView(R.id.tvSelect)
    TextView tvSelect;
    @BindView(R.id.tvWrite)
    TextView tvWrite;

    private ArrayList<ApiAddressEntity> mDatas = new ArrayList<>();
    private ApiAddressAdapter adapter;
    private String apiAddress = "";
    private ApiAddressEntity apiAddressEntity;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_api;
    }

    @Override
    protected void initData() {
        super.initData();
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.str_my_set_api_address);
        apiAddress = SharedPreferencesUtil.getInstance(activity).getStringParam(SP_KEY_API_ADDRESS);

        LinearLayoutManager manager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(manager);
        adapter = new ApiAddressAdapter(R.layout.view_item_api, mDatas, apiAddress);
        adapter.bindToRecyclerView(recyclerView);

        boolean isTest = SharedPreferencesUtil.getInstance(activity).getBooleanParam(SP_KEY_WRITE_API);
        if (!isTest) {
            llSelect.setVisibility(View.VISIBLE);
            llWrite.setVisibility(View.GONE);
            tvSelect.setTextColor(getResources().getColor(R.color.bg_btn_normal));
            tvWrite.setTextColor(getResources().getColor(R.color.font_tab_head_selector));
        } else {
            llSelect.setVisibility(View.GONE);
            llWrite.setVisibility(View.VISIBLE);
            tvSelect.setTextColor(getResources().getColor(R.color.font_tab_head_selector));
            tvWrite.setTextColor(getResources().getColor(R.color.bg_btn_normal));
        }

        String uc = SharedPreferencesUtil.getInstance(activity).getStringParam(SP_KEY_WRITE_API_UC);
        String ac = SharedPreferencesUtil.getInstance(activity).getStringParam(SP_KEY_WRITE_API_AC);
        String acp = SharedPreferencesUtil.getInstance(activity).getStringParam(SP_KEY_WRITE_API_ACP);
        etUc.setText(uc);
        etAc.setText(ac);
        etAcceptance.setText(acp);
    }

    @Override
    protected void setListener() {
        super.setListener();

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                apiAddressEntity = (ApiAddressEntity) adapter.getItem(position);
                apiAddress = apiAddressEntity.getApiAddress();

                //保存API地址
                SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_API_ADDRESS, apiAddress);
                //保存APP名称
                SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_APP_NAME, apiAddressEntity.getAppName());
                //是否填写API
                SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_WRITE_API, false);
                //保存UC地址
                SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_WRITE_API_UC, "");
                //保存AC地址
                SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_WRITE_API_AC, "");
                //保存ACP地址
                SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_WRITE_API_ACP, "");


                MyApplication.getAppContext().deleteCurrentUser();
                MyApplication.getAppContext().getCookieManager().getCookieStore().removeAll();

                GlobalConstant.isTest = false;
                GuardService.isTest = false;
                WebSocketService.isTest = false;

                GlobalConstant.CUR_HOST = apiAddress;
                WebSocketService.setHost(apiAddress);
                GuardService.setHost(apiAddress);

                LoginUrls.getInstance().setHost(GlobalConstant.getHost() + "/" + GlobalConstant.TYPE_UC);
                LoginUrls.getInstance().setHostBusiness(GlobalConstant.getHost());
                LoginUrls.getInstance().setHostLogin(GlobalConstant.getHostLogin());
                AcUrls.getInstance().setHost(GlobalConstant.getHostAC());
                UcUrls.getInstance().setHost(GlobalConstant.getHostUC());
                OtcUrls.getInstance().setHost(GlobalConstant.getHostOTC());
                AgentUrls.getInstance().setHost(GlobalConstant.getHostAgent());

                finish();
            }
        });

    }


    @Override
    protected void loadData() {
        List<ApiAddressEntity> list = new ArrayList<>();
        list.add(new ApiAddressEntity("欧沃", "caymanex.pro"));
        list.add(new ApiAddressEntity("合众承兑", "555hub.com"));
        list.add(new ApiAddressEntity("压力承兑", "bench.bitpay.com"));
        list.add(new ApiAddressEntity("币承兑", "bp.wxmarket.cn"));
        list.add(new ApiAddressEntity("希锦支付", "bittoppayment.top"));
        list.add(new ApiAddressEntity("点币支付", "dbipay.com"));
        list.add(new ApiAddressEntity("通支付", "tongzhifu.vip"));
        list.add(new ApiAddressEntity("鑫众支付", "brick.asia"));
        list.add(new ApiAddressEntity("Money承兑", "money123.vip"));

        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
        adapter.disableLoadMoreIfNotFullPage();
    }

    @OnClick({R.id.tvSelect, R.id.tvWrite, R.id.tvLogin})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvSelect:
                llSelect.setVisibility(View.VISIBLE);
                llWrite.setVisibility(View.GONE);
                tvSelect.setTextColor(getResources().getColor(R.color.bg_btn_normal));
                tvWrite.setTextColor(getResources().getColor(R.color.font_tab_head_selector));
                break;
            case R.id.tvWrite:
                llSelect.setVisibility(View.GONE);
                llWrite.setVisibility(View.VISIBLE);
                tvSelect.setTextColor(getResources().getColor(R.color.font_tab_head_selector));
                tvWrite.setTextColor(getResources().getColor(R.color.bg_btn_normal));
                break;
            case R.id.tvLogin:
                checkInput();
                break;
        }
    }

    /**
     * 检查用户名和密码后进行登录
     */
    @Override
    protected void checkInput() {
        super.checkInput();

        String uc = StringUtils.getText(etUc);
        String ac = StringUtils.getText(etAc);
        String acp = StringUtils.getText(etAcceptance);

        if (StringUtils.isEmpty(uc, ac, acp)) {
            ToastUtils.showToast(activity, getString(R.string.str_incomplete_information));
        } else if (!AppUtils.isIp(uc, ac, acp)) {
            ToastUtils.showToast("ip地址不正确");
        } else {
            //是否填写API
            SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_WRITE_API, true);
            //保存UC地址
            SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_WRITE_API_UC, uc);
            //保存AC地址
            SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_WRITE_API_AC, ac);
            //保存ACP地址
            SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_WRITE_API_ACP, acp);
            //保存APP名称
            SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_APP_NAME, acp);
            //保存API地址
            SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_API_ADDRESS, "");

            finish();

        }
    }

}
