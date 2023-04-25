package server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Invocation {

    private Object object;
    private Method method;

    public Invocation(Object object, Method method){
        this.object = object;
        this.method = method;
    }

    public Object run(HTTPRequest request){

        try {
            return method.invoke(object, request);
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

}
