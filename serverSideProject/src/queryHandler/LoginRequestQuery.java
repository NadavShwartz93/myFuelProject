package queryHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import controller.ServerLogInController;
import emServer.mySqlConnection;

public class LoginRequestQuery {

	public LoginRequestQuery() {
	}

	/**This method check user login detail, and return the answer to LoginController in the client side.
	 * @param message - contain the relevant data for the query.
	 * @param sv is an instance of ServerLogInController class.
	 * @throws SQLException - when there is a problem with sql syntax.
	 */
	@SuppressWarnings("unchecked")
	public static void LoginRequest(Object message, ServerLogInController sv) throws SQLException {
		ResultSet rs;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<String> oldMsg = (ArrayList<String>) message;
		ArrayList<Object> answer = new ArrayList<Object>(); // contain the answer that will be return.
		ArrayList<Object> data = new ArrayList<Object>();
		PreparedStatement ps;
		String temp = null, tempFirstName = null, tempCustomerId = null;
		String tempPosition = null;
		Integer isEmployee = null;
		int employeeNumber = 0;

		// check if user and password are existing
		String tempString = String.format("SELECT user.UserName,user.FK_employeeNumber FROM user "
				+ "WHERE user.UserName = \"%s\" AND user.Password = \"%s\";", oldMsg.get(0), oldMsg.get(1));
		rs = stmt.executeQuery(tempString); // Run the query.
		ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();

		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i)); // Add value from the i column
			}
			temp = (String) data.get(0);
			isEmployee = (Integer) data.get(1);
		}
		data.clear();

		if (temp == null) {
			System.out.println("Erorr user data");
			answer.add("login");
			answer.add("UserDoesntExist");
			sv.es.sendToAllClients(answer);
		} else {
			// checking if user is already online.
			tempString = String.format("SELECT isOnline FROM myfueldb.user WHERE UserName = \"%s\";", oldMsg.get(0));
			rs = stmt.executeQuery(tempString); // Run the query.
			rs.next();
			boolean isOnline = Boolean.parseBoolean(String.valueOf(rs.getObject(1))); // Get isOnline.
			if (isOnline == true) {
				answer.add("login");
				answer.add("UserIsAllreadyOnline");
				sv.es.sendToAllClients(answer);
			} else {

				ps = mySqlConnection.conn
						.prepareStatement("UPDATE myfueldb.user SET  isOnline = 1  WHERE UserName = ?;");
				ps.setString(1, String.valueOf(oldMsg.get(0)));
				ps.executeUpdate();

				if (isEmployee != null) {
					// The login request belong to an employee
					tempString = String.format(
							"SELECT employee.firstName,employee.position, employee.employeeNumber " + "FROM employee "
									+ "WHERE employee.employeeNumber IN (SELECT user.FK_employeeNumber "
									+ "FROM user WHERE user.UserName = \"%s\" AND user.FK_employeeNumber IS NOT NULL);",
							oldMsg.get(0));
					rs = stmt.executeQuery(tempString); // Run the query.
					rsmd = (ResultSetMetaData) rs.getMetaData();
					while (rs.next()) {
						for (int i = 1; i <= rsmd.getColumnCount(); i++) {
							data.add(rs.getObject(i)); // Add value from the i column
						}

						tempFirstName = (String) data.get(0);
						tempPosition = (String) data.get(1);
						employeeNumber = Integer.parseInt(data.get(2).toString());
					}

					if (tempFirstName != null) {
						answer.add("login");
						answer.add(tempPosition);
						answer.add(String.valueOf(oldMsg.get(0)));
						answer.add(employeeNumber);
						// this is the place of chenge
						int companyCustomer = 0;
						answer.add(companyCustomer);
						sv.es.sendToAllClients(answer);
					}
					data.clear();
				}

				else {
					int companyCustomer = 0;
					boolean tempBool;
					// The login request belong to customer
					tempString = String.format(
							"SELECT customer.firstName, customer.id, customer.companyCustomer " + "FROM customer "
									+ "WHERE customer.id IN " + "(SELECT user.FK_customerId " + "FROM user "
									+ "WHERE user.UserName = \"%s\" AND user.FK_customerId IS NOT NULL);",
							oldMsg.get(0));
					rs = stmt.executeQuery(tempString); // Run the query.
					rsmd = (ResultSetMetaData) rs.getMetaData();
					while (rs.next()) {
						for (int i = 1; i <= rsmd.getColumnCount(); i++) {
							data.add(rs.getObject(i)); // Add value from the i column
						}

						tempFirstName = (String) data.get(0);
						tempCustomerId = (String) data.get(1);
						// This is the place of change
						tempBool = (boolean) data.get(2);
						if (tempBool == false)
							companyCustomer = 0;
						else
							companyCustomer = 1;

					}

					if (tempFirstName != null) {
						answer.add("login");
						answer.add("CUSTOMER");
						answer.add(tempFirstName);
						answer.add(tempCustomerId);
						// This is the place of change
						answer.add(companyCustomer);
						sv.es.sendToAllClients(answer);
					}
				}

				rs.close();
			}
		}
	}

	public static void UserDisconnect(Object message, ServerLogInController sv) throws SQLException {
		ArrayList<String> oldMsg = (ArrayList<String>) message;
		PreparedStatement ps;

		ps = mySqlConnection.conn.prepareStatement("UPDATE myfueldb.user SET  isOnline = 0  WHERE UserName = ?;");
		ps.setString(1, String.valueOf(oldMsg.get(0)));
		ps.executeUpdate();
	}

}