package com.liuwen.myproject.base.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-07-25
 */

public abstract class BottomNavigationBaseFragment extends Fragment {

    protected Activity mActivity;
    protected View mRootView;
    private Unbinder unbinder;

    /**
     * 保存全局的Context
     *
     * @param context 上下文
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        //初始化
        init();
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 创建视图时的初始化操作
     */
    protected abstract void init();

    /**
     * @return 返回该Fragment的layout id
     */
    protected abstract int getLayoutId();


    /**
     * 返回当前View
     *
     * @return view
     */
    protected View getContentView() {
        return mRootView;
    }

}
