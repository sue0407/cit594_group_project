package edu.upenn.cit594.ui;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Pattern;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.CovidReader;
import edu.upenn.cit594.processor.DataProcessor;
import edu.upenn.cit594.processor.PopulationReader;
import edu.upenn.cit594.processor.PropertyReader;

public class UserInterface {
	
	protected DataProcessor processor;
	protected Logger logger;
	
	// Data reader
	private PopulationReader populationReader;
	private CovidReader covidReader;
	private PropertyReader properyReader;
	
	public UserInterface(DataProcessor processor, Logger logger) {
		
	public void displayMenuAndResults() {
	
		try {
			
			String menuPrompt = "\nPlease enter an option between 1-7, or 0 to Exit program: \n" +
	                "1. Available Actions\n" +
	                "2. Total Population for All ZIP Codes\n" +
	                "3. Partial or Full Vaccinations Per Capita for each ZIP code for the Specified Date\n" +
	                "4. Average Market Value for the Specified ZIP Code\n" +
	                "5. Average Total Livable Area for the Specified ZIP Code\n" +
	                "6. Total Market Value Per Capita for the Specified ZIP Code\n" +
	                "7. Additional Feature (TBD)\n";
			System.out.println("Welcome!" + menuPrompt);
			System.out.print("> ");
			Scanner sc = new Scanner (System.in);
			int inputInt = sc.nextInt();
			System.out.flush(); // TBD Check where flush() to be placed
			
			// Keep asking for input if it is not 0-7
			while (! (inputInt == 0 || inputInt == 1 || inputInt == 2 || inputInt == 3 || 
					inputInt == 4 || inputInt == 5 || inputInt == 6 || inputInt == 7)){
				System.out.println("Input error. Please enter valid input 0-7: ");
				System.out.print("> ");
				inputInt = sc.nextInt();
				System.out.flush(); // TBD Check where flush() to be placed
			}
			
			while (! (inputInt == 0)) {
			
				
				switch (inputInt) {
				
				case (1): // Available Actions
					System.out.println("BEGIN OUTPUT");
					System.out.println("0");
					System.out.println("1");
					// TBD Printout of other available actions? Shall we use readers?
					if (populationReader != null)  System.out.println("2");}
					if (! (covidReader == null || populationReader == null)) {System.out.println("3");}
					if (propertyReader != null) {
						System.out.println("4");
						System.out.println("5");
						}
					if (! (covidReader == null || propertyReader == null)) {System.out.println("6");}
					System.out.println("END OUTPUT");

				
				case (2): // Total Population for All ZIP Codes
					int totalPop = processor.calcTotalPop() ;
					System.out.println(totalPop);
					break;
					
				case (3): // Partial or Full Vaccinations Per Capita
					System.out.println("Enter vaccination status of \"partial\" or \"full\": ");
					System.out.print("> ");
					String vacciInput = sc.next().toLowerCase(); 
					while (! (vacciInput == "partial" || vacciInput == "full")) {
						System.out.println("Invalid input. Enter vaccination status of \"partial\" or \"full\": ");
						System.out.print("> ");
						vacciInput = sc.next().toLowerCase();
					}
					System.out.println("Enter the date (YYYY-MM-DD): ");
					System.out.print("> ");
					String dateInput = sc.next();
					while (!dateInputCheck(dateInput)){
						System.out.println("Invalid input. Enter the date (YYYY-MM-DD): ");
						System.out.print("> ");
						dateInput = sc.next();
					}
					TreeMap<Integer, Double> vacciPerCap = new TreeMap<Integer, Double>();
					vacciPerCap = processor.calcVacciPerCap(vacciInput, dateInput);
					System.out.println("BEGIN OUTPUT");
					System.out.println(); // TBD Print out
					for (Map.Entry<Integer, Double> entry: vacciPerCap.entrySet()){
						int zipCode = vacciPerCap.firstKey();
						double numVacciPerCap = ((Entry<Integer, Double>) vacciPerCap).getValue();
						System.out.println(zipCode + " " + numVacciPerCap);
					}
					System.out.println("END OUTPUT");
					break;
					
				case (4): // Average Market Value
					System.out.println("Enter a 5-digit ZipCode: \n"); // Use helper method to check valid input
					System.out.print("> ");
					int zipCode = sc.nextInt();
					int [] aveMktVal = processor.calcAveMktVal(zipCode);
					System.out.println(aveMktVal[1]);
					break;
					
				case (5): // Average Total Livable Area
					System.out.println("Enter a 5-digit ZipCode: \n"); //Use helper method to check valid input
					zipCode = sc.nextInt();
					int [] aveTotalLivArea = processor.calcAveLivArea(zipCode);
					System.out.println(aveTotalLivArea[1]);
					break;
					
				case (6): //Total Market Value Per Capita
					System.out.println("Enter a 5-digit ZipCode: \n"); // Use helper method to check valid inputt
					zipCode = sc.nextInt();
					int [] totalMktValPerCap = processor.calcTotalMktValPerCap(zipCode);
					System.out.println(totalMktValPerCap[1]);
					break;
					
				case (7):
					
				default:
					System.out.println("Error: Unexpected error in input\n");
			}
			System.out.println("You choose to exit.");
			return;
			
			
		} catch (IOException e) {
			System.out.println("Error: Input or output error");
			e.printStackTrace();
		}
	}
	}
	

	/*
	 * Helper method to check date input format
	 * @parameter date input
	 * @return boolean result
	 */
	public boolean dateInputCheck (String dateInput) { // TBD correct codes?
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate.parse(dateInput, dtf);
			return true;
		} catch (Exception ingore){
			return false;
		}
	}
	
	public static boolean checkValidZip(int zipcode, boolean property) {
		boolean valid = false;

		// check zipcode in property data (as long as zipcode has more than 5 digits it's valid)
		if(property) {
			if(zipcode >= 10000) {
				valid = true;
			}
		}
		// check zipcode in covid or population data (zipcode needs to be exactly 5 digits to be valid)
		else {
			if(zipcode >= 10000 && zipcode <= 99999) {
				valid = true;
			}
		}
		return valid;
	}
	
	public static int returnValidZip(int zipcode) {
		// when zipcode has more than 5 digits
		if(zipcode > 99999) {
			// cast zipcode to string type
			String zipcodeString = Integer.toString(zipcode);
			// only keep the first 5 characters
			zipcode = Integer.parseInt(zipcodeString.substring(0, 5)); 
			return zipcode;
		}
		else {
			return zipcode;
		}
	}
	
	// check “YYYY-MM-DD hh:mm:ss” format;
	public static boolean checkValidTimestamp(String timestamp) {
		boolean valid = false;
		String timestampRegex = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]:[0-5][0-9]$";
		// create pattern
		Pattern pattern = Pattern.compile(timestampRegex);
		if(pattern.matcher(timestamp).find()) {
			valid = true;
		}
		return valid;
	}
	
}
