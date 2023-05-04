package ORM.Manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QueryTest {

    Session ssn;
    @BeforeAll
    void setUp() throws Exception {
        Configuration config = new Configuration("src/test/java/ORM/Manager/configFile.txt", true);
        SessionFactory sF = config.getFactory();
        ssn = sF.getSession();
    }
    @Test
    void queryTestINT() throws Exception {
        testUsers given1 =new testUsers(10, "Gautam", false, new Date(622790105000L), new Time(System.currentTimeMillis()), 50.5f);
        testUsers given2 =new testUsers(11, "Gautam", false, new Date(622790105000L), new Time(System.currentTimeMillis()), 50.5f);
        ssn.insert(given1);
        ssn.insert(given2);
        List<testUsers> alloutput = ssn.getAll(testUsers.class);
        List<testUsers> output = ssn.query(testUsers.class).filter("ID",">",10,"OR").execute();
        assertEquals(alloutput.size()-output.size(),1);
        assertTrue((output.get(0).ID > 10));
        ssn.rollback();
    }
    @Test
    void queryTestBOOLandString() throws Exception {
        testUsers given1 =new testUsers(10, "Gautam", false, new Date(622790105000L), new Time(System.currentTimeMillis()), 50.5f);
        testUsers given2 =new testUsers(11, "Not-Gautam", true, new Date(622790105000L), new Time(System.currentTimeMillis()), 50.5f);
        testUsers given3 =new testUsers(12, "Not-Gautam", false, new Date(622790105000L), new Time(System.currentTimeMillis()), 50.5f);
        ssn.insert(given1);
        ssn.insert(given2);
        ssn.insert(given3);
        List<testUsers> alloutput = ssn.getAll(testUsers.class);
        List<testUsers> output = ssn.query(testUsers.class).filter("name","=","Gautam","OR").filter("isspcl",true).execute();
        assertEquals(1,alloutput.size()-output.size());
        for(int i=0;i<output.size();i++){
            assertTrue(output.get(i).name.equals("Gautam") || output.get(i).isSpcl);
        }
        ssn.rollback();
    }

    @Test
    void queryTestDATE() throws Exception {
        testUsers given1 =new testUsers(10, "Gautam", false, new Date(2002,1,2), new Time(System.currentTimeMillis()), 50.5f);
        testUsers given2 =new testUsers(11, "Not-Gautam", true, new Date(2002,1,2), new Time(System.currentTimeMillis()), 50.5f);
        testUsers given3 =new testUsers(12, "Not-Gautam", false, new Date(2001,1,1), new Time(System.currentTimeMillis()), 50.5f);
        ssn.insert(given1);
        ssn.insert(given2);
        ssn.insert(given3);
        List<testUsers> alloutput = ssn.getAll(testUsers.class);
        Date dob = new Date(2001,5,9);
        List<testUsers> output = ssn.query(testUsers.class).filter("dob",">",dob).filter("isspcl",true).execute();
        assertEquals(alloutput.size()-output.size(),2);
        ssn.rollback();
    }

    @Test
    void queryTestFloat() throws Exception {
        testUsers given1 =new testUsers(10, "Gautam", false, new Date(2002,1,2), new Time(System.currentTimeMillis()), 50.5f);
        testUsers given2 =new testUsers(11, "Not-Gautam", true, new Date(2002,1,2), new Time(System.currentTimeMillis()), 70.5f);
        testUsers given3 =new testUsers(12, "Not-Gautam", false, new Date(2001,1,1), new Time(System.currentTimeMillis()), 100.5f);
        ssn.insert(given1);
        ssn.insert(given2);
        ssn.insert(given3);
        List<testUsers> alloutput = ssn.getAll(testUsers.class);
        Date dob = new Date(2001,5,9);
        List<testUsers> output = ssn.query(testUsers.class).filter("current_balance",">",60.5f).filter("current_balance","<",100f).execute();
        assertEquals(alloutput.size()-output.size(),2);
        assertTrue(output.get(0).current_balance<100 && output.get(0).current_balance>60);
        ssn.rollback();
    }

    @Test
    void queryTestTime() throws Exception {
        testUsers given1 =new testUsers(10, "Gautam", false, new Date(2002,1,2),Time.valueOf("08:30:00"), 50.5f);
        testUsers given2 =new testUsers(11, "Not-Gautam", true, new Date(2002,1,2),Time.valueOf("09:30:00"), 70.5f);
        testUsers given3 =new testUsers(12, "Not-Gautam", false, new Date(2001,1,1),Time.valueOf("10:30:00"), 100.5f);
        ssn.insert(given1);
        ssn.insert(given2);
        ssn.insert(given3);
        List<testUsers> alloutput = ssn.getAll(testUsers.class);
        Time tim = Time.valueOf("10:00:00");
        List<testUsers> output = ssn.query(testUsers.class).filter("joining_time","<=",tim).filter("joining_time","<",tim).execute();
        assertEquals(alloutput.size()-output.size(),1);
        ssn.rollback();
    }

}