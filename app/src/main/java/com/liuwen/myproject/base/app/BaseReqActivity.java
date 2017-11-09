package com.liuwen.myproject.base.app;

import com.liuwen.myproject.utils.NetUtil;
import com.liuwen.myproject.views.LoadingLayout;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-09-05
 * <p>
 * Activity:初始化即加载数据的Activity基类 封装了LoadingLayout,符合条件建议继承该基类+-
 */

public abstract class BaseReqActivity extends BaseActivity {

    private LoadingLayout mLoadingLayout;
    /**
     * 请求成功后，刷新或者第二次请求的时候出现失败的情况
     */
    private boolean isRequestSucceed;

    public abstract void requestData();

    /**
     * 必须在initViews() setContentView(R.layout.activity_);ButterKnife.bind(this);后调用
     *
     * @param loadingLayout
     */
    protected void initLoadingLayout(LoadingLayout loadingLayout) {
        mLoadingLayout = loadingLayout;
        if (NetUtil.netIsConnected(MyProjectApplication.getInstance())) {
            requestData();
        } else {
            if (mLoadingLayout != null) {
                mLoadingLayout.setStatus(LoadingLayout.No_Network);
                mLoadingLayout.setOnReloadListener(v -> initLoadingLayout(mLoadingLayout));
            }
        }
    }

    /**
     * 网络请求开始
     */
    protected void requestBeginning() {
        if (!isRequestSucceed) {
            if (mLoadingLayout != null) {
                mLoadingLayout.setStatus(LoadingLayout.Loading);
            }
        }
    }

    /**
     * 网络请求成功
     */
    protected void requestSucceed() {
        if (mLoadingLayout != null) {
            mLoadingLayout.setStatus(LoadingLayout.Success);
            isRequestSucceed = true;
        }
    }

    /**
     * 请求失败,最好是单个页面只有一个网络请求的情况下使用
     */
    protected void requestFailed() {
        if (!isRequestSucceed) {
            if (mLoadingLayout != null) {
                mLoadingLayout.setStatus(LoadingLayout.Error);
                mLoadingLayout.setOnErrorListener(v -> initLoadingLayout(mLoadingLayout));
            }
        }
    }
}
