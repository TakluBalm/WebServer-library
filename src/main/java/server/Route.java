package server;

import java.util.Objects;

public class Route {
	String Path;
    String Method;

    public Route(String Path, String Method){
        this.Path = Path;
        this.Method = Method;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        if(this.Path.equals(((Route) o).Path) && this.Method.equals(((Route)o).Method)) return true;

        return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(Path, Method);
    }
}
