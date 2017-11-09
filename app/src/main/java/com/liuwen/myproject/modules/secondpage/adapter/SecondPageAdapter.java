package com.liuwen.myproject.modules.secondpage.adapter;


import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-08-15
 */

public class SecondPageAdapter extends RecyclerArrayAdapter<String> {

    public SecondPageAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new SecondPageViewHodler(parent);
    }
}
