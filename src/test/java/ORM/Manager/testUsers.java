package ORM.Manager;

import ORM.ClassTable.Column;
import ORM.ClassTable.PrimaryKey;
import ORM.ClassTable.Table;
import org.junit.jupiter.api.TestInstance;

import java.sql.Date;
import java.sql.Time;

@Table
public class testUsers {
    @Column
    @PrimaryKey
    int ID;

    @Column
    public
    String name;
    @Column
    Boolean isSpcl;
    @Column
    @PrimaryKey
    Date dob;
    @Column
    Time joining_time;
    @Column
    public
    Float current_balance;

    int x;
    public testUsers(int ID, String name, Boolean isSpcl, Date dob, Time joining_time, Float current_balance) {
        this.ID = ID;
        this.name = name;
        this.isSpcl = isSpcl;
        this.dob = dob;
        this.joining_time = joining_time;
        this.current_balance = current_balance;
    }
}
