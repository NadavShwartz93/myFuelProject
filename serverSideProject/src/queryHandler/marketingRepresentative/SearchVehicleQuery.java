package queryHandler.marketingRepresentative;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import controller.ServerLogInController;
import emServer.mySqlConnection;

public class SearchVehicleQuery {


	public SearchVehicleQuery() {
		
	}
	/**
	 * This method return from db vehicle details.
	 * @param Msg contain the relevant data from client.
	 * @param sv is an instance of {@link ServerLogInController}.
	 * @throws SQLException error with sql syntax.
	 */
	public static void SearchVehicle(Object Msg, ServerLogInController sv) throws SQLException {
		
		@SuppressWarnings("unchecked")
		ArrayList<String> oldMsg = (ArrayList<String>)Msg;
		ArrayList<Object> newMsg = new ArrayList<Object>();
		ArrayList<Object> answer = new ArrayList<Object>() ;

		Statement stmt = mySqlConnection.conn.createStatement();
		ResultSet rs;
		if(oldMsg.size() == 3) {
			String tempString = String.format("SELECT car.carOwner, car.carLicenseNumber, car.Manufacturer, car.carModel, car.manufacturerYear, car.FK_fuelType FROM myfueldb.car WHERE car.%s = '%s';", oldMsg.get(0) ,oldMsg.get(1));
	 		rs=stmt.executeQuery(tempString);
			answer.add(oldMsg.get(2));
		}
		else {
			String tempString = String.format("SELECT car.carOwner, car.carLicenseNumber, car.Manufacturer, car.carModel, car.manufacturerYear, car.FK_fuelType FROM myfueldb.car WHERE car.%s = '%s' AND car.%s = '%s';", oldMsg.get(0) ,oldMsg.get(1), oldMsg.get(2), oldMsg.get(3));
	 		rs=stmt.executeQuery(tempString);
			answer.add(oldMsg.get(4));
		}
			
		
		ResultSetMetaData rsmd = (ResultSetMetaData)rs.getMetaData();
		
		while (rs.next()) {
			ArrayList<Object> data = new ArrayList<Object>();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i));
			}
			newMsg.add(data);
		}
		
		if(newMsg.isEmpty()) {
			String tempString = String.format("SELECT EXISTS " + 
					"(SELECT * FROM myfueldb.customer " + 
					"WHERE customer.id = '%s');", oldMsg.get(1));
			
			rs = stmt.executeQuery(tempString);  //Run the query.
			rsmd = (ResultSetMetaData)rs.getMetaData();
			
			ArrayList<Object> data = new ArrayList<Object>();
			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					data.add(rs.getObject(i));
				}
			}
			Integer num = Integer.parseInt(data.get(0).toString());
			if(num == 1)
				answer.add("Customer exists");
			else
				answer.add("Customer not exists");
			
		}else {
			answer.add("Customer exists");
		}
		answer.add(newMsg);
		rs.close();

		//mysqlConnection.
		sv.es.sendToAllClients(answer);
	 	}
	
	}