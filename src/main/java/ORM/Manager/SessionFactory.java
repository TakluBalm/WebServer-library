package ORM.Manager;

import ORM.ClassTable.PrimaryKey;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import ORM.ClassTable.Column;

public class SessionFactory {

        Config config;
        private String packageName;
        boolean reset = false;

        SessionFactory(Config configObj) throws  Exception{
            this.config = configObj;
            createTable();
        }
        SessionFactory(Config configObj,boolean reset) throws  Exception{
            this.config = configObj;
            if(reset==true){
                reset();
            }
            createTable();
        }

        private static List<Class<?>> findAnnotatedClasses()
                throws Exception {
            Set<Class<?>> set = new HashSet<>();
            ScanResult sr = new ClassGraph().enableAllInfo().scan();
            sr.getAllClasses().forEach(classInfo -> {
                AnnotationInfo u = classInfo.getAnnotationInfo("ORM.ClassTable.Table");
                if(u != null){
                    Class<?> annotatedClass = classInfo.loadClass();
                    set.add(annotatedClass);
                }
            });
            List<Class<?>> annotatedClasses = new ArrayList<>(set);
            return annotatedClasses;
        }

        public <T> void createClassTable(Class<T> cl,Connection con) throws SQLException {
            String tableName = cl.getSimpleName().toLowerCase();
            System.out.println(tableName);
            StringBuilder createTableSql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + "(");
            Field[] fields = cl.getDeclaredFields();
            int len = 0;
            int primLen  =0;
            for(int i=0;i<fields.length;i++){
                if(fields[i].isAnnotationPresent(Column.class)) {
                    len = len + 1;
                    if(fields[i].isAnnotationPresent(PrimaryKey.class))
                        primLen = primLen+1;
                }
            }
            String pk = "PRIMARY KEY (";
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                String fieldName = field.getName().toLowerCase();
                String fieldType = "";
                if(field.isAnnotationPresent(Column.class)) {
                    if (field.getType() == int.class)
                        fieldType = "INT";
                    else if (field.getType() == String.class)
                        fieldType = "VARCHAR";
                    else if (field.getType() == Float.class || field.getType() == float.class)
                        fieldType = "FLOAT";
                    else if (field.getType() == Boolean.class || field.getType() == boolean.class)
                        fieldType = "BOOLEAN";
                    else if (field.getType() == Date.class)
                        fieldType = "date";
                    else if (field.getType() == Time.class)
                        fieldType = "TIME";
                    else {
                        throw new SQLException("TYPE "+field.getType()+" in "+tableName+" is not accepted. Correct it!");
                    }
                    createTableSql.append(fieldName).append(" ").append(fieldType);
                    createTableSql.append(", ");
                }
            }
            int count=0;
            for(int i=0;i< fields.length;i++){
                if(fields[i].isAnnotationPresent(Column.class) && fields[i].isAnnotationPresent(PrimaryKey.class)){
                    pk = pk  + fields[i].getName().toLowerCase();
                    if(count< primLen-1){
                        pk+= ",";
                    }
                    count = count+1;
                }
            }
            pk+= ")";
            if(primLen!=0) {
                createTableSql.append(pk+")");
                System.out.println(createTableSql);
                Statement stmt = con.createStatement();
                stmt.executeUpdate(createTableSql.toString());
            }else{
                throw new SQLException("There should be atleast one primary key in the table " + tableName);
            }
        }

        public void createTable() throws Exception{
            Connection con;
            //create table
            try {
                con = DriverManager.getConnection(config.url, config.username, config.password);
                System.out.println("Connection established");
                List<Class<?>> tableClass =findAnnotatedClasses();
                for(Class<?> c: tableClass){
                    createClassTable(c,con);
                }
            }catch (Exception e){
                System.out.println(("Error connecting to database. Message = " + e.toString()));
            }

        }
        public Session getSession() throws Exception{
            //new session with a new connection variable.
            Connection con = DriverManager.getConnection(config.url, config.username, config.password);
            Session s = new Session(con);
            return s;
        }
        public void reset() throws Exception {
            //delete all exists tables with same name.
            List<Class<?>> all=findAnnotatedClasses();
            Connection conn;
            try {
                conn = DriverManager.getConnection(config.url, config.username, config.password);
                for(Class<?> clazz : findAnnotatedClasses())
                {
                    String tableName = clazz.getSimpleName().toLowerCase();
                    StringBuilder delete_sql=new StringBuilder("DROP TABLE IF EXISTS "+tableName);
                    Statement stmt = conn.createStatement();
                    System.out.println(delete_sql);
                    stmt.executeUpdate(delete_sql.toString());
                }
            }catch (Exception e){
                System.out.println(("Error connecting to database. Message = " + e.toString()));
            }
        }
}
