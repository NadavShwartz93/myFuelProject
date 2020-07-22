package emServer;

import controller.ServerLogInController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ServerMain extends Application {

	@Override
	public void start(Stage arg0) throws Exception {
		ServerLogInController sv = new ServerLogInController(); 
		sv.start(arg0);
	}

	public static void main(String[] args) {
        launch(args);
	}
}
