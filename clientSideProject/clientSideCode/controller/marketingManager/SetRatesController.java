package controller.marketingManager;

import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import emClient.ClientHandlerSingle;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import logic.CheckComponentInput;
import logic.ConfirmRates;
import logic.Sales;
/**
 * This Class is for the Marketing Manager to set fuel rates.
 * @author Boaz Trauthwein
 *
 *
 */
public class SetRatesController implements Initializable {

	public ClientHandlerSingle chs;

	public SetRatesController() {
		chs = ClientHandlerSingle.getInstance();
		chs.setSrc(this);
	}

	@FXML
	private TextField txtPetrol;

	@FXML
	private TextField txtDiesel;

	@FXML
	private TextField txtHomeFuel;

	@FXML
	private TextField txtScooterFuel;

	@FXML
	private Button btnSetMyFuelRates;

	@FXML
	private Button btnSetMaximumRates;

	@FXML
	private Label lblPetrolMyFuel;

	@FXML
	private Label lblPetrolMaxFuel;

	@FXML
	private Label lblDieselMyFuel;

	@FXML
	private Label lblDieselMaxFuel;

	@FXML
	private Label lblScooterMyFuel;

	@FXML
	private Label lblScooterMaxFuel;

	@FXML
	private Label lblHomeMyFuel;

	@FXML
	private Label lblHomeMaxFuel;

	@FXML
	private Label lblErrPetrol;

	@FXML
	private Label lblErrDiesel;

	@FXML
	private Label lblErrScooterFuel;

	@FXML
	private Label lblErrHomeFuel;

	@FXML
	private TableView<ConfirmRates> tblconfirmRates;

	@FXML
	private TableColumn<String, ConfirmRates> fuelTypeCol;

	@FXML
	private TableColumn<String, ConfirmRates> priceTypeCol;

	@FXML
	private TableColumn<Float, ConfirmRates> priceBeforeCol;

	@FXML
	private TableColumn<Float, ConfirmRates> priceAfterCol;

	float petrolMaxFuel, dieselMaxFuel, scooterMaxFuel, homeMaxFuel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		lblErrPetrol.setText("");
		lblErrDiesel.setText("");
		lblErrScooterFuel.setText("");
		lblErrHomeFuel.setText("");

		lblPetrolMyFuel.setText("");
		lblPetrolMaxFuel.setText("");
		lblDieselMyFuel.setText("");
		lblDieselMaxFuel.setText("");
		lblScooterMyFuel.setText("");
		lblScooterMaxFuel.setText("");
		lblHomeMyFuel.setText("");
		lblHomeMaxFuel.setText("");

		btnSetMaximumRates.setTooltip(new Tooltip("Change the Maximum Rate Column"));
		btnSetMyFuelRates.setTooltip(new Tooltip("Change the MyFuel Rate Column"));

		fuelTypeCol.setCellValueFactory(new PropertyValueFactory<>("fuelType"));
		priceTypeCol.setCellValueFactory(new PropertyValueFactory<>("priceType"));
		priceBeforeCol.setCellValueFactory(new PropertyValueFactory<>("priceBefore"));
		priceAfterCol.setCellValueFactory(new PropertyValueFactory<>("priceAfter"));

		getAllRates();

	}
/**
 * This function is called from client handler to set the data to labels.
 * @param msg - Data from server.
 */
	@SuppressWarnings("unchecked")
	public void setRatesInLabelsAndTable(ArrayList<Object> msg) {

		updateTblconfirmRates(msg);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				ArrayList<Object> answer = (ArrayList<Object>) msg.get(1);
				ArrayList<Object> data = (ArrayList<Object>) answer.get(0);
				lblPetrolMyFuel.setText(String.valueOf(data.get(0)));
				lblPetrolMaxFuel.setText(String.valueOf(data.get(1)));
				petrolMaxFuel = Float.parseFloat(String.valueOf(data.get(1)));
				data = (ArrayList<Object>) answer.get(1);
				lblDieselMyFuel.setText(data.get(0).toString());
				lblDieselMaxFuel.setText(data.get(1).toString());
				dieselMaxFuel = Float.parseFloat(String.valueOf(data.get(1)));
				data = (ArrayList<Object>) answer.get(2);
				lblScooterMyFuel.setText(data.get(0).toString());
				lblScooterMaxFuel.setText(data.get(1).toString());
				scooterMaxFuel = Float.parseFloat(String.valueOf(data.get(1)));
				data = (ArrayList<Object>) answer.get(3);
				lblHomeMyFuel.setText(data.get(0).toString());
				lblHomeMaxFuel.setText(data.get(1).toString());
				homeMaxFuel = Float.parseFloat(String.valueOf(data.get(1)));
				lblErrPetrol.setText("");
				lblErrDiesel.setText("");
				lblErrScooterFuel.setText("");
				lblErrHomeFuel.setText("");

			}

		});

	}
/**
 * This function is called when button maximum rate is clicked.
 * @param event - ActionEvent
 */
	@FXML
	void setMaximumRates(ActionEvent event) {
		setFuelRatesToServer("setMaximumRates");
	}
/**
 * This function is called when button fuel rate is clicked.
 * @param event
 */
	@FXML
	void setMyFuelRates(ActionEvent event) {
		setFuelRatesToServer("setMyFuelRates");
	}
