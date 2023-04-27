package server;

public class Route {
	String Path;
    String Method;
	private String[] subHooks;
	private boolean[] paramMask;

    public Route(String Method, String Path){
        this.Path = Path;
        this.Method = Method;
		this.subHooks = Path.split("/");
		this.paramMask = new boolean[subHooks.length];
		for(int i = 0; i < subHooks.length; i++){
			if(subHooks[i].length() == 0) continue;
			if(subHooks[i].charAt(0) == '{' && subHooks[i].charAt(subHooks[i].length()-1) == '}'){
				paramMask[i] = true;
			}else{
				paramMask[i] = false;
			}
		}
    }

	public boolean[] getParameterMask(){
		return paramMask;
	}

	@Override
	public String toString() {
		return new String(Method+"/"+Path);
	}

}
