package queryHandler.marketingRepresentative;
import java.sql.SQLException;
import java.sql.Statement;

import emServer.mySqlConnection;

public class DeleteCustomerQuery {
	
	public DeleteCustomerQuery(){
		
	}
	 /**
	  *	This method send delete customer query.
	  * @param string contain the relevant data from client.
	  * @throws SQLException error with sql syntax.
	  */
	public static void DeleteCustomer(String string) throws SQLException {
		Statement stmt = mySqlConnection.conn.createStatement();
		String tempString = String.format("DELETE FROM myfueldb.customer WHERE Id=\"%s\";",string);
 		stmt.executeUpdate(tempString);
		
	}
}
