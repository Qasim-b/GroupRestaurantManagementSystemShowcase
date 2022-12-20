package com.github.team_project.group11;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

/**
 * This class is the main ordering page provided to the Customer. It utilises the functions in Menu
 * and Order, and connects these with a graphical interface so the user can use them.
 * 
 * @author Luna Cheung
 */
public class CustomerMainPage {

  private boolean isAllergyMenuActive;
  private final ArrayList<String> allergies = new ArrayList<String>(
      Arrays.asList("Clear Filters", "seafood", "nuts", "celery", "gluten", "crustaceans", "eggs",
          "lupin", "milk", "molluscs", "mustard", "peanuts", "sesame oil", "soya", "sulphites"));
  private ArrayList<String> selectedAllergies;

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private AnchorPane dishPlane;

  @FXML
  private VBox dishButtonPane;

  @FXML
  private MenuButton filterDishesButton;

  @FXML
  private AnchorPane drinkPlane;

  @FXML
  private VBox drinkButtonPane;

  @FXML
  private AnchorPane orderPlane;

  @FXML
  private Button clearOrderButton;

  @FXML
  private Button editOrderButton;

  @FXML
  private Button makeOrderButton;

  @FXML
  private TextFlow currentOrderPane;

  @FXML
  private TextFlow currentTotal;

  @FXML
  private MenuButton selectTableButton;

  @FXML
  private Button waiterButton;

