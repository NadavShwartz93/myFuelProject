package controller.marketingDepartmentWorker;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Set;

import controller.FuelType;
import emClient.ClientHandlerSingle;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import logic.AnalysisReport;

/**This class is for manage the  performingAnActivityTracking.fxml file.
 * The class make all the required operation for generate the analysis report.
 * @author Nadav Shwartz.
 *
 */
public class performingAnActivityTrackingController implements Initializable {
	
	public ClientHandlerSingle chs;
	public performingAnActivityTrackingController patc;
	
	public performingAnActivityTrackingController(){
		chs = ClientHandlerSingle.getInstance();
		chs.setPatc(this);
		patc = this;
	}
	
	@FXML
    private TableView<AnalysisReport> customerAnalysisReportTable;
    @FXML
    private TableColumn<String, AnalysisReport> customerIdCol;
    @FXML
    private TableColumn<String, AnalysisReport> fuelCol;
    @FXML
    private TableColumn<String, AnalysisReport> customerTypeCol;
    @FXML
    private TableColumn<LocalTime, AnalysisReport> refuelingTimeAvgCol;
    @FXML
    private TableColumn<AnalysisReport, Integer> customerRankCol;
    @FXML
    private TableColumn<AnalysisReport, String> fuelSumCol;
    @FXML
    private Button saveReportBtn;
	@FXML
    private Button getReportBtn;
    @FXML
    private DatePicker reportStartDate;
    @FXML
    private DatePicker reportEndDate;
    
	private ArrayList<AnalysisReport> customerList = null;
	private String fileContent;

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		customerAnalysisReportTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
		
		fuelCol.setCellValueFactory(new PropertyValueFactory<>("fuelTypes"));
		
		customerTypeCol.setCellValueFactory(new PropertyValueFactory<>("customerType"));
		
		refuelingTimeAvgCol.setCellValueFactory(new PropertyValueFactory<>("refuelingTimeAvg"));	    
	    
	    customerRankCol.setCellValueFactory(new PropertyValueFactory<>("customerRank"));   
	    
	    fuelSumCol.setCellValueFactory(new PropertyValueFactory<>("totalPurchaseAmount")); 
	    
	    reportEndDate.setDisable(true);
	    
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
	
    /**This method call the server side, to bring some relevant data.
     */
    private void getDateForAnalysisReport() {
    	ArrayList<Object> answer = new ArrayList<>();
		answer.add("performingAnActivityTracking");
		answer.add("/getActivityData");
		String star = reportStartDate.getValue().toString();
		String end = reportEndDate.getValue().toString();
		answer.add(star);
		answer.add(end);
		
		chs.client.handleMessageFromClientUI(answer);
	}

    /**This method call to {@link #getDateForAnalysisReport()} just 
     * if the {@link #validation()} return true.
     * @param event is the event of press {@link #getReportBtn}.
     */
    @FXML
    void getReportBtn(ActionEvent event) {
    	if(validation()) {
        	getDateForAnalysisReport();
    	}
    }

    /**This method call to {@link #createAndWriteToFile()} that created the report.
     * @param event is the event of press {@link #saveReportBtn}.
     */
    @FXML
    void saveReportBtn(ActionEvent event) {
    	createAndWriteToFile();
    }
    
