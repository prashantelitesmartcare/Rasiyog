package com.elite.rasiyog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.elite.rasiyog.Utilities.Constants;
import com.elite.rasiyog.Utilities.Utils;

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
import java.util.ArrayList;
import java.util.Calendar;


public class SignupScreen extends Activity implements OnClickListener,
		OnItemSelectedListener {

	EditText name_edit, date_edit, phone_edit, email_edit, password_edit,
			place_of_edit;
	Spinner Place_edit, HRs_edit, min_edit;
	ImageView signup_btn, calnder;
	TextView signup;
	ArrayList<String> hours_list = new ArrayList<String>();
	ArrayList<String> min_list = new ArrayList<String>();
	ArrayList<String> gender_list = new ArrayList<String>();
	ArrayAdapter<String> hours_adapter, min_adpater, gender_adpter;
	String user_id, dob, place_of_birth, email, password, gender, birth_hour,
			birth_mint, date, name, image;
	String Calenderformat, get_name, get_phone, get_email, get_pass, get_date,
			get_hours, get_min, get_place_of_birth, get_gender;
	String error, result, phone_number;
	SharedPreferences myPref;
	String mobileid, token, loginId;
	String device_t = "1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_signup_screen);
		myPref = PreferenceManager
				.getDefaultSharedPreferences(SignupScreen.this);
		loginId = myPref.getString("USER_ID", "");
		mobileid = myPref.getString("DEVICE_ID", "");
		token = myPref.getString("TOKEN_ID", "");
		System.out.println("----token-id----" + token);
		System.out.println("---mobileid-id----" + mobileid);

		find_id();
		// set values on spinners
		setvalues();
	}

	private void find_id() {
		// Edittext
		name_edit = (EditText) findViewById(R.id.name_edit);
		date_edit = (EditText) findViewById(R.id.date_edit);
		phone_edit = (EditText) findViewById(R.id.phone_edit);
		email_edit = (EditText) findViewById(R.id.email_edit);
		place_of_edit = (EditText) findViewById(R.id.place_of_edit);
		password_edit = (EditText) findViewById(R.id.password_edit);
		// Spinner
		Place_edit = (Spinner) findViewById(R.id.Place_edit);
		HRs_edit = (Spinner) findViewById(R.id.HRs_edit);
		min_edit = (Spinner) findViewById(R.id.min_edit);
		// Imageview
		signup_btn = (ImageView) findViewById(R.id.signup_btn);
		calnder = (ImageView) findViewById(R.id.calnder);
		// Text View
		signup = (TextView) findViewById(R.id.signup);
		SpannableString content = new SpannableString("SIGN IN");
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		signup.setText(content);
		// set click listner on views
		date_edit.setOnClickListener(this);
		signup_btn.setOnClickListener(this);
		signup.setOnClickListener(this);
		HRs_edit.setOnItemSelectedListener(this);
		min_edit.setOnItemSelectedListener(this);
		Place_edit.setOnItemSelectedListener(this);
		calnder.setOnClickListener(this);
	}

	// /===============set values on the spinner here========================//
	private void setvalues() {
		// Add values for Hours Spinner
		for (int i = 0; i <= 23; i++) {

			if (i <= 9) {
				if (i == 0) {
					hours_list.add(String.valueOf("Select Hours"));
				}

				hours_list.add(String.valueOf("0" + i));

			} else {

				hours_list.add(String.valueOf(i));

			}
		}
		// Add values for Minutes Spinner
		for (int i = 0; i <= 59; i++) {

			if (i <= 9) {
				if (i == 0) {
					min_list.add(String.valueOf("Select Minutes"));
				}

				min_list.add(String.valueOf("0" + i));
			} else {
				min_list.add(String.valueOf(i));

			}
		}
		// Add values for genderSpinner
		gender_list.add("Select Gender");
		gender_list.add("Male");
		gender_list.add("Female");
		// set adpter on the Hours Spinner
		hours_adapter = new ArrayAdapter<String>(SignupScreen.this,
				R.layout.spinner_view11, R.id.text_main_seen, hours_list);
		hours_adapter.setDropDownViewResource(R.layout.spinner_view31);
		HRs_edit.setAdapter(hours_adapter);
		// set adpter on the Hours Spinner
		min_adpater = new ArrayAdapter<String>(SignupScreen.this,
				R.layout.spinner_view11, R.id.text_main_seen, min_list);
		min_adpater.setDropDownViewResource(R.layout.spinner_view31);
		min_edit.setAdapter(min_adpater);
		// set adpter on the Gender Spinner
		gender_adpter = new ArrayAdapter<String>(SignupScreen.this,
				R.layout.spinner_view11, R.id.text_main_seen, gender_list);
		gender_adpter.setDropDownViewResource(R.layout.spinner_view31);
		Place_edit.setAdapter(gender_adpter);
	}

	// =======================above function finish here
	// =================================//

	// ================ Click Listener function for view ===============//
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.calnder:
			DateID();
			break;
		case R.id.date_edit:
			DateID();
			break;

		case R.id.signup:
			Intent i = new Intent(SignupScreen.this, LoginScreen.class);
			startActivity(i);
			overridePendingTransition(0, 0);
			break;
		case R.id.signup_btn:

			get_name = name_edit.getText().toString().trim();
			get_email = email_edit.getText().toString().trim();
			get_date = date_edit.getText().toString().trim();
			get_pass = password_edit.getText().toString().trim();
			get_phone = phone_edit.getText().toString().trim();
			get_place_of_birth = place_of_edit.getText().toString().trim();

			if (get_name.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter Your Name");
				name_edit.requestFocus();
			}
			if (get_name.contains("[|?*<\":>+\\[\\]/']")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter Your Name");

			} else if (get_date.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(), "Please Enter Date");
				date_edit.requestFocus();
			} else if (get_gender.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Select Gender");
			} else if (get_hours.equalsIgnoreCase("Select Hours")) {
				Utils.show_Toast(getApplicationContext(), "Please Select Hours");
			} else if (get_min.equalsIgnoreCase("Select Minutes")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Select Minutes");
			} else if (get_place_of_birth.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter Place of Birth");
				place_of_edit.requestFocus();
			} else if (get_phone.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter Phone Number");
				phone_edit.requestFocus();
			} else if (get_phone.contains(" ")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter Correct   Phone Number");
			} else if (get_phone.length() < 10) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter 10 Digits Phone Number");
			} else if (get_email.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter Email Address");
			} else if (get_pass.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter Password");
				password_edit.requestFocus();
			} else if (get_pass.length() < 6) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter Minimum 6 Character Password");
			} else if (Utils.isNetWorking(getApplicationContext())) {
				new Signup_loader().execute();
			} else {
				Utils.show_Toast(getApplicationContext(),
						"Internet Not Available");
			}

			break;

		default:
			break;
		}
	}

	// =======================above function finish here
	// =================================//

	private void DateID() {
		// TODO Auto-generated method stub
		final Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		DatePickerDialog dpd = new DatePickerDialog(SignupScreen.this,
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						date_edit.setText(dayOfMonth + "-" + (monthOfYear + 1)
								+ "-" + year);
						// here we get the date selected from the date
						// picker
						Calenderformat = year + "-" + (monthOfYear + 1) + "-"
								+ dayOfMonth;
						System.out.println("===========DAte===="
								+ Calenderformat);
					}
				}, mYear, mMonth, mDay);
		dpd.show();
	}

	// ================ Click Listener function for Spinner ===============//
	@Override
	public void onItemSelected(AdapterView<?> f, View arg1, int arg2, long arg3) {

		switch (f.getId()) {
		case R.id.HRs_edit:
			get_hours = HRs_edit.getSelectedItem().toString();
			break;
		case R.id.min_edit:
			get_min = min_edit.getSelectedItem().toString();
			break;
		case R.id.Place_edit:
			String ff = Place_edit.getSelectedItem().toString();
			if (ff.equalsIgnoreCase("Select Gender")) {
				get_gender = "";
			} else {
				get_gender = Place_edit.getSelectedItem().toString();
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	// =======================above function finish here
	// =================================//

	// ==========================Signup Loader Start
	// Here==========================//
	class Signup_loader extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress;
		private String url;
		private String id;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(SignupScreen.this);
			progress.setMessage("Loading...");
			progress.setIndeterminate(false);
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
			url = Constants.SIGN_UP_URL;// +"email="+get_email+"&password="+get_pass;
			System.out.println("====url=pp==" + url);
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			try {
				MultipartEntity entity = new MultipartEntity();
				entity.addPart("username", new StringBody(get_name));
				entity.addPart("dob", new StringBody(get_date));
				entity.addPart("pob", new StringBody(get_place_of_birth));
				entity.addPart("phoneno", new StringBody(get_phone));
				entity.addPart("pass", new StringBody(get_pass));
				entity.addPart("email", new StringBody(get_email));
				entity.addPart("gender", new StringBody(get_gender));
				entity.addPart("hrs", new StringBody(get_hours));
				entity.addPart("min", new StringBody(get_min));
				/*entity.addPart("push_token", new StringBody(token));
				entity.addPart("device_id", new StringBody(mobileid));*/

				httppost.setEntity(entity);
				System.out.println("------------entity----------" + entity);
				HttpResponse response = httpclient.execute(httppost);
				System.out.println("------------response----------" + response);
				HttpEntity resEntity = response.getEntity();
				System.out.println("========resEntity==========="
						+ resEntity.toString());
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));
				String sResponse;
				StringBuilder s = new StringBuilder();
				while ((sResponse = reader.readLine()) != null) {
					s = s.append(sResponse);
				}
				System.out.println("Response: " + s);
				System.out.println("-----s----" + s);
				JSONObject hh = new JSONObject(s.toString());
				System.out.println("===final result==" + hh);
				id = hh.optString("success");
				user_id = hh.optString("id");
				dob = hh.optString("dob");
				place_of_birth = hh.optString("place_of_birth");
				email = hh.optString("email");
				name = hh.optString("name");
				phone_number = hh.optString("phone_number");
				image = hh.optString("image");
				password = hh.optString("password");
				gender = hh.optString("gender");
				birth_hour = hh.optString("birth_hour");
				birth_mint = hh.optString("birth_mint");
				date = hh.optString("date");
				error = hh.optString("error");
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onPostExecute(Void args) {
			try {
				if (id.equalsIgnoreCase("1")) {

					// Store value in sharedprefrence
					SharedPreferences.Editor editor11 = myPref.edit();
					editor11.putString("User_login", user_id);
					editor11.putString("dob", dob);
					editor11.putString("pob", place_of_birth);
					editor11.putString("email", email);
					editor11.putString("gender", gender);
					editor11.putString("hrs", birth_hour);
					editor11.putString("min", birth_mint);
					editor11.putString("date", date);
					editor11.putString("name", name);
					editor11.putString("image", image);
					editor11.putString("phn", phone_number);
					editor11.commit();
					Intent i = new Intent(SignupScreen.this, Dashboard.class);
					startActivity(i);
					overridePendingTransition(0, 0);
					progress.dismiss();
					Toast.makeText(getApplicationContext(),
							"Sign Up Successfully", Toast.LENGTH_SHORT).show();
				} else {
					progress.dismiss();
					Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT)
							.show();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	// ===============================Sign up Loader Finish
	// Here================//
}
