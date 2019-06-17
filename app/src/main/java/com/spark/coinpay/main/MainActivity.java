package com.spark.coinpay.main;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.coinpay.GlobalConstant;
import com.spark.coinpay.LoginStatus;
import com.spark.coinpay.MyApplication;
import com.spark.coinpay.R;
import com.spark.coinpay.acceptances.level.AcceptancesLevelActivity;
import com.spark.coinpay.bind_account.BindAccountActivity;
import com.spark.coinpay.entity.PayData;
import com.spark.coinpay.entity.VisionEntity;
import com.spark.coinpay.event.CheckLoginSuccessEvent;
import com.spark.coinpay.event.HasPayEvent;
import com.spark.coinpay.event.LoginoutEvent;
import com.spark.coinpay.login.LoginActivity;
import com.spark.coinpay.main.buy.BuyActivity;
import com.spark.coinpay.main.order_detail.OrderDetailActivity;
import com.spark.coinpay.my.MyActivity;
import com.spark.coinpay.my.account_pwd.AccountPwdActivity;
import com.spark.coinpay.my.assets.recharge.RechargeActivity;
import com.spark.coinpay.my.credit.CreditActivity;
import com.spark.coinpay.overtime_order.OverTimeOrderListActivity;
import com.spark.coinpay.view.AlertIosDialog;
import com.spark.coinpay.view.AppVersionDialog;
import com.spark.library.acp.model.AcceptMerchantTrade;
import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.MessageResultAcceptMerchantTrade;
import com.spark.library.acp.model.MessageResultPageOrderInTransit;
import com.spark.library.acp.model.OrderInTransit;
import com.spark.library.acp.model.PageOrderInTransit;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.BaseApplication;
import com.spark.modulebase.event.CheckLoginEvent;
import com.spark.modulebase.base.BaseConstant;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.entity.LoadExceptionEvent;
import com.spark.modulebase.entity.User;
import com.spark.modulebase.event.LoginoutWithoutApiEvent;
import com.spark.modulebase.okhttp.FileCallback;
import com.spark.modulebase.okhttp.OkhttpUtils;
import com.spark.modulebase.utils.ActivityManage;
import com.spark.modulebase.utils.AppUtils;
import com.spark.modulebase.utils.DateUtils;
import com.spark.modulebase.utils.FileUtils;
import com.spark.modulebase.utils.LogUtils;
import com.spark.modulebase.utils.MathUtils;
import com.spark.modulebase.utils.NetworkUtil;
import com.spark.modulebase.utils.SharedPreferencesUtil;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;
import com.spark.modulelogin.entity.CasLoginEntity;
import com.spark.moduleotc.entity.AuthenticationStatusEntity;
import com.spark.netty_library.CMD;
import com.spark.netty_library.ConnectCloseEvent;
import com.spark.netty_library.GuardService;
import com.spark.netty_library.JobWakeUpService;
import com.spark.netty_library.ServiceOpenSuccessEvent;
import com.spark.netty_library.ServiceTypeEvent;
import com.spark.netty_library.WebSocketService;
import com.spark.netty_library.listener.SendMsgListener;
import com.spark.netty_library.message.SocketMessage;
import com.spark.netty_library.message.SocketResponse;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import okhttp3.Request;

import static com.spark.coinpay.GlobalConstant.TYPE_AC;
import static com.spark.coinpay.GlobalConstant.TYPE_OTC;
import static com.spark.coinpay.GlobalConstant.TYPE_UC;
import static com.spark.moduleassets.AssetsConstants.ACP;
import static com.spark.moduleassets.AssetsConstants.COMMON;
import static com.spark.modulebase.base.BaseConstant.SUCCESS_CODE;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_AHTH;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_APP_NAME;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_AUTO_ACCEPT;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_VOICE;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_PAY_PASS;

/**
 * 主页面
 */
public class MainActivity extends OrderActivity implements MainContract.MainView {

