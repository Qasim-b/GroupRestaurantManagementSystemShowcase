package com.github.team_project.group11;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 * This class controls the item page.
 * 
 * @author Rory Thomson-Bird
 *
 */
public class ItemController {

  @FXML
  private Label CategoryLabel;

  @FXML
  private Label calorieLabel;
  
  @FXML
  private Label IDLabel;

  @FXML
  private ListView<String> IngList;

  @FXML
  private Label VeganLabel;

  @FXML
  private Label VegetarianLabel;

  @FXML
  private Button backButton;

  @FXML
  private Label costLabel;

  @FXML
  private Label nameLabel;

  /**
   * Starts up the page
   * 
   * @throws SQLException throws exception
   */
  @FXML
  void initialize() throws SQLException {
    refresh();
  }

  /**
   * Refresh the scene to give the current values in the database
   * 
   * @throws SQLException
   */
  void refresh() throws SQLException {
    IngList.getItems().clear();
    ResultSet rs = Database.executeQuery(OaxacaDriver.connection,
        "SELECT * FROM menu WHERE id = " + Integer.toString(OaxacaDriver.selectedItem));
    rs.next();
    if (rs.getString("vegetarian").equals("t")) {VegetarianLabel.setText("Yes");}
    else {VegetarianLabel.setText("No");}
    if (rs.getString("vegan").equals("t")) {VeganLabel.setText("Yes");}
    else {VeganLabel.setText("No");}
    if(rs.getString("type").equals("f")) {
      CategoryLabel.setText("Food");
    }
    else {
      CategoryLabel.setText("Drink");
    }
    IDLabel.setText(rs.getString("id"));
    nameLabel.setText(rs.getString("name"));
    costLabel.setText(rs.getString("price"));
    calorieLabel.setText(rs.getString("calories"));
    String ingredients[] =
        rs.getString("ingredients").substring(1, rs.getString("ingredients").length() - 1).split(",");
    for (String str : ingredients) {
      IngList.getItems().add(str);
    }
  }

  /**
   * Brings user back to the previous scene
   * 
   * @param event button click
   * @throws IOException throws exception
   */
  @FXML
  void backPressed(ActionEvent event) throws IOException {
    IngList.getItems().clear();
    Parent root = FXMLLoader.load(getClass().getResource("/menuPage.fxml"));
    String css = this.getClass().getResource("/waiter_style_sheet.css").toExternalForm();
    OaxacaDriver.changeScene(root, css);
  }
}
