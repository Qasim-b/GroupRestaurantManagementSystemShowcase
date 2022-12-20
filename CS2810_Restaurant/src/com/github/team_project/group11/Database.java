package com.github.team_project.group11;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * This class executes the database functions for database to connection
 * purposes.
 * 
 * @author Adil Mushtaq Database class used for the project.
 *
 */
public class Database {

	private final static Dotenv dotenv = Dotenv.configure().load();
	private final static String localhost = dotenv.get("DB_HOST");
	private final static String onlinehost = "tyke.db.elephantsql.com";
	private final static String database = dotenv.get("DB_NAME");
	private final static String username = dotenv.get("DB_USERNAME");
	private final static String password = dotenv.get("DB_PASSWORD");
	private final static String onPassword = dotenv.get("ON_PASSWORD");

	/**
	 * This function attempts to connect to the database.
	 * 
	 * @param online - checks online database connection.
	 * @return Connection to database. Returns null object if this fails.
	 * @throws SQLException
	 */
	public static Connection connect(boolean online) {
		Connection connection = null;
		try {
			String protocol = "jdbc:postgresql://";
			String fullURL = "";
			if (online) {
				fullURL = protocol + onlinehost + "/uemtyvmg";
				connection = DriverManager.getConnection(fullURL, "uemtyvmg", onPassword);
			} else {
				fullURL = protocol + localhost + database;
				connection = DriverManager.getConnection(fullURL, username, password);
			}
		} catch (SQLException e) {
			connection = null;
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * Executes a given SQl query and returns the results.
	 *
	 * @param connection the given connection
	 * @param query      the given query
	 * @return the result set
	 * @throws SQLException
	 */
	public static ResultSet executeQuery(Connection connection, String query) throws SQLException {
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(query);
		return rs;
	}

	/**
	 * Executes a given SQL query.
	 * 
	 * @param connection connection to the database
	 * @param query      SQL query
	 * @throws SQLException if query is invalid
	 */
	public static void execute(Connection connection, String query) throws SQLException {
		Statement st = connection.createStatement();
		st.execute(query);
	}

	/**
	 * Adds an entry to a table in the database.
	 * 
	 * @param conn   connection to the database
	 * @param table  specific table inside the database
	 * @param cols   column to be modified
	 * @param values data to be inserted
	 * @throws SQLException if query is invalid
	 */
	public static void addEntry(Connection conn, String table, String[] cols, String[] values) throws SQLException {
		String composedLine = createString(table, cols, values);
		execute(conn, composedLine);
	}

	/**
	 * Allow insertion and splitting of arrays into a table.
	 * 
	 * @param table database table
	 * @param cols  columns in table to be updated
	 * @param vals  values to be inserted
	 * @return String query
	 */
	private final static String createString(String table, String[] cols, String[] vals) {
		String colString = String.join(", ", cols);
		String valString = String.join(", ", vals);
		String query = String.format("INSERT INTO %s (%s) VALUES (%s);", table, colString, valString);
		return query;
	}

	/**
	 * Checks if an element exists inside a table on the database.
	 * 
	 * @param conn  connection to the database
	 * @param cols  column to be checked
	 * @param table table to be checked
	 * @param value value to be checked
	 * @return "True" if value exists, "False" if not
	 * @throws SQLException
	 */
	public final static boolean checkExists(Connection conn, String[] cols, String table, String value)
			throws SQLException {
		String query = String.format("select %s from %s where %s = %s;", cols[0], table, cols[1], value);
		ResultSet rs = executeQuery(conn, query);
		if (rs.next()) {
			return true;
		} else {
			return false;
		}
	}
}
