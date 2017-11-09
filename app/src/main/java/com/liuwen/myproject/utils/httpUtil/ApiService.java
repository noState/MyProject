package com.liuwen.myproject.utils.httpUtil;

import com.liuwen.myproject.base.constant.Constant;
import com.liuwen.myproject.base.entities.response.BannerRes;
import com.liuwen.myproject.base.entities.response.UpdateInfoRes;

import java.util.ArrayList;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-07-26
 */

public interface ApiService {

    /**
     * 获取App更新信息
     *
     * @param Version
     * @param OSType
     * @return
     */
    @FormUrlEncoded
    @POST(Constant.URL_CONNECTED_MOBILE + Constant.URL_GETSERVICEUPDATEINFO)
    Observable<HttpResult<UpdateInfoRes>> getUpdateInfo(
            @Field("Version") String Version
            , @Field("OSType") String OSType);

    /**
     * 获取Banner图信息
     *
     * @return
     */
//    @HTTP(method = "post", path = Constant.URL_CONNECTED_MOBILE + Constant.URL_GETBARSLICE, hasBody = false)
    @POST(Constant.URL_CONNECTED_MOBILE + Constant.URL_GETBARSLICE)
    Observable<HttpResult<ArrayList<BannerRes>>> getBannerInfo();
}
