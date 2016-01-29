package com.elite.rasiyog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.elite.rasiyog.Utilities.Constants;
import com.elite.rasiyog.Utilities.ImageFileManipulation;
import com.elite.rasiyog.Utilities.Utils;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;

public class Profile_Edit extends Activity implements OnClickListener, OnItemSelectedListener {
	
	Uri mUri;
	ProgressDialog progress;
	TextView user_name;
	Bitmap bitmap2;
	SharedPreferences myPref;
	ImageView add_image,save_btn,user_image,back,calnder;
	Spinner HRs_edit,min_edit,gen_edit;
	ArrayList<String> hours_list = new ArrayList<String>();
	ArrayList<String> min_list = new ArrayList<String>();
	ArrayList<String> gender_list = new ArrayList<String>();
	ArrayAdapter<String> hours_adapter,min_adpater,gender_adpter;
	EditText name_edit,DOB_edit,place_edit,phone_edit,email_edit,password_edit;
	String name,dob,place_of_birth,birth_hour,birth_mint,phone_number,email,image,loginId,image_str="",fileName,error;
	String id,name_j,dob_j,place_of_birth_j,birth_hour_j,birth_mint_j,phone_number_j,email_j,password_j,image_j;
	String get_name,get_date,get_hrs,get_min,get_place,get_email,get_phone,Calenderformat="",get_image,get_hr,get_min1,get_gender;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_profile__edit);
		//get the login user id from Preference
		myPref = PreferenceManager.getDefaultSharedPreferences(Profile_Edit.this);
		loginId = myPref.getString("User_login", "");
		System.out.println("----login-id----" + loginId);
		// Find the view id's from xml
		find_id();
		//Call the get profile loader here
		if(Utils.isNetWorking(getApplicationContext()))
		{
			new Get_profile_loader().execute();
		}
		else
		{
			Utils.show_Toast(getApplicationContext(), "Internet Not Available");
		}
	}
	
	private void find_id() {
		//Textview
		user_name = (TextView) findViewById(R.id.user_name);
		//Imageview
		add_image = (ImageView) findViewById(R.id.add_image);
		save_btn = (ImageView) findViewById(R.id.save_btn);
		user_image = (ImageView) findViewById(R.id.user_image);
		
		calnder = (ImageView) findViewById(R.id.calnder);
		back = (ImageView) findViewById(R.id.back);
		//edittext
		name_edit = (EditText) findViewById(R.id.name_edit);
		DOB_edit = (EditText) findViewById(R.id.DOB_edit);
		place_edit = (EditText) findViewById(R.id.place_edit);
		phone_edit = (EditText) findViewById(R.id.phone_edit);
		email_edit = (EditText) findViewById(R.id.email_edit);
		//Spinners
		HRs_edit = (Spinner) findViewById(R.id.HRs_edit);
		min_edit = (Spinner) findViewById(R.id.min_edit);
		gen_edit = (Spinner) findViewById(R.id.gen_edit);
		//setvalues on Spinners
		setvalues();
		//Apply Click Listnere on Views
		add_image.setOnClickListener(this);
		user_image.setOnClickListener(this);
		back.setOnClickListener(this);
		save_btn.setOnClickListener(this);
		DOB_edit.setOnClickListener(this);
		calnder.setOnClickListener(this);
		HRs_edit.setOnItemSelectedListener(this);
		min_edit.setOnItemSelectedListener(this);
		gen_edit.setOnItemSelectedListener(this);
		
	}
	
	//====================Function to set the values on the Spinners====================//
	private void setvalues() {
		//Add values for Hours Spinner
		for (int i = 0; i <= 23; i++) {
			
			if (i <= 9) {
				if(i==0)
				{
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
				if(i==0)
			{
				min_list.add(String.valueOf("Select Minutes"));
			}

				min_list.add(String.valueOf("0" + i));
			} else {
				min_list.add(String.valueOf(i));

			}

		}
		//Add values for genderSpinner
		gender_list.add("Select Gender");
		gender_list.add("Male");
		gender_list.add("Female");		
		//set adapter on the Hours Spinner
		hours_adapter = new ArrayAdapter<String>(Profile_Edit.this, R.layout.spinner_view, R.id.text_main_seen,hours_list);
		hours_adapter.setDropDownViewResource(R.layout.spinner_view3);
		HRs_edit.setAdapter(hours_adapter);
		//set adapter on the Hours Spinner
		min_adpater = new ArrayAdapter<String>(Profile_Edit.this, R.layout.spinner_view, R.id.text_main_seen,min_list);
		min_adpater.setDropDownViewResource(R.layout.spinner_view3);
		min_edit.setAdapter(min_adpater);
		//set adpter on the Gender Spinner
	    gender_adpter = new ArrayAdapter<String>(Profile_Edit.this, R.layout.spinner_view, R.id.text_main_seen,gender_list);
	    gender_adpter.setDropDownViewResource(R.layout.spinner_view3);
	    gen_edit.setAdapter(gender_adpter);
		
	}
	//====================Above function finished here ====================//
	
	
	
	// ===========================================================================//
	// ======================= Get Profile  loader here =========================//
	// =========================================================================//
			
	class Get_profile_loader extends AsyncTask<String, Void, String> {
	private String get_profile;
	private URI uri;
	private String result;

	@Override
	protected void onPreExecute() {
	super.onPreExecute();
	try {
	progress = new ProgressDialog(Profile_Edit.this);
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

	get_profile = Constants.GET_PROFILE+"id="+loginId;
	System.out.println("--get_profile--" + get_profile);
	try {
	uri = new URI(get_profile.replace(' ', '+'));
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
	JSONObject obj1 = jsonA.optJSONObject("user");
	id = obj1.optString("id");
	name_j = obj1.optString("name");
	dob_j = obj1.optString("dob");
	get_gender = obj1.optString("gender");
	place_of_birth_j = obj1.optString("place_of_birth");
	birth_hour_j = obj1.optString("birth_hour");
	birth_mint_j = obj1.optString("birth_mint");
	phone_number_j = obj1.optString("phone_number");
	email_j = obj1.optString("email");
	password_j = obj1.optString("password");
	image_j = obj1.optString("image");
	
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
		user_name.setText(name_j);
		name_edit.setText(name_j);
		DOB_edit.setText(dob_j);
		place_edit.setText(place_of_birth_j);
		phone_edit.setText(phone_number_j);
		email_edit.setText(email_j);
    	Picasso.with(getApplicationContext()).load(image_j).transform(new RoundedTransformation(50, 4))
								.resize(110, 110)
								.centerCrop().into(user_image);
    	// Set values in Hours Spinner
    	for (int i = 0; i < hours_adapter.getCount(); i++) {
			if (birth_hour_j.trim().equals(hours_adapter.getItem(i).toString())) {
				HRs_edit.setSelection(i);
				break;
			}
		}
    	// Set values in Min Spinner
    	for (int i = 0; i < min_adpater.getCount(); i++) {
			if (birth_mint_j.trim().equals(min_adpater.getItem(i).toString())) {
				min_edit.setSelection(i);
				break;
			}
		}
    	
    	if(get_gender.equalsIgnoreCase(""))
    	{}
    	else
    	{
    	for (int i = 0; i < gender_adpter.getCount(); i++) {
			if (get_gender.trim().equals(gender_adpter.getItem(i).toString())) {
				gen_edit.setSelection(i);
				break;
			}
		}
    	}
		progress.dismiss();
	} catch (Exception e) {
		e.printStackTrace();
	}
		}
			}
	//==================== above loader finished here =============================//


  //================== Click Listener Function On view ======================//
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_image:
			openCamera();
			break;
			
		case R.id.user_image:
			openCamera();
			break;
		case R.id.calnder:
			DateID();
			break;
			
		case R.id.DOB_edit:
			DateID();
			break;
			
		case R.id.back:
			onBackPressed();
			break;
			
		case R.id.save_btn:
			get_name = name_edit.getText().toString();
			get_date = DOB_edit.getText().toString();
			get_place = place_edit.getText().toString();
			get_phone = phone_edit.getText().toString();
			get_email = email_edit.getText().toString();
			
			if(get_name.equalsIgnoreCase(""))
			{
				Utils.show_Toast(getApplicationContext(), "Please Enter Your Name");
				name_edit.requestFocus();
			}
			else if(get_date.equalsIgnoreCase(""))
			{
				Utils.show_Toast(getApplicationContext(), "Please Enter Date");
				DOB_edit.requestFocus();
			}
			else if (get_hr.equalsIgnoreCase("Select Hours")) {
				Utils.show_Toast(getApplicationContext(), "Please Select Hours");
			} else if (get_min1.equalsIgnoreCase("Select Minutes")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Select Minutes");
			} 
			else if(get_place.equalsIgnoreCase(""))
			{
				Utils.show_Toast(getApplicationContext(), "Please Enter Birth Place");
				place_edit.requestFocus();
			}
			else if(get_gender.equalsIgnoreCase("Select Gender"))
			{
				Utils.show_Toast(getApplicationContext(), "Please Select Gender");
			}
			else if(get_phone.equalsIgnoreCase(""))
			{
				Utils.show_Toast(getApplicationContext(), "Please Enter Phone Number");
				phone_edit.requestFocus();
			}
			else if (get_phone.contains(" ")) {
				Utils.show_Toast(getApplicationContext(),
						"Please Enter Correct   Phone Number");
			}
			else if(get_phone.length()<10)
			{
				Utils.show_Toast(getApplicationContext(), "Please Enter 10 Digits Phone Number");
			}
			else if(get_email.equalsIgnoreCase(""))
			{
				Utils.show_Toast(getApplicationContext(), "Please Enter Email Address");
				email_edit.requestFocus();
			}
			else if(Utils.isNetWorking(getApplicationContext()))
			{
				new Edit_profile().execute();
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
	//===================== Above function Finish Here========================//
	

	
	private void DateID() {
		// TODO Auto-generated method stub
		final Calendar c = Calendar.getInstance();
		//int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		DatePickerDialog dpd = new DatePickerDialog(Profile_Edit.this,
				new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view,int year, int monthOfYear,int dayOfMonth) {
				DOB_edit.setText(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year );
				//here we get the date selected from the date picker
				Calenderformat=dayOfMonth+"-"+(monthOfYear + 1)+"-"+year;
				System.out.println("===========DAte===="+Calenderformat);
			}
		}, 1945, mMonth, mDay);
		dpd.show();

	}

	// ================= select image form gallery and camera function here======================//
	private void openCamera() {
		runOnUiThread(new Runnable() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				AlertDialog alertDialog = new AlertDialog.Builder(Profile_Edit.this).create();
				alertDialog.setTitle("Select resource" + "");
				// alertDialog.setIcon(R.drawable.app_icon100);
				alertDialog.setMessage("");
				alertDialog.setCancelable(true);
				alertDialog.setButton("Gallery", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							startActivityForResult(Intent.createChooser(intent,"Complete action using"), 1234);
						} catch (ActivityNotFoundException e) {
							e.printStackTrace();
						}
					}
				});
				alertDialog.setButton2("Camera", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						mUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "pic_"+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
						intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
						startActivityForResult(intent, 1);
					}
				});
				alertDialog.show();

			}
		});

	}
	@SuppressWarnings("deprecation")
	public String getRealPathFromURI(Uri contentUri)
	{
		try
		{
			String[] proj = {MediaStore.Images.Media.DATA};
			Cursor cursor = managedQuery(contentUri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			System.out.println("column_index of selected image is:"+column_index);
			cursor.moveToFirst();
			System.out.println("selected image path is:"+cursor.getString(column_index));
			return cursor.getString(column_index);
		}
		catch (Exception e)
		{
			return contentUri.getPath();
		}
	}
	Bitmap photo;
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try{
			if (requestCode == 1234) {
				Bitmap  bitmap2 = null;
				try{
					Uri selectedImageUri = data.getData();
					System.out.println("uriiiiiiii"+selectedImageUri);
					image_str=getRealPathFromURI(selectedImageUri);
					System.out.println("image" + image_str);
					if(image_str!=null)
					{
						ImageFileManipulation objImageFileManipulation = new ImageFileManipulation(Profile_Edit.this);
						try {
							bitmap2 = ThumbnailUtils.extractThumbnail(objImageFileManipulation.getThumbnail(selectedImageUri, 100), 100, 100);
							ByteArrayOutputStream stream= new ByteArrayOutputStream();
							bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, stream);
						    try {
						    	user_image.setScaleType(ScaleType.FIT_XY);
								Picasso.with(getApplicationContext()).load(selectedImageUri).transform(new RoundedTransformation(50, 4))
								.resize(100, 100)
								.centerCrop().into(user_image);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}       
							//Imageview1.setImageBitmap(bitmap2);
							System.out.println("====image_str---" + image_str);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		
			if(requestCode==1) {
				
				image_str =getRealPathFromURI(mUri).toString();
				System.out.println("====ff"+image_str);
				
				ImageFileManipulation objImageFileManipulation = new ImageFileManipulation(Profile_Edit.this);
				try {
					bitmap2 = ThumbnailUtils.extractThumbnail(objImageFileManipulation.getThumbnail(mUri, 100), 100, 100);
					ByteArrayOutputStream stream= new ByteArrayOutputStream();
					((Bitmap) bitmap2).compress(Bitmap.CompressFormat.JPEG, 100, stream);
				    try {
				    	user_image.setScaleType(ScaleType.FIT_XY);
						Picasso.with(getApplicationContext()).load(mUri).transform(new RoundedTransformation(50, 4))
						.resize(100, 100)
						.centerCrop().into(user_image);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    //Imageview1.setImageBitmap(res);        ;
					System.out.println("====image_str---" + image_str);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
    //=====================   above function finished here =====================//
	
	//==================================================================//
    //====================Edit profile loader Start===========================//
	//================================================================//
				class Edit_profile extends AsyncTask<Void, Void, Void> {
					private ProgressDialog progress;
					private String url;
					private String id;

					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						progress = new ProgressDialog(Profile_Edit.this);
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
						 url=Constants.EDIT_PROFILE;
						 System.out.println("====url=pp=="+url);
					     HttpClient httpclient = new DefaultHttpClient();
				         HttpPost httppost = new HttpPost(url);
			        try {
			        	MultipartEntity entity = new MultipartEntity();
			        	if(image_str.equalsIgnoreCase(""))
			        	{
			        	}
			        	else
			        	{
			        		File file1  = new File(image_str);
			        		entity.addPart("image", new FileBody(file1));
			        	}
			        	entity.addPart("id", new StringBody(loginId));
		        		entity.addPart("username", new StringBody(get_name));
		        		entity.addPart("email", new StringBody(get_email));
		        		entity.addPart("phoneno", new StringBody(get_phone));
		        		entity.addPart("dob", new StringBody(get_date));
		        		entity.addPart("pob", new StringBody(get_place));
		        		entity.addPart("gender", new StringBody(get_gender));
		        		entity.addPart("hrs", new StringBody(get_hr));
		        		entity.addPart("min", new StringBody(get_min1));
			        	httppost.setEntity(entity);
			        	System.out.println("------------entity----------"+entity);
			        	HttpResponse response = httpclient.execute(httppost);
			        	System.out.println("------------response----------"+response);
			        	HttpEntity resEntity = response.getEntity();
			        	System.out.println("========resEntity==========="+resEntity.toString());
			        	BufferedReader reader = new BufferedReader(new InputStreamReader(
			            response.getEntity().getContent(), "UTF-8"));
			            String sResponse;
			            StringBuilder s = new StringBuilder();
			              while ((sResponse = reader.readLine()) != null) {
			                  s = s.append(sResponse);
			              }
			              System.out.println("Response: " + s);
			              System.out.println("-----s----" + s);
			              JSONObject hh = new JSONObject(s.toString());
			              System.out.println("===final result=="+hh);
			              id = hh.optString("success");
			              get_image = hh.optString("image");
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
							 SharedPreferences.Editor editor11 = myPref.edit();
						     editor11.putString("image",get_image);
						     editor11.putString("dob",get_date);
						     editor11.putString("pob",get_place);
						     editor11.putString("email",get_email);
						     editor11.putString("hrs",get_hr);
						     editor11.putString("gender",get_gender);
						     editor11.putString("min",get_min1);
						     editor11.putString("name",get_name);
						     editor11.putString("phn",get_phone);
						     editor11.commit();
						Intent i = new Intent(getApplicationContext(),User_profile.class);
						startActivity(i);
						overridePendingTransition(0, 0);
						Utils.show_Toast(getApplicationContext(), "Profile Edit Successfully");
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
				//=========================================================================//
			    //==================== Proflie Edit loader finish =========================//
				//========================================================================//


				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					switch (arg0.getId()) {
					case R.id.HRs_edit:
						get_hr = HRs_edit.getSelectedItem().toString();
						break;
					case R.id.min_edit:
						get_min1 = min_edit.getSelectedItem().toString();
						break;
						
					case R.id.gen_edit:
						get_gender = gen_edit.getSelectedItem().toString();
						break;

					default:
						break;
					}
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
	

}
