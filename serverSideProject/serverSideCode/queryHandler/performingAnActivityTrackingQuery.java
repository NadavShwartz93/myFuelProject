package queryHandler;

import java.awt.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.TIMEOUT;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import controller.ServerLogInController;
import emServer.mySqlConnection;

/**This class is manage all the operation of getting data, from the mySql tables, 
 * and handle this data. All the data is for the generate the analysis report in the client side.
 * @author Nadav Shwartz
 */
public class performingAnActivityTrackingQuery {
	
	
	/**This method get data from mySql table for the analysis report.
	 * @param message is data from client.
	 * @param sv is instance of {@link ServerLogInController}.
	 * @throws SQLException - when there is a problem with sql syntax.
	 */
	public static void getDataForAnalysisReport(ArrayList<Object> message, ServerLogInController sv) throws SQLException {
		ResultSet rs;
		ResultSetMetaData rsmd;
		Statement stmt = mySqlConnection.conn.createStatement();
		ArrayList<Object> data = new ArrayList<Object>();
		ArrayList<Object> answer = new ArrayList<Object>();
		ArrayList<Object> temp = new ArrayList<>();
		Hashtable<String, ArrayList<Object>> customerHt = new Hashtable<>();
		
		//String endBefore = LocalDate.now().toString();
		//String starDate = LocalDate.now().minusWeeks(1).toString();
		String starDate = message.get(0).toString();
		String endBefore = message.get(1).toString();
		
		//Get the customers Id and there type(private, company).
		String tempString = String.format("SELECT  car.FK_customerId, customer.companyCustomer " + 
				"FROM myfueldb.car, myfueldb.customer "
				+ "WHERE car.FK_customerId = customer.id " + 
				"GROUP BY car.FK_customerId;");
		
		rs = stmt.executeQuery(tempString);  //Run the query.
		rsmd = (ResultSetMetaData)rs.getMetaData();
		
		ArrayList<Object> newMsg = new ArrayList<>();
		
		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				data.add(rs.getObject(i)); 
			}
			newMsg.add(data.get(1));
			customerHt.put(data.get(0).toString(), newMsg);
			data = new ArrayList<>();			
			newMsg = new ArrayList<>();
	}
		
		ArrayList<String> customersId = getCustomerId(customerHt);
		
		for (int i = 0; i < customersId.size(); i++) {
			//First get the cars License Number of this customer
			tempString = String.format("SELECT car.carLicenseNumber " + 
					"FROM myfueldb.car " + 
					"WHERE car.FK_customerId = \"%s\";", customersId.get(i));
			
			rs = stmt.executeQuery(tempString);  //Run the query.
			rsmd = (ResultSetMetaData)rs.getMetaData();
			while (rs.next()) {
				for (int j = 1; j <= rsmd.getColumnCount(); j++) {
					data.add(rs.getObject(j)); 
				}
			}
			
			//Second get the purchase data
			tempString = String.format("SELECT fuelpurchases.FK_fuelType, fuelpurchases.fuelPurchasePrice, " + 
					"fuelpurchases.purchasesTime FROM myfueldb.fuelpurchases " + 
					"WHERE fuelpurchases.purchasesDay >= \"%s\" AND "
					+ "fuelpurchases.purchasesDay <= \"%s\" " + 
					"AND fuelpurchases.FK_carLicenseNumber IN(", starDate, endBefore);
			for (int k = 0; k < data.size(); k++) {
				if(k == (data.size() - 1)) {
					tempString+= String.format("\"%s\");", data.get(k).toString());		
				}else {
					tempString+= String.format("\"%s\", ", data.get(k).toString());		
				}
			}
			
			
			rs = stmt.executeQuery(tempString);  //Run the query.
			rsmd = (ResultSetMetaData)rs.getMetaData();
			ArrayList<Object> tempData = new ArrayList<>();		

			while (rs.next()) {
				for (int j = 1; j <= rsmd.getColumnCount(); j++) {
					tempData.add(rs.getObject(j)); 
				}
				temp = (ArrayList<Object>) customerHt.get(customersId.get(i));
				tempData = handleRow(tempData, temp);
				
				customerHt.remove(customersId.get(i));
				customerHt.put(customersId.get(i), tempData);
				tempData = new ArrayList<>();
			}
			data = new ArrayList<>();
		}
		
		ArrayList<String>placeToDelete = new ArrayList<>();
		//remove Key and Value that don't got data.
		int num = customersId.size();
		for (int i = 0; i < num; i++) {
			temp = customerHt.get(customersId.get(i));
			if(temp.size() != 4) {
				customerHt.remove(customersId.get(i));
				placeToDelete.add(customersId.get(i));
				//customersId.remove(i);
			}
		}
		
		//remove id from customersId ArrayList
		for (int i = 0; i < placeToDelete.size(); i++) {
			customersId.remove(placeToDelete.get(i).toString());		
		}
		
		//Calculate the average time for every customer
		for (int i = 0; i < customersId.size(); i++) {
			temp = (ArrayList<Object>) (customerHt.get(customersId.get(i))).get(3);
        	LocalTime avg = timeAvg(temp);
        	
        	temp = customerHt.get(customersId.get(i));
        	temp.remove(3);
        	temp.add(avg);
        	
        	customerHt.replace(customersId.get(i), temp);
        	
		}
		
		answer.add("performingAnActivityTracking");
		answer.add("/setActivityTrackingData");
		
		if(customersId.isEmpty()) {
			answer.add("There is no data");
		}else {
			customerHt = getCustomerRanks(customerHt);
			answer.add("There is data");
			answer.add(customerHt);	
		}
		
		
		sv.es.sendToAllClients(answer);

	}
	
	/**This method get all the keys of the given hashtable
	 * @param hashtable this parameter contain all data that already arrived from the mySql tales.
	 * @return all the keys of the given hashtable.
	 */
	private static ArrayList<String> getCustomerId(Hashtable<String, ArrayList<Object>> hashtable) {
		Set<String> customersId = hashtable.keySet();
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(customersId);
		return list;
	}
	
	/**This method get the ArrayList of customerData and insert to it ArrayList of newRowArray 
	 * according to some order.
	 * @param newRowArray is new data from the mySql tables.
	 * @param customerData is the data that is already ordered. 
	 * @return the union of the given tow parameters into one ArrayList.  
	 */
	private static ArrayList<Object> handleRow(ArrayList<Object> newRowArray, ArrayList<Object> customerData){
		ArrayList<Object> customerFuelType = new ArrayList<>();
		ArrayList<Object>  fuelingTimeAvg  = new ArrayList<>();
		Float sum = (float) 0;
		
		if(customerData.size() == 1) {
			customerFuelType.add(newRowArray.get(0));
			sum = Float.parseFloat(newRowArray.get(1).toString());
			fuelingTimeAvg.add(newRowArray.get(2));
			customerData.add(customerFuelType);
			customerData.add(sum);
			customerData.add(fuelingTimeAvg);
			
		}else {//The customer already has data in the arrayList
			
			customerFuelType = (ArrayList<Object>) customerData.get(1);
			if(!customerFuelType.contains(newRowArray.get(0)))
				customerFuelType.add(newRowArray.get(0));
			
			sum = Float.parseFloat(newRowArray.get(1).toString()) + 
					(Float)customerData.get(2);
			
			fuelingTimeAvg = (ArrayList<Object>) customerData.get(3);
			fuelingTimeAvg.add(newRowArray.get(2));
			
			customerData.remove(1);
			customerData.add(1, customerFuelType);
			customerData.remove(2);
			customerData.add(2, sum);
			customerData.remove(3);
			customerData.add(3, fuelingTimeAvg);

		}
		return customerData;
	}
	
	
	/**This method get ArrayList of LocalTimes and return the average time.
	 * @param timeList is ArrayList of times.
	 * @return the average time of the given ArrayList of times.
	 */
	private static LocalTime timeAvg(ArrayList<Object> timeList) {
		int hour = 0;
		int mints = 0;
		int sec = 0;
		
		for (int i = 0; i < timeList.size(); i++) {
			hour = hour + (LocalTime.parse(timeList.get(i).toString())).getHour();
			mints = mints + (LocalTime.parse(timeList.get(i).toString())).getMinute();
			sec = sec + (LocalTime.parse(timeList.get(i).toString())).getSecond();
		}
		
		hour = Math.floorDiv(hour, timeList.size());
		mints = Math.floorDiv(mints, timeList.size());
		sec = Math.floorDiv(sec, timeList.size());
		LocalTime avgTime = LocalTime.of(hour, mints, sec);
		
		return avgTime;
	}
		
	/**This method get the customer data, and set to every customer a level that depend on the amount of fuel buying.
	 * @param customerHt is the given customer data.
	 * @return the given customer data with rank to every row.
	 */
	private static Hashtable<String, ArrayList<Object>> getCustomerRanks(Hashtable<String, ArrayList<Object>> customerHt){
		
		ArrayList<String> customerId = getCustomerId(customerHt);

		
		//Find the max
		Float max = (Float) (customerHt.get(customerId.get(0))).get(2);
		Float temp;
		for (int i = 1; i < customerId.size(); i++) {
			temp = (Float) (customerHt.get(customerId.get(i))).get(2);
			max = Math.max(max, temp);
		}
		
		//calculate the size of the section	
		int size = Math.floorDiv(Math.round(max), (int)10);
		
		//Create the levels
		ArrayList<ArrayList<Integer>> levels = new ArrayList<>();
		ArrayList<Integer> level_i = new ArrayList<>();
		int numberOfLevels = 10;
		
		for (int i = 0; i < numberOfLevels; i++) {
			if(i == 0) {
				level_i.add(i);
				level_i.add(size);
				levels.add(level_i);
				level_i = new ArrayList<>();
			}else {
				level_i.add(2 + levels.get(i-1).get(1));
				level_i.add(level_i.get(0) + size);
				levels.add(level_i);
				level_i = new ArrayList<>();
			}
		}
		
		//Set level for every row.
		ArrayList<Object> tempArr = new ArrayList<>();
		Float sumOfFuelPurchase = (float) 0;
		Integer rank = 0;
		for (int i = 0; i < customerId.size(); i++) {
			tempArr = customerHt.get(customerId.get(i));
			sumOfFuelPurchase = (Float) tempArr.get(2);
			for (int j = 0; j < numberOfLevels; j++) {
				
				if(levels.get(j).get(0) < sumOfFuelPurchase &&
						levels.get(j).get(1) > sumOfFuelPurchase) {
					rank = j+1;
					break;
				}
			}
			
			tempArr.add(rank);
			customerHt.remove(customerId.get(i).toString());
			customerHt.put(customerId.get(i).toString(), tempArr);
			
		}
		
		
		
		
		return customerHt;
		
	}
	
}
