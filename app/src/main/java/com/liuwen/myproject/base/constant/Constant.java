package com.liuwen.myproject.base.constant;

import android.os.Environment;

import java.io.File;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-07-24
 */

public interface Constant {
    //App状态
    boolean IS_DEBUG = true;
    //是否打印日志
    boolean LOGGER_IS_SHOWLOG = IS_DEBUG;
    //重复点击事件差
    int DURATION = 500;
    //网络http适配处理
    int DEFAULT_TIMEOUT = 20;
    String CODE_RESPONSE_SUCCEED = "000";
    //数据加载事件
    int LOADINGTIME = 0;

    //Log文件
    String SAVE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "MyProject" + File.separator;
    String CACHE_PATH = SAVE_PATH + "Cache/";
    String LOG_PATH = SAVE_PATH + "Log/";

    //SP
    String SP_NORMAL = "myproject";
    String SP_NORMAL_ISFIRSTOPENAPP = "isFirstOpenApp";

    //BaseUrl
    String BASE_URL = IS_DEBUG ? "http://114.55.27.208:8555/" : "https://webapi.xxx.com/";
    //    String BASE_URL = IS_DEBUG ? "http://test.oolaile.com:8090/yuanhan/" : "https://x.oolaile.com/yuanhan/";
    String URL_CONNECTED_MOBILE = "api/";

    //POST获取服务器App版本信息
    String URL_GETSERVICEUPDATEINFO = "PopPage/GetUpdateInfo";
    //GET获取服务器App版本信息
    String URL_GETUPDATEINFO = "sys/getVers.htm";
    //POST首页Banner轮播图
    String URL_GETBARSLICE = "Home/GetBarSlice";

}
