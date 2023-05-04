package server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Invocation {

    private Object object;
    private Method method;
	private String[] paramMask;

	public String[] getParameterMask(){
		return paramMask;
	}

    public Invocation(Object object, Method method, String path){
        this.object = object;
        this.method = method;
		String[] hooks = path.split("/");
		paramMask = new String[hooks.length];
		for(int i = 0; i < hooks.length; i++){
			if(hooks[i].length() == 0)	continue;
			if(hooks[i].charAt(0) == '{' && hooks[i].charAt(hooks[i].length()-1) == '}'){
				paramMask[i] = hooks[i].substring(1, hooks[i].length()-1);
			}else{
				paramMask[i] = null;
			}
		}
    }

    public Object invoke(Request request) throws InvocationTargetException{

		String[] hooks = request.getPath().split("/");
		for(int i = 0; i < paramMask.length; i++){
			if(paramMask[i] != null){
				request.params.put(paramMask[i], hooks[i]);
			}
		}
		try{
			System.out.println(method.getName()+": "+method.getClass());
			return method.invoke(object, request);
		} catch(IllegalAccessException e){
			e.printStackTrace();
			return null;
		}
    }

}