    /**This method adjust the {@link #reportEndDate} to be limited 
     * between {@link #reportStartDate} value and one week later.
     * @param event is the event of selected start time in {@link #reportStartDate}.
     */
    @FXML
    void initializeEndDate(ActionEvent event) {
    	//Clear the previous selected date
    	reportEndDate.getEditor().clear(); //Clear the datePicker textField
    	reportEndDate.setValue(null); //Set the value of the datePicker to null.
    	
    	LocalDate minDate = reportStartDate.getValue();
    	LocalDate maxDate = reportStartDate.getValue().plusWeeks(1);
    	restrictDatePicker(reportEndDate, minDate, maxDate);
    	reportEndDate.setDisable(false);
    }
    
    
   /**This method restrict the datePicker between the request values: minDate to maxDate.
 * @param datePicker is a DatePicker component.
 * @param minDate the minimum date.
 * @param maxDate the maximum date.
 */
    public void restrictDatePicker(DatePicker datePicker, LocalDate minDate, LocalDate maxDate) {
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                         if (item.isBefore(minDate)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }else if (item.isAfter(maxDate)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        datePicker.setDayCellFactory(dayCellFactory);
    }
    
    
    /**This method get Hashtable from the server side, and insert it to 
     * the customerAnalysisReportTable TableView.
     * @param customerData contain the relevant data that arrived from the server side.
     */
    public void setActivityTrackingData(Hashtable<String, ArrayList<Object>> customerData) {
    	//Clear the previous data.
		customerList = new ArrayList<>();
		
		ObservableList<AnalysisReport> itemsForTable = FXCollections.observableArrayList();
		String totalPurchaseAmount = null;
		String customerId = null;
		LocalTime refuelingTimeAvg;
		Integer customerRank = 0;
		
		ArrayList<String> listOfId = getCustomerId(customerData);
		ArrayList<Object>  tempArray  = new ArrayList<>();
		String fuelTypeStr = "";
		
    	for (int i = 0; i < listOfId.size(); i++) {
    		ArrayList<Object> temp = customerData.get(listOfId.get(i));
    	
    		//Id
    		customerId = listOfId.get(i);
    		
    		//Customer type
    		boolean bool = (boolean) temp.get(0);
    		String customerType = null;
    		if(bool)
    			customerType = "Company Customer";
    		else
    			customerType = "Private Customer";
    		
    		//Customer fuel type
    		tempArray = (ArrayList<Object>) temp.get(1);
    		FuelType fuel;
    		for (int j = 0; j < tempArray.size(); j++) {
    			fuel =FuelType.valueOf(tempArray.get(j).toString());
    			if(j == (tempArray.size() - 1))
    				fuelTypeStr+=String.format("%s",fuel.toString() );
    			else
    				fuelTypeStr+=String.format("%s, ",fuel.toString() );
    			
			}
    		
    		//get the total purchase amount
    		totalPurchaseAmount = String.format("%.1f", (Float) temp.get(2));
    		
    		//Time
    		refuelingTimeAvg = (LocalTime) temp.get(3);
    		
    		//Customer rank
    		customerRank = (Integer) temp.get(4);
    		
    		
    		AnalysisReport ar = new AnalysisReport(customerId, fuelTypeStr, customerType, totalPurchaseAmount, 
       				refuelingTimeAvg, customerRank);	
   		/*itemsForTable.add(new AnalysisReport(customerId, fuelTypeStr, customerType, totalPurchaseAmount, 
   				refuelingTimeAvg, customerRank));*/
    	//itemsForTable.add(ar);
   		
   		//customerList.add(new AnalysisReport(customerId, fuelTypeStr/*, customerFuelType*/, customerType, totalPurchaseAmount, 
   				//refuelingTimeAvg, customerRank));
   		sortAndAddCustomerToList(customerList, ar);
   		
   		fuelTypeStr = "";
		}
    	
    	itemsForTable.addAll(customerList);
    	
		Platform.runLater(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				customerAnalysisReportTable.setItems(itemsForTable);
				
				customerRankCol.setSortType(TableColumn.SortType.DESCENDING);
				customerAnalysisReportTable.getSortOrder().addAll(customerRankCol, fuelSumCol);
				customerAnalysisReportTable.sort();
			}
		});
	}
    
	private void sortAndAddCustomerToList(ArrayList<AnalysisReport> listOfCustomers, AnalysisReport ar) {
		if(listOfCustomers.isEmpty()) {
			listOfCustomers.add(ar);
		}else {
			AnalysisReport temp;
			for (int i = 0; i < listOfCustomers.size(); i++) {
				temp = listOfCustomers.get(i);
				if(temp.getCustomerRank() > ar.getCustomerRank()) {//temp > ar //go on if i+1 < loop size
					if(i+1 >= listOfCustomers.size()) {
						listOfCustomers.add(ar);
						break;
					}
				}if(temp.getCustomerRank() == ar.getCustomerRank()) {//temp == ar  //insert
					Float num = Float.parseFloat(temp.getTotalPurchaseAmount());
					Float numNew = Float.parseFloat(ar.getTotalPurchaseAmount());
					if(num > numNew && i+1 >= listOfCustomers.size()) {
						listOfCustomers.add(ar);
						break;
					}else if(num <= numNew) {
						listOfCustomers.remove(i);
						listOfCustomers.add(i, ar);
						listOfCustomers.add(i+1, temp);
						break;
					}
				}else if(temp.getCustomerRank() < ar.getCustomerRank()) {//temp < ar //insert
					listOfCustomers.remove(i);
					listOfCustomers.add(i, ar);
					listOfCustomers.add(i+1, temp);
					break;
				}
			}
		}
		
	}

	/**This method get HashTable, and return ArrayList that contain all the keys of this HashTable.
	 * @param hashtable
	 * @return
	 */
	private ArrayList<String> getCustomerId(Hashtable<String, ArrayList<Object>> hashtable) {
		Set<String> customersId = hashtable.keySet();
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(customersId);
		return list;
	}
	
	private boolean validation() {
		boolean isInputValid = true;

		if (reportStartDate.getValue() == null)
			isInputValid = false;
		
		if(reportEndDate.getValue() == null)
			isInputValid = false;
		
		return isInputValid;
	}
	

	private void createAndWriteToFile() {

		LocalDate star = reportStartDate.getValue();
		LocalDate end = reportEndDate.getValue();
		fileContent = String.format("The analytic report for date: %s - %s:\n\n"
	      		+ "Customer Id		Customer type		Fuel type						Total Purchase Amount		"
	      		+ "Refueling time(Average)		Customer Rank\n\n", 
	    		  star.toString(), end.toString());		
		
		  String row = "";
		  AnalysisReport ar = null;
		  
		  //Find the row with the biggest amount of different fuel types.
		  int numberOfFuelsTypes = 0;
		  for(int i = 0; i < customerList.size(); i++) {
			  ar = customerList.get(i);
	    	  String []fuelTypes = ar.getFuelTypes().split(", ");
			  if(numberOfFuelsTypes < fuelTypes.length)
				  numberOfFuelsTypes = fuelTypes.length;
		  }
	      for (int i = 0; i < customerList.size(); i++) {
	    	  
	    	  ar = customerList.get(i);
	    	  row = String.format("%s \t\t", ar.getCustomerId());
	    	  row += String.format("%s \t", ar.getCustomerType());
	    	  
	    	  String []fuelTypes = ar.getFuelTypes().split(", ");
	    	  if(fuelTypes.length == 1) {
		    	  row += String.format("%s \t\t\t\t\t\t\t", ar.getFuelTypes());	
		    	  row += String.format("%s \t\t\t\t\t\t", ar.getTotalPurchaseAmount());
		    	  row += String.format("%s \t\t\t\t\t", ar.getRefuelingTimeAvg().toString());
		    	  row += String.format("%d\n", ar.getCustomerRank());

	    	  }
	    	  else if(fuelTypes.length == 2) {
		    	  row += String.format("%s \t\t\t\t\t", ar.getFuelTypes());	
		    	  row += String.format("%s \t\t\t\t\t", ar.getTotalPurchaseAmount());
		    	  row += String.format("%s \t\t\t\t\t", ar.getRefuelingTimeAvg().toString());
		    	  row += String.format("%d\n", ar.getCustomerRank());
	    	  }
	    	  else {
		    	  row += String.format("%s \t\t\t", ar.getFuelTypes());	
		    	  row += String.format("%s \t\t\t\t\t", ar.getTotalPurchaseAmount());
		    	  row += String.format("%s \t\t\t\t\t\t", ar.getRefuelingTimeAvg().toString());
		    	  row += String.format("%d\n", ar.getCustomerRank());
	    	  }
	    	  fileContent+=row;
	    	  row = "";
	      }
	      
			/*try {
				boolean fileCreated = false;
				String  filePath = "src/reports", fileName = "";
				File myObj = null;
				while (fileCreated == false) {
					File dir = new File(filePath);
					dir.mkdirs();
					fileName = String.format("Analytic_Report.txt");
					myObj = new File(dir, fileName);
					if (myObj.createNewFile())
						fileCreated = true;
					else
						myObj.delete();//If file allredy exists then delete.
				}

				FileWriter myWriter = new FileWriter(filePath + "/" + fileName);
				myWriter.write(fileContent);
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
			}*/
	      
	      Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
					boolean fileCreated = false;
					String  filePath = "src/reports", fileName = "";
					File myObj = null;
					while (fileCreated == false) {
						File dir = new File(filePath);
						dir.mkdirs();
						fileName = String.format("Analytic_Report.txt");
						myObj = new File(dir, fileName);
						if (myObj.createNewFile())
							fileCreated = true;
						else
							myObj.delete();//If file allredy exists then delete.
					}

					FileWriter myWriter = new FileWriter(filePath + "/" + fileName);
					myWriter.write(fileContent);
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

	public void noDataReruen(ArrayList<Object> newMsg) {
		ObservableList<AnalysisReport> itemsForTable = FXCollections.observableArrayList();
		customerList.clear();

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				customerAnalysisReportTable.setItems(itemsForTable);
				
				customerRankCol.setSortType(TableColumn.SortType.DESCENDING);
				customerAnalysisReportTable.getSortOrder().add(customerRankCol);
				customerAnalysisReportTable.sort();
			}
		});
		
	}

	

	
	
}
