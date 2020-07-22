package queryHandler.customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.mail.MessagingException;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import controller.ServerLogInController;
import emServer.mySqlConnection;

/**This class is manage all the request for getting data and insert data, that related 
 * to the order home heating fuel process. 
 * The data mainly arrive from fuel, homefuelpurchases, customer tables in DB.
 * @author Nadav Shwartz.
 *
 */
public class OrderHomeFuelQuery {

	
	/**This method operate query that bring the price of home fuel from fuel table in DB.
	 * @param sv instance of the ServerLogInController.java class.
	 * @throws SQLException error with sql syntax.
	 */
	public static void getHomeFuelPrice( ServerLogInController sv)  throws SQLException {
		ResultSet rs;
		ResultSetMetaData rsmd;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<Object> answer = new ArrayList<Object>();
		answer.add("OrderHomeFuel");
		answer.add("setHomeFuelPrice");

		ArrayList<Object> data = new ArrayList<Object>();
		String tempString = null;
		float fuelPrice = 0, maxFuelPrice = 0;
		
		tempString = String.format("SELECT fuel.fuelPrice, fuel.maxFuelPrice " + 
			  	"FROM fuel " + 
				"WHERE fuel.fuelType = 'HomeFuel';");
		
		rs = stmt.executeQuery(tempString);  //Run the query.
		rsmd = (ResultSetMetaData)rs.getMetaData();
		
		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i)); // Add value from the i column
			}
			fuelPrice = Float.parseFloat(data.get(0).toString());
			maxFuelPrice = Float.parseFloat(data.get(1).toString());
		}
		answer.add(fuelPrice);
		answer.add(maxFuelPrice);
		sv.es.sendToAllClients(answer);
		
	}
	
	/**This method insert new home fuel order to homefuelpurchases table in DB.
	 * @param message contain the relevant data for this query. 
	 * @param sv is an instance of {@link ServerLogInController}.
	 * @throws SQLException error with sql syntax.
	 */
	
	public static void insertHomeFuelOrder(ArrayList<Object> message, ServerLogInController sv)  throws SQLException{
		ResultSet rs;
		ResultSetMetaData rsmd;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<Object> answer = new ArrayList<Object>();
		answer.add("OrderHomeFuel");
		String tempString = null;
		int fuelAmount = Integer.parseInt(message.get(4).toString());
		int flag = 0;
			
			/**
			 * Run insert query for insert the home fuel order to the homefuelpurchases table.
			 */
			String customerId = message.get(0).toString();
			String orderDate = message.get(1).toString();
			String deliveryDate = message.get(2).toString();
			String deliveryTime = message.get(3).toString();
			String arrivalStatus = message.get(5).toString(); 
			String orderAddress = message.get(6).toString();

			tempString = String.format("INSERT INTO myfueldb.homefuelpurchases " + 
					"(" + 
					"FK_customerId, " + 
					"orderDate, " + 
					"deliveryDate, " + 
					"deliveryTime, " + 
					"fuelAmount, " + 
					"arrivalStatus, address) " + 
					"VALUES " + 
					"(" + 
					"\"%s\", " + 
					"\"%s\", " + 
					"\"%s\", " + 
					"\"%s\", " + 
					"\"%d\", " + 
					"\"%s\", \"%s\" " + 
					");", customerId, orderDate, deliveryDate, deliveryTime, fuelAmount, arrivalStatus, orderAddress);
			stmt.executeUpdate(tempString);  //Run the update query.
			answer.add("Success - order Home fuel has been successfully passed");
			sv.es.sendToAllClients(answer);
	


	}

	/**This method find the customer credit card data, that are located in customer table in DB.
	 * @param newMsg contain the customer id.
	 * @param sv is an instance of {@link ServerLogInController}.
	 * @throws SQLException error with sql syntax.
	 */
	public static void getCustomerCreditCard(ArrayList<Object> newMsg, ServerLogInController sv) throws SQLException {
		ResultSet rs;
		ResultSetMetaData rsmd;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<Object> answer = new ArrayList<Object>();
		answer.add("OrderHomeFuel");
		answer.add("setCustomerData");
		
		String tempString = String.format("SELECT customer.creditCardNumber, customer.creditCard_CVV, " + 
				"customer.creditCard_Month, customer.creditCard_year " + 
				"FROM myfueldb.customer " + 
				"WHERE customer.id = \"%s\";", newMsg.get(0).toString());
		
		rs = stmt.executeQuery(tempString);  //Run the query.
		rsmd = (ResultSetMetaData)rs.getMetaData();
		ArrayList<Object> data = new ArrayList<Object>();
		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i)); // Add value from the i column
			}
		}
		
		answer.add(data);
		sv.es.sendToAllClients(answer);
	}



}
