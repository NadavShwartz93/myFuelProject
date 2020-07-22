package queryHandler.marketingRepresentative;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import emServer.mySqlConnection;

public class SaveNewCustomerQuery {
	
public SaveNewCustomerQuery() {
	
}
/**
 *  This method insert new customer to db.
 * @param newMsg contain the relevant data from client.
 * @throws SQLException error with sql syntax.
 */
public static void SaveNewCustomer(Object newMsg) throws SQLException {
	@SuppressWarnings("unchecked")
	ArrayList<Object> tempMsg = (ArrayList<Object>)newMsg;
	ArrayList<String> oldMsg = (ArrayList<String>)newMsg;
	Statement stmt = mySqlConnection.conn.createStatement();
	String tempString = String.format("INSERT INTO myfueldb.customer (id, firstName,lastName, email, creditCardNumber, creditCard_CVV, companyCustomer, creditCard_Month, creditCard_year) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', %d, '%s', '%s');", oldMsg.get(0), oldMsg.get(1), oldMsg.get(2), oldMsg.get(3), oldMsg.get(4), oldMsg.get(5), (int)tempMsg.get(6), oldMsg.get(7), oldMsg.get(8));
	String tempString2 = String.format("INSERT INTO myfueldb.user (UserName, Password, FK_customerId, isOnline) VALUES ('%s', '%s', '%s', 0);", oldMsg.get(9), oldMsg.get(10), oldMsg.get(0));
	stmt.executeUpdate(tempString);
	stmt.executeUpdate(tempString2);
}

}
