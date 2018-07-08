package main;

import exchange.*;

public class GetGasUSA {

	public static void main(String[] args){
		double CAD;
		double USD;
		double gasPriceCAD;
		double gasPriceUSD;
		
		String location;
		double gasNeeded;
		double moneySaved;
		
		GasCompare gc = new GasCompare();
				
		String city1 = "Vancouver"; // Canadian City
		String city2 = "Blaine"; // American City

		double[] result = gc.getGasPrices(city1, city2);
		
		double cGas = result[0];
		double aGas = result[1];
		aGas = gc.convertUC(aGas);
		moneySaved = (cGas/100) - aGas;
		
		System.out.println("In the USA, you will save $" + moneySaved + " CAD on every litre.");
		System.out.println("The next question is... is that worth it?");
		// Depends on your car and your location!
		
		
	}
}
