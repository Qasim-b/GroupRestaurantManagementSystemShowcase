package com.github.team_project.group11;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Controller for the second KitchenGUI scene
 * 
 * @author Moad Sati
 */

public class ViewKitchenOrderController {
	
	private int orderID;

	@FXML
    private Label OrderIDTxt;

    @FXML
    private ListView<String> OrderViewList;
    
    @FXML
    private ListView<String> ReadyListView;

    @FXML
    private Label StatusLabel;

    @FXML
    private Label TableNumTxt;

    @FXML
    private Button backBtn;

    @FXML
    private Button readyBtn;
    
    @FXML
    private Button readyItem;
    
    /**
     * Individually ready items in an order
     * 
     * @param action click button event
     */
    @FXML
    void readyItemPressing(ActionEvent event) {
    	String selected = OrderViewList.getSelectionModel().getSelectedItem();
    	if (selected == null) { 
      	  Alert noSelection = new Alert(AlertType.WARNING);
            noSelection.setContentText("You have not selected an item.");
            noSelection.show();
      } else {
    		OaxacaDriver.ordermap.get(orderID).addReadyItem(selected.split(":")[0]);
    		try {
				OaxacaDriver.ordermap.get(orderID).updateReadyList(OaxacaDriver.connection);
			} catch (SQLException e) {
	    		KitchenGUIDriver.dataBaseWarningAlert();
				e.printStackTrace();
			}
          updateItemList();
    	}
    }
    
    /**
     * Ready whole orders at a time
     * 
     * @param action click button event
     */
    @FXML
    void readyPressing(ActionEvent event) {
    	try {
			OaxacaDriver.ordermap.get(orderID).readyOrder(OaxacaDriver.connection);
			StatusLabel.setText("Ready");
	        OaxacaDriver.ordermap.get(orderID).updateReadyList(OaxacaDriver.connection);
	        OaxacaDriver.refreshOrderMap();
	        updateItemList();
		} catch (SQLException e) {
    		KitchenGUIDriver.dataBaseWarningAlert();
			e.printStackTrace();
		}
      }
    
    /**
     * Switch back to previous kitchen scene
     * 
     * @param action click button event
     */
    @FXML
    void backBtnPressing(ActionEvent event) throws Exception {
   	 Stage stage = OaxacaDriver.stage;
     Parent root = FXMLLoader.load(getClass().getResource("/KitchenGUI.fxml"));
     String css = this.getClass().getResource("/kitchen_stylesheet.css").toExternalForm();
     Scene scene = new Scene(root, 600, 400);
     scene.getStylesheets().add(css);
     stage.setScene(scene);
     stage.show();
    }
   
    /**
     * Initialise data when scene is launched
     */
    @FXML
    void initialize() {
    	try {
			OaxacaDriver.refreshOrderMap();
			orderID = OaxacaDriver.selectedOrderID;
		    OrderIDTxt.setText(Integer.toString(OaxacaDriver.ordermap.get(orderID).getOrderID()));
		    TableNumTxt.setText(Integer.toString(OaxacaDriver.ordermap.get(orderID).getTable()));
		    updateItemList();
		} catch (SQLException e) {
    		KitchenGUIDriver.dataBaseWarningAlert();
			e.printStackTrace();
		}   
    }  

    /**
     * Refresh the item lists view and ready item lists view from the order data
     */
    void updateItemList() {
    	try {
    		OrderViewList.getItems().clear();
        	ReadyListView.getItems().clear();
        	for(String i : OaxacaDriver.ordermap.get(orderID).getItems()) {
        		String itemstring = i + ":" + Menu.getItemNameById(OaxacaDriver.connection, i);
        		if(OaxacaDriver.ordermap.get(orderID).getReadyItems().contains(i)) {
        			ReadyListView.getItems().add(itemstring);
        		}
        		else {
        			OrderViewList.getItems().add(itemstring);
        		}
        	}
        	
        	if (OaxacaDriver.ordermap.get(orderID).orderIsReady()) {
                StatusLabel.setText("Ready");
              } else {
                if (OaxacaDriver.ordermap.get(orderID).getConfirmed()) {
                	StatusLabel.setText("Confirmed");
                } 
              }
    		
    	} catch (SQLException e){
    		KitchenGUIDriver.dataBaseWarningAlert();
			e.printStackTrace();
    	}
    }
}
