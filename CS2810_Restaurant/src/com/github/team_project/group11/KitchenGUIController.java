package com.github.team_project.group11;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * This class executes the controller function for all kitchen GUI scenes. 
 * 
 * @author Moad Sati
 */
public class KitchenGUIController {

    @FXML
    private ListView<Integer> confirmedOrderList;

    @FXML
    private ListView<Integer> readyList;

    @FXML
    private Button readyToServeBtn;

    @FXML
    private Button refreshBtn;

    @FXML
    private Button selectOrderBtn;

    @FXML
    private Button viewBtn;
    
    @FXML
    private Button logoutbutton;
    
    /**
     * Function to logout from the GUI.
     *
     *@throws IOException
     */
    @FXML
    void logoutPressing(ActionEvent event) throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource("/LoginPage.fxml"));
        String css = this.getClass().getResource("/customer_opening_page.css").toExternalForm();
        OaxacaDriver.changeScene(root, css);
    }
    
    /**
     * Readies the whole order
     * 
     * @param action click button event
     */
    @FXML
    void readyPressing(ActionEvent event) {
    	if (confirmedOrderList.getSelectionModel().getSelectedItem() == null) {
    	      Alert noSelection = new Alert(AlertType.WARNING);
    	      noSelection.setContentText("You have not selected an order.");
    	      noSelection.show();
    	      
   
    	}
    	else {
    		int orderID = confirmedOrderList.getSelectionModel().getSelectedItem();
    		try {
				OaxacaDriver.ordermap.get(orderID).readyOrder(OaxacaDriver.connection);
				refresh();
			} catch (SQLException e) {
	    		KitchenGUIDriver.dataBaseWarningAlert();
				e.printStackTrace();
			}		
    	}
    }
   
    /**
     * Refresh data information
     * 
     * @param action click button event
     */
    @FXML
    void refreshPressing(ActionEvent event) {
    	try {
			OaxacaDriver.refreshOrderMap();
			refresh();
		} catch (SQLException e) {
    		KitchenGUIDriver.dataBaseWarningAlert();
			e.printStackTrace();
		}
    }

    @FXML
    void selectPressing(ActionEvent event) throws IOException {
    }
    
    /**
     * View orders in detail
     * 
     * @param action click button event
     * @throws Exception
     */
    @FXML
    void viewPressing(ActionEvent event) throws Exception {
    	if (confirmedOrderList.getSelectionModel().getSelectedItem() == null) {
  	      Alert noSelection = new Alert(AlertType.WARNING);
  	      noSelection.setContentText("You have not selected an order.");
  	      noSelection.show();
  	    } else {
  	    	OaxacaDriver.selectedOrderID = confirmedOrderList.getSelectionModel().getSelectedItem();
  	      Stage stage = OaxacaDriver.stage;
  	      Parent root = FXMLLoader.load(getClass().getResource("/ViewKitchenOrder.fxml"));
  	      Scene scene = new Scene(root, 600, 400);
  	      scene.getStylesheets().add("/kitchen_stylesheet.css");
  	      stage.setScene(scene);
  	      stage.show();
  	    }
      }
    
    /**
     * Function that launches the scene
     */
    @FXML
    void initialize() {
      refresh();
    }
    
    /**
     * Function that refreshes orders and values in the scenes.
     */
    @FXML
    void refresh() {
    	try {
    		OaxacaDriver.refreshOrderMap();
        	confirmedOrderList.getItems().clear();
        	readyList.getItems().clear();
        	for(Order i : OaxacaDriver.ordermap.values()) {
        		if (i.getCompleted()) {
        			continue;
        		}
        		else if (i.orderIsReady()) {
        			i.ready(OaxacaDriver.connection, i.getOrderID());
        			readyList.getItems().add(i.getOrderID());
        		}
        		else if (i.getConfirmed() == true) {
        			confirmedOrderList.getItems().add(i.getOrderID());			
        		}
        	}	
    	} catch (SQLException e) {
    		KitchenGUIDriver.dataBaseWarningAlert();
    		e.printStackTrace();
    	}
    }
}
