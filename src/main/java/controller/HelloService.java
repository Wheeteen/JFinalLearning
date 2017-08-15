package controller;

import com.jfinal.aop.Before;
import interceptor.AInterceptor;

/**
 * @description:
 * @author: youyinnn
 * @date: 2017/8/15
 */
class HelloService {

    @Before(AInterceptor.class)
    void hello(String name, int age){
        System.out.println("Say hello !!"+name+" : "+age);
    }

}
