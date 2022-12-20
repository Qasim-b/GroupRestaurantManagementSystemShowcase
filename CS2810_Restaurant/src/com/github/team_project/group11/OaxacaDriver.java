package com.github.team_project.group11;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class executes functions to change scenes and refresh the order database.
 * 
 * @author Team 11
 */
public class OaxacaDriver extends Application {
  public static Connection connection;
  public static Stage stage;
  public static Scene scene;
  public static ArrayList<String> calllist;
  public static Order selectedOrder;
  public static int selectedItem;
  public static HashMap<Integer, Order> ordermap = new HashMap<Integer, Order>();
  public static String username;
  public static HashMap<String,String> users = new HashMap<String,String>(); 
  public static ArrayList<Order> orderlist = new ArrayList<Order>();
  public static Integer selectedOrderID;


  /**
   * Application startup
   * 
   * @param args the given argument
   */
  public static void main(String[] args) {
    launch(args);

  }

  /**
   * Create the stage and change the scene to the login page
   */
  public void start(Stage primaryStage) throws Exception {
    connection = Database.connect(false);
    stage = primaryStage;
    Parent root = FXMLLoader.load(getClass().getResource("/LoginPage.fxml"));
    Scene scene = new Scene(root, 600, 400);
    String css = this.getClass().getResource("/waiter_style_sheet.css").toExternalForm();
    scene.getStylesheets().add(css);
    stage.setScene(scene);
    stage.show();
  }
  
  /**
   * Changes given scene
   * 
   * @param root - root scene
   * @param css - related CSS stylesheet
   */
  public static void changeScene(Parent root, String css) {
    scene = new Scene(root, 600, 400);
    scene.getStylesheets().add(css);
    stage.setScene(scene);
    stage.show();
  }
  
  /**
   * Gets the current stage
   * 
   * @return stage
   */
  public static Stage getCurrentStage() {
    return stage;
  }
  
  /**
   * Refreshes the orders in the database 
   * 
   * @throws SQLException
   */
  public static void refreshOrderMap() throws SQLException {
	  for(Order iorder : Staff.getOrders(connection)){
		  ordermap.put(iorder.getOrderID(), iorder);
	  }  
  }
}

