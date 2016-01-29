package com.elite.rasiyog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.elite.rasiyog.Utilities.Constants;
import com.elite.rasiyog.Utilities.Utils;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class HoroscopeDeatil extends Activity implements OnClickListener {
	
	ImageView back,image1;
	ProgressDialog progress;
	TextView name,header_txt,desc;
	String get_key,get_value,get_period,horoscope,sunsine;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_horoscope_deatil);
		find_id();
		get_value = getIntent().getStringExtra("key");
		get_period = getIntent().getStringExtra("key1");
		get_key = get_value.toUpperCase();
		setImage();
		if(Utils.isNetWorking(getApplicationContext()))
		{
			new horoscope_loader().execute();
		}
		else
		{
			Utils.show_Toast(getApplicationContext(), "Internet Not Available");
		}
	}

	private void setImage() {
		name.setText(get_key);
		if(get_value.equalsIgnoreCase("aquarius"))
		{
		image1.setBackgroundResource(R.drawable.acq1);
		}
		else if(get_value.equalsIgnoreCase("aries"))
		{
		image1.setBackgroundResource(R.drawable.ari1);
		}
		else if(get_value.equalsIgnoreCase("cancer"))
			
		{
			image1.setBackgroundResource(R.drawable.can1);
		}
		else if(get_value.equalsIgnoreCase("capricorn"))
		{
			image1.setBackgroundResource(R.drawable.cap1);
		}
		else if(get_value.equalsIgnoreCase("gemini"))
		{
			image1.setBackgroundResource(R.drawable.gem1);
			
		}
		else if(get_value.equalsIgnoreCase("leo"))
		{
			image1.setBackgroundResource(R.drawable.leo1);
		}
		else if(get_value.equalsIgnoreCase("libra"))
		{
			image1.setBackgroundResource(R.drawable.libra1);
		}
		else if(get_value.equalsIgnoreCase("pisces"))
		{
			image1.setBackgroundResource(R.drawable.pis1);
		}
		else if(get_value.equalsIgnoreCase("sagittarius"))
		{
			image1.setBackgroundResource(R.drawable.sag1);
			}
		else if(get_value.equalsIgnoreCase("scorpio"))
		{
			image1.setBackgroundResource(R.drawable.sco1);
		}
		else if(get_value.equalsIgnoreCase("taurus"))
		{
			image1.setBackgroundResource(R.drawable.tar1);
		}
		else if(get_value.equalsIgnoreCase("virgo"))
		{
			image1.setBackgroundResource(R.drawable.vir1);
		}
		
		
		if(get_period.equalsIgnoreCase("today"))
		{
			
			header_txt.setText("TODAY");
		}
		else if(get_period.equalsIgnoreCase("week"))
		{
			
			header_txt.setText("WEEK");
		}
		else if(get_period.equalsIgnoreCase("month"))
		{
			
			header_txt.setText("MONTH");
		}
			
	}

	private void find_id() {
		
		//image_view
		back = (ImageView) findViewById(R.id.back);
		image1= (ImageView) findViewById(R.id.image1);
		
		name = (TextView) findViewById(R.id.name);
		desc = (TextView) findViewById(R.id.desc);
		header_txt = (TextView) findViewById(R.id.header_txt);
		
		back.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.back:
			onBackPressed();
			break;

		default:
			break;
		}
		
	}
	// ===========================================================================//
	// ======================= Get The Horoscope loader =========================//
	// =========================================================================//
	
			class horoscope_loader extends AsyncTask<String, Void, String> {
				private String horoscope_url;
				private URI uri;
				private String result;

				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					try {
						progress = new ProgressDialog(HoroscopeDeatil.this);
						progress.setMessage("Loading...");
						progress.setIndeterminate(false);
						progress.setCancelable(false);
						progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						progress.show();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@SuppressLint("NewApi")
				@Override
				protected String doInBackground(String... params) {

					horoscope_url = Constants.GET_HOROSCOPE+"sunsign="+get_value+"&action="+get_period;

					try {
						uri = new URI(horoscope_url.replace(' ', '+'));

					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
					HttpClient httpclient = new DefaultHttpClient();
					HttpGet request = new HttpGet(uri);
					BasicResponseHandler handler = new BasicResponseHandler();
					try {
						result = httpclient.execute(request, handler);

						if (result.equals("")) {
						} else {
							JSONObject jsonA = new JSONObject(result);
							horoscope = jsonA.optString("horoscope");
							sunsine = jsonA.optString("sunsign");
						}
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return "true";
				}

				@Override
				protected void onPostExecute(String result) {
					super.onPostExecute(result);
					try {
						desc.setText(horoscope);
						progress.dismiss();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
			}
			
}
