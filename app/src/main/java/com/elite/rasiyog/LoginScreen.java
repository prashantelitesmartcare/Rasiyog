package com.elite.rasiyog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.method.PasswordTransformationMethod;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elite.rasiyog.Utilities.Constants;
import com.elite.rasiyog.Utilities.Utils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class LoginScreen extends Activity implements OnClickListener{

	EditText email_edit, password_edit;
	ImageView login_btn, facebok_btn;
	ImageView googleplus;
	TextView signup, forgot_text;
	String get_email, get_pass, error;
	ProgressDialog progress;
	SharedPreferences myPref;
	String loginId, pushToken, DeviceID;
	String mobileid, token;
	String g_id, google_id, google_email, google_name, acess_token2, fb_id,
			user_id, dob, place_of_birth, email, password, gender, birth_hour,
			birth_mint, date, name, image, phone_number, get_fb_email,
			get_fb_id="", get_fb_first_nmae, get_fb_last_name, get_fb_image,
			get_fb_gender;
	String device_t = "1";
	private CallbackManager callbackManager;
	// ==Facebook Account====///
	private static final String APP_ID = "430812557116507";

	private String access_token;
	// ============ Google Accounts======//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		FacebookSdk.sdkInitialize(LoginScreen.this);
		callbackManager = CallbackManager.Factory.create();
		LoginManager.getInstance().logOut();
		//getFbKeyHash("com.elite.rasiyog");
		FacebookSdk.sdkInitialize(LoginScreen.this);
		callbackManager = CallbackManager.Factory.create();
		setContentView(R.layout.activity_login_screen);
		myPref = PreferenceManager.getDefaultSharedPreferences(LoginScreen.this);


		loginId = myPref.getString("USER_ID", "");
		mobileid = myPref.getString("DEVICE_ID", "");
		token = myPref.getString("TOKEN_ID", "");
		System.out.println("----token-id----" + token);
		System.out.println("---mobileid-id----" + mobileid);

		find_id();

		try {
			access_token = myPref.getString("access_token", "");
			if(access_token.equalsIgnoreCase(""))
			{}
			else
			{
				new getFacebookData().execute();
			}

			System.out.println("====access_token======" + access_token);
		} catch (Exception e1) {

			e1.printStackTrace();
		}
		facebooklogin();

	}

	private void facebooklogin() {

		LoginManager.getInstance().registerCallback(callbackManager,
				new FacebookCallback<LoginResult>() {
					@Override
					public void onSuccess(LoginResult loginResult) {
						Log.e("access_token1 fom prefrence", "" + access_token);
						AccessToken token = AccessToken.getCurrentAccessToken();
						Log.e("access_token1 in striggggg==------", "" + access_token);
						SharedPreferences.Editor editor = myPref.edit();
						editor.putString("access_token", token.getToken().toString());
						editor.commit();

						access_token = myPref.getString("access_token","");

						if(access_token.equalsIgnoreCase(""))
						{}
						else

						{
							new getFacebookData().execute();
						}



					}

					@Override
					public void onCancel() {
						Toast.makeText(LoginScreen.this, "Login Cancel", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onError(FacebookException exception) {
						Toast.makeText(LoginScreen.this, exception.getMessage(), Toast.LENGTH_LONG).show();
					}
				});


	}

	private void find_id() {

		email_edit = (EditText) findViewById(R.id.email_edit);
		password_edit = (EditText) findViewById(R.id.password_edit);
		password_edit.setTypeface(Typeface.DEFAULT);
		password_edit.setTransformationMethod(new PasswordTransformationMethod());
		// Imageview
		login_btn = (ImageView) findViewById(R.id.login_btn);
		facebok_btn = (ImageView) findViewById(R.id.facebok_btn);

		// Textview
		signup = (TextView) findViewById(R.id.signup);
		forgot_text = (TextView) findViewById(R.id.forgot_text);

		SpannableString content = new SpannableString("SIGN UP");
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		signup.setText(content);

		SpannableString content1 = new SpannableString("FORGOT PASSWORD?");
		content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
		forgot_text.setText(content1);

		login_btn.setOnClickListener(this);
		signup.setOnClickListener(this);
		facebok_btn.setOnClickListener(this);
		forgot_text.setOnClickListener(this);

	}



	@Override
	protected void onPause() {
		super.onPause();

	}

	// ==================== On click function =======================//
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.login_btn:
			get_email = email_edit.getText().toString();
			get_pass = password_edit.getText().toString();

			if (get_email.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter Email Address");
				email_edit.requestFocus();
			} else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(get_email)
					.matches()) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter Valid Email Address");
			} else if (get_pass.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter Password");
				password_edit.requestFocus();
			} else if (Utils.isNetWorking(getApplicationContext())) {
				new Login_loader().execute();
			} else {
				Utils.show_Toast(getApplicationContext(),
						"Internet Not Avialable");

			}
			break;

		case R.id.signup:
			Intent i = new Intent(LoginScreen.this, SignupScreen.class);
			startActivity(i);
			overridePendingTransition(0, 0);
			break;



		case R.id.forgot_text:
			Intent i1 = new Intent(LoginScreen.this, ForgotScreen.class);
			startActivity(i1);
			overridePendingTransition(0, 0);
			break;

		case R.id.facebok_btn:

			if (access_token.equalsIgnoreCase("")) {
				if (Utils.isNetWorking(getApplicationContext())) {
					LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));


				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							LoginScreen.this);
					builder.setMessage("No internet connection");
					builder.setCancelable(false);
					builder.setTitle("Warning!!!!!");
					builder.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

								}
							});
					AlertDialog alert = builder.create();
					alert.show();
				}
			} else {
				new fbLogin_loader().execute();
			}
			break;

		default:
			break;
		}
	}


	// ==========================Facebook login function
	// finish============================//

	// =========================== Get the User profile details fron
	// Facebook===================///
	class getFacebookData extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(LoginScreen.this);
			progress.setMessage("Loading...");
			progress.setIndeterminate(false);
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.show();
		}

		protected Void doInBackground(Void... params) {
			try {
				fbUserProfile();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		public void fbUserProfile() throws Exception {

			try {
				// access_token = myPref.getString("access_token", null);
				JSONObject jsonObj = null;
				HttpParams httpParameters = new BasicHttpParams();
				HttpConnectionParams
						.setConnectionTimeout(httpParameters, 50000);
				HttpConnectionParams.setSoTimeout(httpParameters, 50000);

				HttpClient client = new DefaultHttpClient(httpParameters);

				String requestURL = "https://graph.facebook.com/me?fields=id,first_name,email&access_token="
						+ access_token;
				Log.i("Request URL:", "---" + requestURL);
				HttpGet request = new HttpGet(requestURL);

				HttpResponse response = client.execute(request);
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				String webServiceInfo = "";

				while ((webServiceInfo = rd.readLine()) != null) {
					Log.i("Service Response:", "-ttt--" + webServiceInfo);
					jsonObj = new JSONObject(webServiceInfo);
					get_fb_email = jsonObj.optString("email");
					get_fb_first_nmae = jsonObj.optString("first_name");
					get_fb_id = jsonObj.optString("id");
				}

			} catch (Exception e) {
			}
		}

		@Override
		protected void onPostExecute(Void args) {
			try {
				System.out.println("==========id=========" + get_fb_id);
				System.out.println("==========email=========" + get_fb_email);
				System.out.println("==========first========="+ get_fb_first_nmae);


				SharedPreferences.Editor editor = myPref.edit();
				editor.putString("fb_id", get_fb_id);
				editor.putString("fb_email", get_fb_email);
				editor.putString("fb_name", get_fb_first_nmae);
				editor.commit();
				progress.dismiss();
				new fbLogin_loader().execute();
			} catch (Exception e) {
				progress.dismiss();
				e.printStackTrace();
			}
		}
	}

	// ===========================Finish Here =================///


	// ==================================================================//
	// ====================Login with facebook Loader=====================//
	// ================================================================//
	class fbLogin_loader extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress;
		private String url1;
		private String id;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(LoginScreen.this);
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
			url1 = Constants.FB_LOGIN;
			System.out.println("====url=pp==" + url1);
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url1);

			try {
				MultipartEntity entity = new MultipartEntity();
				entity.addPart("fb_id", new StringBody(get_fb_id));
				entity.addPart("name", new StringBody(get_fb_first_nmae));
				entity.addPart("email", new StringBody(get_fb_email));
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
				phone_number = hh.optString("phone_number");
				name = hh.optString("name");
				image = hh.optString("image");
				password = hh.optString("password");
				gender = hh.optString("gender");
				fb_id = hh.optString("fb_id");
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
					editor11.putString("hrs", birth_hour);
					editor11.putString("min", birth_mint);
					editor11.putString("name", name);
					editor11.putString("phn", phone_number);
					editor11.putString("image", image);
					editor11.putString("fb_id", fb_id);
					editor11.putString("gender", gender);
					editor11.putString("g_id", "");
					editor11.putString("date", date);

					editor11.commit();

					Intent i = new Intent(getApplicationContext(),
							Dashboard.class);
					startActivity(i);
					overridePendingTransition(0, 0);
					Utils.show_Toast(getApplicationContext(),
							"Login Successfully");
					progress.dismiss();
					finish();
				} else {
					progress.dismiss();
					Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT)
							.show();
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

