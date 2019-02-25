package com.jscheng.srich.route;

/**
 * Created By Chengjunsen on 2019/2/25
 */
public class UriAnalyzerInterceptor implements RouterInterceptor {
    @Override
    public RouterResponse process(RouterChain chain) {
        RouterRequest request = chain.getRequest();
        RouterResponse response = chain.proceed(request);
        String uri = request.getUri();
        Class cls = Router.getRouteTable().get(uri);
        response.setCls(cls);
        return response;
    }
}
