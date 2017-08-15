package controller;

import com.jfinal.core.Controller;
import model.Blog;

import javax.servlet.http.HttpSession;

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

    /**
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
    public void testGetPara(){


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

    /**
     * getModel方法用来接收表单域传递过来的model对象
     * 表单域名称以“modelName.attrName”命名attrName必须和数据表字段名完全一致
     * getBean方法用于支持又getter、setter方法的Model
     * 表单传输的attrName需要和setter名一致
     * */
    public void testGetBeanAndGetModel(){

        //<form action="/blog/save" method="post">
        //        <input name="blog.title" type="text">
        //        <input name="blog.content" type="text">
        //        <input value="Submit" type="Submit">
        //</form>

        // 表单的modelName正好是Blog类名的小写
        Blog blog = getModel(Blog.class);

        // 如果表单域名称为“otherName.title”可以加上一个参数来获取
        blog = getModel(Blog.class, "otherName");

        // 如果在开发的时候需要避免使用modelName前缀 可以使用空串作为modelName来实现获取
        blog = getModel(Blog.class, "");

        // 如果在接收时希望跳过数据转换或者属性名错误异常 则可以传入true参数
        blog = getModel(Blog.class, "", true);

        // 此方法转调了HttpServletRequest.setAttribute(String, Object)方法
        setAttr("age", 20);
    }

    /**
     * session操作
     * set get getSession
     * */
    public void testSession(){
        setSessionAttr("sID","6RW6EF");
        Object sID = getSessionAttr("sID");
        HttpSession session = getSession();
    }
}