
package emServer;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import controller.ServerLogInController;
import queryHandler.*;
import queryHandler.QuarterlyReportsQuery;
import queryHandler.customer.*;
import queryHandler.marketingManager.*;
import queryHandler.marketingRepresentative.*;
import queryHandler.stationManager.*;
import queryHandler.supplier.*;



public class mySqlConnection {
	private ServerLogInController slic;
	public static Connection conn;

public mySqlConnection(ServerLogInController slic) {
	this.slic=slic; //will be used to print msg to user on gui
	}

public boolean ConnectToDB(String host, String schema, String username, String password) {
	try {
		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		System.out.println("Driver definition succeed");
		slic.printToWindow("Driver definition succeed");
	} catch (Exception ex) {
		/* handle the error */
		System.out.println("Driver definition failed");
		slic.printToWindow("Driver definition failed");
	}
	try {
		switch (host) {
		case "Amazon RDS":
			conn = DriverManager.getConnection(
					"jdbc:mysql://myfuel.cselmj4tckoc.us-east-2.rds.amazonaws.com/myfueldb?serverTimezone=Asia/Jerusalem",
					"admin", "JOusGxcu8Ox9KB3SlgAL");
			break;

		case "Microsoft Azure":
			conn = DriverManager.getConnection(
					"jdbc:mysql://myfuledb.mysql.database.azure.com/myfueldb?serverTimezone=Asia/Jerusalem",
					"MYFULE@myfuledb", "xseT4rd7cUt6fvyOy7bgSu3hDni4FjmYg5fbh");
			break;

		case "Localhost":
	    	conn = DriverManager.getConnection("jdbc:mysql://localhost/myfueldb?serverTimezone=Asia/Jerusalem",username,password);

			break;

		}
		System.out.println("SQL connection succeed");
		slic.printToWindow("SQL connection succeed");
		// createTableCourses(conn);
	} catch (SQLException ex) {
		slic.printToWindow("problem logging in check log in details");

		System.out.println("SQLException: " + ex.getMessage());
		// slic.printToWindow("SQLException: " + ex.getMessage());
		System.out.println("SQLState: " + ex.getSQLState());
		// slic.printToWindow("SQLState: " + ex.getSQLState());
		System.out.println("VendorError: " + ex.getErrorCode());
		// slic.printToWindow("VendorError: " + ex.getErrorCode());
		return false; // so upper func will know how to proceed
	}
	return true;
}

	

	public void close() {
			try {
				conn.close();
				System.out.println("SQL was disconnected");
				slic.printToWindow("SQL was disconnected");
			} catch (SQLException e) {				
				System.out.println("problem disconecting SQL");
				slic.printToWindow("problem disconecting SQL");
			}

	}
	
