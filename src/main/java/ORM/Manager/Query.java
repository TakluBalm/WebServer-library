package ORM.Manager;

import ORM.ClassTable.Column;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Query<T> {
    private Class<T> modelClass;
    private String tableName;
    private String selectClause;
    private String whereClause;
    private Connection con;

    public Query(Class<T> modelClass, Connection con) {
        this.con  = con;
        this.modelClass = modelClass;
        this.tableName = modelClass.getSimpleName().toLowerCase();
        this.selectClause = "SELECT * FROM " + this.tableName;
        this.whereClause = "";
    }
    public Query<T> filter(String field,Object value) throws Exception{
        return filter(field, "=", value, "AND");
    }
    public Query<T> filter(String field, String operator, Object value) throws Exception {
        return filter(field, operator, value, "AND");
    }
    public Query<T> filter(String field, String operator, Object value, String clause) throws Exception {
        field = field.toLowerCase();
        clause = clause.toUpperCase();
        if(!clause.equals("AND")&&!clause.equals("OR")){
            throw new IllegalArgumentException("Illegal clause " + clause);
        }
        Field classField = null;
        field = field.trim();
        Field[] fields = this.modelClass.getDeclaredFields();
            // Check if the fieldName matches any field name in class
            for(int i=0;i<fields.length;i++){
                if(fields[i].isAnnotationPresent(Column.class)){
                    String fieldNameLower = fields[i].getName().toLowerCase();
                    if(fieldNameLower.equals(field)){
                        classField = fields[i];
                        break;
                    }
                }
            }
            if(classField==null){
                throw new IllegalArgumentException(field + " is not present in class "+ tableName);
            }
            classField.setAccessible(true);
            Class<?> fieldType = classField.getType();
            String where = "";
            switch (operator) {
                case "=":
                case "!=":
                case ">=":
                case "<=":
                case ">":
                case "<":
                    where = field + " " + operator;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid operator: " + operator);
            }
            if (classField.getType() == int.class) {
                if(value.getClass()==Integer.class){
                    Integer tmp  = (Integer) value;
                    where+= tmp.intValue();
                }
                else
                    where += classField.getInt((int) value);
            }
            else if (classField.getType() == String.class) {
                String val = (String) value;
                where += "'" + val + "'";
            }
            else if (classField.getType() == Float.class)
                where+= (Float)value;
            else if (classField.getType() == Boolean.class || classField.getType() == boolean.class)
                where+= (Boolean)value;
            else if (classField.getType() == Date.class)
                where+= "'"+(Date) value+"'";
            else if (classField.getType() == Time.class)
                where+= "'"+(Time) value+"'";
            else{
                where+= "'"+value.toString()+"'";
            }
            if (whereClause.isEmpty()) {
                whereClause = where+" "+clause+" ";
            } else {
                whereClause += where +" "+clause+" ";
            }
        return this;
    }
    public String getQuery(){
        String sql = selectClause + (whereClause.isEmpty() ? "" : " WHERE " + whereClause);
        String[] tokens = sql.split("\\s+");
        if (tokens[tokens.length - 1].equalsIgnoreCase("and") || tokens[tokens.length - 1].equalsIgnoreCase("or")) {
            sql = sql.substring(0, sql.lastIndexOf(tokens[tokens.length - 1]));
        }
        return sql;
    }

    public List<T> execute(){
        List<T> queryResult = new ArrayList<>();
        String query = this.getQuery();
        System.out.println(query);
        try{
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while(resultSet.next()){
                T obj = getObjFromRS(resultSet,modelClass,metaData,columnCount);
                queryResult.add(obj);
            }
        }catch (Exception e){
            System.out.println("Error in getting all objects. Message  = "+e.toString());
        }
        return queryResult;
    }
    private <T> T getObjFromRS(ResultSet resultSet, Class<T> cl, ResultSetMetaData metaData, int columnCount) {
        try {
            T object = createRandomObject(cl);
            Field[] fields = cl.getDeclaredFields();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object columnValue = resultSet.getObject(i);
                for(int j=0;j< fields.length;j++){
                    if(fields[j].getName().toLowerCase().equals(columnName) && fields[j].isAnnotationPresent(Column.class)){
                        fields[j].setAccessible(true);
                        if(fields[j].getType()==float.class || fields[j].getType()==Float.class) {
                            Double dv  = (Double)columnValue;
                            float  fv = dv.floatValue();
                            fields[j].set(object, fv);
                        }
                        else
                            fields[j].set(object, columnValue);
                        break;
                    }
                }
            }
            return object;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static <T> T createRandomObject(Class<T> clazz) throws Exception {
        // Get the list of constructors for the class
        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length == 0) {
            throw new Exception("Class has no public constructors");
        }
        // Select a random constructor from the list
        Constructor<?> constructor = constructors[0];
        // Get the parameter types for the constructor
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        // Create an array of random parameter values
        Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            args[i] = getRandomValue(parameterTypes[i]);
        }
        // Create a new instance of the class using the random arguments
        @SuppressWarnings("unchecked")
        T object = (T) constructor.newInstance(args);
        return object;
    }
    private static Object getRandomValue(Class<?> type) {
        if (type == String.class) {
            return "";
        } else if (type == int.class) {
            return 0;
        } else if (type == float.class || type == Float.class) {
            return 0f;
        } else if (type == boolean.class || type == Boolean.class) {
            return false;
        } else if (type == Date.class) {
            return new Date(622790105000L);
        } else if (type == Time.class) {
            return  new Time(System.currentTimeMillis());
        }
        else{
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }

}
