package com.liuwen.httpproject.utils.httputil;

import com.orhanobut.logger.Logger;

import rx.Subscriber;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-07-25
 * <p>
 * <p>
 * <p>
 * Subscriber的封装 对OnError()方法进行处理
 */

public abstract class SucceedSubscribe<T> extends Subscriber<T> {

    @Override
    public void onNext(T t) {
    }

    @Override
    public void onCompleted() {
    }

    // TODO: 2017/07/25  doing more
    @Override
    public void onError(Throwable e) {
        Logger.e(e.toString());
        HttpMethods.clearSingleton();
        RxBus.getDefault().post(e);
    }
}
