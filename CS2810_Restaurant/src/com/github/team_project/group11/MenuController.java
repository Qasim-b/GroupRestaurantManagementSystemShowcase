package com.github.team_project.group11;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * This class is executed for the menu functionality in customer scenes.
 * 
 * @author Rory Thomson-Bird
 */
public class MenuController {
  private HashMap<String, String> data;
  @FXML
  private Button addItem;

  @FXML
  private CheckBox molluscBox;

  @FXML
  private CheckBox glutenBox;

  @FXML
  private CheckBox fishBox;

  @FXML
  private CheckBox nutsBox;

  @FXML
  private CheckBox lupinBox;

  @FXML
  private CheckBox milkBox;

  @FXML
  private CheckBox peanutBox;

  @FXML
  private CheckBox crustaceanBox;

  @FXML
  private CheckBox mustardBox;

  @FXML
  private CheckBox sesameBox;

  @FXML
  private CheckBox celeryBox;

  @FXML
  private CheckBox sulphiteBox;

  @FXML
  private CheckBox eggBox;

  @FXML
  private CheckBox soyBox;

  @FXML
  private Button filterButton;

  @FXML
  private Button viewButton;

  @FXML
  private Button backButton;

  @FXML
  private Button helpButton;

  @FXML
  private TextField categoryText;

  @FXML
  private TextField costText;

  @FXML
  private TextField calorieText;

  @FXML
  private TextField ingredientText;

  @FXML
  private ListView<String> menuList;

  @FXML
  private TextField nameText;

  @FXML
  private Button refreshButton;

  @FXML
  private Button removeButton;

  @FXML
  private TextField veganText;

  @FXML
  private TextField vegetarianText;

  /**
   * Add an item to the menu
   * 
   * @param event button click
   * @throws SQLException throws exception
   */
  @FXML
  void addPressed(ActionEvent event) throws SQLException {
    String vege = null;
    String vega = null;
    if (vegetarianText.getText().equals("yes")) {
      vege = "true";
    } else if (vegetarianText.getText().equals("no")) {
      vege = "false";
    } else {
      Alert badInput = new Alert(AlertType.WARNING);
      badInput.setContentText("Vegetarian must be \"yes\" or \"no\".");
      badInput.show();
    }
    if (veganText.getText().equals("yes")) {
      vega = "true";
    } else if (veganText.getText().equals("no")) {
      vega = "false";
    } else {
      Alert badInput = new Alert(AlertType.WARNING);
      badInput.setContentText("Vegan must be \"yes\" or \"no\".");
      badInput.show();
    }
    String newItem = "'" + nameText.getText() + "'," + Float.parseFloat(costText.getText()) + ", '{"
        + ingredientText.getText().toLowerCase() + "}'," + vege + "," + vega + ", '"
        + categoryText.getText() + "'," + calorieText.getText();
    Menu.addEntry(OaxacaDriver.connection, newItem);
    refresh();
    nameText.setText("");
    costText.setText("");
    vegetarianText.setText("");
    veganText.setText("");
    ingredientText.setText("");
    categoryText.setText("");
    calorieText.setText("");
  }

  /**
   * Starts up the page
   * 
   * @throws SQLException
   */
  @FXML
  void initialize() throws SQLException {
    refresh();
  }

  /**
   * Refresh the scene to give the current values in the database
   * 
   * @throws SQLException throws exception
   */
  void refresh() throws SQLException {
    ArrayList<String> allergies = new ArrayList<String>();
    OaxacaDriver.selectedItem = 0;
    menuList.getItems().clear();
    if (molluscBox.isSelected()) {
      allergies.add("molluscs");
    }
    if (glutenBox.isSelected()) {
      allergies.add("gluten");
    }
    if (fishBox.isSelected()) {
      allergies.add("fish");
    }
    if (nutsBox.isSelected()) {
      allergies.add("nuts");
    }
    if (eggBox.isSelected()) {
      allergies.add("eggs");
    }
    if (soyBox.isSelected()) {
      allergies.add("soya");
    }
    if (lupinBox.isSelected()) {
      allergies.add("lupin");
    }
    if (milkBox.isSelected()) {
      allergies.add("milk");
    }
    if (peanutBox.isSelected()) {
      allergies.add("peanuts");
    }
    if (crustaceanBox.isSelected()) {
      allergies.add("crustaceans");
    }
    if (mustardBox.isSelected()) {
      allergies.add("mustard");
    }
    if (sesameBox.isSelected()) {
      allergies.add("sesame");
    }
    if (celeryBox.isSelected()) {
      allergies.add("celery");
    }
    if (sulphiteBox.isSelected()) {
      allergies.add("sulphites");
    }
    if (allergies.size() > 0) {
      data = Menu.allergenMenu(allergies, OaxacaDriver.connection);
    } else {
      data = Menu.getData(OaxacaDriver.connection);
    }
    for (Map.Entry<String, String> set : data.entrySet()) {
      menuList.getItems().add(set.getKey() + "\t" + set.getValue());
    }
  }

  /**
   * Show the user how to add items
   */
  @FXML
  void helpPressed() {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Help");
    alert.setHeaderText("Instructions for creating a menu item");
    alert.setContentText("Item name should be the name of the item \n"
        + "Item cost should be in money form while not including the symbol, eg: 4.50 \n"
        + "Vegetarian should be in the form \"yes\" or \"no\" \n"
        + "Vegan should be in the form \"yes\" or \"no\" \n"
        + "Ingredient list should be a list in the form \"item1, item2, item3\" \n"
        + "Category should be either \"Food\" or \"Drink\"");
    alert.getDialogPane().setPrefSize(450, 250);
    alert.showAndWait();
  }

  /**
   * Change scene to one that displays the item in more detail
   * 
   * @param event button click
   * @throws IOException throws exception
   */
  @FXML
  void viewPressed(ActionEvent event) throws IOException {
    if (menuList.getSelectionModel().getSelectedItem() == null) {
      Alert noSelection = new Alert(AlertType.WARNING);
      noSelection.setContentText("You have not selected an item.");
      noSelection.show();
    } else {
      String item = menuList.getSelectionModel().getSelectedItem();
      String[] itemarr = item.split("\t");
      OaxacaDriver.selectedItem = Integer.parseInt(itemarr[0]);
      Parent root = FXMLLoader.load(getClass().getResource("/MenuItem.fxml"));
      String css = this.getClass().getResource("/waiter_style_sheet.css").toExternalForm();
      OaxacaDriver.changeScene(root, css);
    }
  }

  /**
   * Refresh the page
   * 
   * @param event button click
   * @throws SQLException
   */
  @FXML
  void refreshPressed(ActionEvent event) throws SQLException {
    refresh();
  }

  /**
   * Remove an item
   * 
   * @param event button click
   * @throws SQLException
   */
  @FXML
  void removePressed(ActionEvent event) throws SQLException {
    if (menuList.getSelectionModel().getSelectedItem() == null) {
      Alert noSelection = new Alert(AlertType.WARNING);
      noSelection.setContentText("You have not selected a menu item.");
      noSelection.show();
    } else {
      String item = menuList.getSelectionModel().getSelectedItem();
      String[] itemarr = item.split("\t");
      Menu.deleteRow(OaxacaDriver.connection, itemarr[0]);
      refresh();
    }
  }

  /**
   * Return to the Waiter GUI
   * 
   * @param event
   * @throws IOException
   */
  @FXML
  void backPressed(ActionEvent event) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/WaiterGUI.fxml"));
    String css = this.getClass().getResource("/waiter_style_sheet.css").toExternalForm();
    OaxacaDriver.changeScene(root, css);
  }
}
