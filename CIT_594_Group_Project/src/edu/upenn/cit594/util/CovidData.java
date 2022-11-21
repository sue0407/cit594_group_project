package edu.upenn.cit594.util;

public class CovidData {
	
	int zipCode;
	String timeStamp;
	int partiallyVaccinated;
	int fullyVaccinated;
	
	public CovidData(int zipCode, String timeStamp, int partiallyVaccinated, int fullyVaccinated) {
	
		this.zipCode = zipCode;
		this.timeStamp = timeStamp;
		this.partiallyVaccinated = partiallyVaccinated;
		this.fullyVaccinated = fullyVaccinated;
		
	}

	// getter and setter
	public int getZipCode() {
		return zipCode;
	}

	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getPartiallyVaccinated() {
		return partiallyVaccinated;
	}

	public void setPartiallyVaccinated(int partiallyVaccinated) {
		this.partiallyVaccinated = partiallyVaccinated;
	}

	public int getFullyVaccinated() {
		return fullyVaccinated;
	}

	public void setFullyVaccinated(int fullyVaccinated) {
		this.fullyVaccinated = fullyVaccinated;
	}

}
