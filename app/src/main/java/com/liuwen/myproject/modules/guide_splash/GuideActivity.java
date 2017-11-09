package com.liuwen.myproject.modules.guide_splash;

import android.os.Bundle;

import com.liuwen.myproject.R;
import com.liuwen.myproject.base.app.BaseActivity;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-08-11
 */

public class GuideActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        immersion();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
    }
}