	public void handler(Object msg) throws SQLException {
		
		@SuppressWarnings("unchecked")
		ArrayList<String> oldMsg = (ArrayList<String>)msg;
		ArrayList<Object> newMsg = new ArrayList<Object>();

		 switch (oldMsg.get(0)) { 
		 
		 
    	 	case "LoginRequest"://update DB
    	 	{ 
    	 		newMsg.add(oldMsg.get(1)); //The userName
    	 		newMsg.add(oldMsg.get(2)); //The password
    	 		LoginRequestQuery.LoginRequest(newMsg, slic);
    	 		newMsg.clear();
    		}
    	 	break;
    	 	
    	 	case "LoginRequest/Disconnect":// update DB
    		{
    			newMsg.add(oldMsg.get(1)); // The userName
    			LoginRequestQuery.UserDisconnect(newMsg, slic);
    			newMsg.clear();
    		}break;
    			
    	 	case "customer/OrderHomeFuel" : {
    	 		if(oldMsg.get(1).equals("/getHomeFuelPrice")) {
        	 		OrderHomeFuelQuery.getHomeFuelPrice(slic);
    	 		}else if(oldMsg.get(1).equals("/insertHomeFuelOrder")) {
        	 		newMsg.add(oldMsg.get(2)); //The customer id.
        	 		newMsg.add(oldMsg.get(3)); //The the order date.
        	 		newMsg.add(oldMsg.get(4)); //The delivery date.
        	 		newMsg.add(oldMsg.get(5)); //The delivery time.
        	 		newMsg.add(oldMsg.get(6)); //The home fuel amount. 
        	 		newMsg.add(oldMsg.get(7)); //The order arrival status.
        	 		newMsg.add(oldMsg.get(8)); //The order address.
        	 		OrderHomeFuelQuery.insertHomeFuelOrder(newMsg , slic);
    	 		}else if (oldMsg.get(1).equals("/getCustomerCreditCard")) {
    	 			newMsg.add(oldMsg.get(2));    	 			
    	 			OrderHomeFuelQuery.getCustomerCreditCard(newMsg, slic);
				}
    	 		newMsg.clear();
    	 	}break;
    	 	
    	 	case "customer/OrderHistory":  //update DB
    	 	{
    	 		newMsg.add(oldMsg.get(1)); //Is inProgres checkBox selected.
    	 		newMsg.add(oldMsg.get(2)); //Is delivery checkBox selected.
    	 		newMsg.add(oldMsg.get(3)); //The order left limit - from date.
    	 		newMsg.add(oldMsg.get(4)); //The order right limit - to date.
    	 		newMsg.add(oldMsg.get(5)); //Customer id.
    	 		OrderHistoryQuery.gettingOrderHistory(newMsg, slic);
    	 		newMsg.clear();
    	 	}break;
    	 	    	 		 	
    	 	case "customer/BuyVehicleFuel": {
    	 		if(oldMsg.get(1).equals("/CustomerData")) {
    	 			newMsg.add(oldMsg.get(2));
    	 			newMsg.add(oldMsg.get(3));
        	 		BuyVehicleFuelQuery.getCustomerData(newMsg, slic);
        	 		
    	 		}else {
        	 		newMsg.add(oldMsg.get(1)); //Getting carLicenseNumber.
        	 		newMsg.add(oldMsg.get(2)); //Getting fuelAmount.
        	 		newMsg.add(oldMsg.get(3)); //Getting fuelType.
        	 		newMsg.add(oldMsg.get(4)); //Getting gasStationCompany.
        	 		newMsg.add(oldMsg.get(5)); //Getting gasStationName.
        	 		newMsg.add(oldMsg.get(6)); //Getting FuelPrice
        	 		newMsg.add(oldMsg.get(7)); //Getting paymentType
        	 		newMsg.add(oldMsg.get(8)); //Getting car License Number.
        	 		newMsg.add(oldMsg.get(9)); //Getting the buyTime
        	 		newMsg.add(oldMsg.get(10)); //Getting the buyDate.
        	 		newMsg.add(oldMsg.get(11)); //Getting the pump number.
        	 		BuyVehicleFuelQuery.insertBuyVehicleFuelData(newMsg, slic);
    	 		}
    	 		newMsg.clear();
    	 	}break;
    	 	
    	 	case "supplier": {
    	 		if(oldMsg.get(1).equals("/inventoryOrders")) {
    	 			SupplierQuery.getInventoryFuelOrder(slic);
    	 		}else if(oldMsg.get(1).equals("/insertAndUpdateOrders")) {
    	 			newMsg = (ArrayList<Object>) ((ArrayList<Object>)msg).get(2);
    	 			SupplierQuery.insertAndUpdateOrder(newMsg, slic);
    	 		}else if(oldMsg.get(1).equals("/getHomeFuelOrder")) {
    	 			SupplierQuery.getHomeFuelOrder(slic);
    	 		}else if(oldMsg.get(1).equals("/updateHomeFuelOrder")) {
    	 			newMsg = (ArrayList<Object>) ((ArrayList<Object>)msg).get(2);
    	 			SupplierQuery.updateHomeFuelPurchasesTable(newMsg, slic);
    	 		}

    	 		newMsg.clear();
    	 	}break;
    	 	
    	 	case "performingAnActivityTracking":{
    	 		 if(oldMsg.get(1).equals("/getActivityData")) {
    	 			newMsg.add(oldMsg.get(2));
    	 			newMsg.add(oldMsg.get(3));
     	 			performingAnActivityTrackingQuery.getDataForAnalysisReport(newMsg, slic);
    	 		 }
    	 		newMsg.clear();
    	 	}break;
    	 	
    	 	case "vehicleFuelPurchasesData" :{
    	 		 if(oldMsg.get(1).equals("/getPurchasesData")) {
     	 			newMsg.add(oldMsg.get(2));
     	 			VehicleFuelPurchesesQuery.getPurchasesData(newMsg, slic);
    	 		 }
     	 		newMsg.clear();
    	 	}break;
    	 	
    	 	//This is tal section
    	 	
    	 	case "InventoryStatusRequest": {
    			newMsg.add(oldMsg.get(1));
    			//System.out.println(newMsg);
    			GetInventoryStatusQuery.getInventory(newMsg, slic);
    			newMsg.clear();
    		}break;
    			
    		case "UpdateThresholdQuery": {
    			newMsg.add(oldMsg.get(1));
    			newMsg.add(oldMsg.get(2));
    			newMsg.add(oldMsg.get(3));
    			UpdateThresholdQuery.UpdateThreshold(newMsg);
    			newMsg.clear();
    		}break;
    			

    		case "GetApproveOrderQuery": {
    			newMsg.add(oldMsg.get(1));
    			GetOrdersToApproveQuery.getOrders(newMsg, slic);
    			newMsg.clear();
    		}break;
    			
    		case "ApproveFuelOrderQuery": {
    			newMsg.add(oldMsg.get(1));
    			newMsg.add(oldMsg.get(2));
    			newMsg.add(oldMsg.get(3));
    			ApproveFuelOrderQuery.UpdateThreshold(newMsg);
    			newMsg.clear();
    		}break;
    			
    		case "InventoryReportRequest": {
    			newMsg.add(oldMsg.get(1));
    			InventoryReportQuery.getInventory(newMsg, slic);
    			newMsg.clear();
    		}break;
    			
    		case "gQuarterlyRevenueReport": {
    			newMsg.add(oldMsg.get(1));
    			RevenueReportQuery.getRevenue(newMsg, slic);
    			newMsg.clear();
    		}break;
    		
    		case "gPurchasesReport": {
    			newMsg.add(oldMsg.get(1));
    			PurchasesReportQuery.getRevenue(newMsg, slic);
    			newMsg.clear();
    		}break;
    			

    		
    		
    	 	//This is michel section
    		case "searchVehicles"://update DB
    	 	{
    	 		System.out.println(oldMsg);
    	 		for(int i=1; i<oldMsg.size(); i++) {
    	 			newMsg.add(oldMsg.get(i));
    	 		}
    	 		SearchVehicleQuery.SearchVehicle(newMsg,slic);
    	 		newMsg.clear();
    	 	}
    	 	break;
    	 	
    	 	case "searchCustomer"://read from DB
    	 	{
    	 		System.out.println(oldMsg);
    	 		newMsg.add(oldMsg.get(1));
    	 		newMsg.add(oldMsg.get(2));
    	 		newMsg.add(oldMsg.get(3));
    	 		SearchCustomerQuery.SearchCustomer(newMsg,slic);
    	 		newMsg.clear();
    	 		
     	 	}
    	 	break;
    	 	case "saveNewCustomer"://write to DB
    	 	{

    	 		newMsg.add(oldMsg.get(1));
    	 		newMsg.add(oldMsg.get(2));
    	 		newMsg.add(oldMsg.get(3));
    	 		newMsg.add(oldMsg.get(4));
    	 		newMsg.add(oldMsg.get(5));
    	 		newMsg.add(oldMsg.get(6));
    	 		newMsg.add(oldMsg.get(7));
    	 		newMsg.add(oldMsg.get(8));
    	 		newMsg.add(oldMsg.get(9));
    	 		newMsg.add(oldMsg.get(10));
    	 		newMsg.add(oldMsg.get(11));
    	 		SaveNewCustomerQuery.SaveNewCustomer(newMsg);
    	 		newMsg.clear();
     	 	}
    	 	break;
    	 	case "GetCompany":
    	 	{
    	 		newMsg.add(oldMsg.get(1));
    	 		GetCompanyQuery.GetCompany(newMsg,slic);
    	 		newMsg.clear();
    	 	}
    	 	break;
    	 	case "add model type":
    	 	{
    	 		newMsg.add(oldMsg.get(1));
    	 		newMsg.add(oldMsg.get(2));
    	 		newMsg.add(oldMsg.get(3));
    	 		newMsg.add(oldMsg.get(4));
    	 		newMsg.add(oldMsg.get(5));
    	 		AddModelQuery.AddModel(newMsg);
    	 		newMsg.clear();
    	 	}
    	 	break;
    	 	case "deleteCustomer":
    	 	{
    	 		newMsg.add(oldMsg.get(1));
    	 		DeleteCustomerQuery.DeleteCustomer(oldMsg.get(1));
    	 		newMsg.clear();
    	 	}
    	 	break;
    	 	case "findCustomer":
    	 	{
    	 		newMsg.add(oldMsg.get(1));
    	 		FindCustomerQuery.FindCustomer(newMsg,slic);
    	 		newMsg.clear();
    	 	}
    	 	break;
    	 	case "updateCustomer":
    	 	{
    	 		newMsg.add(oldMsg.get(1));
    	 		newMsg.add(oldMsg.get(2));
    	 		newMsg.add(oldMsg.get(3));
    	 		newMsg.add(oldMsg.get(4));
    	 		newMsg.add(oldMsg.get(5));
    	 		newMsg.add(oldMsg.get(6));
    	 		newMsg.add(oldMsg.get(7));
    	 		newMsg.add(oldMsg.get(8));
    	 		newMsg.add(oldMsg.get(9));
    	 		UpdateCustomerQuery.updateCustomer(newMsg);
    	 		newMsg.clear();
    	 	}
    	 	break;
    	 	case "updateVehicles":
    	 	{
    	 		newMsg.add(oldMsg.get(1));
    	 		newMsg.add(oldMsg.get(2));

    	 		UpdateVehicleQuery.updateVehicle(newMsg);
    	 		newMsg.clear();
    	 	}
    	 	break;
    	 	case "update model type":
    	 	{
    	 		newMsg.add(oldMsg.get(1));
    	 		newMsg.add(oldMsg.get(2));
    	 		newMsg.add(oldMsg.get(3));
    	 		newMsg.add(oldMsg.get(4));
    	 		newMsg.add(oldMsg.get(5));
    	 		UpdateModelQuery.UpdateModelAndCompenies(newMsg);
    	 		newMsg.clear();
    	 	}
    	 	break;
    	  	case "findCustomer2":
    	 	{
    	 		newMsg.add(oldMsg.get(1));
    	 		FindCustomer2Query.findCustomer2(newMsg, slic);
    	 		newMsg.clear();
    	 	}
    	 	break;
    	  	case "addVehicle":
    	 	{
    	 		for(int i=1; i<oldMsg.size(); i++) {
    	 			newMsg.add(oldMsg.get(i));
    	 		}
    	 		AddVehicleQuery.addVehicle(newMsg, slic);
    	 		newMsg.clear();
    	 	}
    	 	break;
    	  	case "addVehicle2":
    	 	{
    	 		for(int i=1; i<oldMsg.size(); i++) {
    	 			newMsg.add(oldMsg.get(i));
    	 		}
    	 		AddVehicleQuery.addVehicle2(newMsg);
    	 		newMsg.clear();
    	 	}
    	 	break;
    	 	
    	 	case "deleteVehicle":
    	 	{
    	 		newMsg.add(oldMsg.get(1));
    	 		DeleteVehicleQuery.DeleteVehicle(oldMsg.get(1), slic);
    	 		newMsg.clear();
    	 	}
    	 	break;
    		case "updateCompanie":
    	 	{
    	 		newMsg.add(oldMsg.get(1));
    	 		newMsg.add(oldMsg.get(2));
    	 		newMsg.add(oldMsg.get(3));
    	 		newMsg.add(oldMsg.get(4));
//    	 		newMsg.add(oldMsg.get(5));
//    	 		newMsg.add(oldMsg.get(6));
//    	 		newMsg.add(oldMsg.get(7));
    	 		//UpdateModelQuery.UpdateCompanies(newMsg);
    	 		UpdateModelQuery.deleteCompanies(oldMsg.get(1).toString());
    	 		UpdateModelQuery.insertNewCompanies(newMsg, slic);
    	 		newMsg.clear();
    	 	}
    	 	break;
    		case "updateModel":
    	 	{
    	 		newMsg.add(oldMsg.get(1));
    	 		newMsg.add(oldMsg.get(2));
    	 		UpdateModelQuery.UpdateModel(newMsg , slic);
    	 		newMsg.clear();
    	 	}
    	 	break;
    	 	
    	 	//This Boaz Section
    		case "marketingManager/setRates": {
    			switch (oldMsg.get(1)) {
    			case "GetAllRates":
    				newMsg.add(oldMsg.get(1));
    				SetRatesQuery.GetRates(newMsg, slic);
    				newMsg.clear();
    				break;

    			case "setMyFuelRates":
    				newMsg.add(oldMsg.get(2));
    				newMsg.add(oldMsg.get(3));
    				newMsg.add(oldMsg.get(4));
    				newMsg.add(oldMsg.get(5));
    				newMsg.add(oldMsg.get(1));
    				SetRatesQuery.requesrChangeMyFuelRates(newMsg, slic);
    				newMsg.clear();
    				break;

    			case "setMaximumRates":
    				newMsg.add(oldMsg.get(2));
    				newMsg.add(oldMsg.get(3));
    				newMsg.add(oldMsg.get(4));
    				newMsg.add(oldMsg.get(5));
    				newMsg.add(oldMsg.get(1));
    				SetRatesQuery.requesrChangeMyFuelRates(newMsg, slic);
    				newMsg.clear();
    				break;
    			}
    		}break;
    			

    		case "marketingManager/initiateSale": {
    			switch (oldMsg.get(1)) {
    			case "GetAllSales":
    				newMsg.add(oldMsg.get(1));
    				InitiateSaleQuery.GetSales(newMsg, slic);
    				newMsg.clear();
    				break;
    			case "SetActivayeStatus":
    				newMsg.add(oldMsg.get(2));//Status
    				newMsg.add(oldMsg.get(3));//Date
    				newMsg.add(oldMsg.get(4));//saleName
    				InitiateSaleQuery.ChangeActivateStatus(newMsg, slic);
    				newMsg.clear();
    				break;
    			}
    		}break;
    			
    			
    		case "marketingManager/GenerateReports": {
    			switch (oldMsg.get(1)) {
    			case "GetAllSales":
    				newMsg.add("marketingManager/GenerateReports/GetAllSales");
    				InitiateSaleQuery.GetSales(newMsg, slic);
    				newMsg.clear();
    				break;
    			case "GenerateSaleReport":
    				newMsg.add("marketingManager/GenerateReports/GenerateSaleReport");
    				newMsg.add(oldMsg.get(2));//fuelType
    				newMsg.add(oldMsg.get(3));//startDate
    				newMsg.add(oldMsg.get(4));//endDate
    				newMsg.add(oldMsg.get(5));//startHour
    				newMsg.add(oldMsg.get(6));//endHour
    				GenerateReportQuery.GenerateSalesReport(newMsg, slic);
    				break;
    			case "GeneratePeriodicReport":
    				newMsg.add("marketingManager/GenerateReports/GeneratePeriodicReport");
    				newMsg.add(oldMsg.get(2));//stratDate
    				newMsg.add(oldMsg.get(3));//endDate
    				GenerateReportQuery.GeneratePeriodicReport(newMsg, slic);
    				break;
    			}
    		}break;
    			

    		case "chainManger/confirmRates": {
    			switch (oldMsg.get(1)) {
    			case "GetAllRates":
    				newMsg.add("chainManager/GetAllRates");
    				SetRatesQuery.GetRates(newMsg, slic);// Need the same query as SetRates.
    				newMsg.clear();
    				break;
    			case "ConfirmRate":
    				newMsg.add("chainManager/GetAllRates");
    				newMsg.add(oldMsg.get(2));// fuelType;
    				newMsg.add(oldMsg.get(3));// priceType
    				newMsg.add(oldMsg.get(4));// price (float)
    				SetRatesQuery.updateFuelRates(newMsg, slic);
    				newMsg.clear();
    				break;
    			case "RejectRate":
    				newMsg.add("chainManager/GetAllRates");
    				newMsg.add(oldMsg.get(2));// fuelType;
    				newMsg.add(oldMsg.get(3));// priceType
    				newMsg.add(oldMsg.get(4));// price (float)
    				SetRatesQuery.deleteFuelRate(newMsg, slic);
    				newMsg.clear();
    				break;

    			}
    		}break;
    			
    		case "SettingPatternOfSalesPromotion": {
    			switch (oldMsg.get(1)) {
    			case "GetAllSales":
    				newMsg.add("SettingPatternOfSalesPromotion/GetAllSales");
    				InitiateSaleQuery.GetSales(newMsg, slic);
    				newMsg.clear();
    				break;
    			case "AddSale":
    				newMsg.add("SettingPatternOfSalesPromotion/GetAllSales");
    				newMsg.add(oldMsg.get(2));// SalenName
    				newMsg.add(oldMsg.get(3));// FuelType
    				newMsg.add(oldMsg.get(4));// Discount
    				newMsg.add(oldMsg.get(5));// StartHour
    				newMsg.add(oldMsg.get(6));// EndHour
    				InitiateSaleQuery.AddSale(newMsg, slic);
    				newMsg.clear();
    				break;
    			case "DeleteSale":
    				newMsg.add("SettingPatternOfSalesPromotion/GetAllSales");
    				newMsg.add(oldMsg.get(2));// SaleName
    				InitiateSaleQuery.DeleteSale(newMsg, slic);
    				newMsg.clear();
    				break;
    			case "UpdateSale":
    				newMsg.add("SettingPatternOfSalesPromotion/GetAllSales");
    				newMsg.add(oldMsg.get(2));// New SalenName
    				newMsg.add(oldMsg.get(3));// New FuelType
    				newMsg.add(oldMsg.get(4));// New Discount
    				newMsg.add(oldMsg.get(5));// New StartHour
    				newMsg.add(oldMsg.get(6));// New EndHour
    				newMsg.add(oldMsg.get(7));// WHERE SaleName
    				InitiateSaleQuery.UpdateSale(newMsg, slic);
    				newMsg.clear();
    				break;
    			}
    		}break;
    		
    		case "chainManager/GetDetailsForQuarterlyReports": {
    			newMsg.add(oldMsg.get(0));// chainManager/GetDetailsForQuarterlyReports
    			QuarterlyReportsQuery.GetDetailsForQuarterlyReports(newMsg, slic);
    		}
    			break;
    		case "chainManager/GetDataForQuarterlyReportsTable": {
    			newMsg.add(oldMsg.get(0));// chainManager/GetDetailsForQuarterlyReports
    			newMsg.add(oldMsg.get(1));// reportType
    			newMsg.add(oldMsg.get(2));// gastationCompany
    			newMsg.add(oldMsg.get(3));// gasStationName
    			QuarterlyReportsQuery.GetDataForQuarterlyReportsTable(newMsg, slic);
    		}
    			break;
    		case "chainManager/GetSpecificReport": {
    			newMsg.add(oldMsg.get(0));// chainManager/GetDetailsForQuarterlyReports
    			newMsg.add(oldMsg.get(1));// reportType
    			newMsg.add(oldMsg.get(2));// gastationCompany
    			newMsg.add(oldMsg.get(3));// gasStationName
    			newMsg.add(oldMsg.get(4));// year
    			newMsg.add(oldMsg.get(5));// quarterNumber
    			QuarterlyReportsQuery.GetSpecificReport(newMsg, slic);
    		}break;
    			

    			
    		

    			
    			
    			
    			
    	 	
    	 	   	 	
    	 	
		 }
	}
}

	
