package queryHandler.stationManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import emServer.mySqlConnection;

public class ApproveFuelOrderQuery {

	public static void UpdateThreshold(Object newMsg) throws SQLException {
		@SuppressWarnings("unchecked")
		ArrayList<String> oldMsg = (ArrayList<String>)newMsg;
		Statement stmt = mySqlConnection.conn.createStatement();
		String tempString = String.format("UPDATE myfueldb.inventoryorder i, myfueldb.gasstation gs " +
				"SET i.confirmStatus = '1' " +
				"WHERE i.FK_gasStationId = gs.gasStationId " +
				"AND gs.FK_gasStationManager = '" + oldMsg.get(0) + "'" +
				"AND i.FK_fuelType = '" + oldMsg.get(1) +"' " +
				"AND i.fuelAmount = '" + oldMsg.get(2) +"';");
 		stmt.executeUpdate(tempString);
	}
}
