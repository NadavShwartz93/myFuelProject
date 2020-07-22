package queryHandler.marketingRepresentative;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import controller.ServerLogInController;
import emServer.mySqlConnection;

public class SearchCustomerQuery {


	public SearchCustomerQuery() {
		
	}
	 /**
	  *  This method returns existing customer.
	  * @param Msg contain the relevant data from client.
	  * @param sv is instance of {@link ServerLogInController}
	  * @throws SQLException error with sql syntax
	  */
	public static void SearchCustomer(Object Msg, ServerLogInController sv) throws SQLException {
		
		@SuppressWarnings("unchecked")
		ArrayList<String> oldMsg = (ArrayList<String>)Msg;
		ArrayList<Object> newMsg = new ArrayList<Object>();
		ArrayList<Object> answer = new ArrayList<Object>() ;
		answer.add(oldMsg.get(2));

		Statement stmt = mySqlConnection.conn.createStatement();
		ResultSet rs;
		
		deleteEmptyRow(sv);//Delete row with null in FK_modelType.
		if(oldMsg.get(1).equals(""))
		{
			String tempString="SELECT customer.id, customer.firstName, customer.lastName, customer.email, customer.creditCardNumber, customer.creditCard_CVV, customer.creditCard_Month, customer.creditCard_year, user.Password, customer.FK_modelType FROM customer, user WHERE user.FK_customerId = customer.id;";
	 		rs=stmt.executeQuery(tempString);
		}
		else 
		{
			System.out.println(oldMsg);
			String tempString = String.format("SELECT customer.id, customer.firstName, customer.lastName, customer.email, customer.creditCardNumber, customer.creditCard_CVV, customer.creditCard_Month, customer.creditCard_year, user.Password, customer.FK_modelType FROM customer, user WHERE customer.%s = '%s' and user.FK_customerId = customer.id;", oldMsg.get(0) ,oldMsg.get(1));
	 		rs=stmt.executeQuery(tempString);
		}
		
		ResultSetMetaData rsmd = (ResultSetMetaData)rs.getMetaData();
		
		while (rs.next()) {
			ArrayList<Object> data = new ArrayList<Object>();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i));
			}
			newMsg.add(data);
		}
		
		answer.add(newMsg);
		rs.close();

		//mysqlConnection.
		sv.es.sendToAllClients(answer);
	 	}
	
	
	/**This method delete all the row, from the customer table, that the modelType of them is null.
	 * @param sv is an instance of {@link ServerLogInController}.
	 * @throws SQLException error with sql syntax.
	 */
	public static void deleteEmptyRow(ServerLogInController sv) throws SQLException {
		Statement stmt = mySqlConnection.conn.createStatement();
		
		String tempString = String.format("DELETE FROM myfueldb.customer " + 
				"WHERE customer.FK_modelType IS NULL;");
		
		stmt.executeUpdate(tempString);

	}
	
	
	
	}

