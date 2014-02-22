package com.cs275.uweather;

import java.io.IOException;
import java.util.ArrayList;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	final Handler mHandler = new Handler();
	private GeoLocationByIP _IPLocation = null;
	private Location _GPSLocation = null;
	private String _URLBase = "http://api.wunderground.com/api/257fbd10479ead52/forecast10day/q/";
	private ArrayList<WeatherEntry> _forecast = null;
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		
		loadData();
	}


	/*
	 * Determines if the phone has a active wireless connection or network connection.
	 * If there is a wireless connection, the geographical location by IP is determined.
	 * Otherwise, it attempts to use the GPS.
	 */
	public void loadData() {
		
		/*
		 * Get information about the connectivity
		 * 		If WIRELESS active -> use IP based location
		 * 		If INTERNET active -> use GPS
		 * 		If NO WIRELESS and NO INTERNET -> error 
		 */
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mMobileInternet = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (mWifi.isConnected()) {
			getLocationByIPAddress();
		} else if (!mMobileInternet.isConnected()) {
			TextView txtZipCode = (TextView) findViewById(R.id.txtZipCode);
			txtZipCode.setText("Cannot pull data. No connectivity.");
		} else {
			getLocationThroughGPS();
		}
	}
	
	/*
	 * renderWeather populates the UI with the geographical location data
	 * and the forecast
	 */
	public void renderWeather() {
		CharSequence zipcode = "";
		CharSequence latitude = "";
		CharSequence longitude = "";
		
		if (_GPSLocation == null) {
			zipcode = "IP-based zipcode: " + _IPLocation.getZipCode();
			latitude = "IP-based latitude: " +_IPLocation.getLatitude();
			longitude = "IP-based longitude: " + _IPLocation.getLongitude();
		}
		
		if (_IPLocation == null) {
			zipcode = "";
			latitude = "GPS-based latitude: " +_GPSLocation.getLatitude();
			longitude = "GPS-based longitude: " + _GPSLocation.getLongitude();
		}
		
		// update the zipcode
		TextView txtZipCode = (TextView) findViewById(R.id.txtZipCode);
		txtZipCode.setText( zipcode );
		
		// update the latitude
		TextView txtLatitude = (TextView) findViewById(R.id.txtLatitude);
		txtLatitude.setText( latitude );

		// update the longitude
		TextView txtLongitude = (TextView) findViewById(R.id.txtLongitude);
		txtLongitude.setText( longitude );
		
		// render the forecast in the list
		ListView lstForecast = (ListView) findViewById(R.id.lstForecast);
		WeatherCustomAdapter userAdapter = new WeatherCustomAdapter(MainActivity.this, R.layout.row, _forecast);
		lstForecast.setItemsCanFocus(false);
		lstForecast.setAdapter(userAdapter);
	}
	
	
	/*
	 * Gets the location coordinates using the GPS
	 * It calls getWUnderground when the coordinates are determined.
	 */
	public void getLocationThroughGPS () {

		LocationManager mgr = (LocationManager) getSystemService(LOCATION_SERVICE);

		LocationListener onLocationChange = new LocationListener() {


			public void onProviderDisabled(String provider) {
				// required for interface, not used
			}

			public void onProviderEnabled(String provider) {
				// required for interface, not used
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {
				// required for interface, not used
			}
			
			public void onLocationChanged(Location location) {
				_GPSLocation = location;
				_IPLocation = null;
				getWUndergroundWeather();
			}

		};

		mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3600000, 1000, onLocationChange);
	}
	
	/*
	 * Sets up a HTTP connection which gets the forecast of either the
	 * latitude/longitude coordinates or the zipcode, depending on the 
	 * method the location parameters were determined.
	 * freegeoip.net -> ZIP code
	 * GPs -> coordinates
	 */
	public void getWUndergroundWeather() {
		
		Thread t = new Thread() {
			public void run() {
				String url = _URLBase;
				if (_IPLocation == null && _GPSLocation != null) {
					url += Double.toString(_GPSLocation.getLatitude())+","+Double.toString(_GPSLocation.getLongitude())+".json";
				} else {
					if (_GPSLocation == null && _IPLocation != null) {
						url += _IPLocation.getZipCode() + ".json";
					} else {
						System.out.println("No location initialized");
					}
				}
				
				
				Weather forecast = new Weather(url);
				try {
					forecast.loadData();
					_forecast = forecast.getForecast();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				mHandler.post(new Runnable() {
		            @Override
		            public void run() {
		            	renderWeather();
		            }
		        });
			}
		};
		t.start();
		
	}
	
	
	/*
	 * It sets up a HTTP connection to freegeoip.net in order to receive
	 * details about the location based on the IP
	 * The HTTP connection runs in a separate thread from UI's
	 */
	protected void getLocationByIPAddress() {
		

		// Fire off a thread to do some work that we shouldn't do directly in
		Thread t = new Thread() {
			public void run() {
				try {
					_IPLocation = new GeoLocationByIP("http://freegeoip.net/json");
					_IPLocation.loadData();
					_GPSLocation = null;
					getWUndergroundWeather();
				} catch (IOException e) {}

			}
		};
		t.start();
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
