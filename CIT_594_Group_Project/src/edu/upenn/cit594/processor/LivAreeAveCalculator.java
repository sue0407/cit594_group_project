package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.PropertyData;

public class LivAreeAveCalculator implements AveCalculator {

	@Override
	public int calcAverage (int zipCode, PropertyData propertyData) {
		propertyData.getTotalLivableArea();
	}

}
