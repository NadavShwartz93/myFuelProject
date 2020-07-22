package controller.chainManager;

import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import logic.ConfirmRates;
import logic.Sales;

public class ConfirmRatesController implements Initializable {
	public ClientHandlerSingle chs;

	public ConfirmRatesController() {
		chs = ClientHandlerSingle.getInstance();
		chs.setCrc(this);
	}

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
	private TableView<ConfirmRates> tblconfirmRates;

	@FXML
	private TableColumn<String, ConfirmRates> fuelTypeCol;

	@FXML
	private TableColumn<String, ConfirmRates> priceTypeCol;

	@FXML
	private TableColumn<Float, ConfirmRates> priceBeforeCol;

	@FXML
	private TableColumn<Float, ConfirmRates> priceAfterCol;

	@FXML
	private Button btnConfirm;

	@FXML
	private Button btnReject;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		fuelTypeCol.setCellValueFactory(new PropertyValueFactory<>("fuelType"));
		priceTypeCol.setCellValueFactory(new PropertyValueFactory<>("priceType"));
		priceBeforeCol.setCellValueFactory(new PropertyValueFactory<>("priceBefore"));
		priceAfterCol.setCellValueFactory(new PropertyValueFactory<>("priceAfter"));

		ArrayList<Object> answer = new ArrayList<>();
		answer.add("chainManger/confirmRates");
		answer.add("GetAllRates");

		chs.client.handleMessageFromClientUI(answer);

	}

	@SuppressWarnings("unchecked")
	public void setLabelsAndConfirmRatesInTable(ArrayList<Object> msg) {
		updateTblconfirmRates(msg);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				ArrayList<Object> answer = (ArrayList<Object>) msg.get(1);
				ArrayList<Object> data = (ArrayList<Object>) answer.get(0);
				lblPetrolMyFuel.setText(String.valueOf(data.get(0)));
				lblPetrolMaxFuel.setText(String.valueOf(data.get(1)));
				data = (ArrayList<Object>) answer.get(1);
				lblDieselMyFuel.setText(data.get(0).toString());
				lblDieselMaxFuel.setText(data.get(1).toString());
				data = (ArrayList<Object>) answer.get(2);
				lblScooterMyFuel.setText(data.get(0).toString());
				lblScooterMaxFuel.setText(data.get(1).toString());
				data = (ArrayList<Object>) answer.get(3);
				lblHomeMyFuel.setText(data.get(0).toString());
				lblHomeMaxFuel.setText(data.get(1).toString());
			}

		});
	}
	
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
	
    @FXML
    void ConfirmRate(ActionEvent event) {
    	ConfirmRates confirmRates = tblconfirmRates.getSelectionModel().getSelectedItem();
		if (confirmRates != null) {
			ArrayList<Object> request = new ArrayList<>();
			request.add("chainManger/confirmRates");
			request.add("ConfirmRate");
			request.add(confirmRates.getFuelType());
			request.add(confirmRates.getPriceType());
			request.add(confirmRates.getPriceAfter());

			chs.client.handleMessageFromClientUI(request);
		}
    }
    
    @FXML
    void RejectRate(ActionEvent event) {
    	ConfirmRates confirmRates = tblconfirmRates.getSelectionModel().getSelectedItem();
		if (confirmRates != null) {
			ArrayList<Object> request = new ArrayList<>();
			request.add("chainManger/confirmRates");
			request.add("RejectRate");
			request.add(confirmRates.getFuelType());
			request.add(confirmRates.getPriceType());
			request.add(confirmRates.getPriceAfter());
			request.add(confirmRates.getPriceAfter());

			chs.client.handleMessageFromClientUI(request);
		}
    }
}


