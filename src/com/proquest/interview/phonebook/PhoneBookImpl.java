package com.proquest.interview.phonebook;

import com.proquest.interview.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PhoneBookImpl implements PhoneBook {

	private List<Person> people = new ArrayList<Person>();

	public PhoneBookImpl() {
		people.addAll(DatabaseUtil.getLegacyPeople());
	}

	@Override
	public void addPerson(Person newPerson) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DatabaseUtil.getConnection();
			ps = conn.prepareStatement("insert into phonebook (name, phonenumber, address) values (?, ?, ?)");
			ps.setString(1, newPerson.getName());
			ps.setString(2, newPerson.getPhoneNumber());
			ps.setString(3, newPerson.getAddress());
			ps.executeUpdate();
			conn.commit();

			people.add(newPerson);
		} catch (Throwable t) {
			rollbackTransaction(conn);
		} finally {
			closeDbResources(conn, ps);
		}
	}

	@Override
	public Person findPerson(String firstName, String lastName) {
		String name = assembleFullName(firstName, lastName);
		Person personToReturn = findPersonInCache(name);
		if (personToReturn == null) {
			personToReturn = findPersonInDbAndAddToCache(name);
		}

		return personToReturn;
	}

	@Override
	public List<Person> getPeople() {
		return people;
	}

	protected Person findPersonInDbAndAddToCache(String name) {
		Person personToReturn = null;

		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DatabaseUtil.getConnection();
			ps = conn.prepareStatement("select name, phonenumber, address from phonebook where name = ?");
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			for (int i = 0; rs.next(); i++) {
				if (i > 0) {
					System.err.println("WARNING: Multiple people found with name " + name + ", returning the first one found");
					break;
				}

				personToReturn = new Person(rs.getString("name"), rs.getString("phonenumber"), rs.getString("address"));
				people.add(personToReturn);
			}
			conn.commit();
		} catch (Throwable t) {
			rollbackTransaction(conn);
		} finally {
			closeDbResources(conn, ps);
		}

		return personToReturn;
	}

	protected Person findPersonInCache(String name) {
		Optional<Person> match = people.stream().filter(p -> p.getName().equals(name)).findFirst();
		return match.orElse(null);
	}

	protected String assembleFullName(String firstName, String lastName) {
		return firstName + " " + lastName;
	}

	private void closeDbResources(Connection conn, PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void rollbackTransaction(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		DatabaseUtil.initDB();  //You should not remove this line, it creates the in-memory database

		/* TODO: create person objects and put them in the PhoneBook and database
		 * John Smith, (248) 123-4567, 1234 Sand Hill Dr, Royal Oak, MI
		 * Cynthia Smith, (824) 128-8758, 875 Main St, Ann Arbor, MI
		*/
		Person johnSmith = new Person("John Smith", "(248) 123-4567", "1234 Sand Hill Dr, Royal Oak, MI");
		Person cynthiaSmith = new Person("Cynthia Smith", "(824) 128-8758", "875 Main St, Ann Arbor, MI");

		PhoneBook phoneBook = new PhoneBookImpl();
		phoneBook.addPerson(johnSmith);
		phoneBook.addPerson(cynthiaSmith);

		// TODO: print the phone book out to System.out
		List<Person> people = phoneBook.getPeople();
		for (Person person : people) {
			System.out.println("Name: " + person.getName());
			System.out.println("Phone Number: " + person.getPhoneNumber());
			System.out.println("Address: " + person.getAddress());
			System.out.println("--------");
		}

		// TODO: find Cynthia Smith and print out just her entry
		Person cynthiaFromDb = phoneBook.findPerson("Cynthia", "Smith");
		System.out.println("Name: " + cynthiaFromDb.getName());
		System.out.println("Phone Number: " + cynthiaFromDb.getPhoneNumber());
		System.out.println("Address: " + cynthiaFromDb.getAddress());

		// TODO: insert the new person objects into the database

	}
}
