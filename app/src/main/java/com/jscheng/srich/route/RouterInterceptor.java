package com.jscheng.srich.route;

/**
 * Created By Chengjunsen on 2019/2/21
 */
public interface RouterInterceptor {

    RouterResponse process(RouterChain chain);

}
