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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
  @FXML
  private PasswordField confirmText;

  @FXML
  private TextField emailText;

  @FXML
  private PasswordField passText;

  @FXML
  private Button registerButton;

  @FXML
  private TextField userText;
  
  @FXML
  private Button backButton;
  
  /**
   * Registers a user and checks given details match the required conditions
   * 
   * @param event - registers the user
   * @throws SQLException
   */
  @FXML
  void registerPressed(ActionEvent event) throws SQLException {
    if (!emailText.getText().contains("@")) {
      Alert badEmail = new Alert(AlertType.WARNING);
      badEmail.setContentText("Email must include @");
      badEmail.show();
    } else {
      if (passText.getText().equals(confirmText.getText())) {
        ResultSet userCheck = Database.executeQuery(OaxacaDriver.connection,
            "Select * FROM users WHERE username = '" + userText.getText() + "'");
        if (!userCheck.next()) {
          userCheck = Database.executeQuery(OaxacaDriver.connection,
              "Select * FROM users WHERE email = '" + emailText.getText() + "'");
          if (!userCheck.next()) {
            String[] newUser = {"'" + userText.getText() + "'", "'" + emailText.getText() + "'",
                "crypt('" + passText.getText() + "', 'password')", "'customer'"};

            Alert newAccount = new Alert(AlertType.INFORMATION);
            newAccount.setContentText("You have successfully made an account!");
            newAccount.setHeaderText("Congratulations "+ userText.getText()+ "!");
            newAccount.setTitle("You have made an account");
            newAccount.show();
            Register.createUser(OaxacaDriver.connection, newUser);
            userText.clear();
            emailText.clear();
            passText.clear();
            confirmText.clear();
          } else {
            Alert badEmail = new Alert(AlertType.WARNING);
            badEmail.setContentText("Email already in use.");
            badEmail.show();
          }
        } else {
          Alert badUsername = new Alert(AlertType.WARNING);
          badUsername.setContentText("Username taken, please pick another.");
          badUsername.show();
        }
      } else {
        Alert noMatch = new Alert(AlertType.WARNING);
        noMatch.setContentText("Password and confirmed password do not match.");
        noMatch.show();
      }
    }
  }
  
  /**
   * Return to login scene
   * 
   * @param event - takes you back to the login page.
   * @throws IOException
   */
  @FXML
  void backPressed(ActionEvent event) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/LoginPage.fxml"));
    String css = this.getClass().getResource("/waiter_style_sheet.css").toExternalForm();
    OaxacaDriver.changeScene(root, css);
  }

}
