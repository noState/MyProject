package com.liuwen.myproject.utils.httpUtil;

import android.util.Log;

import com.google.gson.Gson;
import com.liuwen.myproject.base.app.MyProjectApplication;
import com.liuwen.myproject.base.constant.Constant;
import com.liuwen.myproject.base.entities.request.UpdateInfoReq;
import com.liuwen.myproject.base.entities.response.BannerRes;
import com.liuwen.myproject.base.entities.response.UpdateInfoRes;
import com.liuwen.myproject.utils.CommonUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-07-26
 * <p>
 * <p>
 * <p>
 * 单例HttpMethod 异步
 */

public class HttpMethods {

    private Retrofit retrofit;
    private ApiService mApiService;
    private static HttpMethods INSTANCE;

    public static HttpMethods getInstance() {
        synchronized (HttpMethods.class) {
            if (INSTANCE == null) {
                INSTANCE = new HttpMethods();
            }
        }
        return INSTANCE;
    }

    public static void clearSingleton() {
        INSTANCE = null;
    }

    // 云端响应头拦截器,用来配置缓存策略 设缓存有效期为两天
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    // 设定时间读取缓存,一般情况下建议设置为0
    private static final long CACHE_AGE_SEC = 0;
    private Interceptor mRewriteCacheControlInterceptor = chain -> {
        Request request = chain.request();
        // 在这里统一配置请求头缓存策略以及响应头缓存策略
        if (CommonUtils.isNetworkConnected(MyProjectApplication.getInstance())) {
            // 在有网的情况下CACHE_AGE_SEC秒内读缓存，大于CACHE_AGE_SEC秒后会重新请求数据
            request = request.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + CACHE_AGE_SEC).build();
            Response response = chain.proceed(request);
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + CACHE_AGE_SEC).build();
        } else {
            // 无网情况下CACHE_STALE_SEC秒内读取缓存，大于CACHE_STALE_SEC秒缓存无效报504
            request = request.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC).build();
            Response response = chain.proceed(request);
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC).build();
        }
    };

    private HttpMethods() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
            if (Constant.LOGGER_IS_SHOWLOG) {
//                Logger.d(message);
                Log.i("HttpLogger >>> ", message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder.interceptors().add(loggingInterceptor);
        Cache cache = new Cache(new File(MyProjectApplication.getInstance().getExternalCacheDir(), "ExampleHttpCache"), 1024 * 1024 * 100);
        httpClientBuilder.cache(cache);
        httpClientBuilder.addNetworkInterceptor(mRewriteCacheControlInterceptor);
        httpClientBuilder.addInterceptor(mRewriteCacheControlInterceptor);
        httpClientBuilder.retryOnConnectionFailure(true);
        httpClientBuilder.connectTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        // 配置BaseURL 对后端建议接口标准化
        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(ResponseConvertFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Constant.BASE_URL)
                .build();

        mApiService = retrofit.create(ApiService.class);
    }

    // 注意 根据后端接口响应数据 对此进行调整 否则throw new ApiException(respCode, httpResult.RespCode, httpResult.RespData);
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            String respCode = httpResult.RespCode;
            if (!respCode.equals(Constant.CODE_RESPONSE_SUCCEED)) {
                throw new ApiException(respCode, httpResult.RespCode, httpResult.RespData);
            }
            if (httpResult.RespCode != null) {
                return httpResult.RespData;
            }
            return null;
        }
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

//    ========================================================================RequestMethod==========================================================================

    /**
     * 获取App更新信息
     *
     * @param succeedSubscribe
     * @param updateInfoReq
     */
    public void getUpdateInfo(SucceedSubscribe<UpdateInfoRes> succeedSubscribe, UpdateInfoReq updateInfoReq) {
        Observable<UpdateInfoRes> observable = mApiService.getUpdateInfo(updateInfoReq.Version, updateInfoReq.OSType).map(new HttpResultFunc<>());
        toSubscribe(observable, succeedSubscribe);
    }

    /**
     * 获取Banner信息
     *
     * @param succeedSubscribe
     */
    public void getBannerInfo(SucceedSubscribe<ArrayList<BannerRes>> succeedSubscribe) {
        Observable<ArrayList<BannerRes>> observable = mApiService.getBannerInfo().map(new HttpResultFunc<>());
        toSubscribe(observable, succeedSubscribe);
    }
}
