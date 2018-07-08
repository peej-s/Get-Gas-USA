package exchange;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

public class CurrencyConverter {
	private static final String API_PROVIDER = "https://www.alphavantage.co/";
	private static final String API_QUERY = "query?function=CURRENCY_EXCHANGE_RATE";
	private static final String API_KEY = loadKey();
	private static final String OUTER_KEY = "Realtime Currency Exchange Rate";
	private static final String EXCHANGE_RATE_KEY = "5. Exchange Rate";
	
	public double conversionRate(String fromFX, String toFX){
		double response = getRate(API_PROVIDER + API_QUERY + "&from_currency=" + fromFX + "&to_currency=" + toFX + "&apikey=" + API_KEY);
        return response;
	}
	
	private static String loadKey() {
		// Pulls the API key from an external input file
		try{
			BufferedReader inFile = new BufferedReader(new FileReader("util/APIkey.txt"));
			String line;
			String key = null;
			while((line = inFile.readLine()) != null)
			{
			    key = line;
			}
			inFile.close();
			return key;
		} catch (FileNotFoundException e){
        	System.out.println("Missing API key file.");
        	return null;
        } catch (IOException e) {
        	System.out.println("Invalid API key file.");
        	return null;
		}
	}

	private double getRate(String api_link){
		// Parses out the exchange rate displayed within an api_link
		StringBuffer buffer = new StringBuffer();
		Gson gson = new Gson();
		LinkedTreeMap<String, LinkedTreeMap<String,String>> map = new LinkedTreeMap<String, LinkedTreeMap<String,String>>();

		try {
			URL url = new URL(api_link);
			URLConnection uc = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
						
			String data = in.readLine();
			while (data != null) {
				buffer.append(data);
				data = in.readLine();
			}
			in.close();
			
			map = gson.fromJson(buffer.toString(), Map.class);
			return Double.parseDouble(map.get(OUTER_KEY).get(EXCHANGE_RATE_KEY));
		} catch (IOException e){
			System.out.println(e.getMessage());
			e.printStackTrace();
			return -1;
		}
	}
	
	public static void main(String[] args) {
		CurrencyConverter cc = new CurrencyConverter();
        System.out.println(cc.conversionRate("CAD", "USD"));
	}

}
