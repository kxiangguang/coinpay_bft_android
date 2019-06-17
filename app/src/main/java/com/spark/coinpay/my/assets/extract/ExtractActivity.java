package com.spark.coinpay.my.assets.extract;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.library.ac.model.MessageResult;
import com.spark.moduleassets.entity.Address;
import com.spark.moduleassets.entity.ExtractInfo;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.utils.KeyboardUtils;
import com.spark.modulebase.utils.LogUtils;
import com.spark.modulebase.utils.MathUtils;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 提币
 */
public class ExtractActivity extends BaseActivity implements ExtractContract.ExtractView {

    @BindView(R.id.tvSelectAdress)
    TextView tvSelectAdress;
    @BindView(R.id.etCount)
    EditText etCount;
    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.tvServiceFee)
    TextView tvServiceFee;
    @BindView(R.id.tvFinalCount)
    TextView tvFinalCount;
    @BindView(R.id.tvCollectUnit)
    TextView tvCollectUnit;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.tvCoin)
    TextView tvCoin;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvGetUnit)
    TextView tvGetUnit;
    @BindView(R.id.tvExtract)
    TextView tvExtract;
    @BindView(R.id.tvScan)
    ImageView tvScan;
    @BindView(R.id.llCount22)
    LinearLayout llCount22;

    private ExtractPresenterImpl presenter;
    private ArrayList<Wallet> wallets;
    private String[] coinArrays;
    private Wallet wallet;
    private HashMap<String, ExtractInfo> map;
    private ExtractInfo extractInfo;
    private int withdrawFeeType = 1;//提币手续费类型：1-固定金额 2-按比例
    private String coinName = "";
    private boolean isFeeZero = false;//false 外部提币  true内部提币

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_extract;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.str_extract));
    }

    @Override
    protected void initData() {
        super.initData();
        map = new HashMap<>();
        presenter = new ExtractPresenterImpl(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            wallets = (ArrayList<Wallet>) bundle.getSerializable("wallets");
            if (wallets != null && wallets.size() > 0) {
                coinArrays = new String[wallets.size()];
                for (int i = 0; i < wallets.size(); i++) {
                    Wallet w = wallets.get(i);
                    coinArrays[i] = w.getCoinId();
                }
                coinName = coinArrays[0];
                tvCoin.setText(coinName);
                tvGetUnit.setText(coinName);
                tvCollectUnit.setText(coinName);
                wallet = wallets.get(0);
                String balance = MathUtils.subZeroAndDot(MathUtils.getRundNumber(wallet.getBalance(), 8, null));
                tvBalance.setText(getString(R.string.str_coin_can_banlance) + "  " + balance + "  " + wallet.getCoinId());
            }
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
        if (wallet != null)
            presenter.getExtractInfo(wallet.getCoinId());
    }

    @OnClick({R.id.tvCoin, R.id.tvSelectAdress, R.id.tvAll, R.id.tvExtract, R.id.tvScan, R.id.llCount22, R.id.tvAddress})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvCoin://选择币种
                if (coinArrays == null) return;
                showCoinDialog();
                break;
            case R.id.tvSelectAdress://选择地址
                Bundle bundle = new Bundle();
                bundle.putSerializable("wallets", wallets);
                bundle.putSerializable("wallet", wallet);
                showActivity(ExtractAddressActivity.class, bundle, 1);
                break;
            case R.id.tvAll://全部
                String address = tvAddress.getText().toString().trim();
                if (StringUtils.isEmpty(address)) {
                    handler.sendEmptyMessage(0);
                } else {
                    if (extractInfo != null) {
                        etCount.setText(MathUtils.subZeroAndDot(wallet.getBalance() + ""));
                    }
                }
                break;
            case R.id.tvExtract://确定
                checkInput();
                break;
            case R.id.tvScan://扫一扫
                checkPermission();
                break;
            case R.id.llCount22:
                address = tvAddress.getText().toString().trim();
                if (StringUtils.isEmpty(address)) {
                    handler.sendEmptyMessage(0);
                } else {
                    llCount22.setVisibility(View.GONE);
                }
                break;
            case R.id.tvAddress:
                bundle = new Bundle();
                bundle.putSerializable("wallets", wallets);
                bundle.putSerializable("wallet", wallet);
                showActivity(ExtractAddressActivity.class, bundle, 1);
                break;
        }

    }

    @Override
    protected void setListener() {
        super.setListener();
        etCount.addTextChangedListener(localChangeWatcher);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ToastUtils.showToast("请先选择提币地址");
        }
    };

    private TextWatcher localChangeWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            LogUtils.e("TextWatcher====================beforeTextChanged");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            LogUtils.e("TextWatcher====================onTextChanged");
        }

        @Override
        public void afterTextChanged(Editable s) {
            LogUtils.e("TextWatcher====================afterTextChanged");
            String address = tvAddress.getText().toString().trim();
            if (StringUtils.isNotEmpty(address)) {
                String amount = StringUtils.getText(etCount);
                if (amount.startsWith(".")) {
                    etCount.setText("0.");
                    etCount.setSelection(etCount.length());
                } else {
                    if (StringUtils.isNotEmpty(amount)) {
                        if (isFeeZero) {//false 外部提币  true内部提币
                            tvServiceFee.setText("0");
                            double fee = 0;
//                    if (extractInfo != null) {
//                        fee = extractInfo.getWithdrawFee();
//                    }
                            //withdrawFeeType //提币手续费类型：1-固定金额 2-按比例
                            if (withdrawFeeType == 2) {
                                double money = Double.parseDouble(amount);
                                tvFinalCount.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(money - money * fee, 8, null)));
                            } else {
                                tvFinalCount.setText(MathUtils.subZeroAndDot("" + (Double.parseDouble(amount) - fee)));
                            }
                        } else {
                            double fee = 0;
                            if (extractInfo != null) {
                                fee = extractInfo.getWithdrawFee();
                            }
                            //withdrawFeeType //提币手续费类型：1-固定金额 2-按比例
                            if (withdrawFeeType == 2) {
                                double money = Double.parseDouble(amount);
                                tvFinalCount.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(money - money * fee, 8, null)));
                                tvServiceFee.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(money * fee, 8, null)));
                            } else {
                                tvFinalCount.setText(MathUtils.subZeroAndDot("" + (Double.parseDouble(amount) - fee)));
                            }
                        }

                    } else {
                        tvFinalCount.setText(0 + "");
                        if (isFeeZero) {//false 外部提币  true内部提币
                            double fee = 0;
//                        if (extractInfo != null) {
//                            fee = extractInfo.getWithdrawFee();
//                        }
                            tvServiceFee.setText(MathUtils.subZeroAndDot(fee + ""));
                        } else {
                            double fee = 0;
                            if (extractInfo != null) {
                                fee = extractInfo.getWithdrawFee();
                            }
                            tvServiceFee.setText(MathUtils.subZeroAndDot(fee + ""));
                        }

                    }
                }
            }
        }
    };

    protected void checkInput() {
        super.checkInput();
        String address = tvAddress.getText().toString();
        String amount = StringUtils.getText(etCount);
        String tradePassword = StringUtils.getText(etPassword);
//        double minWithdrawAmount = 0;
//        if (extractInfo != null) {
//            minWithdrawAmount = extractInfo.getMinWithdrawAmount();
//        }
        if (StringUtils.isEmpty(address)) {
            ToastUtils.showToast(activity, getString(R.string.str_select_address));
        } else if (StringUtils.isEmpty(amount)) {
            ToastUtils.showToast(activity, getString(R.string.str_please_input) + getString(R.string.str_min_coin_num));
        } else if (StringUtils.isEmpty(tradePassword)) {
            ToastUtils.showToast(activity, getString(R.string.str_please_input) + getString(R.string.text_money_pwd));
        } else if (Double.parseDouble(amount) > wallet.getBalance()) {
            ToastUtils.showToast(activity, getString(R.string.str_coin_can_banlance) + wallet.getBalance());
        }
//        else if (Double.parseDouble(amount) < minWithdrawAmount) {
//            ToastUtils.showToast(activity, getString(R.string.str_min_coin_num_tag) + minWithdrawAmount);
//        }
        else {
            KeyboardUtils.hideSoftInput(activity);
            presenter.walletWithdraw(address, new BigDecimal(amount), wallet.getCoinId(), tradePassword);
        }

    }

    /**
     * 展示币种
     */
    private void showCoinDialog() {
        NormalListDialog normalDialog = null;
        normalDialog = new NormalListDialog(activity, coinArrays);
        normalDialog.titleBgColor(getResources().getColor(R.color.bg_btn_normal));
        normalDialog.title(getString(R.string.str_select_coin));
        final NormalListDialog finalNormalDialog = normalDialog;
        normalDialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!wallets.get(position).getCoinId().equals(wallet.getCoinId())) {
                    tvAddress.setText("");
                    isFeeZero = false;//false 外部提币  true内部提币
                }
                wallet = wallets.get(position);
                coinName = wallet.getCoinId();
                tvCoin.setText(coinName);
                tvGetUnit.setText(coinName);
                tvCollectUnit.setText(coinName);
                tvBalance.setText(getString(R.string.str_coin_can_banlance) + MathUtils.subZeroAndDot(MathUtils.getRundNumber(wallet.getBalance(), 8, null)) + coinName);
                presenter.getExtractInfo(coinName);
                finalNormalDialog.dismiss();
            }
        });
        normalDialog.show();
    }

    @Override
    public void getExtractInfoSuccess(List<ExtractInfo> list) {
        if (list != null && list.size() > 0) {
            for (ExtractInfo info : list) {
                map.put(info.getCoinName(), info);
            }
            extractInfo = map.get(wallet.getCoinId());
            if (extractInfo != null) {
                //etCount.setHint(getString(R.string.str_min_coin_num_tag) + MathUtils.subZeroAndDot(String.valueOf(extractInfo.getMinWithdrawAmount())));
                tvServiceFee.setText(MathUtils.subZeroAndDot(extractInfo.getWithdrawFee() + ""));
                //提币手续费类型：1-固定金额 2-按比例
                withdrawFeeType = extractInfo.getWithdrawFeeType();
            }
        }

    }

    @Override
    public void walletWithdrawSuccess(String response) {
        if (StringUtils.isNotEmpty(response)) {
            ToastUtils.showToast(this, response);
        } else {
            ToastUtils.showToast(this, getString(R.string.str_save_success));
        }
        setResult(RESULT_OK);
        finish();
    }

    /**
     * check权限
     *
     * @param position
     */
    private void checkPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE, Permission.Group.CAMERA)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        LogUtils.d("permission: " + data.get(0));
                        goScan();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        if (AndPermission.hasAlwaysDeniedPermission(activity, data)) {
                            AndPermission.permissionSetting(activity).execute();
                            return;
                        }
                        ToastUtils.showToast(activity, getString(com.spark.modulebase.R.string.str_no_permission));
                    }
                }).start();
    }

    private void goScan() {
        //showActivity(CaptureActivity.class, null, 2);
        Intent intent = new Intent(this, CaptureActivity.class);
        /**
         * ZxingConfig是配置类
         *可以设置是否显示底部布局，闪光灯，相册，
         * 是否播放提示音  震动
         * 设置扫描框颜色等
         * 也可以不传这个参数
         *
         **/
        ZxingConfig config = new ZxingConfig();
        config.setPlayBeep(true);//是否播放扫描声音 默认为true
        config.setShake(true);//是否震动  默认为true
        config.setDecodeBarCode(true);//是否扫描条形码 默认为true
        config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
        config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
        config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
        config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
        config.setShowbottomLayout(false);//是否显示底部布局，闪光灯，相册，
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, 2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 1:
                    Bundle bundle = data.getExtras();
                    Address address = (Address) bundle.getSerializable("address");
                    tvAddress.setText(address.getAddress());
                    checkAddress(address.getAddress());
                    break;
                case 2:
                    String result = data.getStringExtra(Constant.CODED_CONTENT);
                    parseResult(result);
                    break;
            }
        }

    }

    /**
     * 解析二维码扫描结果
     *
     * @param result
     */
    private void parseResult(String result) {
        if (StringUtils.isEmpty(result)) {
            ToastUtils.showToast(getString(R.string.str_zxing_fail));
            return;
        }
        //CNT:address?amount=10&
        String[] strs = result.split(":");

        if (strs.length > 1) {
            boolean flag = false;
            //strs[0]=CNT
            if (strs[0].equalsIgnoreCase(coinName)) {
                flag = true;
            }

            if (flag) {
                //strs[1]=address?amount=10&
                String[] strBehind = strs[1].split("\\?");
                if (strBehind.length > 1) {
                    //strBehind[0]=address
                    tvAddress.setText(strBehind[0]);//:和?之间
                    checkAddress(strBehind[0]);
                    //strBehind[1]=amount=10&
                    String[] strAnd = strBehind[1].split("&");//?之后
                    if (strAnd.length > 1) {
                        for (int i = 0; i < strAnd.length; i++) {
                            if (strAnd[i].contains("amount=")) {
                                String strMoney = strAnd[i].replace("amount=", "");
                                if (MathUtils.isNumber(strMoney) && MathUtils.getBigDecimalCompareTo(strMoney, "0", 18) > 0) {
                                    etCount.setText(strMoney);
                                }
                            }
                        }
                    } else {
                        if (strBehind[1].contains("amount=")) {
                            String strMoney = strBehind[1].replace("amount=", "");
                            if (MathUtils.isNumber(strMoney) && MathUtils.getBigDecimalCompareTo(strMoney, "0", 18) > 0) {
                                etCount.setText(strMoney);
                            }
                        }
                    }
                } else {
                    tvAddress.setText(strs[1]);
                    checkAddress(strs[1]);
                    if (strs.length > 2) {
                        if (MathUtils.isNumber(strs[2]) && MathUtils.getBigDecimalCompareTo(strs[2], "0", 18) > 0) {
                            etCount.setText(strs[2]);
                        }
                    }
                }
            } else {
                ToastUtils.showToast(getString(R.string.str_please_select) + coinName + getString(R.string.str_extract_addr));
            }
        } else {
            tvAddress.setText(result);
            checkAddress(result);
        }

    }

    /**
     * 用户选择地址后校验提币地址是否内部地址
     */
    private void checkAddress(String address) {
        if (StringUtils.isNotEmpty(address)) {
            etCount.setEnabled(true);
            llCount22.setVisibility(View.GONE);
            presenter.checkAddress(address);
        } else {
            etCount.setEnabled(false);
            llCount22.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void checkAddressSuccess(MessageResult response) {
        if (response != null) {
            isFeeZero = true;//false 外部提币  true内部提币
            String amount = StringUtils.getText(etCount);
            tvServiceFee.setText("0");
            if (StringUtils.isNotEmpty(amount)) {
                double fee = 0;
//                    if (extractInfo != null) {
//                        fee = extractInfo.getWithdrawFee();
//                    }
                //withdrawFeeType //提币手续费类型：1-固定金额 2-按比例
                if (withdrawFeeType == 2) {
                    double money = Double.parseDouble(amount);
                    tvFinalCount.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(money - money * fee, 8, null)));
                } else {
                    tvFinalCount.setText(MathUtils.subZeroAndDot("" + (Double.parseDouble(amount) - fee)));
                }
            }
        }
    }

    @Override
    public void checkAddressFail(HttpErrorEntity response) {
        if (response != null) {
            isFeeZero = false;//false 外部提币  true内部提币
            String amount = StringUtils.getText(etCount);
            if (StringUtils.isNotEmpty(amount)) {
                double fee = 0;
                if (extractInfo != null) {
                    fee = extractInfo.getWithdrawFee();
                }
                //withdrawFeeType //提币手续费类型：1-固定金额 2-按比例
                if (withdrawFeeType == 2) {
                    double money = Double.parseDouble(amount);
                    tvFinalCount.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(money - money * fee, 8, null)));
                    tvServiceFee.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(money * fee, 8, null)));
                } else {
                    tvFinalCount.setText(MathUtils.subZeroAndDot("" + (Double.parseDouble(amount) - fee)));
                    tvServiceFee.setText(MathUtils.subZeroAndDot(fee + ""));
                }
            }
        }
    }

}