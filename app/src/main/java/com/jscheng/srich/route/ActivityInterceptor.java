package com.jscheng.srich.route;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created By Chengjunsen on 2019/2/21
 */
public class ActivityInterceptor implements RouterInterceptor {

    @Override
    public RouterResponse process(RouterChain chain) {
        RouterRequest request = chain.getRequest();
        RouterResponse response = chain.proceed(request);
        if (response.isDone() || !isAimInstance(request.getAim())) {
            return response;
        }
        return doRequst(request, response);
    }

    private RouterResponse doRequst(RouterRequest request, RouterResponse response) {
        Class activityCls = request.getAim();
        Context context = request.getContext();
        response.setIntent(buildIntent(context, activityCls));
        response.setDone(true);

        if (request.isJump()) {
            context.startActivity(buildIntent(context, activityCls));
        }
        return response;
    }

    private boolean isAimInstance(Class cls) {
        boolean isActivity =  Activity.class.isAssignableFrom(cls);
        return isActivity;
    }

    private Intent buildIntent(Context context, Class cls) {
        Intent intent = new Intent(context, cls);
        return intent;
    }
}
