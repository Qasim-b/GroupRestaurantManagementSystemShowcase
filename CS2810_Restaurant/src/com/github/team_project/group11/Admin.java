package com.github.team_project.group11;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Functions for the admin controller
 * 
 * @author Rory Thomson-Bird
 */
public class Admin {

	private static Connection conn = Database.connect(false);

	/**
	 * Get all users
	 * 
	 * @return users
	 * @throws SQLException
	 */
	public static ArrayList<String> getUsers() throws SQLException {
		ArrayList<String> users = new ArrayList<String>();
		ResultSet rs = Database.executeQuery(conn, "SELECT * FROM users");
		while (rs.next()) {
			users.add(rs.getString("user_id") + "\t" + rs.getString("username") + "\t" + rs.getString("email") + "\t"
					+ rs.getString("type"));
		}
		return users;
	}

	/**
	 * Get specific users
	 * 
	 * @param condition
	 * @return users
	 * @throws SQLException
	 */
	public static ArrayList<String> getUsers(String condition) throws SQLException {
		ArrayList<String> users = new ArrayList<String>();
		ResultSet rs = Database.executeQuery(conn, "SELECT * FROM users WHERE type = " + condition);
		while (rs.next()) {
			users.add(rs.getString("user_id") + "\t" + rs.getString("username") + "\t" + rs.getString("email") + "\t"
					+ rs.getString("type"));
		}
		return users;
	}

	/**
	 * Removes a user from the database
	 * 
	 * @param id
	 * @throws SQLException
	 */
	public static void removeUser(String id) throws SQLException {
		Statement st = conn.createStatement();
		st.execute("UPDATE tables SET waiter = 1 WHERE waiter = " + id);
		st.execute("UPDATE orders SET user_id = 1 WHERE user_id = " + id);
		st.execute("DELETE FROM users WHERE user_id = " + id);
	}

	/**
	 * Adds a user to the database
	 * 
	 * @param details
	 * @throws SQLException
	 */
	public static void addUser(String[] details) throws SQLException {
		Statement st = conn.createStatement();
		st.execute("INSERT INTO users (username, email, password, type) VALUES( '" + details[0] + "', '" + details[1]
				+ "', crypt('" + details[2] + "', gen_salt('bf')), '" + details[3] + "')");
	}

	/**
	 * Get the tables for given waiter
	 * 
	 * @param waiter
	 * @return tables
	 * @throws SQLException
	 */
	public static String getTables(String waiter) throws SQLException {
		String tables = new String();
		boolean tableFound = false;
		ResultSet rs = Database.executeQuery(conn, "SELECT * FROM tables WHERE waiter = " + waiter);
		while (rs.next()) {
			tableFound = true;
			tables = tables + ", " + rs.getString(1);
		}
		if (tableFound == true) {
			tables = tables.substring(2);
		}
		return tables;
	}

	/**
	 * Assign waiter to a table
	 * 
	 * @param waiter The selected waiter
	 * @param table
	 * @throws SQLException
	 */
	public static void assignTable(String waiter, String table) throws SQLException {
		Statement st = conn.createStatement();
		st.execute("UPDATE tables SET waiter = " + waiter + " WHERE table_no = " + table);
	}
}
