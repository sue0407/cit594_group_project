package edu.upenn.cit594.processor;

import static org.junit.Assert.*;

import org.junit.Test;

public class DataProcessorTest {

	@Test
	public void testCheckValidZip() throws Exception{
		// when it's not property data, zipcode is not valid if it's not 5 digits 
		assertFalse(DataProcessor.checkValidZip(0, false));
		assertTrue(DataProcessor.checkValidZip(12345, false));
		assertEquals(12345, DataProcessor.returnValidZip(12345));
		
		// when it's property data, zipcode is not valid if it's under 5 digits
		// return first 5 digits if it's more than 5 digits
		assertFalse(DataProcessor.checkValidZip(111, true));
		assertTrue(DataProcessor.checkValidZip(99999, true));
		assertEquals(99999, DataProcessor.returnValidZip(99999));
		assertTrue(DataProcessor.checkValidZip(123456, true));
		assertEquals(12345, DataProcessor.returnValidZip(123456));
	}
	
	@Test
	public void testCheckValidTimestamp() throws Exception{
		assertFalse(DataProcessor.checkValidTimestamp("2022063022"));
		assertTrue(DataProcessor.checkValidTimestamp("2021-03-25 17:20:02"));
	}

}
