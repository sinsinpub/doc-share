package com.jfinal.ext.kit.interceptor;

import com.github.sinsinpub.doc.hint.ThreadSafe;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * 只允许访问get和is方法的简单拦截器.
 * 
 * @author sin_sin
 * @version $Date: Nov 13, 2015 $
 */
@ThreadSafe
public class ReadOnlyInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation inv) {
        String methodName = inv.getMethodName();
        if (methodName.startsWith("get") || methodName.startsWith("is")) {
            inv.invoke();
        }
        throw new IllegalAccessError(String.format("Method %s is not accessible", methodName));
    }

}
