package queryHandler.stationManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import controller.ServerLogInController;
import emServer.mySqlConnection;

public class GetOrdersToApproveQuery {

	public static void getOrders(Object Msg, ServerLogInController sv) throws SQLException {
		
		@SuppressWarnings("unchecked")
		ArrayList<String> oldMsg = (ArrayList<String>)Msg;
		ArrayList<Object> newMsg = new ArrayList<Object>();
		ArrayList<Object> answer = new ArrayList<Object>() ;
		answer.add("approveOrder");

		
		Statement stmt = mySqlConnection.conn.createStatement();
		ResultSet rs;

		//String tempString = "SELECT ior.FK_fuelType, ior.fuelAmount, ior.deliveryDate FROM myfueldb.inventoryorder ior, myfueldb.gasstation gs WHERE ior.FK_gasStationId = gs.gasStationId AND gs.FK_gasStationManager = '"+ oldMsg.get(0) +"' AND ior.confirmStatus = '0';";
		String tempString = "SELECT ior.FK_fuelType, ior.fuelAmount, ior.generateOrderDate FROM myfueldb.inventoryorder ior, myfueldb.gasstation gs WHERE ior.FK_gasStationId = gs.gasStationId AND gs.FK_gasStationManager = '"+ oldMsg.get(0) +"' AND ior.confirmStatus = '0';";
 		rs=stmt.executeQuery(tempString);
 		
		ResultSetMetaData rsmd = (ResultSetMetaData)rs.getMetaData();
		
		while (rs.next()) {
			ArrayList<Object> data = new ArrayList<Object>();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i));
			}
			newMsg.add(data);
			System.out.println(newMsg);
		}
		
		answer.add(newMsg);
		//mysqlConnection
		sv.es.sendToAllClients(answer);
		rs.close();
	}
}