/**
 * This function is called to set fuel rates to server by the fuel kind.
 * @param stringFuelType - fuel type.
 */
	private void setFuelRatesToServer(String stringFuelType) {

		if (CheckIfTextFieldsAreValid(stringFuelType)) {
			ArrayList<Object> answer = new ArrayList<>();
			answer.add("marketingManager/setRates");
			answer.add(stringFuelType);
			answer.add(txtPetrol.getText());
			answer.add(txtDiesel.getText());
			answer.add(txtScooterFuel.getText());
			answer.add(txtHomeFuel.getText());
			chs.client.handleMessageFromClientUI(answer);
			clearErrors();
			txtPetrol.clear();
			txtDiesel.clear();
			txtScooterFuel.clear();
			txtHomeFuel.clear();

		}
	}
/**
 * This function is called to get all rates from server.
 */
	private void getAllRates() {
		ArrayList<Object> answer = new ArrayList<>();
		answer.add("marketingManager/setRates");
		answer.add("GetAllRates");

		chs.client.handleMessageFromClientUI(answer);
	}
/**
 * This function is called to check if all test fields are valid.
 * @param stringFuelType - fuel type. For every type there is a different check.
 * @return
 */
	private boolean CheckIfTextFieldsAreValid(String stringFuelType) {
		// TODO Auto-generated method stub
		boolean isInputValid = true;

		clearErrors();

		if (txtPetrol.getText().compareTo("") != 0)
			if (!CheckComponentInput.isNumber(txtPetrol.getText())) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						lblErrPetrol.setText("Price is not a number");
						txtPetrol.clear();
					}
				});
				isInputValid = false;
			} else if (stringFuelType.compareTo("setMyFuelRates") == 0
					&& Float.parseFloat(txtPetrol.getText()) > petrolMaxFuel) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						lblErrPetrol.setText("Price is to high");
						txtPetrol.clear();
					}
				});
				isInputValid = false;
			}

		if (txtDiesel.getText().compareTo("") != 0)
			if (!CheckComponentInput.isNumber(txtDiesel.getText())) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						lblErrDiesel.setText("Price is not a number");
						txtDiesel.clear();
					}
				});
				isInputValid = false;
			} else if (stringFuelType.compareTo("setMyFuelRates") == 0
					&& Float.parseFloat(txtDiesel.getText()) > dieselMaxFuel) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						lblErrDiesel.setText("Price is to high");
						txtDiesel.clear();
					}
				});
				isInputValid = false;
			}
		if (txtScooterFuel.getText().compareTo("") != 0)
			if (!CheckComponentInput.isNumber(txtScooterFuel.getText())) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {

						lblErrScooterFuel.setText("Price is not a number");
						txtScooterFuel.clear();
					}
				});
				isInputValid = false;
			} else if (stringFuelType.compareTo("setMyFuelRates") == 0
					&& Float.parseFloat(txtScooterFuel.getText()) > scooterMaxFuel) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						lblErrScooterFuel.setText("Price is to high");
						txtScooterFuel.clear();
					}
				});
				isInputValid = false;
			}
		if (txtHomeFuel.getText().compareTo("") != 0)
			if (!CheckComponentInput.isNumber(txtHomeFuel.getText())) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {

						lblErrHomeFuel.setText("Price is not a number");
						txtHomeFuel.clear();
					}
				});
				isInputValid = false;
			} else if (stringFuelType.compareTo("setMyFuelRates") == 0
					&& Float.parseFloat(txtHomeFuel.getText()) > homeMaxFuel) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						lblErrHomeFuel.setText("Price is to high");
						txtHomeFuel.clear();
					}
				});
				isInputValid = false;
			}
		return isInputValid;
	}

	/**
	 * Clears all label Errors.
	 */
	private void clearErrors() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				lblErrPetrol.setText("");
				lblErrDiesel.setText("");
				lblErrScooterFuel.setText("");
				lblErrHomeFuel.setText("");

			}
		});
	}
/**
 * This function is called to update the table of confirm rates
 * @param msg - Data from server to the table.
 */
	private void updateTblconfirmRates(ArrayList<Object> msg) {
		// TODO Auto-generated method stub

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ObservableList<ConfirmRates> itemsForTable;
				ArrayList<Object> answer = (ArrayList<Object>) msg.get(2);
				ArrayList<Object> row;
				itemsForTable = FXCollections.observableArrayList();

				for (int i = 0; i < answer.size(); i++) {
					int j = 0;
					row = (ArrayList<Object>) answer.get(i);
					String fuelType = String.valueOf(row.get(j++));
					String priceType;
					if (String.valueOf(row.get(j++)).compareTo("fuelPrice") == 0)
						priceType = "MyFuel Rates";
					else
						priceType = "Maximum Rates";
					Float priceBefore = Float.valueOf(String.valueOf(row.get(j++)));
					Float priceAfter = Float.valueOf(String.valueOf(row.get(j++)));
					itemsForTable.add(new ConfirmRates(fuelType, priceType, priceBefore, priceAfter));
				}

				tblconfirmRates.setItems(itemsForTable);
			}
		});
	}

}
