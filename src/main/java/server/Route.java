package server;

import java.util.Objects;

public class Route {
	String URL;
    String Method;

    public Route(String URL, String Method){
        this.URL = URL;
        this.Method = Method;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        if(this.URL.equals(((Route) o).URL) && this.Method.equals(((Route)o).Method)) return true;

        return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(URL, Method);
    }
}
