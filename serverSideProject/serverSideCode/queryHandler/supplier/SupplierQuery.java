package queryHandler.supplier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.cj.jdbc.result.ResultSetMetaData;
import com.mysql.cj.protocol.Message;

import controller.ServerLogInController;
import emServer.mySqlConnection;

public class SupplierQuery {

	/**This method read all the data from inventoryorder table in DB, and send it back to the client side.
	 * @param sv is an instance of {@link ServerLogInController}.
	 * @throws SQLException error with sql syntax.
	 */
	public static void getInventoryFuelOrder(ServerLogInController sv) throws SQLException {
		ResultSet rs;
		ResultSetMetaData rsmd;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<Object> answer = new ArrayList<Object>();
		ArrayList<Object> data = new ArrayList<Object>();
		
		
		String tempString = String.format("SELECT gasstation.gasStationName, gasstation.gasStationAddress, " + 
				"inventoryorder.FK_fuelType, inventoryorder.fuelAmount " + 
				"FROM myfueldb.inventoryorder, myfueldb.gasstation " + 
				"WHERE inventoryorder.confirmStatus = 1 AND gasstation.gasStationId = inventoryorder.FK_gasStationId;");
		
		rs = stmt.executeQuery(tempString);  //Run the query.
		rsmd = (ResultSetMetaData)rs.getMetaData();
		
		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i)); // Add value from the i column
			}
		}
		
		answer.add("SupplyInventory");
		answer.add("/setSupplierData");
		answer.add(data);
		sv.es.sendToAllClients(answer);
	}
	
	
	/**This method get inventory orders that the supplier provided, and update the DB. 
	 * First, getting the current amount of the fuel type, second update the inventory table about the new inventoryCapacity
	 * and the last thing, delete this order from inventoryorder table.
	 * @param message contain the relevant data from client.
	 * @param sv is an instance of {@link ServerLogInController}.
	 * @throws SQLException error with sql syntax.
	 */
	@SuppressWarnings("unchecked")
	public static void insertAndUpdateOrder(ArrayList<Object> message, ServerLogInController sv) throws SQLException {
		ResultSet rs;
		ResultSetMetaData rsmd;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<Object> answer = new ArrayList<Object>();
		ArrayList<Object> data = new ArrayList<Object>();
		
		ArrayList<String> tempArrayList  = new ArrayList<>();
		String tempString;
		
		int size = message.size();
		for (int j = 0; j < size; j++) {
			tempArrayList = (ArrayList<String>) message.get(j);
			
			//Getting the current capacity of this fuel type
			tempString = String.format("SELECT inventory.inventoryCapacity " + 
					"FROM myfueldb.inventory " + 
					"WHERE FK_fuelType = \"%s\" AND FK_gasStationId IN " + 
					"( " + 
					"SELECT gasstation.gasStationId " + 
					"FROM myfueldb.gasstation " + 
					"WHERE gasstation.gasStationName = \"%s\" );", tempArrayList.get(2), tempArrayList.get(0)); 
			rs = stmt.executeQuery(tempString);  //Run the query.
			rsmd = (ResultSetMetaData)rs.getMetaData();	
			
			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					data.add(rs.getObject(i)); // Add value from the i column
				}
			}
			
			//This is the current capacity in this gas Station for this fuel type.
			int inventoryCapacity = Integer.parseInt(data.get(0).toString());
			int fuelAmountInOrder = Integer.parseInt(tempArrayList.get(3).toString());
			
			//Update the capacity of the fuel type
			tempString = String.format("UPDATE myfueldb.inventory " + 
					"SET " + 
					"inventoryCapacity = \"%d\" " + 
					"WHERE FK_fuelType = \"%s\" AND FK_gasStationId IN " + 
					"( " + 
					"SELECT gasstation.gasStationId " + 
					"FROM myfueldb.gasstation " + 
					"WHERE gasstation.gasStationName = \"%s\" " + 
					");", inventoryCapacity + fuelAmountInOrder, tempArrayList.get(2).toString(), tempArrayList.get(0));
			
			stmt.executeUpdate(tempString);  //Run the insert query.
			
			
			//Delete this order from inventoryorder 
			tempString = String.format("DELETE FROM myfueldb.inventoryorder " + 
					"WHERE inventoryorder.FK_fuelType = \"%s\" AND inventoryorder.FK_gasStationId IN " + 
					"( " + 
					"SELECT gasstation.gasStationId " + 
					"FROM myfueldb.gasstation " + 
					"WHERE gasstation.gasStationName = \"%s\" " + 
					");", tempArrayList.get(2), tempArrayList.get(0));
			stmt.executeUpdate(tempString);  //Run the insert query.	
		}
		
		SupplierQuery.getInventoryFuelOrder(sv);
	}
	
	
	public static void getHomeFuelOrder(ServerLogInController sv) throws SQLException {
		ResultSet rs;
		ResultSetMetaData rsmd;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<Object> answer = new ArrayList<Object>();
		ArrayList<Object> data = new ArrayList<Object>();
		
		answer.add("SupplyHomeFuelOrders");
		answer.add("/setHomeFuelOrder");
		
		String tempString = String.format("SELECT homefuelpurchases.FK_customerId, homefuelpurchases.orderDate, homefuelpurchases.deliveryDate, " + 
				"homefuelpurchases.fuelAmount, homefuelpurchases.purchaseNumber, " + 
				"homefuelpurchases.address, homefuelpurchases.arrivalStatus " + 
				"FROM myfueldb.homefuelpurchases " + 
				"WHERE homefuelpurchases.arrivalStatus = 'Order sent' OR homefuelpurchases.arrivalStatus = 'Order is in process' \r\n" + 
				"ORDER BY homefuelpurchases.deliveryDate ASC;");
		
		rs = stmt.executeQuery(tempString);  //Run the query.
		rsmd = (ResultSetMetaData)rs.getMetaData();	
		
		ArrayList<ArrayList<Object>> newMsg = new ArrayList<>();
		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i)); // Add value from the i column
			}
			newMsg.add(data);
			data = new ArrayList<Object>();
		}
		
		String customerId = "";
		for (int i = 0; i < newMsg.size(); i++) {
			customerId = (String) newMsg.get(i).get(0);
			
			tempString = String.format("SELECT customer.firstName, customer.lastName " + 
					"FROM myfueldb.customer " + 
					"WHERE customer.id = \"%s\";", customerId);
			rs = stmt.executeQuery(tempString);  //Run the query.
			rsmd = (ResultSetMetaData)rs.getMetaData();	
			
			while (rs.next()) {
				for (int j = 1; j <= rsmd.getColumnCount(); j++) {
					data.add(rs.getObject(j)); // Add value from the i column
				}
				newMsg.get(i).remove(0);
				newMsg.get(i).add(3, customerId);
				newMsg.get(i).add(4, data.get(0));
				newMsg.get(i).add(5, data.get(1));


			}
			
		}

		
		
		answer.add(newMsg);
		sv.es.sendToAllClients(answer);
		
	}
	
	
	@SuppressWarnings("unchecked")
	public static void updateHomeFuelPurchasesTable(ArrayList<Object> message, ServerLogInController sv) throws SQLException {		
		ResultSet rs;
		ResultSetMetaData rsmd;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<Object> tempArrayList  = new ArrayList<>();
		String tempString = null;
		
		int size = message.size();
		for (int i = 0; i < size; i++) {
			tempArrayList = (ArrayList<Object>) message.get(i);
			
			tempString = String.format("UPDATE myfueldb.homefuelpurchases " + 
					"SET " + 
					"arrivalStatus = \"%s\" " + 
					"WHERE homefuelpurchases.purchaseNumber = \"%d\";", tempArrayList.get(1).toString(), tempArrayList.get(0));
			stmt.executeUpdate(tempString);  //Run the update query.
		}
		SupplierQuery.getHomeFuelOrder(sv);
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
