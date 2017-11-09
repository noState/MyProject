package com.liuwen.myproject.base.entities.response;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-08-23
 */

public class BannerRes {

    public String Title;
    public String Url;
    public int Position;
    public String GotoUrl;
    public int Ad_ID;
    public String Ad_NO;

    @Override
    public String toString() {
        return "BannerRes{" +
                "Title='" + Title + '\'' +
                ", Url='" + Url + '\'' +
                ", Position=" + Position +
                ", GotoUrl='" + GotoUrl + '\'' +
                ", Ad_ID=" + Ad_ID +
                ", Ad_NO='" + Ad_NO + '\'' +
                '}';
    }

}
