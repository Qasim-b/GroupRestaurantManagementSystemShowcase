package com.github.team_project.group11;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

/**
 * This class controls the scene where a customer can edit their current order.
 * 
 * @author Luna Cheung
 *
 */
public class CustomerEditOrder {

  @FXML // ResourceBundle that was given to the FXMLLoader
  private ResourceBundle resources;

  @FXML // URL location of the FXML file that was given to the FXMLLoader
  private URL location;

  @FXML // fx:id="dishPlane"
  private AnchorPane dishPlane; // Value injected by FXMLLoader

  @FXML // fx:id="orderList"
  private ListView<String> orderList; // Value injected by FXMLLoader

  @FXML // fx:id="orderPlane"
  private AnchorPane orderPlane; // Value injected by FXMLLoader

  @FXML // fx:id="clearOrderButton"
  private Button clearOrderButton; // Value injected by FXMLLoader

  @FXML // fx:id="menuButton"
  private Button menuButton; // Value injected by FXMLLoader

  @FXML // fx:id="deleteItemButton"
  private Button deleteItemButton; // Value injected by FXMLLoader

  /**
   * This is called when the clear button is selected and clears the entire order but doesn't delete
   * it.
   * 
   * @param event
   */
  @FXML
  void clearOrder(ActionEvent event) {
    CustomerDriver.getCurrentOrder().clearItems();
    refreshOrder();
  }

  /**
   * This is called when the delete item button is selected and deletes the selected item.
   * 
   * @param event
   */
  @FXML
  void deleteItem(ActionEvent event) {
    ObservableList<String> selectedItems = FXCollections.observableArrayList();
    for (String item : orderList.getSelectionModel().getSelectedItems()) {
      item = item.substring(4);
      selectedItems.add(item);
    }

    for (Map.Entry<String, String> set : CustomerDriver.getCurrentMenu().entrySet()) {
      if (selectedItems.contains(set.getValue())) {
        CustomerDriver.getCurrentOrder().removeItem(set.getKey());
      }
    }
    refreshOrder();
  }

  /**
   * This is called when the menu button is pressed and changes the scene back to the ordering
   * scene.
   * 
   * @param event
   * @throws IOException
   */
  @FXML
  void menuButton(ActionEvent event) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/CustomerMainPage.fxml"));
    String css = this.getClass().getResource("/customer_menu.css").toExternalForm();
    CustomerDriver.changeScene(root, css);
  }

  /**
   * This function is used to run the customer edit order scene and is called by the FXMLLoader 
   * when initialisation is complete.
   */
  @FXML 
  void initialize() {
    assert dishPlane != null : "fx:id=\"dishPlane\" was not injected: check your FXML file 'CustomerEditOrder.fxml'.";
    assert orderList != null : "fx:id=\"orderList\" was not injected: check your FXML file 'CustomerEditOrder.fxml'.";
    assert orderPlane != null : "fx:id=\"orderPlane\" was not injected: check your FXML file 'CustomerEditOrder.fxml'.";
    assert clearOrderButton != null : "fx:id=\"clearOrderButton\" was not injected: check your FXML file 'CustomerEditOrder.fxml'.";
    assert menuButton != null : "fx:id=\"menuButton\" was not injected: check your FXML file 'CustomerEditOrder.fxml'.";
    assert deleteItemButton != null : "fx:id=\"deleteItemButton\" was not injected: check your FXML file 'CustomerEditOrder.fxml'.";

    refreshOrder();
  }

  /**
   * This refreshes what is displayed on the order list.
   */
  public void refreshOrder() {
    orderList.getItems().clear();
    ArrayList<String> itemsToAdd = CustomerDriver.formatOrder(CustomerDriver.getCurrentOrder());
    orderList.getItems().addAll(itemsToAdd);
  }
}
