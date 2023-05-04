package server;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class JsonBuilder {
    private Map<String, Object> jsonMap;

    public JsonBuilder() {
        jsonMap = new HashMap<>();
    }

    public JsonBuilder add(String key, Object value) {
        jsonMap.put(key, value);
        return this;
    }

    public JsonBuilder remove(String key){
        jsonMap.remove(key);
        return this;
    }

    public String build() {
        return convertToString(jsonMap);
    }

    private String convertToString(Object obj){
        StringBuilder sb = new StringBuilder();
        if(obj instanceof Iterable){
            Iterator<?> itr = ((Iterable<?>)obj).iterator();
            sb.append("[");
            if(itr.hasNext())   sb.append(convertToString(itr.next()));
            while(itr.hasNext()){
                sb.append(", ");
                sb.append(convertToString(itr.next()));
            }
            sb.append("]");
            return sb.toString();
        }
        if(obj instanceof Object[]){
            Object[] arr = (Object[])obj;
            sb.append("[");
            if(arr.length > 0){
                sb.append(convertToString(arr[0]));
            }
            for(int i = 1; i < arr.length; i++){
                sb.append(", ").append(convertToString(arr[i]));
            }
            sb.append("]");
            return sb.toString();
        }
        if(obj instanceof Map){
            Set<Map.Entry<Object, Object>> es = ((Map<Object, Object>)obj).entrySet();
            sb.append("{");
            for(Map.Entry<?, ?> entry: es){
                sb.append(convertToString(entry.getKey()));
                sb.append(" : ");
                sb.append(convertToString(entry.getValue()));                
            }
            sb.append("}");
            return sb.toString();
        }
        sb.append("\"");
        sb.append(obj.toString());
        sb.append("\"");
        return sb.toString();
    }
}