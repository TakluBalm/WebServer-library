package ORM.Manager;

import ORM.ClassTable.Column;
import ORM.ClassTable.Table;
import ORM.Database.DB;
import ORM.Database.DB_postgres;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Session class uses database apis to send queries.
public class Session {
    Connection con;
    DB db = new DB_postgres();



    Session(Connection con) throws SQLException {
        this.con = con;
        con.setAutoCommit(false);
    }
    protected void finalize() throws SQLException {
      con.rollback();
	  con.close();
    }

    public void commit() throws SQLException {
        con.commit();
    }
    public void rollback() throws SQLException {
        con.rollback();
    }
    public <T> boolean insert(T obj){
        //TODO: save the given obj in database.
        try {
            if(!obj.getClass().isAnnotationPresent(Table.class)){
                System.out.println("Object class "+obj.getClass().getSimpleName().toLowerCase() + " is not annotated with @Table.");
                return false;
            }
            if(doesExists(obj)){
                System.out.println("Object already exits in "+ obj.getClass().getSimpleName().toLowerCase());
                return false;
            }
            PreparedStatement p = con.prepareStatement(db.insert(obj));
            p.execute();
            return true;
        }
        catch (Exception e){
            System.out.println("Error in inserting the object. Error = " + e);
            return false;
        }
    }

    public <T> boolean delete(T obj){
        //TODO: delete the given obj in database.
        try {
            if(!obj.getClass().isAnnotationPresent(Table.class)){
                System.out.println("Object class "+obj.getClass().getSimpleName().toLowerCase() + " is not annotated with @Table.");
                return false;
            }

            PreparedStatement p = con.prepareStatement(db.delete(obj));
            p.execute();
            return true;
        }
        catch (Exception e) {
            System.out.println("Error in deleting object Message = "  + e.toString());
            return false;
        }

    }

    public <T> boolean update(T obj){
        //TODO: update the given obj in database.
        try {
            if(!obj.getClass().isAnnotationPresent(Table.class)){
                System.out.println("Object class "+obj.getClass().getSimpleName().toLowerCase() + " is not annotated with @Table.");
                return false;
            }
            db.update(obj);
            PreparedStatement p = con.prepareStatement(db.update(obj));
            p.execute();
            return true;
        }
        catch (Exception e) {
            System.out.println("Error in updating object Message = "  + e);
            return false;
        }
    }
    public<T> List<T> getAll(Class<T> cl){
        if(!cl.isAnnotationPresent(Table.class)){
            System.out.println("Object class "+cl.getSimpleName().toLowerCase() + " is not annotated with @Table.");
            return new ArrayList<>();
        }
        List<T> queryResult = new ArrayList<>();
        String query = db.getAll(cl);

        try{
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while(resultSet.next()){
                T obj = getObjFromRS(resultSet,cl,metaData,columnCount);
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
    public <T> boolean doesExists(T obj){
        if(!obj.getClass().isAnnotationPresent(Table.class)){
            System.out.println("Object class "+obj.getClass().getSimpleName().toLowerCase() + " is not annotated with @Table.");
            return false;
        }
        //TODO: update the given obj in database.
        try {
            Statement p = con.createStatement();
            String query=db.doesExist(obj);
            ResultSet rs=p.executeQuery(query);
            String val="";
            while(rs.next())
            {
                val=rs.getString(1);
            }
            if(val.equals("0")){return false;}
            return true;
        }
        catch (Exception e) {
            System.out.println("Error in updating object Message = "  + e);
            return false;
        }
    }

    public <T> Query<T> query(Class<T> cl){
        if(!cl.isAnnotationPresent(Table.class)){
            System.out.println("Object class "+cl.getSimpleName().toLowerCase() + " is not annotated with @Table.");
            return null;
        }
        return new Query(cl,this.con);
    }

}
