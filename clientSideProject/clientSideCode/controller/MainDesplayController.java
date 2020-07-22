package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import de.jensd.fx.glyphs.materialdesignicons.utils.MaterialDesignIconFactory;
import emClient.ClientHandlerSingle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainDesplayController implements Initializable {

	private MainDesplayController mdc;
	public ClientHandlerSingle chs;
	private UserType type;

	public MainDesplayController() {
		chs = ClientHandlerSingle.getInstance();
		chs.setMdc(this);
		mdc = this;
	}

	@FXML
	private BorderPane mdBorderPane;
	@FXML
	private VBox VBoxLeftPane;
	@FXML
	private Button btnDisconnect;
	@FXML
	private VBox vBoxLeftPane;
	@FXML
	private Label lblPosition;
	@FXML
	private Label WelcomeLabel;

	public static BorderPane mainBorderPane;

	public Permissions userPer;

	ArrayList<Button> arrbtn = new ArrayList<Button>();
	ArrayList<EventHandler<MouseEvent>> arrEventHandlerMEntered = new ArrayList<EventHandler<MouseEvent>>();
	ArrayList<EventHandler<MouseEvent>> arrEventHandlerMExit = new ArrayList<EventHandler<MouseEvent>>();
	ArrayList<Text> arrText = new ArrayList<Text>();

	int minWidth = 100;
	int minHeight = 100;
	String backgroundColor = "-fx-background-color:  #265077;";
	String iconeSize = "80px";
	String setFillIconColor = "#000d1a";
	String MouseHoverColorSetStyle = "-fx-background-color:  #3775ae;";
	String MouseHoverColorSetFill = "#001a33";
	int setMinWidth = 100;
	int setMinHeight = 100;
	String emptyString = " ";

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		btnDisconnect.getStyleClass().add("button-disconnect");
		String s = String.format("Welcome, %s ", chs.UserName);
		WelcomeLabel.setText(s.toString());
		userPer = new Permissions(chs.type, chs.companyCustomer);
		if ((userPer.TypeToString()).equals("Customer")) {
			lblPosition.setText(emptyString.toString());
		} else
			lblPosition.setText(userPer.TypeToString());

		setButtonsInVBoxLeftPane();

		// Desplay first window

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(userPer.getUrlsList().get(0));
					String temp = userPer.getUrlsList().get(0).toString();
					AnchorPane mainItems;
					mainItems = loader.load();
					mainBorderPane.setCenter(mainItems);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Setting styles for button
				arrbtn.get(0).setStyle("-fx-background-color:  #ffffff;");
				arrbtn.get(0).removeEventHandler(MouseEvent.MOUSE_ENTERED, arrEventHandlerMEntered.get(0));
				arrbtn.get(0).removeEventHandler(MouseEvent.MOUSE_EXITED, arrEventHandlerMExit.get(0));
				arrbtn.get(0).setStyle("-fx-background-color:  #3775ae;");
				arrText.get(0).setFill(Paint.valueOf("#001a33"));// For icon.
			}
		});
		
	}

	@FXML
	void btnDisconnectMouseEntered(MouseEvent event) {
		btnDisconnect.getStyleClass().remove("button-disconnect");
		btnDisconnect.getStyleClass().add("button-disconnect-entered");

	}

	@FXML
	void btnDisconnectMouseExit(MouseEvent event) {
		btnDisconnect.getStyleClass().remove("button-disconnect-entered");
		btnDisconnect.getStyleClass().add("button-disconnect");
	}

	@FXML
	void FuelIconClicked(MouseEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainDesplayController.class.getResource("/fxml/HomeCenterPane.fxml"));
			AnchorPane mainItems;
			mainItems = loader.load();
			mainBorderPane.setCenter(mainItems);
			int i = 0;
			for (Button button : arrbtn) {
				button.addEventHandler(MouseEvent.MOUSE_ENTERED, arrEventHandlerMEntered.get(i));
				button.addEventHandler(MouseEvent.MOUSE_EXITED, arrEventHandlerMExit.get(i));
				button.setStyle("-fx-background-color:  #265077;");
				arrText.get(i++).setFill(Paint.valueOf("#000d1a"));// For icon.
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void setButtonsInVBoxLeftPane() {

		for (int i = 0; i < userPer.getActionsCount(); i++) {
			StackPane sp = new StackPane();
			Button btn = new Button();
			btn.setText(userPer.getBtnNameList().get(i));
			btn.setContentDisplay(ContentDisplay.TOP);
			btn.setTextFill(Paint.valueOf(setFillIconColor));
			btn.setFont(javafx.scene.text.Font.font("System", FontWeight.BOLD, 17));
			btn.minWidth(minWidth);
			btn.minHeight(minHeight);
			btn.setPrefWidth(100);
			btn.setStyle(backgroundColor);
			Text txt = userPer.getIconsList().get(i);
			txt.setFill(Paint.valueOf(setFillIconColor));
			btn.setGraphic(txt);
			btn.setTooltip(new Tooltip(userPer.getToolTipDescription().get(i)));
			URL actionUrl = userPer.getUrlsList().get(i);

			arrEventHandlerMEntered.add(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					btn.setPrefWidth(100);
					btn.setStyle("-fx-background-color:  #3775ae;");
					txt.setFill(Paint.valueOf("#001a33"));
				}
			});
			btn.addEventHandler(MouseEvent.MOUSE_ENTERED, arrEventHandlerMEntered.get(i));

			arrEventHandlerMExit.add(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					btn.setStyle("-fx-background-color:  #265077;");
					txt.setFill(Paint.valueOf("#000d1a"));
				}
			});
			btn.addEventHandler(MouseEvent.MOUSE_EXITED, arrEventHandlerMExit.get(i));

			btn.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent e) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							try {
								int i = 0;
								for (Button button : arrbtn) {
									if (button.equals(btn)) {// Lightening the button that was clicked
										button.setStyle("-fx-background-color:  #ffffff;");
										button.removeEventHandler(MouseEvent.MOUSE_ENTERED,
												arrEventHandlerMEntered.get(i));
										button.removeEventHandler(MouseEvent.MOUSE_EXITED, arrEventHandlerMExit.get(i));
										button.setStyle("-fx-background-color:  #3775ae;");
										arrText.get(i).setFill(Paint.valueOf("#001a33"));// For icon.
									} else {// Setting original color to all of the other buttons.
										button.addEventHandler(MouseEvent.MOUSE_ENTERED,
												arrEventHandlerMEntered.get(i));
										button.addEventHandler(MouseEvent.MOUSE_EXITED, arrEventHandlerMExit.get(i));
										button.setStyle("-fx-background-color:  #265077;");
										arrText.get(i).setFill(Paint.valueOf("#000d1a"));// For icon.
									}
									i++;
								}
								FXMLLoader loader = new FXMLLoader();
								loader.setLocation(actionUrl);
								AnchorPane mainItems;
								mainItems = loader.load();
								mainBorderPane.setCenter(mainItems);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			});
			sp.setMinHeight(100);
			sp.setMinWidth(100);
			sp.setStyle("-fx-background-color:  #265077;");
			sp.getChildren().add(btn);

			arrbtn.add(btn);

			arrText.add(txt);

			vBoxLeftPane.getChildren().add(sp);
		}

	}

	@FXML
	void DisconnectFromSystem(ActionEvent event) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					Stage stage = (Stage) btnDisconnect.getScene().getWindow();
					stage.close();
					LoginController.GetLoginStage().show();
					ArrayList<String> newMsg = new ArrayList<String>();
					newMsg.add("LoginRequest/Disconnect");
					newMsg.add(chs.UserName);
					chs.client.handleMessageFromClientUI(newMsg);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
