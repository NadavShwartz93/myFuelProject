package queryHandler.customer;

import java.lang.annotation.Retention;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.mail.MessagingException;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import controller.ServerLogInController;
import emServer.JavaMailUtil;
import emServer.SendMail;
import emServer.mySqlConnection;

public class BuyVehicleFuelQuery {
	
	/**This method insert new buy vehicle fuel Data into the table.
	 * @param message - contain the relevant data for this query. 
	 * @param sv is instance of ServerLogInController class.
	 * @throws SQLException error with sql syntax.
	 */
	public static void insertBuyVehicleFuelData(ArrayList<Object> message, ServerLogInController sv) throws SQLException {
		ResultSet rs;
		ResultSetMetaData rsmd;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<Object> data = new ArrayList<Object>();
		ArrayList<Object> answer = new ArrayList<Object>();
		answer.add("BuyVehicleFuelView"); //Contain the name in the switch-case to enter in ClientHandlerSingle.
		String tempString = null, tempGasStationId = null;
		int inventoryCapacity = 0;
		int fuelLimit = 0;
		float tempFuelPrice = 0;
		
		String carLicenseNumber = message.get(0).toString(); //Check if this car is exist in car table.
		String fuelAmount = message.get(1).toString(); //Check if this fuel amount is exist in the inventory table.
		String fuelType =  message.get(2).toString();
		String gasStationCompany =  message.get(3).toString();
		String gasStationName =  message.get(4).toString();
		String fuelPurchasePrice = message.get(5).toString(); //This is fuelPurchasePrice.
		String paymentType = message.get(6).toString();
		String customerId =	getCustomerIdByVehicleNumber(message.get(7).toString()); 
		String buyTime = message.get(8).toString();
		String buyDate = message.get(9).toString();
		String pumpNumber = message.get(10).toString();
		
		/**
		 * Check if the carLicenseNumber is existing in the car table, and belong to this customer .
		 */
		tempString = String.format("SELECT car.carLicenseNumber FROM car WHERE car.carLicenseNumber = \"%s\" "
				+ "AND car.FK_customerId = \"%s\";", carLicenseNumber, customerId);
		rs = stmt.executeQuery(tempString);  //Run the query.
		rsmd = (ResultSetMetaData)rs.getMetaData();
		int flag = 0;
		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				flag = 1;
			}
		}
		//The case that carLicenseNumber didn't found.
		if(flag == 0) {
			answer.add("Erorr - carLicenseNumber don't existing in car table");
			sv.es.sendToAllClients(answer);
		}else {
			answer.add("Found - carLicenseNumber existing in car table");
			flag = 0;
			
			/**
			 * Check if their is enough fuel in the inventory table.
			 */
			tempString = String.format("SELECT inventory.inventoryCapacity, inventory.FK_gasStationId, inventory.fuelLimit FROM inventory " + 
					"WHERE inventory.FK_gasStationId IN " + 
					"(SELECT gasstation.gasStationId FROM gasstation WHERE gasstation.gasStationName = \"%s\") " + 
					"AND inventory.FK_fuelType = \"%s\" AND inventory.inventoryCapacity > \"%s\";", gasStationName, fuelType, fuelAmount);
			rs = stmt.executeQuery(tempString);  //Run the query.
			rsmd = (ResultSetMetaData)rs.getMetaData();
			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					flag = 1;
					data.add(rs.getObject(i)); // Getting inventoryCapacity.
				}
				//Convert this string to int.
				inventoryCapacity = Integer.parseInt(data.get(0).toString()); 
				tempGasStationId = data.get(1).toString();
				fuelLimit = Integer.parseInt(data.get(2).toString()); 
			}
			
			//The case that the query return empty - their isn't enough fuel.
			if(flag == 0 ) {
				try {
					updateNewInventoryOrder(fuelType, gasStationName);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
				answer.add("Erorr-their isn't enough fuel in this gasStationName");
				sv.es.sendToAllClients(answer);
				
			}else {
				answer.add("Found - their is enough fuel in this gasStationName");
				flag = 0;
				//run update query for update the inventory capacity of this fuel.
				inventoryCapacity = inventoryCapacity - Integer.parseInt(fuelAmount);
				tempString = String.format("UPDATE myfueldb.inventory " + 
						"SET inventoryCapacity = \"%d\" " + 
						"WHERE FK_fuelType = \"%s\" AND FK_gasStationId = \"%s\";", inventoryCapacity, fuelType, tempGasStationId);
				stmt.executeUpdate(tempString);  //Run the query.
				
				//run the insert query to the fuelpurchases table.
				tempString = String.format("INSERT INTO myfueldb.fuelpurchases " + 
						"(" + 
						"FK_carLicenseNumber, " + 
						"FK_gasStationId, " + 
						"FK_fuelType, " + 
						"fuelAmount, " + 
						"fuelPurchasePrice, " + 
						"pumpNumber, " + 
						"paymentType, purchasesDay, purchasesTime) " + 
						"VALUES( " + 
						"\"%s\", " + 
						"\"%s\", " + 
						"\"%s\", " + 
						"\"%s\", " + 
						"\"%s\", " + 
						"\"%s\", " + 
						"\"%s\", \"%s\", \"%s\");", carLicenseNumber, tempGasStationId, 
						fuelType, fuelAmount, fuelPurchasePrice, pumpNumber, paymentType, buyDate, buyTime);
				stmt.executeUpdate(tempString);  //Run the insert query.
				answer.add("Success - data successfully inserted to the table");
				
				//Check that the current amount of fuel is above the limit.
				checkFuelAmountInInvetory(fuelType, tempGasStationId, gasStationName);
				sv.es.sendToAllClients(answer);
			}
		}
	
	}
	
	/**This method check if the fuel capacity is less then the fuel limit, if true, 
	 * then call to updateNewInventoryOrder(String fuelType, String gasStationName) that make the new invitation.
	 * @param fuelType represent the selected fuel type.
	 * @param gasStationId represent the id of the gas station.
	 * @param gasStationName represent the name of the gas station.
	 * @throws SQLException error with sql syntax.
	 */
	public static void checkFuelAmountInInvetory(String fuelType, String gasStationId, String gasStationName) throws SQLException {
		ResultSet rs;
		ResultSetMetaData rsmd;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<Object> data = new ArrayList<Object>();
		
		String tempString = String.format("SELECT EXISTS( " + 
				"SELECT * " + 
				"FROM myfueldb.inventory " + 
				"WHERE inventory.inventoryCapacity <= inventory.fuelLimit " + 
				"AND inventory.FK_gasStationId = \"%s\" AND inventory.FK_fuelType = \"%s\" " + 
				") as truth;", gasStationId, fuelType);
		
		rs = stmt.executeQuery(tempString);  //Run the query.
		rsmd = (ResultSetMetaData)rs.getMetaData();
		
		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i)); // Add value from the i column
			}
		}
		
		int bool = Integer.parseInt((data.get(0).toString()));
		data.clear();
		if(bool == 1) {
			try {
				updateNewInventoryOrder(fuelType, gasStationName);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	/**This method generate new order of vehicle fuel, the new order will be insert to the inventoryorder table.
	 * @param fuelType represent the selected fuel type.
	 * @param gasStationName represent the name of the gas station
	 * @throws SQLException error with sql syntax.
	 * @throws MessagingException error with mail sending syntax.
	 */
	public static void updateNewInventoryOrder(String fuelType, String gasStationName) throws SQLException, MessagingException {
		ResultSet rs;
		ResultSetMetaData rsmd;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<Object> data = new ArrayList<Object>();
		
		
		//Getting the id of the given gas station name.
		String tempString = String.format("SELECT gasstation.gasStationId FROM myfueldb.gasstation " + 
				"WHERE gasstation.gasStationName = \"%s\";", gasStationName);
		
		rs = stmt.executeQuery(tempString);  //Run the query.
		rsmd = (ResultSetMetaData)rs.getMetaData();
		
		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i)); // Add value from the i column
			}
		}
		
		String gasStationId = data.get(0).toString();
		data.clear();
		
		//Check if there is an invitation in the inventoryorder table for this fuelType and gasStationId.
		tempString = String.format("SELECT EXISTS(SELECT * " + 
				"FROM myfueldb.inventoryorder " + 
				"WHERE inventoryorder.FK_fuelType = \"%s\" " + 
				"AND inventoryorder.FK_gasStationId = \"%s\") as truth;", fuelType, gasStationId);
		rs = stmt.executeQuery(tempString);  //Run the query.
		rsmd = (ResultSetMetaData)rs.getMetaData();
		
		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i)); // Add value from the i column
			}
		}
		
		int bool = Integer.parseInt((data.get(0).toString()));
		data.clear();
		if(bool == 1) {//There is fuel order for this fuelType and gasStationId
			return;
		}else {//There isn't fuel order for this fuelType and gasStationId
			tempString = String.format("SELECT inventory.fuelMax-inventory.inventoryCapacity as fuelToOrder " + 
					"FROM myfueldb.inventory " + 
					"WHERE inventory.FK_gasStationId = \"%s\" "
					+ "AND inventory.FK_fuelType = \"%s\";", gasStationId, fuelType);
			
			rs = stmt.executeQuery(tempString);  //Run the query.
			rsmd = (ResultSetMetaData)rs.getMetaData();
			
			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					data.add(rs.getObject(i)); // Add value from the i column
				}
			}
			
			int fuelAmountToOrder = Integer.valueOf(data.get(0).toString());
			tempString = String.format("INSERT INTO myfueldb.inventoryorder " + 
					"(FK_fuelType, " + 
					"FK_gasStationId, " + 
					"confirmStatus, " + 
					"deliveryDate, " + 
					"generateOrderDate, " + 
					"fuelAmount " + 
					") " + 
					"VALUES " + 
					"(" + 
					"\"%s\", " + 
					"\"%s\", " + 
					"0, " + 
					"null, " + 
					"now(), " + 
					"\"%d\" " + 
					");",fuelType, gasStationId, fuelAmountToOrder);
			stmt.executeUpdate(tempString);  //Run the insert query.
			
			//send email message to STATION_MANAGER to announce about new fuel order for the  inventory.
			sendMailToStationManager(fuelType, gasStationName, fuelAmountToOrder);
			
		}
	}
	
	/**This method send an email message to the STATION_MANAGER to announce about new fuel order for the  inventory.
	 * @param fuelType represent the selected fuel type.
	 * @param gasStationName represent the name of the gas station
	 * @param fuelAmountToOrder represent the amount of the selected fuel.
	 */
	private static void sendMailToStationManager(String fuelType, String gasStationName, int fuelAmountToOrder) {
		String mailTitle = "New fuel order for the  inventory";
		String mailBody = String.format("New fuel order for the inventory,\nFuel type: %s "
				+ "\nGas station name: %s \nFuel amount: %d ", fuelType, gasStationName, fuelAmountToOrder);
				
		new SendMail("swarze39i@gmail.com", mailTitle, mailBody);
	}
	
	/**
	 * @return Getting all the prices of the different fuel type.
	 * @throws SQLException error with sql syntax.
	 */
	public static ArrayList<Object> getVehicleFuelPrice() throws SQLException {
		ResultSet rs;
		ResultSetMetaData rsmd;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<Object> data = new ArrayList<Object>();
		
		//run query to get the price of the fuel.
		String tempString = String.format("SELECT fuel.fuelType, fuel.fuelPrice, fuel.maxFuelPrice "
				+ "FROM myfueldb.fuel WHERE fuel.fuelType != 'HomeFuel';");
		
		rs = stmt.executeQuery(tempString);  //Run the query.
		rsmd = (ResultSetMetaData)rs.getMetaData();
		
		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i)); // Add value from the i column
			}
		}
		return data;
		
	}
	
	/**This method get customer Id, and return the customer model type.
	 * @param customerid - contain the id of the customer.
	 * @return the customer model type of the given customer id.
	 * @throws SQLException error with sql syntax.
	 */
	public static String getCustomerModel(String customerid) throws SQLException{
		ResultSet rs;
		ResultSetMetaData rsmd;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<Object> data = new ArrayList<Object>();
		
		//run query to get the model of customer
		String tempString = String.format("SELECT customer.FK_modelType FROM myfueldb.customer "
				+ "WHERE customer.id = \"%s\";",customerid);
		
		rs = stmt.executeQuery(tempString);  //Run the query.
		rsmd = (ResultSetMetaData)rs.getMetaData();
		
		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i)); // Add value from the i column
			}
		}
		String customerModelType = (String) data.get(0);
		return customerModelType;
	}
	
	/**
	 * @param localTime - the local time that the customer arrived to fuel his vehicle.
	 * @return - the activated sales for this local time.
	 * @throws SQLException error with sql syntax.
	 */
	public static ArrayList<Object> getSale(String localTime) throws SQLException {
		ResultSet rs;
		ResultSetMetaData rsmd;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<Object> data = new ArrayList<Object>();
		
		//run query to get sales
		String tempString = String.format("SELECT sale.FK_fuelType, sale.discountPercent FROM myfueldb.sale " + 
				"WHERE sale.startHour < \"%s\" AND sale.endHour > \"%s\" "
				+ "AND sale.isActive = 1;",localTime,localTime);
		
		rs = stmt.executeQuery(tempString);  //Run the query.
		rsmd = (ResultSetMetaData)rs.getMetaData();
		
		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i)); // Add value from the i column
			}
		}
		
		return data;
		
	}
	
	/**This method get customer Id, and return an ArrayList that contain all the names of gas station company, 
	 * that customer is linked with.
	 * @param customerid this is the Id of customer.
	 * @return all the gas station that  customer is linked to..
	 * @throws SQLException error with sql syntax.
	 */
	public static ArrayList<Object> getCustomerPurchasePlan(String customerid) throws SQLException {
		ResultSet rs;
		ResultSetMetaData rsmd;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<Object> data = new ArrayList<Object>();

		
		//Get the customer purchase plan
		String tempString = String.format("SELECT gasstationcompany.companyName FROM myfueldb.gasstationcompany WHERE gasstationcompany.companyId "
				+ "IN(SELECT purchaseplan.FK_companyId FROM myfueldb.purchaseplan "
				+ "WHERE purchaseplan.FK_customerId = \"%s\");", customerid);
		rs = stmt.executeQuery(tempString);  //Run the query.
		rsmd = (ResultSetMetaData)rs.getMetaData();
		
		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i)); // Add value from the i column
			}
		}
		return data;
	}
	
	/**This method get customer Id, and return the customer registered credit card.
	 * @param customerid is the customer Id.
	 * @return ArrayList that contain all the relevant data.
	 * @throws SQLException
	 */
	private static ArrayList<Object> getCustomerRegisteredCreditCard(String customerid)  throws SQLException  {
		ResultSet rs;
		ResultSetMetaData rsmd;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<Object> data = new ArrayList<Object>();
		
		String tempString = String.format("SELECT customer.creditCardNumber, customer.creditCard_CVV, \r\n" + 
				"customer.creditCard_Month, customer.creditCard_year\r\n" + 
				"FROM myfueldb.customer\r\n" + 
				"WHERE customer.id = \"%s\";", customerid);
		
		rs = stmt.executeQuery(tempString);  //Run the query.
		rsmd = (ResultSetMetaData)rs.getMetaData();
		
		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i)); // Add value from the i column
			}
		}
		return data;
	}

	/**This method get all the relevant data that buy vehicle fuel page require.
	 * @param message - contain the relevant data for calling the helper methods. 
	 * @param sv is an instance of {@link ServerLogInController}.
	 * @throws SQLException error with sql syntax.
	 */
	public static void getCustomerData(ArrayList<Object> message, ServerLogInController sv) throws SQLException {
		ArrayList<Object> answer = new ArrayList<Object>();
		String customerId = getCustomerIdByVehicleNumber(message.get(0).toString());

		answer.add("BuyVehicleFuelView");
		answer.add("/setCustomerData");
		
		if(customerId.equals("VehicleNotExists")) {
			answer.add("VehicleNotExists");
		}else {
			//Get the customer purchase plan.
			ArrayList<Object> temp = getCustomerPurchasePlan(customerId);
			answer.add(temp);
			
			//Get the fuel Price.
			temp = getVehicleFuelPrice();
			answer.add(temp);
			
			//Get customer model type.
			String customerModelType = getCustomerModel(customerId);
			answer.add(customerModelType);
			
			//Get sales.
			LocalTime localTime = (LocalTime) message.get(1);
			temp = getSale(localTime.toString());
			answer.add(temp);
			
			//Get customer registered credit card
			temp = getCustomerRegisteredCreditCard(customerId);
			answer.add(temp);
			
			//Get Gas Station Company and Gas Station.
			answer.add(getGasStationCompanyAndGasStation());
			
			answer.add(getFuelTypesOfCustomer(customerId));
			
		}
		sv.es.sendToAllClients(answer);
	}
	
	
	/**This method get customer car license number, and return the customer Id that association with this car.
	 * @param carLicenseNumber represent the customer car license number.
	 * @return string that represent the customer Id that association with this car.
	 * @throws SQLException
	 */
	private static String getCustomerIdByVehicleNumber(String carLicenseNumber) throws SQLException {
		
		ResultSet rs;
		ResultSetMetaData rsmd;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<Object> data = new ArrayList<Object>();
		
		String tempString = String.format("SELECT car.FK_customerId " + 
				"FROM myfueldb.car " + 
				"WHERE car.carLicenseNumber = \"%s\";", carLicenseNumber);
		
		rs = stmt.executeQuery(tempString);  //Run the query.
		rsmd = (ResultSetMetaData)rs.getMetaData();
		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i)); // Add value from the i column
			}	
		}	
		if(data.isEmpty())
			return "VehicleNotExists"; 
		else
			return data.get(0).toString();
	}
	
	public static ArrayList<ArrayList<Object>> getGasStationCompanyAndGasStation() throws SQLException {
		ResultSet rs;
		ResultSetMetaData rsmd;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<Object> data = new ArrayList<Object>();
		
		String tempString = String.format("SELECT gasstationcompany.companyName,  gasstation.gasStationName " + 
				"FROM myfueldb.gasstation, myfueldb.gasstationcompany " + 
				"WHERE gasstation.FK_companyId = gasstationcompany.companyId;");
		
		rs = stmt.executeQuery(tempString);  //Run the query.
		rsmd = (ResultSetMetaData)rs.getMetaData();
		
		ArrayList<ArrayList<Object>> newMsg = new ArrayList<>();
		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i)); // Add value from the i column
			}
			
			handelNewRow(newMsg, data);		
			data = new ArrayList<>();
		}
		return newMsg;
	}
	
	private static void handelNewRow(ArrayList<ArrayList<Object>> newMsg, ArrayList<Object> data) {
		ArrayList<Object> temp = new ArrayList<>();
		if(newMsg.size() == 0)
			newMsg.add(data);
		else {
			int flag = 0;
			for (int i = 0; i < newMsg.size(); i++) {
				temp = (ArrayList<Object>) newMsg.get(i);
				if(temp.get(0).equals(data.get(0)) && !temp.contains(data.get(1))) {
					temp.add(data.get(1));
					flag = 1;
					break;
				}
				
			}
			if(flag == 0)
				newMsg.add(data);
		}

	}
	
	/**This method get customer Id, and return all the car license numbers and there fuel types.
	 * @param customerid this is the id of customer
	 * @throws SQLException
	 * @return ArrayList that contain all the relevant data.
	 */
	private static ArrayList<ArrayList<Object>> getFuelTypesOfCustomer(String customerid) throws SQLException {
		ResultSet rs;
		ResultSetMetaData rsmd;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<Object> data = new ArrayList<Object>();
		
		String tempString = String.format("SELECT car.carLicenseNumber, car.FK_fuelType\r\n" + 
				"FROM myfueldb.car\r\n" + 
				"WHERE car.FK_customerId = \"%s\";", customerid);
		
		rs = stmt.executeQuery(tempString);  //Run the query.
		rsmd = (ResultSetMetaData)rs.getMetaData();
		
		ArrayList<ArrayList<Object>> newMsg = new ArrayList<>();
		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i)); 
			}
			newMsg.add(data);
			data = new ArrayList<>();
		}
		return newMsg;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
