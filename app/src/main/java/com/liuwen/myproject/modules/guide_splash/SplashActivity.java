package com.liuwen.myproject.modules.guide_splash;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.liuwen.myproject.R;
import com.liuwen.myproject.base.app.BaseActivity;
import com.liuwen.myproject.base.constant.Constant;
import com.liuwen.myproject.modules.MainActivity;
import com.liuwen.myproject.utils.CommonUtils;
import com.liuwen.myproject.utils.securitySPUtil.SecuritySharedPreference;
import com.liuwen.myproject.views.LoadingLayout;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-08-01
 */

public class SplashActivity extends BaseActivity {

    @BindView(R.id.tv_splash_goto)
    TextView tvSplashGoto;
    @BindView(R.id.tv_splash_versionname)
    TextView tvSplashVersionname;

    private Context context;
    private Handler handler;
    private Runnable runnable;
    private Boolean isFirstOpenApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        immersion();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        // 初始化LoadingLayout
        initLoadingLayout();
        //获取App打开状态
        SecuritySharedPreference securitySharedPreference = new SecuritySharedPreference(this, Constant.SP_NORMAL, Context.MODE_PRIVATE);
        isFirstOpenApp = securitySharedPreference.getBoolean(Constant.SP_NORMAL_ISFIRSTOPENAPP, false);
        context = SplashActivity.this;
        handler = new Handler();
        //初始化View
        initView();

    }

    /**
     * 初始化
     */
    private void initView() {
        tvSplashVersionname.setText(CommonUtils.getAppVersionName(context).toString());
        //跳转到下一页
        gotoNewActivity();
        //点击事件
        onClickEvent();
    }

    /**
     * 点击事件
     */
    private void onClickEvent() {
        //跳过 手动跳转
        RxView.clicks(tvSplashGoto)
                .throttleFirst(Constant.DURATION, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        gotoNextPage();
                        if (handler != null) {
                            if (runnable != null) {
                                handler.removeCallbacks(runnable);
                                runnable = null;
                            }
                            handler = null;
                        }
                    }
                });
    }

    /**
     * 自动跳转
     */
    private void gotoNewActivity() {
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                gotoNextPage();
            }
        }, 4500);
    }

    private void gotoNextPage() {
        if (isFirstOpenApp) {
            CommonUtils.startActivity(context, GuideActivity.class);
        } else {
            CommonUtils.startActivity(context, MainActivity.class);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            if (runnable != null) {
                handler.removeCallbacks(runnable);
                runnable = null;
            }
            handler = null;
        }
    }

    /**
     * LoadingLayout
     */
    private void initLoadingLayout() {
        LoadingLayout.getConfig()
                .setErrorText("出错啦~请稍后重试！")
                .setEmptyText("抱歉,暂无数据")
                .setNoNetworkText("无网络连接,请检查您的网络...")
                .setErrorImage(R.mipmap.error)
                .setEmptyImage(R.mipmap.empty)
                .setNoNetworkImage(R.mipmap.no_network)
                .setAllTipTextColor(R.color.colorLoadingGray)
                .setAllTipTextSize(14)
                .setReloadButtonText("点我重试哦")
                .setReloadButtonTextSize(14)
                .setReloadButtonTextColor(R.color.colorLoadingGray)
                .setReloadButtonWidthAndHeight(150, 40)
                .setAllPageBackgroundColor(R.color.colorLoadingBg);
    }
}
