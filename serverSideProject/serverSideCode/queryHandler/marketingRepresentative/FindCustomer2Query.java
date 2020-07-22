package queryHandler.marketingRepresentative;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import controller.ServerLogInController;
import emServer.mySqlConnection;

public class FindCustomer2Query {

public FindCustomer2Query() {
	
}
/**
 * This method check if customer exist and return full name.
 * @param Msg contain the relevant data from client.
 * @param sv is an instance of {@link ServerLogInController}.
 * @throws SQLException error with sql syntax.
 */
public static void findCustomer2(Object Msg, ServerLogInController sv) throws SQLException {
	
	@SuppressWarnings("unchecked")
	ArrayList<String> oldMsg = (ArrayList<String>)Msg;
	ArrayList<Object> newMsg = new ArrayList<Object>();
	ArrayList<Object> answer = new ArrayList<Object>() ;
	answer.add("putCustomerInLabel2");

	Statement stmt = mySqlConnection.conn.createStatement();
	ResultSet rs;
	String tempString=String.format("SELECT customer.id, customer.firstName, customer.lastName "
			+ "FROM myfueldb.customer WHERE customer.id = '%s';",oldMsg.get(0));
 	rs=stmt.executeQuery(tempString);
	

	ResultSetMetaData rsmd = (ResultSetMetaData)rs.getMetaData();
	ArrayList<Object> data = new ArrayList<Object>();
	
	while (rs.next()) {	
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			data.add(rs.getObject(i));
		}
	}
	
	if(data.size() == 0) {
		data.add("This customer does not exist");
	}
	answer.add(data);
	rs.close();
	//mysqlConnection.
	sv.es.sendToAllClients(answer);

 	}

}

