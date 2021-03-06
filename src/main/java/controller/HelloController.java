package controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import interceptor.AInterceptor;
import interceptor.InjectInterceptor;
import model.Account;
import model.Blog;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: youyinnn
 * @date: 2017/8/15
 */
// 配置一个Class级别的拦截器 它会拦截本类的所有方法
@Before(InjectInterceptor.class)
public class HelloController extends Controller {

    public void index(){
        System.out.println("hit");
        //renderText("这个方法是一个action");
        Account account = new Account();
        account.set("username", "John");

        // 把这个对象放到session中
        setSessionAttr("account", account);

        render("common/template.html");
    }

    //配置多个Method级别的拦截器 仅拦截本方法
    @Before({InjectInterceptor.class, AInterceptor.class})
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
        //HttpSession session = getSession();
    }

    /**
     * render系列方法
     * 这个系列的方法支持将不同的渲染视图返回给客户端
     * 支持的类型有：
     *  JFinal Template、FreeMarker、JSP、Velocity、JSON、File、Text、Html
     * 还看可以通过继承Render抽象类来无限扩展视图类型
     *
     * 通常情况下使用Controller.render(String)方法来渲染视图
     * 这时候视图渲染类型由JFinalConfig.configConstant(constant)配置中的
     *  constant.setViewType(ViewType)来决定
     *      该方法支持的viewType有：
     *          JFINAL_TEMPLATE
     *          FreeMarker
     *          JSP
     *          Velocity
     *  缺省时默认为JFINAL_TEMPLATE
     *
     *  需要注意的是 如果我们的Controller方法里面没有任何的路由方法 那么默认的路由行为会路由到viewPath下和方法名一样的html文件
     *  比如下面的方法如果没有任何的路由行为 就会默认路由到如下路径
     *  D:\Users\bigyellow\workspace\JFinalLearning\src\main\webapp/testRender.html
     *
     *  我们可以调用路由方法来取消默认的路由行为
     *      renderNull() 路由反馈空页 不渲染任何数据
     * */
    public void testRender(){
        System.out.println("testRender");

        //路由到/path/test.html    这里path为“/” 所以视图路径为“/test.html”
        //render("test.html");

        //路由到根路径下的aaa/test.html
        //render("/aaa/test.html");

        List<Account> accountsList = new ArrayList<>();
        Account a1 = new Account().set("username", "Jim").set("balance", 1010);
        Account a2 = new Account().set("username", "Jany").set("balance", 1020);
        Account a3 = new Account().set("username", "Johnson").set("balance", 5100);

        accountsList.add(a1);
        accountsList.add(a2);
        accountsList.add(a3);

        //路由返回如下格式Json 以accounts为根的list数据
        //renderJson("accounts", accountsList);

        //{
        //    "accounts": [
        //    {
        //        "balance": 1010,
        //            "username": "Jim"
        //    },
        //    {
        //        "balance": 1020,
        //            "username": "Jany"
        //    },
        //    {
        //        "balance": 5100,
        //            "username": "Johnson"
        //    }
        //  ]
        //}

        // 路由返回{"name":"James","age":20}Json对象
        //renderJson(new Record().set("name", "James").set("age", 20));

        // 路径返回test.html页面并且返回404错误码
        //renderError(404, "test.html");

        // 路由反馈空页 不渲染任何数据
        renderNull();
    }

    /**
     * 对于控制层的触发 只需要简单的访问action方法即可
     * 但是对于业务层拦截器的触发 需要先使用enhance方法对目标对象进行增强 然后再去调用目标方法
     * 以下是例子
     *
     * Duang、Enhancer用来对目标进行增强 让其拥有AOP能力 具体看“AOPEverywhere”
     * */
    public void hello(){
        HelloService helloService = enhance(HelloService.class);

        helloService.hello("Jim",20);
    }

    public void backToController(){
        HttpSession session = getSession();

        Account account = (Account) session.getAttribute("account");

        System.out.println(account);

        renderNull();
    }
}
