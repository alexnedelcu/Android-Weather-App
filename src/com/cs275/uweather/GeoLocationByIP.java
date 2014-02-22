package com.cs275.uweather;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/*
 * GeoLocationByIP class will retrieve the location parameters
 * based on the IP address of the network's router
 * 
 * @author		Alexandru Nedelcu
 */
public class GeoLocationByIP {
	private static String _url = "http://freegeoip.net/json/";
	
	static private String _zipcode;
	static private String _latitude;
	static private String _longitude;
	
	GeoLocationByIP(String pURL) {
		_url = pURL;
	}
	
	/**
	* 
	* 
	* @param	pURL	the URL of the JSON view: e.g. http://freegeoip.net/json/
	*/
	public void loadData() throws IOException {
		
		// Connect to the URL
		URL url = new URL(_url);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();

		// get the root element
		JsonParser jp = new JsonParser();
		JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
		
		// get the ZIP code
		_zipcode = root.getAsJsonObject().get("zipcode").getAsString();
		
		// get the latitude		
		_latitude = root.getAsJsonObject().get("latitude").getAsString();
		
		// get the longitude
		_longitude = root.getAsJsonObject().get("longitude").getAsString();
		
		
	}
	

	/**
	* Get the ZIP code
	* 
	* @return		get the ZIP code as String
	*/
	public String getZipCode() {
		return _zipcode;
	}

	/**
	* Get the latitude
	* 
	* @return		get the latitude as String
	*/
	public String getLatitude() {
		return _latitude;
	}

	/**
	* Get the longitude
	* 
	* @return		get the longitude as String
	*/
	public String getLongitude() {
		return _longitude;
	}
	
//	
//	public static void main(String [] argv) throws IOException {
//		loadData(_url);
//	}
	
}
