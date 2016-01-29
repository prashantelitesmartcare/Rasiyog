package com.elite.rasiyog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.elite.rasiyog.Utilities.Constants;
import com.elite.rasiyog.Utilities.Slidermenu;
import com.elite.rasiyog.Utilities.Utils;
import com.navdrawer.SimpleSideDrawer;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class QuestionScreen extends Activity implements OnClickListener,
		OnItemSelectedListener {

	SimpleSideDrawer mNav;
	Spinner gen_edit, HRs_edit, min_edit;
	EditText place_of_edit, name_edit, email_edit, phone_edit1, date_of_edit1,
			question_edit, attachments_g, attachments_g2, attachments_g3,
			attachments_g4;
	TextView About_txt, Change_txt;
	ImageView attach_btn_g, attach_btn_g2, attach_btn_g3, attach_btn_g4,
			submit,calnder1;
	ProgressDialog progress;
	String description, get_full_name, get_gender, get_date, get_place,
			get_hrs, get_min, get_email, get_phone, camera_key, get_question,
			loginId,get_active;
	public static String image_str = "", image_str2 = "", image_str3 = "",
			image_str4 = "";
	Uri mUri;
	
	
	private Bitmap bitmap11;//,bitmap12;//,bitmap13,bitmap14;
	 String bsae64_1="",bsae64_2="",bsae64_3="",bsae64_4="";
	
	SharedPreferences myPref;
	ArrayList<String> gender_list = new ArrayList<String>();
	ArrayList<String> hours_list = new ArrayList<String>();
	ArrayList<String> min_list = new ArrayList<String>();
	ArrayAdapter<String> hours_adapter, min_adpater, gender_adpter;
	RelativeLayout attachment1, attachment2, attachment3, attachment4;
	String product_name,paypal_id ;
	// =============Paypal values

	private static final String TAG = "paymentExample";
	/**
	 * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
	 * 
	 * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test
	 * credentials from https://developer.paypal.com
	 * 
	 * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
	 * without communicating to PayPal's servers.
	 */
	private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

	// note that these credentials will differ between live & sandbox
	// environments.
	//private static final String CONFIG_CLIENT_ID = "AcoLgRZ988nwmmvVekWvMQVRWjZCsjcRrAziAN9lqaIv_MvH8-jhnSZFp1wxxyDET2f31GM-cmGWbEzm";
	private static final String CONFIG_CLIENT_ID = "ARdmUzW0WuLBbaSYV7lCEkiZl2GtOMF7_fjaZ9SEqtR5EzvqK8GX-VhnE9RLJbaAoFi1TpuZ-uGV8-HT";

	private static final int REQUEST_CODE_PAYMENT = 13;
	private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
	private static final int REQUEST_CODE_PROFILE_SHARING = 3;

	private static PayPalConfiguration config = new PayPalConfiguration()
			.environment(CONFIG_ENVIRONMENT)
			.clientId(CONFIG_CLIENT_ID)
			// The following are only used in PayPalFuturePaymentActivity.
			.merchantName("Example Merchant")
			.merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
			.merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

	String error, success;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_question_screen);
		// Getting the value from Preferences
		myPref = PreferenceManager
				.getDefaultSharedPreferences(QuestionScreen.this);
		loginId = myPref.getString("User_login", "");
		get_full_name = myPref.getString("name", "");
		get_gender = myPref.getString("gender", "");
		get_date = myPref.getString("dob", "");
		get_place = myPref.getString("pob", "");
		get_hrs = myPref.getString("hrs", "");
		get_min = myPref.getString("min", "");
		get_email = myPref.getString("email", "");
		get_phone = myPref.getString("phn", "");
		get_active = myPref.getString("active", "");

		find_id();

		SpannableString content = new SpannableString("RESET");
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		Change_txt.setText(content);
		setvalues();
		set_old_value();

		mNav = new SimpleSideDrawer(this);
		Slidermenu side = new Slidermenu(mNav, QuestionScreen.this);
		side.setDrawer();
		findViewById(R.id.slider).setVisibility(View.GONE);
		mNav = new SimpleSideDrawer(this);
		Slidermenu side1 = new Slidermenu(mNav, QuestionScreen.this);
		side1.setDrawer();
		try {
			get_active = getIntent().getStringExtra("status");
			
			if(get_active.equalsIgnoreCase("Q"))
			{
				findViewById(R.id.question).setBackgroundResource(R.drawable.menu_highlit);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (Utils.isNetWorking(getApplicationContext())) {
			new About_loader().execute();
		}

		Intent intent = new Intent(this, PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
		startService(intent);
	}

	private void find_id() {
		// Text view
		About_txt = (TextView) findViewById(R.id.About_txt);
		Change_txt = (TextView) findViewById(R.id.Change_txt);
		// Spinners
		gen_edit = (Spinner) findViewById(R.id.gen_edit);
		HRs_edit = (Spinner) findViewById(R.id.HRs_edit);
		min_edit = (Spinner) findViewById(R.id.min_edit);
		// Image view
		attach_btn_g = (ImageView) findViewById(R.id.attach_btn_g);
		attach_btn_g2 = (ImageView) findViewById(R.id.attach_btn_g2);
		attach_btn_g3 = (ImageView) findViewById(R.id.attach_btn_g3);
		attach_btn_g4 = (ImageView) findViewById(R.id.attach_btn_g4);
		calnder1 = (ImageView) findViewById(R.id.calnder1);
		submit = (ImageView) findViewById(R.id.submit);
		// Edit text
		place_of_edit = (EditText) findViewById(R.id.Place_edit);
		name_edit = (EditText) findViewById(R.id.name_edit);
		email_edit = (EditText) findViewById(R.id.email_edit);
		phone_edit1 = (EditText) findViewById(R.id.phone_edit1);
		date_of_edit1 = (EditText) findViewById(R.id.date_of_edit1);
		question_edit = (EditText) findViewById(R.id.question_edit);
		attachments_g = (EditText) findViewById(R.id.attachments_g);
		attachments_g2 = (EditText) findViewById(R.id.attachments_g2);
		attachments_g3 = (EditText) findViewById(R.id.attachments_g3);
		attachments_g4 = (EditText) findViewById(R.id.attachments_g4);
		// Relativelayouts
		attachment1 = (RelativeLayout) findViewById(R.id.attachment1);
		attachment2 = (RelativeLayout) findViewById(R.id.attachment2);
		attachment3 = (RelativeLayout) findViewById(R.id.attachment3);
		attachment4 = (RelativeLayout) findViewById(R.id.attachment4);
		// Set visibility of views
		attachment1.setVisibility(View.VISIBLE);
		attachment2.setVisibility(View.GONE);
		attachment3.setVisibility(View.GONE);
		attachment4.setVisibility(View.GONE);

		attach_btn_g.setOnClickListener(this);
		attach_btn_g2.setOnClickListener(this);
		attach_btn_g3.setOnClickListener(this);
		attach_btn_g4.setOnClickListener(this);
		date_of_edit1.setOnClickListener(this);
		submit.setOnClickListener(this);
		Change_txt.setOnClickListener(this);

		gen_edit.setOnItemSelectedListener(this);
		HRs_edit.setOnItemSelectedListener(this);
		min_edit.setOnItemSelectedListener(this);
		calnder1.setOnClickListener(this);
	}

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
		hours_adapter = new ArrayAdapter<String>(QuestionScreen.this,
				R.layout.spinner_view, R.id.text_main_seen, hours_list);
		hours_adapter.setDropDownViewResource(R.layout.spinner_view3);
		HRs_edit.setAdapter(hours_adapter);
		// set adpter on the Hours Spinner
		min_adpater = new ArrayAdapter<String>(QuestionScreen.this,
				R.layout.spinner_view, R.id.text_main_seen, min_list);
		min_adpater.setDropDownViewResource(R.layout.spinner_view3);
		min_edit.setAdapter(min_adpater);
		// set adpter on the Gender Spinner
		gender_adpter = new ArrayAdapter<String>(QuestionScreen.this,
				R.layout.spinner_view, R.id.text_main_seen, gender_list);
		gender_adpter.setDropDownViewResource(R.layout.spinner_view3);
		gen_edit.setAdapter(gender_adpter);
	}

	private void set_old_value() {
		name_edit.setText(get_full_name);
		date_of_edit1.setText(get_date);
		email_edit.setText(get_email);
		place_of_edit.setText(get_place);
		phone_edit1.setText(get_phone);

		// Set values in Hours Spinner
		for (int i = 0; i < hours_adapter.getCount(); i++) {
			if (get_hrs.trim().equals(hours_adapter.getItem(i).toString())) {
				HRs_edit.setSelection(i);
				break;
			}
		}
		// Set values in Min Spinner
		for (int i = 0; i < min_adpater.getCount(); i++) {
			if (get_min.trim().equals(min_adpater.getItem(i).toString())) {
				min_edit.setSelection(i);
				break;
			}
		}
		// Set values in Gender Spinner
		for (int i = 0; i < gender_adpter.getCount(); i++) {
			if (get_gender.trim().equals(gender_adpter.getItem(i).toString())) {
				gen_edit.setSelection(i);
				break;
			}
		}

	}

	// ===========================================================================//
	// ======================= Get About loader here =========================//
	// =========================================================================//

	class About_loader extends AsyncTask<String, Void, String> {
		private String about_url;
		private URI uri;
		private String result;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				progress = new ProgressDialog(QuestionScreen.this);
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

			about_url = Constants.ABOUT_URL + "id=12";
			System.out.println("--about_url--" + about_url);
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
				System.out.println("--result--" + result);
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

	// ==================== above loader finished here
	// =============================//

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.date_of_edit1:
			DateID();
			break;
		case R.id.calnder1:
			DateID();
			break;
		case R.id.attach_btn_g:
			openCamera();
			camera_key = "1";
			break;

		case R.id.attach_btn_g2:
			openCamera();
			camera_key = "2";
			break;

		case R.id.attach_btn_g3:
			openCamera();
			camera_key = "3";
			break;

		case R.id.attach_btn_g4:
			openCamera();
			camera_key = "4";
			break;

		case R.id.Change_txt:
			name_edit.setText("");
			email_edit.setText("");
			phone_edit1.setText("");
			date_of_edit1.setText("");
			place_of_edit.setText("");
			question_edit.setText("");
			HRs_edit.setSelection(0);
			min_edit.setSelection(0);
			gen_edit.setSelection(0);
			attachments_g.setText("");
			bsae64_1="";
			bsae64_2="";
			bsae64_3="";
			bsae64_4="";
			image_str = "";
			image_str2 = "";
			image_str3 = "";
			image_str4 = "";
			mUri = null;
			break;

		case R.id.submit:
			get_full_name = name_edit.getText().toString();
			get_email = email_edit.getText().toString();
			get_phone = phone_edit1.getText().toString();
			get_date = date_of_edit1.getText().toString();
			get_place = place_of_edit.getText().toString();
			get_question = question_edit.getText().toString();

			if (get_full_name.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter Your Name");
				name_edit.requestFocus();

			} else if (get_email.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter Email Address");
				email_edit.requestFocus();

			} else if (get_phone.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter Phone Number");
				phone_edit1.requestFocus();

			}	else if (get_phone.contains(" ")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter Correct   Phone Number");
			} else if (get_phone.length() < 10) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter 10 Digits Phone Number");
			} else if (get_gender.equalsIgnoreCase("Select Gender")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Select Gender");
			} else if (get_date.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(), "Please Select Date");
				date_of_edit1.requestFocus();

			} else if (get_place.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter Birth Place");
				place_of_edit.requestFocus();
			}else if (get_hrs.equalsIgnoreCase("Select Hours")) {
				Utils.show_Toast(getApplicationContext(), "Please Select Hours");
			} else if (get_min.equalsIgnoreCase("Select Minutes")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Select Minutes");
			}
			else if (get_question.equalsIgnoreCase("")) {
			Utils.show_Toast(getApplicationContext(),
					"Please Enter Question");
				question_edit.requestFocus();
		}
			else if (image_str.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Add At Least One Attachment");
			} else {
				new Question_making().execute();
				/*
				 * PAYMENT_INTENT_SALE will cause the payment to complete
				 * immediately. Change PAYMENT_INTENT_SALE to -
				 * PAYMENT_INTENT_AUTHORIZE to only authorize payment and
				 * capture funds later. - PAYMENT_INTENT_ORDER to create a
				 * payment for authorization and capture later via calls from
				 * your server.
				 * 
				 * Also, to include additional payment details and an item list,
				 * see getStuffToBuy() below.
				 *//*
				PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

				*//*
				 * See getStuffToBuy(..) for examples of some available payment
				 * options.
				 *//*

				Intent intent = new Intent(QuestionScreen.this,
						PaymentActivity.class);

				// send the same configuration for restart resiliency
				intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,
						config);

				intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

				startActivityForResult(intent, REQUEST_CODE_PAYMENT);*/
			}/*
			 * if(Utils.isNetWorking(getApplicationContext())) { new
			 * Question_making().execute(); } else {
			 * Utils.show_Toast(getApplicationContext(),
			 * "Please Enter Phone Number"); }
			 */
			break;

		default:
			break;
		}

	}

	private void DateID() {
		// TODO Auto-generated method stub
		final Calendar c = Calendar.getInstance();
		// int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		DatePickerDialog dpd = new DatePickerDialog(QuestionScreen.this,
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						date_of_edit1.setText(dayOfMonth + "-"
								+ (monthOfYear + 1) + "-" + year);
						// here we get the date selected from the date
						// picker
						get_date = dayOfMonth + "-" + (monthOfYear + 1)
								+ "-" + year;
						System.out
								.println("===========DAte====" + get_date);
					}
				}, 1945, mMonth, mDay);
		dpd.show();
	}

	// =======================================================================================//
	// ============== Function for getting image from gallery and camera
	// ==================//
	// =======================================================================================//
	private void openCamera() {
		runOnUiThread(new Runnable() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				AlertDialog alertDialog = new AlertDialog.Builder(
						QuestionScreen.this).create();
				alertDialog.setTitle("Select resource");
				alertDialog.setCancelable(true);
				alertDialog.setButton("Gallery",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(
										Intent.ACTION_PICK,
										MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
								startActivityForResult(Intent.createChooser(
										intent, "Complete action using"), 1234);
							}
						});
				alertDialog.setButton2("Camera",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								mUri = Uri.fromFile(new File(Environment
										.getExternalStorageDirectory(), "pic_"
										+ String.valueOf(System
												.currentTimeMillis()) + ".jpg"));
								intent.putExtra(
										MediaStore.EXTRA_OUTPUT,
										mUri);
								startActivityForResult(intent, 1);
							}
						});
				alertDialog.show();

			}
		});

	}

	@SuppressWarnings("deprecation")
	public String getRealPathFromURI(Uri contentUri) {
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = managedQuery(contentUri, proj, null, null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			System.out.println("column_index of selected image is:"
					+ column_index);
			cursor.moveToFirst();
			System.out.println("selected image path is:"
					+ cursor.getString(column_index));
			return cursor.getString(column_index);
		} catch (Exception e) {
			return contentUri.getPath();
		}
	}

	Bitmap photo;

	@SuppressWarnings("unused")
	private Object bitmap2;

	@SuppressWarnings("unused")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {

			System.out.print("==============get json======" + requestCode);
			if (requestCode == 1234) {
				
				if(resultCode==RESULT_OK)
				{
					System.out.println("====camera keyy=====" + camera_key);
					Bitmap bitmap2 = null;
					try {
						Uri selectedImageUri = null;
						selectedImageUri = data.getData();
						System.out.println("uriiiiiiii" + selectedImageUri);
						if (camera_key.equalsIgnoreCase("1")) {
							image_str = getRealPathFromURI(selectedImageUri);
							System.out.println("==image_str" + selectedImageUri);
							if (image_str != null) {
								 bitmap11 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
								 bsae64_1 = getStringImage(bitmap11);
								attachments_g.setText(image_str);
								//attachment2.setVisibility(View.VISIBLE);
								//attachment3.setVisibility(View.GONE);
								//attachment4.setVisibility(View.GONE);
							}
						} /*else if (camera_key.equalsIgnoreCase("2")) {
							image_str2 = getRealPathFromURI(selectedImageUri);
							System.out.println("===image_str2" + selectedImageUri);
							if (image_str2 != null) {
								 bitmap12 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
								 bsae64_2 = getStringImage(bitmap12);
								attachments_g2.setText(image_str2);
								attachment2.setVisibility(View.VISIBLE);
								//attachment3.setVisibility(View.VISIBLE);
								//attachment4.setVisibility(View.GONE);
							}
						}*//* else if (camera_key.equalsIgnoreCase("3")) {
							image_str3 = getRealPathFromURI(selectedImageUri);
							System.out.println("===image_str3" + selectedImageUri);
							if (image_str3 != null) {
								 bitmap13 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
								 bsae64_3 = getStringImage(bitmap13);
								attachments_g3.setText(image_str3);
								attachment2.setVisibility(View.VISIBLE);
								attachment3.setVisibility(View.VISIBLE);
								attachment4.setVisibility(View.VISIBLE);
							}
						} else if (camera_key.equalsIgnoreCase("4")) {
							image_str4 = getRealPathFromURI(selectedImageUri);
							System.out.println("======image_str4"
									+ selectedImageUri);

							if (image_str4 != null) {
								 bitmap14 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
								 bsae64_4 = getStringImage(bitmap14);
								attachments_g4.setText(image_str4);
								attachment2.setVisibility(View.VISIBLE);
								attachment3.setVisibility(View.VISIBLE);
								attachment4.setVisibility(View.VISIBLE);
							}
						}*/

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else if(resultCode==RESULT_CANCELED)
				{

					if (camera_key.equalsIgnoreCase("1")) {
						image_str = "";
						bsae64_1="";
					}/* else if (camera_key.equalsIgnoreCase("2")) {
						image_str2 = "";
						bsae64_2="";
						
					}*/ /*else if (camera_key.equalsIgnoreCase("3")) {
						image_str3 = "";
						bsae64_3="";
						
					} else if (camera_key.equalsIgnoreCase("4")) {
						image_str4 = "";
						bsae64_4="";
					
					}*/
					Toast.makeText(getApplicationContext(),
							"User Cancelled Image Capture", Toast.LENGTH_SHORT)
							.show();
				
				}
			
			} else if (requestCode == 1) {
				if(resultCode==RESULT_OK)
				{

					String ff = getRealPathFromURI(mUri).toString();
					if (camera_key.equalsIgnoreCase("1")) {
						image_str = ff;
						if (image_str != null) {
							 bitmap11 = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
							 bsae64_1 = getStringImage(bitmap11);
							attachments_g.setText(image_str);
							//attachment2.setVisibility(View.VISIBLE);
							//attachment3.setVisibility(View.GONE);
							//attachment4.setVisibility(View.GONE);
						}
					} /*else if (camera_key.equalsIgnoreCase("2")) {
						image_str2 = ff;
						if (image_str2 != null) {
							 bitmap12 = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
							 bsae64_2 = getStringImage(bitmap12);
							attachments_g2.setText(image_str2);
							attachment2.setVisibility(View.VISIBLE);
							//attachment3.setVisibility(View.VISIBLE);
							//attachment4.setVisibility(View.GONE);
						}
					}*//* else if (camera_key.equalsIgnoreCase("3")) {
						image_str3 = ff;
						if (image_str3 != null) {
							 bitmap13 = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
							 bsae64_3 = getStringImage(bitmap13);
							attachments_g3.setText(image_str3);
							attachment2.setVisibility(View.VISIBLE);
							//attachment3.setVisibility(View.VISIBLE);
							//attachment4.setVisibility(View.VISIBLE);
						}
					} else if (camera_key.equalsIgnoreCase("4")) {
						image_str4 = ff;
						if (image_str4 != null) {
							 bitmap14 = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
							 bsae64_4 = getStringImage(bitmap14);
							//attachments_g4.setText(image_str4);
							//attachment2.setVisibility(View.VISIBLE);
							//attachment3.setVisibility(View.VISIBLE);
							//attachment4.setVisibility(View.VISIBLE);
						}
					}*/
				}
				else if(resultCode==RESULT_CANCELED)
				{

					if (camera_key.equalsIgnoreCase("1")) {
						image_str = "";
						bsae64_1="";
					} /*else if (camera_key.equalsIgnoreCase("2")) {
						image_str2 = "";
						bsae64_2="";
						
					} *//*else if (camera_key.equalsIgnoreCase("3")) {
						image_str3 = "";
						bsae64_3="";
						
					} else if (camera_key.equalsIgnoreCase("4")) {
						image_str4 = "";
						bsae64_4="";
					
					}*/
					Toast.makeText(getApplicationContext(),
							"User Cancelled Image Capture", Toast.LENGTH_SHORT)
							.show();
				
				}


			}

			else if (requestCode == REQUEST_CODE_PAYMENT) {
				if (resultCode == Activity.RESULT_OK) {
					PaymentConfirmation confirm = data
							.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
					if (confirm != null) {
						try {
							System.out.print("==============get json======"
									+ confirm.toJSONObject().toString(4));

							JSONObject j = new JSONObject(confirm
									.toJSONObject().toString(4));

							JSONObject g = j.optJSONObject("response");
							JSONObject k = j.optJSONObject("client");

							 product_name = k.optString("product_name");
							 paypal_id = g.optString("id");

							System.out.println("===product name==="
									+ product_name);
							System.out.println("===product id===" + paypal_id);

							Log.i(TAG, confirm.getPayment().toJSONObject()
									.toString(4));
							/**
							 * TODO: send 'confirm' (and possibly
							 * confirm.getPayment() to your server for
							 * verification or consent completion. See
							 * https://developer
							 * .paypal.com/webapps/developer/docs
							 * /integration/mobile/verify-mobile-payment/ for
							 * more details.
							 * 
							 * For sample mobile backend interactions, see
							 * https:
							 * //github.com/paypal/rest-api-sdk-python/tree
							 * /master/samples/mobile_backend
							 */
							Toast.makeText(
									getApplicationContext(),
									"PaymentConfirmation info received from PayPal",
									Toast.LENGTH_LONG).show();
							if (Utils.isNetWorking(getApplicationContext())) {
								new Question_making().execute();
								new Payment_loader().execute();
							} else {
								Utils.show_Toast(getApplicationContext(),
										"Internet Not Available");
							}

						} catch (JSONException e) {
							Log.e(TAG,
									"an extremely unlikely failure occurred: ",
									e);
						}
					}
				} else if (resultCode == Activity.RESULT_CANCELED) {
					Toast.makeText(getApplicationContext(),
							"The user canceled.", Toast.LENGTH_LONG).show();
					Log.i(TAG, "The user canceled.");
				} else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
					Toast.makeText(
							getApplicationContext(),
							"An invalid Payment or PayPalConfiguration was submitted. Please see the docs.",
							Toast.LENGTH_LONG);
					Log.i(TAG,
							"An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ====================== above function finished here
	// ==========================//
	
	
	  public String getStringImage(Bitmap bmp){
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        bmp.compress(Bitmap.CompressFormat.JPEG, 40, baos);
	        byte[] imageBytes = baos.toByteArray();
	        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
	        return encodedImage;
	    }

	  
	////=========================================================//
		//======================payment loadre ======================//
		//===========================================================//
		class Payment_loader extends AsyncTask<Void, Void, Void> {
			//private ProgressDialog progress;
			private String url;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			/*	progress = new ProgressDialog(QuestionScreen.this);
				progress.setMessage("Loading...");
				progress.setIndeterminate(false);
				progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progress.show();*/
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
				url = Constants.PAYMENT_URL;
				System.out.println("====url=pp==" + url);
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url);
				try {
					MultipartEntity entity = new MultipartEntity();
					entity.addPart("user_id", new StringBody(loginId));
					entity.addPart("payment_method", new StringBody(product_name));
					entity.addPart("payment_id", new StringBody(paypal_id));
					entity.addPart("amount", new StringBody("501"));
					entity.addPart("type", new StringBody("Question"));
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
					success = hh.optString("success");
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

					if (success.equalsIgnoreCase("1")) {
						
						//progress.dismiss();
					
					} else {
						Utils.show_Toast(getApplicationContext(), "" + error);
						//progress.dismiss();
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	// ===========================================================================//
	// ======================= Hororscope Making loader here
	// =========================//
	// =========================================================================//

	class Question_making extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress;
		private String url;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(QuestionScreen.this);
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
			url = Constants.QUESTION_URL;
			System.out.println("====url=pp==" + url);
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			try {
				MultipartEntity entity = new MultipartEntity();
				if (image_str.equalsIgnoreCase("")) {
				} else {
					entity.addPart("image", new StringBody(bsae64_1));
					//entity.addPart("image", new FileBody(file1));
				}
				/*if (image_str2.equalsIgnoreCase("")) {
				} else {
					entity.addPart("image1", new StringBody(bsae64_2));
					//entity.addPart("image1", new FileBody(file2));
				}*/
				/*if (image_str3.equalsIgnoreCase("")) {
				} else {
					entity.addPart("image2", new StringBody(bsae64_3));
					//entity.addPart("image2", new FileBody(file3));
				}
				if (image_str4.equalsIgnoreCase("")) {
				} else {
					entity.addPart("image3", new StringBody(bsae64_4));
					//entity.addPart("image3", new FileBody(file4));
				}*/
				entity.addPart("user_id", new StringBody(loginId));
				entity.addPart("name", new StringBody(get_full_name));
				entity.addPart("email", new StringBody(get_email));
				entity.addPart("phone_number", new StringBody(get_phone));
				entity.addPart("gender", new StringBody(get_gender));
				entity.addPart("dob", new StringBody(get_date));
				entity.addPart("place_of_birth", new StringBody(get_place));
				entity.addPart("birth_hour", new StringBody(get_hrs));
				entity.addPart("birth_mint", new StringBody(get_min));
				entity.addPart("question", new StringBody(get_question));

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
				success = hh.optString("success");
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

				if (success.equalsIgnoreCase("1")) {
					Utils.show_Toast(getApplicationContext(),
							"Post Successfully");
							question_edit.setText("");
					attachments_g2.setText("");
					image_str="";
					bsae64_1="";
					attachments_g.setText("");
					progress.dismiss();
				/*	Intent i = new Intent(QuestionScreen.this,QuestionScreen.class);
					startActivity(i);
					overridePendingTransition(0, 0);*/
				} else {
					Utils.show_Toast(getApplicationContext(), "" + error);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// ==================== above loader finished here
	// =============================//

	// /=============== Click function of spinners here ===================//
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		switch (arg0.getId()) {
		case R.id.gen_edit:
			get_gender = gen_edit.getSelectedItem().toString();
			break;
		case R.id.HRs_edit:
			get_hrs = HRs_edit.getSelectedItem().toString();
			break;
		case R.id.min_edit:
			get_min = min_edit.getSelectedItem().toString();
			break;
		default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		System.out.println("====key Event====" + event);
		System.out.println("====keyCode ====" + keyCode);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onBackPressed();
			return true;
		}
		return false;
	}

	/*
	 * public boolean onKeyDown(int keyCode, KeyEvent event) {
	 * System.out.println("====key Event===="+event);
	 * System.out.println("====keyCode ===="+keyCode); if (keyCode ==
	 * KeyEvent.KEYCODE_CAMERA) { onBackPressed(); } return true; }
	 */

	// ========================pay pal functions=========================//
	private PayPalPayment getThingToBuy(String paymentIntent) {
		return new PayPalPayment(new BigDecimal("7.44"), "USD", "Question Put",
				paymentIntent);
	}

	public void onFuturePaymentPressed(View pressed) {
		Intent intent = new Intent(QuestionScreen.this,
				PayPalFuturePaymentActivity.class);

		// send the same configuration for restart resiliency
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

		startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
	}

	public void onProfileSharingPressed(View pressed) {
		Intent intent = new Intent(QuestionScreen.this,
				PayPalProfileSharingActivity.class);

		// send the same configuration for restart resiliency
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

		intent.putExtra(PayPalProfileSharingActivity.EXTRA_REQUESTED_SCOPES,
				getOauthScopes());

		startActivityForResult(intent, REQUEST_CODE_PROFILE_SHARING);
	}

	private PayPalOAuthScopes getOauthScopes() {
		/*
		 * create the set of required scopes Note: see
		 * https://developer.paypal.com
		 * /docs/integration/direct/identity/attributes/ for mapping between the
		 * attributes you select for this app in the PayPal developer portal and
		 * the scopes required here.
		 */
		Set<String> scopes = new HashSet<String>(Arrays.asList(
				PayPalOAuthScopes.PAYPAL_SCOPE_EMAIL,
				PayPalOAuthScopes.PAYPAL_SCOPE_ADDRESS));
		return new PayPalOAuthScopes(scopes);
	}

	@SuppressWarnings("unused")
	private void sendAuthorizationToServer(PayPalAuthorization authorization) {

		/**
		 * TODO: Send the authorization response to your server, where it can
		 * exchange the authorization code for OAuth access and refresh tokens.
		 * 
		 * Your server must then store these tokens, so that your server code
		 * can execute payments for this user in the future.
		 * 
		 * A more complete example that includes the required app-server to
		 * PayPal-server integration is available from
		 * https://github.com/paypal/
		 * rest-api-sdk-python/tree/master/samples/mobile_backend
		 */

	}

	@Override
	public void onDestroy() {
		// Stop service when done
		stopService(new Intent(this, PayPalService.class));
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Utils.show_Toast(getApplicationContext(), "I m Here");

	}
}
