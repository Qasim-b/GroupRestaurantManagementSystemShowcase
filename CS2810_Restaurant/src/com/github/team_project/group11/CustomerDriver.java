package com.github.team_project.group11;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Consumer;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * This class is executed to start the Customer GUI. It will first load the Customer start page, and
 * through that page other parts of the Customer GUI can be accessed.
 * 
 * @author Luna Cheung
 */
public class CustomerDriver { 
                                                 
  public static Stage currentStage;
  public static Scene currentScene;
  public static Order currentOrder;
  public static HashMap<String, String> currentMenu;
  public static Connection connection;
  public static int userID;

  /**
   * This function starts the customer GUI.
   * 
   * @param primaryStage The current stage, passed from the login controller
   * @param ID The user login ID
   * @throws SQLException 
   */
  public static void start(Stage primaryStage, int ID) throws SQLException {
    currentStage = primaryStage;
    userID = ID;
    currentOrder = null;
    connection = Database.connect(false);
    currentMenu = Menu.getData(CustomerDriver.getConnection());
  }

  /**
   * This changes the current scene and displays the new scene to the user.
   * 
   * @param root
   * @param css - The CSS file to be used for the new scene.
   */
  public static void changeScene(Parent root, String css) {
    currentScene = new Scene(root, 600, 400);
    currentScene.getStylesheets().add(css);
    currentStage.setScene(currentScene);
    currentStage.show();
  }

  /**
   * This gets the current stage.
   * 
   * @return The current stage
   */
  public static Stage getCurrentStage() {
    return currentStage;
  }

  /**
   * This returns the current order being made by the Customer.
   * 
   * @return The current order
   */
  public static Order getCurrentOrder() {
    return currentOrder;
  }

  /**
   * This sets the current order being made by the Customer.
   * 
   * @param order - The new order.
   */
  public static void setCurrentOrder(Order order) {
    currentOrder = order;
  }

  /**
   * This returns the current menu loaded from the database.
   * 
   * @return The most updated menu
   */
  public static HashMap<String, String> getCurrentMenu() {
    return currentMenu;
  }

  /**
   * This sets the current menu.
   * 
   * @param menu - The new menu.
   */
  public static void setCurrentMenu(HashMap<String, String> menu) {
    currentMenu = menu;
  }

  /**
   * This refreshes the order, and formats it in a way to be displayed.
   * 
   * @return An ArrayList containing the order formatted.
   */
  public static ArrayList<String> formatOrder(Order order) {
    HashSet<String> itemsNotDisplayed = new HashSet<String>(order.getItems());
    ArrayList<String> itemsToAdd = new ArrayList<String>();

    for (String item : itemsNotDisplayed) {
      int counter = Collections.frequency(order.getItems(), item);
      String itemName = currentMenu.get(item);
      itemsToAdd.add(Integer.toString(counter) + " x " + itemName);
    }
    return itemsToAdd;
  }
  
  /**
   * This function refreshes the order so that it shows the latest possible version from the database.
   * 
   * @param orderPane
   * @param totalPane
   */
  public static void refreshOrder(TextFlow orderPane, TextFlow totalPane) {
    HashSet<String> itemsNotDisplayed = new HashSet<String>(currentOrder.getItems());

    orderPane.getChildren().clear();
    totalPane.getChildren().clear();

    for (String item : itemsNotDisplayed) {
      int counter = Collections.frequency(currentOrder.getItems(), item);
      orderPane.getChildren()
          .add(new Text(Integer.toString(counter) + " x " + currentMenu.get(item) + "\n"));
    }

    try {
      totalPane.getChildren()
          .add(new Text("Total: £" + currentOrder.calcTotal(connection, currentOrder.getItems())));
    } catch (Exception e) {
      System.err.println(e);
      totalPane.getChildren().add(new Text("Cannot calculate total"));
    }
  }

  /**
   * This gets the user logged in.
   * 
   * @return The ID of the current user
   */
  public static int getUserID() {
    return userID;
  }

  /**
   * This gets the current scene.
   * 
   * @return The current scene
   */
  public static Scene getCurrentScene() {
    return currentScene;
  }

  /**
   * This function calls the waiter to a table.
   * 
   * @param tableNum - An integer variable representing the table number to call a waiter to.
   */
  public static void callWaiter(int tableNum) {
    try {
      Customer.callWaiter(tableNum);
      Alert message = new Alert(AlertType.NONE,
          "The waiter will be to table " + tableNum + " shortly", ButtonType.APPLY);
      message.show();
    } catch (Exception e) {
      Alert message = new Alert(AlertType.NONE,
          "Sorry the waiter could not be called. Please try again.", ButtonType.APPLY);
      message.show();
    }
  }

  /**
   * Makes the table buttons.
   * 
   * @param tableButton - The MenuButton where the MenuItems representing the buttons should go.
   * @param setTableFunc - The function to be ran when the MenuItem is selected.
   */
  public static void makeTableButtons(MenuButton tableButton, Consumer<Integer> setTableFunc) {
    tableButton.getItems().clear();
    for (int i = 1; i < 10; i++) {
      int tableNum = i;
      MenuItem table = new MenuItem(Integer.toString(tableNum));
      table.setOnAction(event -> setTableFunc.accept(tableNum));
      tableButton.getItems().add(table);
    }
  }

  /**
   * Gets the current connection to the database.
   * 
   * @return The connection to the database
   */
  public static Connection getConnection() {
    return connection;
  }
}
