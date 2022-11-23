package edu.upenn.cit594.datamanagement;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import edu.upenn.cit594.util.CovidData;

public class CovidJSONReaderTest {

	@Test
	public void testReadCovidJSONFile() throws Exception {
		String filename = "covid_data_downsampled.json";
		ArrayList<CovidData> covidList = CovidJSONReader.readCovidJSONFile(filename);
		for(CovidData covidData : covidList) {
			int zipcode = covidData.getZipCode();
			System.out.println("zipcode: " + zipcode);
			String timestamp = covidData.getTimeStamp();
			System.out.println("timestamp: " + timestamp);
			int partiallyVaccinated = covidData.getPartiallyVaccinated();
			System.out.println("partiallyVaccinated: " + partiallyVaccinated);
			int fullyVaccinated = covidData.getFullyVaccinated();
			System.out.println("fullyVaccinated: " + fullyVaccinated);
			System.out.println("\n");
		}
	}

}
