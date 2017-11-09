package com.liuwen.myproject.utils.httpUtil;

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
        Logger.d("Succeed! Data >>> " + t.toString());
    }

    // TODO: 2017/07/25 onCompleted() 在 onNext()输出完后调用 与 onError()互斥
    @Override
    public void onCompleted() {
    }

    // TODO: 2017/07/25  doing more
    @Override
    public void onError(Throwable e) {
        Logger.e("Fail! >>> " + e.toString());
        HttpMethods.clearSingleton();
        RxBus.getDefault().post(e);
    }
}
