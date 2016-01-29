package com.elite.rasiyog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.elite.rasiyog.Adapters.Message_Adpter;
import com.elite.rasiyog.Utilities.Constants;
import com.elite.rasiyog.Utilities.Slidermenu;
import com.elite.rasiyog.Utilities.Utils;
import com.elite.rasiyog.model.Message_gettersetter;
import com.navdrawer.SimpleSideDrawer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Message extends Activity implements OnClickListener,
		OnItemClickListener {
	ArrayList<Message_gettersetter> getset;
	Message_Adpter adpter;
	ListView message_list;
	SimpleSideDrawer mNav;
	SharedPreferences myPref;
	String loginId,get_active, msg, image_g, date, time, error, name_g, type, attachment;
	ImageView back, referesh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_message);
		myPref = PreferenceManager.getDefaultSharedPreferences(Message.this);
		loginId = myPref.getString("User_login", "");
		get_active = myPref.getString("active", "");
		find_id();
		mNav = new SimpleSideDrawer(this);
		Slidermenu side = new Slidermenu(mNav, Message.this);
		side.setDrawer();
		findViewById(R.id.slider).setVisibility(View.GONE);
		mNav = new SimpleSideDrawer(this);
		Slidermenu side1 = new Slidermenu(mNav, Message.this);
		side1.setDrawer();
		try {
			get_active = getIntent().getStringExtra("status");
			
			if(get_active.equalsIgnoreCase("M"))
			{
				findViewById(R.id.message).setBackgroundResource(R.drawable.menu_highlit);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (Utils.isNetWorking(getApplicationContext())) {
			new Get_Msg_Loader().execute();
		} else {
			Utils.show_Toast(getApplicationContext(), "Internet Not Available");
		}

	}

	private void find_id() {
		message_list = (ListView) findViewById(R.id.message_list);

		message_list.setOnItemClickListener(this);
		referesh = (ImageView) findViewById(R.id.referesh);
		referesh.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.referesh:
			new Get_Msg_Loader().execute();
			break;

		default:
			break;
		}

	}

	// ==================================================================//
	// ====================Get_Msg Loader Start===========================//
	// ================================================================//
	class Get_Msg_Loader extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress;
		private String url;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(Message.this);
			progress.setMessage("Loading...");
			progress.setIndeterminate(false);
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.show();
		}

		protected Void doInBackground(Void... params) {
			getset = new ArrayList<Message_gettersetter>();
			getset.clear();
			try {
				upload();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		public void upload() throws Exception {
			url = Constants.GET_MSG;// +"email="+get_email+"&password="+get_pass;
			System.out.println("====url=pp==" + url);
			System.out.println("====url=pp==" + loginId);
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			try {
				MultipartEntity entity = new MultipartEntity();
				entity.addPart("reciver_id", new StringBody(loginId));
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
				JSONObject h = new JSONObject(s.toString());
				error= h.optString("error");
				JSONArray ss = h.optJSONArray("user");
				for (int i = 0; i < ss.length(); i++) {
					JSONObject hh = ss.optJSONObject(i);
					System.out.println("===final result==" + hh);
					name_g = hh.optString("name");
					msg = hh.optString("message").toString().trim();
					image_g = hh.optString("image");
					date = hh.optString("date");
					attachment = hh.optString("attachment");
					type = hh.optString("category");
					time = hh.optString("time");

					Message_gettersetter jj = new Message_gettersetter(name_g,
							msg, type, date, time, image_g, attachment);
					getset.add(jj);
				}

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onPostExecute(Void args) {
			try {
				if (getset.size() > 0) {
					adpter = new Message_Adpter(Message.this,
							R.layout.message_view, getset);
					message_list.setAdapter(adpter);

					progress.dismiss();
				} else {
					progress.dismiss();
					Toast.makeText(getApplicationContext(), "" + error, 2000)
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		Intent i = new Intent(Message.this, Message_Details.class);
		i.putExtra("name", getset.get(arg2).getName());
		i.putExtra("msg", getset.get(arg2).getMessage());
		i.putExtra("type", getset.get(arg2).getType());
		i.putExtra("image", getset.get(arg2).getImage());
		i.putExtra("date", getset.get(arg2).getDate());
		i.putExtra("time", getset.get(arg2).getTime());
		i.putExtra("url", getset.get(arg2).getAttachment());
		startActivity(i);
		overridePendingTransition(0, 0);

	}

}
