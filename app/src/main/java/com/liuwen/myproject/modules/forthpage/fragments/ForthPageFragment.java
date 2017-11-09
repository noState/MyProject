package com.liuwen.myproject.modules.forthpage.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.liuwen.myproject.R;
import com.liuwen.myproject.base.app.BaseFragment;
import com.liuwen.myproject.base.constant.Constant;
import com.liuwen.myproject.base.constant.IntentKey;
import com.liuwen.myproject.modules.forthpage.activities.EventTestActivity;
import com.liuwen.myproject.modules.forthpage.activities.QRCodeActivity;
import com.liuwen.myproject.modules.h5.webview.WebViewActivity;
import com.liuwen.myproject.utils.CommonUtils;
import com.sunfusheng.marqueeview.MarqueeView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-07-25
 */

public class ForthPageFragment extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.button_forthpage)
    Button buttonForthpage;
    @BindView(R.id.button_forthpage01)
    Button buttonForthpage01;
    @BindView(R.id.button_forthpage02)
    Button buttonForthpage02;
    @BindView(R.id.vertical_MarqueeView)
    MarqueeView verticalMarqueeView;
    @BindView(R.id.horizontal_MarqueeView)
    MarqueeView horizontalMarqueeView;
    private Unbinder unbinder;

    private ArrayList<String> messageList = new ArrayList<>();

    /**
     * 预初始化
     */
    private void prepareInit() {
        initView();
        onClick();
    }

    /**
     * 初始化View
     */
    private void initView() {
        messageList.add("这世间还有希望,还有憧憬,还有诗和远方");
        messageList.add("我觉得人生的意义不在此地,我要去远方追寻生命的真谛");
        messageList.add("星空如此璀璨,你我却在此争斗不息,真是大煞风景");
        messageList.add("谁敢称无敌,哪个敢言不败");
        messageList.add("吾为天帝,当镇压世间一切敌");

        // 在代码里设置自己的动画
        verticalMarqueeView.startWithList(messageList, R.anim.anim_bottom_in, R.anim.anim_top_out);
        horizontalMarqueeView.startWithList(messageList, R.anim.anim_right_in, R.anim.anim_left_out);
    }

    /**
     * 点击事件的监听
     */
    private void onClick() {
        //RxBus
        RxView.clicks(buttonForthpage)
                .throttleFirst(Constant.DURATION, TimeUnit.MILLISECONDS)
                .subscribe(aVoid -> CommonUtils.startActivity(getContext(), EventTestActivity.class));
        //WebViewActivity
        RxView.clicks(buttonForthpage01)
                .throttleFirst(Constant.DURATION, TimeUnit.MILLISECONDS)
                .subscribe(aVoid -> {
                    String url = "http://www.baidu.com";
                    startActivity(new Intent(getContext(), WebViewActivity.class).putExtra(IntentKey.INTENT_TO_WEBVIEWACT_URL, url));
//                    startActivity(new Intent(getContext(), X5WebViewActivity.class).putExtra(IntentKey.INTENT_TO_X5WEBVIEWACT_URL, url));
                });
        //QRCode
        RxView.clicks(buttonForthpage02)
                .throttleFirst(Constant.DURATION, TimeUnit.MILLISECONDS)
                .subscribe(aVoid -> CommonUtils.startActivity(ForthPageFragment.this.getContext(), QRCodeActivity.class));
    }

    @Override
    protected void onCreate() {
        //预初始化
        prepareInit();
    }

    @Override
    public void requestData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_forthpage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        verticalMarqueeView.startFlipping();
        horizontalMarqueeView.startFlipping();
    }

    @Override
    public void onStop() {
        super.onStop();
        verticalMarqueeView.stopFlipping();
        horizontalMarqueeView.stopFlipping();
    }
}
