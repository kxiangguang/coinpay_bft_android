package com.spark.coinpay.acceptances.authentication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.modulebase.utils.BitmapUtils;
import com.spark.modulebase.utils.FileUtils;
import com.spark.modulebase.utils.LogUtils;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;
import com.spark.modulebase.utils.UriUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.coinpay.GlobalConstant.CHOOSE_ALBUM;
import static com.spark.coinpay.GlobalConstant.TAKE_PHOTO;

/**
 * 承兑商认证
 */
public class AuthenticationActivity extends BaseActivity implements AuthentucatuinContract.AuthentucatuinView {
    @BindView(R.id.ivSelf)
    ImageView ivSelf;
    @BindView(R.id.ivAssets)
    ImageView ivAssets;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.tvApplyNow)
    TextView tvApplyNow;

    private String assetImg;//个人资产证明
    private String tradeDataImg;//数字资产证明
    private AuthentucatuinPresenterImpl presenter;
    private Long id;
    private Uri imageUri;
    private File imageFile;
    private ImageView currentImg;
    private boolean isSelf;
    private String filename = "asset.jpg";

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_authentication;
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
                    if (currentImg != null) {
                        Bitmap bitmap = BitmapUtils.zoomBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()), currentImg.getWidth(), currentImg.getHeight());
                        String base64Data = BitmapUtils.imgToBase64(bitmap);
                        bitmap.recycle();
                        presenter.doUpLoad("data:image/jpeg;base64," + base64Data);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.str_detify));
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new AuthentucatuinPresenterImpl(this);
        imageFile = FileUtils.getCacheSaveFile(this, filename);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getLong("id");
        }
    }

    @OnClick({R.id.llSelf, R.id.llAssets, R.id.tvApplyNow})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.llSelf:
                isSelf = true;
                currentImg = ivSelf;
                break;
            case R.id.llAssets:
                isSelf = false;
                currentImg = ivAssets;
                break;
            case R.id.tvApplyNow:
                checkInput();
                return;
        }
        actionSheetDialogNoTitle();
    }

    @Override
    protected void setListener() {
        super.setListener();
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvApplyNow.setEnabled(true);
                    tvApplyNow.setBackgroundResource(R.drawable.ripple_btn_global_option_corner_selector);
                    tvApplyNow.setTextColor(getResources().getColor(R.color.white));
                } else {
                    tvApplyNow.setEnabled(false);
                    tvApplyNow.setBackgroundResource(R.drawable.shape_bg_normal_corner_grey_enabled);
                    tvApplyNow.setTextColor(getResources().getColor(R.color.font_main_content));
                }
            }
        });
    }

    @Override
    protected void checkInput() {
        super.checkInput();
        if (!checkbox.isChecked()) {
            ToastUtils.showToast(activity, getString(R.string.str_agree_tag));
            return;
        }
//        if (StringUtils.isEmpty(assetImg)) {
//            ToastUtils.showToast(activity, getString(R.string.str_please_up) + getString(R.string.str_self));
//        } else if (StringUtils.isEmpty(tradeDataImg)) {
//            ToastUtils.showToast(activity, getString(R.string.str_please_up) + getString(R.string.str_assets));
//        } else {
//            presenter.doAuthencation(id, assetImg, "", tradeDataImg);
//        }
        presenter.doAuthencation(id, "", "", "");
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
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
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
    public void doUpLoadSuccess(String response) {
        Glide.with(this).load(response).into(currentImg);
        if (isSelf) {
            assetImg = response;
        } else {
            tradeDataImg = response;
        }
    }

    @Override
    public void doAuthencationSuccess(String response) {
        ToastUtils.showToast(activity, response);
        setResult(RESULT_OK);
        finish();
    }
}
