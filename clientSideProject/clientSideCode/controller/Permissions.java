package controller;

import java.net.URL;
import java.util.ArrayList;

import org.controlsfx.glyphfont.FontAwesome.Glyph;

//import controller.MainDesplayController;
import de.jensd.fx.glyphs.GlyphIcons;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.utils.MaterialDesignIconFactory;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.utils.MaterialIconFactory;
import javafx.scene.text.Text;

public class Permissions {


	private UserType type;
	private ArrayList<Text> iconsList;
	private ArrayList<URL> urlsList;
	private ArrayList<String> btnNameList;
	private ArrayList<String> toolTipDescription;
	private int actionsNum;
	
	public Permissions(UserType type, int customerType)
	{
		this.type = type;
		iconsList = new ArrayList<Text>();
		urlsList = new ArrayList<URL>();
		btnNameList = new ArrayList<String>();
		toolTipDescription = new ArrayList<String>();
		
		switch (this.type)
		{
		case USER:
			
			break;
		case CUSTOMER:
		{
			iconsList.add(MaterialDesignIconFactory.get().createIcon(MaterialDesignIcon.CAR, "80px"));
			urlsList.add(MainDesplayController.class.getResource("/fxml/customer/VehicleFuelPurcheses.fxml"));	
			btnNameList.add("Purcheses");
			toolTipDescription.add("Vehicle History Purcheses");
			
			if(customerType == 0) {
			iconsList.add(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.HOME, "80px"));
			urlsList.add(MainDesplayController.class.getResource("/fxml/customer/OrderHomeFuel.fxml"));	
			btnNameList.add("Buy Fuel");
			toolTipDescription.add("New Order - Home Fuel");

			
			iconsList.add(MaterialDesignIconFactory.get().createIcon(MaterialDesignIcon.SHOPPING, "80px"));
			urlsList.add(MainDesplayController.class.getResource("/fxml/customer/OrderHistory.fxml"));
			btnNameList.add("History");
			toolTipDescription.add("Home Fuel Order History");
			}
			

			
			actionsNum = urlsList.size();
			break;
		}	
		
		case STATION_MANAGER:
		{
			iconsList.add(MaterialDesignIconFactory.get().createIcon(MaterialDesignIcon.CHART_BAR, "80px"));
			urlsList.add(MainDesplayController.class.getResource("/fxml/stationManager/InventoryStatus.fxml"));
			btnNameList.add("Status");
			toolTipDescription.add("Inventory Status");

			
			iconsList.add(MaterialDesignIconFactory.get().createIcon(MaterialDesignIcon.FILE_DOCUMENT, "80px"));
			urlsList.add(MainDesplayController.class.getResource("/fxml/stationManager/generateQuarterlyReports.fxml"));
			btnNameList.add("Reports");
			toolTipDescription.add("Generate Quarterly Reports");

			
			iconsList.add(MaterialIconFactory.get().createIcon(MaterialIcon.TUNE, "80px"));
			urlsList.add(MainDesplayController.class.getResource("/fxml/stationManager/setThresholdForNotification.fxml"));	
			btnNameList.add("Threshold");
			toolTipDescription.add("Setting Threshold For Notification");

			
			iconsList.add(MaterialDesignIconFactory.get().createIcon(MaterialDesignIcon.PLAYLIST_CHECK, "80px"));
			urlsList.add(MainDesplayController.class.getResource("/fxml/stationManager/approvesInventoryPurchases.fxml"));
			btnNameList.add("Inventory");
			toolTipDescription.add("Approve Inventory Order");

			
			actionsNum = 4;
			
			break;
		}
		
		case MARKETING_REPRESENTATIVE:
		{
			
			iconsList.add(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.USER_PLUS, "67px"));
			urlsList.add(MainDesplayController.class.getResource("/fxml/MarketingRepresentative/ManageCustomer.fxml"));
			btnNameList.add("Customer");
			toolTipDescription.add("Manage Customer");

			
			iconsList.add(MaterialDesignIconFactory.get().createIcon(MaterialDesignIcon.CAR, "80px"));
			urlsList.add(MainDesplayController.class.getResource("/fxml/MarketingRepresentative/ManageVehicle.fxml"));
			btnNameList.add("Vehicles");
			toolTipDescription.add("Manage Vehicles");

			
			iconsList.add(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.VCARD, "65px"));
			urlsList.add(MainDesplayController.class.getResource("/fxml/MarketingRepresentative/ManageClientAssociationWithPurchasePlanModel.fxml"));
			btnNameList.add("Associate");
			toolTipDescription.add("Associat Customer To Purchase Model");

				
			actionsNum = 3;
			
