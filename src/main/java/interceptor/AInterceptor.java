package interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * @description:
 * @author: youyinnn
 * @date: 2017/8/15
 */
public class AInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation inv) {
        System.out.println("Before the AInterceptor");
        inv.invoke();
        System.out.println("After the AInterceptor");
    }
}
