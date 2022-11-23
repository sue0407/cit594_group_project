package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
	
	private BufferedReader reader;
	
	public CSVReader(String filename) throws IOException {
		if(filename != null) {
			if(filename.trim().toLowerCase().endsWith(".csv")) {
				try {
					this.reader = Files.newBufferedReader(Paths.get(filename));
				}
				catch(FileNotFoundException e) {
					System.out.println(filename+" is not found.");
				}
				catch(IOException e) {
					e.printStackTrace();
			}
		}
			else {
				System.out.println("the file doesn't end with csv extension.");
				throw new IOException();
			}
		}
		else {
			System.out.println("the input file is null.");
			throw new IOException();
		}
}
	
	   /**
     * This method reads in just enough characters to process a single valid CSV row, 
     * represented as an array of strings where each element of the array is a field of the row. 
     *
     * @return a single row of CSV represented as a string array, where each
     *         element of the array is a field of the row; or {@code null} when
     *         there are no more rows left to be read.
     * @throws IOException when the underlying reader encountered an error
     */
	enum STATES {STARTOFFIELD, ESCAPEDFIELD, UNESCAPEDFIELD, STARTOFLINE, ENDOFLINE, ENDOFTEXT, FOUNDCR};
	public String[] readRow() throws IOException{
		
		// initialize the char states we want to check in the state machine
    	final int comma = 44;
    	final int lf = 10;
    	final int cr = 13;
    	final int dquote = 34;
    	
    	// initialize startingChar, nextChar, nextNextChar and StringBuilder sb
    	int startingChar;
    	int nextChar;
    	int nextNextChar;
    	StringBuilder sb = new StringBuilder();
    	
    	// initialize an arraylist called lines
    	List<String> lines = new ArrayList<String>();
    	
    	// set the initial state as START
    	STATES state = STATES.STARTOFFIELD;
    	
    	// end the while loop until we hit ENDOFTEXT state
    	while(state != STATES.ENDOFTEXT) {
        	
    		switch(state) {
    		
       		// we are at STARTOFFIELDNOW
    		case STARTOFFIELD:
    			
    			// read the first character, save into startingChar
    	    	startingChar = reader.read();
    	    	
    	    	switch(startingChar) {
    			
    			default:
    				// append the first character to sb
    				sb.append((char)startingChar);
    				// switch the state to UNESCAPEDFIELD
    				state = STATES.UNESCAPEDFIELD;
    				break;
    	    	
    			// check startingChar's value to see if it hits the end of the text
    			case -1: 
    				// switch state of ENDOFTEXT and we should return null
    				state = STATES.ENDOFTEXT;
    				this.reader.close();    				
    				break;
    				
    			// if the startingChar is a double quote, it is an escapedfield
    			case dquote:
    				state = STATES.ESCAPEDFIELD;
    				break;
    				
    			// if the field is an unescapedfield, it should not start with anything other than textdata
        		// if we come across cr/lf in the beginning of the line, we should return an empty field []
        		case lf:
        			lines.add("");
        			state = STATES.ENDOFLINE;
        			break;
        			
        		case cr:
        			lines.add("");
        			state = STATES.FOUNDCR;
        			break;
        				
        		// comma at start: suppose to add an empty field	
        		case comma:
        			state = STATES.STARTOFLINE;
        			break;
        		}
        		break;
        		
        	// we are at UNESCPEDFIELD
        	case UNESCAPEDFIELD:
       			// read next character, save it into nextChar
        		nextChar = reader.read();
        						
        		switch(nextChar) {
        		
        		// by default, the nextChar should be a textData, we append each char to sb
				default:
					sb.append((char)nextChar);
					break;
					
        		// if the nextChar is a comma, do nothing and switch the state to STARTOFLINE
        		case comma:
        			state = STATES.STARTOFLINE;
        			break;
        							
        		// if the nextChar is a dquote, it's not legal and should throw an error
        		case dquote:
        			System.out.println("format error: unescaped textdata shouldn't contain double quote.");
        			break;
        		
        		// end of line
        		case lf:
        			// add sb to lines
        			lines.add(sb.toString());
        			state = STATES.ENDOFLINE;
        			break;

        		case cr:
        			state = STATES.FOUNDCR;
        			//lines.add(sb.toString());
        			break;
        		}
        		break;
        		
    		// we are at ESCAPEDFIELD
    		case ESCAPEDFIELD:
    				
    			// use reader to get both nextChar and nextNextChar
    			nextChar = reader.read();
    				
    			switch(nextChar){
    			
    				default:
    					sb.append((char)nextChar);
    					break;
    					
    				case dquote:
    					nextNextChar = reader.read();
    					// 2 double quotes case
    					if(nextNextChar == dquote) {
    						sb.append((char)nextNextChar);
    					}
    					// end of field
    					else if(nextNextChar == comma){
    						lines.add(sb.toString());
    						sb = new StringBuilder();
    						state = STATES.STARTOFFIELD;
    					}
    					else if (nextNextChar == cr ) {
    						state = STATES.FOUNDCR;
    						//lines.add(sb.toString());
    						break;
    					}
    					else if (nextNextChar == lf) {
    						state = STATES.ENDOFLINE;
    						lines.add(sb.toString());
    						break;
    					}
    					else{
    						state = STATES.STARTOFLINE;
    					}
    					break;				
    				}
    				break;
    				
    			// we are at STATEOFLINE state
    			case STARTOFLINE:
    								
    				// check if the sb is null or empty
    				if(sb.toString() != null) {
    				// add sb to lines
    				lines.add(sb.toString());
    				// re-initialize the string builder
    				sb = new StringBuilder();
    				// set the state to STARTOFFIELD
    				state = STATES.STARTOFFIELD;
    				break;
    			}
    			break;
    							
    			// we are at ENDOFLINE state
    			case ENDOFLINE:
    				String[] linesArray = (String[]) lines.toArray(new String[lines.size()]);
    				return linesArray;
    				
    			case ENDOFTEXT:
    				return null;
    				
    			case FOUNDCR:
    				nextChar = reader.read();
    				switch(nextChar) {
    				
    				default:
						System.out.println("format error: unescaped textdata shouldn't contain double quote.");
						break;
						
    				case lf:
    					state = STATES.ENDOFLINE;
    					// add sb to lines
            			lines.add(sb.toString());
    					break;
		
    				}
    				break;

    		}
    	}
		return null;
	}
}
