package com.liuwen.myproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.liuwen.myproject.base.app.BaseReqActivity;
import com.liuwen.myproject.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-08-28
 */

public class TestActivity extends BaseReqActivity {

    @BindView(R.id.iv_Back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_Subhead)
    TextView tvSubhead;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.rb_RefundAct_CZ)
    RadioButton rbRefundActCZ;
    @BindView(R.id.rb_RefundAct_DK)
    RadioButton rbRefundActDK;
    @BindView(R.id.rg_RefundAct)
    RadioGroup rgRefundAct;

    private List<ImageView> viewList;//view数组
    private ArrayList<String> messageList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        // 预初始化
        init();
    }

    public void init() {
        initData();
        initView();
    }

    @Override
    public void requestData() {
    }

    private void initData() {
        viewList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(R.drawable.test);
            viewList.add(imageView);
        }
    }

    private void initView() {
        button.setOnClickListener(v -> ToastUtil.errorTastyToast("错误信息!!!"));
        viewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView(viewList.get(position));
            }
        });
    }

}
