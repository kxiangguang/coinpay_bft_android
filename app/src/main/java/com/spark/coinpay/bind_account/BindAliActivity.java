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
import com.spark.coinpay.GlobalConstant;
import com.spark.coinpay.MyApplication;
import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.entity.PayWaySetting;
import com.spark.coinpay.view.DeleteDialog;
import com.spark.library.acp.model.MessageResult;
import com.spark.modulebase.entity.User;
import com.spark.modulebase.utils.BitmapUtils;
import com.spark.modulebase.utils.FileUtils;
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
 * 绑定支付宝或者微信
 */
public class BindAliActivity extends BaseActivity implements BindAccountContract.AliView {
    @BindView(R.id.ivIdFace)
    ImageView idFaceImg;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etAccount)
    EditText etAccount;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.llAli)
    LinearLayout llAli;
    @BindView(R.id.llWeChat)
    LinearLayout llWeChat;
    @BindView(R.id.etWechatAccount)
    EditText etWechatAccount;
    @BindView(R.id.etWarn)
    EditText etWarn;
    @BindView(R.id.etDay)
    EditText etDay;
    @BindView(R.id.etAccountId)
    EditText etAccountId;
    @BindView(R.id.tvGetAccountId)
    TextView tvGetAccountId;
    @BindView(R.id.etAccountNickName)
    EditText etAccountNickName;
    @BindView(R.id.tvWechatAccountNickName)
    EditText tvWechatAccountNickName;

    private String payWay;
    private Uri imageUri;
    private File imageFile;
    private String filename = "qrCode.jpg";


    private BindAliPresenterImpl presenter;
    private String url = "";
    private PayWaySetting payWaySetting;
    private boolean isUpdate = false;//添加或者更新
    String account = "";
    String accountId = "";
    String accountNickname = "";
    String name = "";
    String warn = "";
    String dayLimit = "";
    private DeleteDialog deleteDialog;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_bind_ali;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
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
        imageFile = FileUtils.getCacheSaveFile(this, filename);
        presenter = new BindAliPresenterImpl(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            payWay = bundle.getString("payWay");
            payWaySetting = (PayWaySetting) bundle.getSerializable("data");
            if (payWaySetting != null) {
                isUpdate = true;
                tvConfirm.setText(getResources().getString(R.string.str_text_change));
                if (GlobalConstant.ALIPAY.equals(payWay)) {
                    etAccount.setText(payWaySetting.getPayAddress());
                    etAccountId.setText(payWaySetting.getExpandField());
                    etAccountNickName.setText(payWaySetting.getNickname());
                } else if (GlobalConstant.WECHAT.equals(payWay)) {
                    etWechatAccount.setText(payWaySetting.getPayAddress());
                    tvWechatAccountNickName.setText(payWaySetting.getNickname());
                }
                if (StringUtils.isNotEmpty(payWaySetting.getQrCodeUrl())) {
                    url = payWaySetting.getQrCodeUrl().toString();
                    Glide.with(this).load(url).into(idFaceImg);
                }
                etWarn.setText(payWaySetting.getPayNotes());
                etDay.setText(MathUtils.subZeroAndDot(payWaySetting.getDayLimit() + ""));
            }
        }
        if (isUpdate) {
            if (GlobalConstant.ALIPAY.equals(payWay)) {
                tvTitle.setText(getString(R.string.str_text_change) + getString(R.string.str_ali) + getString(R.string.str_account_account));
                llAli.setVisibility(View.VISIBLE);
                llWeChat.setVisibility(View.GONE);
            } else if (GlobalConstant.WECHAT.equals(payWay)) {
                tvTitle.setText(getString(R.string.str_text_change) + getString(R.string.str_wechat) + getString(R.string.str_account_account));
                llAli.setVisibility(View.GONE);
                llWeChat.setVisibility(View.VISIBLE);
            }
        } else {
            if (GlobalConstant.ALIPAY.equals(payWay)) {
                tvTitle.setText(getString(R.string.str_account_seting) + getString(R.string.str_ali) + getString(R.string.str_account_account));
                llAli.setVisibility(View.VISIBLE);
                llWeChat.setVisibility(View.GONE);
            } else if (GlobalConstant.WECHAT.equals(payWay)) {
                tvTitle.setText(getString(R.string.str_account_seting) + getString(R.string.str_wechat) + getString(R.string.str_account_account));
                llAli.setVisibility(View.GONE);
                llWeChat.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick({R.id.ivIdFace, R.id.tvConfirm, R.id.tvGetAccountId})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.ivIdFace:
                actionSheetDialogNoTitle();
                break;
            case R.id.tvConfirm:
                confirm();
                break;
            case R.id.tvGetAccountId:
                showActivity(BindAliAccountIdActivity.class, null);
                break;
        }
    }

    private void confirm() {
        if (payWay.equals(GlobalConstant.ALIPAY)) {
            account = etAccount.getText().toString().trim();
            accountId = etAccountId.getText().toString().trim();
            accountNickname = etAccountNickName.getText().toString().trim();
            if (StringUtils.isEmpty(accountId)) {
//                ToastUtils.showToast(this, R.string.incomplete_information);
//                return;
            }
        } else if (payWay.equals(GlobalConstant.WECHAT)) {
            account = etWechatAccount.getText().toString().trim();
            accountNickname = tvWechatAccountNickName.getText().toString().trim();
        }
        name = etName.getText().toString();
        warn = etWarn.getText().toString();
        dayLimit = etDay.getText().toString();
        if (StringUtils.isEmpty(name, account, url, dayLimit, accountNickname)) {
            ToastUtils.showToast(this, R.string.incomplete_information);
        } else {
            showReleaseDialog();
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

    @Override
    public void doBindAliOrWechatSuccess(MessageResult obj) {
        ToastUtils.showToast(this, obj.getMessage());
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void doUpdateAliOrWechatSuccess(MessageResult obj) {
        ToastUtils.showToast(this, obj.getMessage());
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
                        if (payWay.equals(GlobalConstant.ALIPAY)) {
                            presenter.getUpdateAliOrWechat(payWaySetting.getId(), payWay, account, getString(R.string.str_ali), "", pwd, url, warn, dayLimit, accountId, accountNickname, name);
                        } else {
                            presenter.getUpdateAliOrWechat(payWaySetting.getId(), payWay, account, getString(R.string.str_wechat), "", pwd, url, warn, dayLimit, accountId, accountNickname, name);
                        }
                    } else {
                        if (payWay.equals(GlobalConstant.ALIPAY)) {
                            presenter.getBindAliOrWechat(payWay, account, getString(R.string.str_ali), "", pwd, url, warn, dayLimit, accountId, accountNickname, name);
                        } else {
                            presenter.getBindAliOrWechat(payWay, account, getString(R.string.str_wechat), "", pwd, url, warn, dayLimit, accountId, accountNickname, name);
                        }
                    }
                    deleteDialog.dismiss();
                }

            }
        });
        deleteDialog.setTitle(getResources().getString(R.string.str_please_input_assets_pwd));
        deleteDialog.show();
    }

}
