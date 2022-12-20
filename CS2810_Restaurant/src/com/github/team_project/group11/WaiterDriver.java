package com.github.team_project.group11;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * Starts the waiter GUI scenes
 * 
 * @author Rory Thomson-Bird
 */
public class WaiterDriver extends Application {

  public static Stage stage;  
  public static ArrayList<String> itemlist;
  public static ArrayList<String> itemlist2;
  public static ArrayList<Order> orderlist = new ArrayList<Order>();
  public static ArrayList<String> readylist = new ArrayList<String>();
  public static ArrayList<String> calllist;
  public static Order order1;
  public static Order order2;
  public static Order selectedOrder;
  public static int selectedItem;
  public static HashMap<Integer, Order> ordermap = new HashMap<Integer, Order>();
  public static Connection connection;
  public static String username;
  public static HashMap<String,String> users = new HashMap<String,String>(); 

  /**
   * Waiter GUI startup
   * 
   * @param args the given argument
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Create the stage and change the scene to the Waiter GUI
   * 
   * @throws Exception
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
}