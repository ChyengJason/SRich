package com.jscheng.srich.route;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 基于APT写的简单路由（可去掉）
 * Created By Chengjunsen on 2019/2/21
 */
public class Router {
    private static final String ROUTE_ROOT_PAKCAGE = "com.jscheng.processor";

    private static HashMap<String, Class> mRouteTable = new HashMap<>();

    private static List<RouterInterceptor> mInterceptor = new ArrayList<>();

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

    public static void init(Application application) {
        initInterceptor();
        initRoute(application);
    }

    private static void initInterceptor() {
        Router.addInterceptor(new LogInterceptor());
        Router.addInterceptor(new ActivityInterceptor());
        Router.addInterceptor(new UriAnalyzerInterceptor());
    }

    private static void initRoute(Application application) {
        try {
            Set<String> routerMap = RouteInfoUtil.getFileNameByPackageName(application, ROUTE_ROOT_PAKCAGE);
            for (String className : routerMap) {
                ((IRouteInfo) Class.forName(className).getConstructor().newInstance()).load(mRouteTable);
            }
        } catch (PackageManager.NameNotFoundException |
                ClassNotFoundException |
                InterruptedException |
                InstantiationException |
                InvocationTargetException |
                NoSuchMethodException |
                IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
