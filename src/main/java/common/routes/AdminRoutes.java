package common.routes;

import com.jfinal.config.Routes;
import interceptor.AInterceptor;

/**
 * @description:
 * @author: youyinnn
 * @date: 2017/8/14
 */
public class AdminRoutes extends Routes {

    /**
     * 这里是给后端路由的配置
     * */
    @Override
    public void config() {
        setBaseViewPath("/view/admin");

        /*
        * 以下方法向AdminRoutes中添加了拦截器
        * 这个拦截器会工作于改Routes类下的所有Controller
        * 是Class级别的拦截
        * */
        addInterceptor(new AInterceptor());

        //add("/admin", AdminController.class);
        //add("/admin/user", UserController.class);
    }
}
