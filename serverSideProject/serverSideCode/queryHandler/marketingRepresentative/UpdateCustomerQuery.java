package queryHandler.marketingRepresentative;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import emServer.mySqlConnection;

public class UpdateCustomerQuery {

public UpdateCustomerQuery() {
		
	}
	/**
	 * This method update customer inside table.
	 * @param newMsg contain the relevant data from client.
	 * @throws SQLException error with sql syntax.
	 */
	public static void updateCustomer(Object newMsg) throws SQLException {
		@SuppressWarnings("unchecked")
		ArrayList<String> oldMsg = (ArrayList<String>)newMsg;
		Statement stmt = mySqlConnection.conn.createStatement();
		String tempString = String.format("UPDATE myfueldb.customer SET firstName = \"%s\", lastName = \"%s\", email = \"%s\", creditCardNumber = \"%s\", creditCard_CVV = \"%s\", creditCard_Month = \"%s\", creditCard_year = \"%s\"  WHERE id=\"%s\";", oldMsg.get(1), oldMsg.get(2), oldMsg.get(3), oldMsg.get(4), oldMsg.get(5), oldMsg.get(6), oldMsg.get(7), oldMsg.get(0));
 		stmt.executeUpdate(tempString);
 		tempString = String.format("UPDATE myfueldb.user SET Password = \"%s\"  WHERE FK_customerId=\"%s\";",oldMsg.get(8), oldMsg.get(0));
 		stmt.executeUpdate(tempString);
 		
	}
	

}