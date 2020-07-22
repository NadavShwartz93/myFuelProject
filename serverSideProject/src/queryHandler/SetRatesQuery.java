package queryHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import controller.ServerLogInController;
import emServer.mySqlConnection;
/**
 * This function is called to select, insert, update or delete elements in fuel table.
 * @author Boaz Trauthwein
 *
 */
public class SetRatesQuery {
	@SuppressWarnings("unchecked")
	public static void GetRates(Object Msg, ServerLogInController sv) throws SQLException {

		ArrayList<String> oldMsg = (ArrayList<String>) Msg;
		ArrayList<Object> answer = new ArrayList<Object>(); // contain the answer that will be send.
		ArrayList<Object> newColumsTbl1 = new ArrayList<Object>();
		ArrayList<Object> newColumsTbl2 = new ArrayList<Object>();
		Statement stmt = mySqlConnection.conn.createStatement();
		ResultSet rs;
		String tempString;
		ResultSetMetaData rsmd;

		answer.add(oldMsg.get(0));

		tempString = "SELECT fuelPrice, maxFuelPrice FROM myfueldb.fuel;";
		rs = stmt.executeQuery(tempString); // Run the query.
		rsmd = (ResultSetMetaData) rs.getMetaData();
		while (rs.next()) {
			ArrayList<Object> row = new ArrayList<Object>();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				row.add(rs.getObject(i)); // Add value from the i column
			}
			newColumsTbl1.add(row);
		}
		answer.add(newColumsTbl1);

		tempString = "SELECT FK_fuelType, priceType, priceBefore, priceAfter FROM myfueldb.confirmrates;";
		rs = stmt.executeQuery(tempString); // Run the query.
		rsmd = (ResultSetMetaData) rs.getMetaData();
		while (rs.next()) {
			ArrayList<Object> row = new ArrayList<Object>();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				row.add(rs.getObject(i)); // Add value from the i column
			}
			newColumsTbl2.add(row);
		}
		answer.add(newColumsTbl2);

		sv.es.sendToAllClients(answer);

	}
/**
 * This function is called to change a fuel rate.
 * @param Msg - data from client
 * @param sv - server controller.
 * @throws SQLException - when there is a problem with sql syntax.
 */
	public static void requesrChangeMyFuelRates(Object Msg, ServerLogInController sv) throws SQLException {
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<Object> answer = new ArrayList<Object>(); // contain the answer that will be send.
		String tempString;
		ResultSetMetaData rsmd;
		ResultSet rs;
		Float oldPrice;
		int count;
		String priceType = "";
		String fuelType = "";

		ArrayList<String> oldMsg = (ArrayList<String>) Msg;

		if (oldMsg.get(4).compareTo("setMyFuelRates") == 0)
			priceType = "fuelPrice";
		else
			priceType = "maxFuelPrice";

		ArrayList<String> arrFuelTypes = new ArrayList<String>(
				Arrays.asList("Petrol", "Diesel", "ScooterFuel", "HomeFuel"));

		for (int i = 0; i < arrFuelTypes.size(); i++) {
			if (oldMsg.get(i).compareTo("") != 0) {
				fuelType = arrFuelTypes.get(i);

				PreparedStatement ps;
				tempString = "SELECT COUNT(FK_fuelType) FROM myfueldb.confirmrates WHERE FK_fuelType=\"" + fuelType
						+ "\" AND priceType = \"" + priceType + "\";";
				rs = stmt.executeQuery(tempString);
				rsmd = (ResultSetMetaData) rs.getMetaData();
				rs.next();
				count = Integer.parseInt(String.valueOf(rs.getObject(1)));
				if (count > 0) {
					ps = mySqlConnection.conn
							.prepareStatement("UPDATE myfueldb.confirmrates SET priceAfter = ? WHERE FK_fuelType = \""
									+ fuelType + "\" AND priceType = \"" + priceType + "\";");
					ps.setFloat(1, Float.parseFloat(oldMsg.get(i)));
					ps.executeUpdate();
				} else {
					// Getting old Petrol Price
					tempString = "SELECT " + priceType + " FROM myfueldb.fuel WHERE fuelType = \"" + fuelType + "\";";

					rs = stmt.executeQuery(tempString);
					rsmd = (ResultSetMetaData) rs.getMetaData();
					rs.next();
					oldPrice = Float.parseFloat(String.valueOf(rs.getObject(1)));
					// Inserting the request to change new price for Petrol.
					ps = mySqlConnection.conn.prepareStatement(
							"INSERT INTO myfueldb.confirmrates (FK_fuelType, priceType, priceBefore, priceAfter) VALUES (?,?,?,?);");
					ps.setString(1, fuelType);
					ps.setString(2, priceType);
					ps.setFloat(3, oldPrice);
					ps.setFloat(4, Float.parseFloat(oldMsg.get(i)));
					ps.executeUpdate();
				}
			}

		}
		answer.add("GetAllRates");
		GetRates(answer, sv);

	}
