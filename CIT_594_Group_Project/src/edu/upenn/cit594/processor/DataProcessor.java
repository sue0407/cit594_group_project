package edu.upenn.cit594.processor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import edu.upenn.cit594.util.PopulationData;
import edu.upenn.cit594.util.CovidData;
import edu.upenn.cit594.util.PropertyData;

public class DataProcessor {
	
	// Data from data utility
	private List <PopulationData> populationDataList;
	private List <CovidData> covidDataList;
	private List <PropertyData> propertyDataList;

	
	// TBD DO we need readers in processor?
	private PopulationReader populationReader;
	private CovidReader covidReader;
	private PropertyReader properyReader;
	
	// Memoization data set (first Integer is zip code for all HashMap other than option 3)
	// For population related various analysis (e.g. per capita)
	private HashMap<Integer, Integer> memZipCodePopulationMap = new HashMap<>(); 
	// Option 3 for partial or full total vaccinations per capita for the specific date.  More explanation later in option 3 method
	private HashMap<String, HashMap<Integer, double[]>> mimDateVacciMap = new HashMap<String, HashMap<Integer, double[]>>(); 
	// Option 4 for average market value of property for the given zip code
	private HashMap<Integer, Integer> memZipCodeAveMktValMap  = new HashMap<>(); 
	// Option 5 for average total livable area of property for the given zip code
	private HashMap<Integer, Integer> memZipCodeAveLivAreaMap  = new HashMap<>(); 
	// Option 6 for total market value of property per capita for the given zip code
	private HashMap<Integer, Integer> memZipCodeMktValPerCapMap = new HashMap<>(); 
	
	// Other map for option 3
	private HashMap<String, HashMap<Integer,double[]>> dateVacciMap;
	
	public DataProcessor () {
	}

	// TBD how to use readers?
	public DataProcessor (PopulationReader populationReader, CovidReader covidReader, PropertyReader properyReader) {
		this.populationReader = populationReader;
		this.covidReader = covidReader;
		this.propertyReader = propertyReader;
	}
	
	/*
	 * Option 1: Available actions
	 * Currently in the UserInterface class
	 */
//	public int showAvailableActions () {
//		
//	}
	
	
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
	 * Option 3: Partial or Full Vaccinations Per Capita for a Specified Date
	 * @parameter vaccination status (partial or full) and specified date
	 * @return Map of zip code (int) and vaccinations per capita (double)
	 */
	
	public TreeMap<Integer, Double> calcVacciPerCap (String vacciStatus, String date){
		
		// Set up variables
		int zipCode; // TBD
		double numVacciPerCap;
//		double[] numVacciPerCap = new double [2];
		double printNumVacciPerCap; 
		Comparator<Integer> comparator = (i1, i2) -> i1.compareTo(i2); // TBD Sort map by zip code
		TreeMap<Integer, Double> printVacciParCapMap = new TreeMap<>(comparator); // TreeMap for printout
		
		// Get HashMap of vaccinations data from helper method 
		// first double for partially vaccinated per cap and second full
		HashMap<String, HashMap<Integer, double[]>> dateVacciMap = new HashMap<String, HashMap<Integer, double[]>>();
		dateVacciMap = updateVacciPerCapMap(date);
		
		for (String key1: dateVacciMap.keySet()) { // TBD how to get data from nested Map
			if (date.equals(key1)){
				for (HashMap<Integer, double[]> key2: dateVacciMap.values()) {
					if (vacciStatus == "partial") {
						numVacciPerCap = (double) dateVacciMap.get(key1).get(key2)[0];
						
					} else { // vacciStatus == "full")
						numVacciPerCap = (double) dateVacciMap.get(key1).get(key2)[1];
					}
					zipCode = (int) dateVacciMap.get(key1).getKey(); // TBD How to get zipcode key int od nested map?
					printVacciParCapMap.put(zipCode, numVacciPerCap);
					
				} return printVacciParCapMap;
			} else {
				System.out.println(0);
				System.out.println("There was no vaccinations data for the specified date");
		}
		}
	}
	
	/*
	 * Helper method for Option 3 to create HashMap / memoization to include vaccinations related data
	 * HashMap structure: Outside String is date. Inside Map Integer is zip code
	 * Within inside Map, two double are per capita vaccinations (first is number of partially vaccinated / second is fully vaccinated)
	 * @ parameter specified date (no parameter for partial or full vaccinations / update HashMap for both data)
	 * @ return Map of date (String), zip code (int) and vaccinations per capita (double)
	 */
	
	public HashMap<String, HashMap<Integer,double[]>> updateVacciPerCapMap(String date) {
		
		if (covidDataList == null || populationDataList == null) {
			System.out.println("Covid data or pupulation data are null.");
			this.dateVacciMap = null;
			return dateVacciMap; 
		}
		
		// Variables set up
		int zipCode;
		int zipCodePopulation;
		int partialVacci;
		int fullVacci;
		double[] numVacciPerCap = new double [2]; // first double for partially vaccinated per cap and second full
		
		// Return the existing data from the hash map if it already exists
		if (mimDateVacciMap.containsKey(date)) { 
			return mimDateVacciMap;
		} 
		
		else {
			this.dateVacciMap = new HashMap<String, HashMap<Integer, double[]>>();
			HashMap<Integer, double[]> covidZipCodeNumVacciMap = new HashMap<>();
			covidZipCodeNumVacciMap.put(zipCode, numVacciPerCap);
			
			for (CovidData covid: covidDataList) {
				if (date.equals(covid.getDate())) { // TBD how to get date?
					zipCode = covid.getZipCode();
					zipCodePopulation = calcZipCodePopulation(zipCode);
						
					partialVacci = covid.getPartiallyVaccinated();
					numVacciPerCap[0] = (double) (partialVacci / zipCodePopulation) ; // Partially vaccinated per capita
					fullVacci = covid.getFullyVaccinated();
					numVacciPerCap[1] = (double) fullVacci / zipCodePopulation ; // Fully vaccinated per capita
					covidZipCodeNumVacciMap.put(zipCode, numVacciPerCap);
					}
					mimDateVacciMap.put(date, covidZipCodeNumVacciMap); // Update mimoization map
					dateVacciMap.put(date, covidZipCodeNumVacciMap);
					return dateVacciMap;
			}
		}
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
	}

	
	/*
	 * Option 6: Total Market Value Per Capita for a specific ZIP code
	 * @ parameter specified ZIP code
	 * @ return total ave. market value per capita
	 */
	
	public int [ ] calcTotalMktValPerCap(int zipCode) {
		
		if (propertyDataList == null || populationDataList == null) {
			System.out.println("Property data or pupulation data are null.");
			return null; // OK with return null?
		} 
		
		// Set variables for return
		int aveMktValPerCap = 0;
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
			int zipCodePopoulation = calcZipCodePopulation(zipCode);
			zipCodeMktValPerCap[0] = zipCode;
			if (zipCodePopoulation == 0) { // Return 0 if the population of the given zip code is 0
				zipCodeMktValPerCap[1] = 0;
				return zipCodeMktValPerCap;
			}
			zipCodeMktValPerCap[1] = (int) sumMktVal / zipCodePopoulation ;
			return zipCodeMktValPerCap;
		}
		
	}
	
	/*
	 *Option 7: Additional feature
	 * @ parameter
	 * @ return
	 */
	
	
	
	
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
	
	/*
	 *  Get average data of property (market value or total livable area) for the given zip code
	 *  Strategy design pattern
	 */
	
	public static int calcAverage(int zipCode, PropertyData propertyData) {
			

		}
	
	
}
