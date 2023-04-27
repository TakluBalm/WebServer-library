package server;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Invocation {

    private Object object;
    private Method method;
	private boolean[] paramMask;

	public boolean[] getParameterMask(){
		return paramMask;
	}

    public Invocation(Object object, Method method, boolean[] paramMask){
        this.object = object;
        this.method = method;
        this.paramMask = paramMask;
    }

    public Object invoke(Request request){

		String[] hooks = request.getPath().split("/");
		ArrayList<Object> args = new ArrayList<>();
		args.add(request);
		for(int i = 0; i < paramMask.length; i++){
			if(paramMask[i]){
				args.add(hooks[i]);
			}
		}

        try {
            return method.invoke(object, args.toArray());
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

}
