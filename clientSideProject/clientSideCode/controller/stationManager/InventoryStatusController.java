package controller.stationManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import emClient.ClientHandlerSingle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;


public class InventoryStatusController implements Initializable{

	public ClientHandlerSingle chs;
	public InventoryStatusController invC;

    @FXML
    private ProgressBar petrolBar;

    @FXML
    private ProgressBar dieselBar;

    @FXML
    private ProgressBar scooterBar;

    @FXML
    private Label txtPPrec;

    @FXML
    private Label txtDieselPrec;

    @FXML
    private Label txtScooterPrec;
    
	private int petrolCap, dieselCap, scooterCap;
    private int petrolInv, dieselInv, scooterInv;
    private int petrolThr, dieselThr, scooterThr;
    
    public InventoryStatusController()
    {    	
		chs = ClientHandlerSingle.getInstance();
		chs.setInvC(this);
		invC = this;

    }
    
    private void inventoryStatus()
    {
    	ArrayList<String> newMsg = new ArrayList<String>();
    	newMsg.add("InventoryStatusRequest");
    	newMsg.add(String.valueOf(chs.employeeNumber));
    	chs.client.handleMessageFromClientUI(newMsg);	
    }

    
    public void handleAnswer(ArrayList<Object> answer)
    {
    	
    	answer.remove(0);
    	answer = (ArrayList<Object>) answer.get(0);
    	for (Object fuel : answer)
    	{
    		//ArrayList<String> tmp = (ArrayList<String>)fuel;
    		switch ((String)(((ArrayList<Object>) fuel).get(0)))
    		{
    			case "Petrol":
    			{
    				setPetrolInv((int)((ArrayList<Object>) fuel).get(1));
    				setPetrolCap((int)((ArrayList<Object>) fuel).get(2));
    				setPetrolThr((int)((ArrayList<Object>) fuel).get(3));
    			}
    			break;
    			case "Diesel":
    			{
    				setDieselInv((int)((ArrayList<Object>) fuel).get(1));
    				setDieselCap((int)((ArrayList<Object>) fuel).get(2));
    				setDieselThr((int)((ArrayList<Object>) fuel).get(3));
    			}
    			break;
    			case "ScooterFuel":
    			{
    				setScooterInv((int)((ArrayList<Object>) fuel).get(1));
    				setScooterCap((int)((ArrayList<Object>) fuel).get(2));
    				setScooterThr((int)((ArrayList<Object>) fuel).get(3));
    			}
    			break;
    			default:
    				break;
    		}
    	}
    	updateBars();
    }
    
    public void setPetrolThr(int petrolThr) {
		this.petrolThr = petrolThr;
	}

	public void setDieselThr(int dieselThr) {
		this.dieselThr = dieselThr;
	}

	public void setScooterThr(int scooterThr) {
		this.scooterThr = scooterThr;
	}

	public void updateBars()
    {
    	Platform.runLater(new Runnable() {
    		@Override
    		public void run()
    		{
    			bar(petrolBar, txtPPrec, petrolInv, petrolCap, petrolThr);
    			bar(dieselBar, txtDieselPrec, dieselInv, dieselCap, dieselThr);
    			bar(scooterBar, txtScooterPrec, scooterInv, scooterCap, scooterThr);
    		}
    		
    		private void bar(ProgressBar pBar, Label lbl, int inv, int cap, int threshold)
    		{
    			double perc = (double)inv/cap;
    			pBar.setProgress(perc);
    			lbl.setText(String.format("%.0f", perc*100) + "%");
    			
    			if (inv < threshold)
    			{
    				pBar.setStyle("-fx-accent: rgb(225, 0, 0);");
    			}
    		}
    	});
    }
        
    public int getPetrolCap() {
		return petrolCap;
	}

	public void setPetrolCap(int petrolCap) {
		this.petrolCap = petrolCap;
	}

	public int getDieselCap() {
		return dieselCap;
	}

	public void setDieselCap(int dieselCap) {
		this.dieselCap = dieselCap;
	}

	public int getScooterCap() {
		return scooterCap;
	}

	public void setScooterCap(int scooterCap) {
		this.scooterCap = scooterCap;
	}

	public int getPetrolInv() {
		return petrolInv;
	}

	public void setPetrolInv(int petrolInv) {
		this.petrolInv = petrolInv;
	}

	public int getDieselInv() {
		return dieselInv;
	}

	public void setDieselInv(int dieselInv) {
		this.dieselInv = dieselInv;
	}

	public int getScooterInv() {
		return scooterInv;
	}

	public void setScooterInv(int scooterInv) {
		this.scooterInv = scooterInv;
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		inventoryStatus();
	}

}
