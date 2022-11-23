package edu.upenn.cit594.datamanagement;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import edu.upenn.cit594.util.CovidData;
import edu.upenn.cit594.processor.DataProcessor;


public class CovidJSONReader {
	
	String filename;
	
	public CovidJSONReader(String filename) {
		this.filename = filename;
	}
	
	public static ArrayList<CovidData> readCovidJSONFile(String filename) throws Exception{
		// initialize returned list
		ArrayList<CovidData> covidList  = new ArrayList<CovidData>();
		
		if(filename != null && filename.trim().toLowerCase().endsWith(".json")) {
			try {
				FileReader fileReader = new FileReader(filename);
				// parsing file
				JSONParser parser = new JSONParser();
		        try{
		        	JSONArray jsonArray = (JSONArray)parser.parse(fileReader);

			        for(int i=0; i<jsonArray.size(); i++) {
			        	// initialize variables to construct CovidData
			        	int zipcode = 0;
			        	String timestamp = null;
			        	int partiallyVaccinated = 0;
			        	int fullyVaccinated = 0;
			        	// get each line
			        	JSONObject jsonObj = (JSONObject) jsonArray.get(i);
			        	@SuppressWarnings("unchecked")
						Set<String> keys = jsonObj.keySet();
			        	if(keys.contains("zip_code")) {
			        		// get each zipcode and convert to int
			        		try {
			        			int tempZip = Integer.parseInt(jsonObj.get("zip_code").toString());
			        			// if the zipcode is valid
			        			if(DataProcessor.checkValidZip(tempZip, false)) {
			        				zipcode = DataProcessor.returnValidZip(tempZip);
			        			}
			        			// if the zipcode is not valid, ignore the whole record
			        			else {
			        				continue;
			        			}
			        		}
			        		// if we fail to cast zipcode to numeric, ignore the whole record
			        		catch (NumberFormatException ex){
			                    ex.printStackTrace();
			                    continue;
			                }
			        	}
			        	else {
			        		continue;
			        	}
			   
			        	if(keys.contains("etl_timestamp")) {
			        		try {
			        			String tempTimestamp = (String) jsonObj.get("etl_timestamp");
				        		// check if the format is valid timestamp
				        		if(DataProcessor.checkValidTimestamp(tempTimestamp)) {
				        			timestamp = tempTimestamp;
				        		}
				        		// else, ignore the whole line
				        		else {
				        			continue;
				        		}
			        		}
			        		catch (IllegalFormatException e) {
			        			e.printStackTrace();
			        			continue;
			        		}
			        	}
			        	else {
			        		continue;
			        	}
			        	
			        	if(keys.contains("partially_vaccinated") && jsonObj.get("partially_vaccinated") != null) {
			        		partiallyVaccinated = Integer.parseInt(jsonObj.get("partially_vaccinated").toString());
			        	}
			        	else {
			        		partiallyVaccinated = 0;
			        	}
			        	
			        	if(keys.contains("fully_vaccinated") && jsonObj.get("fully_vaccinated") != null) {
			        		fullyVaccinated = Integer.parseInt(jsonObj.get("fully_vaccinated").toString());
			        	}
			        	else {
			        		fullyVaccinated = 0;
			        	}
			        	
			        	CovidData covidData = new CovidData(zipcode, timestamp, partiallyVaccinated, fullyVaccinated);
			        	// add the CovidData object to returned covidList
			        	covidList.add(covidData);
			        } 			        
		        }
		    catch(Exception e) {
		    	e.printStackTrace();
		    	return covidList;
		    }
		        fileReader.close();
			}
			catch(FileNotFoundException e) {
				System.out.println(filename+" is not found.");
				return covidList;
			}
			catch(IOException e) {
				e.printStackTrace();
				return covidList;
			}
			catch (Exception e) {
	    		e.printStackTrace();
	    		return covidList;
	    	}
		}
		else {
			System.out.println("the input file is not a .json file.");
			return null;
		}
		
		return covidList;
	}
}
