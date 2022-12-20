package com.github.team_project.group11;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;

/**
 * This class is executed to run the past orders scene.
 * 
 * @author Francesca Coss
 */
public class PastOrdersController {

  @FXML
  private Button BackToHomePage;

  @FXML
  private ListView<String> PastOrderIDs;

  @FXML
  private ListView<String> PastOrderContent;

  @FXML
  private Button displayButton;

  @FXML
  private Button refreshButton;

  /**
   * Returns the customer (user) to the home page.
   * 
   * @param Event
   * @throws Exception
   **/
  @FXML
  void SendToHomePage(ActionEvent event) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/Start_page.fxml"));
    String css = this.getClass().getResource("/customer_menu.css").toExternalForm();
    CustomerDriver.changeScene(root, css);
  }

  /**
   * Displays the selected order.
   * 
   * @param event
   * @throws SQLException
   */
  @FXML
  void displayOrder(ActionEvent event) {

    if (PastOrderIDs.getSelectionModel().getSelectedItem() == null) {
      Alert noSelection = new Alert(AlertType.WARNING);
      noSelection.setContentText("You have not selected a previous order.");
      noSelection.show();
    } else {

      try {
        for (Order order : Customer.getOrders(CustomerDriver.getConnection(),
            CustomerDriver.getUserID())) {
          if (PastOrderIDs.getSelectionModel().getSelectedItem()
              .equals(String.valueOf(order.getOrderID()))) {
            formatOrder(order);
          }
        }
      } catch (Exception e) {
        PastOrderContent.getItems().clear();
        Alert noSelection = new Alert(AlertType.WARNING);
        noSelection.setContentText("Sorry this order could not be displayed. Please try again.");
        noSelection.show();
      }
    }
  }

  /**
   * Refreshes the order in-case there has been status changes.
   * 
   * @param event
   * @throws Exception
   */
  @FXML
  void refresh(ActionEvent event) throws Exception {
    PastOrderIDs.getItems().clear();
    PastOrderContent.getItems().clear();
    ListOrderIDs();
  }

  @FXML
  void initialize() throws Exception {
    assert PastOrderIDs != null : "fx:id=\"PastOrderIDs\" was not injected: check your FXML file 'PastOrders.fxml'.";
    assert displayButton != null : "fx:id=\"displayButton\" was not injected: check your FXML file 'PastOrders.fxml'.";
    assert PastOrderContent != null : "fx:id=\"PastOrderContent\" was not injected: check your FXML file 'PastOrders.fxml'.";
    assert BackToHomePage != null : "fx:id=\"BackToHomePage\" was not injected: check your FXML file 'PastOrders.fxml'.";

    ListOrderIDs();
  }

  /**
   * Lists all past orders of an individual customer account.
   * 
   * @throws Exception
   */
  void ListOrderIDs() throws Exception {
    ArrayList<Order> orders = Customer.getOrders(CustomerDriver.getConnection(), CustomerDriver.getUserID());
    Collections.reverse(orders);
    for (Order order : orders) {
      PastOrderIDs.getItems().add(String.valueOf(order.getOrderID()));
    }
  }

  /**
   * Formats the selected order so it can be displayed in the PastOrderContent listview.
   * 
   * @param order The selected order
   * @throws SQLException
   */
  void formatOrder(Order order) throws SQLException {
    PastOrderContent.getItems().clear();

    ArrayList<String> content =
        new ArrayList<String>(Arrays.asList("Order ID: " + String.valueOf(order.getOrderID()),
            "Time made: " + order.getTime(), "Total cost: £" + String.valueOf(order.getTotal()), "",
            "TRACKING", "Is order in kitchen: " + order.getConfirmed(),
            "Is order delivered: " + order.isReady(), "", "ORDER"));

    PastOrderContent.getItems().addAll(content);
    PastOrderContent.getItems().addAll(CustomerDriver.formatOrder(order));
  }
}