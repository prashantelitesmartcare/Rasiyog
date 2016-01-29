package com.elite.rasiyog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.elite.rasiyog.Utilities.Constants;
import com.elite.rasiyog.Utilities.Slidermenu;
import com.elite.rasiyog.Utilities.Utils;
import com.navdrawer.SimpleSideDrawer;

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

public class Dashboard extends Activity {
	
	SimpleSideDrawer mNav;
	String get_email,get_name,get_image,loginId,get_place,get_fb1,get_google_id,get_active;
	ImageView save,menu;
	SharedPreferences myPref;
	TextView About_txt;
	ProgressDialog progress;
	String description;
	Dialog dialog2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dashboard);
		//get the login user id from Preference
		try {
			myPref = PreferenceManager.getDefaultSharedPreferences(Dashboard.this);
			loginId = myPref.getString("User_login", "");
			get_place = myPref.getString("pob", "");
			get_fb1 = myPref.getString("fb_id", "");
			get_google_id = myPref.getString("g_id", "");
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		find_id();
		
		try {
			if(get_place.equalsIgnoreCase(""))
			{
				 dialog2 = new Dialog(Dashboard.this);
				   dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
				   dialog2.getWindow().setBackgroundDrawable(
				     new ColorDrawable(Color.TRANSPARENT));
				   dialog2.setContentView(R.layout.main_box);
				   dialog2.setCancelable(false);
				   dialog2.show();
				   save = (ImageView)dialog2.findViewById(R.id.save22);
				   save.setOnClickListener(new OnClickListener() {
				    
				    @Override
				    public void onClick(View v) {
				     Intent i = new Intent(Dashboard.this,Profile_Edit.class);
				     startActivity(i);
				     overridePendingTransition(0, 0);
				     
				    }
				   });
			}
			else
			{}
			
			if(get_fb1.equalsIgnoreCase("")&&get_google_id.equals(""))
			{
				
			}
			else
			{
				findViewById(R.id.change_password).setVisibility(View.GONE);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mNav = new SimpleSideDrawer(this);	
		Slidermenu side = new Slidermenu(mNav, Dashboard.this);
		side.setDrawer();
		findViewById(R.id.slider).setVisibility(View.GONE);
		mNav = new SimpleSideDrawer(this);	
		Slidermenu side1 = new Slidermenu(mNav, Dashboard.this);
		side1.setDrawer();
		
		try {
			get_active = getIntent().getStringExtra("status");
			
			if(get_active.equalsIgnoreCase("h"))
			{
				findViewById(R.id.Home).setBackgroundResource(R.drawable.menu_highlit);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(Utils.isNetWorking(getApplicationContext()))
		{
			new About_loader().execute();
		}
	}


	private void find_id() {
		//Text view
		About_txt = (TextView) findViewById(R.id.desc);	
		
	}
	
	// ===========================================================================//
		// ======================= Get About  loader here =========================//
		// =========================================================================//
				
		class About_loader extends AsyncTask<String, Void, String> {
				private String about_url;
				private URI uri;
				private String result;

							@Override
							protected void onPreExecute() {
								super.onPreExecute();
								try {
									progress = new ProgressDialog(Dashboard.this);
									progress.setMessage("Loading...");
									progress.setIndeterminate(false);
									progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
									progress.show();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							@SuppressLint("NewApi")
							@Override
							protected String doInBackground(String... params) {

								about_url = Constants.ABOUT_URL+"id=10";

								try {
									uri = new URI(about_url.replace(' ', '+'));

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
										JSONObject obj1 = jsonA.optJSONObject("about");
										description = obj1.optString("description");
									}
								} catch (ClientProtocolException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								} catch (JSONException e) {
									e.printStackTrace();
								}
								return "true";
							}

							@Override
							protected void onPostExecute(String result) {
								super.onPostExecute(result);
								try {
									About_txt.setText(description);
									progress.dismiss();
								} catch (Exception e) {
									e.printStackTrace();
								}
								}
						}
						//==================== above loader finished here =============================//


	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			

			 moveTaskToBack(true);
			return true;
		}
		return false;
	}

}
