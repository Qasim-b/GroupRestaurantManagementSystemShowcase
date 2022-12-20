package com.github.team_project.group11;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class that allows the user to place on order on the system and then uploads
 * the data onto the online database.
 * 
 * @author Qasim Baig
 */
public class Order {

	private boolean confirmed;
	private boolean completed;
	private String timestamp;
	private int orderId;
	private float total;
	private ArrayList<String> items;
	private ArrayList<String> readyItems;
	private ArrayList<String> food;
	private ArrayList<String> drinks;
	private boolean ready;
	private int tableNo;
	private int userId;
	private float amountPaid;

	/**
	 * Default constructor for the order.
	 */
	public Order() {
		this.completed = false;
		this.confirmed = false;
		this.ready = false;
		this.food = new ArrayList<String>();
		this.drinks = new ArrayList<String>();
		this.items = new ArrayList<String>();
		this.readyItems = new ArrayList<String>();
		this.amountPaid = 0;
		this.total = 0;
	}

	/**
	 * Constructor for orders to be used in the backend.
	 * 
	 * @param orderId    the order id
	 * @param userId     users id
	 * @param total      total cost of order
	 * @param amountPaid how much of the order has been paid for
	 * @param timestamp  when the order was placed
	 * @param items      items in the order
	 * @param tableNo    table number for the order
	 * @param confirmed  if a waiter has confirmed the order
	 * @param readyItems items that are ready
	 * @param ready      if whole order is ready
	 * @param completed  if the order has been completed >>>>>>> refs/heads/Order
	 */
	public Order(int orderId, int userId, float total, float amountPaid, String timestamp, ArrayList<String> items,
			int tableNo, boolean confirmed, ArrayList<String> readyItems, boolean ready, boolean completed) {
		this.orderId = orderId;
		this.userId = userId;
		this.total = total;
		this.amountPaid = amountPaid;
		this.timestamp = timestamp;
		this.items = items;
		this.tableNo = tableNo;
		this.completed = completed;
		this.confirmed = confirmed;
		this.ready = ready;
		this.readyItems = readyItems;
	}

	/**
	 * Constructor for customers to view their order on GUI.
	 * 
	 * @param orderId
	 * @param userId
	 * @param total
	 * @param timestamp
	 * @param items
	 * @param ready
	 */
	public Order(int orderId, int userId, float total, String timestamp, ArrayList<String> items, boolean ready,
			boolean completed) {
		this.orderId = orderId;
		this.userId = userId;
		this.total = total;
		this.timestamp = timestamp;
		this.items = items;
		this.completed = completed;
	}

	/**
	 * Setter for table number.
	 * 
	 * @param tableNo table number of order
	 */
	public void setTableNo(int tableNo) {
		this.tableNo = tableNo;
	}

	/**
	 * Calculate total cost of the customers items.
	 * 
	 * @param conn  - connection to database
	 * @param items - all food items in users order
	 * @return total - price of the users order
	 * @throws SQLException
	 */
	public float calcTotal(Connection conn, ArrayList<String> items) throws SQLException {
		float total = 0;
		for (String item : this.items) {
			total += Menu.getPrice(item, conn);
		}
		return total;
	}

	/**
	 * This function confirms the order.
	 * 
	 * 
	 * @param conn connection to the database
	 * @throws SQLException
	 */
	public void confirm(Connection conn) throws SQLException {
		float total = calcTotal(conn, this.items);
		String itemStr = toString(this.items);
		this.timestamp = getTime();
		upload(conn, this.userId, total, itemStr, this.timestamp, this.tableNo);
	}

	/**
	 * Inserts the users order into the online database, allows assigning an user id
	 * and timestamp to the order when utilised in the confirm function.
	 * 
	 * @param conn      connection to database
	 * @param total     total cost of order
	 * @param items     name of food items in user order
	 * @param timestamp time of order confirmation
	 * @throws SQLException
	 */
	public void upload(Connection conn, int userId, float total, String items, String timestamp, int tableNo)
			throws SQLException {
		String composedLine = String.format(
				"INSERT INTO orders(user_id, total, items, confirmed, timestamp, completed, table_no, ready, amount_paid) VALUES (%d, %s, '{%s}', true, '%s', false, %d, false, %f);",
				userId, total, items, timestamp, tableNo, total);
		Database.execute(conn, composedLine);
	}

	/**
	 * This function updates the order status to ready.
	 * 
	 * 
	 * @param conn connection to database
	 * @param id   id of item
	 * @throws SQLException if id doesnt exist
	 */
	public void ready(Connection conn, int id) throws SQLException {
		update(conn, id, "ready", true);
		this.ready = true;
	}

	/**
	 * Loops through all items and sets their state to ready.
	 * 
	 * @param conn - database connection
	 * @throws SQLException
	 */
	public void readyOrder(Connection conn) throws SQLException {
		for (String i : items) {
			if (!this.readyItems.contains(i)) {
				addReadyItem(i);
			}
		}
		updateReadyList(conn);
	}

	/**
	 * Checks if all items in the order are ready.
	 * 
	 * @return boolean depending on if the number of ready items is equal to number
	 *         of items in the order.
	 */
	public boolean orderIsReady() {
		return (this.items.size() == this.readyItems.size());
	}

	/**
	 * Set an order as completed and updates it in the database.
	 * 
	 * @param conn - database connection
	 * @throws SQLException
	 */
	public void complete(Connection conn) throws SQLException {
		this.completed = true;
		update(conn, this.orderId, "completed", true);
	}

