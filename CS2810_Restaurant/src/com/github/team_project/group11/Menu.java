package com.github.team_project.group11;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Abstracted view of restaurant view.
 * 
 * @author Qasim Baig
 */
public class Menu {

	/**
	 * Fetches all the names and prices from the menu to be stored in HashMap.
	 * 
	 * @param connection connection to the database.
	 * @return a HashMap consisting of name, price pairs.
	 * @throws SQLException
	 */
	public static HashMap<String, String> getData(Connection connection) throws SQLException {
		String query = "SELECT * FROM menu";
		ResultSet rs = Database.executeQuery(connection, query);
		HashMap<String, String> dishes = new HashMap<String, String>();
		while (rs.next()) {
			dishes.put(rs.getString(1), rs.getString(2));
		}
		return dishes;
	}

	/**
	 * Delete an item and corresponding info from the menu.
	 * 
	 * @param connection   - database connection
	 * @param identifierId - used to select the item to be deleted
	 * @throws SQLException
	 */
	public static void deleteRow(Connection connection, String identifierId) throws SQLException {
		String composedLine = String.format("DELETE FROM menu WHERE id = %s;", identifierId);
		Database.execute(connection, composedLine);
	}

	/**
	 * This allows the user to edit specific values in the menu.
	 * 
	 * @param connection database connection
	 * @param column     specifies which column to modify the value of
	 * @param value      value that will replace the existing one
	 * @param idvalue    specifies the row to be modified
	 * @throws SQLException
	 */
	public static void editEntry(Connection connection, String column, String value, String idvalue)
			throws SQLException {
		String composedLine = String.format("UPDATE menu " + "SET %s" + " = '%s' WHERE id = %s;", column, value,
				idvalue);
		Database.execute(connection, composedLine);
	}

	/**
	 * Adds new entry to the menu database.
	 * 
	 * @param connection
	 * @param values     comma separated string to be inserted into menu via sql.
	 * @throws SQLException
	 */
	public static void addEntry(Connection connection, String values) throws SQLException {
		String composedLine = String
				.format("INSERT INTO menu (name, price, ingredients, vegetarian, vegan, type, calories) VALUES ("
						+ values + ");");
		Database.execute(connection, composedLine);
	}

	/**
	 * Allows the user to search and be shown multiple similar item names.
	 * 
	 * @param search     the users input string
	 * @param connection connection to the database
	 * @return ArrayList containing all matching items related to the search
	 * @throws SQLException
	 */
	public static ArrayList<String> searchResults(String search, Connection connection) throws SQLException {
		ArrayList<String> searchResult = new ArrayList<String>();
		String composedLine = String.format("SELECT name from menu WHERE name LIKE '%s';", search);
		ResultSet rs = Database.executeQuery(connection, composedLine);
		while (rs.next()) {
			searchResult.add(rs.getString(1));
		}
		return searchResult;
	}

	/**
	 * Specific search for items in the menu database by using their unique ID
	 * value.
	 * 
	 * @param connection connection to database
	 * @param id         unique identifier for item in menu database
	 * @return name of item as a string
	 * @throws SQLException
	 */
	public static String getItemNameById(Connection connection, String id) throws SQLException {
		String composedLine = String.format("SELECT name FROM public.menu WHERE id = '%s';", id);
		ResultSet rs = Database.executeQuery(connection, composedLine);
		if (rs.next()) {
			return rs.getString(1);
		}
		return null;
	}

	/**
	 * Creates a new menu for allergen customers.
	 *
	 * 
	 * @param allergies  checked against ingredient of dish
	 * @param connection link to the database
	 * @return an updated menu for allergen customers
	 * @throws SQLException
	 */
	public static HashMap<String, String> allergenMenu(ArrayList<String> allergies, Connection connection)
			throws SQLException {
		HashMap<String, String> aMenu = new HashMap<String, String>();
		String exp = "SELECT * FROM MENU WHERE NOT";
		for (String allerg : allergies) {
			exp = exp + "('" + allerg + "' = ANY (ingredients)) AND NOT";
		}
		exp = exp.substring(0, exp.length() - 8) + ";";
		ResultSet rs = Database.executeQuery(connection, exp);
		while (rs.next()) {
			aMenu.put(rs.getString(1), rs.getString(2));
		}
		return aMenu;
	}

	/**
	 * Gets the ingredients of a dish.
	 *
	 * @param dish       reference to a dish's ingredients
	 * @param connection link to the database
	 * @return all ingredients of a dish
	 * @throws SQLException
	 */
	public static ArrayList<String> dishIngredients(String dish, Connection connection) throws SQLException {
		ArrayList<String> menuIng = new ArrayList<String>();
		String query = "SELECT ingredients FROM menu WHERE name ='" + dish.toLowerCase() + "'";
		ResultSet rs = Database.executeQuery(connection, query);
		while (rs.next()) {
			menuIng.add(rs.getString(1));
		}
		return menuIng;
	}

	/**
	 * Gets calories of a specific item requested.
	 * 
	 * @param conn connection to the database
	 * @param id   id of the item
	 * @return calorie amount
	 * @throws SQLException if id does not exist
	 */
	public static String getCalories(Connection conn, String id) throws SQLException {
		String query = String.format("Select calories from menu where id = %s", id);
		ResultSet rs = Database.executeQuery(conn, query);
		rs.next();
		return rs.getString("calories");
	}

	/**
	 * Gets price of an item from its id.
	 * 
	 * @param id         id of requested item
	 * @param connection connection to database
	 * @return price of item
	 * @throws SQLException if item does not exist
	 */
	public static float getPrice(String id, Connection connection) throws SQLException {
		String composedLine = String.format("SELECT price from menu WHERE id = %s;", id);
		ResultSet rs = Database.executeQuery(connection, composedLine);
		while (rs.next()) {
			return Float.parseFloat(rs.getString(1).substring(1));
		}
		return 0;
	}

	/**
	 * Gets the type of the item (dish or drink) chosen.
	 * 
	 * @param item       item to find type of
	 * @param connection connection to database
	 * @return item type
	 * @throws SQLException if item does not exist
	 */
	public static String getType(String item, Connection connection) throws SQLException {
		String composedLine = String.format("Select type from menu where name = '%s';", item);
		ResultSet rs = Database.executeQuery(connection, composedLine);
		while (rs.next()) {
			return rs.getString(1);
		}
		return null;
	}

}
