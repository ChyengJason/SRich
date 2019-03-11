package com.jscheng.srich.route;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created By Chengjunsen on 2019/2/21
 */
public class ActivityInterceptor implements RouterInterceptor {

    @Override
    public RouterResponse process(RouterChain chain) {
        RouterRequest request = chain.getRequest();
        RouterResponse response = chain.proceed(request);
        if (response.isDone() || !isAimInstance(response.getCls())) {
            return response;
        }
        return doRequst(request, response);
    }

    private boolean isAimInstance(Class cls) {
        if (cls == null) {
            return false;
        }
        boolean isActivity =  Activity.class.isAssignableFrom(cls);
        return isActivity;
    }

    private RouterResponse doRequst(RouterRequest request, RouterResponse response) {
        Class activityCls = response.getCls();
        Context context = request.getContext();
        Bundle bundle = request.getBundle();
        Intent intent = new Intent(context, activityCls);

        response.setIntent(intent);
        response.setDone(true);

        if (request.isJump()) {
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
        return response;
    }
}
