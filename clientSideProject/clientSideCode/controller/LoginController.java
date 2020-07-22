package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import controller.MainDesplayController;
import emClient.ClientHandlerSingle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LoginController extends Application implements Initializable {

	public ClientHandlerSingle chs;
	public LoginController lic;
	private static Stage thisStage;
	private static BorderPane mainLayout;
	private static AnchorPane mainLayoutAnchor;
	public Permissions userPer;
	private MainDesplayController mdc;

	public LoginController() {
		chs = ClientHandlerSingle.getInstance();
		chs.setLic(this);
		lic = this;
	}

	public void saveStage(Stage thisStage) {
		this.thisStage = thisStage;
	}

	@FXML
	private TextField UserNameBar;

	@FXML
	private PasswordField PassBar;

	@FXML
	private Button LoginBtn;

	@FXML
	private Button btnFuelCar;

	@FXML
	private Label lblErrLogIn;

	@FXML
	void LoginBtn(ActionEvent event) {
		// String tempUserName = UserNameBar.getText();
		// String tempPass = PassBar.getText();
		ArrayList<String> newMsg = new ArrayList<String>();
		newMsg.add("LoginRequest");
		newMsg.add(UserNameBar.getText());
		newMsg.add(PassBar.getText());
		chs.client.handleMessageFromClientUI(newMsg);
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					lblErrLogIn.setText("");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public void initialize(URL location, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		//UserNameBar.setText("boaz");
		//PassBar.setText("123456");
		lblErrLogIn.setText("");
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

	}

	public void openUser() {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					thisStage.getScene().getWindow().hide();
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(LoginController.class.getResource("/fxml/MainDesplay.fxml"));
					mainLayout = loader.load();
					Stage stage = new Stage();
					// stage.setTitle("Employee App");
					// stage = loader.load();
					Scene scene = new Scene(mainLayout);
					Image image = new Image("/icons/fuel.png");
					stage.getIcons().add(image);
					stage.setTitle("MyFuel - Team 6");
					stage.setScene(scene);
					stage.show();

					FXMLLoader loader2 = new FXMLLoader();
		//			loader2.setLocation(MainDesplayController.class.getResource("/fxml/HomeCenterPane.fxml"));
					AnchorPane mainItems;
		//			mainItems = loader2.load();
					mainItems = new AnchorPane();//New
					
					mainLayout.setCenter(mainItems);
					MainDesplayController.mainBorderPane = mainLayout;

					stage.setOnCloseRequest(event -> {
						//thisStage.show();
						ArrayList<String> newMsg = new ArrayList<String>();
						newMsg.add("LoginRequest/Disconnect");
						String name = chs.UserName;
						newMsg.add(chs.UserName);
						chs.client.handleMessageFromClientUI(newMsg);
						chs.client.quit();
						
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@FXML
	void GoFuelCar(ActionEvent event) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					thisStage.getScene().getWindow().hide();
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(LoginController.class.getResource("/fxml/customer/BuyVehicleFuel.fxml"));
					mainLayoutAnchor = loader.load();
					Stage stage = new Stage();
					// stage.setTitle("Employee App");
					// stage = loader.load();
					Scene scene = new Scene(mainLayoutAnchor);
					Image image = new Image("/icons/fuel.png");
					stage.getIcons().add(image);
					stage.setTitle("MyFuel - Team 6");
					stage.setScene(scene);
					stage.show();

					lblErrLogIn.setText("");
					/*
					 * FXMLLoader loader2 = new FXMLLoader();
					 * loader2.setLocation(MainDesplayController.class.getResource(
					 * "/fxml/HomeCenterPane.fxml")); AnchorPane mainItems; mainItems =
					 * loader2.load(); mainLayout.setCenter(mainItems);
					 * MainDesplayController.mainBorderPane = mainLayout;
					 */

					stage.setOnCloseRequest(event -> {
						thisStage.show();
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public void UserDoesntExist() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					lblErrLogIn.setText("User doesn't exist");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public void UserIsAllreadyOnline() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					lblErrLogIn.setText("User can't login twice");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static Stage GetLoginStage()
	{
		return thisStage;
	}
}
