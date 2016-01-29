package com.elite.rasiyog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

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


public class ForgotScreen extends Activity implements OnClickListener {
	
	EditText email_edit;
	ImageView sumit_btn,back;
	String success,error,get_email,message;
	ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_forgot_screen);
		
		find_id();
	}

	private void find_id() {
		// Edit Text
		email_edit = (EditText) findViewById(R.id.email_edit);
		//Image View
		sumit_btn = (ImageView) findViewById(R.id.sumit_btn);
		back = (ImageView) findViewById(R.id.back);
		//Apply click listener
		sumit_btn.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sumit_btn:
			get_email = email_edit.getText().toString();
			
			if(get_email.equalsIgnoreCase(""))
			{
				Utils.show_Toast(getApplicationContext(), "Please Enter Email Address");
				email_edit.requestFocus();
			}
			else if(Utils.isNetWorking(getApplicationContext()))
			{
				new Forgot1_loader().execute();
			}
			else
			{
				Utils.show_Toast(getApplicationContext(), "Internet Not Available");	
			}
			break;
		case R.id.back:
			Intent i = new Intent(ForgotScreen.this,LoginScreen.class);
			startActivity(i);
			overridePendingTransition(0, 0);
			break;


		default:
			break;
		}
		
	}

	// ===========================================================================//
	// ======================= Get About  loader here =========================//
	// =========================================================================//
			
	class Forgot1_loader extends AsyncTask<String, Void, String> {
			private String about_url;
			private URI uri;
			private String result;

		@Override
		protected void onPreExecute() {
		super.onPreExecute();
		try {
		progress = new ProgressDialog(ForgotScreen.this);
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

		about_url = Constants.FORGOT_PASSWORD+"email="+get_email;

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
					success = jsonA.optString("success");
					message = jsonA.optString("message");
					error = jsonA.optString("error");
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
				if(success.equalsIgnoreCase("1"))
				{
					Intent intent = new Intent(getApplicationContext(),LoginScreen.class);
					startActivity(intent);
					overridePendingTransition(0, 0);
					finish();
					Utils.show_Toast(getApplicationContext(), ""+message);
					progress.dismiss();	
				}
				else
				{
					Utils.show_Toast(getApplicationContext(), ""+error);
					progress.dismiss();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
							}
					}
	//==================== above loader finished here =============================//


}