    @BindView(R.id.tvStartOrder)
    TextView tvStartOrder;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvCommission)
    TextView tvCommission;
    @BindView(R.id.tvFinishRate)
    TextView tvFinishRate;
    @BindView(R.id.tvOrderSpeed)
    TextView tvOrderSpeed;
    @BindView(R.id.tvOrderCount)
    TextView tvOrderCount;
    @BindView(R.id.llNotStartOrder)
    LinearLayout llNotStartOrder;
    @BindView(R.id.llStopOrder)
    LinearLayout llStopOrder;
    @BindView(R.id.llOrderDetail)
    LinearLayout llOrderDetail;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.ivType)
    ImageView ivType;
    @BindView(R.id.tvDirection)
    TextView tvDirection;
    @BindView(R.id.tvCount)
    TextView tvCount;
    @BindView(R.id.tvOrderCreateTime)
    TextView tvOrderCreateTime;
    @BindView(R.id.tvTypeCNY)
    TextView tvTypeCNY;
    @BindView(R.id.tvPayCount)
    TextView tvPayCount;
    @BindView(R.id.tvRate)
    TextView tvRate;
    @BindView(R.id.tvPayRefer)
    TextView tvPayRefer;
    @BindView(R.id.ivAuthStatusRealName)
    ImageView ivAuthStatusRealName;
    @BindView(R.id.tvAuthStatusRealName)
    TextView tvAuthStatusRealName;
    @BindView(R.id.ivAuthStatusRealName2)
    ImageView ivAuthStatusRealName2;
    @BindView(R.id.tvAuthStatusRealNameSet)
    TextView tvAuthStatusRealNameSet;
    @BindView(R.id.ivAuthStatusAcceptanceMerchant)
    ImageView ivAuthStatusAcceptanceMerchant;
    @BindView(R.id.tvAuthStatusAcceptanceMerchant)
    TextView tvAuthStatusAcceptanceMerchant;
    @BindView(R.id.ivAuthStatusAcceptanceMerchant2)
    ImageView ivAuthStatusAcceptanceMerchant2;
    @BindView(R.id.tvAuthStatusAcceptanceMerchantSet)
    TextView tvAuthStatusAcceptanceMerchantSet;
    @BindView(R.id.ivAuthStatusPass)
    ImageView ivAuthStatusPass;
    @BindView(R.id.tvAuthStatusPass)
    TextView tvAuthStatusPass;
    @BindView(R.id.ivAuthStatusPass2)
    ImageView ivAuthStatusPass2;
    @BindView(R.id.tvAuthStatusPassSet)
    TextView tvAuthStatusPassSet;
    @BindView(R.id.ivAuthStatusType)
    ImageView ivAuthStatusType;
    @BindView(R.id.tvAuthStatusType)
    TextView tvAuthStatusType;
    @BindView(R.id.ivAuthStatusType2)
    ImageView ivAuthStatusType2;
    @BindView(R.id.tvAuthStatusTypeSet)
    TextView tvAuthStatusTypeSet;
    @BindView(R.id.llAuthStatus)
    LinearLayout llAuthStatus;
    @BindView(R.id.tvOrderingOrNot)
    TextView tvOrderingOrNot;
    @BindView(R.id.llAccectOrder)
    LinearLayout llAccectOrder;
    @BindView(R.id.tvPayTime)
    TextView tvPayTime;
    @BindView(R.id.llRelease)
    LinearLayout llRelease;
    @BindView(R.id.tvRealease)
    TextView tvRealease;
    @BindView(R.id.tvRealeaseTime)
    TextView tvRealeaseTime;
    @BindView(R.id.tvPay)
    TextView tvPay;
    @BindView(R.id.ivOverTimeOrder)
    ImageView ivOverTimeOrder;
    @BindView(R.id.tvAdd)
    TextView tvAdd;
    @BindView(R.id.ivPayReferCopy)
    ImageView ivPayReferCopy;

    private static final short ORDER_CHANNEL = 0;
    private long lastPressTime;//首页二次返回时间
    private boolean isStart = false;//是否开始接单：true开始接单  false停止接单
    private boolean isHas = false;//是否有订单：true有订单  false没有订单
    private boolean isShort = false;//账户余额是否不足：true不足  false正常
    private MainPresenterImpl presenter;
    private LoginStatus loginStatus;
    private String type;//接单类型： 卖币 买币 全部
    private int takingType = 1;//接单类型： 1卖币 2买币 3全部
    private boolean isShowLoading = true;
    private int statusRealName = 0;//实名认证状态:0-未认证 1待审核 2-审核不通过  3-已认证
    private MediaPlayer mediaPlayer;//播放音频
    //防止多次跳转401监听
    private boolean isTokenUnUsed = false;
    private boolean isHasPay = false;//由长连接指令20004获取的支付状态

    //倒计时
    private long randomTime;//订单剩余时间（毫秒）
    private int position = 1;//1待付款 2待放行
    //websocket
    private WebSocketService webSocketService;//接单webSocketService
    private GuardService guardService;//接单webSocketService
    private ServiceConnection mConnection;
    private ServiceConnection mConnection2;
    private ArrayList<Wallet> walletsTrade = new ArrayList<>();
    private List<PayData> payDatas;
    private PowerManager.WakeLock mWakeLock = null;//唤醒锁
    private KeyguardManager.KeyguardLock keyguardLock = null;//禁止锁屏
    private boolean isWebSocketService = true;
    private String orderId;//取消订单、放行、支付使用
    private AlertIosDialog alertIosDialog;
    private ProgressDialog progressDialog;
    private AppVersionDialog appVersionDialog;
    private boolean isCheckVersion = false;//首次检测版本更新
    private boolean isNeedReconnect = false;//是否需要重连websocket
    private boolean isGoToLogin = false;//保证登录界面只跳转一次

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initReceiver();
    }

    /**
     * 注册网络监听的广播
     */
    private void initReceiver() {
        IntentFilter timeFilter = new IntentFilter();
        timeFilter.addAction("android.net.ethernet.ETHERNET_STATE_CHANGED");
        timeFilter.addAction("android.net.ethernet.STATE_CHANGE");
        timeFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        timeFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        timeFilter.addAction("android.net.wifi.STATE_CHANGE");
        timeFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(netReceiver, timeFilter);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1://刷新首页数据
                    getMainInfo(false);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        long now = System.currentTimeMillis();
        if (lastPressTime == 0 || now - lastPressTime > 2 * 1000) {
            ToastUtils.showToast(activity, getString(R.string.str_exit_again));
            lastPressTime = now;
        } else if (now - lastPressTime < 2 * 1000) {
            if (isWebSocketService) {
                if (webSocketService != null)
                    webSocketService.closeWebsocket(false);
            } else {
                if (guardService != null)
                    guardService.closeWebsocket();
            }
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mConnection != null) {
            if (isWebSocketService)
                unbindService(mConnection);
        }
        if (mConnection2 != null) {
            if (!isWebSocketService)
                unbindService(mConnection2);
        }
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();//释放资源
        }
        mTimeHandler.removeCallbacks(runnable);
        presenter.destory();
        releaseWakeLock();
        releaseKeyguardLock();

        if (netReceiver != null) {
            unregisterReceiver(netReceiver);
            netReceiver = null;
        }
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        String appName = SharedPreferencesUtil.getInstance(activity).getStringParam(SP_KEY_APP_NAME);
        tvTitle.setText(appName);
    }

    @Override
    protected void initData() {
        super.initData();
        bindSocketService();
        presenter = new MainPresenterImpl(this);
        initProgressDialog();
    }

    /**
     * 打开raw目录下的音乐mp3文件
     */
    private void openRawMusic(int type) {
        if (SharedPreferencesUtil.getInstance(activity).contains(SP_KEY_VOICE)) {
            if (!SharedPreferencesUtil.getInstance(activity).getBooleanParam(SP_KEY_VOICE)) {
                return;
            }
        }
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
        } else {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setLooping(false);
        }
        switch (type) {
            case 1:
                mediaPlayer = MediaPlayer.create(this, R.raw.start);
                break;
            case 2:
                mediaPlayer = MediaPlayer.create(this, R.raw.stop);
                break;
            case 3:
                mediaPlayer = MediaPlayer.create(this, R.raw.new_);
                break;
            case 4:
                mediaPlayer = MediaPlayer.create(this, R.raw.pay);
                break;
            case 5:
                mediaPlayer = MediaPlayer.create(this, R.raw.paying);
                break;
            case 6:
                mediaPlayer = MediaPlayer.create(this, R.raw.auto_release_success);
                break;
            case 7:
                mediaPlayer = MediaPlayer.create(this, R.raw.auto_release_fail);
                break;
            case 8:
                mediaPlayer = MediaPlayer.create(this, R.raw.connect_close);
                break;
        }
        mediaPlayer.start();
    }


    /**
     * 加载数据
     */
    @Override
    protected void loadData() {
        super.loadData();
        loginStatus = MyApplication.getMyApplication().getLoginStatus();
        getMainInfo(true);
        mTimeHandler.sendEmptyMessageDelayed(3, 5000);
    }

    /**
     * 加载数据
     */
    private void getMainInfo(boolean isShow) {
        isShowLoading = isShow;
        //查询派单前实名认证、承兑商身份认证、设置交易密码、设置收款方式状态
        presenter.findAuthenticationStatus();
    }

    /**
     * 查询超时单
     */
    private void getList(boolean isShow) {
        HashMap<String, String> map = new HashMap<>();
        map.put("pageNo", 1 + "");
        map.put("pageSize", 1 + "");
        map.put("status", "5");
        presenter.getOrder(isShow, map);
    }

    @Override
    public void getOrderSuccess(List<OrderInTransit> list) {
        if (list == null) return;
        if (list.size() > 0) {
            ivOverTimeOrder.setVisibility(View.VISIBLE);
        } else {
            ivOverTimeOrder.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showLoading() {
        if (!isShowLoading) {
            return;
        }
        if (randomTime < 0) {//倒计时结束时randomTime<0,此时不展示加载进度条
            return;
        }
        super.showLoading();
    }

    @OnClick({R.id.ivMy, R.id.tvStartOrder, R.id.llOrderDetail, R.id.tvAuthStatusRealNameSet, R.id.tvAuthStatusAcceptanceMerchantSet,
            R.id.tvAuthStatusPassSet, R.id.tvAuthStatusTypeSet, R.id.ivOverTimeOrder, R.id.tvAdd, R.id.ivPayReferCopy, R.id.ivBuy})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.ivMy://我的
                showActivity(MyActivity.class, null);
                break;
            case R.id.tvStartOrder:
                if (!isStart) {//开始接单
                    //showOrderDialog();
                    type = getString(R.string.str_duiru);
                    takingType = 1;
                    startOrder();
                } else {//停止接单
                    stopOrder();
                }
                break;
            case R.id.llOrderDetail://跳转到详情
                showActivity(OrderDetailActivity.class, null, 1);
                break;
            case R.id.tvAuthStatusRealNameSet://实名认证-去设置
                Bundle bundle = new Bundle();
                bundle.putInt("NoticeType", statusRealName);
                showActivity(CreditActivity.class, bundle, 1);
                break;
            case R.id.tvAuthStatusAcceptanceMerchantSet://承兑商身份认证-去设置
                bundle = new Bundle();
                bundle.putBoolean("isMain", true);
                showActivity(AcceptancesLevelActivity.class, bundle, 1);
                break;
            case R.id.tvAuthStatusPassSet://设置交易密码-去设置
                showActivity(AccountPwdActivity.class, null, 1);
                break;
            case R.id.tvAuthStatusTypeSet://设置收款方式-去设置
                //交易密码是否设置
                if (SharedPreferencesUtil.getInstance(activity).getBooleanParam(SP_PAY_PASS)) {
                    bundle = new Bundle();
                    bundle.putBoolean("isMain", true);
                    showActivity(BindAccountActivity.class, bundle, 1);
                } else {
                    ToastUtils.showToast(activity, getString(R.string.set_money_pwd_first));
                }
                break;
            case R.id.ivOverTimeOrder://超时单列表
                showActivity(OverTimeOrderListActivity.class, null, 1);
                break;
            case R.id.tvAdd://去充值
                //查询交易账户
                presenter.getWallet(COMMON);
                break;
            case R.id.ivPayReferCopy://复制
                copyText(tvPayRefer.getText().toString());
                break;
            case R.id.ivBuy://一键买币
                showActivity(BuyActivity.class, null, 1);
                break;
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMainInfo(false);
            }
        });
    }

    /**
     * 开始接单-发送登录指令
     */
    private void startLogin() {
        String sid = SharedPreferencesUtil.getInstance(BaseApplication.getAppContext()).getSid();
        HashMap<String, String> params = new HashMap<>();
        params.put("sid", sid);
        LogUtils.e("开始接单-发送登录指令sid==" + sid);
        String body = new Gson().toJson(params);
        if (isWebSocketService) {
            if (webSocketService != null)
                webSocketService.sendData(new SocketMessage(ORDER_CHANNEL, CMD.LOGIN, body.getBytes()), sendMsgListener);
        } else {
            if (guardService != null)
                guardService.sendData(new SocketMessage(ORDER_CHANNEL, CMD.LOGIN, body.getBytes()), sendMsgListener);
        }

    }

    /**
     * 开始接单-发送连接指令
     */
    private void startConnect() {
        if (isWebSocketService) {
            if (webSocketService != null)
                webSocketService.sendData(new SocketMessage(ORDER_CHANNEL, CMD.CONNECT, null), sendMsgListener);
        } else {
            if (guardService != null)
                guardService.sendData(new SocketMessage(ORDER_CHANNEL, CMD.CONNECT, null), sendMsgListener);
        }
    }

    /**
     * 开始接单-发送接单指令
     */
    private void startOrder() {
        HashMap<String, Integer> params = new HashMap<>();
        params.put("takingType", takingType);
        String body = new Gson().toJson(params);
        if (isWebSocketService) {
            if (webSocketService != null)
                webSocketService.sendData(new SocketMessage(ORDER_CHANNEL, CMD.START_ORDER, body.getBytes()), sendMsgListener);
        } else {
            if (guardService != null)
                guardService.sendData(new SocketMessage(ORDER_CHANNEL, CMD.START_ORDER, body.getBytes()), sendMsgListener);
        }
    }

    /**
     * 停止接单-发送停止接单指令
     */
    private void stopOrder() {
        if (isWebSocketService) {
            if (webSocketService != null)
                webSocketService.sendData(new SocketMessage(ORDER_CHANNEL, CMD.STOP_ORDER, null), sendMsgListener);
        } else {
            if (guardService != null)
                guardService.sendData(new SocketMessage(ORDER_CHANNEL, CMD.STOP_ORDER, null), sendMsgListener);
        }
    }

    /**
     * 选择接单类型
     */
    private void showOrderDialog() {
        final String[] stringItems = {getString(R.string.str_duiru), getString(R.string.str_duichu), getString(R.string.str_all)};
        final ActionSheetDialog dialog = new ActionSheetDialog(activity, stringItems, null);
        dialog.isTitleShow(false).itemTextColor(getResources().getColor(R.color.font_main_title))
                .cancelText(getResources().getColor(R.color.font_main_content))
                .cancelText(getResources().getString(R.string.str_cancel)).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                type = stringItems[position];
                takingType = position + 1;
                startOrder();
            }
        });
    }

    /**
     * 修改状态
     */
    private void changeStatus(boolean start) {
        isStart = start;
        if (tvStartOrder != null) {
            tvStartOrder.setText(isStart ? R.string.str_stop_order : R.string.str_start_order);
        }
        if (tvStatus != null) {
            tvStatus.setText(isStart ? R.string.str_ordering : R.string.str_not_start_order);
        }
        if (tvType != null) {
            if (isStart) {
                tvType.setText(type);
            } else {
                tvType.setText(R.string.str_duiru);
            }
        }
    }

    /**
     * 检测登录状态
     */
    public void onEvent(CheckLoginEvent event) {
        if (event.type.contains(TYPE_OTC)) {
            presenter.checkBusinessLogin(TYPE_OTC);
        } else if (event.type.contains(TYPE_UC)) {
            presenter.checkBusinessLogin(TYPE_UC);
        } else if (event.type.contains(TYPE_AC)) {
            presenter.checkBusinessLogin(TYPE_AC);
        }
    }

    @Override
    public void checkBusinessLoginSuccess(CasLoginEntity casLoginEntity) {
        if (casLoginEntity != null) {
            String type = casLoginEntity.getType();
            if (!casLoginEntity.isLogin()) {
                String gtc = MyApplication.getMyApplication().getCurrentUser().getGtc();
                presenter.doLoginBusiness(gtc, type);
            } else {
                setLogin(type);
            }
        }
    }

    @Override
    public void doLoginBusinessSuccess(String type) {
        setLogin(type);
        getMainInfo(false);
        EventBus.getDefault().post(new CheckLoginSuccessEvent());
    }

    /**
     * 解析今日接单数等数据
     *
     * @param response
     */
    @Override
    public void findAcceptMerchantTradeSuccess(MessageResultAcceptMerchantTrade response) {
        mTimeHandler.sendEmptyMessage(5);
        //解析今日接单数等数据
        AcceptMerchantTrade trade = response.getData();
        initMerchantTradeInfo(trade);
    }

    /**
     * 设置今日接单数等信息
     *
     * @param trade
     */
    private void initMerchantTradeInfo(AcceptMerchantTrade trade) {
        if (trade != null) {
            int orderCount = trade.getDaysBuyOrder() + trade.getDaysSellOrder();
            tvOrderCount.setText(String.valueOf(orderCount));

            double totalBuyReward = 0;
            double totalSellReward = 0;
            if (trade.getDaysBuyReward() != null) {
                totalBuyReward = trade.getDaysBuyReward().doubleValue();
            }
            if (trade.getDaysSellReward() != null) {
                totalSellReward = trade.getDaysSellReward().doubleValue();
            }
            String reward = MathUtils.subZeroAndDot(MathUtils.getRundNumber(totalBuyReward + totalSellReward, BaseConstant.MONEY_FORMAT, null));
            tvCommission.setText(reward);

            int successCount = trade.getTotalSuccessBuyOrder() + trade.getTotalSuccessSellOrder();
            int totalCount = trade.getTotalBuyOrder() + trade.getTotalSellOrder();
            String rate = AppUtils.getRate(successCount, totalCount);
            tvFinishRate.setText(rate);

            int avgTime = trade.getAvgPayTime() * trade.getTotalBuyOrder() + trade.getAvgReleaseTime() * trade.getTotalSellOrder();
            String speed = AppUtils.getSpeed(avgTime, totalCount);
            tvOrderSpeed.setText(speed);
        }
    }

    /**
     * 查询在途订单成功
     */
    @Override
    public void findOrderInTransitSuccess(MessageResultPageOrderInTransit response) {
        if (response != null) {
            PageOrderInTransit data = response.getData();
            List<OrderInTransit> records = data.getRecords();
            if (records != null && records.size() > 0) {
                orderInTransit = records.get(0);
                //设置倒计时
                mTimeHandler.removeCallbacks(runnable);
                initOrderContent(orderInTransit);
                isHas = true;
                if (isHasPay) {
                    openRawMusic(4);
                    EventBus.getDefault().post(new HasPayEvent());
                    isHasPay = false;
                } else {
                    if (randomTime > 0) {
                        openRawMusic(3);
                    }
                }
                //查询今日接单数等数据
                presenter.findAcceptMerchantTrade();
            } else {
                isHas = false;
                //没有订单不在刷新时间显示
                mTimeHandler.removeMessages(2);
            }
        }
        //设置界面显示状态
        setVisiableStatus();
        mTimeHandler.sendEmptyMessage(5);
    }

    /**
     * 设置订单信息
     *
     * @param orderInTransit
     */
    private void initOrderContent(OrderInTransit orderInTransit) {
        orderId = orderInTransit.getOrderId();
        int direction = orderInTransit.getDirection();

        if (orderInTransit.getActualAmount() == null) {
            tvCount.setText(MathUtils.subZeroAndDot(orderInTransit.getAmount() + ""));
        } else {
            tvCount.setText(MathUtils.subZeroAndDot(orderInTransit.getActualAmount() + ""));
        }
        tvOrderCreateTime.setText(DateUtils.getFormatTime(DateUtils.DATE_FORMAT_1, orderInTransit.getCreateTime()));

        tvPayCount.setText(MathUtils.subZeroAndDot(orderInTransit.getNumber() + ""));
        payDatas = new Gson().fromJson(orderInTransit.getPayData(), new TypeToken<List<PayData>>() {
        }.getType());
        if (payDatas != null && payDatas.size() > 0) {
            PayData curPayData = payDatas.get(0);
            String payType = curPayData.getPayType();
            String result = "";
            switch (payType) {
                case GlobalConstant.ALIPAY:
                    result = AppUtils.getPayAccountStr(curPayData.getPayAddress(), 1, getResources().getString(R.string.str_ali));
                    break;
                case GlobalConstant.WECHAT:
                    result = AppUtils.getPayAccountStr(curPayData.getPayAddress(), 2, getResources().getString(R.string.str_wechat));
                    break;
                case GlobalConstant.BANK:
                    result = AppUtils.getPayAccountStr(curPayData.getPayAddress(), 3, curPayData.getBank());
                    break;
                case GlobalConstant.PAYPAL:
                    result = AppUtils.getPayAccountStr(curPayData.getPayAddress(), 4, getResources().getString(R.string.str_paypal));
                    break;
            }
            tvPayRefer.setText(result);
        }
        tvRate.setText(MathUtils.subZeroAndDot(orderInTransit.getPrice() + ""));

        if (direction == 2) {//买币
            ivType.setImageResource(R.mipmap.icon_duichu);
            tvTypeCNY.setText(R.string.str_duichu_cny);
            tvCount.setTextColor(getResources().getColor(R.color.font_red));
            tvDirection.setText(String.format(getString(R.string.str_get_usdt), orderInTransit.getCoinId()));
            llRelease.setVisibility(View.GONE);
        } else {//卖币
            ivType.setImageResource(R.mipmap.icon_duiru);
            tvTypeCNY.setText(R.string.str_duiru_cny);
            tvCount.setTextColor(getResources().getColor(R.color.font_green));
            tvDirection.setText(String.format(getString(R.string.str_pay_usdt), orderInTransit.getCoinId()));
            llRelease.setVisibility(View.VISIBLE);
        }
        if (StringUtils.isEmpty(orderInTransit.getActualPayment())) {
            tvPay.setText(getString(R.string.str_wait_pay));
            tvRealease.setText(getString(R.string.str_need_release));
            tvRealeaseTime.setText("");
            if (direction == 2) {
                position = 1;
                long endTime = orderInTransit.getCreateTime().getTime() + orderInTransit.getTimeLimit() * 1000;
                long nowTime = new Date().getTime();
                randomTime = endTime - nowTime;
                LogUtils.e("时间差：" + randomTime);
                if (randomTime > 0) {
                    tvPayTime.setText(DateUtils.getTimeDown(randomTime, DateUtils.DATE_FORMAT_2, "", DateUtils.DATE_FORMAT_3));
                    mTimeHandler.post(runnable);//刷新时间
                } else {
                    setTime(0);
                    mTimeHandler.removeCallbacks(runnable);
                    getOrder();
                }
            } else {
                position = 3;
                long endTime = orderInTransit.getCreateTime().getTime() + orderInTransit.getTimeLimit() * 1000;
                long nowTime = new Date().getTime();
                randomTime = endTime - nowTime;
                LogUtils.e("时间差：" + randomTime);
                if (randomTime > 0) {
                    tvPayTime.setText(DateUtils.getTimeDown(randomTime, DateUtils.DATE_FORMAT_2, "", DateUtils.DATE_FORMAT_4));
                    mTimeHandler.post(runnable);//刷新时间
                } else {
                    setTime(0);
                    mTimeHandler.removeCallbacks(runnable);
                    getOrder();
                }

                llRelease.setVisibility(View.GONE);
            }
        } else {
            tvPay.setText(getString(R.string.str_already_pay));
            if (orderInTransit.getStatus() != null) {
                //订单状态 0-已取消 1-未付款 2-已付款 3-已完成 4-申诉中 5.超时单
                if (orderInTransit.getStatus() == 5) {//超时单
                    tvRealease.setText(getString(R.string.str_need_release_out));
                    tvRealeaseTime.setText("");
                } else {
                    tvRealease.setText(getString(R.string.str_need_release));
                    position = 2;
                    long endTime = orderInTransit.getCreateTime().getTime() + orderInTransit.getTimeLimit() * 1000;
                    long nowTime = new Date().getTime();
                    randomTime = endTime - nowTime;
                    LogUtils.e("时间差：" + randomTime);
                    if (randomTime > 0) {
                        tvRealeaseTime.setText(DateUtils.getTimeDown(randomTime, DateUtils.DATE_FORMAT_2, "", DateUtils.DATE_FORMAT_4));
                        mTimeHandler.post(runnable);//刷新时间
                    } else {
                        setTime(0);
                        mTimeHandler.removeCallbacks(runnable);
                        getOrder();
                    }

                }
            } else {
                tvRealease.setText(getString(R.string.str_need_release));
                tvRealeaseTime.setText("");
            }

            tvPayTime.setText(DateUtils.getFormatTime(DateUtils.DATE_FORMAT_1, orderInTransit.getPayTime()));
        }
    }

    private void stopRefreshing() {
        if (refreshLayout != null) {
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void dealError(VolleyError volleyError) {
        super.dealError(volleyError);
        mTimeHandler.sendEmptyMessage(5);
        if (isTokenUnUsed) {
            isTokenUnUsed = false;
        }
    }

    @Override
    public void dealError(HttpErrorEntity httpErrorEntity) {
        super.dealError(httpErrorEntity);
        mTimeHandler.sendEmptyMessage(5);
        if (isTokenUnUsed) {
            isTokenUnUsed = false;
        }
    }

    /**
     * 设置登录状态
     *
     * @param type
     */
    private void setLogin(String type) {
        if (StringUtils.isNotEmpty(type)) {
            switch (type) {
                case TYPE_AC:
                    loginStatus.setAcLogin(true);
                    break;
                case TYPE_UC:
                    loginStatus.setUcLogin(true);
                    break;
                case TYPE_OTC:
                    loginStatus.setOtcLogin(true);
                    break;
            }
        }
    }

    /**
     * TCP连接socket监听
     */
    SendMsgListener sendMsgListener = new SendMsgListener() {

        @Override
        public void onMessageResponse(SocketResponse socketResponse) {
            Message message = Message.obtain();
            String response = socketResponse.getResponse();
            int cmd = socketResponse.getCmd();
            try {
                JSONObject jsonObject = new JSONObject(response);
                int code = jsonObject.optInt("code");
                if (code == SUCCESS_CODE) {
                    switch (cmd) {
                        case CMD.LOGIN:
                            startConnect();
                            break;
                        case CMD.CONNECT:
                            LogUtils.e("连接11003建立成功isStart==" + isStart);
                            if (isStart) {
                                startOrder();
                            }
                            break;
                        case CMD.START_ORDER://收到开始接单指令
                            message.what = CMD.START_ORDER;
                            handler.sendMessage(message);
                            break;
                        case CMD.STOP_ORDER:
                            message.what = CMD.STOP_ORDER;
                            handler.sendMessage(message);
                            break;
                    }
                } else if (code == 30516) {
                    isShort = true;
                    setVisiableStatus();
                } else if (code == 30568) {
                    ToastUtils.showToast("账户被禁用");
                } else if (code == 504) {
                    //{"code":504,"message":"用户会话失效，请重新登录！","responseString":"504~用户会话失效，请重新登录！"}
                    //{"code":504,"message":"NOLOGIN_ERROR","responseString":"504~NOLOGIN_ERROR"}
                    String msg = jsonObject.optString("message");
                    if (StringUtils.isNotEmpty(msg)) {
                        if (msg.contains("NOLOGIN_ERROR")) {
                            if (!isTokenUnUsed) {
                                isTokenUnUsed = true;
                                presenter.loginOut();
                            }
//                            isNeedReconnect = true;
//                            reconnect();

                        } else {
                            startLogin();
                        }
                    }
                } else {
                    switch (cmd) {
                        case CMD.ACCEPT_ORDER://订单承接通知,收到该指令，说明承兑商有订单匹配，此时需要查询在途订单
                            int direction = jsonObject.optInt("direction");
                            message.what = CMD.ACCEPT_ORDER;
                            handler.sendMessage(message);
                            break;
                        case CMD.ORDER_STATUS://订单状态变更
                            int status = jsonObject.optInt("status");
                            if (status == 2) {//为2时，订单由  待支付 到 已支付状态
                                isHasPay = true;
                                message.what = CMD.ORDER_STATUS;
                                handler.sendMessage(message);
                            } else if (status == 11) {//为11时，无法支付取消
                                isHasPay = false;
                                message.what = 205;
                                String msg = jsonObject.optString("message");
                                if (StringUtils.isNotEmpty(msg)) {
                                    message.obj = msg;
                                    handler.sendMessage(message);
                                }
                            } else if (status == 8) {//为8时，卖币订单客户取消
                                isHasPay = false;
                                message.what = 206;
                                String msg = jsonObject.optString("message");
                                if (StringUtils.isNotEmpty(msg)) {
                                    message.obj = msg;
                                    handler.sendMessage(message);
                                }
                            } else if (status == 12) {//为12时，正在打款
                                isHasPay = false;
                                message.what = 207;
                                handler.sendMessage(message);
                            } else {
                                isHasPay = false;
                                message.what = CMD.ORDER_STATUS;
                                handler.sendMessage(message);
                            }
                            break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                LogUtils.e("socket监听===========================================================解析异常");
            }
        }

        @Override
        public void error() {
            LogUtils.e("socket监听===========================================================发送消息异常");
            /*if (!isTokenUnUsed) {
                //ToastUtils.showToast("发送消息异常===退出登录");
                isTokenUnUsed = true;
                presenter.loginOut();
            }*/

            isNeedReconnect = true;
            reconnect();

        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CMD.START_ORDER://开始接单
                    ToastUtils.showToast(MainActivity.this, "开始接单");
                    changeStatus(true);
                    //订单承接通知,收到该指令，说明承兑商有订单匹配，此时需要查询在途订单
                    getOrder();
                    openRawMusic(1);
                    break;
                case CMD.STOP_ORDER://停止接单
                    ToastUtils.showToast(MainActivity.this, "停止接单");
                    changeStatus(false);
                    openRawMusic(2);
                    //设置界面显示状态
                    setVisiableStatus();
                    break;
                case CMD.ACCEPT_ORDER: //订单承接通知,收到该指令，说明承兑商有订单匹配，此时需要查询在途订单
                    getOrder();
                    break;
                case CMD.ORDER_STATUS: //订单状态变更
                    getOrder();
                    break;
                case 205://为11时，无法支付取消
                    String message = (String) msg.obj;
                    //type 1:状态为11 2：状态为8
                    showAlerDialog(message, 1);
                    break;
                case 206://为8时，卖币订单客户取消
                    //type 1:状态为11 2：状态为8
                    showAlerDialog("", 2);
                    break;
                case 207://为12时，正在打款
                    openRawMusic(5);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public void findAuthenticationStatusSuccess(String response) {
        mTimeHandler.sendEmptyMessage(5);
        //解析认证状态数据
        AuthenticationStatusEntity entity = new Gson().fromJson(response, AuthenticationStatusEntity.class);
        //实名认证状态:0-未认证 1待审核 2-审核不通过  3-已认证
        //承兑商认证状态 0：未认证 1：认证-待审核 2：认证-审核成功 3：认证-审核失败 5：退保-待审核 6：退保-审核失败 7:退保-审核成功 8:退保-已退还保证金
        if (entity != null && entity.getCode() == SUCCESS_CODE) {
            if (entity.getData().getIsReal().getStatus() == 3 && entity.getData().getIsCertified().getStatus() == 2
                    && entity.getData().getIsSetPayPass().getIsCompleted() == 1 && entity.getData().getIsSetPayType().getIsCompleted() == 1) {

                llAuthStatus.setVisibility(View.GONE);
                llNotStartOrder.setVisibility(View.VISIBLE);
                llAccectOrder.setVisibility(View.VISIBLE);
                llStopOrder.setVisibility(View.GONE);
                llOrderDetail.setVisibility(View.GONE);

                //查询账户余额是否不足
                getWalletMoney();
                //查询今日接单数等数据
                presenter.findAcceptMerchantTrade();
                // 查询超时单
                getList(true);

            } else {

                llAuthStatus.setVisibility(View.VISIBLE);
                llNotStartOrder.setVisibility(View.GONE);
                llAccectOrder.setVisibility(View.GONE);
                llStopOrder.setVisibility(View.GONE);
                llOrderDetail.setVisibility(View.GONE);

                statusRealName = entity.getData().getIsReal().getStatus();
                if (entity.getData().getIsReal().getStatus() == 3) {
                    ivAuthStatusRealName.setSelected(true);
                    tvAuthStatusRealName.setTextColor(getResources().getColor(R.color.font_main_title));
                    ivAuthStatusRealName2.setVisibility(View.VISIBLE);
                    tvAuthStatusRealNameSet.setVisibility(View.GONE);
                } else {
                    ivAuthStatusRealName.setSelected(false);
                    tvAuthStatusRealName.setTextColor(getResources().getColor(R.color.font_grey_a5a5a5));
                    ivAuthStatusRealName2.setVisibility(View.GONE);
                    tvAuthStatusRealNameSet.setVisibility(View.VISIBLE);
                    //实名认证状态:0-未认证 1待审核 2-审核不通过  3-已认证
                    switch (entity.getData().getIsReal().getStatus()) {
                        case 0:
                            tvAuthStatusRealNameSet.setText(R.string.str_set);
                            break;
                        case 1:
                            tvAuthStatusRealNameSet.setText(R.string.str_creditting);
                            break;
                        case 2:
                            tvAuthStatusRealNameSet.setText(R.string.str_creditfail);
                            break;
                    }
                }

                if (entity.getData().getIsCertified().getStatus() == 2) {
                    ivAuthStatusAcceptanceMerchant.setSelected(true);
                    tvAuthStatusAcceptanceMerchant.setTextColor(getResources().getColor(R.color.font_main_title));
                    ivAuthStatusAcceptanceMerchant2.setVisibility(View.VISIBLE);
                    tvAuthStatusAcceptanceMerchantSet.setVisibility(View.GONE);
                } else {
                    ivAuthStatusAcceptanceMerchant.setSelected(false);
                    tvAuthStatusAcceptanceMerchant.setTextColor(getResources().getColor(R.color.font_grey_a5a5a5));
                    ivAuthStatusAcceptanceMerchant2.setVisibility(View.GONE);
                    tvAuthStatusAcceptanceMerchantSet.setVisibility(View.VISIBLE);
                    //承兑商认证状态 0：未认证 1：认证-待审核 2：认证-审核成功 3：认证-审核失败 5：退保-待审核 6：退保-审核失败 7:退保-审核成功 8:退保-已退还保证金
                    switch (entity.getData().getIsCertified().getStatus()) {
                        case 0:
                            tvAuthStatusAcceptanceMerchantSet.setText(R.string.str_set);
                            break;
                        case 1:
                            tvAuthStatusAcceptanceMerchantSet.setText(R.string.str_creditting);
                            break;
                        case 3:
                            tvAuthStatusAcceptanceMerchantSet.setText(R.string.str_creditfail);
                            break;
                        case 5:
                            tvAuthStatusAcceptanceMerchantSet.setText(R.string.str_creditting_back);
                            break;
                        case 6:
                            tvAuthStatusAcceptanceMerchantSet.setText(R.string.str_creditting_fail);
                            break;
                        case 7:
                            tvAuthStatusAcceptanceMerchantSet.setText(R.string.str_creditting_success);
                            break;
                        case 8:
                            tvAuthStatusAcceptanceMerchantSet.setText(R.string.str_creditting_finish);
                            break;

                    }
                }

                if (entity.getData().getIsSetPayPass().getIsCompleted() == 1) {
                    ivAuthStatusPass.setSelected(true);
                    tvAuthStatusPass.setTextColor(getResources().getColor(R.color.font_main_title));
                    ivAuthStatusPass2.setVisibility(View.VISIBLE);
                    tvAuthStatusPassSet.setVisibility(View.GONE);
                } else {
                    ivAuthStatusPass.setSelected(false);
                    tvAuthStatusPass.setTextColor(getResources().getColor(R.color.font_grey_a5a5a5));
                    ivAuthStatusPass2.setVisibility(View.GONE);
                    tvAuthStatusPassSet.setVisibility(View.VISIBLE);
                }

                if (entity.getData().getIsSetPayType().getIsCompleted() == 1) {
                    ivAuthStatusType.setSelected(true);
                    tvAuthStatusType.setTextColor(getResources().getColor(R.color.font_main_title));
                    ivAuthStatusType2.setVisibility(View.VISIBLE);
                    tvAuthStatusTypeSet.setVisibility(View.GONE);
                } else {
                    ivAuthStatusType.setSelected(false);
                    tvAuthStatusType.setTextColor(getResources().getColor(R.color.font_grey_a5a5a5));
                    ivAuthStatusType2.setVisibility(View.GONE);
                    tvAuthStatusTypeSet.setVisibility(View.VISIBLE);
                }
            }

            if (entity.getData().getIsCertified().getStatus() == 2) {
                //承兑商是否认证
                SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_AHTH, true);
            } else {
                //承兑商是否认证
                SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_AHTH, false);
            }

            if (entity.getData().getIsSetPayPass().getIsCompleted() == 1) {
                //交易密码是否设置
                SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_PAY_PASS, true);
            } else {
                //交易密码是否设置
                SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_PAY_PASS, false);
            }
        }


    }


    /**
     * 数据加载异常通知
     * 401登录无效监听
     *
     * @param event
     */
    public void onEvent(LoadExceptionEvent event) {
        if (!isTokenUnUsed) {
            isTokenUnUsed = true;
            presenter.loginOut();
        }
    }

    /**
     * type：1WebSocketService 2GuardService
     *
     * @param event
     */
    public void onEvent(ServiceOpenSuccessEvent event) {
        if (event.type == 1) {
            startLogin();
        }
        if (event.type == 2) {
            startLogin();
        }
    }

    /**
     * type：1WebSocketService 2GuardService
     *
     * @param event
     */
    public void onEvent(ServiceTypeEvent event) {
        if (event.type == 1) {
            isWebSocketService = true;
        }
        if (event.type == 2) {
            isWebSocketService = false;
        }
    }

    /**
     * type：1WebSocketService 2GuardService
     *
     * @param event
     */
    public void onEvent(ConnectCloseEvent event) {
        if (isStart) {
            LogUtils.e("播放断线语音222222222222222222222222222222222222222");
            openRawMusic(8);
        }
    }

    @Override
    public void loginOutSuccess(String response) {
        if (response != null) {
            if (isWebSocketService) {
                LogUtils.e("退出登录，关闭Websocket====webSocketService==" + webSocketService);
                if (webSocketService != null)
                    webSocketService.closeWebsocket(false);
            } else {
                if (guardService != null)
                    guardService.closeWebsocket();
            }

            goToLogin();
        }
    }

    /**
     * 退出登录-不调用退出接口
     *
     * @param event
     */
    public void onEvent(LoginoutWithoutApiEvent event) {
        if (isWebSocketService) {
            if (webSocketService != null)
                webSocketService.closeWebsocket(false);
        } else {
            if (guardService != null)
                guardService.closeWebsocket();
        }
        goToLogin();
    }

    private void goToLogin() {
        if (!isGoToLogin) {
            isGoToLogin = true;
            Bundle bundle = new Bundle();
            User currentUser = MyApplication.getAppContext().getCurrentUser();
            String mobilePhone = currentUser.getMobilePhone();
            String userName = "";
            if (StringUtils.isNotEmpty(mobilePhone) && mobilePhone.startsWith("86")) {
                userName = mobilePhone.substring(2, mobilePhone.length());
            }
            bundle.putString("username", userName);
            MyApplication.getAppContext().deleteCurrentUser();
            MyApplication.getAppContext().getCookieManager().getCookieStore().removeAll();
            isTokenUnUsed = false;
            ActivityManage.finishAll();
            showActivity(LoginActivity.class, bundle);
        }
    }

    /**
     * 退出登录 主动断开长连接
     *
     * @param event
     */
    public void onEvent(LoginoutEvent event) {
        if (isWebSocketService) {
            if (webSocketService != null)
                webSocketService.closeWebsocket(false);
        } else {
            if (guardService != null)
                guardService.closeWebsocket();
        }
    }

    /**
     * 设置界面显示状态
     */
    private void setVisiableStatus() {
        //isShort 账户余额是否不足：true不足  false正常
        if (isShort) {
            llStopOrder.setVisibility(View.VISIBLE);
            llNotStartOrder.setVisibility(View.GONE);
            llOrderDetail.setVisibility(View.GONE);
            llAuthStatus.setVisibility(View.GONE);
            llAccectOrder.setVisibility(View.GONE);
        } else {
            //isHas 是否有订单：true有订单  false没有订单
            if (isHas) {
                llOrderDetail.setVisibility(View.VISIBLE);
                llAccectOrder.setVisibility(View.VISIBLE);
                llStopOrder.setVisibility(View.GONE);
                llNotStartOrder.setVisibility(View.GONE);
                llAuthStatus.setVisibility(View.GONE);
            } else {
                //isStart 是否开始接单：true开始接单  false停止接单
                if (isStart) {
                    llOrderDetail.setVisibility(View.GONE);
                    llAccectOrder.setVisibility(View.VISIBLE);
                    llStopOrder.setVisibility(View.GONE);
                    llNotStartOrder.setVisibility(View.GONE);
                    llAuthStatus.setVisibility(View.GONE);
                } else {
                    llOrderDetail.setVisibility(View.GONE);
                    llAccectOrder.setVisibility(View.VISIBLE);
                    llStopOrder.setVisibility(View.GONE);
                    llNotStartOrder.setVisibility(View.VISIBLE);
                    llAuthStatus.setVisibility(View.GONE);
                }
            }
        }
    }

    Handler mTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://刷新剩余时间
                    if (randomTime > 0) {
                        randomTime -= 1000;
                        setTime(randomTime);
                    } else {
                        setTime(0);
                        mTimeHandler.removeCallbacks(runnable);
                        getOrder();
                    }
                    break;
                case 2://10秒刷新一次
                    LogUtils.e("刷新在途订单时间:" + DateUtils.getFormatTime(DateUtils.DATE_FORMAT_1, new Date()));
                    presenter.findOrderInTransit();
                    break;
                case 3:
                    acquireWakeLock();
                    disableKeyguard();
                    setWifiDormancy();
                    testService();
                    break;
                case 4:
                    startLogin();
                    break;
                case 5:
                    stopRefreshing();
                    break;
            }
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mTimeHandler.postDelayed(runnable, 1000);
            Message message = new Message();
            message.what = 1;
            mTimeHandler.sendMessage(message);
        }
    };

    /**
     * 设置时间
     *
     * @param randomTime
     */
    private void setTime(long randomTime) {
        if (position == 1) {//1待付款 2待放行
            if (tvPayTime != null)
                tvPayTime.setText(DateUtils.getTimeDown(randomTime, DateUtils.DATE_FORMAT_2, "", DateUtils.DATE_FORMAT_3));
        } else if (position == 2) {
            if (tvRealeaseTime != null)
                tvRealeaseTime.setText(DateUtils.getTimeDown(randomTime, DateUtils.DATE_FORMAT_2, "", DateUtils.DATE_FORMAT_4));
        } else if (position == 3) {
            if (tvPayTime != null)
                tvPayTime.setText(DateUtils.getTimeDown(randomTime, DateUtils.DATE_FORMAT_2, "", DateUtils.DATE_FORMAT_4));
        }

    }

    /**
     * 查询在途订单
     */
    private void getOrder() {
        if (randomTime < 0) {
            mTimeHandler.sendEmptyMessageDelayed(2, 10000);
        } else {
            presenter.findOrderInTransit();
        }

    }

    /**
     * 查询账户余额是否不足
     */
    private void getWalletMoney() {
        //查询交易账户
        presenter.getWallet(ACP);
    }

    @Override
    public void getWalletSuccess(String type, List<Wallet> list) {
        if (type == COMMON) {
            for (Wallet wallet : walletsTrade) {
                for (Wallet entity : list) {
                    if (entity.getCoinId().equals(wallet.getCoinId())) {
                        wallet.setAddress(entity.getAddress());
                    }
                }
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("wallets", walletsTrade);
            showActivity(RechargeActivity.class, bundle, 1);
        } else if (type == ACP) {
            this.walletsTrade.clear();
            this.walletsTrade.addAll(list);

            double sumTrade = 0;
            for (Wallet coin : list) {
                sumTrade += coin.getTotalLegalAssetBalance();
            }
            //isShort 账户余额是否不足：true不足  false正常
            if (sumTrade > 0) {
                isShort = false;
                //查询在途订单
                getOrder();
            } else {
                isShort = true;
            }
            setVisiableStatus();
        }

    }


    /**
     * 获取service对象
     */
    private void bindSocketService() {
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                WebSocketService.MyBinder binder = (WebSocketService.MyBinder) service;
                webSocketService = binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        mConnection2 = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                GuardService.MyBinder binder = (GuardService.MyBinder) service;
                guardService = binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        //启动Service
        startAllServices();
    }


    /**
     * 获取唤醒锁
     */
    private void acquireWakeLock() {
        if (mWakeLock == null) {
            PowerManager mPM = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            mWakeLock = mPM.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getCanonicalName());
            if (mWakeLock != null) {
                mWakeLock.acquire();
            }
        }
    }

    /**
     * 禁止锁屏
     */
    private void disableKeyguard() {
        //禁止锁屏
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
        keyguardLock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
        keyguardLock.disableKeyguard();//关闭系统锁屏
    }

    /**
     * 释放锁
     */
    private void releaseWakeLock() {
        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    /**
     * 释放屏幕
     */
    private void releaseKeyguardLock() {
        try {
            if (keyguardLock != null) {
                keyguardLock.reenableKeyguard();
                keyguardLock = null;
            }
        } catch (Exception e) {

        }
    }

    /**
     * 开启所有Service
     */
    private void startAllServices() {
        Intent intent = new Intent(getApplicationContext(), WebSocketService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);

//        Intent guardIntent = new Intent(getApplicationContext(), GuardService.class);
//        bindService(guardIntent, mConnection2, BIND_AUTO_CREATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d("开启JobWakeUpService", "JobWakeUpService: ");
            //版本必须大于5.0
            startService(new Intent(this, JobWakeUpService.class));
        }
    }

    @Override
    public void releaseOrderSuccess(MessageResult response) {
        if (response != null) {
            //自动放行成功
            openRawMusic(6);
            getMainInfo(false);
        }
    }

    @Override
    public void releaseOrderFail(HttpErrorEntity httpErrorEntity) {
        if (httpErrorEntity != null) {
            //自动放行失败
            openRawMusic(7);
            if (StringUtils.isNotEmpty(httpErrorEntity.getMessage())) {
                ToastUtils.showToast(activity, httpErrorEntity.getMessage());
            }
        }
    }

    /**
     * 风控提醒对话框
     * type 1:状态为11 2：状态为8
     */
    private void showAlerDialog(String message, int type) {
        alertIosDialog = new AlertIosDialog(activity);
        alertIosDialog.withWidthScale(0.8f);
        if (type == 1) {
            alertIosDialog
                    .setImg(0)
                    .setContent(message)
                    .setTag(getString(R.string.str_pay_warn_tag))
                    .setTagColor(getResources().getColor(R.color.font_sec_title));
        }
        if (type == 2) {
            alertIosDialog
                    .setImg(R.mipmap.icon_warm_tag)
                    .setContent(getString(R.string.str_pay_custom_cancel_tag));
        }
        alertIosDialog.setPositiveClickLister(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertIosDialog.dismiss();
            }
        });
        alertIosDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isShowLoading = false;
                getOrder();
            }
        });
        alertIosDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //跳转通知授权界面
        //openNotification();
        if (!isCheckVersion) {
            isCheckVersion = true;
            allowUnKnowSrc(activity);
            checkPermission();
        }

    }

    /**
     * 允许安装未知来源的应用
     * 其中的变量：1代表允许，如果换成0则代表禁止
     */
    public static void allowUnKnowSrc(Context context) {
        try {
            android.provider.Settings.Global.putInt(context.getContentResolver(),
                    android.provider.Settings.Secure.INSTALL_NON_MARKET_APPS, 1);
        } catch (SecurityException e) {
            //LogUtils.getInstance().d(e);
        }
    }

    /**
     * 是否已授权
     *
     * @return
     */
    private boolean isNotificationServiceEnable() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 跳转通知授权界面
     */
    private void openNotification() {
        boolean isAuthor = isNotificationServiceEnable();
        if (!isAuthor) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                //直接跳转通知授权界面
                //android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS是API 22才加入到Settings里，这里直接写死
                startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            }
        }
    }

    @Override
    public void checkVersionSuccess(String response) {
        if (StringUtils.isNotEmpty(response)) {
            VisionEntity entity = new Gson().fromJson(response, VisionEntity.class);
            if (entity != null && entity.getData() != null && (AppUtils.compareVersion(entity.getData().getVersion(), AppUtils.getVersionName(activity)) == 1)) {
                if (StringUtils.isNotEmpty(entity.getData().getUrl())) {
                    showVersionDialog(entity);
                } else {
                    ToastUtils.showToast(activity, getString(R.string.update_address_error_tag));
                }
            }
        }
    }

    /**
     * check权限
     *
     * @param position
     */
    private void checkPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        LogUtils.d("permission: " + data.get(0));
                        presenter.checkVersion();
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
     * 版本更新提示框
     */
    private void showVersionDialog(final VisionEntity visionEntity) {
        appVersionDialog = new AppVersionDialog(activity, visionEntity);
        appVersionDialog.setPositiveOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visionEntity.getData().getUrl() == null || "".equals(visionEntity.getData().getUrl())) {
                    ToastUtils.showToast(activity, getString(R.string.update_address_error_tag));
                } else {
                    download(visionEntity.getData().getUrl());
                }
                appVersionDialog.dismiss();
            }
        });
        appVersionDialog.show();
    }

    /**
     * 下载
     *
     * @param url
     */
    private void download(String url) {
        OkhttpUtils.get().url(url).build().execute(new FileCallback(FileUtils.getCacheSaveFile(this, "application.apk").getAbsolutePath()) {
            @Override
            public void inProgress(float progress) {
                progressDialog.show();
                progressDialog.setProgress((int) (progress * 100));
            }

            @Override
            public void onError(Request request, HttpErrorEntity e) {
                progressDialog.dismiss();
                String msg = e.getMessage();
                LogUtils.e("下载download===============================================onError===msg==" + msg);
            }

            @Override
            public void onResponse(File response) {
                progressDialog.dismiss();
                AppUtils.installAPk(response, activity);
            }
        });
    }

    /**
     * 初始化进度条
     */
    private void initProgressDialog() {
        //创建进度条对话框
        progressDialog = new ProgressDialog(this);
        //设置标题
        progressDialog.setTitle(getString(R.string.downloading_tag));
        //设置信息
        progressDialog.setMessage(getString(R.string.downloading_crazy_tag));
        progressDialog.setProgress(0);
        //设置显示的格式
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    /**
     * 设置WIFI不休眠并确保黑屏后网络通信正常
     */
    public void setWifiDormancy() {
        int pol = Settings.System.WIFI_SLEEP_POLICY_NEVER;
        Settings.System.putInt(getContentResolver(), Settings.System.WIFI_SLEEP_POLICY, pol);
    }

    /**
     * 利用Android自带的定时器AlarmManager实现
     */
    private void testService() {
        Intent intent = new Intent(activity, WebSocketService.class);
        PendingIntent pi = PendingIntent.getService(activity, 1, intent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
        if (alarm != null) {
            alarm.cancel(pi);
            // 闹钟在系统睡眠状态下会唤醒系统并执行提示功能
            // pendingIntent 为发送广播
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarm.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pi);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarm.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pi);
            } else {
                alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 1000, pi);
            }
        }
    }

    /**
     * 网络状态监听
     */
    BroadcastReceiver netReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isAvailable()) {
                    int type2 = networkInfo.getType();
                    String typeName = networkInfo.getTypeName();
                    LogUtils.e(type2 + "=========================" + typeName);
                    switch (type2) {
                        case 0://移动 网络    2G 3G 4G 都是一样的 实测 mix2s 联通卡
                            break;
                        case 1: //wifi网络
                            break;
                        case 9:  //网线连接
                            break;
                    }

                    if (isNeedReconnect) {
                        reconnect();
                    }

                } else {// 无网络
                    LogUtils.e("无网络 =========================");
                    isNeedReconnect = true;
                }
            }
        }

    };

    /**
     * 重新连接websocket
     */
    private void reconnect() {
        if (!NetworkUtil.checkNetwork(BaseApplication.getAppContext())) {
            ToastUtils.showToast(getString(R.string.str_please_check_net));
            return;
        }

        if (isNeedReconnect) {
            isNeedReconnect = false;
            if (isWebSocketService) {
                Log.e("", "WebSocketService- 000000000000000");
                if (webSocketService != null)
                    webSocketService.reconnect();
            } else {
                if (guardService != null)
                    guardService.reconnect();
            }
        }
    }


}
