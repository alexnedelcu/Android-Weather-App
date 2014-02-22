package com.cs275.uweather;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherCustomAdapter extends ArrayAdapter<WeatherEntry> {

	Context context;
	int layoutResourceId;
	ArrayList<WeatherEntry> data = new ArrayList<WeatherEntry>();

	public WeatherCustomAdapter(Context context, int layoutResourceId, ArrayList<WeatherEntry> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@SuppressLint({ "NewApi", "SimpleDateFormat" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		WeatherHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new WeatherHolder();
			holder.imgIcon = (ImageView) row.findViewById(R.id.imgIcon);
			holder.txtDay = (TextView) row.findViewById(R.id.txtDay);
			holder.txtSummary = (TextView) row.findViewById(R.id.txtSummary);
			holder.txtTemperature = (TextView) row.findViewById(R.id.txtTemperature);
			row.setTag(holder);
		} else {
			holder = (WeatherHolder) row.getTag();
		}
		WeatherEntry w = data.get(position);

		
		// format the day field
		Date day = w._dDate;
		SimpleDateFormat sdf=new SimpleDateFormat("EEEE dd/yy");
		
		// download and show the icon
		new DownloadImageTask(holder.imgIcon).execute(w._dImageURL);
		
		System.out.println(w._dImageURL);
		holder.txtDay.setText(sdf.format(day));
		holder.txtSummary.setText(w._dSummary);
		holder.txtTemperature.setText(String.valueOf(w._dTemperatureHigh.intValue()) + " / " + String.valueOf(w._dTemperatureLow.intValue())+" F");

		return row;

	}
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) {
	        bmImage.setImageBitmap(result);
	    }
	}

	static class WeatherHolder {
		ImageView imgIcon;
		TextView txtDay;
		TextView txtSummary;
		TextView txtTemperature;
	}

}
