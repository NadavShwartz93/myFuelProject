package queryHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import controller.ServerLogInController;
import emServer.mySqlConnection;

public class QuarterlyReportsQuery {
	/**
	 * This function returns to client basic information in quarter reports to get started.
	 * @param Msg - details from client
	 * @param sv - server controller
	 * @throws SQLException when sql syntax is not correct.
	 */
	@SuppressWarnings("unchecked")
	public static void GetDetailsForQuarterlyReports(Object Msg, ServerLogInController sv) throws SQLException {

		ArrayList<String> oldMsg = (ArrayList<String>) Msg;
		ArrayList<Object> answer = new ArrayList<Object>(); // contain the answer that will be send.
		ArrayList<Object> newColums = new ArrayList<Object>();
		Statement stmt = mySqlConnection.conn.createStatement();
		ResultSet rs;
		String tempString;
		ResultSetMetaData rsmd;

		answer.add(oldMsg.get(0));

		tempString = "SELECT gc.companyName, gs.gasStationName "
				+ "FROM myfueldb.quarterreports AS qr, myfueldb.gasstation AS gs, myfueldb.gasstationcompany AS gc, myfueldb.employee AS em "
				+ "WHERE gs.gasStationId = qr.FK_gasStation_Id AND gc.companyId = gs.FK_companyId AND em.employeeNumber = gs.FK_gasStationManager; ";
		rs = stmt.executeQuery(tempString); // Run the query.
		rsmd = (ResultSetMetaData) rs.getMetaData();
		while (rs.next()) {
			ArrayList<Object> row = new ArrayList<Object>();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				row.add(rs.getObject(i)); // Add value from the i column
			}
			if (!newColums.contains(row))
				newColums.add(row);
		}

		answer.add(newColums);
		sv.es.sendToAllClients(answer);

	}
	/**
	 * This function returns reports option for a specific gas station.
	 * @param Msg - details from client
	 * @param sv - server controller
	 * @throws SQLException when sql syntax is not correct.
	 */
	@SuppressWarnings("unchecked")
	public static void GetDataForQuarterlyReportsTable(Object Msg, ServerLogInController sv) throws SQLException {
		ArrayList<String> oldMsg = (ArrayList<String>) Msg;
		ArrayList<Object> answer = new ArrayList<Object>(); // contain the answer that will be send.
		ArrayList<Object> newColums = new ArrayList<Object>();
		Statement stmt = mySqlConnection.conn.createStatement();
		ResultSet rs;
		String tempString;
		ResultSetMetaData rsmd;

		answer.add(oldMsg.get(0));

		tempString = String.format(
				"SELECT qr.reportType, gc.companyName, gs.gasStationName, qr.qr_Year, qr.QuarterNumber "
						+ "FROM myfueldb.quarterreports AS qr, myfueldb.gasstation AS gs, myfueldb.gasstationcompany AS gc "
						+ "WHERE gs.gasStationId = qr.FK_gasStation_Id AND gc.companyId = gs.FK_companyId "
						+ "AND qr.reportType = \"%s\" AND gc.companyName = \"%s\" AND gs.gasStationName = \"%s\" ORDER BY qr.qr_Year, qr.QuarterNumber DESC; ",
				String.valueOf(oldMsg.get(1)), String.valueOf(oldMsg.get(2)), String.valueOf(oldMsg.get(3)));
		rs = stmt.executeQuery(tempString); // Run the query.
		rsmd = (ResultSetMetaData) rs.getMetaData();
		while (rs.next()) {
			ArrayList<Object> row = new ArrayList<Object>();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				row.add(rs.getObject(i)); // Add value from the i column
			}
			if (!newColums.contains(row))
				newColums.add(row);
		}

		answer.add(newColums);
		sv.es.sendToAllClients(answer);
	}
	/**
	 * This function returns to client a specific report that was selected for a gas station.
	 * @param Msg - details from client
	 * @param sv - server controller
	 * @throws SQLException when sql syntax is not correct.
	 */
	public static void GetSpecificReport(Object Msg, ServerLogInController sv) throws SQLException {
		// TODO Auto-generated method stub
		ArrayList<String> oldMsg = (ArrayList<String>) Msg;
		ArrayList<Object> answer = new ArrayList<Object>(); // contain the answer that will be send.
		ArrayList<Object> newColums = new ArrayList<Object>();
		Statement stmt = mySqlConnection.conn.createStatement();
		ResultSet rs;
		String tempString;
		ResultSetMetaData rsmd;

		answer.add(oldMsg.get(0));

		tempString = String.format("SELECT qr.theFile "
				+ "FROM myfueldb.quarterreports AS qr, myfueldb.gasstation AS gs, myfueldb.gasstationcompany AS gc "
				+ "WHERE gs.gasStationId = qr.FK_gasStation_Id AND gc.companyId = gs.FK_companyId "
				+ "AND qr.reportType = \"%s\" AND gc.companyName = \"%s\" AND gs.gasStationName = \"%s\" "
				+ "AND qr.qr_Year = %s AND qr.QuarterNumber = \"%s\";", String.valueOf(oldMsg.get(1)),
				String.valueOf(oldMsg.get(2)), String.valueOf(oldMsg.get(3)),
				String.valueOf(oldMsg.get(4)),
				String.valueOf(oldMsg.get(5)));
		rs = stmt.executeQuery(tempString); // Run the query.
		rs.next();
		newColums.add(rs.getObject(1)); // Add value from the i column

		answer.add(newColums);
		sv.es.sendToAllClients(answer);
	}
}
