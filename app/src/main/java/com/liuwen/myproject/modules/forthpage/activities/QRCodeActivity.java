package com.liuwen.myproject.modules.forthpage.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuwen.myproject.R;
import com.liuwen.myproject.base.app.BaseActivity;
import com.liuwen.myproject.utils.CommonUtils;
import com.liuwen.myproject.utils.CreateQRUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-08-08
 */

public class QRCodeActivity extends BaseActivity {

    @BindView(R.id.iv_QRCode)
    ImageView ivQRCode;
    @BindView(R.id.iv_Back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_Subhead)
    TextView tvSubhead;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        //ButterKnife
        ButterKnife.bind(this);
        //初始化
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        tvTitle.setText("二维码");
        //生成二维码
        Bitmap bitmapFromResource = CommonUtils.getBitmapFromResource(this, R.mipmap.app_logo);
        Bitmap qrImage = CreateQRUtil.createQRImage(this, "http://www.baidu.com", bitmapFromResource);
        ivQRCode.setImageBitmap(qrImage);
    }

}
