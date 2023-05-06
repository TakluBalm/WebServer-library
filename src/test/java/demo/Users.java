package demo;

import ORM.ClassTable.Column;
import ORM.ClassTable.PrimaryKey;
import ORM.ClassTable.Table;

@Table
public class Users {

	@Column
	@PrimaryKey
	String username;

	@Column
	String password;

}
