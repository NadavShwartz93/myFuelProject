package controller.marketingDepartmentWorker;

import javafx.scene.control.Label;
import java.net.URL;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.LocalTimeStringConverter;
import logic.CheckComponentInput;
import logic.Sales;
/**
 * This Class is for the Marketing Department to create or update sales template.
 * @author Boaz Trauthwein
 *
 */
public class SettingPatternOfSalesPromotionController implements Initializable {
	public ClientHandlerSingle chs;
	// private ArrayList<Object> salesTable;
	// private java.util.Map<String, Integer> mapTableCoulmn;
	ObservableList<Sales> itemsForTable;

	public SettingPatternOfSalesPromotionController() {
		chs = ClientHandlerSingle.getInstance();
		chs.setSpospc(this);
	}

	@FXML
	private TableView<Sales> tblSales;

	@FXML
	private TableColumn<String, Sales> saleNameCol;

	@FXML
	private TableColumn<String, Sales> fuelTypeCol;

	@FXML
	private TableColumn<String, Sales> discountCol;

	@FXML
	private TableColumn<String, Sales> startHourCol;

	@FXML
	private TableColumn<String, Sales> endHourCol;

	@FXML
	private TableColumn<String, Sales> isActiveCol;

	@FXML
	private Button btnDelete;

	@FXML
	private TextField txtDiscount;

	@FXML
	private ChoiceBox<String> cbFuelType;

	@FXML
	private TextField txtSaleName;

	@FXML
	private TextField txtSHHour;

	@FXML
	private TextField txtSHMinutes;

	@FXML
	private TextField txtEHHour;

	@FXML
	private TextField txtEHMinute;

	@FXML
	private Button btnAdd;

	@FXML
	private Button btnUpdate;

	@FXML
	private Button btnClear;

	@FXML
	private Label lblErrSaleName;

	@FXML
	private Label lblErrDiscount;

	@FXML
	private Label lblErrStartHour;

	@FXML
	private Label lblErrEndHour;

	@FXML
	private Label lblErrFuelType;

	@FXML
	private Label lblErrOverlapping;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		saleNameCol.setCellValueFactory(new PropertyValueFactory<>("saleName"));
		fuelTypeCol.setCellValueFactory(new PropertyValueFactory<>("fuelType"));
		discountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
		startHourCol.setCellValueFactory(new PropertyValueFactory<>("startHour"));
		endHourCol.setCellValueFactory(new PropertyValueFactory<>("endHour"));

		cbFuelType.getItems().addAll("Petrol", "Diesel", "ScooterFuel");

		ArrayList<Object> request = new ArrayList<>();
		request.add("SettingPatternOfSalesPromotion");
		request.add("GetAllSales");

		chs.client.handleMessageFromClientUI(request);
	}
/**
 * This function is called when "Add sale" is clicked on to add a sale to the server.
 * @param event - ActionEvent
 */
	@FXML
	void AddSale(ActionEvent event) {
		if (checkInput("Add")) {
			if (CheckIfTimeIsValid()) {
				ArrayList<Object> request = new ArrayList<>();
				request.add("SettingPatternOfSalesPromotion");
				request.add("AddSale");
				request.add(txtSaleName.getText());
				request.add(cbFuelType.getValue());
				request.add(txtDiscount.getText());
				request.add(txtSHHour.getText() + ":" + txtSHMinutes.getText());
				request.add(txtEHHour.getText() + ":" + txtEHMinute.getText());
				chs.client.handleMessageFromClientUI(request);
			}
		}
	}
/**
 * This function is called to check if the time that is insert is valid.
 * @return true or false.
 */
	private boolean CheckIfTimeIsValid() {
		if (Integer.parseInt(txtSHMinutes.getText()) < 10)
			txtSHMinutes.setText("0" + Integer.parseInt(txtSHMinutes.getText().toString()));
		if (Integer.parseInt(txtEHMinute.getText()) < 10)
			txtEHMinute.setText("0" + Integer.parseInt(txtEHMinute.getText().toString()));
		if (Integer.parseInt(txtSHHour.getText()) < 10)
			txtSHHour.setText("0" + Integer.parseInt(txtSHHour.getText().toString()));
		if (Integer.parseInt(txtEHHour.getText()) < 10)
			txtEHHour.setText("0" + Integer.parseInt(txtEHHour.getText().toString()));
		
		LocalTime startTime = LocalTime.parse(txtSHHour.getText() + ":" + txtSHMinutes.getText());
		LocalTime endTime = LocalTime.parse(txtEHHour.getText() + ":" + txtEHMinute.getText());

		if (startTime.isAfter(endTime)) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblErrOverlapping.setText("Start time is after end time");
				}
			});
			return false;
		}
		return true;

	}
