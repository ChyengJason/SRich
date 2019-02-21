package com.jscheng.srich.route;

import java.util.List;
import java.util.Stack;

/**
 * Created By Chengjunsen on 2019/2/21
 */
public class RouterChain {

    private RouterRequest request;

    private List<RouterInterceptor> interceptors;

    private int index;

    public RouterChain(RouterRequest request, List<RouterInterceptor> interceptors) {
        this.request = request;
        this.interceptors = interceptors;
        this.index = 0;
    }

    public RouterRequest getRequest() {
        return request;
    }

    public RouterResponse proceed(RouterRequest request) {
        this.request = request;
        if (index < 0 || index >= interceptors.size()) {
            return new RouterResponse();
        }
        return interceptors.get(index++).process(this);
    }
}
