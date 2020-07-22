package queryHandler.marketingManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import controller.ServerLogInController;
import emServer.mySqlConnection;

/**
 * This function is called to to generate reports for marketing manager..
 * 
 * @author Boaz Trauthwein
 *
 */
public class GenerateReportQuery {
	/**
	 * This function is called to generate a sales report.
	 * 
	 * @param Msg - data from client
	 * @param sv  - server controller.
	 * @throws SQLException - when there is a problem with sql syntax.
	 */
	@SuppressWarnings("unchecked")
	public static void GenerateSalesReport(Object Msg, ServerLogInController sv) throws SQLException {

		ArrayList<String> oldMsg = (ArrayList<String>) Msg;
		ArrayList<Object> answer = new ArrayList<Object>(); // contain the answer that will be send.
		ArrayList<Object> tableFuelPurchases = new ArrayList<Object>();

		Statement stmt = mySqlConnection.conn.createStatement();
		ResultSet rs;
		String tempString;
		ResultSetMetaData rsmd;

		answer.add(oldMsg.get(0));

		// Getting specific fuelPurchases by Date and Time.
		tempString = String.format(
				"SELECT * FROM myfueldb.fuelpurchases WHERE FK_fuelTYpe = \"%s\" AND purchasesDay BETWEEN \"%s\" AND \"%s\" AND purchasesTime BETWEEN \"%s\" AND \"%s\";",
				String.valueOf(oldMsg.get(1)), String.valueOf(oldMsg.get(2)), String.valueOf(oldMsg.get(3)),
				String.valueOf(oldMsg.get(4)), String.valueOf(oldMsg.get(5)));
		rs = stmt.executeQuery(tempString); // Run the query.
		rsmd = (ResultSetMetaData) rs.getMetaData();
		ArrayList<String> arrCarLicense = new ArrayList<String>();
		while (rs.next()) {
			ArrayList<Object> row = new ArrayList<Object>();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				row.add(rs.getObject(i)); // Add value from the i column
				if (i == 2)// carLicenseNumber
					if (arrCarLicense.contains(String.valueOf(rs.getObject(i))) == false)
						arrCarLicense.add(String.valueOf(rs.getObject(i)));
			}
			tableFuelPurchases.add(row);
		}

		ArrayList<Object> newtbl = new ArrayList<Object>();
		ArrayList<Float> dollar = new ArrayList<Float>();
		ArrayList<String> arrCustomerId = new ArrayList<String>();
		for (int i = 0; i < arrCarLicense.size(); i++) {
			float totalAmountOfPurchases = 0;
			for (int j = 0; j < tableFuelPurchases.size(); j++) {
				ArrayList<Object> row = (ArrayList<Object>) tableFuelPurchases.get(j);
				if (arrCarLicense.get(i).compareTo(String.valueOf(row.get(1))) == 0)
					totalAmountOfPurchases += Float.parseFloat(String.valueOf(row.get(5)));
			}

			tempString = String.format("SELECT FK_customerId FROM myfueldb.car WHERE carLicenseNumber = \"%s\";",
					arrCarLicense.get(i));
			rs = stmt.executeQuery(tempString); // Run the query.
			rsmd = (ResultSetMetaData) rs.getMetaData();
			rs.next();
			String customerId = String.valueOf(rs.getObject(1));

			if (arrCustomerId.contains(customerId) == true) {
				for (int k = 0; k < arrCustomerId.size(); k++) {
					if (arrCustomerId.get(k).compareTo(customerId) == 0)
						dollar.set(k, dollar.get(k) + totalAmountOfPurchases);
				}
			} else {
				arrCustomerId.add(new String(customerId));
				dollar.add(new Float(totalAmountOfPurchases));
			}

		}

		for (int i = 0; i < arrCustomerId.size(); i++) {
			ArrayList<Object> row = new ArrayList<Object>();
			row.add(arrCustomerId.get(i));
			row.add(dollar.get(i));
			newtbl.add(row);
		}

		answer.add(newtbl);
		sv.es.sendToAllClients(answer);

	}

	/**
	 * This function is calledto generate a periodic report.
	 * 
	 * @param Msg - data from client
	 * @param sv  - server controller.
	 * @throws SQLException - when there is a problem with sql syntax.
	 */
	public static void GeneratePeriodicReport(Object Msg, ServerLogInController sv) throws SQLException {
		// TODO Auto-generated method stub
		ArrayList<Object> oldMsg = (ArrayList<Object>) Msg;
		ArrayList<Object> answer = new ArrayList<Object>(); // contain the answer that will be send.

		Statement stmt = mySqlConnection.conn.createStatement();
		ResultSet rs;
		String tempString, strHelp = "";
		ResultSetMetaData rsmd;

		answer.add(String.valueOf(oldMsg.get(0)));
		ArrayList<Object> tblPurcheses = new ArrayList<Object>();
		tempString = String.format(
				"SELECT FK_gasStationId, FK_carLicenseNumber, fuelPurchasePrice FROM myfueldb.fuelpurchases WHERE purchasesDay BETWEEN \"%s\" AND \"%s\";",
				String.valueOf(oldMsg.get(1)), String.valueOf(oldMsg.get(2)));
		rs = stmt.executeQuery(tempString); // Run the query.
		rsmd = (ResultSetMetaData) rs.getMetaData();
		while (rs.next()) {
			ArrayList<Object> row = new ArrayList<Object>();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				row.add(rs.getObject(i)); // Add value from the i column
			}
			tblPurcheses.add(row);
		}
		// Map<companyID,CompanyName>
		Map<String, String> gasStations = new HashMap<String, String>();
		// Map<carNumber,customerId>
		Map<String, String> customers = new HashMap<String, String>();
		// Finding gasStation Company Name and customer Id to return usefull data to
		// controller
		for (int i = 0; i < tblPurcheses.size(); i++) {
			ArrayList<Object> row = (ArrayList<Object>) tblPurcheses.get(i);
			if (!gasStations.containsKey(String.valueOf(row.get(0)))) {
				tempString = String.format(
						"SELECT companyName FROM myfueldb.gasstationcompany WHERE companyId IN (SELECT FK_companyID FROM  myfueldb.gasstation WHERE gasstationId = \"%s\");",
						String.valueOf(row.get(0)));
				rs = stmt.executeQuery(tempString); // Run the query.
				rs.next();
				gasStations.put(String.valueOf(row.get(0)), String.valueOf(rs.getObject(1)));
			}
			if (!customers.containsKey(String.valueOf(row.get(1)))) {
				tempString = String.format("SELECT FK_customerId FROM myfueldb.car WHERE carLicenseNumber = \"%s\";",
						String.valueOf(row.get(1)));
				rs = stmt.executeQuery(tempString); // Run the query.
				rs.next();
				customers.put(String.valueOf(row.get(1)), String.valueOf(rs.getObject(1)));
			}
		}

		// Put the right data found in table
		for (int i = 0; i < tblPurcheses.size(); i++) {
			ArrayList<Object> row = (ArrayList<Object>) tblPurcheses.get(i);
			row.set(0, new String(gasStations.get(row.get(0))));
			row.set(1, new String(customers.get(row.get(1))));
		}

		answer.add(tblPurcheses);
		sv.es.sendToAllClients(answer);
	}

}
