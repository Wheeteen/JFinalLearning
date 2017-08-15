package common;

import com.jfinal.config.*;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.template.Engine;
import common.routes.AdminRoutes;
import common.routes.FrontRoutes;

/**
 * @description:
 * @author: youyinnn
 * @date: 2017/8/14
 */
public class MyConfig extends JFinalConfig {

    /**
    * 此方法用来配置JFinal常量
    * */
    @Override
    public void configConstant(Constants constants) {
        // 如这个配置会对每次的请求输出请求信息 *开发者模式*
        constants.setDevMode(true);

        /*
        * 这个参数设置url中携带多个参数之间的分隔符 默认是“-”
        * 在Controller中可以通过getPara(int index)分别取出这些值
        * */
        constants.setUrlParaSeparator("&");

        /*
        * PropKit工具类用来操作外部配置文件
        * 可以在任意时空使用
        * 第一次使用use加在的配置会成为主配置
        * 可以通过PropKit.get(...)直接取值
        * 这个方法加载配置文件内容后会讲数据存在内存里面
        * 可以通过PropKit.useless(...)来清除内容
        * */
        PropKit.use("firstConfig.txt");

        constants.setDevMode(PropKit.getBoolean("devMode"));
    }

    /**
     * 此方法用来配置JFinal访问的路由
     * */
    @Override
    public void configRoute(Routes routes) {
        /*
        * 这个方法用于为该Routes内部的所有Controller设置视图渲染时的基础路径
        *   基础路径：
        *       Routes.add(controllerKey, controllerClass, viewPath)方法传入的viewPath
        *       Controller.render(view)方法传入的view
        *   联合组成最终的视图路径
        *   finalView = baseViewPath + viewPath + view
        *
        * 注意：当view以“/”打头时表示绝对路径 baseViewPath 和 viewPath 会被忽略
        * */
        routes.setBaseViewPath("BaseView");

        //routes.addInterceptor(new FrontInterceptor);

        /*
        * Routes add(String controllerKey, Class<? extends Controller> controllerClass, String viewPath)
        * Routes add(String controllerKey, Class<? extends Controller> controllerClass)
        *   这是添加路由的方法
        *       参数1 controllerKey：指访问某个Controller所需要的字符串 唯一对应
        *       参数2 controllerClass：Controller的类
        *       参数3 viewPath：该Controller返回的视图的相对路径 未指定的时候默认是controllerKey
        *
        *   路由规则：
        *           url组成                           访问目标
        *       controllerKey                   Controller.index()
        *       controllerKey/method            Controller.method()
        *       controllerKey/method/v0-v1      Controller.method() 所带参数值为：v0-v1
        *       controllerKey/v0-v1             Controller.index() 所带参数值为：v0-v1
        *
        *   规则指出：JFinal访问一个确切的Action需要使用controllerKey和method来精确定位
        *   urlPara是为了能在url中携带参数
        *
        *   注意：
        *       1、controllerKey、method、urlPara三部分必须使用“/”分隔
        *       2、controllerKey自身也可以包含正斜杠 如“/admin/article” 实质上实现了struts2的命名空间
        * */
        //routes.add("/hello",HelloController.class);

        /*
        * 我们把前后端的路由拆分后在这里合并起来
        * 这样的方式让JFinalConfig文件更加简洁
        * 同时也方便团队开发
        * 每个模块的开发团队都不需要同时修改JFinalConfig文件
        * 避免版本冲突
        * */
        routes.add(new FrontRoutes());
        routes.add(new AdminRoutes());
    }

    /**
     * 此方法用来配置模板引擎
     * */
    @Override
    public void configEngine(Engine engine) {

    }

    /**
     * 此方法用来配置JFinal的插件
     * 如配置Druid等
     * JFinal插件架构是最主要的拓展方式之一
     * */
    @Override
    public void configPlugin(Plugins plugins) {
        // 非第一次使用PropKit.use(...)加载的配置 需要通过每次使用use类指定配置文件名后再来取值
        String username = PropKit.use("secondConfig.txt").get("username");

        // 使用得到的username

        // 也可以先得到一个Prop对象 再通过该对象取值
        Prop p = PropKit.use("secondConfig.txt");
        String password = p.get("password");

        // 使用得到的password
    }

    /**
     * 此方法用来配置项目的全局拦截器
     *      全局拦截所有的action请求
     *      除非使用了@Clear在controller中清除
     * 全局拦截器分为两种：
     *  1、控制层拦截器
     *  2、业务层拦截器
     *
     * 多级别拦截器的执行顺序如下：
     *  Global、Inject、Class、Method
     *
     * Clear注释：
     *  用于清除自身所处层级以上的拦截器
     *
     *  例：
     *      Clear声明在Method层的时候 会清除Global、Inject、CLass级别的拦截器
     *      Clear声明在Class层的时候 会清除Global、Inject级别的拦截器
     *
     *      Clear注释还可以设置参数 用于清除指定的"上层"拦截器 @Clear({AAA.class,BBB.class})
     * */
    @Override
    public void configInterceptor(Interceptors interceptors) {
        //interceptors.add(new AuthInterceptor);

        // 全局的控制层拦截器
        //interceptors.addGlobalActionInterceptor(...);

        // 全局业务层拦截器
        //interceptors.addGlobalServiceInterceptor(...);
    }

    /**
     * 此方法用来配置项目的Handler
     * */
    @Override
    public void configHandler(Handlers handlers) {

    }

    /**
     * JFinal会在系统启动完成之后回调afterJFinalStart方法
     * */
    @Override
    public void afterJFinalStart() {
        super.afterJFinalStart();
        System.out.println("JFinal Started!!");
    }

    /**
     * JFinal会在系统关闭前回调beforeJFinalStop方法
     * */
    @Override
    public void beforeJFinalStop() {
        super.beforeJFinalStop();
        System.out.println("JFinal Started!!");
    }
}