/**
 * This function is called to delete a sales template in server.
 * @param event - ActionEvent
 */
	@FXML
	void DeleteSale(ActionEvent event) {
		Sales selectedSale = tblSales.getSelectionModel().getSelectedItem();
		if (selectedSale != null) {
			ArrayList<Object> request = new ArrayList<>();
			request.add("SettingPatternOfSalesPromotion");
			request.add("DeleteSale");
			request.add(selectedSale.saleName);
			chs.client.handleMessageFromClientUI(request);
		}
	}
/**
 * This function is called to update a sales template to server.
 * @param event
 */
	@FXML
	void UpdateSale(ActionEvent event) {
		if (checkInput("Update")) {
			Sales selectedSale = tblSales.getSelectionModel().getSelectedItem();
			if (selectedSale != null) {
				// if (!SaleOverlapp("Update")) {
				ArrayList<Object> request = new ArrayList<>();
				request.add("SettingPatternOfSalesPromotion");
				request.add("UpdateSale");
				request.add(txtSaleName.getText());
				request.add(cbFuelType.getValue());
				request.add(txtDiscount.getText());
				request.add(txtSHHour.getText() + ":" + txtSHMinutes.getText());
				request.add(txtEHHour.getText() + ":" + txtEHMinute.getText());
				request.add(selectedSale.saleName);
				chs.client.handleMessageFromClientUI(request);
				// }
			}
		}
	}
/**
 * This function is called when a sales template is clicked in the table.
 * @param event - MouseEvent
 */
	@FXML
	void ChooseSale(MouseEvent event) {
		Sales selectedSale = tblSales.getSelectionModel().getSelectedItem();
		if (selectedSale != null) {
			txtSaleName.setText(selectedSale.saleName);
			cbFuelType.setValue(selectedSale.fuelType);
			txtDiscount.setText(selectedSale.discount.toString());
			txtSHHour.setText(String.valueOf(selectedSale.startHour.getHour()));
			txtSHMinutes.setText(String.valueOf(selectedSale.startHour.getMinute()));
			if (Integer.parseInt(txtSHMinutes.getText()) == 0)
				txtSHMinutes.setText("00");
			txtEHHour.setText(String.valueOf(selectedSale.endHour.getHour()));
			txtEHMinute.setText(String.valueOf(selectedSale.endHour.getMinute()));
			if (Integer.parseInt(txtEHMinute.getText()) == 0)
				txtEHMinute.setText("00");
		}

	}
/**
 * This function is called when button "clear" is clicked to clear all components.
 * @param event - ActionEvent
 */
	@FXML
	void ClearComponents(ActionEvent event) {
		ClearAllComponents();
		clearErrors();
	}

	private void ClearAllComponents() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				txtDiscount.setText("");
				cbFuelType.setValue("Petrol");
				txtSaleName.setText("");
				txtSHHour.setText("");
				txtSHMinutes.setText("");
				txtEHHour.setText("");
				txtEHMinute.setText("");
			}
		});
	}
/**
 * This function is called to initialize the sales template table with values from server.
 * @param answer - Data from server
 */
	public void setSalesInTableInitialize(ArrayList<Object> answer) {
		clearErrors();
		ClearAllComponents();

		ArrayList<Object> table = (ArrayList<Object>) answer.get(1);
		ArrayList<Object> row;
		itemsForTable = FXCollections.observableArrayList();

		for (int i = 0; i < table.size(); i++) {
			int j = 0;
			row = (ArrayList<Object>) table.get(i);
			String saleName = String.valueOf(row.get(j++));
			String fuelType = String.valueOf(row.get(j++));
			Float disscount = Float.valueOf(String.valueOf(row.get(j++)));
			LocalTime startHour = LocalTime.parse(String.valueOf(row.get(j++)));
			LocalTime endHour = LocalTime.parse(String.valueOf(row.get(j++)));
			Boolean isActive = Boolean.valueOf(String.valueOf(row.get(j++)));
			itemsForTable.add(new Sales(saleName, fuelType, disscount, startHour, endHour, isActive));
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				tblSales.setItems(itemsForTable);
			}
		});
	}
