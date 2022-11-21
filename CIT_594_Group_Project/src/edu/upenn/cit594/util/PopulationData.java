package edu.upenn.cit594.util;

public class PopulationData {
	
	int zipCode;
	int population;
	
	public PopulationData(int zipCode, int population) {
		this.zipCode = zipCode;
		this.population = population;
	}

	public int getZipCode() {
		return zipCode;
	}

	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

}
