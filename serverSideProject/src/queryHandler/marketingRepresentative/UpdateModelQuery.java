package queryHandler.marketingRepresentative;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import controller.ServerLogInController;
import emServer.mySqlConnection;

public class UpdateModelQuery {


	
	public UpdateModelQuery() {
		
	}
	/**
	 * This method update model and fuel companies.
	 * @param newMsg contain the relevant data from client.
	 * @throws SQLException error with sql syntax.
	 */
	public static void UpdateModelAndCompenies(Object newMsg) throws SQLException {
		@SuppressWarnings("unchecked")
		ArrayList<String> oldMsg = (ArrayList<String>)newMsg;
		Statement stmt = mySqlConnection.conn.createStatement();
		String tempString = String.format("UPDATE `myfueldb`.`customer` SET customer.FK_modelType='%s' Where customer.Id= '%s';", oldMsg.get(1), oldMsg.get(0));
 		stmt.executeUpdate(tempString);
 		if (oldMsg.get(3)==""||oldMsg.get(4)=="") {
			String tempString4 = String.format("INSERT INTO `myfueldb`.`purchaseplan`(`FK_customerId`,`FK_companyId`) VALUES ('%s','%s');", oldMsg.get(0), oldMsg.get(3));
			stmt.executeUpdate(tempString4);
		}
		else {	
		String tempString5 = String.format("INSERT INTO `myfueldb`.`purchaseplan` (`FK_customerId`,`FK_companyId`) VALUES ('%s','%s'),('%s','%s'),('%s','%s');",oldMsg.get(0), oldMsg.get(3), oldMsg.get(0), oldMsg.get(4), oldMsg.get(0), oldMsg.get(5));
		stmt.executeUpdate(tempString5);

		}
 		

	}
	/**
	 * This method update only model type.
	 * @param newMsg contain the relevant data from client.
	 * @param sv is an instance of {@link ServerLogInController}.
	 * @throws SQLException error with sql syntax.
	 */
	public static void UpdateModel(Object newMsg, ServerLogInController sv) throws SQLException {
		@SuppressWarnings("unchecked")
		ArrayList<String> oldMsg = (ArrayList<String>)newMsg;
		Statement stmt = mySqlConnection.conn.createStatement();
		String tempString = String.format("UPDATE `myfueldb`.`customer` SET customer.FK_modelType='%s' Where customer.Id= '%s';", oldMsg.get(1), oldMsg.get(0));
 		stmt.executeUpdate(tempString);
 		
		ArrayList<String> tempArr = new ArrayList<String>();
		tempArr.add(oldMsg.get(0).toString());
		
		FindCustomerQuery.FindCustomer(tempArr, sv);
 		
	}
	/**
	 * This methods delete old companies from exist customer and insert a new companies to the same one.
	 * @param newMsg contain the relevant data from client.
	 * @throws SQLException error with sql syntax.
	 */
	public static void deleteCompanies(Object newMsg) throws SQLException {
		String CustomerId = (String)newMsg;
		Statement stmt = mySqlConnection.conn.createStatement();
		
		
			String tempString = String.format("DELETE FROM myfueldb.purchaseplan " + 
					"WHERE purchaseplan.FK_customerId ='%s';", CustomerId);
			stmt.executeUpdate(tempString);
	}
	
	public static void insertNewCompanies(Object newMsg, ServerLogInController sv) throws SQLException {
		ResultSet rs;
		ArrayList<String> oldMsg = (ArrayList<String>)newMsg;
		ArrayList<String> tempMsg = new ArrayList<String>();
		Statement stmt = mySqlConnection.conn.createStatement();
		tempMsg.add(oldMsg.get(0));
		String tempString;
		
		if(oldMsg.get(2).equals("") && oldMsg.get(3).equals("")){
			tempMsg.add(oldMsg.get(1));
			tempString = String.format("SELECT gasstationcompany.companyId FROM myfueldb.gasstationcompany "
					+ "WHERE gasstationcompany.companyName = '%s';"
					, tempMsg.get(1).toString());
		}
		
		if(!oldMsg.get(2).equals("") && oldMsg.get(3).equals("")){
			tempMsg.add(oldMsg.get(1));
			tempMsg.add(oldMsg.get(2));				
			
			tempString = String.format("SELECT gasstationcompany.companyId FROM myfueldb.gasstationcompany "
					+ "WHERE gasstationcompany.companyName = '%s' "
					+ "OR gasstationcompany.companyName = '%s';",tempMsg.get(1).toString(), tempMsg.get(2).toString());
	 		
			}
		else{
			tempMsg.add(oldMsg.get(1));
			tempMsg.add(oldMsg.get(2));		
			tempMsg.add(oldMsg.get(3));	
			
			tempString = String.format("SELECT gasstationcompany.companyId FROM myfueldb.gasstationcompany "
					+ "WHERE gasstationcompany.companyName = '%s' "
					+ "OR gasstationcompany.companyName = '%s' OR gasstationcompany.companyName = '%s';", tempMsg.get(1).toString(), tempMsg.get(2).toString(), tempMsg.get(3).toString());
	 		
			}	
		
 		rs=stmt.executeQuery(tempString);
		ResultSetMetaData rsmd = (ResultSetMetaData)rs.getMetaData();		
		
		ArrayList<Object> data = new ArrayList<Object>();

		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i));
			}			
		}
		
		
		
		for(int i = 0; i < data.size(); i++) {
			tempString = String.format("INSERT INTO `myfueldb`.`purchaseplan`(`FK_customerId`,`FK_companyId`) "
					+ "VALUES ('%s','%s');", oldMsg.get(0).toString(), data.get(i).toString());
			stmt.executeUpdate(tempString);

		}
		
		ArrayList<String> tempArr = new ArrayList<String>();
		tempArr.add(oldMsg.get(0).toString());
		
		FindCustomerQuery.FindCustomer(tempArr, sv);
	
	}
 		
}
