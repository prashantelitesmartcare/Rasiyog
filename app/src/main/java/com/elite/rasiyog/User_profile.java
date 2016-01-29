package com.elite.rasiyog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.elite.rasiyog.Utilities.Constants;
import com.elite.rasiyog.Utilities.Slidermenu;
import com.elite.rasiyog.Utilities.Utils;
import com.navdrawer.SimpleSideDrawer;
import com.squareup.picasso.Picasso;

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

public class User_profile extends Activity implements View.OnClickListener {

    SimpleSideDrawer mNav;
    ProgressDialog progress;
    TextView user_name1;
    SharedPreferences myPref;
    ImageView add_image,save_btn,edit_btn,user_image,menu;
    EditText name_edit,DOB_edit,place_edit,phone_edit,email_edit,password_edit,Hrous_edit,min_edit;
    String name,dob,place_of_birth,birth_hour,birth_mint,phone_number,email,image,loginId,get_active;
    String id,name_j,dob_j,place_of_birth_j,birth_hour_j,birth_mint_j,phone_number_j,email_j,password_j,image_j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_profile);
//get the login user id from Preference
        myPref = PreferenceManager.getDefaultSharedPreferences(User_profile.this);
        loginId = myPref.getString("User_login", "");
        get_active = myPref.getString("active", "");
        System.out.println("----login-id----" + loginId);
        // Find the view id's from xml
        find_id();
        //initialize Slider here
        mNav = new SimpleSideDrawer(this);
        Slidermenu side = new Slidermenu(mNav, User_profile.this);
        side.setDrawer();
        findViewById(R.id.slider).setVisibility(View.GONE);
        mNav = new SimpleSideDrawer(this);
        Slidermenu side1 = new Slidermenu(mNav, User_profile.this);
        side1.setDrawer();
        try {
            get_active = getIntent().getStringExtra("status");

            if(get_active.equalsIgnoreCase("p"))
            {
                findViewById(R.id.profile).setBackgroundResource(R.drawable.menu_highlit);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
        user_name1 = (TextView) findViewById(R.id.user_name1);
        //Imageview
        add_image = (ImageView) findViewById(R.id.add_image);
        save_btn = (ImageView) findViewById(R.id.save_btn);
        edit_btn = (ImageView) findViewById(R.id.edit_btn);
        user_image = (ImageView) findViewById(R.id.user_image);
        menu = (ImageView) findViewById(R.id.menu);
        //edittext
        name_edit = (EditText) findViewById(R.id.name_edit);
        DOB_edit = (EditText) findViewById(R.id.DOB_edit);
        place_edit = (EditText) findViewById(R.id.place_edit);
        phone_edit = (EditText) findViewById(R.id.phone_edit);
        email_edit = (EditText) findViewById(R.id.email_edit);
        Hrous_edit = (EditText) findViewById(R.id.Hrous_edit);
        min_edit = (EditText) findViewById(R.id.min_edit);
        //password_edit = (EditText) findViewById(R.id.password_edit);
        edit_btn.setOnClickListener(this);
        menu.setOnClickListener(this);

    }

    //=====================Click function here for image view and other views====================//
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.edit_btn:
                Intent i = new Intent(User_profile.this,Profile_Edit.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.menu:
                System.out.println("===activre======"+get_active);
                SharedPreferences.Editor editor11 = myPref.edit();
                editor11.putString("active","P");
                editor11.commit();
                break;
            default:
                break;
        }
    }
    //=======================above function finished here=======================================//


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
                progress = new ProgressDialog(User_profile.this);
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
                user_name1.setText(name_j);
                name_edit.setText(name_j);
                DOB_edit.setText(dob_j);
                Hrous_edit.setText(birth_hour_j);
                min_edit.setText(birth_mint_j);
                place_edit.setText(place_of_birth_j);
                phone_edit.setText(phone_number_j);
                email_edit.setText(email_j);
                user_image.setScaleType(ImageView.ScaleType.FIT_XY);
                Picasso.with(getApplicationContext()).load(image_j).transform(new RoundedTransformation(50, 4))
                        .resize(110, 110)
                        .centerCrop().into(user_image);
                progress.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //==================== above loader finished here =============================//
}
