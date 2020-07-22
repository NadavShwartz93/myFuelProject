package queryHandler.marketingRepresentative;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import controller.ServerLogInController;
import emServer.mySqlConnection;

public class GetCompanyQuery {
	
	public GetCompanyQuery() {
	
	}
	 /**
	  *  This method returns existing fuel companies for registration.
	  * @param message contain the relevant data from client.
	  * @param sv is instance of {@link ServerLogInController}
	  * @throws SQLException error with sql syntax
	  */
	public static void GetCompany(Object message, ServerLogInController sv) throws SQLException {
		ResultSet rs;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<String> oldMsg = (ArrayList<String>)message;
		ArrayList<Object> newMsg = new ArrayList<Object>();
		ArrayList<Object> answer = new ArrayList<Object>() ; //contain the answer that will be return.
		ArrayList<Object> data = new ArrayList<Object>();
		answer.add(oldMsg.get(0));

		String tempString = String.format("SELECT gasstationcompany.companyName FROM myfueldb.gasstationcompany\r\n" + 
				"WHERE gasstationcompany.companyId NOT IN(\r\n" + 
				"SELECT gasstation.FK_companyId FROM myfueldb.gasstation\r\n" + 
				"WHERE gasstation.gasStationId IN \r\n" + 
				"(SELECT inventory.FK_gasStationId\r\n" + 
				"FROM myfueldb.inventory\r\n" + 
				"WHERE inventory.FK_fuelType = 'HomeFuel')\r\n" + 
				");");
		rs = stmt.executeQuery(tempString);  //Run the query.
		ResultSetMetaData rsmd = (ResultSetMetaData)rs.getMetaData();
		
		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i)); // Add value from the i column
			}
			newMsg.add(data);
		}
		answer.add(newMsg);
		sv.es.sendToAllClients(answer);
		rs.close();
	}
}