/*	// ==================================================================//
	// ====================Login with Google+ Loader ===============///
	// ================================================================//
	class Google_Login_loader extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress;
		private String url1;
		private String id;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(LoginScreen.this);
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
			url1 = Constants.G_LOGIN;// +"email="+get_email+"&password="+get_pass;
			System.out.println("====url=pp==" + url1);
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url1);

			try {
				MultipartEntity entity = new MultipartEntity();
				entity.addPart("google_id", new StringBody(google_id));
				entity.addPart("name", new StringBody(google_name));
				entity.addPart("email", new StringBody(google_email));
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
				phone_number = hh.optString("phone_number");
				name = hh.optString("name");
				image = hh.optString("image");
				password = hh.optString("password");
				gender = hh.optString("gender");
				g_id = hh.optString("google_id");
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
					editor11.putString("hrs", birth_hour);
					editor11.putString("min", birth_mint);
					editor11.putString("name", name);
					editor11.putString("phn", phone_number);
					editor11.putString("image", image);
					editor11.putString("g_id", google_id);
					editor11.putString("fb_id", "");
					editor11.putString("gender", gender);
					editor11.putString("date", date);

					editor11.commit();

					Intent i = new Intent(getApplicationContext(),
							Dashboard.class);
					startActivity(i);
					overridePendingTransition(0, 0);
					Utils.show_Toast(getApplicationContext(),
							"Login Successfully");
					progress.dismiss();
					finish();
				} else {
					progress.dismiss();
					Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT)
							.show();
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}*/

	// ==================================================================//
	// ====================Login Loader Start===========================//
	// ================================================================//
	class Login_loader extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress;
		private String url;
		private String id;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(LoginScreen.this);
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
			url = Constants.LOGIN_URL;// +"email="+get_email+"&password="+get_pass;
			System.out.println("====url=pp==" + url);
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			System.out.println("=-email=====" + get_email);
			System.out.println("=-passs=====" + get_pass);
			try {
				MultipartEntity entity = new MultipartEntity();
				entity.addPart("email", new StringBody(get_email));
				entity.addPart("password", new StringBody(get_pass));
				/*entity.addPart("push_token", new StringBody(token));
				entity.addPart("device_id", new StringBody(mobileid));*/
				entity.addPart("device_type", new StringBody(device_t));
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
				phone_number = hh.optString("phone_number");
				name = hh.optString("name");
				image = hh.optString("image");
				password = hh.optString("password");
				gender = hh.optString("gender");
				fb_id = hh.optString("fb_id");
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
					editor11.putString("fb_id", fb_id);
					editor11.putString("phn", phone_number);
					editor11.putString("active", "");
					editor11.commit();

					Intent i = new Intent(getApplicationContext(),
							Dashboard.class);
					startActivity(i);
					overridePendingTransition(0, 0);
					Utils.show_Toast(getApplicationContext(),
							"Login Successfully");
					progress.dismiss();
					finish();
				} else {
					progress.dismiss();
					Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT)
							.show();
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	// ==================================================================//
	// ====================Login loader finish =========================//
	// ================================================================//

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(true);
			return true;
		}
		return false;
	}

	public void getFbKeyHash(String packageName) {

		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					packageName,
					PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("YourKeyHash :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
				System.out.println("YourKeyHash: "+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (PackageManager.NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}

	}

	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent i) {
		callbackManager.onActivityResult(reqCode, resCode, i);
	}

}
