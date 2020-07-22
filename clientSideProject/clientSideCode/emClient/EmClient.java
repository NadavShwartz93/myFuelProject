 

package emClient;

import ocsf.client.*;
import java.io.*;


public class EmClient extends AbstractClient
{
  
	ClientHandlerSingle conn; 

  
  public EmClient(String host, int port, ClientHandlerSingle conn) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.conn = conn;
    openConnection();
  }

  
 
  public void handleMessageFromServer(Object msg) 
  {
	try {
		conn.handleAnswer(msg);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }

  
  public void handleMessageFromClientUI(Object msg)  
  {
    try
    {
    	sendToServer(msg);
    }
    catch(IOException e)
    {
      System.out.println(e);
      e.printStackTrace();
      quit();
    }
  }
  
 
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}

