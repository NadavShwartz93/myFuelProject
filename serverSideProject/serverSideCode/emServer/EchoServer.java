package emServer;

import java.sql.SQLException;

import controller.ServerLogInController;
import ocsf.server.*;

public class EchoServer extends AbstractServer {
	private ServerLogInController controller;
	private mySqlConnection conn;
	
	  public EchoServer(int port,ServerLogInController controller,mySqlConnection conn) 
	  {
	    super(port);
	    this.controller=controller;
	    this.conn=conn;
	  }

	  public void handleMessageFromClient(Object msg, ConnectionToClient client)
	  {
		
		try {
			conn.handler(msg);
		} catch (SQLException e) {
			e.printStackTrace();
			}
	}

	  protected void serverStarted()
	  {
	    System.out.println("Server listening for connections on port " + getPort());//tells user server is connected
	    
	    controller.printToWindow("Server listening for connections on port " + getPort());//tells user server is connected on server gui
	  }
	
	  protected void serverStopped()
	  {//tells user server is disconnected
	    System.out.println("Server has stopped listening for connections.");
	    controller.printToWindow("Server has stopped listening for connections.");
	  }
}