package logic;

public class QuarterlyReports {
	private String reportType, gasStationCompany, gasStationName;
    private int year, quarterNumber;



	public QuarterlyReports(String reportType, String gasStationCompany, String gasStationName, int year,
			int quarterNumber) {
		super();
		this.reportType = reportType;
		this.gasStationCompany = gasStationCompany;
		this.gasStationName = gasStationName;
		this.year = year;
		this.quarterNumber = quarterNumber;
	}
	/**
	 * @return Report Type
	 */
	public String getReportType() {
		return reportType;
	}
	/**
	 * 
	 * @param Report Type
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	/**
	 * 
	 * @return Gas Station Company Name
	 */
	public String getGasStationCompany() {
		return gasStationCompany;
	}
	/**
	 * 
	 * @param Gas Station Company Name
	 */
	public void setGasStationCompany(String gasStationCompany) {
		this.gasStationCompany = gasStationCompany;
	}
	/**
	 * 
	 * @return Gas Station Name
	 */
	public String getGasStationName() {
		return gasStationName;
	}
	/**
	 * 
	 * @param Gas Station Name
	 */
	public void setGasStationName(String gasStationName) {
		this.gasStationName = gasStationName;
	}
	/**
	 * @return Year of Quarter
	 */
	public int getYear() {
		return year;
	}
	/**
	 * 
	 * @param Year of Quarter
	 */
	public void setYear(int year) {
		this.year = year;
	}
	/**
	 * 
	 * @return Quarter Number
	 */
	public int getQuarterNumber() {
		return quarterNumber;
	}

	/**
	 * @param quarterNumber - set the quarterNumber value to quarterNumber field.
	 */
	public void setQuarterNumber(int quarterNumber) {
		this.quarterNumber = quarterNumber;
	}
}
