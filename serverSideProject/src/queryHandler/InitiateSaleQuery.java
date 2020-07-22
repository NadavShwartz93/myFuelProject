package queryHandler;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import controller.ServerLogInController;
import emServer.mySqlConnection;
/**
 * This function is called to select, insert, update or delete elements in sale table.
 * @author Boaz Trauthwein
 *
 */
public class InitiateSaleQuery {
	/**
	 * This function is called to get all sales table.
	 * @param Msg - data from client
	 * @param sv - server controller.
	 * @throws SQLException - when there is a problem with sql syntax.
	 */
	@SuppressWarnings("unchecked")
	public static void GetSales(Object Msg, ServerLogInController sv) throws SQLException {

		ArrayList<String> oldMsg = (ArrayList<String>) Msg;
		ArrayList<Object> answer = new ArrayList<Object>(); // contain the answer that will be send.
		ArrayList<Object> newColums = new ArrayList<Object>();
		Statement stmt = mySqlConnection.conn.createStatement();
		ResultSet rs;
		String tempString;
		ResultSetMetaData rsmd;

		answer.add(oldMsg.get(0));

		tempString = "SELECT * FROM myfueldb.sale;";
		rs = stmt.executeQuery(tempString); // Run the query.
		rsmd = (ResultSetMetaData) rs.getMetaData();
		while (rs.next()) {
			ArrayList<Object> row = new ArrayList<Object>();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				row.add(rs.getObject(i)); // Add value from the i column
			}
			newColums.add(row);
		}

		answer.add(newColums);
		sv.es.sendToAllClients(answer);

	}
/**
 * 
 * This function is called to change activity status in sale.
 * @param Msg - data from client
 * @param sv - server controller.
 * @throws SQLException - when there is a problem with sql syntax.
 */
	public static void ChangeActivateStatus(Object Msg, ServerLogInController sv) throws SQLException {

		ArrayList<String> oldMsg = (ArrayList<String>) Msg;
		ArrayList<Object> answer = new ArrayList<Object>(); // contain the answer that will be send.
		PreparedStatement ps;
		
		Statement stmt = mySqlConnection.conn.createStatement();
		ResultSet rs;
		String tempString;
		ResultSetMetaData rsmd;

		tempString = String.format("Select saleNumer FROM myfueldb.sale WHERE saleName = \"%s\"", String.valueOf(oldMsg.get(2)));
		rs = stmt.executeQuery(tempString); // Run the query.
		rs.next();
		int saleNumber = Integer.parseInt(String.valueOf(rs.getObject(1)));

		if (Integer.parseInt(String.valueOf(oldMsg.get(0))) == 1) {
			ps = mySqlConnection.conn.prepareStatement("UPDATE myfueldb.sale SET isActive = ?, startDate = ?, endDate = NULL WHERE saleNumer = ?;");
			ps.setInt(1, Integer.parseInt(String.valueOf(oldMsg.get(0))));
			ps.setDate(2, Date.valueOf(String.valueOf(oldMsg.get(1))));
			ps.setInt(3, saleNumber);
			ps.executeUpdate();
		} else {
			ps = mySqlConnection.conn.prepareStatement("UPDATE myfueldb.sale SET isActive = ?, endDate = ? WHERE saleNumer = ?;");
			ps.setInt(1, Integer.parseInt(String.valueOf(oldMsg.get(0))));
			ps.setDate(2, Date.valueOf(String.valueOf(oldMsg.get(1))));
			ps.setInt(3, saleNumber);
			ps.executeUpdate();
		}
		answer.add("GetAllSales");
		GetSales(answer, sv);
	}
/**
 * This function is called to add a sale to table.
 * @param Msg - data from client
 * @param sv - server controller.
 * @throws SQLException - when there is a problem with sql syntax.
 */
	public static void AddSale(Object Msg, ServerLogInController sv) throws SQLException {

		ArrayList<String> oldMsg = (ArrayList<String>) Msg;
		ArrayList<Object> answer = new ArrayList<Object>(); // contain the answer that will be send.
		PreparedStatement ps;

		ps = mySqlConnection.conn.prepareStatement(
				"INSERT INTO myfueldb.sale (saleName, FK_fuelType, discountPercent, startHour, endHour, isActive) VALUES (?,?,?,?,?,0); ");
		ps.setString(1, String.valueOf(oldMsg.get(1)));
		ps.setString(2, String.valueOf(oldMsg.get(2)));
		ps.setFloat(3, Float.parseFloat(String.valueOf(oldMsg.get(3))));
		ps.setString(4, String.valueOf(oldMsg.get(4)));
		ps.setString(5, String.valueOf(oldMsg.get(5)));
		ps.executeUpdate();

		answer.add(oldMsg.get(0));

		GetSales(answer, sv);
	}
/**
 * This function is called to delete sale in table
 * @param Msg - data from client
 * @param sv - server controller.
 * @throws SQLException - when there is a problem with sql syntax.
 */
	public static void DeleteSale(Object Msg, ServerLogInController sv) throws SQLException {
		// TODO Auto-generated method stub
		ArrayList<String> oldMsg = (ArrayList<String>) Msg;
		ArrayList<Object> answer = new ArrayList<Object>(); // contain the answer that will be send.
		PreparedStatement ps;
		Statement stmt = mySqlConnection.conn.createStatement();
		ResultSet rs;
		String tempString;
		ResultSetMetaData rsmd;

		answer.add(oldMsg.get(0));

		tempString = String.format("SELECT saleNumer FROM myfueldb.sale WHERE saleName = \"%s\";",
				String.valueOf(oldMsg.get(1)));
		rs = stmt.executeQuery(tempString); // Run the query.
		rsmd = (ResultSetMetaData) rs.getMetaData();
		rs.next();
		int num = Integer.parseInt(String.valueOf(rs.getObject(1))); // Add value from the i column

		ps = mySqlConnection.conn.prepareStatement("DELETE FROM myfueldb.sale WHERE saleNumer = ?;");
		ps.setInt(1, num);
		ps.executeUpdate();

		answer.add(oldMsg.get(0));

		GetSales(answer, sv);
	}
/**
 * This function is called to update sale in sales table.
 * @param Msg - data from client
 * @param sv - server controller.
 * @throws SQLException - when there is a problem with sql syntax.
 */
	public static void UpdateSale(Object Msg, ServerLogInController sv) throws SQLException {
		// TODO Auto-generated method stub
		ArrayList<String> oldMsg = (ArrayList<String>) Msg;
		ArrayList<Object> answer = new ArrayList<Object>(); // contain the answer that will be send.
		PreparedStatement ps;

		ps = mySqlConnection.conn.prepareStatement(
				"UPDATE myfueldb.sale SET saleName = ?, FK_fuelType = ?, discountPercent = ?, startHour = ?, endHour = ? WHERE saleName = ?;");
		ps.setString(1, String.valueOf(oldMsg.get(1)));
		ps.setString(2, String.valueOf(oldMsg.get(2)));
		ps.setFloat(3, Float.parseFloat(String.valueOf(oldMsg.get(3))));
		ps.setString(4, String.valueOf(oldMsg.get(4)));
		ps.setString(5, String.valueOf(oldMsg.get(5)));
		ps.setString(6, String.valueOf(oldMsg.get(6)));
		ps.executeUpdate();

		answer.add(oldMsg.get(0));

		GetSales(answer, sv);
	}

}
