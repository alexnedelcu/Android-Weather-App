package com.cs275.uweather;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/*
 * Weather class will serve as a way to read the data from REST
 * weather API and load it in built-in Java data structures.
 * 
 * @author		Alexandru Nedelcu
 */
public class Weather {
	private static String _url = "http://api.wunderground.com/api/257fbd10479ead52/forecast10day/q/19104.json";
	
	static List<Date> _dDate = new ArrayList<Date>();
	static List<Float> _dTemperatureHigh = new ArrayList<Float>();
	static List<Float> _dTemperatureLow = new ArrayList<Float>();
	static List<String> _dDayName = new ArrayList<String>();
	static List<String> _dImageURL = new ArrayList<String>();
	static List<String> _dSummary = new ArrayList<String>();
	
	static ArrayList<WeatherEntry> _forecast = new ArrayList<WeatherEntry>();
	
	Weather(String pURL) {
		_url = pURL;
	}
	
	/**
	* Sends a request to the REST API of Weather Underground's forecast.
	* The URL that is entered could request data based on the city, 
	* ZIP code and latitude/longitude. The data is being loaded in the
	* lists declared above as private attributes. 
	* 
	* @param	pURL	the URL of the JSON view: e.g. http://api.wunderground.com/api/257fbd10479ead52/forecast10day/q/19104.json
	*/
	public void loadData() throws IOException {
		
		// Connect to the URL
		URL url = new URL(_url);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();

		// get the root element
		JsonParser jp = new JsonParser();
		JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
		
		// get number of days
		JsonObject rootobj = root.getAsJsonObject();
		System.out.println(rootobj.toString());
		JsonArray daysJson = rootobj.get("forecast").getAsJsonObject().get("simpleforecast").getAsJsonObject().get("forecastday").getAsJsonArray();
		int n = daysJson.size();
		
		// iterate through each day of the forecast
		for (int i=0; i<n; i++) {
			
			WeatherEntry entry = new WeatherEntry();

			// get the date
			Long unixTime = daysJson.get(i).getAsJsonObject().get("date").getAsJsonObject().get("epoch").getAsLong();
			Date x = new Date(unixTime*1000);
			entry._dDate = x;
			
			// get the high temperature
			Float tempHigh = daysJson.get(i).getAsJsonObject().get("high").getAsJsonObject().get("fahrenheit").getAsFloat();
			entry._dTemperatureHigh = tempHigh;
			
			// get the low temperature
			Float tempLow = daysJson.get(i).getAsJsonObject().get("low").getAsJsonObject().get("fahrenheit").getAsFloat();
			entry._dTemperatureLow = tempLow;
			
			// get the image URL
			String imageURL = daysJson.get(i).getAsJsonObject().get("icon_url").getAsString();
			entry._dImageURL = imageURL;	
			
			// get the condition
			String conditions = daysJson.get(i).getAsJsonObject().get("conditions").getAsString();
			entry._dSummary = conditions;
			
			_forecast.add(entry);
		}
	}
	
	
	/**
	* Get the date for one specified day
	* 
	* @param	i	day number in the forecast.  The value "0" is today
	* @return		Date object of the specified day in the forecast
	*/
	public Date getDate(int i) {
		return _forecast.get(i)._dDate;
	}
	
	/**
	* Get the high temperature for one specified day
	* 
	* @param	i	day number in the forecast.  The value "0" is today
	* @return		High temperature as a Float
	*/
	public Float getTemperatureHigh(int i) {
		return _forecast.get(i)._dTemperatureHigh;
	}
	
	
	/**
	* Get the low temperature for one specified day
	* 
	* @param	i	day number in the forecast.  The value "0" is today
	* @return		Low temperature as a Float
	*/
	public Float getTemperatureLow(int i) {
		return _forecast.get(i)._dTemperatureLow;
	}	
	
	
	/**
	* Get the image URL for one specified day
	* 
	* @param	i	day number in the forecast.  The value "0" is today
	* @return		Get the image URL as a String
	*/
	public String getImageURL(int i) {
		return _forecast.get(i)._dImageURL;
	}
	
	/**
	* Get the summary for one specified day
	* 
	* @param	i	day number in the forecast.  The value "0" is today
	* @return		Get the summary as a String
	*/
	public String getSummary(int i) {
		return _forecast.get(i)._dSummary;
	}
	
	/**
	* Get the ArrayList of the WeatherEntries
	* 
	* @return		Get the whole forecast
	*/
	public ArrayList<WeatherEntry> getForecast() {
		return _forecast;
	}
	
}
