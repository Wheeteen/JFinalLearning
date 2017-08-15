package controller;

import com.jfinal.core.Controller;

/**
 * @description:
 * @author: youyinnn
 * @date: 2017/8/15
 */
public class HelloController extends Controller {

    public void index(){
        renderText("这个方法是一个action");
    }

    public String test(){
        return "index.html";
    }

    public void testPara(){
        /*
        * Controller提供了getPara系列方法来从请求中获取参数 getPara系列方法分为两种
        * 第一种：
        *   第一个形参为String的getPara系列方法
        *   该系列方法是对HttpServletRequest.getParameter(String name)的封装
        *
        * 第二种：
        *   第一个形参为int或者无形参的getPara系列方法
        *   这种方法是获取urlPara中所带的参数值
        *
        * getParaMap和getParaNames分别对应HttpServletRequest中的getParameterMap和getParameterNames
        *
        * 以下是一些方法调用例子
        * */

        // 获取表单域中title值
        String title = getPara("title");
        // 获取表单域中phoneNumber值并转成Long类型
        Long phoneNumber = getParaToLong("phoneNumber");

        // http://localhost/controllerKey/method/2-8-Jone-N8

        // 获取urlPara中的第一个值 即“2”
        String paraZero = getPara(0);
        // 获取urlPara中的第四个值 即N8 但是这个时候N8表示为“-8” 所以我们获取到的是-8
        Integer paraThreeToInt = getParaToInt(3);
        // 获取整个urlPara 即“2-8-Jone-N8”
        String para = getPara();
    }
}
