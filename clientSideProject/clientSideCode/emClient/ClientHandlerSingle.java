package emClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import controller.*;
import controller.customer.*;
import controller.marketingManager.*;
import controller.marketingDepartmentWorker.*;
import controller.chainManager.*;
import controller.marketingRepresentative.*;
import controller.stationManager.*;
import controller.supplier.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ClientHandlerSingle {
	
	  final public static int DEFAULT_PORT = 5555;
	  private static ClientHandlerSingle chs= null;
	  public EmClient client;
	  private LoginController lic;
	  private MainDesplayController mdc;
	  public UserType type;
	  public String customerid = null;
	  public int employeeNumber = 0; 
	  public String UserName;//This is first name.
	  //This is the change
	  public int companyCustomer = 0;
	  Stage primaryStage;
	  private static BorderPane mainLayout;
	  
	  //This is nadav section
	  private OrderHistoryController ohc;
	  private BuyVehicleFuelController bvfc;
	  private OrderHomeFuelController ohfc;
	  private OrderHomeFuelPriceController ohfpc;
	  private supplyInventoryOrdersController sioc;
	  private supplyHomeFuelOrdersController shfoc;
	  private performingAnActivityTrackingController patc;
	  private VehicleFuelPurchesesController vfpc;
	  
	  //This is boaz section
	  private SetRatesController src;
	  private InitiateSaleController isc;
	  private SettingPatternOfSalesPromotionController spospc;
	  private ConfirmRatesController crc;
	  private GenerateReportsController grc;
	  private WatchingQuarterlyReportsController wqrc;

	  //This is michel section
	  private ManageCustomerController mcc;
	  private ManageVehicleController mvc;
	  private AddCustomerController acc;
	  private AddVehicleController avc;
	  private ManageClientAssociationWithPurchasePlanModelController caw;
	  private AddClientAssociationWithPurchasePlanModelController acawp;
	  private AddClientAssociationWithPurchasePlanModelController2 acawp2;
	  private AddVehicleController2 avc2;
	  
	  //This is tal section 
	  private SetThresholdController sthc;
	  private InventoryStatusController invC;
	  private ApproveInventoryOrderController aioc;
	  private GenerateQuarterlyReportsController gqr;
	  
	  
	  //This is tal section 
	  public void setInvC(InventoryStatusController invC) {
		this.invC = invC;
	  }
	  
	  public void setSthc(SetThresholdController sthc) {
		  this.sthc = sthc;
	  }
	  
		public void setAioc(ApproveInventoryOrderController aioc) {
			this.aioc = aioc;
		}

		public void setGqr(GenerateQuarterlyReportsController gqr) {
			this.gqr = gqr;
		}
			
	  //This is boaz section
		public void setSrc(SetRatesController src) {
			this.src = src;
		}
		
		public void setIsc(InitiateSaleController isc) {
			this.isc = isc;
		}
		
		public void setSpospc(SettingPatternOfSalesPromotionController spospc) {
			this.spospc = spospc;

		}
		
		public void setCrc(ConfirmRatesController crc) {
			this.crc = crc;
		}
		
		public void setGrc(GenerateReportsController grc) {
			this.grc = grc;
		}
		
		public void setWqrc(WatchingQuarterlyReportsController wqrc) {
			this.wqrc = wqrc;
		}

	  
	  
	  //This is michel section
	  public void setAcawp(AddClientAssociationWithPurchasePlanModelController acawp) {
			this.acawp = acawp;
	  }
	  
	  public void setAcawp2(AddClientAssociationWithPurchasePlanModelController2 acawp2) {
			this.acawp2 = acawp2;
	  }
	  
	  public void setMvc(ManageVehicleController mvc) {
			this.mvc = mvc;
	  }
	  
	  public void setMcc(ManageCustomerController mcc) {
			this.mcc = mcc;
	  }
	  
	  public void setCawc(ManageClientAssociationWithPurchasePlanModelController caw) {
			this.caw = caw;
	  }
	  
	  public void setAcc(AddCustomerController acc) {
			this.acc = acc;
	  }
	  
	  public void setAvc(AddVehicleController avc) {
			this.avc = avc;			
	  }
	  
	  public void setAvc2(AddVehicleController2 avc2) {
			this.avc2 = avc2;			
	  }
	  
	  
	  //This is Nadav section 
	  public void setPatc(performingAnActivityTrackingController patc) {
		this.patc = patc;
	  }
	  
	  public void setSioc(supplyInventoryOrdersController sioc) {
		this.sioc = sioc;
	  }
	  
	  public void setShfoc(supplyHomeFuelOrdersController shfoc) {
		this.shfoc = shfoc;
	}
	  
	  public void setOhfpc(OrderHomeFuelPriceController ohfpc) {
		this.ohfpc = ohfpc;
	  }
	  
	  public void setOhfc(OrderHomeFuelController ohfc) {
		this.ohfc = ohfc;
	  }
	  
	  public void setBvfc(BuyVehicleFuelController bvfc) {
		this.bvfc = bvfc;
	  }
	  
	  public void setOhc(OrderHistoryController ohc) {
		this.ohc = ohc;
	  }

	  public void setLic(LoginController lic) {
		this.lic = lic;
	}
	    
	  public void setMdc(MainDesplayController mdc) {
			this.mdc = mdc;
		}
	  
	public void setVfpc(VehicleFuelPurchesesController vfpc) {
		this.vfpc = vfpc;
	}


	private ClientHandlerSingle() {
		String host =  "localhost";
		 try 
		    {
		    	client= new EmClient(host, DEFAULT_PORT, this);
		    } 
		    catch(IOException exception) 
		    {
		      System.out.println("Error: Can't setup connection!"
		                + " Terminating client.");
		      System.exit(1);
		    }
	}
	
	public static ClientHandlerSingle getInstance() {
		if (chs == null) {
			chs = new ClientHandlerSingle();
		}
		return chs;
	}

	@SuppressWarnings({ "static-access", "unchecked" })
	public void handleAnswer(Object msg) throws IOException {
		@SuppressWarnings("unchecked")
		ArrayList<Object> answer = (ArrayList<Object>)msg;
		ArrayList<Object> newMsg = new ArrayList<Object>();

		
		switch ((String)answer.get(0)) {
		 
		case "login": {
			if (String.valueOf(answer.get(1)).compareTo("UserDoesntExist") == 0) {
				lic.UserDoesntExist();
			} else if (String.valueOf(answer.get(1)).compareTo("UserIsAllreadyOnline") == 0) {
				lic.UserIsAllreadyOnline();
			} else {
				type = UserType.valueOf((String) answer.get(1));
				lic.openUser();
				UserName = ((String) answer.get(2));
				if (type.equals(UserType.CUSTOMER))
					customerid = ((String) answer.get(3));
				else {
					int number = Integer.parseInt(answer.get(3).toString());
					employeeNumber = number;
				}
				companyCustomer = (int) answer.get(4); // This is added.
			}
		}break;
			
 	 		case "OrderHomeFuel":{
 	 			if(((String)answer.get(1)).equals("setHomeFuelPrice")){
 	 				float fuelPrice = Float.parseFloat(answer.get(2).toString());
 	 				float maxFuelPrice = Float.parseFloat(answer.get(3).toString());
 	 				ohfc.setHomeFuelPrice(fuelPrice, maxFuelPrice);
 	 			}else if (((String)answer.get(1)).equals("setCustomerData")) {
 	 				newMsg.add(answer.get(2));
 	 				ohfpc.setCustomerData(newMsg);
				}else{
 	 				String message = (String)answer.get(1);
 	 				ohfpc.HandleData(message);
 	 			}	
 	 		}break;
 	 		
 	 		case "OrderHistoryView":{
 	 			ohc.setOrderInTable(answer);
 	 		}break;
 	 		
 	 		case "BuyVehicleFuelView":{
 	 			if(((String)answer.get(1)).equals("/setCustomerData")){
 	 				if(answer.get(2).equals("VehicleNotExists")) {
 	 					newMsg.add(answer.get(2));
 	 				}else {//The case that the vehicle is exists.
 	 	 				newMsg.add(answer.get(2));
 	 	 				newMsg.add(answer.get(3));
 	 	 				newMsg.add(answer.get(4));
 	 	 				newMsg.add(answer.get(5));
 	 	 				newMsg.add(answer.get(6));
 	 	 				newMsg.add(answer.get(7));
 	 	 				newMsg.add(answer.get(8));
 	 				}
 	 				bvfc.setCustomerData(newMsg);
 	 			}else {
 	 	 			bvfc.handelMessages(answer);
 	 			}
 	 			newMsg.clear();
 	 		}break;
 	 		
 	 		case "SupplyInventory":{
 	 			if(((String)answer.get(1)).equals("/setSupplierData")) {
 	 				newMsg = (ArrayList<Object>) answer.get(2);
 	 				sioc.setSupplierData(newMsg);
 	 			}
 	 			newMsg.clear();
 	 		}break;
 	 		
 	 		case "SupplyHomeFuelOrders" :{
 	 			if(((String)answer.get(1)).equals("/setHomeFuelOrder")) {
 	 				newMsg =  (ArrayList<Object>) answer.get(2);
 	 				shfoc.setHomeFuelOrder(newMsg);
 	 			}
 	 			newMsg.clear();
 	 		}break;
 	 		
 	 		case "performingAnActivityTracking" :{
 	 			if(((String)answer.get(1)).equals("/setActivityTrackingData")) {
 	 				if(!((String)answer.get(2)).equals("There is no data")) {
 	 	 				patc.setActivityTrackingData((Hashtable<String, ArrayList<Object>>) answer.get(3));
 	 				}else {
 	 					newMsg.add(answer.get(2).toString());
 	 					patc.noDataReruen(newMsg);
 	 				}
 	 			}
 	 			newMsg.clear();
 	 		}break;
 	 		
 	 		case "vehicleFuelPurchasesData" :{
 	 			if(((String)answer.get(1)).equals("/setDataInTabe")) {
 	 				if(answer.size() == 3) {
 	 					newMsg.add(answer.get(2).toString());
 	 					vfpc.setDataInTabe(newMsg);
 	 				}else {
 	 					newMsg.add(answer.get(2).toString());
 	 					newMsg.add(answer.get(3));
 	 					if(answer.size() == 5)
 	 						newMsg.add(answer.get(4));
 	 					vfpc.setDataInTabe(newMsg);
 	 				}
 	 			}
 	 			newMsg.clear();
 	 		}break;
 	 		
 	 		
 	 		//This is tal section
 	 		case "inventoryStatus": {
 				invC.handleAnswer(answer);
 			}break;
 				
 			case "approveOrder": {
 				aioc.showDataInTable(answer);
 			}break;
 				
 			case "inventoryReport": {
 				gqr.makeReport(answer, "Inventory_Report");
 			}break;
 				
 			case "revenueReport": {
 				gqr.makeReport(answer, "Revenue_Report");
 			}break;
 				
 			case "purchasesReport": {
 				gqr.makeReport(answer, "Purchases_Report");
 			}break;
 	 		
 	 		
 	 		
 	 		
 	 		//This is Boaz section
 			case "chainManager/GetAllRates": {
 				crc.setLabelsAndConfirmRatesInTable(answer);
 			}break;
 			
 			case "GetAllRates": {

 				src.setRatesInLabelsAndTable(answer);
 			}break;
 				
 			case "SettingPatternOfSalesPromotion/GetAllSales": {
 				spospc.setSalesInTableInitialize(answer);
 			}break;
 			
 			case "GetAllSales": {

 				isc.setSalesInTableInitialize(answer);
 			}break;
 			
 			case "marketingManager/GenerateReports/GetAllSales": {
 				grc.setReportSalesInTableInitialize(answer);
 			}break;
 			
 			case "marketingManager/GenerateReports/GenerateSaleReport": {
 				grc.generateSaleReportInFile(answer);
 			}break;
 				
 			case "marketingManager/GenerateReports/GeneratePeriodicReport": {
 				grc.generatePeriodicReportInFile(answer);
 			}break;
 			
 			
 			case "chainManager/GetDetailsForQuarterlyReports": {
 				wqrc.SetComboBoxData(answer);
 			}break;
 				
 			case "chainManager/GetDataForQuarterlyReportsTable": {
 				wqrc.GetDataForQuarterlyReportsTable(answer);
 			}break;
 				
 			case "chainManager/GetSpecificReport": {
 				wqrc.ShowSpecificReport(answer);
 			}break;
 				

 				
 				
 				
 				
 				
 	 		
 	 		
 	 		//This is michael section
 	 		
 	 		case "showCustomerInTable":{
  	 			mcc.showDataInTable(answer.get(1));
 	 		}
 	 		break;
 	 		
 	 		case "show vehicle in table":{
 	 			if(answer.get(1).equals("Customer not exists")) {
 	 				mvc.customerNotExists();
 	 			}else {
 	 				mvc.setAddBtnDisable(false);
 	 			}
	 				mvc.showDataInTable(answer.get(2));
 	 		}
 	 		break;
 	 		
 	 		case "showCompanyInList":{
 	 			acawp.setList(answer.get(1));
 	 			//showDataInTable(answer.get(1));
 	 		}
 	 		break;
 	 		case "showCompanyInList2":{
 	 			acawp2.setList(answer.get(1));
 	 			//showDataInTable(answer.get(1));
 	 		}
 	 		break;
 	 		case "showCompanyInListMnage":{
 	 			caw.setList(answer.get(1));
 	 		}break;
 	 		case "putCustomerInLabel":{
 	 			caw.setOnLabel(answer.get(1));
 	 		}break;
 	 		
 	 		case "saveVehiclTableData":{
 	 			avc.getDataFromServer(answer.get(1));
 	 		}break;
 	 		
		}	
	}
	
}
