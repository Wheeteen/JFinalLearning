package common.routes;

import com.jfinal.config.Routes;

/**
 * @description:
 * @author: youyinnn
 * @date: 2017/8/14
 */
public class FrontRoutes extends Routes {

    /**
     * 这里是给前端路由的配置
     * */
    @Override
    public void config() {
        setBaseViewPath("/view/front");
        //add("/", IndexController.class);
        //add("/blog", BlogController.class);
    }
}
