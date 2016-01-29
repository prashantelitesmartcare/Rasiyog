package com.elite.rasiyog;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.elite.rasiyog.Utilities.Slidermenu;
import com.navdrawer.SimpleSideDrawer;

public class HorscopeScreen extends Activity implements OnClickListener {

	SimpleSideDrawer mNav;
	Intent i;
	String get_key1,get_active;
	ImageView ares,taurus,gemini,cancer,leo,virgo,libra,scorpio,sagitarius,capricon,acqurious,pisces;
	SharedPreferences myPref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_horscope_screen);
		myPref = PreferenceManager.getDefaultSharedPreferences(HorscopeScreen.this);
		get_active = myPref.getString("active", "");
		find_id();
		
		
		get_key1 = getIntent().getStringExtra("key1");
				
		
		mNav = new SimpleSideDrawer(this);	
		Slidermenu side = new Slidermenu(mNav, HorscopeScreen.this);
		side.setDrawer();
		findViewById(R.id.slider).setVisibility(View.GONE);
		mNav = new SimpleSideDrawer(this);	
		Slidermenu side1 = new Slidermenu(mNav, HorscopeScreen.this);
		side1.setDrawer();
		try {
			get_active = getIntent().getStringExtra("status");
			
			if(get_active.equalsIgnoreCase("week"))
			{
				findViewById(R.id.weeek).setBackgroundResource(R.drawable.menu_highlit);
			}
			else if (get_active.equalsIgnoreCase("month")) {
				findViewById(R.id.month).setBackgroundResource(R.drawable.menu_highlit);
			}
			else if (get_active.equalsIgnoreCase("today")) {
				findViewById(R.id.today).setBackgroundResource(R.drawable.menu_highlit);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void find_id() {
		//image views
		ares = (ImageView) findViewById(R.id.ares);
		taurus = (ImageView) findViewById(R.id.taurus);
		gemini = (ImageView) findViewById(R.id.gemini);
		cancer = (ImageView) findViewById(R.id.cancer);
		leo = (ImageView) findViewById(R.id.leo);
		virgo = (ImageView) findViewById(R.id.virgo);
		libra = (ImageView) findViewById(R.id.libra);
		scorpio = (ImageView) findViewById(R.id.scorpio);
		sagitarius = (ImageView) findViewById(R.id.sagitarius);
		capricon = (ImageView) findViewById(R.id.capricon);
		acqurious = (ImageView) findViewById(R.id.acqurious);
		pisces = (ImageView) findViewById(R.id.pisces);
		
		ares.setOnClickListener(this);
		taurus.setOnClickListener(this);
		gemini.setOnClickListener(this);
		cancer.setOnClickListener(this);
		leo.setOnClickListener(this);
		virgo.setOnClickListener(this);
		libra.setOnClickListener(this);
		scorpio.setOnClickListener(this);
		sagitarius.setOnClickListener(this);
		capricon.setOnClickListener(this);
		acqurious.setOnClickListener(this);
		pisces.setOnClickListener(this);
		
		
		
	}
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		
		case R.id.ares:
			i=  new Intent(HorscopeScreen.this,HoroscopeDeatil.class);
			i.putExtra("key", "aries");
			i.putExtra("key1", get_key1);
			startActivity(i);
			overridePendingTransition(0, 0);
			break;
		case R.id.taurus:
			i=  new Intent(HorscopeScreen.this,HoroscopeDeatil.class);
			i.putExtra("key", "taurus");
			i.putExtra("key1", get_key1);
			startActivity(i);
			overridePendingTransition(0, 0);
					break;
		case R.id.gemini:
			i=  new Intent(HorscopeScreen.this,HoroscopeDeatil.class);
			i.putExtra("key", "gemini");
			i.putExtra("key1", get_key1);
			startActivity(i);
			overridePendingTransition(0, 0);
			break;
		case R.id.cancer:
			i=  new Intent(HorscopeScreen.this,HoroscopeDeatil.class);
			i.putExtra("key", "cancer");
			i.putExtra("key1", get_key1);
			startActivity(i);
			overridePendingTransition(0, 0);
			break;
		case R.id.leo:
			i=  new Intent(HorscopeScreen.this,HoroscopeDeatil.class);
			i.putExtra("key", "leo");
			i.putExtra("key1", get_key1);
			startActivity(i);
			overridePendingTransition(0, 0);
			break;
			
		case R.id.virgo:
			i=  new Intent(HorscopeScreen.this,HoroscopeDeatil.class);
			i.putExtra("key", "virgo");
			i.putExtra("key1", get_key1);
			startActivity(i);
			overridePendingTransition(0, 0);
			break;
		case R.id.libra:
			i=  new Intent(HorscopeScreen.this,HoroscopeDeatil.class);
			i.putExtra("key", "libra");
			i.putExtra("key1", get_key1);
			startActivity(i);
			overridePendingTransition(0, 0);
			break;
		case R.id.scorpio:
			i=  new Intent(HorscopeScreen.this,HoroscopeDeatil.class);
			i.putExtra("key", "scorpio");
			i.putExtra("key1", get_key1);
			startActivity(i);
			overridePendingTransition(0, 0);
			break;
		case R.id.sagitarius:
			i=  new Intent(HorscopeScreen.this,HoroscopeDeatil.class);
			i.putExtra("key", "sagittarius");
			i.putExtra("key1", get_key1);
			startActivity(i);
			overridePendingTransition(0, 0);
			break;
			
		case R.id.capricon:
			i=  new Intent(HorscopeScreen.this,HoroscopeDeatil.class);
			i.putExtra("key", "capricorn");
			i.putExtra("key1", get_key1);
			startActivity(i);
			overridePendingTransition(0, 0);
			break;
		case R.id.acqurious:
			i=  new Intent(HorscopeScreen.this,HoroscopeDeatil.class);
			i.putExtra("key", "aquarius");
			i.putExtra("key1", get_key1);
			startActivity(i);
			overridePendingTransition(0, 0);
			break;
		case R.id.pisces:
			i=  new Intent(HorscopeScreen.this,HoroscopeDeatil.class);
			i.putExtra("key", "pisces");
			i.putExtra("key1", get_key1);
			startActivity(i);
			overridePendingTransition(0, 0);
			break;
	

		default:
			break;
		}
		
	}

	

}
