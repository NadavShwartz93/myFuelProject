package controller.chainManager;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import emClient.ClientHandlerSingle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import logic.QuarterlyReports;
import logic.ReportSale;
import logic.Sales;

public class WatchingQuarterlyReportsController implements Initializable {
	public ClientHandlerSingle chs;
	private ArrayList<Object> salesTable;
	private java.util.Map<String, ArrayList<String>> mapGasStations;
	ObservableList<Sales> itemsForTable;

	public WatchingQuarterlyReportsController() {
		chs = ClientHandlerSingle.getInstance();
		chs.setWqrc(this);
	}

	@FXML
	private ComboBox<String> cbReportType;

	@FXML
	private ComboBox<String> cbGasStationCompany;

	@FXML
	private ComboBox<String> cbGasStationName;

	@FXML
	private Button btnFind;

	@FXML
	private TableView<QuarterlyReports> tblReports;

	@FXML
	private TableColumn<String, QuarterlyReports> reportTypeCol;

	@FXML
	private TableColumn<String, QuarterlyReports> gasStationCompanyCol;

	@FXML
	private TableColumn<String, QuarterlyReports> gasStationNameCol;

	@FXML
	private TableColumn<Integer, QuarterlyReports> yearCol;

	@FXML
	private TableColumn<Integer, QuarterlyReports> quarterNumberCol;

	@FXML
	private Button btnViewReport;

	@FXML
	private Label lblErr;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		reportTypeCol.setCellValueFactory(new PropertyValueFactory<>("reportType"));
		gasStationCompanyCol.setCellValueFactory(new PropertyValueFactory<>("gasStationCompany"));
		gasStationNameCol.setCellValueFactory(new PropertyValueFactory<>("gasStationName"));
		yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
		quarterNumberCol.setCellValueFactory(new PropertyValueFactory<>("quarterNumber"));

		lblErr.setText("");
		
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

		cbReportType.getItems().addAll("Quarterly Revenue", "Purchases", "Inventory");

		ArrayList<Object> request = new ArrayList<>();
		request.add("chainManager/GetDetailsForQuarterlyReports");

		chs.client.handleMessageFromClientUI(request);

