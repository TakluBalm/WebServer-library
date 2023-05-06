package ORM.Manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConfigurationTest {
    Configuration config;
    SessionFactory sF;
    @BeforeEach
    void setup() throws Exception {
        config = new Configuration("src/test/java/ORM/Manager/configFile.txt", true);
        sF = config.getFactory();
    }
    @Test
    void checkInsert() throws Exception {
        testUsers given =new testUsers(10, "Gautam", false, new Date(622790105000L), new Time(System.currentTimeMillis()), 50.5f);
        Session ssn = sF.getSession();
        ssn.insert(given);
        testUsers indb =  ssn.getAll(testUsers.class).get(0);
        assertEquals(given.ID,indb.ID);
        assertEquals(given.name,indb.name);
        assertEquals(given.isSpcl,indb.isSpcl);
        assertEquals(given.dob.toString(),indb.dob.toString());
        assertEquals(given.joining_time.toString(),indb.joining_time.toString());
        assertEquals(given.current_balance,indb.current_balance);
    }
    @Test
    void checkInsertNonDefinedObject() throws Exception {
        testUsers given =new testUsers(10, "Gautam", false, new Date(622790105000L), new Time(System.currentTimeMillis()), 50.5f);
        Session ssn = sF.getSession();
        ssn.insert(given);
        testUsers indb =  ssn.getAll(testUsers.class).get(0);
        assertEquals(given.ID,indb.ID);
        assertEquals(given.name,indb.name);
        assertEquals(given.isSpcl,indb.isSpcl);
        assertEquals(given.dob.toString(),indb.dob.toString());
        assertEquals(given.joining_time.toString(),indb.joining_time.toString());
        assertEquals(given.current_balance,indb.current_balance);
    }
    @Test
    void checkInsertNonTable() throws Exception {
        int x = 10;
        Session ssn = sF.getSession();
        assertFalse(ssn.insert(x));
    }

    @Test
    void checkDeleteSingle() throws Exception{
        testUsers given =new testUsers(10, "Gautam", false, new Date(622790105000L), new Time(System.currentTimeMillis()), 50.5f);
        Session ssn = sF.getSession();
        ssn.insert(given);
        List<testUsers> before =  ssn.getAll(testUsers.class);
        ssn.delete(given);
        List<testUsers> after =  ssn.getAll(testUsers.class);
        assertEquals(after.size(),0);
        assertEquals(before.size(),1);
    }
    @Test
    void checkUpdatetNonTable() throws Exception {
        int x = 10;
        Session ssn = sF.getSession();
        assertFalse(ssn.update(x));
    }
    @Test
    void checkDeletetNonTable() throws Exception {
        int x = 10;
        Session ssn = sF.getSession();
        assertFalse(ssn.delete(x));
    }
    @Test
    void getAllNontable() throws Exception {
        List<Integer> x =new ArrayList<>();
        Session ssn = sF.getSession();
        assertTrue(ssn.getAll(x.getClass()).size()==0);
		ssn.rollback();

    }
    @Test
    void checkUpdate() throws Exception {
        testUsers u =new testUsers(10, "Gautam", false, new Date(622790105000L), new Time(System.currentTimeMillis()), 50.5f);
        Session ssn = sF.getSession();
        ssn.insert(u);
        u.current_balance = 90f;
        u.name = "Non-Gautam";
        ssn.update(u);
        testUsers indb =  ssn.getAll(testUsers.class).get(0);
        assertEquals(u.current_balance,indb.current_balance);
        assertTrue(u.name.equals(indb.name));
    }
    @Test
    void checkDelete() throws Exception {
        testUsers u1 =new testUsers(10, "Gautam", false, new Date(622790105000L), new Time(System.currentTimeMillis()), 50.5f);
        testUsers u2 =new testUsers(11, "Non-Gautam", false, new Date(622790105000L), new Time(System.currentTimeMillis()), 50.5f);
        Session ssn = sF.getSession();
        ssn.insert(u1);
        ssn.insert(u2);
        ssn.delete(u1);
        List<testUsers> indb =  ssn.getAll(testUsers.class);
        assertEquals(indb.size(),1);
        System.out.println(indb.get(0).name);
        assertTrue(indb.get(0).name.equals("Non-Gautam"));

    }
    @Test
    void insertNULL() throws Exception {
        testUsers given =new testUsers(10, "Gautam", false, new Date(622790105000L),null, 50.5f);
        Session ssn = sF.getSession();
        ssn.insert(given);
        testUsers indb =  ssn.getAll(testUsers.class).get(0);
        assertEquals(given.ID,indb.ID);
        assertEquals(given.name,indb.name);
        assertEquals(given.isSpcl,indb.isSpcl);
        assertEquals(given.dob.toString(),indb.dob.toString());
        assertNull(indb.joining_time);
        assertEquals(given.current_balance,indb.current_balance);
    }
    @Test
    void onlyforcov() throws Exception {
        onlyCov given =new onlyCov(10, "Gautam", false, new Date(622790105000L), new Time(System.currentTimeMillis()), 50.5f);
        Session ssn = sF.getSession();
        ssn.insert(given);
        ssn.update(given);
        ssn.delete(given);
        List<onlyCov> indb =  ssn.getAll(onlyCov.class);
        assertEquals(indb.size(),0);
    }
}