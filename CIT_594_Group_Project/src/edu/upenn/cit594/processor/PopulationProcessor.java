package edu.upenn.cit594.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upenn.cit594.util.PopulationData;

public class PopulationProcessor {
	
	// Data from data utility
	private List <PopulationData> populationDataList;

	// Memoization data set (first Integer is zip code for all HashMap other than option 3)
	// For population related various analysis (e.g. per capita)
	private HashMap<Integer, Integer> memZipCodePopulationMap = new HashMap<>(); 

	public PopulationProcessor (){
	}
	
	/*
	 * Option 2: Total Population for all ZIP code
	 * @ parameter none
	 * @ return total population
	 */
	public int calcTotalPop() {
		int totalPop = 0;
		for (PopulationData pop: populationDataList) { // Population Data to be checked & any need of check valid zip code or not
			totalPop += pop.getPopulation(); //Population Data to be checked	
		}
		return totalPop;
	}

	/*
	 * Helper method to create population per zip code for memoization
	 * @parameter int zip code
	 * @return current or updated HashMap of zip code & population
	 */
	public Map<Integer, Integer> updateMemZipCodePopulationMap (int zipCode) {
		
		if (populationDataList == null) {this.memZipCodePopulationMap = null;} 
		else {
			// Firstly check whether memoization zip code population map already has the given zip code. If exists, return the current map as it is
			if (memZipCodePopulationMap.containsKey(zipCode)) {return memZipCodePopulationMap;}
			else { // Go through population data list. If the given zip code is found, update the hash map with zip code and population of the given zip code
				for (PopulationData pop: populationDataList) {
					if (zipCode == pop.getZipCode()){
						this.memZipCodePopulationMap.put (zipCode, pop.getPopulation()); // Update the hash map
					}					
				}
			}
			}
		return memZipCodePopulationMap; // Return null if pupulationDataList is null or return the existing hash map
	}
	
	/*
	 * Helper method to get population for the given zip code from memoization Map
	 * @parameter zip code
	 * @return population
	 */
	
	public int calcZipCodePopulation (int zipCode) {
		
		HashMap<Integer, Integer> zipCodePopoulationMap = new HashMap<>();
		zipCodePopoulationMap = (HashMap<Integer, Integer>) updateMemZipCodePopulationMap(zipCode);
		int zipCodePopoulation = zipCodePopoulationMap.get(zipCode);
		return zipCodePopoulation;
	}
	

}
