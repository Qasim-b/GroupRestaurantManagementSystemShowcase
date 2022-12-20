package com.github.team_project.group11;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * @author Rory Thomson-Bird
 *
 */
public class LoginController {

  @FXML
  private Button loginButton;

  @FXML
  private TextField passText;

  @FXML
  private Button registerButton;

  @FXML
  private TextField userText;

  /**
   * Logs in to the correct GUI
   * 
   * @param event Button Click
   * @throws SQLException
   * @throws IOException
   */
  @FXML
  void loginPressed(ActionEvent event) throws SQLException, IOException {
    OaxacaDriver.username = "";

    if (Login.validatePassword(OaxacaDriver.connection, "'" + userText.getText() + "'", "'"+passText.getText()+"'")) {
      ResultSet rs =
          Database.executeQuery(OaxacaDriver.connection, "SELECT * FROM USERS WHERE username = '"
              + userText.getText() + "' AND password = crypt('"+passText.getText()+"', password);");
      rs.next();
      if (rs.getString("type").equals("waiter")) {
        OaxacaDriver.username = userText.getText();
        Parent root = FXMLLoader.load(getClass().getResource("/waiterGUI.fxml"));
        String css = this.getClass().getResource("/waiter_style_sheet.css").toExternalForm();
        OaxacaDriver.changeScene(root, css);
      } else if (rs.getString("type").equals("admin")) {
        OaxacaDriver.username = userText.getText();
        Parent root = FXMLLoader.load(getClass().getResource("/adminPage.fxml"));
        String css = this.getClass().getResource("/waiter_style_sheet.css").toExternalForm();
        OaxacaDriver.changeScene(root, css);

      } else if (rs.getString("type").equals("customer")) {
        CustomerDriver.start(OaxacaDriver.getCurrentStage(), rs.getInt("user_id"));
        Parent root = FXMLLoader.load(getClass().getResource("/Start_page.fxml"));
        String css = this.getClass().getResource("/customer_menu.css").toExternalForm();
        CustomerDriver.changeScene(root, css);

      } else if (rs.getString("type").equals("kitchen")) {
        Parent root = FXMLLoader.load(getClass().getResource("/KitchenGUI.fxml"));
        String css = this.getClass().getResource("/kitchen_stylesheet.css").toExternalForm();
        OaxacaDriver.changeScene(root, css);

      } else {
        Alert noSelection = new Alert(AlertType.WARNING);
        noSelection.setContentText("Invalid user.");
        noSelection.show();
      }
    } else {
      Alert badLogin = new Alert(AlertType.WARNING);
      badLogin.setContentText("Incorrect username or password.");
      badLogin.show();
    }
  }

  /**
   * Send to the register scene
   * @param event - registration activated
   * @throws IOException
   */
  @FXML
  void registerPressed(ActionEvent event) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/registerPage.fxml"));
    String css = this.getClass().getResource("/waiter_style_sheet.css").toExternalForm();
    OaxacaDriver.changeScene(root, css);
  }
}