		mapGasStations = new HashMap<String, ArrayList<String>>();
		cbGasStationName.setTooltip(new Tooltip("You have to choose first gas station company"));
	}

	/**
	 * This function is called to set values for Gas Station Names
	 * 
	 * @param event - ActionEvent
	 */
	@FXML
	void ChooseGasStationCompany(ActionEvent event) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				cbGasStationName.getItems().clear();
				cbGasStationName.getItems().addAll(mapGasStations.get(cbGasStationCompany.getValue().toString()));
			}
		});

	}

	@FXML
	void ChooseGasStationName(ActionEvent event) {

	}

	@FXML
	void ChooseReportType(ActionEvent event) {

	}
	/**
	 * This function is called after clicking the btnFind button to to get report details in table.
	 * @param event ActionEvent
	 */
	@FXML
	void FindRequest(ActionEvent event) {
		String reportType, company, name, error = null;
		reportType = cbReportType.getValue();
		company = cbGasStationCompany.getValue();
		name = cbGasStationName.getValue();
		if (reportType == null)
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblErr.setText("Choose report type");
				}
			});
		else if (company == null)
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblErr.setText("Choose gas station company");
				}
			});
		else if (name == null)
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblErr.setText("Choose gas station name");
				}
			});
		else {
			if (reportType.equals("Quarterly Revenue"))
				reportType = "revenue_report";
			else if (reportType.equals("Purchases"))
				reportType = "purchased_report";
			else if (reportType.equals("Inventory"))
				reportType = "inventory_report";

			ArrayList<Object> request = new ArrayList<>();
			request.add("chainManager/GetDataForQuarterlyReportsTable");
			request.add(reportType);
			request.add(company);
			request.add(name);

			chs.client.handleMessageFromClientUI(request);

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblErr.setText("");
				}
			});
		}

	}
	/**
	 * This function is called after clicking the btnViewReport button to see the Report.
	 * @param event - ActionEvent
	 */
	@FXML
	void ViewReport(ActionEvent event) {
		QuarterlyReports selectedReport = tblReports.getSelectionModel().getSelectedItem();
		if (selectedReport != null) {
			ArrayList<Object> request = new ArrayList<>();
			request.add("chainManager/GetSpecificReport");
			String reportType = selectedReport.getReportType();
			if (reportType.equals("Quarterly Revenue"))
				reportType = "revenue_report";
			else if (reportType.equals("Purchases"))
				reportType = "purchased_report";
			else if (reportType.equals("Inventory"))
				reportType = "inventory_report";
			request.add(reportType);
			request.add(selectedReport.getGasStationCompany());
			request.add(selectedReport.getGasStationName());
			request.add(selectedReport.getYear());
			request.add(selectedReport.getQuarterNumber());

			chs.client.handleMessageFromClientUI(request);

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblErr.setText("");
				}
			});
		} else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblErr.setText("Select Report");
				}
			});
		}
	}

	/**
	 * This function is called from server to set up data in controller and put
	 * values in gas station companies.
	 * 
	 * @param answer - Data from server
	 */
	@SuppressWarnings("unchecked")
	public void SetComboBoxData(ArrayList<Object> answer) {
		// TODO Auto-generated method stub
		ArrayList<Object> table = (ArrayList<Object>) answer.get(1);
		String company = null, name = null;

		for (int i = 0; i < table.size(); i++) {
			ArrayList<Object> row = (ArrayList<Object>) table.get(i);
			company = String.valueOf(row.get(0));
			name = String.valueOf(row.get(1));
			// cbGasStationCompany.getItems().add(String.valueOf(row.get(0)));

			if (mapGasStations.containsKey(company))
				mapGasStations.get(company).add(new String(name));
			else {
				mapGasStations.put(new String(company), new ArrayList<String>());
				mapGasStations.get(company).add(new String(name));
			}
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				cbGasStationCompany.getItems().addAll(mapGasStations.keySet());
			}
		});

	}
	/**
	 * This function is called after coming back from server to fill the details in table.
	 * @param answer - Details from the server to show in the table.
	 */
	@SuppressWarnings("unchecked")
	public void GetDataForQuarterlyReportsTable(ArrayList<Object> answer) {
		ArrayList<Object> table = (ArrayList<Object>) answer.get(1);
		ObservableList<QuarterlyReports> itemsForTable = FXCollections.observableArrayList();
		ArrayList<Object> row;

		for (int i = 0; i < table.size(); i++) {
			row = (ArrayList<Object>) table.get(i);
			int j = 0;
			String reportType = String.valueOf(row.get(j++));
			if (reportType.equals("revenue_report"))
				reportType = "Quarterly Revenue";
			else if (reportType.equals("purchased_report"))
				reportType = "Purchases";
			else if (reportType.equals("inventory_report"))
				reportType = "Inventory";
			String gasStationCompany = String.valueOf(row.get(j++));
			String gasStationName = String.valueOf(row.get(j++));
			int year = Integer.parseInt(String.valueOf(row.get(j++)));
			int quarterNumber = Integer.parseInt(String.valueOf(row.get(j++)));
			itemsForTable.add(new QuarterlyReports(reportType, gasStationCompany, gasStationName, year, quarterNumber));
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				tblReports.setItems(itemsForTable);
			}
		});
	}
	/**
	 * This function is called after coming back from server to show the requested report.
	 * @param answer - Details from the server to show the report.
	 */
	@SuppressWarnings("unchecked")
	public void ShowSpecificReport(ArrayList<Object> answer) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				String strForFile;
				try {
					QuarterlyReports selectedReport = tblReports.getSelectionModel().getSelectedItem();
					boolean fileCreated = false;
					String filename = "", filePath = "src/reports";
					File myObj = null;
					while (fileCreated == false) {
						File dir = new File(filePath);
						dir.mkdirs();
						filename = selectedReport.getReportType() + "_" + selectedReport.getGasStationCompany() + "_"
								+ selectedReport.getGasStationName() + "_" + selectedReport.getYear() + "_"
								+ selectedReport.getQuarterNumber() + ".txt";
						myObj = new File(dir, filename);
						if (myObj.createNewFile())
							fileCreated = true;
						else
							myObj.delete();// If file allredy exists then delete.
					}

					strForFile = String.valueOf(((ArrayList<Object>) answer.get(1)).get(0));

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

}