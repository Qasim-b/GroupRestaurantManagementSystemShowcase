package com.github.team_project.group11;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class executes login validation for the login scenes.
 *
 * @author Team_11
 *
 */
public class Login {
	/**
	 * This function checks whether the password is valid for access to the
	 * respective GUI scenes for the individual user according to their access
	 * rights.
	 * 
	 * @param conn     - database connectivity
	 * @param username - username
	 * @param password - password
	 * @return A boolean response to whether the password is valid for further
	 *         access into the system.
	 * @throws SQLException
	 */
	public final static boolean validatePassword(Connection conn, String username, String password)
			throws SQLException {
		String query = String.format(
				"select user_id from users where username = %s and password = crypt(%s, password);", username,
				password);
		ResultSet rs = Database.executeQuery(conn, query);
		if (rs.next()) {
			return true;
		} else {
			return false;
		}
	}
}
