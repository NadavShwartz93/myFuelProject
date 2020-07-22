package queryHandler.stationManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import emServer.mySqlConnection;

public class UpdateThresholdQuery {
	
	public UpdateThresholdQuery() {
		
	}
	
	public static void UpdateThreshold(Object newMsg) throws SQLException {
		@SuppressWarnings("unchecked")
		ArrayList<String> oldMsg = (ArrayList<String>)newMsg;
		Statement stmt = mySqlConnection.conn.createStatement();
		String tempString = String.format("UPDATE inventory i, gasstation gs SET fuelLimit= \"%s\" WHERE FK_fuelType= \"%s\" AND i.FK_gasStationId = gs.gasStationId AND gs.FK_gasStationManager = \"%s\";", oldMsg.get(0), oldMsg.get(1), oldMsg.get(2));
 		stmt.executeUpdate(tempString);
	}

}
