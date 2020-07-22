package queryHandler.stationManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import controller.ServerLogInController;
import emServer.mySqlConnection;
//import sun.util.resources.LocaleData;

public class InventoryReportQuery {
		
	
	/**
	 * The function execute queries for the Inventory report.
	 * later the function builds the report and save it to the DB.
	 * The method return the full report.
	 * @param Msg - the report type and the employee number of the user.
	 * @param sv  is an instance of {@link ServerLogInController}.
	 */
		public static void getInventory(Object Msg, ServerLogInController sv) throws SQLException {
			
			@SuppressWarnings("unchecked")
			ArrayList<String> oldMsg = (ArrayList<String>)Msg;
			ArrayList<Object> newMsg = new ArrayList<Object>();
			ArrayList<Object> answer = new ArrayList<Object>() ;
			answer.add("inventoryReport");

			
			Statement stmt = mySqlConnection.conn.createStatement();
			ResultSet rs;

			String tempString = "SELECT e.firstName, e.lastName, gsc.companyName, gs.gasStationName, gs.gasStationId FROM myfueldb.employee e, myfueldb.gasstation gs, myfueldb.gasstationcompany gsc WHERE gs.FK_gasStationManager = e.employeeNumber AND gs.FK_gasStationManager = '" + oldMsg.get(0) + "' AND gs.FK_companyId = gsc.companyId;";
	 		rs=stmt.executeQuery(tempString);
	 		
			ResultSetMetaData rsmd = (ResultSetMetaData)rs.getMetaData();
			
			while (rs.next()) {
				ArrayList<Object> data = new ArrayList<Object>();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					data.add(rs.getObject(i));
				}
				newMsg.add(data);
			}
						
			answer.add(newMsg);
			
			tempString = "SELECT i.FK_fuelType, i.inventoryCapacity, i.fuelMax FROM inventory i, myfueldb.gasstation gs WHERE i.FK_gasStationId = gs.gasStationId AND gs.FK_gasStationManager = '" + oldMsg.get(0) + "';";
	 		rs=stmt.executeQuery(tempString);
	 		
			rsmd = (ResultSetMetaData)rs.getMetaData();
			
			while (rs.next()) {
				ArrayList<Object> data = new ArrayList<Object>();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					data.add(rs.getObject(i));
				}
				newMsg.add(data);
			}
			
			if (newMsg.size() == 1)
			{
				int z = 0;
				ArrayList<Object> zeroPertrol = new ArrayList<Object>();
				zeroPertrol.add("Petrol");
				zeroPertrol.add(z);
				zeroPertrol.add(z);
				newMsg.add(zeroPertrol);
				ArrayList<Object> zeroDiesel = new ArrayList<Object>();
				zeroDiesel.add("Diesel");
				zeroDiesel.add(z);
				zeroDiesel.add(z);
				newMsg.add(zeroDiesel);
				ArrayList<Object> zeroScooter = new ArrayList<Object>();
				zeroScooter.add("Scooter Fuel");
				zeroScooter.add(z);
				zeroScooter.add(z);
				newMsg.add(zeroScooter);
			}
			
			String report = createReport(newMsg);
			
			ArrayList<Object> tmp = (ArrayList<Object>) answer.get(1);
			tmp = (ArrayList<Object>)tmp.get(0);
			
			String stationId = (String)tmp.get(4);;
			LocalDate date = LocalDate.now();
			int year = date.getYear();
			int month = date.getMonthValue();
			int quarter;
			
			if (month == 1)
				year--;
			if (month >= 1 && month <= 3)
				quarter = 1;
			else if (month >= 4 && month <= 6)
				quarter = 2;
			else if (month >= 7 && month <= 9)
				quarter = 3;
			else
				quarter = 4;

			try
			{
			tempString = "INSERT INTO `myfueldb`.`quarterreports` (`QuarterNumber`, `qr_Year`, `reportType`, `FK_gasStation_Id`, `theFile`) VALUES ('" + quarter + "', '" + year + "', 'inventory_report', '" + stationId + "', '" + report + "');";
	 		stmt.executeUpdate(tempString);
			}
			catch (SQLException e)
			{
				if (e.getMessage().contains("Duplicate entry"))
				{
					tempString = "SELECT theFile FROM quarterreports WHERE reportType = 'inventory_report' AND FK_gasStation_Id = '" + stationId + "' AND qr_Year = '" + year + "' AND QuarterNumber = '" + quarter + "';";
			 		stmt.executeQuery(tempString);
				}
			}
			answer.add(report);
			
			//mysqlConnection
			sv.es.sendToAllClients(answer);
			rs.close();
		 }

		private static String createReport(ArrayList<Object> reportData) {
	    	String strForFile, strTemp;
			ArrayList<Object> table = (ArrayList<Object>) reportData.get(0);
			strForFile = "";
			strForFile += "Inventory Report:\n\n";
			strForFile += String.format("Gas Station Company:\t%s\n", table.get(2));
			strForFile += String.format("Gas Station Name:\t%s\n", table.get(3));
			strForFile += String.format("Gas Station Manager:\t%s %s\n", table.get(0), table.get(1));

			strForFile += "\n\n";

			strTemp = "";
			for (int i = 1; i < reportData.size(); i++) {
				ArrayList<Object> row = (ArrayList<Object>) reportData.get(i);
				strTemp += String.format("%s:\t%s/%s\n", (String)row.get(0), String.valueOf(row.get(1)), String.valueOf(row.get(2)));
			}
			strForFile += strTemp;
			
			
			
			return strForFile;
		}
		
		
	}

