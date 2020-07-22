package emClient;

import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ClientMain extends Application {
	
	LoginController lic;
	public ClientHandlerSingle chs;
		
	@Override
	public void start(Stage arg0) throws Exception {
		try {
			chs = ClientHandlerSingle.getInstance();
			
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LogIn.fxml"));
			Parent root =(Parent)loader.load();
			Scene scene = new Scene(root);
			arg0.getIcons().add(new Image("/icons/fuel.png"));
			arg0.setTitle("MyFule Login");
			arg0.setScene(scene);
			arg0.show();
			arg0.setOnCloseRequest(event -> {
				chs.client.quit();
				//thisStage.show();
			});
			lic = loader.getController();
			lic.saveStage(arg0);
			} catch(Exception e) {
				e.printStackTrace();
			}
	}
		  	
	public static void main(String[] args) {
        launch(args);
	}		
}

