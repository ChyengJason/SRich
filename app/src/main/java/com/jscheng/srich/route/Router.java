package com.jscheng.srich.route;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created By Chengjunsen on 2019/2/21
 */
public class Router {
    private static HashMap<String, Class> mRouteTable;

    private static List<RouterInterceptor> mInterceptor;

    static {
        mRouteTable = new HashMap<>();
        mInterceptor = new ArrayList<>();
        RouterConfig.config();
    }

    public static <T> void addUri(String uri, Class<T> cls) {
        mRouteTable.put(uri, cls);
    }

    public static void addInterceptor(RouterInterceptor interceptor) {
        mInterceptor.add(interceptor);
    }

    public static Map<String, Class> getRouteTable() {
        return mRouteTable;
    }

    public static List<RouterInterceptor> getInterceptor() {
        return mInterceptor;
    }

    public static RouterRequest with(Context context) {
        RouterRequest request = new RouterRequest(context);
        return request;
    }
}
