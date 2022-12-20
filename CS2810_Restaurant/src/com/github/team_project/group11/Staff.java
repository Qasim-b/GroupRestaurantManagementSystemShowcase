package com.github.team_project.group11;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class executes all staff GUI functionalities
 *
 * @author Team_11
 */
public class Staff {
	/**
	 * Removes tables that do not require a waiter.
	 * 
	 * @param tableNumber removed from tables that need a waiter
	 * @throws SQLException
	 */
	public static void removeCall(int tableNumber) throws SQLException {
		String composedLine = String.format("UPDATE tables set waiter_needed = false where table_no = %s;",
				tableNumber);
		Database.execute(Database.connect(false), composedLine);
	}

	/**
	 * Calls a waiter for a table.
	 *
	 * @param connection link to database
	 * @param waiter     to be called
	 * @return a list of all tables that require a waiter
	 * @throws SQLException
	 */
	public static ArrayList<String> getCalls(Connection connection, String waiter) throws SQLException {
		ArrayList<String> output = new ArrayList<String>();
		String composedLine = String.format("SELECT table_no from tables where waiter_needed = true and waiter = %s;",
				waiter);
		ResultSet rs = Database.executeQuery(connection, composedLine);
		while (rs.next()) {
			output.add(rs.getString(1));
		}
		return output;
	}

	/**
	 * Create order for customer.
	 *
	 * @param conn link to database
	 * @return an order of a customer
	 * @throws SQLException
	 */
	public static ArrayList<Order> getOrders(Connection conn) throws SQLException {
		ArrayList<Order> orders = new ArrayList<Order>();
		String query = "select * from orders;";
		ResultSet rs = Database.executeQuery(conn, query);
		while (rs.next()) {
			ArrayList<String> itemArray = getItemsArray(rs.getArray("items"));
			ArrayList<String> readyItems = getItemsArray(rs.getArray("ready_items"));
			float total = Float.parseFloat(rs.getString("total").substring(1));
			float amountPaid = Float.parseFloat(rs.getString("amount_paid").substring(1));
			orders.add(new Order(rs.getInt("id"), rs.getInt("user_id"), total, amountPaid, rs.getString("timestamp"),
					itemArray, rs.getInt("table_no"), rs.getBoolean("confirmed"), readyItems, rs.getBoolean("ready"),
					rs.getBoolean("completed")));
		}
		return orders;
	}

	/**
	 * Adds all items to the collection.
	 *
	 * @param array of items
	 * @return a collection of items
	 * @throws SQLException
	 */
	public static ArrayList<String> getItemsArray(Array array) throws SQLException {
		ArrayList<String> items = new ArrayList<String>();
		for (String item : (String[]) array.getArray()) {
			items.add(item);
		}
		return items;
	}
}
