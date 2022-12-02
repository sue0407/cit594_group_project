package edu.upenn.cit594.processor;

import java.util.HashMap;
import java.util.List;

import edu.upenn.cit594.util.PopulationData;
import edu.upenn.cit594.util.PropertyData;

public class PropertyProcessor {
	
	// Data from data utility
	private List <PopulationData> populationDataList;
	private List <PropertyData> propertyDataList;
	
	// Initialize other processor
	private PopulationProcessor populationProcessor;
	
	// Memoization data set (first Integer is zip code for all HashMap other than option 3)
	// Option 4 for average market value of property for the given zip code
	private HashMap<Integer, Integer> memZipCodeAveMktValMap  = new HashMap<>(); 
	// Option 5 for average total livable area of property for the given zip code
	private HashMap<Integer, Integer> memZipCodeAveLivAreaMap  = new HashMap<>(); 
	// Option 6 for total market value of property per capita for the given zip code
	private HashMap<Integer, Integer> memZipCodeMktValPerCapMap = new HashMap<>(); 
	
	public PropertyProcessor() {
	}
	
	/*
	 * Option 4: Average Market Value for a specific ZIP code
	 * Note: Strategy design pattern
	 * @ parameter specified zip code
	 * @ return ave. market value
	 */
	
	public int[] calcAveMktVal(int zipCode) {
		
		if (propertyDataList == null) {
			System.out.println("Property data is null");
			return null; // TBD OK with return null?
		} 
		
		// Set variables for return
		int aveMktVal = 0;
		int [] zipCodeAveMktVal = new int [2];
		
		// Return the existing data from the hash map if it already exists
		if (memZipCodeAveMktValMap.containsKey(zipCode)) {
			zipCodeAveMktVal[0] = zipCode;
			zipCodeAveMktVal[1] = memZipCodeAveMktValMap.get(zipCode);
			return zipCodeAveMktVal;
		} 
		
		else {
			double sumMktVal = 0;
			int sumCount = 0;
			for (PropertyData prop: propertyDataList) {
				if (zipCode == prop.getZipCode()) {
					sumMktVal += prop.getMarketValue();
					sumCount ++;
				}
				aveMktVal = (int) sumMktVal / sumCount;
				memZipCodeAveMktValMap.put(zipCode, aveMktVal); // Update memoization hash map
				
				zipCodeAveMktVal[0] = zipCode;
				zipCodeAveMktVal[1] = aveMktVal;
				return zipCodeAveMktVal;
			}
			
		}
		return zipCodeAveMktVal;
	}
	
	/*
	 * Option 5: Average Total Livable Area for a specific ZIP code
	 * Note: Strategy design pattern
	 * @ parameter specified ZIP code
	 * @ return ave. livable area
	 */
	public int [] calcAveLivArea(int zipCode) {
		
		if (propertyDataList == null) {
			System.out.println("Property data are null.");
			return null; // OK with return null?
		} 
		
		// Set variables for return
		int aveLivArea = 0;
		int [] zipCodeAveLivArea = new int [2];
		
		// Return the existing data from the hash map if it already exists
		if (memZipCodeAveLivAreaMap.containsKey(zipCode)) {
			zipCodeAveLivArea[0] = zipCode;
			zipCodeAveLivArea[1] = memZipCodeAveLivAreaMap.get(zipCode);
			return zipCodeAveLivArea;
		} 
		
		else {
			double sumLivArea = 0;
			int sumCount = 0;
			for (PropertyData prop: propertyDataList) {
				if (zipCode == prop.getZipCode()) {
					sumLivArea += prop.getTotalLivableArea();
					sumCount ++;
				}
				aveLivArea = (int) sumLivArea / sumCount;
				memZipCodeAveLivAreaMap.put(zipCode, aveLivArea); // Update memoization hash map
				
				zipCodeAveLivArea[0] = zipCode;
				zipCodeAveLivArea[1] = aveLivArea;
				return zipCodeAveLivArea;
			}
			
		}
		return zipCodeAveLivArea;
	}

	/*
	 * Option 6: Total Market Value Per Capita for a specific ZIP code
	 * @ parameter specified ZIP code
	 * @ return total ave. market value per capita
	 */
	
	public int [] calcTotalMktValPerCap(int zipCode) {
		
		if (propertyDataList == null || populationDataList == null) {
			System.out.println("Property data or pupulation data are null.");
			return null; // OK with return null?
		} 
		
		// Set variables for return
		int [] zipCodeMktValPerCap = new int [2];
		
		// Return the existing data from the hash map if it already exists
		if (memZipCodeMktValPerCapMap.containsKey(zipCode)) {
			zipCodeMktValPerCap[0] = zipCode;
			zipCodeMktValPerCap[1] = memZipCodeMktValPerCapMap.get(zipCode);
			return zipCodeMktValPerCap;
		} 
		
		// Calculate total property market value by the given zip code or return 0 value if zip code does not exist
		double sumMktVal = 0;
		int sumPropCount = 0;
		for (PropertyData prop: propertyDataList) {
			if (zipCode == prop.getZipCode()) {
				sumMktVal += prop.getMarketValue();
				sumPropCount ++;
			}
		}
		if (sumMktVal == 0 || sumPropCount == 0) { // Return 0 if total property market value is 0 or zip code does not exist (sumPropCount is 0)
			zipCodeMktValPerCap[0] = zipCode;
			zipCodeMktValPerCap[1] = 0;
			return zipCodeMktValPerCap;
		} else {
			int zipCodePopoulation = populationProcessor.calcZipCodePopulation(zipCode);
			zipCodeMktValPerCap[0] = zipCode;
			if (zipCodePopoulation == 0) { // Return 0 if the population of the given zip code is 0
				zipCodeMktValPerCap[1] = 0;
				return zipCodeMktValPerCap;
			}
			zipCodeMktValPerCap[1] = (int) sumMktVal / zipCodePopoulation ;
			return zipCodeMktValPerCap;
		}
		
	}

	
}