  /**
   * Receives an event regarding the edit order button and switches the scene to the edit order
   * screen.
   * 
   * @param event
   * @throws IOException
   */
  @FXML
  void editOrder(ActionEvent event) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/CustomerEditOrder.fxml"));
    String css = this.getClass().getResource("/customer_menu.css").toExternalForm();
    CustomerDriver.changeScene(root, css);
  }

  /**
   * Receives an event regarding the make order button and switches the scene to the payment screen.
   * 
   * @param event
   * @throws IOException
   */
  @FXML
  void makeOrder(ActionEvent event) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/PaymentScreen.fxml"));
    String css = this.getClass().getResource("/customer_menu.css").toExternalForm();
    CustomerDriver.changeScene(root, css);
  }

  /**
   * Called when the call waiter button is pressed. It calls the waiter to the set table.
   * 
   * @param event
   */
  @FXML
  void callWaiter(ActionEvent event) {
    CustomerDriver.callWaiter(CustomerDriver.getCurrentOrder().getTable());
  }
  
  /**
   * This function initialises the customer main page functionality.
   * 
   * @throws SQLException
   */
  @FXML
  void initialize() throws SQLException {
    assert dishPlane != null : "fx:id=\"dishPlane\" was not injected: check your FXML file 'CustomerMainPage.fxml'.";
    assert dishButtonPane != null : "fx:id=\"dishButtonPane\" was not injected: check your FXML file 'CustomerMainPage.fxml'.";
    assert filterDishesButton != null : "fx:id=\"filterDishesButton\" was not injected: check your FXML file 'CustomerMainPage.fxml'.";
    assert drinkPlane != null : "fx:id=\"drinkPlane\" was not injected: check your FXML file 'CustomerMainPage.fxml'.";
    assert drinkButtonPane != null : "fx:id=\"drinkButtonPane\" was not injected: check your FXML file 'CustomerMainPage.fxml'.";
    assert orderPlane != null : "fx:id=\"orderPlane\" was not injected: check your FXML file 'CustomerMainPage.fxml'.";
    assert editOrderButton != null : "fx:id=\"editOrderButton\" was not injected: check your FXML file 'CustomerMainPage.fxml'.";
    assert makeOrderButton != null : "fx:id=\"makeOrderButton\" was not injected: check your FXML file 'CustomerMainPage.fxml'.";
    assert currentOrderPane != null : "fx:id=\"currentOrderPane\" was not injected: check your FXML file 'CustomerMainPage.fxml'.";
    assert currentTotal != null : "fx:id=\"currentTotal\" was not injected: check your FXML file 'CustomerMainPage.fxml'.";
    assert selectTableButton != null : "fx:id=\"selectTableButton\" was not injected: check your FXML file 'CustomerMainPage.fxml'.";

    selectedAllergies = new ArrayList<String>();
    waiterButton.setDisable(true);

    // Extract dishes from menu
    refreshMenu();

    // Create a new order
    if (CustomerDriver.getCurrentOrder() == null) {
      CustomerDriver.setCurrentOrder(new Order());
      CustomerDriver.getCurrentOrder().setUserId(CustomerDriver.getUserID());
    }
    refreshOrder();

    // Check if there is already a saved table
    if (CustomerDriver.getCurrentOrder().getTable() != 0) {
      refreshTable();
    }

    // Button Generation
    makeMenuButtons(CustomerDriver.getCurrentMenu());
    makeFilterButtons(allergies, filterDishesButton);
    makeTableButtons();

    // See if Order button needs to be disabled
    checkPossibleOrder();
  }

  /**
   * The function called when a dish or drink button is pressed. It will display a popup explaining
   * the ingredients of the items, and then the Customer can decide to proceed to add it to their
   * order or discard it.
   * 
   * @param dishID The ID of the dish selected.
   * @param dishName The name of the dish selected.
   * @throws SQLException
   */
  public void selectDish(String dishID, String dishName) {
    Alert dishInfo = new Alert(AlertType.NONE, dishName, ButtonType.APPLY, ButtonType.CANCEL);

    try {
      ArrayList<String> ingredients = Menu.dishIngredients(dishName, CustomerDriver.getConnection());
      String ingredientsFormatted = String.join(", ", ingredients); // This will throw an error if
                                                                    // the item cannot be found
      dishInfo.setContentText(dishName.toUpperCase() + "\nThis dish contains the following: "
          + ingredientsFormatted.substring(1, ingredientsFormatted.length() - 1) + "\nIt contains "
          + Menu.getCalories(CustomerDriver.getConnection(), dishID) + " calories" + "\nClick apply to add to order");

    } catch (Exception e) {

      dishInfo.setContentText(
          "Sorry we couldn't pull the information for this dish. Please try again later");
      System.err.println(e);
    }

    // Alert buttons for dishInfo
    Optional<ButtonType> result = dishInfo.showAndWait();
    if (!result.isPresent()) {
      // This is needed to catch the case the customer cancels the item
      // Do nothing
    } else if (result.get() == ButtonType.APPLY) {
      CustomerDriver.getCurrentOrder().addItem(dishID);
      refreshOrder();
    }
  }

  /**
   * Refreshes the order pane so any items added to the order are displayed.
   * 
   * @throws SQLException
   */
  public void refreshOrder() {
    CustomerDriver.refreshOrder(currentOrderPane, currentTotal);
    checkPossibleOrder();
  }

  /**
   * Refreshes the saved menu. If there is an allergy active, it pulls the menu not containing that
   * allergy instead.
   * 
   * @throws SQLException
   */
  public void refreshMenu() throws SQLException {
    if (!isAllergyMenuActive) {
      Menu.getData(CustomerDriver.getConnection());
      CustomerDriver.setCurrentMenu(Menu.getData(CustomerDriver.getConnection()));
    }
  }

  /**
   * This makes the menu buttons used to select items off the menu.
   * 
   * @param buttonData A HashMap containing the button ID and dish name
   * @throws SQLException
   */
  public void makeMenuButtons(HashMap<String, String> buttonData) throws SQLException {

    // Reset the panes in-case there has been changes to the menu
    dishButtonPane.getChildren().clear();
    drinkButtonPane.getChildren().clear();

    // Adds the dishes
    for (Map.Entry<String, String> set : buttonData.entrySet()) {
      Button button = new Button(set.getValue());
      button.setMaxWidth(Double.MAX_VALUE);
      button.setOnAction(event -> selectDish(set.getKey(), set.getValue()));

      // Determines which column it should go into
      if (Menu.getType(set.getValue(), CustomerDriver.getConnection()).equals("f")) {
        dishButtonPane.getChildren().add(button);
      } else {
        drinkButtonPane.getChildren().add(button);
      }
    }
  }

  /**
   * This makes the filter buttons.
   * 
   * @param allergies An ArrayList of allergies
   * @param button The button where the allergies will go
   */
  public void makeFilterButtons(ArrayList<String> allergies, MenuButton button) {

    // Resetting the button in-case there has been changes to the allergies
    button.getItems().clear();
    for (String allergy : allergies) {
      CheckMenuItem allergyItem = new CheckMenuItem(allergy);
      allergyItem.setOnAction(event -> selectAllergy(allergyItem, allergyItem.getText(), button));
      button.getItems().add(allergyItem);
    }
  }

  /**
   * This selects an allergy(s) and changes the dishes available.
   * 
   * @param allergyItem The MenuItem representation of the allergy
   * @param allergy The allergy selected
   * @param button The MenuButton that holds the MenuItems that represent the allergies
   */
  public void selectAllergy(CheckMenuItem allergyItem, String allergy, MenuButton button) {

    try {

      // In the case clear filters is selected, clear all allergies
      if (allergy.equals("Clear Filters")) {
        button.setText("Filter Items");
        makeFilterButtons(allergies, button); // Re-making the buttons de-selects all current
                                              // filters
        isAllergyMenuActive = false;

      } else {

        // If the allergyItem is selected, then the allergy has been selected
        if (allergyItem.isSelected()) {
          selectedAllergies.add(allergy);
          isAllergyMenuActive = true;

          // If the allergyItem is de-selected, then the allergy has been removed
        } else {
          selectedAllergies.remove(allergy);
        }

        // If there are no current active allergies, then all allergies have been de-selected
        if (selectedAllergies.size() == 0) {
          button.setText("Filter Items");
          isAllergyMenuActive = false;

          // Otherwise update the menu with the selected allergies
        } else {
          button.setText("No " + String.join(", ", selectedAllergies));
          CustomerDriver.setCurrentMenu(Menu.allergenMenu(selectedAllergies, CustomerDriver.getConnection()));
        }
      }

      // Refresh the menu so it shows the changes made above
      refreshMenu();
      makeMenuButtons(CustomerDriver.getCurrentMenu());

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Makes the table MenuItems to represent the table numbers.
   */
  public void makeTableButtons() {
    Consumer<Integer> setTableFunc = tableNum -> setTable(tableNum);
    CustomerDriver.makeTableButtons(selectTableButton, setTableFunc);
  }

  /**
   * Runs when a table is selected. It sets the order's table number and refreshes the table.
   * 
   * @param i The table number selected.
   */
  public void setTable(int i) {
    CustomerDriver.getCurrentOrder().setTableNo(i);
    refreshTable();
  }

  /**
   * Updates the table button to show what table is selected and check if it's now possible to make
   * an order.
   */
  public void refreshTable() {
    selectTableButton.setText("Table " + CustomerDriver.getCurrentOrder().getTable());
    checkPossibleOrder();
    waiterButton.setDisable(false);
  }

  /**
   * Checks if it is possible to make an order by determining if: a table is selected and there is
   * at least one item in the order
   */
  public void checkPossibleOrder() {
    if (CustomerDriver.getCurrentOrder().getTable() != 0 && CustomerDriver.getCurrentOrder().getItems().size() != 0) {
      makeOrderButton.setDisable(false);
    } else {
      makeOrderButton.setDisable(true);
    }
  }
}
