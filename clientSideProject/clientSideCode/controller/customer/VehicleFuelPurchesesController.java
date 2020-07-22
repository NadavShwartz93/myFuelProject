package controller.customer;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import controller.FuelType;
import emClient.ClientHandlerSingle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.Media;
import logic.vehicleAllPurchases;
import logic.vehicleMonthlyPayments;

/**This class is for manage the VehicleFuelPurcheses.fxml file. 
 * the class is manage all the process of bring and insert data to the pair of table
 * in this window. This window have no interaction with the customer.
 * @author Nadav Shwartz.
 */
public class VehicleFuelPurchesesController implements Initializable {
	
	public ClientHandlerSingle chs;
	public VehicleFuelPurchesesController vfpc;
	
	public VehicleFuelPurchesesController() {
		chs = ClientHandlerSingle.getInstance();
		chs.setVfpc(this);
		vfpc = this;
	}
	


	@FXML
    private TableView<vehicleAllPurchases> allPurchasesTableView;
    @FXML
    private TableColumn<vehicleAllPurchases, String> CarLIcenseNumberCol;
    @FXML
    private TableColumn<vehicleAllPurchases, String> GasStationNameCol;
    @FXML
    private TableColumn<vehicleAllPurchases, FuelType> FuelTypeCol;
    @FXML
    private TableColumn<vehicleAllPurchases, Float> FuelAmountCol;
    @FXML
    private TableColumn<vehicleAllPurchases, Float> paymentCol;
    @FXML
    private TableColumn<vehicleAllPurchases, LocalDate> dateCol;
    @FXML
    private TableColumn<vehicleAllPurchases, LocalTime> timeCol;
    
    @FXML
    private TableView<vehicleMonthlyPayments> monthlyPaymentsTableView;
    @FXML
    private TableColumn<vehicleMonthlyPayments, Integer> yearCol;
    @FXML
    private TableColumn<vehicleMonthlyPayments, Integer> monthCol;
    @FXML
    private TableColumn<vehicleMonthlyPayments, Float> monthlyPaymentCol;
    @FXML
    private Label lblPayment;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//First table:
		allPurchasesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		CarLIcenseNumberCol.setCellValueFactory(new PropertyValueFactory<>("carLIcenseNumber"));
		GasStationNameCol.setCellValueFactory(new PropertyValueFactory<>("gasStationName"));
		FuelTypeCol.setCellValueFactory(new PropertyValueFactory<>("fuelType"));
		FuelAmountCol.setCellValueFactory(new PropertyValueFactory<>("fuelAmount"));
		paymentCol.setCellValueFactory(new PropertyValueFactory<>("payment"));
		dateCol.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
		timeCol.setCellValueFactory(new PropertyValueFactory<>("purchaseTime"));
		
		//Second table:
		monthlyPaymentsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
		monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
		monthlyPaymentCol.setCellValueFactory(new PropertyValueFactory<>("totalMonthlyPayment"));
		
