package edu.upenn.cit594.datamanagement;

import static org.junit.Assert.*;

import java.io.IOException;
import org.junit.Test;

public class CSVReaderTest {
	
	@Test
	public void testWrongExtension() throws IOException{
		// wrong format - should throw IOException
		boolean thrown = false;
		String filename = "tricky.css";
		try {
			CSVReader csvReader = new CSVReader(filename);
		}
		catch(IOException e) {
			thrown = true;
		}
		assertTrue(thrown);
	}

	@Test
	public void testNullFile() throws IOException{
		boolean thrown = false;
		String filename = null;
		try {
			var csvReader = new CSVReader(filename);
		}
		catch(IOException e) {
			thrown = true;
		}
		assertTrue(thrown);
	}
	
	@Test
	public void testReadRow() throws IOException {
		String filename1 = "tricky.csv";
		var csvReader1 = new CSVReader(filename1); 
		String [] header1;
		header1 = csvReader1.readRow();
		assertEquals(3, header1.length);
		assertEquals("Header field 0", header1[0]);
		assertEquals("\",\"\",\"\"\",,\",fun, right?", header1[1]);
		assertEquals("\n" + "Still a header field (2 to be specific", header1[2]);
		String [] row1_1;
		row1_1 = csvReader1.readRow();
		assertEquals(3, row1_1.length);
		assertEquals("0.0", row1_1[0]);
		assertEquals("0.1", row1_1[1]);
		assertEquals("0.2", row1_1[2]);
		String [] row1_2;
		row1_2 = csvReader1.readRow();
		assertEquals(3, row1_2.length);
		assertEquals("1.0", row1_2[0]);
		assertEquals("1.1", row1_2[1]);
		assertEquals("1.2", row1_2[2]);
		String [] row1_3;
		row1_3 = csvReader1.readRow();
		assertEquals(3, row1_3.length);
		assertEquals("2.0, but only because it's zero indexed" + "\n" + "I think", row1_3[0]);
		assertEquals("2.1", row1_3[1]);
		assertEquals("\"2.2\"", row1_3[2]);
		
		
		String filename2 = "quirky_props.csv";
		var csvReader2 = new CSVReader(filename2); 
		String [] header2;
		header2 = csvReader2.readRow();
		assertEquals(78, header2.length);
		assertEquals("market_value", header2[0]);
		assertEquals("market_value_date", header2[75]);
		String [] row2_1;
		row2_1 = csvReader2.readRow();
		assertEquals("1662300.0", row2_1[0]); // market_value
		assertEquals("", row2_1[2]); // empty field
		assertEquals("", row2_1[77]); // empty field
		assertEquals("19106", row2_1[69]); // zipcode
		assertEquals("3030.0", row2_1[70]); // total_liveable_area
		int count = 2;
		String[] row;
		while ((row = csvReader2.readRow()) != null) {
            count++;
        }
		assertEquals(5, count);
		
		String filename3 = "population.csv";
		var csvReader3 = new CSVReader(filename3); 
		String [] row_3;
		int count2 = 0;
		while ((row_3 = csvReader3.readRow()) != null) {
			assertEquals(2, row_3.length);
            count2++;
        }
		assertEquals(50, count2);
	}
}

