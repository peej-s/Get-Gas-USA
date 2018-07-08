package exchange;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/* 
 * Pulls gas price from two cities and makes a comparison
 * Assume 1st city is Canadian and 2nd city is USA, and converts USA units to CAD units
 * USD/gallon -> CAD/Litre
 */

public class GasCompare {
	private static final String GAS_PROVIDER = "https://www.gasbuddy.com/";
	
	private double lowestPrice(String city){
		StringBuffer buffer = new StringBuffer();
		try {
			URL url = new URL(GAS_PROVIDER + "home?search=" + city.toLowerCase() + "&fuel=1");
			URLConnection uc = url.openConnection();
			uc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));			
			Document doc; 
			String elements[];
			double lowestPrice = 0;
			//double averagePrice = 0; // Will look into average prices later
			
			String data = in.readLine();
			while (data != null) {
				buffer.append(data);
				data = in.readLine();
			}
			in.close();
			
			doc = Jsoup.parse(buffer.toString());
			elements = doc.html().split("\n");
			
			for (int i = 0; i < elements.length; i++){
				if (elements[i].contains("Lowest Price"))
				{
				    lowestPrice = Double.parseDouble(Jsoup.parse(elements[i-1]).text().replaceAll("[$¢]",""));
				}
			}
			return lowestPrice;
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return -1;
		}	
	}
	
	public double convertUC(double price){
		// Convert a USA gas price (USD/gallon) to a CAN gas price (CAD/litre)
		DecimalFormat df = new DecimalFormat("####0.00");
        CurrencyConverter cc = new CurrencyConverter();
		double USD2CAD = cc.conversionRate("USD", "CAD");
		double G2L = 3.78541; // Gallons to Litre
		
		return Double.parseDouble(df.format(price * USD2CAD / G2L)); 
		
	}

	public double[] getGasPrices(String city1, String city2){
		double priceCAN = lowestPrice(city1); // in CAD cents per litre
		double priceUSA = lowestPrice(city2); // in USD dollars per litre
				
	    return new double[] {priceCAN, priceUSA};
	}
	
	public static void main(String[] args) {
		String city1 = "Toronto"; // Canadian City
		String city2 = "Buffalo"; // American City
		GasCompare gc = new GasCompare();
		double[] result = gc.getGasPrices(city1, city2);
		System.out.println("The lowest price in " + city1 + " is: " + result[0] + " CAD cents per litre.");
		System.out.println("The lowest price in " + city2 + " is: " + result[1] + " USD per gallon.");
	}
	
}
