package com.github.team_project.group11;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;

/**
 * This class controls the GUI for the customer start page.
 * 
 * @author Luna Cheung
 *
 */
public class CustomerStartPage {

  private int tableNum;

  @FXML // ResourceBundle that was given to the FXMLLoader
  private ResourceBundle resources;

  @FXML // URL location of the FXML file that was given to the FXMLLoader
  private URL location;

  @FXML
  private Label title;

  @FXML
  private Button buttonMain;

  @FXML
  private Button buttonHelp;

  @FXML
  private Button buttonPast;

  @FXML
  private MenuButton tableButton;

  @FXML
  private Button logoutButton;

  /**
   * This function is called when the make order button is pressed. It changes the scene to the main
   * page.
   * 
   * @param event
   * @throws IOException
   */
  @FXML
  void mainButtonPressed(ActionEvent event) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/CustomerMainPage.fxml"));
    String css = this.getClass().getResource("/customer_opening_page.css").toExternalForm();
    CustomerDriver.changeScene(root, css);
  }

  /**
   * This function is called when the help waiter button is pressed.
   * 
   * @param event
   */
  @FXML
  void helpButtonPressed(ActionEvent event) {
    CustomerDriver.callWaiter(tableNum);
  }

  /**
   * This function is called when the past orders button is pressed. It changes the scene to the
   * past order scene.
   * 
   * @param event
   * @throws IOException
   */
  @FXML
  void pastButtonPressed(ActionEvent event) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/PastOrders.fxml"));
    String css = this.getClass().getResource("/customer_opening_page.css").toExternalForm();
    CustomerDriver.changeScene(root, css);
  }

  /**
   * This function is called when the logout button is pressed. It changes the scene to the login
   * scene.
   * 
   * @param event
   * @throws IOException
   */
  @FXML
  void logout(ActionEvent event) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/LoginPage.fxml"));
    String css = this.getClass().getResource("/customer_opening_page.css").toExternalForm();
    OaxacaDriver.changeScene(root, css);
  }

  /**
   * This function is called upon by the FXMLLoader to support the running of the customer start page scene.
   */
  @FXML 
  void initialize() {
    assert title != null : "fx:id=\"title\" was not injected: check your FXML file 'Start_page.fxml'.";
    assert buttonMain != null : "fx:id=\"buttonMain\" was not injected: check your FXML file 'Start_page.fxml'.";
    assert buttonHelp != null : "fx:id=\"buttonHelp\" was not injected: check your FXML file 'Start_page.fxml'.";
    assert buttonPast != null : "fx:id=\"buttonPast\" was not injected: check your FXML file 'Start_page.fxml'.";

    buttonHelp.setDisable(true);
    makeTableButtons();
  }


  /**
   * Makes the table MenuItems to represent the table numbers.
   */
  public void makeTableButtons() {
    Consumer<Integer> setTableFunc = tableNum -> setTable(tableNum);
    CustomerDriver.makeTableButtons(tableButton, setTableFunc);
  }

  /**
   * Sets the current table.
   * 
   * @param tableNum The table to be selected
   */
  public void setTable(int tableNum) {
    this.tableNum = tableNum;
    tableButton.setText("Table " + tableNum);
    buttonHelp.setDisable(false);
  }
}

