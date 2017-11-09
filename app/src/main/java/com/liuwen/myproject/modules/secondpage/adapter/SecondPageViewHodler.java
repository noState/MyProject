package com.liuwen.myproject.modules.secondpage.adapter;

import android.support.v7.widget.CardView;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.liuwen.myproject.R;

import butterknife.BindView;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-08-15
 */

public class SecondPageViewHodler extends BaseViewHolder<String> {

    @BindView(R.id.cardview)
    CardView cardview;

    public SecondPageViewHodler(ViewGroup parent) {
        super(parent, R.layout.recyclerview_secondpage);
    }

    @Override
    public void setData(String data) {
        super.setData(data);
    }
}
