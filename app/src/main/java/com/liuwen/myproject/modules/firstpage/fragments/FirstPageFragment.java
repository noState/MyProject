package com.liuwen.myproject.modules.firstpage.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liuwen.myproject.R;
import com.liuwen.myproject.base.app.ActivityManager;
import com.liuwen.myproject.base.app.BaseFragment;
import com.liuwen.myproject.base.app.MyProjectApplication;
import com.liuwen.myproject.base.entities.request.UpdateInfoReq;
import com.liuwen.myproject.base.entities.response.BannerRes;
import com.liuwen.myproject.base.entities.response.UpdateInfoRes;
import com.liuwen.myproject.modules.MainActivity;
import com.liuwen.myproject.utils.CommonUtils;
import com.liuwen.myproject.utils.httpUtil.HttpMethods;
import com.liuwen.myproject.utils.httpUtil.HttpResult;
import com.liuwen.myproject.utils.httpUtil.SucceedSubscribe;
import com.liuwen.myproject.views.SimpleSwipeRefreshLayout;
import com.liuwen.myproject.views.LoadingLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-07-25
 */

public class FirstPageFragment extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    Unbinder unbinder;
    @BindView(R.id.loadlayout_firstpage)
    LoadingLayout loadindLayout;
    @BindView(R.id.csrl_firstPage)
    SimpleSwipeRefreshLayout csrlFirstPage;

    private UpdateInfoReq updateInfoReq;

    @Override
    protected void onCreate() {
        initSwipe();
        initLoadingLayout(loadindLayout);
    }

    private void initSwipe() {
        csrlFirstPage.setColorSchemeColors(getResources().getColor(R.color.colorTheme), getResources().getColor(R.color.colorTheme));
        csrlFirstPage.setOnRefreshListener(() -> {
            requestData();
        });
    }

    @Override
    public void requestData() {
        requestBeginning();
        // 初始化请求体
        setRequestData();
        if (!(csrlFirstPage != null && csrlFirstPage.isRefreshing())) {
            // App更新信息
            getServerUpdateInfo(updateInfoReq);
        }
        // Banner信息
        getBanner();
    }

    /**
     * 构建请求体
     */
    private void setRequestData() {
        // 更新接口请求体
        updateInfoReq = new UpdateInfoReq();
        updateInfoReq.Version = CommonUtils.getAppVersionName(MyProjectApplication.getInstance());
        updateInfoReq.OSType = "A";
    }

    /**
     * POST获取服务器更新信息
     *
     * @param updateInfoReq
     */
    public void getServerUpdateInfo(UpdateInfoReq updateInfoReq) {
        SucceedSubscribe<UpdateInfoRes> succeedSubscribe = new SucceedSubscribe<UpdateInfoRes>() {
            @Override
            public void onNext(UpdateInfoRes updateInfoRes) {
                super.onNext(updateInfoRes);
            }
        };
        HttpMethods.getInstance().getUpdateInfo(succeedSubscribe, updateInfoReq);
        ActivityManager.getActivity(MainActivity.class).addSubscription(succeedSubscribe);
    }

    /**
     * 获取Banner信息
     */
    public void getBanner() {
        SucceedSubscribe<ArrayList<BannerRes>> succeedSubscribe = new SucceedSubscribe<ArrayList<BannerRes>>() {

            @Override
            public void onNext(ArrayList<BannerRes> bannerResArrayList) {
                super.onNext(bannerResArrayList);
                requestSucceedV();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                requestFailedV();
            }
        };
        HttpMethods.getInstance().getBannerInfo(succeedSubscribe);
        ActivityManager.getActivity(MainActivity.class).addSubscription(succeedSubscribe);
    }

    private void requestSucceedV() {
        requestSucceed();
        stopSwipe();
    }

    private void requestFailedV() {
        requestFailed();
        stopSwipe();
    }

    private void stopSwipe() {
        if (csrlFirstPage != null) {
            if (csrlFirstPage.isRefreshing()) {
                csrlFirstPage.setRefreshing(false);
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_firstpage;
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
}
