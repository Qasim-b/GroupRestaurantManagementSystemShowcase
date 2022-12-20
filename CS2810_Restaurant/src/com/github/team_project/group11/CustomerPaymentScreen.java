package com.github.team_project.group11;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

/**
 * This class controls the GUI features on the payment window for the customer.
 * 
 * @author Luna Cheung
 *
 */
public class CustomerPaymentScreen {

  @FXML // ResourceBundle that was given to the FXMLLoader
  private ResourceBundle resources;

  @FXML // URL location of the FXML file that was given to the FXMLLoader
  private URL location;

  @FXML
  private AnchorPane dishPlane;

  @FXML
  private VBox dishButtonPane;

  @FXML
  private TextField cardNumberField;

  @FXML
  private MenuButton expiryDateMonthButton;

  @FXML
  private MenuButton expiryDateYearButton;

  @FXML
  private TextField cvvField;

  @FXML
  private AnchorPane orderPlane;

  @FXML
  private Button returnToMenuButton;

  @FXML
  private Button payButton;

  @FXML
  private TextFlow currentOrderPane;

  @FXML
  private TextFlow currentTotal;

  /**
   * This function is called when the pay button is pressed. If the payment can be validated, it
   * sends the order to the orders database and returns to the start screen. If the payment can't be
   * validated, it notifies the user of this and does nothing.
   *
   * @param event
   * @throws IOException
   * @throws ParseException
   * @throws SQLException
   */
  @FXML
  void payOrder(ActionEvent event) throws IOException, ParseException, SQLException {

    Payment payment = new Payment(CustomerDriver.getCurrentOrder().getTotal());
    if (payment.processPayment(cardNumberField.getText(), cvvField.getText(), convertDate())) {

      // Sends order and notifies the customer
      CustomerDriver.getCurrentOrder().confirm(CustomerDriver.getConnection());
      Alert popup = new Alert(AlertType.NONE,
          "Your payment is successful!\nThank you for placing your order", ButtonType.OK);
      popup.show();

      // Resets the current order and returns to start screen
      CustomerDriver.setCurrentOrder(null);
      Parent root = FXMLLoader.load(getClass().getResource("/Start_page.fxml"));
      String css = this.getClass().getResource("/customer_menu.css").toExternalForm();
      CustomerDriver.changeScene(root, css);

    } else {

      // If payment isn't successful, alert the user
      Alert popup = new Alert(AlertType.NONE,
          "Your payment details could not be verified.\nPlease check them again, before continuing",
          ButtonType.OK);
      popup.show();
    }
  }

  /**
   * This function is called when the return to menu button is clicked. It changes the scene back to
   * the order scene.
   * 
   * @param event
   * @throws IOException
   */
  @FXML
  void returnToMenuClicked(ActionEvent event) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/CustomerMainPage.fxml"));
    String css = this.getClass().getResource("/customer_menu.css").toExternalForm();
    CustomerDriver.changeScene(root, css);
  }

  /**
   * This function is called to initialise the payment screen scene the customers will use.
   * 
   * @throws SQLException
   */
  @FXML // This method is called by the FXMLLoader when initialization is complete
  void initialize() throws SQLException {
    assert dishPlane != null : "fx:id=\"dishPlane\" was not injected: check your FXML file 'PaymentScreen.fxml'.";
    assert dishButtonPane != null : "fx:id=\"dishButtonPane\" was not injected: check your FXML file 'PaymentScreen.fxml'.";
    assert cardNumberField != null : "fx:id=\"cardNumberField\" was not injected: check your FXML file 'PaymentScreen.fxml'.";
    assert expiryDateMonthButton != null : "fx:id=\"expiryDateMonthButton\" was not injected: check your FXML file 'PaymentScreen.fxml'.";
    assert expiryDateYearButton != null : "fx:id=\"expiryDateYearButton\" was not injected: check your FXML file 'PaymentScreen.fxml'.";
    assert cvvField != null : "fx:id=\"cvvField\" was not injected: check your FXML file 'PaymentScreen.fxml'.";
    assert orderPlane != null : "fx:id=\"orderPlane\" was not injected: check your FXML file 'PaymentScreen.fxml'.";
    assert returnToMenuButton != null : "fx:id=\"returnToMenuButton\" was not injected: check your FXML file 'PaymentScreen.fxml'.";
    assert payButton != null : "fx:id=\"payButton\" was not injected: check your FXML file 'PaymentScreen.fxml'.";
    assert currentOrderPane != null : "fx:id=\"currentOrderPane\" was not injected: check your FXML file 'PaymentScreen.fxml'.";
    assert currentTotal != null : "fx:id=\"currentTotal\" was not injected: check your FXML file 'PaymentScreen.fxml'.";

    CustomerDriver.refreshOrder(currentOrderPane, currentTotal);
    payButton.setDisable(true);

    populateRadioMenuButton(expiryDateMonthButton, new ArrayList<>(
        Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12")));
    populateRadioMenuButton(expiryDateYearButton,
        new ArrayList<>(Arrays.asList("22", "23", "24", "25", "26", "27", "28", "29", "30")));

    // Adding the listeners to the payment text fields
    cardNumberField.addEventHandler(KeyEvent.ANY, event -> checkFields());
    cvvField.addEventHandler(KeyEvent.ANY, event -> checkFields());
  }

  /**
   * This checks if the payment fields are empty. This is not the same as validation, as it's not
   * checking if it's a valid value - only if it is empty.
   */
  public void checkFields() {
    if (cardNumberField.getText().isBlank() || cvvField.getText().isBlank()
        || expiryDateMonthButton.getText().equals("Month")
        || expiryDateYearButton.getText().equals("Year")) {
      payButton.setDisable(true);
    } else {
      payButton.setDisable(false);
    }
  }

  /**
   * This populates any RadioMenuButtons that need RadioMenuItems generating.
   * 
   * @param button - The button to be populated
   * @param items - The items to populate the button with
   */
  public void populateRadioMenuButton(MenuButton button, ArrayList<String> items) {
    ToggleGroup group = new ToggleGroup();
    for (String item : items) {
      RadioMenuItem menuItem = new RadioMenuItem(item);
      menuItem.setOnAction(event -> select(item, button));
      menuItem.setToggleGroup(group);
      button.getItems().add(menuItem);
    }
  }

  /**
   * This is called when a MenuItem is selected. It changes the value of the MenuButton it is part
   * of and also listens for if the buttons contain a value.
   * 
   * @param item
   * @param button
   */
  public void select(String item, MenuButton button) {
    button.setText(item);
    checkFields();
  }

  /**
   * This converts the expiry date String into a Date.
   * 
   * @return A Date representation of the String.
   * @throws ParseException
   */
  public Date convertDate() throws ParseException {
    Date date = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    date = formatter
        .parse("31-" + expiryDateMonthButton.getText() + "-20" + expiryDateYearButton.getText());
    return date;
  }
}
