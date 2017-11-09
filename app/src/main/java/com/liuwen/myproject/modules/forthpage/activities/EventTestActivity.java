package com.liuwen.myproject.modules.forthpage.activities;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.liuwen.myproject.R;
import com.liuwen.myproject.base.app.BaseActivity;
import com.liuwen.myproject.base.app.MyProjectApplication;
import com.liuwen.myproject.base.constant.Constant;
import com.liuwen.myproject.base.constant.IntentKey;
import com.liuwen.myproject.utils.ToastUtil;
import com.liuwen.myproject.utils.httpUtil.RxBus;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-07-25
 */

public class EventTestActivity extends BaseActivity {

    @BindView(R.id.iv_Back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_Subhead)
    TextView tvSubhead;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.button_eventtest)
    Button buttonEventtest;
    private Context context;
    private RxPermissions rxPermissions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventtest);
        //ButterKnife绑定View
        ButterKnife.bind(this);
        //初始化
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        context = EventTestActivity.this;
        rxPermissions = new RxPermissions(this);
        //初始化View
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        tvTitle.setText("响应式");
        //点击事件
        onClick();
    }

    /**
     * 点击事件的监听
     */
    private void onClick() {
        RxView.clicks(buttonEventtest)
                .throttleFirst(Constant.DURATION, TimeUnit.MILLISECONDS)
                .compose(rxPermissions.ensure(Manifest.permission.READ_PHONE_STATE))// 权限管理
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                        Logger.d("允许Manifest.permission.READ_PHONE_STATE权限-----当前设备号:" + tm.getDeviceId().toString());
                    } else {
                        Logger.d("拒绝Manifest.permission.READ_PHONE_STATE权限-----禁:获取设备号");
                    }
                    ToastUtil.showTopToast(MyProjectApplication.getInstance(), "响应式");
                    RxBus.getDefault().post(IntentKey.MAINACT_SVP_SETPOSITION_1);
                    EventTestActivity.this.finish();
                });
    }
}
