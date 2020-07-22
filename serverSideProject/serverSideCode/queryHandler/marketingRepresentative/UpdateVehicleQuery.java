package queryHandler.marketingRepresentative;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import emServer.mySqlConnection;

public class UpdateVehicleQuery {
	
	/**
	 * This method update vehicle inside table.
	 * @param newMsg contain the relevant data from client.
	 * @throws SQLException error with sql syntax.
	 */
	public static void updateVehicle(Object newMsg) throws SQLException {
		@SuppressWarnings("unchecked")
		ArrayList<String> oldMsg = (ArrayList<String>)newMsg;
		Statement stmt = mySqlConnection.conn.createStatement();
		String tempString = String.format("UPDATE myfueldb.car SET carOwner = \"%s\" WHERE carLicenseNumber = \"%s\";", oldMsg.get(0), oldMsg.get(1));
 		stmt.executeUpdate(tempString); 		
 		
	}
}