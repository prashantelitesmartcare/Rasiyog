package com.elite.rasiyog.Utilities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.elite.rasiyog.Change_Password;
import com.elite.rasiyog.Dashboard;
import com.elite.rasiyog.HorscopeScreen;
import com.elite.rasiyog.HorscopemakingScreen;
import com.elite.rasiyog.LoginScreen;
import com.elite.rasiyog.MatchMaking;
import com.elite.rasiyog.Message;
import com.elite.rasiyog.QuestionScreen;
import com.elite.rasiyog.R;
import com.elite.rasiyog.RoundedTransformation;
import com.elite.rasiyog.User_profile;
import com.elite.rasiyog.VastuScreen;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.navdrawer.SimpleSideDrawer;
import com.squareup.picasso.Picasso;

public class Slidermenu {
	private SimpleSideDrawer mNav;
	private Activity activity;
	SharedPreferences myPref;
	TextView user_name,user_email;
	ImageView user_image;
	private CallbackManager callbackManager;
String name,email,image,get_active,access_token;
	public Slidermenu(final SimpleSideDrawer mNav, Activity activity) {
		this.mNav = mNav;
		this.activity = activity;
		mNav.setLeftBehindContentView(R.layout.activity_behind_left_simple);
		myPref = PreferenceManager.getDefaultSharedPreferences(activity);
		name = myPref.getString("name", "");
		email = myPref.getString("email", "");
		image = myPref.getString("image", "");
		get_active = myPref.getString("active", "");
		access_token= myPref.getString("access_token", "");

		if(access_token.equalsIgnoreCase(""))
		{
			activity.findViewById(R.id.change_password).setVisibility(View.VISIBLE);
		}
		else
		{
			activity.findViewById(R.id.change_password).setVisibility(View.GONE);
		}
		FacebookSdk.sdkInitialize(activity);
		callbackManager = CallbackManager.Factory.create();
		facebooklogin();
		System.out.println("===activre======"+get_active);

	}

