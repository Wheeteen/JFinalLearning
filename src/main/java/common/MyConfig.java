package common;

import com.jfinal.config.*;
import com.jfinal.template.Engine;

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
        // 如这个配置会对每次的请求输出请求信息
        constants.setDevMode(true);

        /*
        * 这个参数设置url中携带多个参数之间的分隔符 默认是“-”
        * 在Controller中可以通过getPara(int index)分别取出这些值
        * */
        constants.setUrlParaSeparator("&");
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

    }

    /**
     * 此方法用来配置项目的全局拦截器
     *      全局拦截所有的action请求
     *      除非使用了@Clear在controller中清除
     * */
    @Override
    public void configInterceptor(Interceptors interceptors) {
        //interceptors.add(new AuthInterceptor);
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
