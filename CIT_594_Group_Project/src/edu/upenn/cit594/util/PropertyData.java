package edu.upenn.cit594.util;

public class PropertyData {
	
	int zipCode;
	double totalLivableArea;
	double marketValue;
	
	public PropertyData(int zipCode, double totalLivableArea, double marketValue) {
	
		this.zipCode = zipCode;
		this.totalLivableArea = totalLivableArea;
		this.marketValue = marketValue;
	}

	public int getZipCode() {
		return zipCode;
	}

	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	public double getTotalLivableArea() {
		return totalLivableArea;
	}

	public void setTotalLivableArea(double totalLivableArea) {
		this.totalLivableArea = totalLivableArea;
	}

	public double getMarketValue() {
		return marketValue;
	}

	public void setMarketValue(double marketValue) {
		this.marketValue = marketValue;
	}

}
