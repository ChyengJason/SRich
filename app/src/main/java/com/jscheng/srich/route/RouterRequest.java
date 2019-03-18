package com.jscheng.srich.route;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Chengjunsen on 2019/2/21
 */
public class RouterRequest {
    private Context context;
    private String uri;
    private boolean isJump;
    private Bundle bundle;

    public RouterRequest(Context context) {
        this.context = context;
        this.isJump = false;
        this.bundle = new Bundle();
    }

    public RouterRequest route(String uri) {
        this.uri = uri;
        return this;
    }

    public RouterRequest intent(Intent intent) {
        this.bundle = intent.getExtras();
        return this;
    }

    public RouterRequest intent(String key, int what) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        this.bundle.putInt(key, what);
        return this;
    }

    public RouterRequest intent(String key, String what) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        this.bundle.putString(key, what);
        return this;
    }

    public RouterRequest intent(String key, List<String> what) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        this.bundle.putStringArrayList(key, new ArrayList(what));
        return this;
    }


    public RouterRequest intent(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        this.bundle.putAll(bundle);
        return this;
    }

    public boolean go() {
        check();
        this.isJump = true;
        return process().isDone();
    }

    public Bundle getBundle() {
        check();
        if (bundle == null) {
            bundle = new Bundle();
        }
        return bundle;
    }

    private RouterResponse process() {
       List<RouterInterceptor> interceptors = Router.getInterceptor();
       RouterChain chain = new RouterChain(this, interceptors);
       return chain.proceed(this);
    }

    private void check() {
        if (context == null) {
            throw new RuntimeException("context cannot be null");
        }
        if (TextUtils.isEmpty(uri)) {
            throw new RuntimeException("uri cannot be null or isEmpty");
        }
        Object object = Router.getRouteTable().get(uri);
        if ( object == null) {
            throw new RuntimeException("you should register uri at first");
        }
    }

    public String getUri() {
        return uri;
    }

    public Context getContext() {
        return context;
    }

    public boolean isJump() {
        return isJump;
    }
}
