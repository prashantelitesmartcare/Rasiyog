package com.elite.rasiyog;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.elite.rasiyog.Utilities.Utils;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class Message_Details extends Activity implements OnClickListener {
	
	ImageView back;
	String name,msg,image,type,date,time,attach,File_name;
	ImageView userimage;
	TextView username,desc,date1,time1,attach1;
	 public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_message__details);
		find_id();
		
		try {
			name = getIntent().getStringExtra("name");
			msg = getIntent().getStringExtra("msg");
			type = getIntent().getStringExtra("type");
			date = getIntent().getStringExtra("date");
			time = getIntent().getStringExtra("time");
			image = getIntent().getStringExtra("image");
			attach = getIntent().getStringExtra("url");
			
			username.setText(name);
			desc.setText(msg);
			date1.setText(date);
			time1.setText(time);
			Picasso.with(getApplicationContext()).load(image).transform(new RoundedTransformation(50, 4))
			.resize(110, 110)
			.centerCrop().into(userimage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void find_id() {
		back = (ImageView)findViewById(R.id.back);
		userimage = (ImageView)findViewById(R.id.userimage);
	
		//Textviw
		username = (TextView)findViewById(R.id.username);
		desc = (TextView)findViewById(R.id.desc);
		date1 = (TextView)findViewById(R.id.date);
		time1 = (TextView)findViewById(R.id.time);
		attach1 = (TextView)findViewById(R.id.attach);
		SpannableString content = new SpannableString("ATTACHMENT");
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		attach1.setText(content);
		back.setOnClickListener(this);
		attach1.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			onBackPressed();
			break;
		
		case R.id.attach:
			String url = attach;
			String hh = url.replace("http://192.168.1.18/mobile/horoscope/uploads/attachment/", "");
			System.out.println("==NEW NAME===="+hh);
    		String s = hh;
			 
			 String[] split = s.split("/");
			  File_name = split[1];
			 System.out.println("==File_name===="+File_name);
		        new DownloadFileAsync().execute(url);
			break;
		default:
			break;
		}
		
	}
	
	class DownloadFileAsync extends AsyncTask<String, String, String> {
		   
		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		@Override
		protected String doInBackground(String... aurl) {
			int count;

		try {

		URL url = new URL(aurl[0]);
		URLConnection conexion = url.openConnection();
		conexion.connect();

		int lenghtOfFile = conexion.getContentLength();
		Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
		 File storagePath = new File(Environment.getExternalStorageDirectory() + "/UJAS Attachment/"); 
	        storagePath.mkdirs(); 
	        File myImage = new File(storagePath,
	                Long.toString(System.currentTimeMillis()) + File_name);

		InputStream input = new BufferedInputStream(url.openStream());
		OutputStream output = new FileOutputStream(myImage);

		byte data[] = new byte[1024];

		long total = 0;

			while ((count = input.read(data)) != -1) {
				total += count;
				publishProgress(""+(int)((total*100)/lenghtOfFile));
				output.write(data, 0, count);
			}

			output.flush();
			output.close();
			input.close();
		} catch (Exception e) {}
		return null;

		}
		protected void onProgressUpdate(String... progress) {
			 Log.d("ANDRO_ASYNC",progress[0]);
			 mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String unused) {
			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			Utils.show_Toast(getApplicationContext(), "Downloading completed");
		}
	}
	
	   @Override
	    protected Dialog onCreateDialog(int id) {
	        switch (id) {
			case DIALOG_DOWNLOAD_PROGRESS:
				mProgressDialog = new ProgressDialog(this);
				mProgressDialog.setMessage("Downloading file..");
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				mProgressDialog.setCancelable(false);
				mProgressDialog.show();
				return mProgressDialog;
			default:
				return null;
	        }
	    }


	

}
