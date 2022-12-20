package com.github.team_project.group11;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class provides functions for connecting the front-end CustomerStartPage,
 * CustomerOrderPage and CustomerMainPage classes with back-end database
 * functionality and information for waiter calling and previous customer order
 * details.
 * 
 * @author Adil Mushtaq Class representing the customer view of the system.
 *
 */
public class Customer {

	/**
	 * This function notifies the waiter via the waiter GUI scene when the customer
	 * presses the callWaiter button on their customer order page.
	 * 
	 * @param tableNumber - An integer variable for table numbers allocated to
	 *                    customers in the CustomerOrderPage and CustomerMainPage
	 *                    java classes.
	 * @throws SQLException - For database connectivity
	 * 
	 */
	public static void callWaiter(int tableNumber) throws SQLException {
		String composedLine = String.format("UPDATE tables set waiter_needed = true where table_no = %d;", tableNumber);
		Database.execute(Database.connect(false), composedLine);
	}

	/**
	 * Fetches order for the customer to view.
	 * 
	 * @param conn connection to database
	 * @param id   customer id
	 * @return customer orders
	 * @throws SQLException if id doesnt exist
	 */
	public static ArrayList<Order> getOrders(Connection conn, int id) throws SQLException {
		ArrayList<Order> orders = new ArrayList<Order>();
		String query = String.format("select * from orders where user_id = %d;", id);
		ResultSet rs = Database.executeQuery(conn, query);
		while (rs.next()) {
			ArrayList<String> itemArray = Staff.getItemsArray(rs.getArray("items"));
			float total = Float.parseFloat(rs.getString("total").substring(1));
			orders.add(new Order(rs.getInt("id"), rs.getInt("user_id"), total, rs.getString("timestamp"), itemArray,
					rs.getBoolean("ready"), rs.getBoolean("completed")));
		}
		return orders;
	}
}