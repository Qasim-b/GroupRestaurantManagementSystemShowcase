package com.github.team_project.group11;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

/**
 * This class controls the Waiter GUI scenes and executes the functionality for them.
 * 
 * @author Rory Thomson-Bird
 *
 */
public class WaiterGUI {

  @FXML
  private ListView<Integer> callList;

  @FXML
  private ListView<Integer> finishedList;

  @FXML
  private ListView<Integer> confirmedList;

  @FXML
  private ListView<String> readyList;

  @FXML
  private Button tableButton;

  @FXML
  private Button refreshButton;

  @FXML
  private Button removeCallButton;

  @FXML
  private Button selectOrderButton;

  @FXML
  private Button finishOrderButton;

  @FXML
  private Button menuButton;

  @FXML
  private Button backButton;

  /**
   * Remove table call
   * 
   * @param event button click
   * @throws Exception
   */
  @FXML
  void removePressed(ActionEvent event) throws Exception {
    if (callList.getSelectionModel().getSelectedItem() == null) {
      Alert noSelection = new Alert(AlertType.WARNING);
      noSelection.setContentText("You have not selected a table call.");
      noSelection.show();
    } else {
      Staff.removeCall(callList.getSelectionModel().getSelectedItem());
      refresh();
    }
  }

  /**
   * Refresh the scene
   * 
   * @param event button click
   * @throws Exception
   */
  @FXML
  void resfreshPressed(ActionEvent event) throws Exception {
    refresh();
  }

  /**
   * Start up the page
   * 
   * @throws Exception
   */
  @FXML
  void initialize() throws Exception {
    ResultSet rs = Database.executeQuery(OaxacaDriver.connection,
        "SELECT * from users WHERE username = '" + OaxacaDriver.username + "';");
    while (rs.next()) {
      OaxacaDriver.users.put(rs.getString("username"), rs.getString("user_id"));
    }
    refresh();
  }

  /**
   * Display the current orders in correct categories
   * 
   * @throws Exception
   */
  void refresh() throws Exception {
    readyList.getItems().clear();
    confirmedList.getItems().clear();
    finishedList.getItems().clear();
    callList.getItems().clear();
    OaxacaDriver.orderlist = Staff.getOrders(OaxacaDriver.connection);
    ArrayList<Order> temporders = new ArrayList<Order>();
    while (!OaxacaDriver.orderlist.isEmpty()) {
      Order order = OaxacaDriver.orderlist.remove(0);
      temporders.add(order);
      OaxacaDriver.ordermap.put(order.getOrderID(), order);
    }
    OaxacaDriver.orderlist = temporders;
    OaxacaDriver.selectedOrder = null;
    ArrayList<Order> tempOrder = new ArrayList<Order>();
    ArrayList<String> tempCall = new ArrayList<String>();
    HashMap<String, String> menu = Menu.getData(OaxacaDriver.connection);
    OaxacaDriver.calllist =
        Staff.getCalls(OaxacaDriver.connection, OaxacaDriver.users.get(OaxacaDriver.username));
    while (!OaxacaDriver.calllist.isEmpty()) {
      int call = Integer.parseInt(OaxacaDriver.calllist.remove(0));

      callList.getItems().add(call);
      tempCall.add(Integer.toString(call));
    }
    while (!OaxacaDriver.orderlist.isEmpty()) {
      Order order = OaxacaDriver.orderlist.remove(0);
      if (order.getCompleted() == true) {
        finishedList.getItems().add(order.getOrderID());
        tempOrder.add(order);
      } else {
        ArrayList<String> readyItems = new ArrayList<String>();
        while (!order.getReadyItems().isEmpty()) {
          String itemid = order.getReadyItems().remove(0);
          readyList.getItems().add(menu.get(itemid) + ". \t Order: " + order.getOrderID()
              + " \t Table: " + order.getTable());
          readyItems.add(itemid);
        }
        confirmedList.getItems().add(order.getOrderID());
        tempOrder.add(order);
        while (!readyItems.isEmpty()) {
          order.addReadyItem(readyItems.remove(0));
        }
      }
    }
    while (!tempOrder.isEmpty()) {
      OaxacaDriver.orderlist.add(tempOrder.remove(0));
    }
    while (!tempCall.isEmpty()) {
      OaxacaDriver.calllist.add(tempCall.remove(0));
    }
  }


  /**
   * Select finished orders for more details
   * 
   * @param event Button click
   * @throws IOException
   */
  @FXML
  void selectFinishedPressed(ActionEvent event) throws IOException {
    if (finishedList.getSelectionModel().getSelectedItem() == null) {
      Alert noSelection = new Alert(AlertType.WARNING);
      noSelection.setContentText("You have not selected an order.");
      noSelection.show();
    } else {
      OaxacaDriver.selectedOrder =
          OaxacaDriver.ordermap.get(finishedList.getSelectionModel().getSelectedItem());
      Parent root = FXMLLoader.load(getClass().getResource("/waiterOrder.fxml"));
      String css = this.getClass().getResource("/waiter_style_sheet.css").toExternalForm();
      OaxacaDriver.changeScene(root, css);
    }
  }

  /**
   * View the menu page
   * 
   * @throws IOException throws exception
   */
  @FXML
  void menuButtonPressed() throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/menuPage.fxml"));
    String css = this.getClass().getResource("/waiter_style_sheet.css").toExternalForm();
    OaxacaDriver.changeScene(root, css);
  }

  /**
   * Select the order to view in more detail
   * 
   * @param event button click
   * @throws Exception throws exception
   */
  @FXML
  void selectPressed(ActionEvent event) throws Exception {
    if (confirmedList.getSelectionModel().getSelectedItem() == null) {
      Alert noSelection = new Alert(AlertType.WARNING);
      noSelection.setContentText("You have not selected an order.");
      noSelection.show();
    } else {
      OaxacaDriver.selectedOrder =
          OaxacaDriver.ordermap.get(confirmedList.getSelectionModel().getSelectedItem());

      Parent root = FXMLLoader.load(getClass().getResource("/waiterOrder.fxml"));
      String css = this.getClass().getResource("/waiter_style_sheet.css").toExternalForm();
      OaxacaDriver.changeScene(root, css);
    }
  }

  /**
   * View the tables assigned to the user
   * 
   * @param event
   * @throws Exception
   */
  @FXML
  void tablePressed(ActionEvent event) throws Exception {
    Alert tableinfo = new Alert(AlertType.INFORMATION);
    String alertmsg = "";
    ResultSet rs = Database.executeQuery(OaxacaDriver.connection,
        "SELECT table_no from tables WHERE waiter = '"
            + OaxacaDriver.users.get(OaxacaDriver.username) + "';");
    while (rs.next()) {
      alertmsg = alertmsg + rs.getString("table_no") + "\n";
    }
    tableinfo.setHeaderText("Your assigned tables are: ");
    tableinfo.setContentText(alertmsg);
    tableinfo.show();
  }

  /**
   * Log out
   * 
   * @param event
   * @throws IOException
   */
  @FXML
  void backPressed(ActionEvent event) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/LoginPage.fxml"));
    String css = this.getClass().getResource("/waiter_style_sheet.css").toExternalForm();
    OaxacaDriver.changeScene(root, css);
  }
}