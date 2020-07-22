package queryHandler.customer;

import java.nio.channels.ReadableByteChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import controller.ServerLogInController;
import emServer.mySqlConnection;

public class VehicleFuelPurchesesQuery {

	/**This method find all the relevant data from the DB to the tables in the page VehicleFuelPurcheses.fxml.
	 * @param message contain customer id.
	 * @param sv instance of the ServerLogInController.java class.
	 * @throws SQLException error with sql syntax.
	 */
	public static void getPurchasesData(ArrayList<Object> message, ServerLogInController sv) throws SQLException {
		String customerId = message.get(0).toString();
		ArrayList<Object>  answer = new ArrayList<Object>();
		
		answer.add("vehicleFuelPurchasesData");
		answer.add("/setDataInTabe");
		
		//Get the car license numbers of customer.
		ArrayList<String> carLicenseNumbers = getCustomerCars(customerId);
		if(!carLicenseNumbers.isEmpty()) {
			//Get customer model.
			String customerModel = getCustomerModel(customerId, sv);
			answer.add(customerModel);
			
			answer.add(getDataForAllPurchasesTable(carLicenseNumbers, sv));
						
			if(!customerModel.equals("NormalFueling")) {
				answer.add(getDataForMonthlyPaymentsTable(carLicenseNumbers, sv));
			}
		}else {
			answer.add("Customer have no cars");
		}

		sv.es.sendToAllClients(answer);

	}

