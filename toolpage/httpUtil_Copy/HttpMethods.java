package com.liuwen.httpproject.utils.httputil;

import com.google.gson.Gson;
import com.liuwen.httpproject.base.app.MyProjectApplication;
import com.liuwen.httpproject.base.constant.Constant;
import com.liuwen.httpproject.base.entities.response.BannerRes;
import com.liuwen.httpproject.base.entities.response.GetUpdateInfoRes;
import com.liuwen.httpproject.base.entities.response.UpdateInfoRes;
import com.liuwen.httpproject.utils.CommonUtils;
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
    private Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
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
        }
    };

    private HttpMethods() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (Constant.LOGGER_IS_SHOWLOG) {
                    Logger.d(message);
                }
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        httpClientBuilder.interceptors().add(loggingInterceptor);
        Cache cache = new Cache(new File(MyProjectApplication.getInstance().getExternalCacheDir(), "ExampleHttpCache"), 1024 * 1024 * 100);
        httpClientBuilder.cache(cache);
        httpClientBuilder.addNetworkInterceptor(mRewriteCacheControlInterceptor);
        httpClientBuilder.addInterceptor(mRewriteCacheControlInterceptor);
        httpClientBuilder.retryOnConnectionFailure(true);
        httpClientBuilder.connectTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        //配置BaseURL 对后端建议接口标准化
        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(ResponseConvertFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Constant.BASE_URL)
                .build();

        mApiService = retrofit.create(ApiService.class);
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
}
