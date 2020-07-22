package queryHandler.marketingRepresentative;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import controller.ServerLogInController;
import emServer.mySqlConnection;

public class AddVehicleQuery {
	/**
	 * 	This method insert car for specific customer and show this on table.
	 * @param newMsg contain the relevant data from client.
	 * @param sv is an instance of {@link ServerLogInController}.
	 * @throws SQLException error with sql syntax.
	 */

	public static void addVehicle(ArrayList<Object> newMsg, ServerLogInController sv) throws SQLException {
		@SuppressWarnings("unchecked")
		Statement stmt = mySqlConnection.conn.createStatement();
		String tempString = String.format("INSERT INTO myfueldb.car (FK_customerId, carLicenseNumber, FK_fuelType, Manufacturer, carModel, manufacturerYear, carOwner) VALUES ('%s', '%s', '%s', '%s','%s', '%s', '%s')",newMsg.get(0),newMsg.get(1),newMsg.get(2),newMsg.get(3),newMsg.get(4),newMsg.get(5),newMsg.get(6));
 		stmt.executeUpdate(tempString);
 		
 		
		ArrayList<Object> answer = new ArrayList<Object>() ;
		answer.add("saveVehiclTableData");
		
		tempString = String.format("SELECT car.carOwner, car.carLicenseNumber, car.Manufacturer, car.carModel, car.manufacturerYear, car.FK_fuelType "
				+ "FROM myfueldb.car WHERE car.FK_customerId = '%s';", newMsg.get(0));
		ResultSet rs = stmt.executeQuery(tempString);  //Run the query.
		ResultSetMetaData rsmd = (ResultSetMetaData)rs.getMetaData();
		
		newMsg = new ArrayList<Object>();
		ArrayList<String> data = new ArrayList<String>();
		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i).toString()); // Add value from the i column
			}
			newMsg.add(data);
			data = new ArrayList<String>();
		}
		
		answer.add(newMsg);
		
		sv.es.sendToAllClients(answer);
	}
	/**
	 * 	This method insert car for specific customer.
	 * @param newMsg contain the relevant data from client.
	 * @throws SQLException error with sql syntax.
	 */
	public static void addVehicle2(ArrayList<Object> newMsg) throws SQLException {
		@SuppressWarnings("unchecked")
		Statement stmt = mySqlConnection.conn.createStatement();
		String tempString = String.format("INSERT INTO myfueldb.car (FK_customerId, carLicenseNumber, FK_fuelType, Manufacturer, carModel, manufacturerYear, carOwner) VALUES ('%s', '%s', '%s', '%s','%s', '%s', '%s')",newMsg.get(0),newMsg.get(1),newMsg.get(2),newMsg.get(3),newMsg.get(4),newMsg.get(5),newMsg.get(6));
 		stmt.executeUpdate(tempString);
	}
}