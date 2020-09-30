package com.proquest.interview.util;

import com.proquest.interview.phonebook.Person;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is just a utility class, you should not have to change anything here
 * @author rconklin
 */
public class DatabaseUtil {

	private static List<Person> legacyPeople = new ArrayList<Person>();

	private static final Person PERSON1 = new LegacyPerson("Chris Johnson","(321) 231-7876", "452 Freeman Drive, Algonac, MI");
	private static final Person PERSON2 = new LegacyPerson("Dave Williams","(231) 502-1236", "285 Huron St, Port Austin, MI");

	public static void initDB() {
		try {
			Connection cn = getConnection();
			Statement stmt = cn.createStatement();
			stmt.execute("DROP TABLE PHONEBOOK IF EXISTS");
			stmt.execute("CREATE TABLE PHONEBOOK (NAME varchar(255), PHONENUMBER varchar(255), ADDRESS varchar(255))");
			PreparedStatement ps = cn.prepareStatement("INSERT INTO PHONEBOOK (NAME, PHONENUMBER, ADDRESS) VALUES(?,?,?)");

			ps.setString(1, PERSON1.getName());
			ps.setString(2, PERSON1.getPhoneNumber());
			ps.setString(3, PERSON1.getAddress());
			ps.executeUpdate();
			legacyPeople.add(PERSON1);
			ps.clearParameters();

			ps.setString(1, PERSON2.getName());
			ps.setString(2, PERSON2.getPhoneNumber());
			ps.setString(3, PERSON2.getAddress());
			ps.executeUpdate();
			legacyPeople.add(PERSON2);

			cn.commit();
			cn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName("org.hsqldb.jdbcDriver");
		return DriverManager.getConnection("jdbc:hsqldb:mem", "sa", "");
	}

	public static List<Person> getLegacyPeople() {
		return legacyPeople;
	}
}
