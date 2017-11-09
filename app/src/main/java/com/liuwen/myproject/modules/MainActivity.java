package com.liuwen.myproject.modules;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.liuwen.myproject.R;
import com.liuwen.myproject.base.app.BaseActivity;
import com.liuwen.myproject.base.constant.Constant;
import com.liuwen.myproject.base.constant.IntentKey;
import com.liuwen.myproject.modules.firstpage.fragments.FirstPageFragment;
import com.liuwen.myproject.modules.forthpage.fragments.ForthPageFragment;
import com.liuwen.myproject.modules.secondpage.fragments.SecondPageFragment;
import com.liuwen.myproject.modules.thirdpage.fragments.ThirdPageFragment;
import com.liuwen.myproject.utils.CommonUtils;
import com.liuwen.myproject.utils.DoubleClickExitAppUtil;
import com.liuwen.myproject.utils.ToastUtil;
import com.liuwen.myproject.utils.httpUtil.RxBus;
import com.liuwen.myproject.utils.httpUtil.SucceedSubscribe;
import com.liuwen.myproject.views.SlideViewPager;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_viewpager)
    SlideViewPager mainViewpager;
    @BindView(R.id.main_bottombar)
    BottomNavigationBar mainBottombar;

    private String[] bottomItems = {"老一", "小二", "小三", "小四"};
    private FirstPageFragment firstPageFragment;
    private SecondPageFragment secondPageFragment;
    private ThirdPageFragment thirdPageFragment;
    private ForthPageFragment forthPageFragment;
    private DoubleClickExitAppUtil doubleClickExitAppUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // 沉浸式状态栏
        // immersion();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ButterKnife绑定View
        ButterKnife.bind(this);
        // 初始化
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        // 收集设备信息
        CommonUtils.collectDeviceInfo();
        // 初始化View
        initView();
        // 组件通讯(修改当前状态)
        onRxBusEventResponse();
    }

    /**
     * 初始化View
     */
    private void initView() {
        initSlideViewPager();
        initBottomBar();
        // 双击退出
        doubleClickExitAppUtil = new DoubleClickExitAppUtil(this, mainBottombar);
    }

    /**
     * 初始化SlideViewPager
     */
    private void initSlideViewPager() {
        mainViewpager.setScanScroll(true); // 控制SlideViewPager左右滑动
        mainViewpager.setOffscreenPageLimit(bottomItems.length);// 预加载
        mainViewpager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = null;
                switch (position) {
                    case 0:// 第一界面
                        if (firstPageFragment == null) {
                            firstPageFragment = new FirstPageFragment();
                        }
                        fragment = firstPageFragment;
                        break;
                    case 1:// 第二界面
                        if (secondPageFragment == null) {
                            secondPageFragment = new SecondPageFragment();
                        }
                        fragment = secondPageFragment;
                        break;
                    case 2:// 第三界面
                        if (thirdPageFragment == null) {
                            thirdPageFragment = new ThirdPageFragment();
                        }
                        fragment = thirdPageFragment;
                        break;
                    case 3:// 第四界面
                        if (forthPageFragment == null) {
                            forthPageFragment = new ForthPageFragment();
                        }
                        fragment = forthPageFragment;
                        break;
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return bottomItems.length;
            }
        });
        mainViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mainBottombar.getCurrentSelectedPosition() != position) {
                    // SlideViewPager绑定BottomNavigationBar
                    mainBottombar.selectTab(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化BottomNavigationBar
     * <p>
     * BottomNavigationBar可以实现添加角标的功能
     */
    private void initBottomBar() {
        mainBottombar.addItem(new BottomNavigationItem(R.drawable.tab_homepage_active, bottomItems[0]).setInactiveIconResource(R.drawable.tab_homepage_inactive))
                .addItem(new BottomNavigationItem(R.drawable.tab_financial_active, bottomItems[1]).setInactiveIconResource(R.drawable.tab_financial_inactive))
                .addItem(new BottomNavigationItem(R.drawable.tab_wealth_active, bottomItems[2]).setInactiveIconResource(R.drawable.tab_wealth_inactive))
                .addItem(new BottomNavigationItem(R.drawable.tab_more_active, bottomItems[3]).setInactiveIconResource(R.drawable.tab_more_inactive))
                .setActiveColor(R.color.colorTheme)
                .initialise();
        mainBottombar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                if (mainViewpager.getCurrentItem() != position) {
                    // BottomNavigationBar绑定SlideViewPager
                    mainViewpager.setCurrentItem(position, false);
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    /**
     * 组件通讯(修改当前状态)
     */
    private void onRxBusEventResponse() {
        Subscription subscribe = RxBus.getDefault()
                .toObservable(Integer.class)
                .subscribe(new SucceedSubscribe<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        super.onNext(integer);
                        switch (integer) {
                            case IntentKey.MAINACT_SVP_SETPOSITION_1:
                                mainViewpager.setCurrentItem(1);
                                break;
                        }
                    }
                });
        addSubscription(subscribe);
    }

    /**
     * 菜单键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return doubleClickExitAppUtil.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 销毁MainActivity
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 程序从前台退居后台调用的方法 一般用来程序退居后台释放内存
     *
     * @param level
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch (level) {
            case TRIM_MEMORY_UI_HIDDEN: // 程序退居后台之后,可在该方法对程序做内存释放,提高用户体验
                Logger.d("App Run To BackGround");
                break;
        }
    }
}
