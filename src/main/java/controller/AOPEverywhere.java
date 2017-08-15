package controller;

import com.jfinal.aop.Duang;
import com.jfinal.aop.Enhancer;
import interceptor.InjectInterceptor;
import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: youyinnn
 * @date: 2017/8/15
 */
public class AOPEverywhere {

    /**
     * Duang、Enhancer用来对目标进行增强 让其拥有AOP能力
     *
     * 这两类方法可以在任何地方对目标进行增强
     * 他们和Controller.enhance()系方法在功能上完全一样
     * */
    public static void main(String[] args) {
        HelloService helloService = Duang.duang(HelloService.class);

        helloService.hello("Robb",30);

        HelloService helloService1 = Enhancer.enhance(HelloService.class);
        helloService1.hello("Jimmy",10);
    }

    /**
     * Inject拦截器是指在使用enhancer或者duang方法增强时使用参数传入的拦截器
     * Inject可以对目标完全无入侵的应用AOP
     *
     * 加入我们需要拦截的方法目标在Jar包中 我们无法对其加上Before注释 这时可以使用Inject拦截器进行AOP增强
     * */
    @Test
    public void injectDemo(){
        HelloService helloService = Enhancer.enhance(HelloService.class, InjectInterceptor.class);

        // 这里可以掩饰顺序 Inject先于Method
        helloService.hello("David", 15);
    }
}
