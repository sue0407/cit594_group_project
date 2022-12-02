package edu.upenn.cit594.processor;

import java.util.HashMap;
import java.util.List;

import edu.upenn.cit594.util.PropertyData;



public class PropertyAverageCalculator {
	
	// Data from data utility
	private List <PropertyData> propertyDataList;
	
	// Memoization data set (first Integer is zip code for all HashMap other than option 3)
	// For population related various analysis (e.g. per capita)
	// Option 4 for average market value of property for the given zip code
	private HashMap<Integer, Integer> memZipCodeAveMktValMap  = new HashMap<>(); 
	// Option 5 for average total livable area of property for the given zip code
	private HashMap<Integer, Integer> memZipCodeAveLivAreaMap  = new HashMap<>(); 

	public int [] calcAverage(int zipCode, String dataType) {
		
		if (propertyDataList == null) {
			System.out.println("Property data are null.");
			return null; // OK with return null?
		} 
		
		// Set variables for return
		int aveData = 0;
		int [] zipCodeAve = new int [2];
		
		if (dataType.equals("marketValue")) {
			// Return the existing data from the hash map if it already exists
			if (memZipCodeAveMktValMap.containsKey(zipCode)) {
				zipCodeAve[0] = zipCode;
				zipCodeAve[1] = memZipCodeAveMktValMap.get(zipCode);
				return zipCodeAve;
			
		} else { // dataType is "livableArea"
			// Return the existing data from the hash map if it already exists
			if (memZipCodeAveLivAreaMap.containsKey(zipCode)) {
				zipCodeAve[0] = zipCode;
				zipCodeAve[1] = memZipCodeAveLivAreaMap.get(zipCode);
				return zipCodeAve;
			}
		}
		
		double sumData = 0;
		int sumCount = 0;
		for (PropertyData prop: propertyDataList) {	
			if (zipCode == prop.getZipCode()) {
				if (dataType.equals("marketValue")) {
					sumData += prop.getMarketValue();
				} else { // dataType is "livableArea"
					sumData += prop.getTotalLivableArea();
				}
				sumCount ++;
				}
				aveData = (int) sumData / sumCount;
				if (dataType.equals("marketValue")) {
					memZipCodeAveMktValMap.put(zipCode, aveData); // Update memoization hash map
				} else { // dataType is "livableArea"
					memZipCodeAveLivAreaMap.put(zipCode, aveData); // Update memoization hash map
				}
				zipCodeAve[0] = zipCode;
				zipCodeAve[1] = aveData;
				return zipCodeAve;
			}
		}
		return zipCodeAve;
	}

}
