package ORM.Manager;


import ORM.ClassTable.Column;
import ORM.ClassTable.PrimaryKey;
import ORM.ClassTable.Table;

import java.sql.Date;
import java.sql.Time;

@Table
public class onlyCov {
    @Column
    @PrimaryKey
    int ID;

    @Column
    @PrimaryKey
    public
    String name;
    @Column
    @PrimaryKey
    Boolean isSpcl;
    @Column
    @PrimaryKey
    Date dob;
    @Column
    @PrimaryKey
    Time joining_time;
    @Column
    @PrimaryKey
    public
    Float current_balance;

    int x;
    public onlyCov(int ID, String name, Boolean isSpcl, Date dob, Time joining_time, Float current_balance) {
        this.ID = ID;
        this.name = name;
        this.isSpcl = isSpcl;
        this.dob = dob;
        this.joining_time = joining_time;
        this.current_balance = current_balance;
    }
}