		getDataFromServer();
	}
	
	/**This method call the server side to bring all the relevant data.
	 */
	private void getDataFromServer(){
		ArrayList<Object> answer = new ArrayList<>(); 
		answer.add("vehicleFuelPurchasesData");
		answer.add("/getPurchasesData");
		answer.add(chs.customerid);
		
		chs.client.handleMessageFromClientUI(answer);
	
	}
	
	/**This method get relevant data from the server side.
	 * @param message contain all the relevant data that arrived from the server side.
	 */
	@SuppressWarnings("unchecked")
	public void setDataInTabe(ArrayList<Object> message) {
		if(message.size() == 1) {
			System.out.println(message.get(0).toString());
		}else {
			String customerModelString = message.get(0).toString();
			setDataInAllPurchasesTable((ArrayList<ArrayList<Object>>) message.get(1));
			if(!customerModelString.equals("NormalFueling"))
				setDataInMonthlyPaymentsTable((ArrayList<ArrayList<Object>>) message.get(2));
			else {
				monthlyPaymentsTableView.setDisable(true);
				lblPayment.setVisible(false);
			}

			
		}
		
		
	}
		
	/**This method get relevant data for TableView, insert it to the TableView, 
	 * and display it on the window.
	 * @param message contain all the relevant data for the allPurchasesTableView TableView
	 */
	private void setDataInAllPurchasesTable(ArrayList<ArrayList<Object>> message) {
		ObservableList<vehicleAllPurchases> itemsForTable = FXCollections.observableArrayList();
		
		
		ArrayList<Object> temp = new ArrayList<>();
		for (int i = 0; i < message.size(); i++) {
			temp = message.get(i);
			
			String carLIcenseNumber = temp.get(0).toString();
		   String gasStationName = temp.get(1).toString();
		   FuelType fuelType = FuelType.valueOf(temp.get(2).toString());
		   Float fuelAmount = Float.parseFloat(temp.get(3).toString());
		   
		   String t = String.format("%.1f", Float.parseFloat(temp.get(4).toString()));
		   Float payment = Float.parseFloat(t);
		   
		   LocalDate purchaseDate = LocalDate.parse(temp.get(5).toString());
		   LocalTime purchaseTime = LocalTime.parse(temp.get(6).toString());
			
		   itemsForTable.add(new vehicleAllPurchases(carLIcenseNumber, gasStationName, fuelType, 
				   fuelAmount, payment, purchaseDate, purchaseTime));
		}
		Platform.runLater(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				allPurchasesTableView.setItems(itemsForTable);
				
				dateCol.setSortType(TableColumn.SortType.DESCENDING);
				timeCol.setSortType(TableColumn.SortType.DESCENDING);
				allPurchasesTableView.getSortOrder().addAll(dateCol, timeCol);
				allPurchasesTableView.sort();
			}
		});
	}
	
	/**This method get relevant data for TableView, insert it to the TableView, 
	 * and display it on the window.
	 * @param message contain all the relevant data for the monthlyPaymentsTableView TableView
	 */
	private void setDataInMonthlyPaymentsTable(ArrayList<ArrayList<Object>> message) {
		ObservableList<vehicleMonthlyPayments> itemsForTable = FXCollections.observableArrayList();
		
		ArrayList<Object> temp = new ArrayList<>();
		for (int i = 0; i < message.size(); i++) {
			temp = message.get(i);
			
			Integer year = (Integer) temp.get(0);
			Integer month = (Integer) temp.get(1);
			
			String t = String.format("%.1f", Float.parseFloat(temp.get(2).toString()));
			Float totalMonthlyPayments = Float.parseFloat(t);
			
			
			
		    itemsForTable.add(new vehicleMonthlyPayments(year, month, totalMonthlyPayments));
		    
		}
		
		
		Platform.runLater(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				monthlyPaymentsTableView.setItems(itemsForTable);
				
				yearCol.setSortType(TableColumn.SortType.DESCENDING);
				monthCol.setSortType(TableColumn.SortType.DESCENDING);
				monthlyPaymentsTableView.getSortOrder().addAll(yearCol, monthCol);
				monthlyPaymentsTableView.sort();
				
			}
		});
		
		//Update the payment label
		Integer year = LocalDate.now().getYear();
		Integer month = LocalDate.now().getMonthValue();
		int flag = 0;
		for (int i = 0; i < message.size(); i++) {
			temp = message.get(i);

			if(year.equals(temp.get(0))&& month.equals(temp.get(1))) {
				flag = 1;
				updatePaymentLabel((Float) temp.get(2));
				break;
			}
			
		}
		if(flag == 0) {
			Float t = (float) 0;
			updatePaymentLabel(t);

		}
		
	}
	
	/**This method update the lblPayment Label, about the total payment 
	 * cost of the customer vehicle purchases.
	 * @param totalPayment is the number of the month total payment cost of the customer purchases.
	 */
	private void updatePaymentLabel(Float totalPayment) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				String temp = lblPayment.getText().toString();
				temp +=String.format(" %.1f", totalPayment);
				lblPayment.setText(temp);
			}
		});
	}

}
