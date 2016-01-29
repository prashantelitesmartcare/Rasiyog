package com.elite.rasiyog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.elite.rasiyog.Utilities.Constants;
import com.elite.rasiyog.Utilities.Slidermenu;
import com.elite.rasiyog.Utilities.Utils;
import com.navdrawer.SimpleSideDrawer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Change_Password extends Activity implements OnClickListener {
	
	SimpleSideDrawer mNav;
	EditText old_pass_edit,new_pass_edit;
	ImageView save_btn;
	String get_old,get_new,error,id,loginId,get_active;
	SharedPreferences myPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_change__password);
		//get the login user id from Preference
		myPref = PreferenceManager.getDefaultSharedPreferences(Change_Password.this);
		loginId = myPref.getString("User_login", "");
		get_active = myPref.getString("active", "");

		//Find The View Id's From The Xml
		find_id();
		//Here we call the slider
		mNav = new SimpleSideDrawer(this);	
		Slidermenu side = new Slidermenu(mNav, Change_Password.this);
		side.setDrawer();
		findViewById(R.id.slider).setVisibility(View.GONE);
		mNav = new SimpleSideDrawer(this);	
		Slidermenu side1 = new Slidermenu(mNav, Change_Password.this);
		side1.setDrawer();
		try {
			get_active = getIntent().getStringExtra("status");
			
			if(get_active.equalsIgnoreCase("C"))
			{
				findViewById(R.id.change_password).setBackgroundResource(R.drawable.menu_highlit);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void find_id() {
		//Edit text
		old_pass_edit = (EditText) findViewById(R.id.old_pass_edit);
		new_pass_edit = (EditText) findViewById(R.id.new_pass_edit);
		//Imageview
		save_btn = (ImageView) findViewById(R.id.save_btn);
		//Apply click Listnere On views
		save_btn.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.save_btn:
			get_old = old_pass_edit.getText().toString();
			get_new = new_pass_edit.getText().toString();
			
			if(get_old.equalsIgnoreCase(""))
			{
				Utils.show_Toast(getApplicationContext(), "Please Enter Old Password");
				old_pass_edit.requestFocus();
			}
			else if(get_new.equalsIgnoreCase(""))
			{
				Utils.show_Toast(getApplicationContext(), "Please Enter New Password");
				new_pass_edit.requestFocus();
			}
			else if(Utils.isNetWorking(getApplicationContext()))
			{
				new Change_password().execute();
			}
			else
			{
				
			Utils.show_Toast(getApplicationContext(), "Internet Not Available");

			}

			
			break;

		default:
			break;
		}
		
	}
	
	//==================================================================//
    //====================Change passwor Loader Start===========================//
	//================================================================//
				class Change_password extends AsyncTask<Void, Void, Void> {
					private ProgressDialog progress;
					private String url;
					private String id;

					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						progress = new ProgressDialog(Change_Password.this);
						progress.setMessage("Loading...");
						progress.setIndeterminate(false);
						progress.setCancelable(false);
						progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);				
						progress.show();
					}
					
					protected Void doInBackground(Void... params) {
						try {
							upload();
						} catch (Exception e) {
							e.printStackTrace();
						}			
						
						return null;
					}
				
				public void upload() throws Exception {
						 url= Constants.CHANGE_PASSWORD;//+"email="+get_email+"&password="+get_pass;

					     HttpClient httpclient = new DefaultHttpClient();
				         HttpPost httppost = new HttpPost(url);
			        try {
			        	MultipartEntity entity = new MultipartEntity();
			        	entity.addPart("id", new StringBody(loginId));
			        	entity.addPart("oldpass", new StringBody(get_old));
		        		entity.addPart("newpass", new StringBody(get_new));
			        	httppost.setEntity(entity);

			        	HttpResponse response = httpclient.execute(httppost);

			        	HttpEntity resEntity = response.getEntity();

			        	BufferedReader reader = new BufferedReader(new InputStreamReader(
			            response.getEntity().getContent(), "UTF-8"));
			            String sResponse;
			            StringBuilder s = new StringBuilder();
			              while ((sResponse = reader.readLine()) != null) {
			                  s = s.append(sResponse);
			              }

			              JSONObject hh = new JSONObject(s.toString());

			              id = hh.optString("success");
			              error=hh.optString("error");
			           } catch (ClientProtocolException e) {
			        	e.printStackTrace();
			        } catch (IOException e) {
			        	e.printStackTrace();
			        }
				}
				
				
				@Override
				protected void onPostExecute(Void args) {		
					try {
						if(id.equalsIgnoreCase("1"))
						{
							
						Intent i = new Intent(getApplicationContext(),Dashboard.class);
						startActivity(i);
						overridePendingTransition(0, 0);
						Utils.show_Toast(getApplicationContext(), "Password Changed Successfully");
						progress.dismiss();
						finish();
						}
						else
						{
							progress.dismiss();
							Toast.makeText(getApplicationContext(), ""+error, Toast.LENGTH_SHORT).show();
						}
						} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				}	
				//============================================================================//
			    //====================Change password loader finish =========================//
				//===========================================================================//

	

}