	/**
	 * Uses id of an order to update it as completed in the database.
	 * 
	 * @param conn connection to database
	 * @param id   unique order id
	 * @param col  specifier for table value to be updated
	 * @param bool boolean to be inserted into the table
	 * @throws SQLException
	 */
	public void update(Connection conn, int id, String col, Boolean bool) throws SQLException {
		String composedLine = String.format("UPDATE orders SET %s = %b WHERE id = %d;", col, bool, id);
		Database.execute(conn, composedLine);
	}

	/**
	 * Update the readyItems list when called.
	 * 
	 * @param conn connection to database
	 * @throws SQLException
	 */
	public void updateReadyList(Connection conn) throws SQLException {
		String query = String.format("UPDATE orders SET ready_items = '{%s}' WHERE id = %s;", toString(this.readyItems),
				this.orderId);
		Database.execute(conn, query);
	}

	/**
	 * Method to separate drink and food items in the order.
	 * 
	 * @param items dishes and drinks
	 * @param conn  connection to the database
	 * @throws SQLException
	 */
	public void splitItems(Connection conn) throws SQLException {
		for (String item : this.items) {
			if (Menu.getType(item, conn) == "f") {
				this.food.add(item);
			} else if (Menu.getType(item, conn) == "d") {
				this.drinks.add(item);
			}
		}
	}

	/**
	 * Adds item to item arrayList.
	 * 
	 * @param item item to be added
	 */
	public void addItem(String item) {
		this.items.add(item);
	}

	/**
	 * Removes item from item arrayList.
	 * 
	 * @param item item to be removed
	 */
	public void removeItem(String item) {
		this.items.remove(item);
	}

	/**
	 * Clears all items from items Arraylist.
	 */
	public void clearItems() {
		this.items.clear();
	}

	/**
	 * Method to add item to the ready arrayList.
	 * 
	 * @param item item to be added
	 */
	public void addReadyItem(String item) {
		this.readyItems.add(item);
	}

	/**
	 * Method to remove item from the ready arrayList.
	 * 
	 * @param item item to be removed.
	 */
	public void removeReadyItem(String item) {
		this.readyItems.remove(item);
	}

	/**
	 * Checks that the order is associated to a table.
	 *
	 * @param order_id unique reference to the individual order that is placed
	 * @throws SQLException if order id not found
	 */
	public void changeTable(Connection conn) throws SQLException {
		String query = String.format("UPDATE Orders SET table_num = %d WHERE order_id = %d;", this.tableNo,
				this.orderId);
		Database.execute(conn, query);
	}

	/**
	 * Combines items in the order into a single string with each item separated by
	 * ",".
	 * 
	 * @param itemList contains items in the order
	 * @return items in the order as a single string
	 */
	public String toString(ArrayList<String> itemList) {
		String items = String.join(", ", itemList);
		return items;
	}

	/**
	 * Getter for current time of order when it is placed.
	 * 
	 * @return time when function is called in "yyyy/MM/dd HH:mm:ss" format
	 */
	public String getTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime timestamp = LocalDateTime.now();
		return formatter.format(timestamp);
	}

	/**
	 * Gets sent order confirmation status.
	 * 
	 * @return order confirmation status
	 */
	public boolean getConfirmed() {
		return this.confirmed;
	}

	/**
	 * Gets order completion status
	 * 
	 * @return boolean order completed status
	 */
	public boolean getCompleted() {
		return this.completed;
	}

	/**
	 * Getter for order id.
	 * 
	 * @return order id
	 */
	public int getOrderID() {
		return this.orderId;
	}

	/**
	 * Getter for table number.
	 * 
	 * @return table number
	 */
	public int getTable() {
		return this.tableNo;
	}

	/**
	 * Getter for food items in the order.
	 * 
	 * @return arrayList of food
	 */
	public ArrayList<String> getFood() {
		return this.food;
	}

	/**
	 * Getter for drinks in the order.
	 * 
	 * @return arrayList of drinks
	 */
	public ArrayList<String> getDrinks() {
		return this.drinks;
	}

	/**
	 * Getter for items.
	 * 
	 * @return arrayList of items
	 */
	public ArrayList<String> getItems() {
		return this.items;
	}

	/**
	 * Getter for ready items.
	 * 
	 * @return arrayList of ready items
	 */
	public ArrayList<String> getReadyItems() {
		return this.readyItems;
	}

	/**
	 * Getter for order ready status.
	 * 
	 * @return boolean ready status
	 */
	public boolean isReady() {
		return this.ready;
	}

	/**
	 * Getter for user id.
	 * 
	 * @return user id
	 */
	public int getUserId() {
		return this.userId;
	}

	/**
	 * Getter to return total price of order.
	 * 
	 * @return total prices
	 */
	public float getTotal() {
		return total;
	}

	/**
	 * Sets a user id to the order.
	 * 
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

//	public void refund(Connection conn) {
//
//	}

	/**
	 * Demo function that allows user to pay for their order.
	 * 
	 * @param cardNum    16 digit card number
	 * @param secCode    3 digit security code
	 * @param expiryDate date in mm/yy format
	 * 
	 * @return True if payment has been confirmed False if there is an issue with
	 *         card details
	 */
	public boolean makePayment(String cardNum, String secCode, Date expiryDate) {
		Payment payment = new Payment(this.total);
		if (payment.processPayment(cardNum, secCode, expiryDate)) {
			this.amountPaid = this.total;
			return true;
		}
		return false;
	}
}
