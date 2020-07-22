package controller.marketingManager;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import emClient.ClientHandlerSingle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import logic.ReportSale;
import logic.Sales;
/**
 * This Class is for the Marketing Manager to generate reports.
 * @author Boaz Trauthwein
 *
 */
public class GenerateReportsController implements Initializable {
	public ClientHandlerSingle chs;
	private ArrayList<Object> salesTable;
	ObservableList<ReportSale> itemsForTable;
	ReportSale selectedSale;
	private String strForFile;

	float totalAmountOfPurchases;

	public GenerateReportsController() {
		chs = ClientHandlerSingle.getInstance();
		chs.setGrc(this);
	}

	@FXML
	private TableView<ReportSale> tblSales;

	@FXML
	private TableColumn<String, Sales> saleNameCol;

	@FXML
	private TableColumn<String, Sales> fuelTypeCol;

	@FXML
	private TableColumn<Float, Sales> discountCol;

	@FXML
	private TableColumn<LocalTime, Sales> startHourCol;

	@FXML
	private TableColumn<LocalTime, Sales> endHourCol;

	@FXML
	private TableColumn<LocalDate, Sales> startDateCol;

	@FXML
	private TableColumn<LocalDate, Sales> endDateCol;

	@FXML
	private Button btnSaleReport;

	@FXML
	private DatePicker dpStart;

	@FXML
	private DatePicker dpEnd;

