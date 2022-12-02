package edu.upenn.cit594.processor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import edu.upenn.cit594.util.CovidData;
import edu.upenn.cit594.util.PopulationData;
import edu.upenn.cit594.util.PropertyData;

public class CovidProcessor {
	
	// Data from data utility
	private List <PopulationData> populationDataList;
	private List <CovidData> covidDataList;
	
	// Memoization data set (first Integer is zip code for all HashMap other than option 3)
	// Option 3 for partial or full total vaccinations per capita for the specific date.  More explanation later in option 3 method
	private HashMap<String, HashMap<Integer, double[]>> mimDateVacciMap = new HashMap<String, HashMap<Integer, double[]>>(); 
	
	// Initialize other processor
	private PopulationProcessor populationProcessor;
	
	// Other map for option 3
	private HashMap<String, HashMap<Integer,double[]>> dateVacciMap;
	
	public CovidProcessor () {
		
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
					zipCodePopulation = populationProcessor.calcZipCodePopulation(zipCode);
						
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
	

}
