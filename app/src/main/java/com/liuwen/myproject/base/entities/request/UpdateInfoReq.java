package com.liuwen.myproject.base.entities.request;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-09-04
 */

public class UpdateInfoReq {

    public String Version;

    public String OSType;

    @Override
    public String toString() {
        return "UpdateInfoReq{" +
                "Version='" + Version + '\'' +
                ", OSType='" + OSType + '\'' +
                '}';
    }
}