/**
 * This function is called to update a fuel rate.
 * @param Msg - data from client
 * @param sv - server controller.
 * @throws SQLException - when there is a problem with sql syntax.
 */
	public static void updateFuelRates(Object Msg, ServerLogInController sv) throws SQLException {
		ArrayList<String> oldMsg = (ArrayList<String>) Msg;
		ArrayList<Object> answer = new ArrayList<Object>(); // contain the answer that will be send.
		PreparedStatement ps, ps3;
		String priceType = "";

		if (String.valueOf(oldMsg.get(2)).compareTo("MyFuel Rates") == 0) {
			ps = mySqlConnection.conn.prepareStatement("UPDATE myfueldb.fuel SET fuelPrice = ? WHERE fuelType = ?;");
			priceType = "fuelPrice";
			ps.setFloat(1, Float.parseFloat(String.valueOf(oldMsg.get(3))));
			ps.setString(2, String.valueOf(oldMsg.get(1)));
			ps.executeUpdate();
		} else {
			Statement stmt = mySqlConnection.conn.createStatement();
			ResultSet rs;
			String tempString;
			ResultSetMetaData rsmd;

			tempString = String.format("SELECT fuelPrice FROM myfueldb.fuel WHERE fuelType = \"%s\";",
					String.valueOf(oldMsg.get(1)));
			rs = stmt.executeQuery(tempString); // Run the query.
			rsmd = (ResultSetMetaData) rs.getMetaData();
			rs.next();
			Float fuelPrice = Float.parseFloat(String.valueOf(rs.getObject(1)));

			if (fuelPrice > Float.parseFloat(String.valueOf(oldMsg.get(3)))) {
				Float temp = Float.parseFloat(String.valueOf(oldMsg.get(3)));
				ps = mySqlConnection.conn.prepareStatement(
						"UPDATE myfueldb.fuel SET maxFuelPrice = ?, fuelPrice = ? WHERE fuelType = ?;");
				priceType = "maxFuelPrice";//Its for ps3;
				ps.setFloat(1, Float.parseFloat(String.valueOf(oldMsg.get(3))));
				ps.setFloat(2, Float.parseFloat(String.valueOf(oldMsg.get(3))));
				ps.setString(3, String.valueOf(oldMsg.get(1)));
				ps.executeUpdate();
			} else {
				ps = mySqlConnection.conn
						.prepareStatement("UPDATE myfueldb.fuel SET maxFuelPrice = ? WHERE fuelType = ?;");
				priceType = "maxFuelPrice";//Its for ps3;
				ps.setFloat(1, Float.parseFloat(String.valueOf(oldMsg.get(3))));
				ps.setString(2, String.valueOf(oldMsg.get(1)));
				ps.executeUpdate();
			}
		}

		ps3 = mySqlConnection.conn
				.prepareStatement("DELETE FROM myfueldb.confirmrates WHERE FK_fuelType = ? AND priceType = ?;");
		ps3.setString(1, String.valueOf(oldMsg.get(1)));
		ps3.setString(2, priceType);
		ps3.executeUpdate();

		answer.add(oldMsg.get(0));

		GetRates(answer, sv);
	}
/**
 * This function is called to delete a fuel rate.
 * @param Msg - data from client
 * @param sv - server controller.
 * @throws SQLException - when there is a problem with sql syntax.
 */
	public static void deleteFuelRate(Object Msg, ServerLogInController sv) throws SQLException {
		// TODO Auto-generated method stub
		ArrayList<String> oldMsg = (ArrayList<String>) Msg;
		ArrayList<Object> answer = new ArrayList<Object>(); // contain the answer that will be send.
		PreparedStatement ps;
		String priceType = "";

		if (String.valueOf(oldMsg.get(2)).compareTo("MyFuel Rates") == 0) 
			priceType = "fuelPrice";
		else
			priceType = "maxFuelPrice";
		
		ps = mySqlConnection.conn
				.prepareStatement("DELETE FROM myfueldb.confirmrates WHERE FK_fuelType = ? AND priceType = ?;");
		ps.setString(1, String.valueOf(oldMsg.get(1)));
		ps.setString(2, priceType);
		ps.executeUpdate();

		answer.add(oldMsg.get(0));

		GetRates(answer, sv);
		
		
	}

}
