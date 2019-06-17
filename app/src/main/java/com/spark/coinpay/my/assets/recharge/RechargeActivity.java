package com.spark.coinpay.my.assets.recharge;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.utils.AppUtils;
import com.spark.modulebase.utils.BitmapUtils;
import com.spark.modulebase.utils.DateUtils;
import com.spark.modulebase.utils.LogUtils;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 充币
 */

public class RechargeActivity extends BaseActivity {
    @BindView(R.id.tvCoin)
    TextView tvCoin;
    @BindView(R.id.ivAddress)
    ImageView ivAddress;
    @BindView(R.id.tvAdress)
    TextView tvAdress;
    @BindView(R.id.tvCopy)
    TextView tvCopy;
    @BindView(R.id.tvSave)
    TextView tvSave;
    @BindView(R.id.tvNotice)
    TextView tvNotice;
    @BindView(R.id.etCount)
    EditText etCount;

    private String[] coinArrays;
    private Wallet wallet;
    private Bitmap saveBitmap;
    private ArrayList<Wallet> wallets;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_recharge;
    }

    /**
     * 检查是否具有权限
     */
    private void checkPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        LogUtils.d("permission: " + data.get(0));
                        save();
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

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.str_recharge));
    }

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            wallets = (ArrayList<Wallet>) bundle.getSerializable("wallets");
            if (wallets != null && wallets.size() > 0) {
                coinArrays = new String[wallets.size()];
                for (int i = 0; i < wallets.size(); i++) {
                    Wallet w = wallets.get(i);
                    coinArrays[i] = w.getCoinId();
                }
                tvCoin.setText(coinArrays[0]);
                wallet = wallets.get(0);
                tvAdress.setText(wallet.getAddress());
                setImage();
                tvNotice.setText("风险提示：请勿向该地址充值任何非" + coinArrays[0] + "资产");
            }
        }
    }


    @OnClick({R.id.tvCoin, R.id.tvSave, R.id.tvCopy, R.id.tvPay})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvCoin:
                if (coinArrays == null) return;
                showCoinDialog();
                break;
            case R.id.tvCopy:
                AppUtils.copyText(activity, tvAdress.getText().toString().trim());
                ToastUtils.showToast(activity, getString(R.string.str_copy_success));
                break;
            case R.id.tvSave:
                if (wallet == null)
                    return;
                checkPermission();
                break;
            case R.id.tvPay:
                clickExtract();
                break;
        }
    }

    private void clickExtract() {
        String count = StringUtils.getText(etCount);
        if (StringUtils.isEmpty(count)) {
            ToastUtils.showToast("请输入数量");
            return;
        }
        if (AppUtils.isInstalled(this, "com.spark.wallet")) {
//            if (memberWallet != null && coin != null) {
//                String count = etCount.getText().toString();
//                if (StringUtils.isNotEmpty(count) && Integer.valueOf(count) > 0) {
            Bundle bundle = new Bundle();
            bundle.putString("orderNo", System.currentTimeMillis() + "");
            bundle.putString("address", tvAdress.getText().toString());
            bundle.putString("amount", count);
            bundle.putString("coinName", tvCoin.getText().toString());
            bundle.putBoolean("isJumpApp", true);

            //跳转应用中某个页面
            Intent intent = new Intent();
            intent.setClassName("com.spark.wallet", "com.spark.wallet.activity.wallet_coin.ExtractActivity");
            intent.putExtras(bundle);
            startActivityForResult(intent, 0);
//                } else {
//                    ToastUtils.showToast("充币个数必须大于0");
//                }
//            }
        } else {
            ToastUtils.showToast("请先安装新比特应用");
        }
    }

    /**
     * 显示二维码
     */
    private void setImage() {
        if (StringUtils.isEmpty(wallet.getAddress())) return;

        String address = wallet.getAddress();
        String amount = 0 + "";
        String orderNo = System.currentTimeMillis() + "";
        String coinName = wallet.getCoinId();

        //final String addressStr = "bitpay://transfer?amount=" + amount + "&address=" + address + "&orderNo=" + orderNo + "&coinName=" + coinName;
        final String addressStr = address;

        ivAddress.post(new Runnable() {
            @Override
            public void run() {
                saveBitmap = AppUtils.createQRCode(addressStr, Math.min(ivAddress.getWidth(), ivAddress.getHeight()));
                ivAddress.setImageBitmap(saveBitmap);
            }
        });
    }

    /**
     * 展示币种
     */
    private void showCoinDialog() {
        NormalListDialog normalDialog = null;
        normalDialog = new NormalListDialog(activity, coinArrays);
        normalDialog.titleBgColor(getResources().getColor(R.color.bg_btn_normal));
//        normalDialog.widthScale(0.8f);
        normalDialog.title(getString(R.string.str_select_coin));
        final NormalListDialog finalNormalDialog = normalDialog;
        normalDialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                wallet = wallets.get(position);
                tvCoin.setText(wallet.getCoinId());
                tvAdress.setText(wallet.getAddress());
                setImage();
                tvNotice.setText("风险提示：请勿向该地址充值任何非" + wallet.getCoinId() + "资产");
                finalNormalDialog.dismiss();
            }
        });
        normalDialog.show();
    }

    /**
     * 保存到相册
     */
    private void save() {
        String time = DateUtils.getFormatTime(DateUtils.DATE_FORMAT_1, new Date());
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getPackageName() + "/" + time + ".jpg");
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            if (saveBitmap != null) {
                BitmapUtils.saveBitmapToFile(saveBitmap, file, 100);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (file != null && file.exists()) {
            Uri uri = Uri.fromFile(file);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            ToastUtils.showToast(activity, getString(R.string.str_save_success));
        }
    }

}