	private void facebooklogin() {

		LoginManager.getInstance().registerCallback(callbackManager,
				new FacebookCallback<LoginResult>() {
					@Override
					public void onSuccess(LoginResult loginResult) {

						Toast.makeText(activity, "Success", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onCancel() {
						Toast.makeText(activity, "Login Cancel", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onError(FacebookException exception) {
						Toast.makeText(activity, exception.getMessage(), Toast.LENGTH_LONG).show();
					}
				});
	}

	public void setDrawer() {
		user_name = (TextView) activity.findViewById(R.id.user_name_menu1);
		user_email = (TextView) activity.findViewById(R.id.user_email_menu1);
		user_image = (ImageView) activity.findViewById(R.id.user_image_menu1);
		user_name.setText(name);
		user_email.setText(email);
		user_image.setScaleType(ScaleType.FIT_XY);
		if(image.equalsIgnoreCase(""))
		{
			
		}
		else
		{
			Picasso.with(activity).load(image).transform(new RoundedTransformation(50, 4))
			.resize(110, 110)
			.centerCrop().into(user_image);
		}
		
		
		
		activity.findViewById(R.id.menu).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						mNav.toggleLeftDrawer();
						if(get_active.equalsIgnoreCase(""))
						{}
						else if(get_active.equalsIgnoreCase("H"))
						{
							activity.findViewById(R.id.Home).setBackgroundResource(R.drawable.headder);
							activity.findViewById(R.id.today).setBackgroundColor(Color.WHITE);
							activity.findViewById(R.id.weeek).setBackgroundColor(Color.WHITE);
							activity.findViewById(R.id.month).setBackgroundColor(Color.WHITE);
							activity.findViewById(R.id.profile).setBackgroundColor(Color.WHITE);
							activity.findViewById(R.id.message).setBackgroundColor(Color.WHITE);
							activity.findViewById(R.id.horoscope).setBackgroundColor(Color.WHITE);
							activity.findViewById(R.id.question).setBackgroundColor(Color.WHITE);
							activity.findViewById(R.id.match).setBackgroundColor(Color.WHITE);
							activity.findViewById(R.id.vastu).setBackgroundColor(Color.WHITE);
							activity.findViewById(R.id.change_password).setBackgroundColor(Color.WHITE);
						}
						
						else if(get_active.equalsIgnoreCase("P"))
						{
							activity.findViewById(R.id.Home).setBackgroundColor(Color.WHITE);
							activity.findViewById(R.id.today).setBackgroundColor(Color.WHITE);
							activity.findViewById(R.id.weeek).setBackgroundColor(Color.WHITE);
							activity.findViewById(R.id.month).setBackgroundColor(Color.WHITE);
							activity.findViewById(R.id.profile).setBackgroundResource(R.drawable.headder);
							activity.findViewById(R.id.message).setBackgroundColor(Color.WHITE);
							activity.findViewById(R.id.horoscope).setBackgroundColor(Color.WHITE);
							activity.findViewById(R.id.question).setBackgroundColor(Color.WHITE);
							activity.findViewById(R.id.match).setBackgroundColor(Color.WHITE);
							activity.findViewById(R.id.vastu).setBackgroundColor(Color.WHITE);
							activity.findViewById(R.id.change_password).setBackgroundColor(Color.WHITE);
						}
					}
				});

		activity.findViewById(R.id.Home).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						
						 Intent in = new Intent(activity,Dashboard.class);
						 in.putExtra("status", "h");
						 activity.startActivity(in);
						 activity.overridePendingTransition(0, 0);
						 
					}
				});

		activity.findViewById(R.id.today).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent in = new Intent(activity, HorscopeScreen.class);
						in.putExtra("key1", "today");
						 in.putExtra("status", "today");
						activity.startActivity(in);
						activity.overridePendingTransition(0, 0);
					}
				});

		activity.findViewById(R.id.weeek).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {

						Intent in = new Intent(activity, HorscopeScreen.class);
						in.putExtra("key1", "week");
						 in.putExtra("status", "week");
						activity.startActivity(in);
						activity.overridePendingTransition(0, 0);

					}
				});
		activity.findViewById(R.id.month).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {

						Intent in = new Intent(activity, HorscopeScreen.class);
						in.putExtra("key1", "month");
						in.putExtra("status", "month");
						activity.startActivity(in);
						activity.overridePendingTransition(0, 0);
					}
				});
		activity.findViewById(R.id.profile).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						
					

						Intent in = new Intent(activity, User_profile.class);
						 in.putExtra("status", "p");
						activity.startActivity(in);
						activity.overridePendingTransition(0, 0);
					}
				});
		activity.findViewById(R.id.message).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						
					
						Intent in = new Intent(activity, Message.class);
						 in.putExtra("status", "M");
						activity.startActivity(in);
						activity.overridePendingTransition(0, 0);
					}
				});

		activity.findViewById(R.id.horoscope).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {

						Intent in = new Intent(activity,
								HorscopemakingScreen.class);
						 in.putExtra("status", "Ho");
						activity.startActivity(in);
						activity.overridePendingTransition(0, 0);

					}
				});
		activity.findViewById(R.id.question).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {

						Intent in = new Intent(activity, QuestionScreen.class);
						 in.putExtra("status", "Q");
						activity.startActivity(in);
						activity.overridePendingTransition(0, 0);

					}
				});
		activity.findViewById(R.id.match).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {

						Intent in = new Intent(activity, MatchMaking.class);
						 in.putExtra("status", "Mat");
						activity.startActivity(in);
						activity.overridePendingTransition(0, 0);

					}
				});
		activity.findViewById(R.id.vastu).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {

						Intent in = new Intent(activity, VastuScreen.class);
						 in.putExtra("status", "Va");
						activity.startActivity(in);
						activity.overridePendingTransition(0, 0);

					}
				});
		activity.findViewById(R.id.change_password).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {

						Intent in = new Intent(activity, Change_Password.class);
						 in.putExtra("status", "C");
						activity.startActivity(in);
						activity.overridePendingTransition(0, 0);

					}
				});
		activity.findViewById(R.id.logout).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {

						LoginManager.getInstance().logOut();
						Intent in = new Intent(activity, LoginScreen.class);
						activity.startActivity(in);
						activity.overridePendingTransition(0, 0);
						  SharedPreferences.Editor editor11 = myPref.edit();
						  editor11.putString("User_login","");
						  editor11.putString("dob","");
						  editor11.putString("pob","");
						  editor11.putString("email","");
						  editor11.putString("gender","");
						  editor11.putString("hrs","");
						  editor11.putString("min","");
						  editor11.putString("date","");
						  editor11.putString("name","");
						  editor11.putString("image","");
						  editor11.putString("access_token","");
						  editor11.commit();
					}
				});

	}

}