			break;
		}	
		case MARKETING_MANAGER:
		{
			iconsList.add(MaterialDesignIconFactory.get().createIcon(MaterialDesignIcon.CHART_AREASPLINE, "80px"));
			urlsList.add(MainDesplayController.class.getResource("/fxml/marketingManager/InitiateSale.fxml"));
			btnNameList.add("Sales");
			toolTipDescription.add("Initiate Sales Operation");

			
			iconsList.add(MaterialDesignIconFactory.get().createIcon(MaterialDesignIcon.FILE_DOCUMENT, "80px"));
			urlsList.add(MainDesplayController.class.getResource("/fxml/marketingManager/GenerateReports.fxml"));	
			btnNameList.add("Reports");
			toolTipDescription.add("Initiate Sales Operation");

			
			iconsList.add(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.DOLLAR, "80px"));
			urlsList.add(MainDesplayController.class.getResource("/fxml/marketingManager/SetRates.fxml"));
			btnNameList.add("Rates");
			toolTipDescription.add("Set Rates");

			
			actionsNum = 3;
			
			break;
		}	
		
		case MARKETING_DEPARTMENT_WORKER:
		{
			iconsList.add(MaterialDesignIconFactory.get().createIcon(MaterialDesignIcon.CHART_AREASPLINE, "80px"));
			urlsList.add(MainDesplayController.class.getResource("/fxml/marketingDepartmentWorker/settingPatternOfSalesPromotion.fxml"));
			btnNameList.add("Sale");
			toolTipDescription.add("Sales Promotion Patterns");

			
			iconsList.add(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.DATABASE, "80px"));
			urlsList.add(MainDesplayController.class.getResource("/fxml/marketingDepartmentWorker/performingAnActivityTracking.fxml"));
			btnNameList.add("Tracking");
			toolTipDescription.add("Performing Activity Tracking");

			
			actionsNum = 2;
			
			break;
		}
		
		case CHAIN_MANAGER:
		{
			iconsList.add(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.DOLLAR, "80px"));
			urlsList.add(MainDesplayController.class.getResource("/fxml/chainManager/confirmRates.fxml"));
			btnNameList.add("Rates");
			toolTipDescription.add("Confirm Rates");

			
			iconsList.add(MaterialDesignIconFactory.get().createIcon(MaterialDesignIcon.FILE_DOCUMENT, "80px"));
			urlsList.add(MainDesplayController.class.getResource("/fxml/chainManager/watchingQuarterlyReports.fxml"));
			btnNameList.add("Reports");
			toolTipDescription.add("Watching Quarterly reports");

			
			actionsNum = 2;
			
			break;
		}
		
		case SUPPLIER:
		{
			iconsList.add(MaterialDesignIconFactory.get().createIcon(MaterialDesignIcon.BULLHORN, "80px"));
			urlsList.add(MainDesplayController.class.getResource("/fxml/supplier/supplyInventoryOrders.fxml"));
			btnNameList.add("Inventory");
			toolTipDescription.add("Supply Inventory Orders");
			
			iconsList.add(MaterialDesignIconFactory.get().createIcon(MaterialDesignIcon.ALERT_CIRCLE_OUTLINE, "80px"));
			urlsList.add(MainDesplayController.class.getResource("/fxml/supplier/supplyHomeFuelOrders.fxml"));
			btnNameList.add("Home");
			toolTipDescription.add("Supply Home Fuel Orders");

			
			actionsNum = 2;
		}
		break;
		
		default:
			break;
		}
			
	}
	
	public ArrayList<String> getBtnNameList() {
		return btnNameList;
	}
	
	public ArrayList<String> getToolTipDescription() {
		return toolTipDescription;
	}

	public UserType getType() {
		return type;
	}

	public ArrayList<Text> getIconsList() {
		return iconsList;
	}

	public ArrayList<URL> getUrlsList() {
		return urlsList;
	}
	
	public int getActionsCount() {
		return actionsNum;
	}
	
	public String TypeToString() {
		
		int spc = 0;
		String pos = type.toString().toLowerCase();
		pos = pos.substring(0, 1).toUpperCase() + pos.substring(1);
		pos = pos.replace('_', ' ');
		spc = pos.indexOf(' ');
		if (spc != -1)
		{
			pos = pos.substring(0, spc+1) + pos.substring(spc+1, spc+2).toUpperCase() + pos.substring(spc+2);
		}
	
		return pos;
		
	}
	
}
