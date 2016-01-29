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
import org.json.JSONArray;
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

public class VastuScreen extends Activity implements OnClickListener,
		OnItemSelectedListener {
	SimpleSideDrawer mNav;
	Uri mUri;
	Spinner HRs_edit, min_edit, vastu_type;
	ArrayList<String> dummy = new ArrayList<String>();
	ArrayAdapter<String> adapter;
	TextView About_txt, Change_txt;
	String description;
	ProgressDialog progress;
	ArrayList<String> hours_list = new ArrayList<String>();
	ArrayList<String> min_list = new ArrayList<String>();
	ArrayList<String> v_id_list;
	ArrayList<String> v_name_list;
	ArrayAdapter<String> hours_adapter, min_adpater, vastu_adapter;
	SharedPreferences myPref;
	ImageView submit, attach_btn_g, attach_btn_g2, attach_btn_g3,
			attach_btn_g4,calnder2;
	EditText date_edit2, Place_edit, question_edit, attachments_g,
			attachments_g2, attachments_g3, attachments_g4;
	RelativeLayout attachment1, attachment2, attachment3, attachment4;
	public static String image_str = "", image_str2 = "", image_str3 = "",
			image_str4 = "", camera_key;
	String loginId, get_date, get_place, get_hrs, get_min, v_id, v_name,
			get_question, success, error, get_type,get_active;
	
	 private Bitmap bitmap11,bitmap12;//,bitmap13,bitmap14;
	 String bsae64_1="",bsae64_2="",bsae64_3="",bsae64_4="";
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
	private static final String CONFIG_CLIENT_ID = "ARdmUzW0WuLBbaSYV7lCEkiZl2GtOMF7_fjaZ9SEqtR5EzvqK8GX-VhnE9RLJbaAoFi1TpuZ-uGV8-HT";

	private static final int REQUEST_CODE_PAYMENT = 13;
	private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
	private static final int REQUEST_CODE_PROFILE_SHARING = 3;

	private static PayPalConfiguration config = new PayPalConfiguration()
			.environment(CONFIG_ENVIRONMENT)
			.clientId(CONFIG_CLIENT_ID)
			// The following are only used in PayPalFuturePaymentActivity.
			.merchantName("Merchant")
			.merchantPrivacyPolicyUri(
					Uri.parse("https://www.example.com/privacy"))
			.merchantUserAgreementUri(
					Uri.parse("https://www.example.com/legal"));

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_vastu_screen);

		// Getting the value from Preferences
		myPref = PreferenceManager
				.getDefaultSharedPreferences(VastuScreen.this);
		loginId = myPref.getString("User_login", "");
		get_date = myPref.getString("dob", "");
		get_place = myPref.getString("pob", "");
		get_hrs = myPref.getString("hrs", "");
		get_min = myPref.getString("min", "");
		get_active = myPref.getString("active", "");
		image_str = "";
		image_str2 = "";
		image_str3 = "";
		image_str4 = "";
		mUri = null;

		find_id();
		setvalues();
		set_old_value();

		mNav = new SimpleSideDrawer(this);
		Slidermenu side = new Slidermenu(mNav, VastuScreen.this);
		side.setDrawer();
		findViewById(R.id.slider).setVisibility(View.GONE);
		mNav = new SimpleSideDrawer(this);
		Slidermenu side1 = new Slidermenu(mNav, VastuScreen.this);
		side1.setDrawer();
		try {
			get_active = getIntent().getStringExtra("status");
			
			if(get_active.equalsIgnoreCase("Va"))
			{
				findViewById(R.id.vastu).setBackgroundResource(R.drawable.menu_highlit);
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

		SpannableString content = new SpannableString("RESET");
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		Change_txt.setText(content);
		// Spinners
		HRs_edit = (Spinner) findViewById(R.id.HRs_edit);
		min_edit = (Spinner) findViewById(R.id.min_edit);
		vastu_type = (Spinner) findViewById(R.id.vastu_type);
		// Editext
		// date_edit2,Place_edit,question_edit;
		date_edit2 = (EditText) findViewById(R.id.date_edit2);
		Place_edit = (EditText) findViewById(R.id.Place_edit);
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

		// Image view
		attach_btn_g = (ImageView) findViewById(R.id.attach_btn_g);
		attach_btn_g2 = (ImageView) findViewById(R.id.attach_btn_g2);
		attach_btn_g3 = (ImageView) findViewById(R.id.attach_btn_g3);
		attach_btn_g4 = (ImageView) findViewById(R.id.attach_btn_g4);
		submit = (ImageView) findViewById(R.id.submit);
		calnder2= (ImageView) findViewById(R.id.calnder2);
		// Apply Click Listener
		submit.setOnClickListener(this);
		attach_btn_g.setOnClickListener(this);
		attach_btn_g2.setOnClickListener(this);
		attach_btn_g3.setOnClickListener(this);
		attach_btn_g4.setOnClickListener(this);
		date_edit2.setOnClickListener(this);
		submit.setOnClickListener(this);
		Change_txt.setOnClickListener(this);
		calnder2.setOnClickListener(this);
		HRs_edit.setOnItemSelectedListener(this);
		min_edit.setOnItemSelectedListener(this);
		vastu_type.setOnItemSelectedListener(this);

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
		// set adpter on the Hours Spinner
		hours_adapter = new ArrayAdapter<String>(VastuScreen.this,
				R.layout.spinner_view, R.id.text_main_seen, hours_list);
		hours_adapter.setDropDownViewResource(R.layout.spinner_view3);
		HRs_edit.setAdapter(hours_adapter);
		// set adpter on the Hours Spinner
		min_adpater = new ArrayAdapter<String>(VastuScreen.this,
				R.layout.spinner_view, R.id.text_main_seen, min_list);
		min_adpater.setDropDownViewResource(R.layout.spinner_view3);
		min_edit.setAdapter(min_adpater);

	}

	private void set_old_value() {
		date_edit2.setText(get_date);
		Place_edit.setText(get_place);

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
				progress = new ProgressDialog(VastuScreen.this);
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

			about_url = Constants.ABOUT_URL + "id=8";
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
				new Vastu_Type().execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// ==================== above loader finished here
	// =============================//

	// ================================================================================//
	// ======================= Get Vastu Type loader here
	// ===========================//
	// ==============================================================================//

	class Vastu_Type extends AsyncTask<String, Void, String> {

		private String about_url;
		private URI uri;
		private String result;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@SuppressLint("NewApi")
		@Override
		protected String doInBackground(String... params) {
			v_id_list = new ArrayList<String>();
			v_name_list = new ArrayList<String>();
			about_url = Constants.TYPE_VASTU;
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
					JSONArray obj1 = jsonA.optJSONArray("vastu_type");
					for (int i = 0; i < obj1.length(); i++) {
						if (i == 0) {
							v_name_list.add("Select Vastu");
							v_id_list.add("00");
						}
						JSONObject hh = obj1.optJSONObject(i);
						v_id = hh.optString("id");
						v_name = hh.optString("vastu_type");

						v_id_list.add(v_id);
						v_name_list.add(v_name);

					}

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

				vastu_adapter = new ArrayAdapter<String>(VastuScreen.this,
						R.layout.spinner_view, R.id.text_main_seen, v_name_list);
				vastu_adapter.setDropDownViewResource(R.layout.spinner_view3);
				vastu_type.setAdapter(vastu_adapter);
				// progress.dismiss();
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
		case R.id.date_edit2:
			DateID();
			break;
		case R.id.calnder2:
			DateID();
			break;

		case R.id.Change_txt:

			date_edit2.setText("");
			Place_edit.setText("");
			question_edit.setText("");
			HRs_edit.setSelection(0);
			min_edit.setSelection(0);
			vastu_type.setSelection(0);
			attachments_g.setText("");
			attachments_g2.setText("");
			image_str = "";
			image_str2 = "";
			image_str3 = "";
			image_str4 = "";
			bsae64_1="";
			bsae64_2="";
			bsae64_3="";
			bsae64_4="";
			mUri = null;

			break;

		case R.id.submit:
			get_date = date_edit2.getText().toString();
			get_place = Place_edit.getText().toString();
			get_question = question_edit.getText().toString();
			 if (get_type.equalsIgnoreCase("Select Vastu")) {
					Utils.show_Toast(getApplicationContext(),
							"Please Select Vastu Type");
				}
			else if (get_date.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(), "Please Enter Date");
				date_edit2.requestFocus();
			} 
			 else if (get_place.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter Birth Place");
				Place_edit.requestFocus();
			} else if (get_hrs.equalsIgnoreCase("Select Hours")) {
				Utils.show_Toast(getApplicationContext(), "Please Select Hours");
			} else if (get_min.equalsIgnoreCase("Select Minutes")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Select Minutes");
			} else if (get_question.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter Description");
				question_edit.requestFocus();
			} else if (image_str.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Add At Least One Attachment");
			} else {
				 new Vast_making().execute();
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

				Intent intent = new Intent(VastuScreen.this,
						PaymentActivity.class);

				// send the same configuration for restart resiliency
				intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,
						config);

				intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

				startActivityForResult(intent, REQUEST_CODE_PAYMENT);*/
			}

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
		DatePickerDialog dpd = new DatePickerDialog(VastuScreen.this,
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						date_edit2.setText(dayOfMonth + "-"
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
						VastuScreen.this).create();
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
								attachment2.setVisibility(View.VISIBLE);
								attachment3.setVisibility(View.GONE);
								attachment4.setVisibility(View.GONE);
							}
						} else if (camera_key.equalsIgnoreCase("2")) {
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
						} /*else if (camera_key.equalsIgnoreCase("3")) {
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
						}
*/
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				else if(resultCode == RESULT_CANCELED)
				{
					if (camera_key.equalsIgnoreCase("1")) {
						image_str = "";
						bsae64_1="";
					} else if (camera_key.equalsIgnoreCase("2")) {
						image_str2 = "";
						bsae64_2="";
						
					} /*else if (camera_key.equalsIgnoreCase("3")) {
						image_str3 = "";
						bsae64_3="";
						
					} else if (camera_key.equalsIgnoreCase("4")) {
						image_str4 = "";
						bsae64_4="";
					
					}*/
					Toast.makeText(getApplicationContext(),
							"User Cancelled Image Select", Toast.LENGTH_SHORT)
							.show();
				}
				
			}

			else if (requestCode == 1) {
					if(resultCode==RESULT_OK)
					{
						String ff = getRealPathFromURI(mUri).toString();
						if (camera_key.equalsIgnoreCase("1")) {
							image_str = ff;
							if (image_str != null) {
								 bitmap11 = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
								 bsae64_1 = getStringImage(bitmap11);
								attachments_g.setText(image_str);
								attachment2.setVisibility(View.VISIBLE);
								attachment3.setVisibility(View.GONE);
								attachment4.setVisibility(View.GONE);
							}
						} else if (camera_key.equalsIgnoreCase("2")) {
							image_str2 = ff;
							if (image_str2 != null) {
								 bitmap12 = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
								 bsae64_2 = getStringImage(bitmap12);
								attachments_g2.setText(image_str2);
								attachment2.setVisibility(View.VISIBLE);
								//attachment3.setVisibility(View.VISIBLE);
								//attachment4.setVisibility(View.GONE);
							}
						}/* else if (camera_key.equalsIgnoreCase("3")) {
							image_str3 = ff;
							if (image_str3 != null) {
								 bitmap13 = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
								 bsae64_3 = getStringImage(bitmap13);
								attachments_g3.setText(image_str3);
								attachment2.setVisibility(View.VISIBLE);
								attachment3.setVisibility(View.VISIBLE);
								attachment4.setVisibility(View.VISIBLE);
							}
						} else if (camera_key.equalsIgnoreCase("4")) {
							image_str4 = ff;
							if (image_str4 != null) {
								 bitmap14 = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
								 bsae64_4 = getStringImage(bitmap14);
								attachments_g4.setText(image_str4);
								attachment2.setVisibility(View.VISIBLE);
								attachment3.setVisibility(View.VISIBLE);
								attachment4.setVisibility(View.VISIBLE);
							}
						}
	*/
					}
					else if(resultCode == RESULT_CANCELED)
					{
						if (camera_key.equalsIgnoreCase("1")) {
							image_str = "";
							bsae64_1="";
						} else if (camera_key.equalsIgnoreCase("2")) {
							image_str2 = "";
							bsae64_2="";
							
						} /*else if (camera_key.equalsIgnoreCase("3")) {
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
			
			} else if (requestCode == REQUEST_CODE_PAYMENT) {
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

							product_name= k.optString("product_name");
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
								new Vast_making().execute();
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
	
	  public String getStringImage(Bitmap bmp){
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        bmp.compress(Bitmap.CompressFormat.JPEG, 40, baos);
	        byte[] imageBytes = baos.toByteArray();
	        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
	        return encodedImage;
	    }

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		switch (arg0.getId()) {

		case R.id.HRs_edit:
			get_hrs = HRs_edit.getSelectedItem().toString();
			break;
		case R.id.min_edit:
			get_min = min_edit.getSelectedItem().toString();
			break;
		case R.id.vastu_type:
			get_type = vastu_type.getSelectedItem().toString();
			break;
		default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	// ====================== above function finished here
	// ==========================//
	
	
	////=========================================================//
	//======================payment loadre ======================//
	//===========================================================//
	class Payment_loader extends AsyncTask<Void, Void, Void> {
		//private ProgressDialog progress;
		private String url;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/*progress = new ProgressDialog(VastuScreen.this);
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
				entity.addPart("amount", new StringBody("1101"));
				entity.addPart("type", new StringBody(" Vastu"));
				

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
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	

	// ===========================================================================//
	// ======================= Vastu Making loader here
	// =========================//
	// =========================================================================//

	class Vast_making extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress;
		private String url;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(VastuScreen.this);
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
			url = Constants.VASTU_URL;
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
				if (image_str2.equalsIgnoreCase("")) {
				} else {
					entity.addPart("image1", new StringBody(bsae64_2));
				//	entity.addPart("image1", new FileBody(file2));
				}
				/*if (image_str3.equalsIgnoreCase("")) {
				} else {
					entity.addPart("image2", new StringBody(bsae64_3));
					///entity.addPart("image2", new FileBody(file3));
				}
				if (image_str4.equalsIgnoreCase("")) {
				} else {
					entity.addPart("image3", new StringBody(bsae64_4));
					//entity.addPart("image3", new FileBody(file4));
				}*/
				entity.addPart("user_id", new StringBody(loginId));
				entity.addPart("vastu_type", new StringBody(get_type));
				entity.addPart("dob", new StringBody(get_date));
				entity.addPart("description", new StringBody(get_question));
				entity.addPart("place_of_birth", new StringBody(get_place));
				entity.addPart("birth_hour", new StringBody(get_hrs));
				entity.addPart("birth_mint", new StringBody(get_min));

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
					image_str="";
					image_str2="";
					bsae64_1="";
					bsae64_2="";
					vastu_type.setSelection(0);
					attachments_g.setText("");
					question_edit.setText("");
						attachments_g2.setText("");
					progress.dismiss();
				
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

	// ========================pay pal functions=========================//
	private PayPalPayment getThingToBuy(String paymentIntent) {
		return new PayPalPayment(new BigDecimal("16.36"), "USD", "Vastu Making",
				paymentIntent);
	}

	public void onFuturePaymentPressed(View pressed) {
		Intent intent = new Intent(VastuScreen.this,
				PayPalFuturePaymentActivity.class);

		// send the same configuration for restart resiliency
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

		startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
	}

	public void onProfileSharingPressed(View pressed) {
		Intent intent = new Intent(VastuScreen.this,
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

}
