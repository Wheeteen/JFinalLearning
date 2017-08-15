package interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * @description:
 * @author: youyinnn
 * @date: 2017/8/15
 */
public class InjectInterceptor implements Interceptor {

    /**
     * Interceptor可以对方法进行拦截
     * */
    @Override
    public void intercept(Invocation inv) {
        System.out.println("This is a Inject Interceptor");
        inv.invoke();

        /*
        * Invocation 作为拦截器的唯一重要参数提供了许多方法供拦截器使用
        *
        *       方法                                      描述
        * void invoke()                     传递本次调用 调用剩下的拦截器和目标方法
        * Controller getController()        获取Action调用的Controller对象（仅用于控制层拦截
        * String getActionKey()             获取。。。（仅。。
        * String getControllerKey()         获取。。。（仅。。
        * String getViewPath()              获取Action调用的视图路径（仅。。
        * <T> T getTarget()                 获取被拦截方法所属的对象
        * Method getMethod()                获取被拦截方法的Method对象
        * String getMethodName()            。。
        * Object[] getArgs()                获取被拦截方法的所有参数
        * Object getArg(int)                获取被拦截方法的指定需要的参数
        * <T> T getReturnValue()            获取被拦截方法的返回值
        * void setArg(int)                  设置被拦截方法指定序号的参数值
        * void serReturnValue(Object)       设置被拦截方法的返回值
        * boolean isActionInvocation()      判断是否为Action调用 也即判断是否为控制层拦截
        * */
    }
}
