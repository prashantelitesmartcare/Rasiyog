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

import com.elite.rasiyog.Utilities.Constants;
import com.elite.rasiyog.Utilities.Slidermenu;
import com.elite.rasiyog.Utilities.Utils;
import com.navdrawer.SimpleSideDrawer;

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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;

public class MatchMaking extends Activity implements OnClickListener,
		OnItemSelectedListener {

	Uri mUri;
	SimpleSideDrawer mNav;
	ArrayList<String> dummy = new ArrayList<String>();
	ArrayAdapter<String> adapter;
	Spinner HRs_edit, min_edit, HRs_edit_g, min_edit_g;
	TextView About_txt, Change_txt;
	ProgressDialog progress;
	ArrayList<String> gender_list = new ArrayList<String>();
	ArrayList<String> hours_list = new ArrayList<String>();
	ArrayList<String> min_list = new ArrayList<String>();
	ArrayAdapter<String> hours_adapter, min_adpater, gender_adpter;
	String description, camera_key, gender_key, get_full_name, get_gender,
			get_date, get_place, get_hrs, get_min;
	ImageView attach_btn_b, attach_btn_b2, attach_btn_b3, attach_btn_b4,
			attach_btn_g, attach_btn_g2, attach_btn_g3, attach_btn_g4, submit,calnder,calnder_g;
	RelativeLayout attachment1_b, attachment2_b, attachment3_b, attachment4_b,
			attachment1, attachment2, attachment3, attachment4;
	EditText attachments_b, attachments_b2, attachments_b3, attachments_b4,
			attachments_g, attachments_g2, attachments_g3, attachments_g4;
	EditText full_name_edit, date_edit, Place_edit, full_name_edit_g,
			date_edit_g, Place_edit_g;
	String image_str = "", image_str2 = "", image_str3 = "", image_str4 = "",
			image_str5 = "", image_str6 = "", image_str7 = "", image_str8 = "";
	SharedPreferences myPref;
	String loginId, get_boy_name, get_boy_date, get_boy_place, get_boy_hrs,
			get_boy_min, get_girl_name, get_gir_date, get_gril_place,
			get_girl_hrs, get_girl_min, success, error,get_active;
	
	 private Bitmap bitmap11,bitmap15;//,bitmap12,bitmap13,bitmap14,,bitmap16,bitmap17,bitmap18;
	 String bsae64_1="",bsae64_2="",bsae64_3="",bsae64_4="", bsae64_5="",bsae64_6="",bsae64_7="",bsae64_8="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_match_making);
		// Getting the value from Preferences
		myPref = PreferenceManager
				.getDefaultSharedPreferences(MatchMaking.this);
		loginId = myPref.getString("User_login", "");
		get_full_name = myPref.getString("name", "");
		get_gender = myPref.getString("gender", "");
		get_date = myPref.getString("dob", "");
		get_place = myPref.getString("pob", "");
		get_hrs = myPref.getString("hrs", "");
		get_min = myPref.getString("min", "");
		get_active = myPref.getString("active", "");
		System.out.println("=======date======="+get_date);
		find_id();
		setvalue();
		set_old_value();

		mNav = new SimpleSideDrawer(this);
		Slidermenu side = new Slidermenu(mNav, MatchMaking.this);
		side.setDrawer();
		findViewById(R.id.slider).setVisibility(View.GONE);
		mNav = new SimpleSideDrawer(this);
		Slidermenu side1 = new Slidermenu(mNav, MatchMaking.this);
		side1.setDrawer();
		try {
			get_active = getIntent().getStringExtra("status");
			
			if(get_active.equalsIgnoreCase("Mat"))
			{
				findViewById(R.id.match).setBackgroundResource(R.drawable.menu_highlit);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (Utils.isNetWorking(getApplicationContext())) {
			new About_loader().execute();
		}
	}

	private void setvalue() {
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
		hours_adapter = new ArrayAdapter<String>(MatchMaking.this,
				R.layout.spinner_view, R.id.text_main_seen, hours_list);
		hours_adapter.setDropDownViewResource(R.layout.spinner_view3);
		HRs_edit.setAdapter(hours_adapter);
		HRs_edit_g.setAdapter(hours_adapter);
		// set adpter on the Hours Spinner
		min_adpater = new ArrayAdapter<String>(MatchMaking.this,
				R.layout.spinner_view, R.id.text_main_seen, min_list);
		min_adpater.setDropDownViewResource(R.layout.spinner_view3);
		min_edit.setAdapter(min_adpater);
		min_edit_g.setAdapter(min_adpater);

	}

	private void set_old_value() {

		if (get_gender.equalsIgnoreCase("Male")) {
			full_name_edit.setText(get_full_name);
			date_edit.setText(get_date);
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
		} else if (get_gender.equalsIgnoreCase("Female")) {
			full_name_edit_g.setText(get_full_name);
			date_edit_g.setText(get_date);
			Place_edit_g.setText(get_place);

			// Set values in Hours Spinner
			for (int i = 0; i < hours_adapter.getCount(); i++) {
				if (get_hrs.trim().equals(hours_adapter.getItem(i).toString())) {
					HRs_edit_g.setSelection(i);
					break;
				}
			}
			// Set values in Min Spinner
			for (int i = 0; i < min_adpater.getCount(); i++) {
				if (get_min.trim().equals(min_adpater.getItem(i).toString())) {
					min_edit_g.setSelection(i);
					break;
				}
			}
		}
	}

	private void find_id() {
		// Text view
		About_txt = (TextView) findViewById(R.id.About_txt);
		Change_txt = (TextView) findViewById(R.id.Change_txt);

		SpannableString content = new SpannableString("RESET");
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		Change_txt.setText(content);
		// spinners for boy
		HRs_edit = (Spinner) findViewById(R.id.HRs_edit);
		min_edit = (Spinner) findViewById(R.id.min_edit);

		// spiiner for girl
		HRs_edit_g = (Spinner) findViewById(R.id.HRs_edit_g);
		min_edit_g = (Spinner) findViewById(R.id.min_edit_g);

		// Edittetx for boys
		attachments_b = (EditText) findViewById(R.id.attachments_b);
		attachments_b2 = (EditText) findViewById(R.id.attachments_b2);
		attachments_b3 = (EditText) findViewById(R.id.attachments_b3);
		attachments_b4 = (EditText) findViewById(R.id.attachments_b4);
		Place_edit = (EditText) findViewById(R.id.Place_edit);
		full_name_edit = (EditText) findViewById(R.id.full_name_edit);
		date_edit = (EditText) findViewById(R.id.date_edit);
		// Edittetx for girl
		attachments_g = (EditText) findViewById(R.id.attachments_g);
		attachments_g2 = (EditText) findViewById(R.id.attachments_g2);
		attachments_g3 = (EditText) findViewById(R.id.attachments_g3);
		attachments_g4 = (EditText) findViewById(R.id.attachments_g4);
		Place_edit_g = (EditText) findViewById(R.id.Place_edit_g);
		full_name_edit_g = (EditText) findViewById(R.id.full_name_edit_g);
		date_edit_g = (EditText) findViewById(R.id.date_edit_g);
		// Relativelayouts for boy
		attachment1_b = (RelativeLayout) findViewById(R.id.attachment1_b);
		attachment2_b = (RelativeLayout) findViewById(R.id.attachment2_b);
		attachment3_b = (RelativeLayout) findViewById(R.id.attachment3_b);
		attachment4_b = (RelativeLayout) findViewById(R.id.attachment4_b);
		// Set visibility of views
		attachment1_b.setVisibility(View.VISIBLE);
		attachment2_b.setVisibility(View.GONE);
		attachment3_b.setVisibility(View.GONE);
		attachment4_b.setVisibility(View.GONE);
		// Relativelayouts for girl
		attachment1 = (RelativeLayout) findViewById(R.id.attachment1);
		attachment2 = (RelativeLayout) findViewById(R.id.attachment2);
		attachment3 = (RelativeLayout) findViewById(R.id.attachment3);
		attachment4 = (RelativeLayout) findViewById(R.id.attachment4);
		// Set visibility of views
		attachment1.setVisibility(View.VISIBLE);
		attachment2.setVisibility(View.GONE);
		attachment3.setVisibility(View.GONE);
		attachment4.setVisibility(View.GONE);
		// Imageview
		submit = (ImageView) findViewById(R.id.submit);
		// for boy
		attach_btn_b = (ImageView) findViewById(R.id.attach_btn_b);
		attach_btn_b2 = (ImageView) findViewById(R.id.attach_btn_b2);
		attach_btn_b3 = (ImageView) findViewById(R.id.attach_btn_b3);
		attach_btn_b4 = (ImageView) findViewById(R.id.attach_btn_b4);
		// for girl
		attach_btn_g = (ImageView) findViewById(R.id.attach_btn_g);
		attach_btn_g2 = (ImageView) findViewById(R.id.attach_btn_g2);
		attach_btn_g3 = (ImageView) findViewById(R.id.attach_btn_g3);
		attach_btn_g4 = (ImageView) findViewById(R.id.attach_btn_g4);
		calnder_g= (ImageView) findViewById(R.id.calnder_g);
		calnder= (ImageView) findViewById(R.id.calnder);
		// Apply click listnere
		attach_btn_b.setOnClickListener(this);
		attach_btn_b2.setOnClickListener(this);
		attach_btn_b3.setOnClickListener(this);
		attach_btn_b4.setOnClickListener(this);
		Change_txt.setOnClickListener(this);
		calnder.setOnClickListener(this);
		calnder_g.setOnClickListener(this);
		attach_btn_g.setOnClickListener(this);
		attach_btn_g2.setOnClickListener(this);
		attach_btn_g3.setOnClickListener(this);
		attach_btn_g4.setOnClickListener(this);

		date_edit_g.setOnClickListener(this);
		date_edit.setOnClickListener(this);
		submit.setOnClickListener(this);

		HRs_edit.setOnItemSelectedListener(this);
		HRs_edit_g.setOnItemSelectedListener(this);

		min_edit.setOnItemSelectedListener(this);
		min_edit_g.setOnItemSelectedListener(this);

	}

	// ===========================================================================//
	// ======================= Get About loader here
	// ===========================//
	// =========================================================================//

	class About_loader extends AsyncTask<String, Void, String> {
		private String about_url;
		private URI uri;
		private String result;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				progress = new ProgressDialog(MatchMaking.this);
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
			about_url = Constants.ABOUT_URL + "id=11";
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
		case R.id.attach_btn_b:
			openCamera();
			camera_key = "1";
			gender_key = "boy";
			break;
		case R.id.attach_btn_b2:
			openCamera();
			camera_key = "2";
			gender_key = "boy";
			break;
		case R.id.attach_btn_b3:
			openCamera();
			camera_key = "3";
			gender_key = "boy";
			break;
		case R.id.attach_btn_b4:
			openCamera();
			camera_key = "4";
			gender_key = "boy";
			break;
		case R.id.attach_btn_g:
			openCamera();
			camera_key = "5";
			gender_key = "girl";
			break;
		case R.id.attach_btn_g2:
			openCamera();
			camera_key = "6";
			gender_key = "girl";
			break;
		case R.id.attach_btn_g3:
			openCamera();
			camera_key = "7";
			gender_key = "girl";
			break;
		case R.id.attach_btn_g4:
			openCamera();
			camera_key = "8";
			gender_key = "girl";
			break;
		case R.id.Change_txt:
			date_edit.setText("");
			full_name_edit.setText("");
			Place_edit.setText("");
			full_name_edit_g.setText("");
			date_edit_g.setText("");
			HRs_edit.setSelection(0);
			min_edit.setSelection(0);
			HRs_edit_g.setSelection(0);
			min_edit_g.setSelection(0);
			Place_edit_g.setText("");
			attachments_b.setText("");
			attachments_g.setText("");
			image_str = "";
			image_str2 = "";
			image_str3 = "";
			image_str4 = ""
			;bsae64_1="";
			bsae64_2="";
			bsae64_3="";
			bsae64_4="";
			bsae64_5="";
			bsae64_6="";
			bsae64_7="";
			bsae64_8="";



			mUri = null;

			break;

		case R.id.date_edit:
			DateID();
			break;
		case R.id.calnder:
			DateID();
			break;
		case R.id.calnder_g:
			DateID2();
			break;
		case R.id.date_edit_g:
			DateID2();
			break;
		case R.id.submit:
			get_boy_name = full_name_edit.getText().toString();
			get_boy_date = date_edit.getText().toString();
			get_boy_place = Place_edit.getText().toString();

			get_girl_name = full_name_edit_g.getText().toString();
			get_gir_date = date_edit_g.getText().toString();
			get_gril_place = Place_edit_g.getText().toString();

			if (get_boy_name.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter Boy Name");
				full_name_edit.requestFocus();
			} else if (get_boy_date.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter Boy D.O.B");
				date_edit.requestFocus();
			} else if (get_boy_place.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter Boy Birth Place");
				Place_edit.requestFocus();
			} else if (get_boy_hrs.equalsIgnoreCase("Select Hours")) {
				Utils.show_Toast(getApplicationContext(), "Please Select Hours");
			} else if (get_boy_min.equalsIgnoreCase("Select Minutes")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Select Minutes");
			} else if (image_str.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Add At Least One Attachment For Boy");
			} else if (get_girl_name.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter Girl Name");
				full_name_edit_g.requestFocus();
			} else if (get_gir_date.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter Girl D.O.B");
				date_edit_g.requestFocus();
			} else if (get_gril_place.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter Girl Birth Place");
				Place_edit_g.requestFocus();
			} else if (get_girl_hrs.equalsIgnoreCase("Select Hours")) {
				Utils.show_Toast(getApplicationContext(), "Please Select Hours");
			} else if (get_girl_min.equalsIgnoreCase("Select Minutes")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Select Minutes");
			} else if (image_str5.equalsIgnoreCase("")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Add At Least One Attachment For Girl");
			} else if (Utils.isNetWorking(getApplicationContext())) {
				new Match_making().execute();
			} else {
				Utils.show_Toast(getApplicationContext(),
						"Internet Not Available");
			}

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
		DatePickerDialog dpd = new DatePickerDialog(
				MatchMaking.this,
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year,
										  int monthOfYear, int dayOfMonth) {
						date_edit.setText(dayOfMonth + "-"
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

	private void DateID2() {
		// TODO Auto-generated method stub
		final Calendar c = Calendar.getInstance();
		// int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		DatePickerDialog dpd = new DatePickerDialog(
				MatchMaking.this,
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year,
										  int monthOfYear, int dayOfMonth) {
						date_edit_g.setText(dayOfMonth + "-"
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
						MatchMaking.this).create();
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
								mUri = null;
								image_str = "";
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
					System.out.println("----camera keyy-----" + camera_key);
					System.out.println("----gender keyy-----" + gender_key);
					Bitmap bitmap2 = null;
					try {
						Uri selectedImageUri = null;
						selectedImageUri = data.getData();
						System.out.println("uriiiiiiii" + selectedImageUri);
						if (gender_key.equalsIgnoreCase("boy")) {
							if (camera_key.equalsIgnoreCase("1")) {
								image_str = getRealPathFromURI(selectedImageUri);
								System.out.println("-----path1 for boy-----"
										+ image_str);
								if (image_str != null) {
									 bitmap11 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
									 bsae64_1 = getStringImage(bitmap11);
									attachments_b.setText(image_str);
									//attachment2_b.setVisibility(View.VISIBLE);
									//attachment3_b.setVisibility(View.GONE);
									//attachment4_b.setVisibility(View.GONE);
								}
							} /*else if (camera_key.equalsIgnoreCase("2")) {
								image_str2 = getRealPathFromURI(selectedImageUri);
								System.out.println("-----path2 for boy-----"
										+ image_str2);
								if (image_str2 != null) {
									 bitmap12 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
									 bsae64_2 = getStringImage(bitmap12);
									attachments_b2.setText(image_str2);
									attachment2_b.setVisibility(View.VISIBLE);
									attachment3_b.setVisibility(View.VISIBLE);
									attachment4_b.setVisibility(View.GONE);
								}
							} else if (camera_key.equalsIgnoreCase("3")) {
								image_str3 = getRealPathFromURI(selectedImageUri);
								System.out.println("-----path3 for boy-----"
										+ image_str3);
								if (image_str3 != null) {
									 bitmap13 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
									 bsae64_3 = getStringImage(bitmap13);
									attachments_b3.setText(image_str3);
									attachment2_b.setVisibility(View.VISIBLE);
									attachment3_b.setVisibility(View.VISIBLE);
									attachment4_b.setVisibility(View.VISIBLE);
								}
							} else if (camera_key.equalsIgnoreCase("4")) {
								image_str4 = getRealPathFromURI(selectedImageUri);
								System.out.println("-----path4 for boy-----"
										+ image_str4);

								if (image_str4 != null) {
									 bitmap14 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
									 bsae64_4 = getStringImage(bitmap14);
									attachments_b4.setText(image_str4);
									attachment2_b.setVisibility(View.VISIBLE);
									attachment3_b.setVisibility(View.VISIBLE);
									attachment4_b.setVisibility(View.VISIBLE);
								}
							}*/
						} else if (gender_key.equalsIgnoreCase("girl")) {
							if (camera_key.equalsIgnoreCase("5")) {
								image_str5 = getRealPathFromURI(selectedImageUri);
								System.out.println("-----path1 for girl-----"
										+ image_str5);
								if (image_str5 != null) {
									 bitmap15 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
									 bsae64_5 = getStringImage(bitmap15);
									attachments_g.setText(image_str5);
									//attachment2.setVisibility(View.VISIBLE);
									//attachment3.setVisibility(View.GONE);
									//attachment4.setVisibility(View.GONE);
								}
							} /*else if (camera_key.equalsIgnoreCase("6")) {
								image_str6 = getRealPathFromURI(selectedImageUri);
								System.out.println("-----path2 for girl-----"
										+ image_str6);
								if (image_str6 != null) {
									 bitmap16 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
									 bsae64_6 = getStringImage(bitmap16);
									attachments_g2.setText(image_str6);
									attachment2.setVisibility(View.VISIBLE);
									attachment3.setVisibility(View.VISIBLE);
									attachment4.setVisibility(View.GONE);
								}
							} else if (camera_key.equalsIgnoreCase("7")) {
								image_str7 = getRealPathFromURI(selectedImageUri);
								System.out.println("-----path3 for girl-----"
										+ image_str7);
								if (image_str7 != null) {
									 bitmap17 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
									 bsae64_7 = getStringImage(bitmap17);
									attachments_g3.setText(image_str7);
									attachment2.setVisibility(View.VISIBLE);
									attachment3.setVisibility(View.VISIBLE);
									attachment4.setVisibility(View.VISIBLE);
								}
							} else if (camera_key.equalsIgnoreCase("8")) {
								image_str8 = getRealPathFromURI(selectedImageUri);
								System.out.println("-----path4 for girl-----"
										+ image_str8);

								if (image_str8 != null) {
									 bitmap18 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
									 bsae64_8 = getStringImage(bitmap18);
									attachments_g4.setText(image_str8);
									attachment2.setVisibility(View.VISIBLE);
									attachment3.setVisibility(View.VISIBLE);
									attachment4.setVisibility(View.VISIBLE);
								}
							}*/
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else if(resultCode==RESULT_CANCELED)
				{
					if (gender_key.equalsIgnoreCase("boy")) {
						if (camera_key.equalsIgnoreCase("1")) {
							image_str="";
							bsae64_1="";
						} /*else if (camera_key.equalsIgnoreCase("2")) {
							image_str2="";
							bsae64_2="";
						} else if (camera_key.equalsIgnoreCase("3")) {
							image_str3="";
							bsae64_3="";
						} else if (camera_key.equalsIgnoreCase("4")) {
							image_str4="";
							bsae64_4="";
						}*/
					} else if (gender_key.equalsIgnoreCase("girl")) {
						if (camera_key.equalsIgnoreCase("5")) {
							image_str5="";
							bsae64_5="";
						} /*else if (camera_key.equalsIgnoreCase("6")) {
							image_str6="";
							bsae64_6="";
						} else if (camera_key.equalsIgnoreCase("7")) {
							image_str7="";
							bsae64_7="";
						} else if (camera_key.equalsIgnoreCase("8")) {

							image_str8="";
							bsae64_8="";
						}*/
					}
				}
				
			}

			if (requestCode == 1) {
				
				if(resultCode==RESULT_OK)
				{
					String ff = getRealPathFromURI(mUri).toString();

					if (gender_key.equalsIgnoreCase("boy")) {
						if (camera_key.equalsIgnoreCase("1")) {
							image_str = ff;
							if (image_str != null) {
								 bitmap11 = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
								 bsae64_1 = getStringImage(bitmap11);
								attachments_b.setText(image_str);
								//attachment2_b.setVisibility(View.VISIBLE);
								//attachment3_b.setVisibility(View.GONE);
								//attachment4_b.setVisibility(View.GONE);
							}
						}/* else if (camera_key.equalsIgnoreCase("2")) {
							image_str2 = ff;
							if (image_str2 != null) {
								 bitmap12 = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
								 bsae64_2 = getStringImage(bitmap12);
								attachments_b2.setText(image_str2);
								attachment2_b.setVisibility(View.VISIBLE);
								attachment3_b.setVisibility(View.VISIBLE);
								attachment4_b.setVisibility(View.GONE);
							}
						} else if (camera_key.equalsIgnoreCase("3")) {
							image_str3 = ff;
							if (image_str3 != null) {
								 bitmap13 = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
								 bsae64_3 = getStringImage(bitmap13);
								attachments_b3.setText(image_str3);
								attachment2_b.setVisibility(View.VISIBLE);
								attachment3_b.setVisibility(View.VISIBLE);
								attachment4_b.setVisibility(View.VISIBLE);
							}
						} else if (camera_key.equalsIgnoreCase("4")) {
							image_str4 = ff;
							if (image_str4 != null) {
								 bitmap14 = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
								 bsae64_4 = getStringImage(bitmap14);
								attachments_b4.setText(image_str4);
								attachment2_b.setVisibility(View.VISIBLE);
								attachment3_b.setVisibility(View.VISIBLE);
								attachment4_b.setVisibility(View.VISIBLE);
							}
						}*/
					} else {
						if (camera_key.equalsIgnoreCase("5")) {
							image_str5 = ff;
							if (image_str5 != null) {
								 bitmap15 = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
								 bsae64_5 = getStringImage(bitmap15);
								attachments_g.setText(image_str5);
								//attachment2.setVisibility(View.VISIBLE);
								//attachment3.setVisibility(View.GONE);
								//attachment4.setVisibility(View.GONE);
							}
						} /*else if (camera_key.equalsIgnoreCase("6")) {
							image_str6 = ff;
							if (image_str6 != null) {
								 bitmap16 = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
								 bsae64_6 = getStringImage(bitmap16);
								attachments_g2.setText(image_str6);
								attachment2.setVisibility(View.VISIBLE);
								attachment3.setVisibility(View.VISIBLE);
								attachment4.setVisibility(View.GONE);
							}
						} else if (camera_key.equalsIgnoreCase("7")) {
							image_str7 = ff;
							if (image_str7 != null) {
								 bitmap17 = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
								 bsae64_7 = getStringImage(bitmap17);
								attachments_g3.setText(image_str7);
								attachment2.setVisibility(View.VISIBLE);
								attachment3.setVisibility(View.VISIBLE);
								attachment4.setVisibility(View.VISIBLE);
							}
						} else if (camera_key.equalsIgnoreCase("8")) {
							image_str8 = ff;
							if (image_str8 != null) {
								 bitmap18 = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
								 bsae64_8 = getStringImage(bitmap18);
								attachments_g4.setText(image_str8);
								attachment2.setVisibility(View.VISIBLE);
								attachment3.setVisibility(View.VISIBLE);
								attachment4.setVisibility(View.VISIBLE);
							}
						}*/
					}
				}
				else if(resultCode==RESULT_CANCELED)
				{

					if (gender_key.equalsIgnoreCase("boy")) {
						if (camera_key.equalsIgnoreCase("1")) {
							image_str="";
							bsae64_1="";
						}/* else if (camera_key.equalsIgnoreCase("2")) {
							image_str2="";
							bsae64_2="";
						} else if (camera_key.equalsIgnoreCase("3")) {
							image_str3="";
							bsae64_3="";
						} else if (camera_key.equalsIgnoreCase("4")) {
							image_str4="";
							bsae64_4="";
						}*/
					} else if (gender_key.equalsIgnoreCase("girl")) {
						if (camera_key.equalsIgnoreCase("5")) {
							image_str5="";
							bsae64_5="";
						} /*else if (camera_key.equalsIgnoreCase("6")) {
							image_str6="";
							bsae64_6="";
						} else if (camera_key.equalsIgnoreCase("7")) {
							image_str7="";
							bsae64_7="";
						} else if (camera_key.equalsIgnoreCase("8")) {

							image_str8="";
							bsae64_8="";
						}*/
					}
				
				}
					
				

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ====================== above function finished here
	// ==========================//

	///================================================================//
	//==========Function to convert the image into Base64 string=======//
	//================================================================//

	  public String getStringImage(Bitmap bmp){
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        bmp.compress(Bitmap.CompressFormat.JPEG, 40, baos);
	        byte[] imageBytes = baos.toByteArray();
	        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
	        return encodedImage;
	    }
	// ======= Click function for Spinners
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		switch (arg0.getId()) {
		case R.id.HRs_edit:
			get_boy_hrs = HRs_edit.getSelectedItem().toString();
			break;
		case R.id.min_edit:
			get_boy_min = min_edit.getSelectedItem().toString();
			break;
		case R.id.HRs_edit_g:
			get_girl_hrs = HRs_edit_g.getSelectedItem().toString();
			break;
		case R.id.min_edit_g:
			get_girl_min = min_edit_g.getSelectedItem().toString();
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

	// ===========================================================================//
	// ======================= Match Making loader here
	// =========================//
	// =========================================================================//

	class Match_making extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress;
		private String match_making_url;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(MatchMaking.this);
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
			match_making_url = Constants.MATCH_MAKING;
			System.out.println("====image one==" + image_str);
			System.out.println("====image two==" + image_str5);
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(match_making_url);
			try {
				MultipartEntity entity = new MultipartEntity();
				if (image_str.equalsIgnoreCase("")) {
				} else {
					entity.addPart("image", new StringBody(bsae64_1));
					//entity.addPart("image", new FileBody(file1));
				}
				if (image_str5.equalsIgnoreCase("")) {
				} else {
					entity.addPart("image1", new StringBody(bsae64_5));
					//entity.addPart("image1", new FileBody(file2));
				}
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
				if (image_str5.equalsIgnoreCase("")) {
				} else {
					entity.addPart("image4", new StringBody(bsae64_5));
					//entity.addPart("image4", new FileBody(file5));
				}
				/*if (image_str6.equalsIgnoreCase("")) {
				} else {
					entity.addPart("image5", new StringBody(bsae64_6));
					//entity.addPart("image5", new FileBody(file6));
				}
				if (image_str7.equalsIgnoreCase("")) {
				} else {
					entity.addPart("image6", new StringBody(bsae64_7));
					//entity.addPart("image6", new FileBody(file7));
				}
				if (image_str8.equalsIgnoreCase("")) {
				} else {
					entity.addPart("image7", new StringBody(bsae64_8));
					//entity.addPart("image7", new FileBody(file8));
				}*/
				entity.addPart("user_id", new StringBody(loginId));
				entity.addPart("boy_fullname", new StringBody(get_boy_name));
				entity.addPart("boy_dob", new StringBody(get_boy_date));
				entity.addPart("boy_birth_place", new StringBody(get_boy_place));
				entity.addPart("boy_birth_hour", new StringBody(get_boy_hrs));
				entity.addPart("boy_birth_mint", new StringBody(get_boy_min));
				entity.addPart("girl_fullname", new StringBody(get_girl_name));
				entity.addPart("girl_dob", new StringBody(get_gir_date));
				entity.addPart("girl_birth_place", new StringBody(get_gril_place));
				entity.addPart("girl_birth_hour", new StringBody(get_girl_hrs));
				entity.addPart("girl_birth_mint", new StringBody(get_girl_min));

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
					
					if (get_gender.equalsIgnoreCase("Male")) {

						full_name_edit_g.setText("");
						date_edit_g.setText("");
						Place_edit_g.setText("");
						HRs_edit_g.setSelection(0);
						min_edit_g.setSelection(0);
						image_str="";
						bsae64_1="";
						attachments_b.setText("");
						image_str="";
						bsae64_5="";
						attachments_g.setText("");
						///===============
						
					} else if (get_gender.equalsIgnoreCase("Female")) {
						
						full_name_edit.setText("");
						date_edit.setText("");
						Place_edit.setText("");
						HRs_edit.setSelection(0);
						min_edit.setSelection(0);
						image_str="";
						bsae64_5="";
						attachments_g.setText("");
						image_str="";
						bsae64_1="";
						attachments_b.setText("");
					}
					progress.dismiss();
					/*Intent i = new Intent(MatchMaking.this, MatchMaking.class);
					startActivity(i);
					overridePendingTransition(0, 0);
					finish();*/
				} else {
					progress.dismiss();
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

}