/**
 * This function is called to check input from components if they are valid for the next step.
 * @param action - the kind of action to be taken
 * @return true or false.
 */
	private boolean checkInput(String action) {
		boolean isInputValid = true;
		clearErrors();

		if (txtSaleName.getText().compareTo("") == 0) {
			isInputValid = false;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblErrSaleName.setText("Type name");
				}
			});
		}
		if (action.compareTo("Add") == 0)
			for (Sales sale : itemsForTable)
				if (txtSaleName.getText().compareTo(sale.saleName) == 0) {
					isInputValid = false;
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							lblErrSaleName.setText("Name allready exists");
						}
					});
				}

		if (txtDiscount.getText().compareTo("") == 0) {
			isInputValid = false;
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					lblErrDiscount.setText("Type discount");
				}
			});
		} else if (!CheckComponentInput.isNumber(txtDiscount.getText())) {
			isInputValid = false;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblErrDiscount.setText("Discount is not a number");
				}
			});
		}

		if (txtSHHour.getText().compareTo("") == 0)

		{
			isInputValid = false;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblErrStartHour.setText("Type Hour");
				}
			});
		} else if (!CheckComponentInput.isNumber(txtSHHour.getText())) {
			isInputValid = false;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblErrStartHour.setText("That's not a time");
				}
			});
		} else if (Integer.parseInt(txtSHHour.getText()) > 23 || Integer.parseInt(txtSHHour.getText()) < 0) {
			isInputValid = false;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblErrStartHour.setText("Hour 0-23");
				}
			});
		}

		if (txtSHMinutes.getText().compareTo("") == 0) {
			isInputValid = false;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblErrStartHour.setText("Type Minute");
				}
			});
		} else if (!CheckComponentInput.isNumber(txtSHMinutes.getText())) {
			isInputValid = false;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblErrStartHour.setText("That's not a time");
				}
			});
		} else if (Integer.parseInt(txtSHMinutes.getText()) > 59 || Integer.parseInt(txtSHMinutes.getText()) < 0) {
			isInputValid = false;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblErrStartHour.setText("Minutes 0-59");
				}
			});
		}

		if (txtEHHour.getText().compareTo("") == 0) {
			isInputValid = false;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblErrEndHour.setText("Type Hour");
				}
			});
		} else if (!CheckComponentInput.isNumber(txtEHHour.getText())) {
			isInputValid = false;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblErrEndHour.setText("That's not a time");
				}
			});
		} else if (Integer.parseInt(txtEHHour.getText()) > 23 || Integer.parseInt(txtEHHour.getText()) < 0) {
			isInputValid = false;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblErrEndHour.setText("Hour 0-23");
				}
			});
		}

		if (txtEHMinute.getText().compareTo("") == 0) {
			isInputValid = false;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblErrEndHour.setText("Type Minute");
				}
			});
		} else if (!CheckComponentInput.isNumber(txtEHMinute.getText())) {
			isInputValid = false;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblErrEndHour.setText("That's not a time");
				}
			});
		} else if (Integer.parseInt(txtEHMinute.getText()) > 59 || Integer.parseInt(txtEHMinute.getText()) < 0) {
			isInputValid = false;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblErrEndHour.setText("Minutes 0-59");
				}
			});
		}

		return isInputValid;
	}
/**
 * This function is called when button "clear" is clicked to clear all components.
 */
	private void clearErrors() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				lblErrSaleName.setText("");
				lblErrFuelType.setText("");
				lblErrDiscount.setText("");
				lblErrStartHour.setText("");
				lblErrEndHour.setText("");
				lblErrOverlapping.setText("");
			}
		});
	}

}
