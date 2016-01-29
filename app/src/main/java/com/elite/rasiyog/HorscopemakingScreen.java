package com.elite.rasiyog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.payu.india.Extras.PayUChecksum;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.payuui.PayUBaseActivity;

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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class HorscopemakingScreen extends Activity implements OnClickListener,
        OnItemSelectedListener {

    SimpleSideDrawer mNav;
    Spinner gen_edit, country, HRs_edit, min_edit;
    EditText fullname_edit, date_edit2, email_g, Place_edit, attachments_g,
            attachments_g2, attachments_g3, attachments_g4, name_edit,
            address1, address2, city_edit, state_edit, pin, phone_edit;
    ArrayList<String> dummy = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ImageView submit, attach_btn_g, attach_btn_g2, attach_btn_g3,
            attach_btn_g4, cancel_h, submit_d, calnder2;
    Dialog dialog;
    TextView About_txt, Change_txt;
    ProgressDialog progress;
    Uri mUri;
    RelativeLayout attachment1, attachment2, attachment3, attachment4;
    public static String image_str = "", image_str2 = "", image_str3 = "",
            image_str4 = "", camera_key, description;
    ArrayList<String> gender_list = new ArrayList<String>();
    ArrayList<String> hours_list = new ArrayList<String>();
    ArrayList<String> min_list = new ArrayList<String>();
    ArrayAdapter<String> hours_adapter, min_adpater, gender_adpter;
    SharedPreferences myPref;

    String loginId, get_active, error, get_full_name, get_gender, get_date, get_place,
            get_hrs, get_min, get_email, get_address1, get_address2, get_city,
            get_state, get_pin, get_phone, success;

    private Bitmap bitmap11, bitmap12;//,bitmap13,bitmap14;
    String bsae64_1 = "", bsae64_2 = "", bsae64_3 = "", bsae64_4 = "";
    String product_name, paypal_id;

    // =============Paypal values

    private static final String TAG = "paymentExample";
    /**
     * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
     * <p/>
     * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test
     * credentials from https://developer.paypal.com
     * <p/>
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

    //=========================================================//
    Intent intent;
    private PayUChecksum checksum;
    private PostData postData;
    private String key;
    private String salt;
    private String var1;
    //    private mPaymentParams mPaymentParams;
    private PaymentParams mPaymentParams;
    private PayuConfig payuConfig;
    private String cardBin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_horscopemaking_screen);


      // Getting the value from Preferences
        myPref = PreferenceManager
                .getDefaultSharedPreferences(HorscopemakingScreen.this);
        loginId = myPref.getString("User_login", "");
        get_full_name = myPref.getString("name", "");
        get_gender = myPref.getString("gender", "");
        get_date = myPref.getString("dob", "");
        get_place = myPref.getString("pob", "");
        get_hrs = myPref.getString("hrs", "");
        get_min = myPref.getString("min", "");
        get_email = myPref.getString("email", "");
        get_active = myPref.getString("active", "");
        image_str = "";
        image_str2 = "";
        image_str3 = "";
        image_str4 = "";
        mUri = null;

        find_id();

        SpannableString content = new SpannableString("RESET");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        Change_txt.setText(content);
        setvalues();
        set_old_value();

        mNav = new SimpleSideDrawer(this);
        Slidermenu side = new Slidermenu(mNav, HorscopemakingScreen.this);
        side.setDrawer();
        findViewById(R.id.slider).setVisibility(View.GONE);
        mNav = new SimpleSideDrawer(this);
        Slidermenu side1 = new Slidermenu(mNav, HorscopemakingScreen.this);
        side1.setDrawer();
        try {
            get_active = getIntent().getStringExtra("status");

            if (get_active.equalsIgnoreCase("Ho")) {
                findViewById(R.id.horoscope).setBackgroundResource(R.drawable.menu_highlit);
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

    private void set_old_value() {
        fullname_edit.setText(get_full_name);
        date_edit2.setText(get_date);
        email_g.setText(get_email);
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
        // Set values in Gender Spinner
        for (int i = 0; i < gender_adpter.getCount(); i++) {
            if (get_gender.trim().equals(gender_adpter.getItem(i).toString())) {
                gen_edit.setSelection(i);
                break;
            }
        }

    }

    private void find_id() {
        // Text view
        About_txt = (TextView) findViewById(R.id.About_txt);
        Change_txt = (TextView) findViewById(R.id.Change_txt);
        // Spinner
        gen_edit = (Spinner) findViewById(R.id.gen_edit);
        HRs_edit = (Spinner) findViewById(R.id.HRs_edit);
        min_edit = (Spinner) findViewById(R.id.min_edit);
        // Edit Text
        attachments_g = (EditText) findViewById(R.id.attachments_g);
        attachments_g2 = (EditText) findViewById(R.id.attachments_g2);
        attachments_g3 = (EditText) findViewById(R.id.attachments_g3);
        attachments_g4 = (EditText) findViewById(R.id.attachments_g4);
        fullname_edit = (EditText) findViewById(R.id.fullname_edit);
        date_edit2 = (EditText) findViewById(R.id.date_edit2);
        email_g = (EditText) findViewById(R.id.email_g);
        Place_edit = (EditText) findViewById(R.id.Place_edit);
        // Image view
        attach_btn_g = (ImageView) findViewById(R.id.attach_btn_g);
        attach_btn_g2 = (ImageView) findViewById(R.id.attach_btn_g2);
        attach_btn_g3 = (ImageView) findViewById(R.id.attach_btn_g3);
        attach_btn_g4 = (ImageView) findViewById(R.id.attach_btn_g4);
        submit = (ImageView) findViewById(R.id.submit);
        calnder2 = (ImageView) findViewById(R.id.calnder2);
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
        // Apply Click Listener
        submit.setOnClickListener(this);
        attach_btn_g.setOnClickListener(this);
        attach_btn_g2.setOnClickListener(this);
        attach_btn_g3.setOnClickListener(this);
        attach_btn_g4.setOnClickListener(this);
        date_edit2.setOnClickListener(this);
        Change_txt.setOnClickListener(this);
        calnder2.setOnClickListener(this);

        gen_edit.setOnItemSelectedListener(this);
        HRs_edit.setOnItemSelectedListener(this);
        min_edit.setOnItemSelectedListener(this);
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
        hours_adapter = new ArrayAdapter<String>(HorscopemakingScreen.this,
                R.layout.spinner_view, R.id.text_main_seen, hours_list);
        hours_adapter.setDropDownViewResource(R.layout.spinner_view3);
        HRs_edit.setAdapter(hours_adapter);
        // set adpter on the Hours Spinner
        min_adpater = new ArrayAdapter<String>(HorscopemakingScreen.this,
                R.layout.spinner_view, R.id.text_main_seen, min_list);
        min_adpater.setDropDownViewResource(R.layout.spinner_view3);
        min_edit.setAdapter(min_adpater);
        // set adpter on the Gender Spinner
        gender_adpter = new ArrayAdapter<String>(HorscopemakingScreen.this,
                R.layout.spinner_view, R.id.text_main_seen, gender_list);
        gender_adpter.setDropDownViewResource(R.layout.spinner_view3);
        gen_edit.setAdapter(gender_adpter);
    }

    // ==================Click gunction for buttons or
    // imageviews==============================///
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.submit:
                get_full_name = fullname_edit.getText().toString();
                get_date = date_edit2.getText().toString();
                get_place = Place_edit.getText().toString();
                get_email = email_g.getText().toString();

                if (get_full_name.equalsIgnoreCase("")) {
                    Utils.show_Toast(getApplicationContext(),
                            "Please Enter Your Name");
                    fullname_edit.requestFocus();
                } else if (get_date.equalsIgnoreCase("")) {
                    Utils.show_Toast(getApplicationContext(), "Please Select Date");
                    date_edit2.requestFocus();
                } else if (get_gender.equalsIgnoreCase("Select Gender")) {
                    Utils.show_Toast(getApplicationContext(),
                            "Please Select Gender");

                } else if (get_hrs.equalsIgnoreCase("Select Hours")) {
                    Utils.show_Toast(getApplicationContext(),
                            "Please Select Hours");

                } else if (get_min.equalsIgnoreCase("Select Minutes")) {
                    Utils.show_Toast(getApplicationContext(),
                            "Please Select Minutes");
                } else if (get_place.equalsIgnoreCase("")) {
                    Utils.show_Toast(getApplicationContext(),
                            "Please Enter Birth Place");
                    Place_edit.requestFocus();
                } else if (get_email.equalsIgnoreCase("")) {
                    Utils.show_Toast(getApplicationContext(),
                            "Please Enter Email Address");

                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(get_email).matches()) {
                    Utils.show_Toast(getApplicationContext(), "Please Enter Valid Email Address");
                } else if (image_str.equalsIgnoreCase("")) {
                    Utils.show_Toast(getApplicationContext(),
                            "Please Add At Least One Attachment");
                } else {
                    dialog = new Dialog(HorscopemakingScreen.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.photo_view_lyout);
                    dialog.show();
                    cancel_h = (ImageView) dialog.findViewById(R.id.cancle);
                    submit_d = (ImageView) dialog.findViewById(R.id.submit);
                    name_edit = (EditText) dialog.findViewById(R.id.name_edit);
                    address1 = (EditText) dialog.findViewById(R.id.address1);
                    address2 = (EditText) dialog.findViewById(R.id.address2);
                    city_edit = (EditText) dialog.findViewById(R.id.city_edit);
                    state_edit = (EditText) dialog.findViewById(R.id.state_edit);
                    pin = (EditText) dialog.findViewById(R.id.pin);
                    phone_edit = (EditText) dialog.findViewById(R.id.phone_edit);

                    name_edit.setText(get_full_name);

                    cancel_h.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    submit_d.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            get_address1 = address1.getText().toString();
                            get_address2 = address2.getText().toString();
                            get_city = city_edit.getText().toString();
                            get_state = state_edit.getText().toString();
                            get_pin = pin.getText().toString();
                            get_phone = phone_edit.getText().toString();
                            get_full_name = name_edit.getText().toString();

                            if (get_full_name.equalsIgnoreCase("")) {
                                Utils.show_Toast(getApplicationContext(),
                                        "Please Enter Name");
                            } else if (get_address1.equalsIgnoreCase("")) {
                                Utils.show_Toast(getApplicationContext(),
                                        "Please Enter Address 1");
                            } else if (get_address2.equalsIgnoreCase("")) {
                                Utils.show_Toast(getApplicationContext(),
                                        "Please Enter Address 2");
                            } else if (get_city.equalsIgnoreCase("")) {
                                Utils.show_Toast(getApplicationContext(),
                                        "Please Enter City");
                            } else if (get_state.equalsIgnoreCase("")) {
                                Utils.show_Toast(getApplicationContext(),
                                        "Please Enter State");
                            } else if (get_pin.equalsIgnoreCase("")) {
                                Utils.show_Toast(getApplicationContext(),
                                        "Please Enter Pin Code");
                            } else if (get_pin.length() < 6) {
                                Utils.show_Toast(getApplicationContext(),
                                        "Please Enter 6 Digits  Pin Code");
                            } else if (get_phone.equalsIgnoreCase("")) {
                                Utils.show_Toast(getApplicationContext(),
                                        "Please Enter Phone Number");
                            } else if (get_phone.contains(" ")) {
                                Utils.show_Toast(getApplicationContext(),
                                        "Please Enter Correct  Phone Number");
                            } else if (get_phone.length() < 10) {
                                Utils.show_Toast(getApplicationContext(),
                                        "Please Enter 10 Digits Phone Number");
                            } else {
                                dialog.dismiss();
                              new Horoscope_making().execute();
                            }

                        }
                    });

                }
                break;

            case R.id.date_edit2:
                DateID();
                break;
            case R.id.calnder2:
                DateID();
                break;

            case R.id.Change_txt:
                fullname_edit.setText("");
                date_edit2.setText("");
                Place_edit.setText("");
                email_g.setText("");
                HRs_edit.setSelection(0);
                min_edit.setSelection(0);
                gen_edit.setSelection(0);
                attachments_g.setText("");
                attachments_g2.setText("");
                image_str = "";
                image_str2 = "";
                image_str3 = "";
                image_str4 = "";
                mUri = null;
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

    // ===========================above function finish
    // here==============================//

    private void DateID() {
        // TODO Auto-generated method stub
        final Calendar c = Calendar.getInstance();
        // int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(
                HorscopemakingScreen.this,
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
                        HorscopemakingScreen.this).create();
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
            String[] proj = {MediaStore.Images.Media.DATA};
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
                if (resultCode == RESULT_OK) {
                    try {
                        Uri selectedImageUri = null;
                        selectedImageUri = data.getData();
                        System.out.println("uriiiiiiii" + selectedImageUri);
                        if (camera_key.equalsIgnoreCase("1")) {
                            bitmap11 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                            bsae64_1 = getStringImage(bitmap11);
                            image_str = getRealPathFromURI(selectedImageUri);
                            System.out.println("==image_str" + selectedImageUri);
                            if (image_str != null) {
                                attachments_g.setText(image_str);

                            }
                        } else if (camera_key.equalsIgnoreCase("2")) {

                            image_str2 = getRealPathFromURI(selectedImageUri);
                            System.out.println("===image_str2" + selectedImageUri);
                            if (image_str2 != null) {
                                bitmap12 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                                bsae64_2 = getStringImage(bitmap12);
                                attachments_g2.setText(image_str2);
                                attachment2.setVisibility(View.VISIBLE);

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    if (camera_key.equalsIgnoreCase("1")) {
                        image_str = "";
                        bsae64_1 = "";
                    } else if (camera_key.equalsIgnoreCase("2")) {
                        image_str2 = "";
                        bsae64_2 = "";

                    }
                    Toast.makeText(getApplicationContext(),
                            "User Cancelled Image Select", Toast.LENGTH_SHORT)
                            .show();
                }


                Bitmap bitmap2 = null;

            } else if (requestCode == 1) {
                if (resultCode == RESULT_OK) {
                    String ff = getRealPathFromURI(mUri).toString();
                    if (camera_key.equalsIgnoreCase("1")) {
                        image_str = ff;
                        if (image_str != null) {
                            bitmap11 = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
                            bsae64_1 = getStringImage(bitmap11);
                            attachments_g.setText(image_str);

                        }
                    } else if (camera_key.equalsIgnoreCase("2")) {
                        image_str2 = ff;
                        if (image_str2 != null) {
                            bitmap12 = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
                            bsae64_2 = getStringImage(bitmap12);
                            attachments_g2.setText(image_str2);
                            attachment2.setVisibility(View.VISIBLE);

                        }
                    }
                } else if (resultCode == RESULT_CANCELED) {

                    if (camera_key.equalsIgnoreCase("1")) {
                        image_str = "";
                        bsae64_1 = "";
                    } else if (camera_key.equalsIgnoreCase("2")) {
                        image_str2 = "";
                        bsae64_2 = "";

                    }
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

                            System.out.print("============g======"
                                    + g);
                            System.out.print("===========kkg======"
                                    + k);


                            product_name = k.optString("product_name");
                            paypal_id = g.optString("id");

                            System.out.println("===product name===" + product_name);
                            System.out.println("===product id===" + paypal_id);

                            Log.i(TAG, confirm.getPayment().toJSONObject()
                                    .toString(4));

                            Toast.makeText(
                                    getApplicationContext(),
                                    "PaymentConfirmation info received from PayPal",
                                    Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            if (Utils.isNetWorking(getApplicationContext())) {
                                new Horoscope_making().execute();
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
            }else  if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
                if(data != null ) {
                    new AlertDialog.Builder(this)
                            .setCancelable(false)
                            .setMessage(data.getStringExtra("result"))
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            }).show();
                }else{
                    Toast.makeText(this, "Could not receive data", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            try {

                MultipartEntity entity = new MultipartEntity();
                entity.addPart("user_id", new StringBody(loginId));
                entity.addPart("payment_method", new StringBody(product_name));
                entity.addPart("payment_id", new StringBody(paypal_id));
                entity.addPart("amount", new StringBody("2101"));
                entity.addPart("type", new StringBody("Horoscope"));
                httppost.setEntity(entity);

                HttpResponse response = httpclient.execute(httppost);

                HttpEntity resEntity = response.getEntity();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                response.getEntity().getContent(), "UTF-8"));
                String sResponse;
                StringBuilder s = new StringBuilder();
                while ((sResponse = reader.readLine()) != null) {
                    s = s.append(sResponse);
                }

                JSONObject hh = new JSONObject(s.toString());

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



    //====================stringgg image===================//

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    // ====================== above function finished he=========================//
    // ======================= Get About loader here =========================//


    class About_loader extends AsyncTask<String, Void, String> {
        private String about_url;
        private URI uri;
        private String result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                progress = new ProgressDialog(HorscopemakingScreen.this);
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

            about_url = Constants.ABOUT_URL + "id=9";

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

    // ==================== above loader finished here
    // ======================= Hororscope Making loader here
    // =========================================================================//

    class Horoscope_making extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress;
        private String url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(HorscopemakingScreen.this);
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
            url = Constants.HORODCOPE_MAKING_URL;
            System.out.println("====url=pp==" + url);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            try {
                MultipartEntity entity = new MultipartEntity();
                if (image_str.equalsIgnoreCase("")) {
                } else {
                    entity.addPart("image", new StringBody(bsae64_1));

                }

                entity.addPart("user_id", new StringBody(loginId));
                entity.addPart("username", new StringBody(get_full_name));
                entity.addPart("gender", new StringBody(get_gender));
                entity.addPart("dob", new StringBody(get_date));
                entity.addPart("pob", new StringBody(get_place));
                entity.addPart("phoneno", new StringBody(get_phone));
                entity.addPart("pin_code", new StringBody(get_pin));
                entity.addPart("address1", new StringBody(get_address1));
                entity.addPart("address2", new StringBody(get_address2));
                entity.addPart("birth_hour", new StringBody(get_hrs));
                entity.addPart("birth_min", new StringBody(get_min));
                entity.addPart("email", new StringBody(get_email));
                entity.addPart("city", new StringBody(get_city));
                entity.addPart("state", new StringBody(get_state));
                httppost.setEntity(entity);

                HttpResponse response = httpclient.execute(httppost);

                HttpEntity resEntity = response.getEntity();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                response.getEntity().getContent(), "UTF-8"));
                String sResponse;
                StringBuilder s = new StringBuilder();
                while ((sResponse = reader.readLine()) != null) {
                    s = s.append(sResponse);
                }

                JSONObject hh = new JSONObject(s.toString());

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

                    image_str = "";
                    image_str2 = "";
                    bsae64_1 = "";
                    bsae64_2 = "";
                    email_g.setText("");
                    attachments_g.setText("");
                    attachments_g2.setText("");
                    progress.dismiss();

                } else {
                    Utils.show_Toast(getApplicationContext(), "" + error);
                    progress.dismiss();
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

    }

    // ========================pay pal functions=========================//
    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal("31.21"), "USD", "Horoscope Making",
                paymentIntent);
    }

    public void onFuturePaymentPressed(View pressed) {
        Intent intent = new Intent(HorscopemakingScreen.this,
                PayPalFuturePaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
    }

    public void onProfileSharingPressed(View pressed) {
        Intent intent = new Intent(HorscopemakingScreen.this,
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

    //========================================================================//
    //======================== PAY U MONEY FINCTIONS =========================//
    //=======================================================================//

    private void navigateToBaseActivity(){
        intent = new Intent(this, PayUBaseActivity.class);

        mPaymentParams = new PaymentParams();
        payuConfig = new PayuConfig();

        mPaymentParams.setKey("0MQaQP");
        key = "0MQaQP";
        mPaymentParams.setAmount("2101.00");
        mPaymentParams.setProductInfo("HoroscopeMaking");
        mPaymentParams.setFirstName("Elite");
        mPaymentParams.setEmail("testingelitemohali2@gmail.com");
        mPaymentParams.setTxnId("1453118535292");
        mPaymentParams.setSurl("https://payu.herokuapp.com/success");
        mPaymentParams.setFurl("https://payu.herokuapp.com/failure");
        mPaymentParams.setUdf1("udf1");
        mPaymentParams.setUdf2("udf2");
        mPaymentParams.setUdf3("udf3");
        mPaymentParams.setUdf4("udf4");
        mPaymentParams.setUdf5("udf5");
        mPaymentParams.setUserCredentials("0MQaQP:payutest@payu.in");
        var1 = "0MQaQP:payutest@payu.in";
        mPaymentParams.setOfferKey("off");
        intent.putExtra(PayuConstants.SALT, "salt");
        salt = "salt";
        String environment = "0";
        payuConfig.setEnvironment(environment.contentEquals(""+PayuConstants.PRODUCTION_ENV) ? PayuConstants.PRODUCTION_ENV :PayuConstants.MOBILE_STAGING_ENV);
        cardBin = "elite";
        // generate hash from server
        // just a sample. Acturally Merchant should generate from his server.
        if(null == salt) generateHashFromServer(mPaymentParams);
        else generateHashFromSDK(mPaymentParams, intent.getStringExtra(PayuConstants.SALT));
    }


    /****************************** Server hash generation ********************************/
    // lets generate hashes from server
    public void generateHashFromServer(PaymentParams mPaymentParams){
        // lets create the post params

        StringBuffer postParamsBuffer = new StringBuffer();
        postParamsBuffer.append(concatParams(PayuConstants.KEY, mPaymentParams.getKey()));
        postParamsBuffer.append(concatParams(PayuConstants.AMOUNT, mPaymentParams.getAmount()));
        postParamsBuffer.append(concatParams(PayuConstants.TXNID, mPaymentParams.getTxnId()));
        postParamsBuffer.append(concatParams(PayuConstants.EMAIL, null == mPaymentParams.getEmail() ? "" : mPaymentParams.getEmail()));
        postParamsBuffer.append(concatParams(PayuConstants.PRODUCT_INFO, mPaymentParams.getProductInfo()));
        postParamsBuffer.append(concatParams(PayuConstants.FIRST_NAME, null == mPaymentParams.getFirstName() ? "" : mPaymentParams.getFirstName()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF1, mPaymentParams.getUdf1() == null ? "" : mPaymentParams.getUdf1()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF2, mPaymentParams.getUdf2() == null ? "" : mPaymentParams.getUdf2()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF3, mPaymentParams.getUdf3() == null ? "" : mPaymentParams.getUdf3()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF4, mPaymentParams.getUdf4() == null ? "" : mPaymentParams.getUdf4()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF5, mPaymentParams.getUdf5() == null ? "" : mPaymentParams.getUdf5()));
        postParamsBuffer.append(concatParams(PayuConstants.USER_CREDENTIALS, mPaymentParams.getUserCredentials() == null ? PayuConstants.DEFAULT : mPaymentParams.getUserCredentials()));

        // for offer_key
        if(null != mPaymentParams.getOfferKey())
            postParamsBuffer.append(concatParams(PayuConstants.OFFER_KEY, mPaymentParams.getOfferKey()));
        // for check_isDomestic
        if(null != cardBin)
            postParamsBuffer.append(concatParams("card_bin", cardBin));

        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();
        // make api call
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);
    }

    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }


    /****************************** Client hash generation ***********************************/
    // Do not use this, you may use this only for testing.
    // lets generate hashes.
    // This should be done from server side..
    // Do not keep salt anywhere in app.
    public void generateHashFromSDK(PaymentParams mPaymentParams, String Salt){
        PayuHashes payuHashes = new PayuHashes();
        postData = new PostData();

        // payment Hash;
        checksum = null;
        checksum = new PayUChecksum();
        checksum.setAmount(mPaymentParams.getAmount());
        checksum.setKey(mPaymentParams.getKey());
        checksum.setTxnid(mPaymentParams.getTxnId());
        checksum.setEmail(mPaymentParams.getEmail());
        checksum.setSalt(salt);
        checksum.setProductinfo(mPaymentParams.getProductInfo());
        checksum.setFirstname(mPaymentParams.getFirstName());
        checksum.setUdf1(mPaymentParams.getUdf1());
        checksum.setUdf2(mPaymentParams.getUdf2());
        checksum.setUdf3(mPaymentParams.getUdf3());
        checksum.setUdf4(mPaymentParams.getUdf4());
        checksum.setUdf5(mPaymentParams.getUdf5());

        postData = checksum.getHash();
        if(postData.getCode() == PayuErrors.NO_ERROR){
            payuHashes.setPaymentHash(postData.getResult());
        }

        // checksum for payemnt related details
        // var1 should be either user credentials or default
        var1 = var1 == null ? PayuConstants.DEFAULT : var1 ;

        if((postData = calculateHash(key, PayuConstants.PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) // Assign post data first then check for success
            payuHashes.setPaymentRelatedDetailsForMobileSdkHash(postData.getResult());
        //vas
        if((postData = calculateHash(key, PayuConstants.VAS_FOR_MOBILE_SDK, PayuConstants.DEFAULT, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
            payuHashes.setVasForMobileSdkHash(postData.getResult());

        // getIbibocodes
        if((postData = calculateHash(key, PayuConstants.GET_MERCHANT_IBIBO_CODES, PayuConstants.DEFAULT, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
            payuHashes.setMerchantIbiboCodesHash(postData.getResult());

        if(!var1.contentEquals(PayuConstants.DEFAULT)){
            // get user card
            if((postData = calculateHash(key, PayuConstants.GET_USER_CARDS, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) // todo rename storedc ard
                payuHashes.setStoredCardsHash(postData.getResult());
            // save user card
            if((postData = calculateHash(key, PayuConstants.SAVE_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
                payuHashes.setSaveCardHash(postData.getResult());
            // delete user card
            if((postData = calculateHash(key, PayuConstants.DELETE_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
                payuHashes.setDeleteCardHash(postData.getResult());
            // edit user card
            if((postData = calculateHash(key, PayuConstants.EDIT_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
                payuHashes.setEditCardHash(postData.getResult());
        }

        if(mPaymentParams.getOfferKey() != null ){
            postData = calculateHash(key, PayuConstants.OFFER_KEY, mPaymentParams.getOfferKey(), salt);
            if(postData.getCode() == PayuErrors.NO_ERROR){
                payuHashes.setCheckOfferStatusHash(postData.getResult());
            }
        }

        if(mPaymentParams.getOfferKey() != null && (postData = calculateHash(key, PayuConstants.CHECK_OFFER_STATUS, mPaymentParams.getOfferKey(), salt)) != null && postData.getCode() == PayuErrors.NO_ERROR ){
            payuHashes.setCheckOfferStatusHash(postData.getResult());
        }

        // we have generated all the hases now lest launch sdk's ui
        launchSdkUI(payuHashes);
    }


    // deprecated, should be used only for testing.
    private PostData calculateHash(String key, String command, String var1, String salt) {
        checksum = null;
        checksum = new PayUChecksum();
        checksum.setKey(key);
        checksum.setCommand(command);
        checksum.setVar1(var1);
        checksum.setSalt(salt);
        return checksum.getHash();
    }
    class GetHashesFromServerTask extends AsyncTask<String, String, PayuHashes>{

        @Override
        protected PayuHashes doInBackground(String ... postParams) {
            PayuHashes payuHashes = new PayuHashes();
            try {
//                URL url = new URL(PayuConstants.MOBILE_TEST_FETCH_DATA_URL);
//                        URL url = new URL("http://10.100.81.49:80/merchant/postservice?form=2");;

                URL url = new URL("https://payu.herokuapp.com/get_hash");

                // get the payuConfig first
                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());
                System.out.print("========respones=========="+response);
                Iterator<String> payuHashIterator = response.keys();
                while(payuHashIterator.hasNext()){
                    String key = payuHashIterator.next();
                    switch (key){
                        case "payment_hash":
                            payuHashes.setPaymentHash(response.getString(key));
                            break;
                        case "get_merchant_ibibo_codes_hash": //
                            payuHashes.setMerchantIbiboCodesHash(response.getString(key));
                            break;
                        case "vas_for_mobile_sdk_hash":
                            payuHashes.setVasForMobileSdkHash(response.getString(key));
                            break;
                        case "payment_related_details_for_mobile_sdk_hash":
                            payuHashes.setPaymentRelatedDetailsForMobileSdkHash(response.getString(key));
                            break;
                        case "delete_user_card_hash":
                            payuHashes.setDeleteCardHash(response.getString(key));
                            break;
                        case "get_user_cards_hash":
                            payuHashes.setStoredCardsHash(response.getString(key));
                            break;
                        case "edit_user_card_hash":
                            payuHashes.setEditCardHash(response.getString(key));
                            break;
                        case "save_user_card_hash":
                            payuHashes.setSaveCardHash(response.getString(key));
                            break;
                        case "check_offer_status_hash":
                            payuHashes.setCheckOfferStatusHash(response.getString(key));
                            break;
                        case "check_isDomestic_hash":
                            payuHashes.setCheckIsDomesticHash(response.getString(key));
                            break;
                        default:
                            break;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return payuHashes;
        }

        @Override
        protected void onPostExecute(PayuHashes payuHashes) {
            super.onPostExecute(payuHashes);
            launchSdkUI(payuHashes);
        }
    }

    public void launchSdkUI(PayuHashes payuHashes){
        // let me add the other params which i might use from other activity

        intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
//        intent.putExtra(PayuConstants.PAYMENT_DEFAULT_PARAMS, mPaymentParams);
        intent.putExtra(PayuConstants.PAYMENT_PARAMS, mPaymentParams);
        intent.putExtra(PayuConstants.PAYU_HASHES, payuHashes);
        Log.e("hashes=------",""+payuHashes);


        /**
         *  just for testing, dont do this in production.
         *  i need to generate hash for {@link com.payu.india.Tasks.GetTransactionInfoTask} since it requires transaction id, i don't generate hash from my server
         *  merchant should generate the hash from his server.
         *
         */
        intent.putExtra(PayuConstants.SALT, salt);

        startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);

    }



}
