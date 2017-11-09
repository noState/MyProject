package com.liuwen.myproject.utils.httpUtil;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-07-26
 */

public class HttpResult<T> {

    public T RespData;

    public String RespCode;

    public String RespDesc;

    @Override
    public String toString() {
        return "HttpResult{" +
                "RespData=" + RespData +
                ", RespCode='" + RespCode + '\'' +
                ", RespDesc='" + RespDesc + '\'' +
                '}';
    }
}
