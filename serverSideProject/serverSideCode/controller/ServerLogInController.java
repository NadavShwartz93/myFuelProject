package controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import emServer.EchoServer;
import emServer.mySqlConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ServerLogInController implements Initializable{

	@FXML
	private Button LoginBtn;

	@FXML
	private TextField UserNameBar;

	@FXML
	private TextField SchemaBar;

	@FXML
	private TextField PassBar;

	@FXML
	private TextField PortBar;

	@FXML
	private Label StatusLabel;

	@FXML
	private TextArea PrintScreen;
    
    @FXML
    private ComboBox<String> cbHostName;


	final public static int DEFAULT_PORT = 5555;
	public EchoServer es;
	private boolean loggedIn;
	private mySqlConnection conn;

	public ServerLogInController() {
		this.conn = new mySqlConnection(this);
		loggedIn = false;
	}

	public void start(Stage primaryStage) {// Starts the gui window
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/ServerAndDBConnection.fxml"));// loads the fxml
																										// file created
																										// by the scene
																										// builder
			Scene scene = new Scene(root);
			primaryStage.setTitle("Server & DB connection");
			primaryStage.setScene(scene);
			
			//Image image = new Image("/icons/server.png");//New
			Image image = new Image("/icons/server2.jpg");//New
			primaryStage.getIcons().add(image);
			
			primaryStage.show();
			primaryStage.setOnCloseRequest(event -> {
				try {
					System.exit(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
				primaryStage.close();
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void LoginBtn(ActionEvent event) throws Exception {
		if (!loggedIn) { // this button has two conditions login/logout this boolean lets the function
							// know on what mode it is on now
			int port;
			boolean flag = false;
			try {
				port = Integer.parseInt(PortBar.getText()); // copies port from gui
			} catch (Throwable t) {
				port = DEFAULT_PORT; // if there was a problem coping port it wil set to "5555"
			}

			es = new EchoServer(port, this, conn); // sets up a new server connection
			try {
				es.listen(); // Start listening for connections if there is a problem it will catch the
								// exception
				flag = true; // wont turn if exception jumps
			} catch (Exception ex) {
				System.out.println("ERROR - Could not listen for clients!");
				PrintScreen.appendText("ERROR - Could not listen for clients!");// will print to gui
			}
			if (flag) { // will enter only if server was connected seccesfuly
				if (conn.ConnectToDB(cbHostName.getValue(), SchemaBar.getText(), UserNameBar.getText(),
						PassBar.getText())) {
					StatusLabel.setStyle("-fx-background-color:GREEN"); // sets logged off logo to be green
					StatusLabel.setText("Logged in"); // changes text on status label to say "LOGGED IN"
					LoginBtn.setText("Log out");// changes text on button label to say "LOG OUT"
					loggedIn = true; // change boolean so next press will log out
				} else {
					es.close(); // if server connection was bad it will close it
				}
			}
		} else { // if log out was pressed
			StatusLabel.setStyle("-fx-background-color:RED"); // sets status label
			StatusLabel.setText("Logged out");// sets status label
			LoginBtn.setText("Log in");// sets butoon
			Logout();
		}
	}

	public void Logout() throws IOException {

		loggedIn = false;// change boolean so next press will log in
		es.close();
		conn.close();// closes sql connection
	}

	public void printToWindow(String msg) {
		PrintScreen.appendText(msg + "\n"); // prints to textarea on gui
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cbHostName.getItems().addAll("Amazon RDS","Microsoft Azure","Localhost");
		
		
		cbHostName.setValue("Localhost");
		UserNameBar.setText("root");
		SchemaBar.setText("myfueldb");

	}
	
	@FXML
    void ChangeDataBaseServers(ActionEvent event) {
    	switch (cbHostName.getValue().toString()) {
		case "Localhost":
			UserNameBar.setText("root");
			SchemaBar.setText("myfueldb");
			PassBar.setText("");
			break;
		case "Amazon RDS":
			UserNameBar.setText("admin");
			SchemaBar.setText("myfueldb");
			PassBar.setText("JOusGxcu8Ox9KB3SlgAL");
			break;
		case "Microsoft Azure":
			UserNameBar.setText("MYFULE@myfuledb");
			SchemaBar.setText("myfueldb");
			PassBar.setText("xseT4rd7cUt6fvyOy7bgSu3hDni4FjmYg5fbh");
			break;

		default:
			break;
		}
	}


}