package common.routes;

import com.jfinal.config.Routes;

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

        //
        //addInterceptor(new AdminInterceptor);

        //add("/admin", AdminController.class);

        //add("/admin/user", UserController.class);
    }
}
