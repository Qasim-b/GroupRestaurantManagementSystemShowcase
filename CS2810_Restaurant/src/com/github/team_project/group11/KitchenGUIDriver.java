package com.github.team_project.group11;


import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * This class launches the kitchen GUI scenes.
 * 
 * @author Moad Sati
 */
public class KitchenGUIDriver extends Application {
	public static Integer selectedOrder;
	public static HashMap<Integer, Order> ordermap = new HashMap<Integer, Order>(); 
	public static Stage stage;

	/**
	 * Starts up the GUI.
	 *
	 * @param Main String arguments to launch the GUI.
	 */
	public static void main(String[] args) {
    launch(args);
	}
  
	public static void refreshOrderMap() {
		try {
			for(Order iorder : Staff.getOrders(OaxacaDriver.connection)){
			ordermap.put(iorder.getOrderID(), iorder);
			}
		} 
		catch (SQLException e) {
			dataBaseWarningAlert();
			e.printStackTrace();
		}	  
	}
  
	/**
	* Shows a database error alert.
	*
	*/
	static void dataBaseWarningAlert() {
		Alert dataBaseError = new Alert(AlertType.ERROR);
		dataBaseError.setContentText("Database Error, please check the stack trace.");
		dataBaseError.show();
	}

	/**
	* Displays the GUI.
	* @throws IOException 
	*/
	@Override
	public void start(Stage primaryStage) throws IOException {
		stage = primaryStage;
	    Parent root = FXMLLoader.load(getClass().getResource("/KitchenGUI.fxml"));
	    Scene scene = new Scene(root, 600, 400);
	    String css = this.getClass().getResource("/kitchen_stylesheet.css").toExternalForm();
	    scene.getStylesheets().add(css);
	    primaryStage.setScene(scene);
	    primaryStage.show();   
	}
}