	@FXML
	private Button btnPeriodicReport;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		saleNameCol.setCellValueFactory(new PropertyValueFactory<>("saleName"));
		fuelTypeCol.setCellValueFactory(new PropertyValueFactory<>("fuelType"));
		discountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
		startHourCol.setCellValueFactory(new PropertyValueFactory<>("startHour"));
		endHourCol.setCellValueFactory(new PropertyValueFactory<>("endHour"));
		startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
		endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));

		restrictDatePicker(dpStart, null, LocalDate.now());
		dpEnd.setDisable(true);

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

		ArrayList<Object> request = new ArrayList<>();
		request.add("marketingManager/GenerateReports");
		request.add("GetAllSales");

		chs.client.handleMessageFromClientUI(request);
	}

	/**
	 * This function is called to build the table for Reports on Sale.
	 * 
	 * @param answer - Table returned from Server
	 */
	public void setReportSalesInTableInitialize(ArrayList<Object> answer) {
		ArrayList<Object> table = (ArrayList<Object>) answer.get(1);
		itemsForTable = FXCollections.observableArrayList();
		ArrayList<Object> row;

		for (int i = 0; i < table.size(); i++) {

			int j = 0;
			row = (ArrayList<Object>) table.get(i);
			if (row.get(7) != null) {
				String saleName = String.valueOf(row.get(j++));
				String fuelType = String.valueOf(row.get(j++));
				Float disscount = Float.valueOf(String.valueOf(row.get(j++)));
				LocalTime startHour = LocalTime.parse(String.valueOf(row.get(j++)));
				LocalTime endHour = LocalTime.parse(String.valueOf(row.get(j++)));
				j += 2;
				LocalDate startDate = LocalDate.parse(String.valueOf(row.get(j++)));
				LocalDate endDate;
				if (row.get(j) == null)
					endDate = LocalDate.now();
				else
					endDate = LocalDate.parse(String.valueOf(row.get(j)));
				itemsForTable
						.add(new ReportSale(saleName, fuelType, disscount, startHour, endHour, startDate, endDate));
			}
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				tblSales.setItems(itemsForTable);
			}
		});

	}

	/**
	 * This function is called when "Generate Sale Report" is pressed, to retrieve
	 * from Server the Data for the sale report
	 * 
	 * @param event - ActionEvent
	 */
	@FXML
	void GenerateSaleReport(ActionEvent event) {
		selectedSale = tblSales.getSelectionModel().getSelectedItem();
		if (selectedSale != null) {
			ArrayList<Object> request = new ArrayList<>();
			request.add("marketingManager/GenerateReports");
			request.add("GenerateSaleReport");
			request.add(selectedSale.getFuelType());
			request.add(selectedSale.getStartDate());
			request.add(selectedSale.getEndDate());
			request.add(selectedSale.getStartHour());
			request.add(selectedSale.getEndHour());

			chs.client.handleMessageFromClientUI(request);
		}
	}

	/**
	 * The function is called after returning from server to generate the sales
	 * report as a file for the user.
	 * 
	 * @param answer - Table with Sales Data to generate the report
	 */
	public void generateSaleReportInFile(ArrayList<Object> answer) {
		String strTemp;
		ArrayList<Object> table = (ArrayList<Object>) answer.get(1);
		strForFile = "";
		strForFile += "Sale Report:\n\n";
		strForFile += String.format("Sale Name:\t%s\n", selectedSale.getSaleName());
		strForFile += String.format("Fuel Type:\t%s\n", selectedSale.getFuelType());
		strForFile += String.format("Start Time:\t%s\n", selectedSale.getStartHour());
		strForFile += String.format("End Time:\t%s\n", selectedSale.getEndHour());
		strForFile += String.format("Start Date:\t%s\n", selectedSale.getStartDate());
		strForFile += String.format("End Date:\t%s\n", selectedSale.getEndDate());
		strForFile += "\n\n";
		strForFile += String.format("Number of buying Customers:\t%s\n", table.size());

		strTemp = "";
		float dollar = 0;
		for (int i = 0; i < table.size(); i++) {
			ArrayList<Object> row = (ArrayList<Object>) table.get(i);
			strTemp += String.format("%s\t%s\n", String.valueOf(row.get(0)), String.valueOf(row.get(1)));
			dollar += Float.parseFloat(String.valueOf(row.get(1)));
		}
		strForFile += String.format("Total Amount of purchases:\t%s\n\n", dollar);
		ArrayList<String> arrCustomerID = new ArrayList<String>();
		strForFile += "Sum of purchases for every customer:\nCustomer Id:\tAmount:\n";
		strForFile += strTemp;

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
				filename = selectedSale.getSaleName() + "_Report.txt";
				myObj = new File(dir, filename);
				if (myObj.createNewFile())
					fileCreated = true;
				else
					myObj.delete();// If file allredy exists then delete.
			}

			FileWriter myWriter = new FileWriter(filePath + "/" + filename);
			myWriter.write(strForFile);
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

	/**
	 * This function is called when "Generate Periodic Report" is pressed, to
	 * retrieve from Server the Data for the periodic report.
	 * 
	 * @param event - ActionEvent
	 */
	@FXML
	void GeneratePeriodicReport(ActionEvent event) {
		if (dpStart.getValue() != null && dpEnd.getValue() != null) {
			ArrayList<Object> request = new ArrayList<>();
			request.add("marketingManager/GenerateReports");
			request.add("GeneratePeriodicReport");
			request.add(dpStart.getValue());
			request.add(dpEnd.getValue());
			chs.client.handleMessageFromClientUI(request);
		}
	}

	@FXML
	void initializeEndDate(ActionEvent event) {
		// Clear the previous selected date
		dpEnd.getEditor().clear(); // Clear the datePicker textField
		dpEnd.setValue(null); // Set the value of the datePicker to null.

		LocalDate minDate = dpStart.getValue();
		restrictDatePicker(dpEnd, minDate, LocalDate.now());
		dpEnd.setDisable(false);
	}

	/**
	 * Blocks the datePicker between minDate - maxDate
	 * 
	 * @param datePicker - DatePicker in the fxml file
	 * @param minDate    - minimal date.
	 * @param maxDate - maximal date.
	 */

	public void restrictDatePicker(DatePicker datePicker, LocalDate minDate, LocalDate maxDate) {
		final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						if (minDate != null && item.isBefore(minDate)) {
							setDisable(true);
							setStyle("-fx-background-color: #ffc0cb;");
						} else if (item.isAfter(maxDate)) {
							setDisable(true);
							setStyle("-fx-background-color: #ffc0cb;");
						}
					}
				};
			}
		};
		datePicker.setDayCellFactory(dayCellFactory);
	}

	/**
	 * The function is called after returning from server to generate the periodic
	 * report as a file for the user.
	 * 
	 * @param answer - Table with Sales Data to generate the report
	 */
	public void generatePeriodicReportInFile(ArrayList<Object> answer) {
		String strForFile, strTemp;
		ArrayList<Object> table = (ArrayList<Object>) answer.get(1);
		strForFile = "";
		strForFile += "Periodic Report Report: " + dpStart.getValue().toString() + " - " + dpEnd.getValue().toString()
				+ "\n";

		ArrayList<String> gasCompanyNames = new ArrayList<String>();
		ArrayList<String> customers;
		ArrayList<Float> totalPurches;
		int idx = 0;
		for (int i = 0; i < table.size(); i++) {
			ArrayList<Object> row = (ArrayList<Object>) table.get(i);
			if (!gasCompanyNames.contains(String.valueOf(row.get(0)))) {
				gasCompanyNames.add(String.valueOf(row.get(0)));
				customers = new ArrayList<String>();
				totalPurches = new ArrayList<Float>();
				// Arranging data loop
				for (int j = 0; j < table.size(); j++) {
					ArrayList<Object> row2 = (ArrayList<Object>) table.get(j);
					if (gasCompanyNames.get(gasCompanyNames.size() - 1).compareTo(String.valueOf(row2.get(0))) == 0) {
						if (customers.contains(String.valueOf(row2.get(1)))) {
							idx = customers.indexOf(String.valueOf(row2.get(1)));
							totalPurches.set(idx,
									totalPurches.get(idx) + Float.parseFloat(String.valueOf(row2.get(2))));
						} else {
							customers.add(new String(String.valueOf(row2.get(1))));
							totalPurches.add(Float.parseFloat(String.valueOf(row2.get(2))));
						}
						table.remove(row2);
					}
				}
				// Printing loop
				float sum = 0;
				for (float purchase : totalPurches)
					sum += purchase;
				strForFile += "\n" + gasCompanyNames.get(gasCompanyNames.size() - 1) + " (Sum Purchesed: " + sum
						+ " $)";
				strForFile += "\n\tCustomer ID\tTotal Purchased($)";
				for (int j = 0; j < customers.size(); j++) {
					strForFile += "\n\t" + customers.get(j) + "\t" + totalPurches.get(j);
				}
				strForFile += "\n";
			}
		}

		try {
			boolean fileCreated = false;
			String filename = "", filePath = "src/reports";
			File myObj = null;
			while (fileCreated == false) {
				File dir = new File(filePath);
				dir.mkdirs();
				filename = "Periodic_Report.txt";
				myObj = new File(dir, filename);
				if (myObj.createNewFile())
					fileCreated = true;
				else
					myObj.delete();// If file allredy exists then delete.
			}

			FileWriter myWriter = new FileWriter(filePath + "/" + filename);
			myWriter.write(strForFile);
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

}
