package controller.stationManager;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.ResourceBundle;

//import com.sun.org.apache.xerces.internal.parsers.IntegratedParserConfiguration;

import emClient.ClientHandlerSingle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

public class GenerateQuarterlyReportsController implements Initializable{
	
	public ClientHandlerSingle chs;
	public GenerateQuarterlyReportsController gqr;
	private ToggleGroup radioGroup;
	private String gasCompany, gasStation, stationManager;


    @FXML
    private RadioButton rbQuarterlyRevenueReport;

    @FXML
    private RadioButton rbPurchasesReport;

    @FXML
    private RadioButton rbInventoryReport;

    @FXML
    private Button btnGenerate;
    
    public GenerateQuarterlyReportsController()
    {
		chs = ClientHandlerSingle.getInstance();
		chs.setGqr(this);
		gqr = this;
    }
    
    
    @FXML
    void generateReport(MouseEvent event) {

    	Toggle selected = radioGroup.getSelectedToggle();
    	
    	if ((RadioButton)selected == rbInventoryReport)
    	{
    		sendRequest("InventoryReportRequest");
    	}
    	else if ((RadioButton)selected == rbPurchasesReport)
    	{
    		sendRequest("gPurchasesReport");
    	}
    	else if ((RadioButton)selected == rbQuarterlyRevenueReport)
    	{
    		sendRequest("gQuarterlyRevenueReport");
    	}
    	else 
    	{
    		
    	}
    }
    
	/**
	 * The function is called after returning from server to create the report file and open it for the user.
     * @param answer - The report.
     * @param reportName - Report name.
     */

    public void makeReport(ArrayList<Object> answer, String reportName)
    {
    	System.out.println("Got the report");
    	Platform.runLater(new Runnable() {
			@Override
			public void run() {
		try {
			boolean fileCreated = false;
			String filename = "", filePath = "src/reports";
			File myObj = null;
			while (fileCreated == false) {
				File dir = new File(filePath);
				dir.mkdirs();
				filename = reportName + ".txt";
				myObj = new File(dir, filename);
				if (myObj.createNewFile())
					fileCreated = true;
				else
					myObj.delete();// If file already exists then delete.
			}

			FileWriter myWriter = new FileWriter(filePath + "/" + filename);
			myWriter.write((String)answer.get(2));
			myWriter.close();

			// constructor of file class having file as argument
			if (!Desktop.isDesktopSupported())// check if Desktop is supported by Platform or not
			{
				System.out.println("not supported");
				return;
			}
			Desktop desktop = Desktop.getDesktop();
			if (myObj.exists()) // checks file exists or not
				desktop.open(myObj); // opens the specified file
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
				}
		});
    }
    
    private void sendRequest(String report)
    {
    	ArrayList<String> newMsg = new ArrayList<String>();
    	newMsg.add(report);
    	newMsg.add(String.valueOf(chs.employeeNumber));
    	chs.client.handleMessageFromClientUI(newMsg);	
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		radioGroup = new ToggleGroup();
		
    	rbInventoryReport.setToggleGroup(radioGroup);
    	rbPurchasesReport.setToggleGroup(radioGroup);
    	rbQuarterlyRevenueReport.setToggleGroup(radioGroup);
    	
    	ArrayList<String> newMsg = new ArrayList<String>();
    	newMsg.add("initGenerateReport");
    	newMsg.add(String.valueOf(chs.employeeNumber));
    	chs.client.handleMessageFromClientUI(newMsg);	
    	
    	Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Delete all files in directory.
				File dir = new File("src/reports");
				for (File file : dir.listFiles())
				if (!file.isDirectory())
				file.delete();
				}
		});
		
	}

}
