package com.github.team_project.group11;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class executes the registration functionality of the registration
 * scenes.
 * 
 * @author Adil Mushtaq Register new users to system.
 * 
 * @author Team_11
 *
 */
public class Register {
	/**
	 * Creates a new user
	 * 
	 */
	private final static String[] users = { "username", "email", "password", "type" };

	public final static void createUser(Connection conn, String[] values) throws SQLException {
		Database.addEntry(conn, "users", users, values);
	}
}
