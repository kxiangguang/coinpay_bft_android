package com.spark.coinpay.bind_account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.spark.coinpay.GlobalConstant;
import com.spark.coinpay.MyApplication;
import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.entity.PayWaySetting;
import com.spark.coinpay.view.DeleteDialog;
import com.spark.library.acp.model.MessageResult;
import com.spark.modulebase.entity.User;
import com.spark.modulebase.utils.BitmapUtils;
import com.spark.modulebase.utils.LogUtils;
import com.spark.modulebase.utils.MathUtils;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;
import com.spark.modulebase.utils.UriUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.spark.coinpay.GlobalConstant.CHOOSE_ALBUM;
import static com.spark.coinpay.GlobalConstant.TAKE_PHOTO;

/**
 * 绑定银行卡
 */

public class BindBankActivity extends BaseActivity implements BindAccountContract.BankView {
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.llBank)
    LinearLayout llBank;
    @BindView(R.id.etBranch)
    EditText etBranch;
    @BindView(R.id.llBranch)
    LinearLayout llBranch;
    @BindView(R.id.etAccount)
    EditText etAccount;
    @BindView(R.id.etConfirmAccount)
    EditText etConfirmAccount;
    @BindView(R.id.llConfirmAccount)
    LinearLayout llConfirmAccount;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.tvBank)
    TextView tvBank;
    @BindView(R.id.etWarn)
    EditText etWarn;
    @BindView(R.id.etDay)
    EditText etDay;
    @BindView(R.id.ivIdFace)
    ImageView idFaceImg;

    private BindBankPresenterImpl presenter;
    private PayWaySetting setting = new PayWaySetting();
    private NormalListDialog dialog;
    private String[] bankNames;
    private PayWaySetting payWaySetting;
    private boolean isUpdate = false;//添加或者更新
    String name = "";
    String bank = "";
    String branch = "";
    String account = "";
    String reaccount = "";
    String warn = "";
    String dayLimit = "";
    private DeleteDialog deleteDialog;

    private Uri imageUri;
    private File imageFile;
    private String filename = "qrCode.jpg";
    private String url = "";

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_bind_bank;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_ALBUM:
                case TAKE_PHOTO:
                    if (requestCode == CHOOSE_ALBUM) {
                        if (resultCode != RESULT_OK) return;
                        imageUri = data.getData();
                        if (Build.VERSION.SDK_INT >= 19)
                            imageFile = UriUtils.getUriFromKitKat(this, imageUri);
                        else
                            imageFile = UriUtils.getUriBeforeKitKat(this, imageUri);
                        if (imageFile == null) {
                            ToastUtils.showToast(activity, getString(R.string.str_library_file_exception));
                            return;
                        }
                    }
                    if (idFaceImg != null) {
                        Bitmap bitmap = BitmapUtils.zoomBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()), idFaceImg.getWidth(), idFaceImg.getHeight());
                        String base64Data = BitmapUtils.imgToBase64(bitmap);
                        bitmap.recycle();
                        presenter.uploadBase64Pic("data:image/jpeg;base64," + base64Data);
                    }
                    break;
            }
        }
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        User user = MyApplication.getAppContext().getCurrentUser();
        if (user != null && StringUtils.isNotEmpty(user.getRealName())) {
            etName.setText(user.getRealName());
        }

    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new BindBankPresenterImpl(this);
        bankNames = getResources().getStringArray(R.array.bank_name);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            payWaySetting = (PayWaySetting) bundle.getSerializable("data");
            if (payWaySetting != null) {
                isUpdate = true;
                tvConfirm.setText(getResources().getString(R.string.str_text_change));
                tvBank.setText(payWaySetting.getBank());
                etBranch.setText(payWaySetting.getBranch());
                etAccount.setText(payWaySetting.getPayAddress());
                etWarn.setText(payWaySetting.getPayNotes());
                etDay.setText(MathUtils.subZeroAndDot(payWaySetting.getDayLimit() + ""));
                if (StringUtils.isNotEmpty(payWaySetting.getQrCodeUrl())) {
                    url = payWaySetting.getQrCodeUrl().toString();
                    Glide.with(this).load(url).into(idFaceImg);
                }
            }
        }
        if (isUpdate) {
            tvTitle.setText(getString(R.string.str_text_change) + getString(R.string.str_bank_num));
        } else {
            tvTitle.setText(getString(R.string.str_account_seting) + getString(R.string.str_bank_num));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @OnClick({R.id.llBank, R.id.tvConfirm, R.id.ivIdFace})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.llBank:
                showDialog();
                break;
            case R.id.tvConfirm:
                confirm();
                break;
            case R.id.ivIdFace:
                actionSheetDialogNoTitle();
                break;
        }
    }

    /**
     * 选择头像弹框
     */
    private void actionSheetDialogNoTitle() {
        final String[] stringItems = {getString(R.string.str_camera), getString(R.string.str_album)};
        final ActionSheetDialog dialog = new ActionSheetDialog(activity, stringItems, null);
        dialog.isTitleShow(false).itemTextColor(getResources().getColor(R.color.font_main_title))
                .cancelText(getResources().getColor(R.color.font_main_content))
                .cancelText(getResources().getString(R.string.str_cancel)).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                    case 1:
                        checkPermission(position);
                        break;
                }
                dialog.dismiss();
            }
        });
    }

    /**
     * check权限
     *
     * @param position
     */
    private void checkPermission(final int position) {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE, Permission.Group.CAMERA)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        LogUtils.d("permission: " + data.get(0));
                        if (position == 0) {
                            startCamera();
                        } else {
                            chooseFromAlbum();
                        }
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

    /**
     * 照相机
     */
    private void startCamera() {
        imageUri = UriUtils.getUriForFile(this, imageFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    /**
     * 相册选择
     */
    private void chooseFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_ALBUM);
    }


    @Override
    public void uploadBase64PicSuccess(String obj) {
        ToastUtils.showToast(this, R.string.str_upload_success);
        url = obj;
        Glide.with(this).load(url).into(idFaceImg);
    }

    /**
     * 提交数据
     */
    private void confirm() {
        name = etName.getText().toString();
        bank = tvBank.getText().toString();
        branch = etBranch.getText().toString();
        account = etAccount.getText().toString();
        reaccount = etConfirmAccount.getText().toString();
        warn = etWarn.getText().toString();
        dayLimit = etDay.getText().toString();
        if (!StringUtils.isEmpty(name, bank, branch, account, reaccount, dayLimit)) {
            if (account.equals(reaccount)) {
                showReleaseDialog();
            } else {
                ToastUtils.showToast(this, R.string.str_diff_cardnumber);
            }
        } else {
            ToastUtils.showToast(this, R.string.incomplete_information);
        }
    }

    /**
     * 选择开户银行
     */
    private void showDialog() {
        dialog = new NormalListDialog(activity, bankNames);
        dialog.title("请选择银行");
        dialog.titleBgColor(getResources().getColor(R.color.sec_font_title));
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                String bankName = bankNames[position];
                tvBank.setText(bankName);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public void doBindBankSuccess(MessageResult obj) {
        ToastUtils.showToast(this, obj.getMessage());
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void doUpdateBankSuccess(MessageResult obj) {
        ToastUtils.showToast(this, R.string.str_upload_success);
        setResult(RESULT_OK);
        finish();
    }

    /**
     * 提示框-请输入交易密码
     */
    private void showReleaseDialog() {
        deleteDialog = new DeleteDialog(activity);
        deleteDialog.setPositiveOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = StringUtils.getText(deleteDialog.getPwdEditText());
                if (StringUtils.isEmpty(pwd)) {
                    ToastUtils.showToast(activity, getString(R.string.str_please_input_assets_pwd));
                } else if (pwd.length() != 6) {
                    ToastUtils.showToast(activity, getString(R.string.text_money_pwd_tag));
                } else {
                    if (isUpdate) {
                        presenter.doUpdateBank(payWaySetting.getId(), GlobalConstant.BANK, account, bank, branch, pwd, warn, dayLimit, url);
                    } else {
                        presenter.doBindBank(GlobalConstant.BANK, account, bank, branch, pwd, warn, dayLimit, url);
                    }
                    deleteDialog.dismiss();
                }

            }
        });
        deleteDialog.setTitle(getResources().getString(R.string.str_please_input_assets_pwd));
        deleteDialog.show();
    }

}
