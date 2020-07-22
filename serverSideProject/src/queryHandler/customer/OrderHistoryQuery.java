package queryHandler.customer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import controller.ServerLogInController;
import emServer.mySqlConnection;

/**This class is manage all the request for getting data and insert data, that related 
 * to the order home heating fuel process. 
 * @author Nadav Shwartz.
 */
public class OrderHistoryQuery {
	
	/**This method get the home fuel orders of customer according to the customer choices.
	 * @param message is ArrayList of type Object contain the relevant data for this query. 
	 * @param sv is an instance of {@link ServerLogInController}.
	 * @throws SQLException error with sql syntax.
	 */
	public static void  gettingOrderHistory(ArrayList<Object> message, ServerLogInController sv) throws SQLException{
		ResultSet rs;
		ResultSetMetaData rsmd;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<Object> data = new ArrayList<Object>();
		ArrayList<Object> answer = new ArrayList<Object>();
		
		//Object temp = null;
		String tempString = null;
		int x = 0;
		Boolean inProgressSelected = (Boolean) message.get(0);
		Boolean deliveredSelected =  (Boolean) message.get(1);
		String from_Date = message.get(2).toString();
		String to_Date = message.get(3).toString();
		String customerId = message.get(4).toString();		
		
		if(inProgressSelected && !deliveredSelected)
			x = 0;//'Order sent', 'Order is in process'
		if(!inProgressSelected && deliveredSelected)
			x = 1;//'Order delivered'
		
		/**
		 * The case that user want to see order that in progress and order that delivered.
		 */
		if(inProgressSelected && deliveredSelected || (!inProgressSelected && !deliveredSelected)) {
			tempString = String.format("SELECT homefuelpurchases.orderDate, homefuelpurchases.fuelAmount, " +
					"homefuelpurchases.deliveryDate, homefuelpurchases.arrivalStatus " + 
					"FROM myfueldb.homefuelpurchases " + 
					"WHERE homefuelpurchases.FK_customerId = \"%s\" AND " + 
					"homefuelpurchases.orderDate BETWEEN CAST(\"%s\" AS DATE) AND CAST(\"%s\" AS DATE);",customerId, from_Date,to_Date);

			rs = stmt.executeQuery(tempString);  //Run the query.
			rsmd = (ResultSetMetaData)rs.getMetaData();
			

			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					data.add(rs.getObject(i)); // Add value from the i column
				}

			}
			answer.add("OrderHistoryView");
			answer.add(data);
			
			sv.es.sendToAllClients(answer);
			
			
		}else {
			 /**
			  * The case that user want to see order that in progress - didn't arrive, 
			  * Or want to see order that arrived.
			  */
			if(x == 0) {
				tempString = String.format("SELECT homefuelpurchases.orderDate, homefuelpurchases.fuelAmount, "
						+ "homefuelpurchases.deliveryDate, homefuelpurchases.arrivalStatus " + 
						"FROM myfueldb.homefuelpurchases " + 
						"WHERE homefuelpurchases.FK_customerId = \"%s\" AND " + 
						"homefuelpurchases.orderDate >= \"%s\" AND homefuelpurchases.orderDate <= \"%s\" " + 
						"AND homefuelpurchases.arrivalStatus = \"%s\" OR "
						+ "homefuelpurchases.arrivalStatus = \"%s\";", customerId, from_Date,to_Date,"Order sent", "Order is in process");
				//					"WHERE fuelpurchases.purchasesDay >= \"%s\" AND "
				//+ "fuelpurchases.purchasesDay <= \"%s\" " + 
			}else {
				tempString = String.format("SELECT homefuelpurchases.orderDate, homefuelpurchases.fuelAmount, "
						+ "homefuelpurchases.deliveryDate, homefuelpurchases.arrivalStatus " + 
						"FROM myfueldb.homefuelpurchases " + 
						"WHERE homefuelpurchases.FK_customerId = \"%s\" AND " + 
						"homefuelpurchases.orderDate >= \"%s\" AND homefuelpurchases.orderDate <= \"%s\" " + 
						"AND homefuelpurchases.arrivalStatus = \"%s\";", customerId, from_Date,to_Date,"Order delivered");
			}
			
			rs = stmt.executeQuery(tempString);  //Run the query.
			rsmd = (ResultSetMetaData)rs.getMetaData();		

			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					data.add(rs.getObject(i)); // Add value from the i column
				}
			}
			answer.add("OrderHistoryView");
			answer.add(data);
			
			sv.es.sendToAllClients(answer);
		}


	
	}

}
