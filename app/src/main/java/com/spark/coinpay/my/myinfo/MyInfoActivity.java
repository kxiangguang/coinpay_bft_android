package com.spark.coinpay.my.myinfo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.NormalDialog;
import com.google.gson.Gson;
import com.spark.coinpay.MyApplication;
import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.bind_phone.BindPhoneActivity;
import com.spark.coinpay.login.LoginActivity;
import com.spark.coinpay.my.credit.CreditActivity;
import com.spark.modulebase.entity.User;
import com.spark.modulebase.utils.ActivityManage;
import com.spark.modulebase.utils.AppUtils;
import com.spark.modulebase.utils.BitmapUtils;
import com.spark.modulebase.utils.FileUtils;
import com.spark.modulebase.utils.LogUtils;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;
import com.spark.modulebase.utils.UriUtils;
import com.spark.modulebase.widget.CircleImageView;
import com.spark.moduleotc.entity.AuthenticationStatusEntity;
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
import static com.spark.modulebase.base.BaseConstant.SUCCESS_CODE;

/**
 * 账户设置
 */
public class MyInfoActivity extends BaseActivity implements MyInfoContract.MyInfoView {


    @BindView(R.id.ivHeader)
    CircleImageView ivHeader;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.llPhone)
    LinearLayout llPhone;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.llEmail)
    LinearLayout llEmail;
    @BindView(R.id.tvIdCard)
    TextView tvIdCard;
    @BindView(R.id.llIdCard)
    LinearLayout llIdCard;
    @BindView(R.id.llAvatar)
    LinearLayout llAvatar;

    private MyInfoPresenterImpl presenter;
    private String url;
    private File imageFile;
    private String filename = "header.jpg";
    private Uri imageUri;
    private int statusRealName = 0;//实名认证状态:0-未认证 1待审核 2-审核不通过  3-已认证

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
                    if (ivHeader != null) {
                        Bitmap bitmap = BitmapUtils.zoomBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()), ivHeader.getWidth(), ivHeader.getHeight());
                        String base64Data = BitmapUtils.imgToBase64(bitmap);
                        bitmap.recycle();
                        presenter.uploadBase64Pic("data:image/jpeg;base64," + base64Data);
                    }
                    break;
            }
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_my_info;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.str_account_set));
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new MyInfoPresenterImpl(this);
        imageFile = FileUtils.getCacheSaveFile(this, filename);
        String phone = MyApplication.getAppContext().getCurrentUser().getMobilePhone();
        if (StringUtils.isNotEmpty(phone)) {
            if (phone.startsWith("86")) {
                phone = phone.substring(2, phone.length());
            }
            phone = AppUtils.addStar(phone);
            tvPhone.setText(phone);
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
        //获取用户信息
        presenter.getUserInfo();
        //查询派单前实名认证、承兑商身份认证、设置交易密码、设置收款方式状态
        presenter.findAuthenticationStatus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @OnClick({R.id.llAvatar, R.id.llPhone, R.id.llIdCard, R.id.llEmail})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.llAvatar:
                actionSheetDialogNoTitle();
                break;
            case R.id.llPhone:
                String phone = MyApplication.getAppContext().getCurrentUser().getMobilePhone();
                if (StringUtils.isEmpty(phone)) {
                    showActivity(BindPhoneActivity.class, null, 2);
                } else {
                    if (phone.startsWith("86")) {
                        phone = phone.substring(2, phone.length());
                    }
                    bundle = new Bundle();
                    bundle.putString("phone", phone);
                    bundle.putBoolean("isChg", true);
                    showActivity(BindPhoneActivity.class, bundle, 2);
                }
                break;
            case R.id.llIdCard: // 实名认证
                bundle = new Bundle();
                bundle.putInt("NoticeType", statusRealName);
                showActivity(CreditActivity.class, bundle, 2);
                break;
            case R.id.llEmail:
                break;
        }
    }

    /**
     * 选择头像弹框
     */
    private void actionSheetDialogNoTitle() {
        final String[] stringItems = {getString(R.string.str_photo), getString(R.string.str_album)};
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

    /**
     * 根据安全参数显示
     */
    private void fillViews() {
        if (statusRealName == 0) {  // 未审核
            tvIdCard.setText(R.string.str_unverified); // 未审核
        } else if (statusRealName == 1) { // 审核中
            tvIdCard.setText(R.string.str_creditting);
        } else if (statusRealName == 2) { // 审核失败
            tvIdCard.setText(R.string.str_creditfail); // 审核失败
        } else if (statusRealName == 3) { // 已认证
            tvIdCard.setText(R.string.str_verification);
        }
    }


    @Override
    public void uploadBase64PicSuccess(String obj) {
        url = obj;
        presenter.avatar(obj);
    }

    @Override
    public void avatarSuccess(String obj) {
        User user = MyApplication.getAppContext().getCurrentUser();
        user.setAvatar(url);
        MyApplication.getAppContext().setCurrentUser(user);
        Glide.with(this).load(url).into(ivHeader);
    }

    @Override
    public void findAuthenticationStatusSuccess(String response) {
        //解析认证状态数据
        AuthenticationStatusEntity entity = new Gson().fromJson(response, AuthenticationStatusEntity.class);
        //实名认证状态:0-未认证 1待审核 2-审核不通过  3-已认证
        //承兑商认证状态 0：未认证 1：认证-待审核 2：认证-审核成功 3：认证-审核失败 5：退保-待审核 6：退保-审核失败 7:退保-审核成功 8:退保-已退还保证金
        if (entity != null && entity.getCode() == SUCCESS_CODE) {
            statusRealName = entity.getData().getIsReal().getStatus();
            fillViews();
        }
    }

    /**
     * 获取用户信息成功
     *
     * @param user
     */
    @Override
    public void getUserInfoSuccess(User user) {
        if (user != null && StringUtils.isNotEmpty(user.getAvatar())) {
            Glide.with(this).load(user.getAvatar()).into(ivHeader);
        }
    }

}
