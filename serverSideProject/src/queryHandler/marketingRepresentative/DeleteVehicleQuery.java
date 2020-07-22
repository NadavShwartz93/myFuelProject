package queryHandler.marketingRepresentative;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import controller.ServerLogInController;
import emServer.mySqlConnection;

public class DeleteVehicleQuery {
	
	public DeleteVehicleQuery(){
		
	}
/**
 * This method send delete vehicle query.
 * @param string contain the relevant data from client.
 * @param sv is an instance of {@link ServerLogInController}.
 * @throws SQLException error with sql syntax.
 */
	public static void DeleteVehicle(String string, ServerLogInController sv) throws SQLException {
		ResultSet rs;
		ResultSetMetaData rsmd;
		Statement stmt = mySqlConnection.conn.createStatement();
		
		String tempString = String.format("SELECT car.FK_customerId " + 
 				"FROM myfueldb.car " + 
 				"WHERE car.carLicenseNumber = '%s';", string);
 		
 		ArrayList<String> data = new ArrayList<String>();
 		rs = stmt.executeQuery(tempString);  //Run the query.
		rsmd = (ResultSetMetaData)rs.getMetaData();
		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i).toString()); // Add value from the i column
			}
		}
		String customerId = data.get(0).toString();
		
		tempString = String.format("DELETE FROM myfueldb.car WHERE carLicenseNumber=\"%s\";",string);
 		stmt.executeUpdate(tempString);
 		
 		
 		data = new ArrayList<String>();
 		data.add("FK_customerId");
 		data.add(customerId);
 		data.add("show vehicle in table");
 		
 		SearchVehicleQuery.SearchVehicle(data, sv);
 		rs.close();
	}
}
