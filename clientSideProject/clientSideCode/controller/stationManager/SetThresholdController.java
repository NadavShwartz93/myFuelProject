package controller.stationManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import emClient.ClientHandlerSingle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;


public class SetThresholdController  implements Initializable{
	
	public ClientHandlerSingle chs;
	public SetThresholdController sthc;

	@FXML
	private ChoiceBox<String> cbFuelType;

	@FXML
	private TextField txtThreshold;

	@FXML
	private Button btnSend;
	
    @FXML
    private Label txtNotification;
	
	public SetThresholdController()
	{
		chs = ClientHandlerSingle.getInstance();
		chs.setSthc(this);
		sthc = this;
		
	}
	

    @FXML
    void updateThreshold(MouseEvent event) {

    	String fuel = cbFuelType.getValue();
    	
    	if (fuel == "Please choose fuel type")
    	{
    		txtNotification.setText("Please choose a fuel type.");
    		return;
    	}
    	
		System.out.println(fuel);
    	String amount = txtThreshold.getText();
    	
    	if (isNumeric(amount))
    	{
        	ArrayList<String> newMsg = new ArrayList<String>();
        	newMsg.add("UpdateThresholdQuery");
        	newMsg.add(amount);
        	newMsg.add(fuel);
        	newMsg.add(String.valueOf(chs.employeeNumber));
        	chs.client.handleMessageFromClientUI(newMsg);
        	
        	String tmp = "Threshold for " + fuel + " was successfully set on " + amount + ".";
        	txtNotification.setText(tmp);
    	}
    }
    
    public static boolean isNumeric(String amount) {
        if (amount == null) {
            return false;
        }
        try {
        	Double d = Double.parseDouble(amount);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cbFuelType.getItems().addAll(FXCollections.observableArrayList("Please choose fuel type", "Petrol", "Diesel", "Scooter", "Home Fuel"));
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				cbFuelType.setValue("Please choose fuel type");
			}	
		});
	}

}
