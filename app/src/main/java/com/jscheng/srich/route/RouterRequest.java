package com.jscheng.srich.route;

import android.content.Context;
import android.content.Intent;

import java.util.List;

/**
 * Created By Chengjunsen on 2019/2/21
 */
public class RouterRequest {
    private Context context;
    private String uri;
    private boolean isJump;

    public RouterRequest(Context context) {
        this.context = context;
        this.isJump = false;
    }

    public RouterRequest route(String uri) {
        this.uri = uri;
        return this;
    }

    public boolean go() {
        check();
        this.isJump = true;
        return process().isDone();
    }

    public Intent getIntent() {
        check();
        this.isJump = false;
        RouterResponse response = process();
        if (response.isDone()) {
            return response.getIntent();
        }
        return new Intent();
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
        if (uri == null || uri.isEmpty()) {
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
