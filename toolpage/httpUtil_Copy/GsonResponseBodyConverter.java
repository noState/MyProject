package com.liuwen.httpproject.utils.httputil;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-07-26
 * <p>
 * <p>
 * <p>
 * 注意:根据后台响应数据对HttpResult做相应的调整，否则HttpResult无法被解析出来
 */

public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final Type type;

    GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        Logger.json(response);
        return gson.fromJson(response, type);
    }
}