	/**This method find all the car license numbers that associate to the customer.
	 * @param customerId is string that represent the customer id.
	 * @return ArrayList from type String, that contain all car license numbers of customer.
	 * @throws SQLException
	 */
	private static ArrayList<String> getCustomerCars(String customerId) throws SQLException {
		ResultSet rs;
		ResultSetMetaData rsmd;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<String> data = new ArrayList<>();

		
		String tempString = String.format("SELECT car.carLicenseNumber " + 
				"FROM myfueldb.car " + 
				"WHERE car.FK_customerId = \"%s\";", customerId);
		rs = stmt.executeQuery(tempString);  //Run the query.
		rsmd = (ResultSetMetaData)rs.getMetaData();
		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i).toString()); // Add value from the i column
			}
		}
		
		return data;
	}

	/**This method find the model type that customer is related to.
	 * @param customerId is string that represent the customer id.
	 * @param sv instance of the ServerLogInController.java class.
	 * @return String that represent the customer model type.
	 * @throws SQLException
	 */
	private static String getCustomerModel(String customerId, ServerLogInController sv) throws SQLException {
		ResultSet rs;
		ResultSetMetaData rsmd;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<Object> data = new ArrayList<Object>();
		
		String tempString = String.format("SELECT customer.FK_modelType " + 
				"FROM myfueldb.customer " + 
				"WHERE customer.id = \"%S\";", customerId);
		rs = stmt.executeQuery(tempString);  //Run the query.
		rsmd = (ResultSetMetaData)rs.getMetaData();
		
		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i)); // Add value from the i column
			}
		}
		
		return data.get(0).toString();
	}

	/**This method get ArrayList of customer car license numbers and return all the
	 * some relevant data.
	 * @param carLicenseNumbers is ArrayList of type String that represent all the car license numbers of customer.
	 * @param sv instance of the ServerLogInController.java class.
	 * @return all the relevant data for the allPurchasesTableView TableView in VehicleFuelPurcheses.fxml.
	 * @throws SQLException
	 */
	private static ArrayList<ArrayList<Object>> getDataForAllPurchasesTable(ArrayList<String> carLicenseNumbers, ServerLogInController sv) throws SQLException {
		ResultSet rs;
		ResultSetMetaData rsmd;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<Object> data = new ArrayList<Object>();
		
		String tempString = String.format("SELECT fuelpurchases.FK_carLicenseNumber, " + 
				"gasstation.gasStationName, fuelpurchases.FK_fuelType, " + 
				"fuelpurchases.fuelAmount, fuelpurchases.fuelPurchasePrice, " + 
				"fuelpurchases.purchasesDay, fuelpurchases.purchasesTime " + 
				"FROM myfueldb.fuelpurchases, myfueldb.gasstation " + 
				"WHERE fuelpurchases.FK_gasStationId = gasstation.gasStationId " + 
				"AND fuelpurchases.FK_carLicenseNumber IN (");
		
		for (int i = 0; i < carLicenseNumbers.size(); i++) {
			if(i == (carLicenseNumbers.size()-1))
				tempString+=String.format("\"%s\");", carLicenseNumbers.get(i));
			else {
				tempString+=String.format("\"%s\", ", carLicenseNumbers.get(i));
			}	
		}
		
		rs = stmt.executeQuery(tempString);  //Run the query.
		rsmd = (ResultSetMetaData)rs.getMetaData();
		
		ArrayList<ArrayList<Object>> newMsg = new ArrayList<ArrayList<Object>>();
		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i)); // Add value from the i column
			}
			newMsg.add(data);
			data = new ArrayList<Object>();
		}
		
		return newMsg;
	}

	/**This method get list of car license numbers that belong to customer, 
	 * and return all the relevant data.
	 * and return data of the purchases day and purchase price
	 * of the vehicle fuel buying.
	 * @param carLicenseNumbers the list of the cars license numbers.
	 * @param sv instance of the ServerLogInController.java class.
	 * @return all the relevant data for the monthlyPaymentsTableView TableView in VehicleFuelPurcheses.fxml.
	 * @throws SQLException
	 */
	private static ArrayList<ArrayList<Object>> getDataForMonthlyPaymentsTable(ArrayList<String> carLicenseNumbers, ServerLogInController sv) throws SQLException {
		ResultSet rs;
		ResultSetMetaData rsmd;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<Object> data = new ArrayList<Object>();
		
		String tempString = String.format("SELECT fuelpurchases.purchasesDay, fuelpurchases.fuelPurchasePrice " + 
				"FROM myfueldb.fuelpurchases " + 
				"WHERE fuelpurchases.FK_carLicenseNumber IN(");
		
		for (int i = 0; i < carLicenseNumbers.size(); i++) {
			if(i == (carLicenseNumbers.size()-1))
				tempString+=String.format("\"%s\");", carLicenseNumbers.get(i));
			else {
				tempString+=String.format("\"%s\", ", carLicenseNumbers.get(i));
			}	
		}
		
		rs = stmt.executeQuery(tempString);  //Run the query.
		rsmd = (ResultSetMetaData)rs.getMetaData();
		
		ArrayList<ArrayList<Object>> newMsg = new ArrayList<ArrayList<Object>>();
		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i)); // Add value from the i column
			}
			handleNewRow(newMsg, data);
			data = new ArrayList<Object>();
		}
	
		
		
		
		return newMsg;
	}

	/**This method handle row of data that arrived from the DB. 
	 * @param newMsg this is the ArrayList that contain all the data from the DB, that is order by certain order.
	 * @param data this is ArrayList from type Object that contain row of that from the DB. 
	 */
	private static void handleNewRow(ArrayList<ArrayList<Object>> newMsg, ArrayList<Object> data) {
		ArrayList<Object> temp = new ArrayList<Object>();
		if(newMsg.isEmpty()) {//The case newMsg is empty
			LocalDate date = LocalDate.parse(data.get(0).toString());
			Integer year = date.getYear();
			Integer month = date.getMonthValue();
			temp.add(year);
			temp.add(month);
			temp.add(Float.parseFloat(data.get(1).toString()));
			newMsg.add(temp);
		}else {
			int flag = 0;
			for (int i = 0; i < newMsg.size(); i++) {
				temp = newMsg.get(i);
				LocalDate date = LocalDate.parse(data.get(0).toString());
				Integer year = date.getYear();
				Integer month = date.getMonthValue();
				
				if(temp.get(0).equals(year)
						&& temp.get(1).equals(month)) {
					flag = 1;
					float sum = (float) temp.get(2) + 
							Float.parseFloat(data.get(1).toString());
					temp.remove(2);
					temp.add(sum);
					break;
				}
			}
			if(flag == 0) {
				LocalDate date = LocalDate.parse(data.get(0).toString());
				Integer year = date.getYear();
				Integer month = date.getMonthValue();
				temp = new ArrayList<Object>();
				temp.add(year);
				temp.add(month);
				temp.add(Float.parseFloat(data.get(1).toString()));
				newMsg.add(temp);
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
