package com.liuwen.myproject.base.entities.response;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-07-26
 */

public class UpdateInfoRes {
    //最新版本号
    public String version;
    //最新下载链接
    public String downloadUrl;
    //是否要强制更新
    public boolean isforcibly;
    //弹出的图片
    public String imgUrl;

    @Override
    public String toString() {
        return "UpdateInfoRes{" +
                "version='" + version + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", isforcibly=" + isforcibly +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
