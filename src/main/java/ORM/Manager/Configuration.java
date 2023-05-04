package ORM.Manager;
import java.io.*;
import java.sql.*;
import java.lang.*;
import java.util.*;




//Configuration configures the database connection based configPath xml, create SessionFactory and creates Table
public class Configuration {
    private String configPath;
    private SessionFactory sessionFactory;

    private Config configObj;

    private SessionFactory ssnf;

    private boolean reset = false;
    public Configuration(String configPath){
        this.configPath = configPath;
        config();
    }
    public Configuration(String configPath,boolean reset){
        this.configPath = configPath;
        this.reset = reset;
        config();
    }
    private void config(){
        //TODO: configures the database connection based configPath and xml create session factory
        try {
            FileInputStream fis=new FileInputStream(this.configPath);
            Properties p=new Properties ();
            p.load(fis);
            String url= (String)p.get("URL");
            String username= (String)p.get("User");
            String password= (String)p.get("Password");
            configObj = new Config(url,username,password); //create config OBJ
            fis.close();
        }catch (Exception e){
            System.out.println("Error in reading configuration file! ERROR: "+e.toString());
        }
    }



    public SessionFactory getFactory() throws Exception{
        if(ssnf==null)
            ssnf = new SessionFactory(configObj,this.reset);
        return ssnf;
    }
}
