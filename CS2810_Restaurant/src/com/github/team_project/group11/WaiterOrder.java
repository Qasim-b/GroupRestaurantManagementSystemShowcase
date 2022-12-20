package com.github.team_project.group11;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * This class controls the waiter order (management) screen
 * 
 * @author Rory Thomson-Bird
 *
 */
public class WaiterOrder {

  @FXML
  private Button completeButton;

  @FXML
  private Label StatusLabel;

  @FXML
  private Label TableNumLabel;

  @FXML
  private Label orderIdLabel;

  @FXML
  private Button backButton;

  @FXML
  private Button deleteButton;

  @FXML
  private Button readyButton;

  @FXML
  private ListView<String> itemList;

  @FXML
  private TextField itemRemoveText;

  @FXML
  private Button removeButton;

  /**
   * Startup the order page
   * 
   * @throws SQLException 
   */
  @FXML
  void initialize() throws SQLException {
    refresh();
  }

  /**
   * Refreshes the order page to contain the correct values
   * 
   * @throws SQLException 
   */
  void refresh() throws SQLException {
    itemList.getItems().clear();
    orderIdLabel.setText(Integer.toString(OaxacaDriver.selectedOrder.getOrderID()));
    TableNumLabel.setText(Integer.toString(OaxacaDriver.selectedOrder.getTable()));
    if (OaxacaDriver.selectedOrder.getCompleted()) {
      StatusLabel.setText("Completed");
    } else {
      StatusLabel.setText("Confirmed");
    }
    ArrayList<String> Items = new ArrayList<String>();
    while (!OaxacaDriver.selectedOrder.getItems().isEmpty()) {
      HashMap<String, String> menu = Menu.getData(OaxacaDriver.connection);
      String item = OaxacaDriver.selectedOrder.getItems().remove(0);
      Items.add(item);
      if (OaxacaDriver.selectedOrder.getReadyItems().contains(item)) {
        itemList.getItems().add(item + "\t" + menu.get(item) + "(Ready)");
      } else {
        itemList.getItems().add(item + "\t" + menu.get(item));
      }
    }
    while (!Items.isEmpty()) {
      OaxacaDriver.selectedOrder.getItems().add(Items.remove(0));
    }
  }

  /**
   * Exits the Waiter order scene and takes the user back to the (main) waiter GUI scene.
   * 
   * @throws IOException
   */
  void exit() throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/waiterGUI.fxml"));
    String css = this.getClass().getResource("/waiter_style_sheet.css").toExternalForm();
    OaxacaDriver.changeScene(root, css);
  }

  /**
   * Set the order to complete
   * 
   * @param event button click
   * @throws SQLException 
   */
  @FXML
  void completePressed(ActionEvent event) throws SQLException {
    OaxacaDriver.selectedOrder.complete(OaxacaDriver.connection);
    refresh();
  }

  /**
   * Send user back to the WaiterGUI
   * 
   * @param event button click
   * @throws Exception throws exception
   */
  @FXML
  void backPressed(ActionEvent event) throws Exception {
    while (!itemList.getItems().isEmpty()) {
      OaxacaDriver.selectedOrder.addItem(itemList.getItems().get(0));
      itemList.getItems().remove(0);
    }
    itemList.getItems().clear();
    exit();
  }


  /**
   * Delete order
   * 
   * @param event button click
   * @throws IOException 
   * @throws SQLException 
   */
  @FXML
  void deletePressed(ActionEvent event) throws IOException, SQLException {
    Database.execute(OaxacaDriver.connection, "DELETE FROM orders WHERE id = "+ OaxacaDriver.selectedOrder.getOrderID());
    exit();
  }

  /**
   * Ready an item
   * 
   * @param event button click
   * @throws SQLException
   */
  @FXML
  void readyPressed(ActionEvent event) throws SQLException {
    String[] selected = itemList.getSelectionModel().getSelectedItem().split("\t");
    OaxacaDriver.selectedOrder.addReadyItem(selected[0]);
    Statement st = OaxacaDriver.connection.createStatement();

    st.execute("UPDATE ORDERS set ready_items = '{"
        + OaxacaDriver.selectedOrder.getReadyItems().toString().substring(1,
            OaxacaDriver.selectedOrder.getReadyItems().toString().length() - 1)
        + "}' WHERE id = " + OaxacaDriver.selectedOrder.getOrderID()
        + " AND ready_items IS DISTINCT FROM '{"
        + OaxacaDriver.selectedOrder.getReadyItems().toString().substring(1,
            OaxacaDriver.selectedOrder.getReadyItems().toString().length() - 1)
        + "}';");
    st.execute(
        "UPDATE ORDERS set items = '{"
            + OaxacaDriver.selectedOrder.getItems().toString()
                .substring(1, OaxacaDriver.selectedOrder.getItems().toString().length() - 1)
            + "}' WHERE id = " + OaxacaDriver.selectedOrder.getOrderID()
            + " AND items IS DISTINCT FROM '{" + OaxacaDriver.selectedOrder.getItems().toString()
                .substring(1, OaxacaDriver.selectedOrder.getItems().toString().length() - 1)
            + "}';");
    refresh();


  }

  /**
   * Remove item from an order
   * 
   * @param event button click
   * @throws SQLException 
   */
  @FXML
  void removePressed(ActionEvent event) throws SQLException {
    itemList.getItems().remove(itemList.getSelectionModel().getSelectedItem());
    String[] item = itemList.getSelectionModel().getSelectedItem().split("\t");
    OaxacaDriver.selectedOrder.removeItem(item[0]);
    Database.execute(OaxacaDriver.connection,"UPDATE orders SET items = '{"+OaxacaDriver.selectedOrder.getItems().toString()
        .substring(1, OaxacaDriver.selectedOrder.getItems().toString().length() - 1)+"}' WHERE id = "+ OaxacaDriver.selectedOrder.getOrderID());
    refresh();
  }

}
