package queryHandler.marketingRepresentative;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import controller.ServerLogInController;
import emServer.mySqlConnection;

public class FindCustomerQuery {


	public FindCustomerQuery() {
		
	}
	/**
	 *  This method check if customer exist and return model and companies.
	 * @param Msg contain the relevant data from client.
	 * @param sv is instance of {@link ServerLogInController}.
	 * @throws SQLException error with sql syntax.
	 */
	public static void FindCustomer(Object Msg, ServerLogInController sv) throws SQLException {
		
		@SuppressWarnings("unchecked")
		ArrayList<String> oldMsg = (ArrayList<String>)Msg;
		ArrayList<Object> newMsg = new ArrayList<Object>();
		ArrayList<Object> answer = new ArrayList<Object>() ;
		answer.add("putCustomerInLabel");

		Statement stmt = mySqlConnection.conn.createStatement();
		ResultSet rs;
		String tempString=String.format("SELECT customer.id, customer.firstName, customer.lastName, customer.FK_modelType, newTable.companyName\r\n" + 
				"FROM myfueldb.customer, \r\n" + 
				"(\r\n" + 
				"SELECT purchaseplan.FK_companyId, gasstationcompany.companyName\r\n" + 
				"FROM myfueldb.purchaseplan, myfueldb.gasstationcompany\r\n" + 
				"WHERE purchaseplan.FK_customerId = '%s' AND gasstationcompany.companyId = purchaseplan.FK_companyId\r\n" + 
				") as newTable\r\n" + 
				"WHERE customer.id = '%s';",oldMsg.get(0), oldMsg.get(0));
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
		rs.close();

		//mysqlConnection.
		sv.es.sendToAllClients(answer);
	 	}

	}



