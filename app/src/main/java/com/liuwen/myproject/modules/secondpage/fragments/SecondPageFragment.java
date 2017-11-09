package com.liuwen.myproject.modules.secondpage.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.liuwen.myproject.R;
import com.liuwen.myproject.base.app.BaseFragment;
import com.liuwen.myproject.base.app.MyProjectApplication;
import com.liuwen.myproject.base.constant.Constant;
import com.liuwen.myproject.modules.MainActivity;
import com.liuwen.myproject.modules.secondpage.adapter.SecondPageAdapter;
import com.liuwen.myproject.utils.ToastUtil;
import com.liuwen.myproject.utils.httpUtil.RxBus;
import com.liuwen.myproject.utils.httpUtil.SucceedSubscribe;
import com.liuwen.myproject.views.LoadingLayout;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-07-25
 */

public class SecondPageFragment extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.erv_secondpage)
    EasyRecyclerView ervSecondpage;
    @BindView(R.id.loadlayout_secondpage)
    LoadingLayout loadlayoutSecondpage;
    @BindView(R.id.fab_secondpage_top)
    FloatingActionButton fabSecondpageTop;

    private Unbinder unbinder;
    private SecondPageAdapter secondPageAdapter;
    private Subscription mSubscribe;

    @Override
    protected void onCreate() {
        onRxBusEventResponse();
        initEasyRecyclerView();
        initLoadingLayout(loadlayoutSecondpage);
        onClickEvent();
    }

    @Override
    public void requestData() {
        requestBeginning();
        mSubscribe = Observable.timer(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
                    ArrayList<String> testList = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        String string = new String();
                        testList.add(string);
                    }
                    secondPageAdapter.addAll(testList);
                    requestSucceed();
                });
        ((MainActivity) mActivity).addSubscription(mSubscribe);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_secondpage;
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

    /**
     * 点击事件
     */
    private void onClickEvent() {
        RxView.clicks(fabSecondpageTop).throttleFirst(Constant.DURATION, TimeUnit.MILLISECONDS).subscribe(aVoid -> ervSecondpage.scrollToPosition(0));
    }

    /**
     * EasyRecyclerView
     */
    private void initEasyRecyclerView() {
        ervSecondpage.setRefreshingColorResources(R.color.colorTheme);
        ervSecondpage.setLayoutManager(new LinearLayoutManager(mActivity));
        ervSecondpage.setEmptyView(R.layout.content_recyclerview_empty);
        ervSecondpage.setAdapterWithProgress(secondPageAdapter = new SecondPageAdapter(mActivity));
        secondPageAdapter.setOnItemClickListener(position -> ToastUtil.showTopToast(MyProjectApplication.getInstance(), "Position>>>" + position));
        ervSecondpage.setRefreshListener(() -> ervRefresh());
        secondPageAdapter.setMore(R.layout.content_recyclerview_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                ervLoadMore();
            }

            @Override
            public void onMoreClick() {

            }
        });
        secondPageAdapter.setError(R.layout.content_recyclerview_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {

            }

            @Override
            public void onErrorClick() {
                ervLoadMore();
            }
        });
    }

    public void ervRefresh() {
        mSubscribe = Observable.timer(Constant.LOADINGTIME, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> requestData());
        ((MainActivity) mActivity).addSubscription(mSubscribe);
    }

    public void ervLoadMore() {
        mSubscribe = Observable.timer(Constant.LOADINGTIME, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> requestData());
        ((MainActivity) mActivity).addSubscription(mSubscribe);
    }

    /**
     * 组件通讯(修改当前状态)
     */
    private void onRxBusEventResponse() {
        Subscription subscribe = RxBus.getDefault().toObservable(Integer.class).subscribe(new SucceedSubscribe<Integer>() {
            @Override
            public void onNext(Integer integer) {
                super.onNext(integer);
                switch (integer) {
                }
            }
        });
        ((MainActivity) mActivity).addSubscription(subscribe);
    }
}
