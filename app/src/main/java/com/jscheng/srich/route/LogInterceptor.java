package com.jscheng.srich.route;

import android.util.Log;

/**
 * Created By Chengjunsen on 2019/2/21
 */
public class LogInterceptor implements RouterInterceptor {
    private static final String TAG= "RouterLog";
    @Override
    public RouterResponse process(RouterChain chain) {
        RouterRequest request = chain.getRequest();
        Log.d(TAG, request.getUri());
        RouterResponse response = chain.proceed(request);
        return response;
    }
}
