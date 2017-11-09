package com.liuwen.myproject.modules.thirdpage.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liuwen.myproject.R;
import com.liuwen.myproject.base.app.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-07-25
 */

public class ThirdPageFragment extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    Unbinder unbinder;

    @Override
    protected void onCreate() {

    }

    @Override
    public void requestData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_thirdpage;
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
