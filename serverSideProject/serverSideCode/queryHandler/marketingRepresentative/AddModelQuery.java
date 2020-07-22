package queryHandler.marketingRepresentative;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import emServer.mySqlConnection;

public class AddModelQuery {
	
public AddModelQuery() {
		
	}
	/**
	 * This method insert model and fuel companies for new customer.
	 * @param newMsg contain the relevant data from client.
	 * @throws SQLException error with sql syntax.
	 */
	public static void AddModel(Object newMsg) throws SQLException {
		@SuppressWarnings("unchecked")
		ArrayList<Object> tempMsg = new ArrayList<Object>();
		ArrayList<String> oldMsg = (ArrayList<String>)newMsg;
		Statement stmt = mySqlConnection.conn.createStatement();
		String tempString = String.format("UPDATE `myfueldb`.`customer` SET customer.FK_modelType='%s' "
				+ "Where customer.Id= '%s';", oldMsg.get(1), oldMsg.get(0));
 		stmt.executeUpdate(tempString);
		ResultSet rs;
	
			String tempString2 = String.format("SELECT gasstationcompany.companyId FROM myfueldb.gasstationcompany "
					+ "WHERE gasstationcompany.companyName = '%s' "
					+ "OR gasstationcompany.companyName = '%s' OR gasstationcompany.companyName = '%s';", oldMsg.get(2), oldMsg.get(3), oldMsg.get(4));
	 		rs=stmt.executeQuery(tempString2);

		
		ResultSetMetaData rsmd = (ResultSetMetaData)rs.getMetaData();		
		ArrayList<Object> data = new ArrayList<Object>();

		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i));
			}
		}
		
		for (int i = 0; i < data.size(); i++) {
			String tempString4 = String.format("INSERT INTO `myfueldb`.`purchaseplan`(`FK_customerId`,`FK_companyId`) "
					+ "VALUES ('%s','%s');", oldMsg.get(0), data.get(i).toString());
			stmt.executeUpdate(tempString4);
		}

	}
	

}



