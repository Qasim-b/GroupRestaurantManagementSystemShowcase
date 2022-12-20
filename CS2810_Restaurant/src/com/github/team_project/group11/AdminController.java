package com.github.team_project.group11;

import java.io.IOException;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

/**
 * Controls the Admin page
 * 
 * @author Rory Thomson-Bird 
 */
public class AdminController {

  @FXML
  private MenuItem allItem;

  @FXML
  private MenuItem customerItem;

  @FXML
  private MenuItem waiterItem;

  @FXML
  private MenuItem KitchenItem;

  @FXML
  private Button assignButton;

  @FXML
  private Button addButton;

  @FXML
  private MenuButton userMenu;

  @FXML
  private Button backButton;

  @FXML
  private TextField emailText;

  @FXML
  private TextField passText;

  @FXML
  private Button refreshButton;
  
  @FXML
  private Button reAssignButton;

  @FXML
  private Button deleteButton;

  @FXML
  private TextField typeText;

  @FXML
  private ListView<String> userList;

  @FXML
  private TextField userText;

  /**
   * Add new user
   * 
   * @param event Button click
   * @throws SQLException 
   */
  @FXML
  void addPressed(ActionEvent event) throws SQLException {
    String[] details =
        {userText.getText(), emailText.getText(), passText.getText(), typeText.getText()};
    Admin.addUser(details);
    Alert newAccount = new Alert(AlertType.INFORMATION);
    newAccount.setContentText("You have successfully made an account for a "+typeText.getText());
    newAccount.setHeaderText("New account");
    newAccount.setTitle("You have made an account");
    newAccount.show();
    refresh();
  }

  /**
   * Deletes a user
   * 
   * @param event Button click
   * @throws SQLException
   */
  @FXML
  void deletePressed(ActionEvent event) throws SQLException {
    String[] user = userList.getSelectionModel().getSelectedItem().split("\t");
    Admin.removeUser(user[0]);
    refresh();
  }

  /**
   * Return user to the login page
   * 
   * @param event Button click
   * @throws IOException
   */
  @FXML
  void backPressed(ActionEvent event) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/LoginPage.fxml"));
    String css = this.getClass().getResource("/waiter_style_sheet.css").toExternalForm();
    OaxacaDriver.changeScene(root, css);
  }

  /**
   * Manual refresh of page
   * 
   * @param event Button click
   * @throws SQLException
   */
  @FXML
  void refreshPressed(ActionEvent event) throws SQLException {
    refresh();
  }

  /**
   * Initialise the page
   * 
   * @throws SQLException
   */
  @FXML
  void initialize() throws SQLException {
    refresh();
  }

  /**
   * Refresh the page
   * 
   * @throws SQLException
   */
  void refresh() throws SQLException {
    userMenu.setText("All");
    userList.getItems().clear();
    userList.getItems().addAll(Admin.getUsers());
  }


  /**
   * Displays all users
   * 
   * @param event
   * @throws SQLException
   */
  @FXML
  void allClicked(ActionEvent event) throws SQLException {
    refresh();
  }

  /**
   * Displays the waiters
   * 
   * @param event
   * @throws SQLException
   */
  @FXML
  void waiterClicked(ActionEvent event) throws SQLException {
    userMenu.setText("Waiters");
    userList.getItems().clear();
    userList.getItems().addAll(Admin.getUsers("'waiter'"));
  }


  /**
   * Displays kitchen staff
   * 
   * @param event
   * @throws SQLException
   */
  @FXML
  void kitchenClicked(ActionEvent event) throws SQLException {
    userMenu.setText("Kitchen Staff");
    userList.getItems().clear();
    userList.getItems().addAll(Admin.getUsers("'kitchen'"));
  }


  /**
   * Displays customers
   * 
   * @param event
   * @throws SQLException
   */
  @FXML
  void customerClicked(ActionEvent event) throws SQLException {
    userMenu.setText("Customers");
    userList.getItems().clear();
    userList.getItems().addAll(Admin.getUsers("'customer'"));
  }


  /**
   * View assigned table and create pop-up to assign a waiter a table
   * 
   * @param event
   * @throws SQLException
   */
  @FXML
  void assignPressed(ActionEvent event) throws SQLException {
    String[] user = userList.getSelectionModel().getSelectedItem().split("\t");
    if (user[3].equals("waiter")) {
      TextInputDialog td = new TextInputDialog();
      td.setHeaderText("Enter table:");
      td.setContentText("Unassigned tables: "+Admin.getTables("1")+"\n" + "Assigned tables: "+Admin.getTables(user[0])+"\n");
      td.showAndWait();
      Admin.assignTable(user[0], td.getEditor().getText());
    } else {
      Alert noWaiter = new Alert(AlertType.WARNING);
      noWaiter.setContentText("Please select a waiter.");
      noWaiter.show();
    }
  }
  
  /**
   * Removes waiters assignment to table with pop-up
   * 
   * @param event
   * @throws SQLException
   */
  @FXML
  void reAssignPressed(ActionEvent event) throws SQLException {
    String[] user = userList.getSelectionModel().getSelectedItem().split("\t");
    if (user[3].equals("waiter")) {
      TextInputDialog td = new TextInputDialog();
      td.setHeaderText("Enter table:");
      td.setContentText("Unassigned tables: "+Admin.getTables("1")+"\n" + "Assigned tables: "+Admin.getTables(user[0])+"\n");
      td.showAndWait();
      Admin.assignTable("1", td.getEditor().getText());
    } else {
      Alert noWaiter = new Alert(AlertType.WARNING);
      noWaiter.setContentText("Please select a waiter.");
      noWaiter.show();
    }
  }
  
}
