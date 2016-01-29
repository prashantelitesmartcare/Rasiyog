package com.elite.rasiyog.Utilities;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.elite.rasiyog.R;


@SuppressLint({ "NewApi", "DrawAllocation" }) public class Utils {

	/*
	 * 
	 * news feed link declared in rss feed
	 */
	//http://news.google.com/news?output=rss

	public static final String PhotoUtil = null;
	public static int listChild = 0;
	public static int listChild1 = 0;
	public static void hideSoftKeyboard(Activity activity) {
	    InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}

	
	public static void CopyStream(java.io.InputStream is, java.io.OutputStream os)
	{
		final int buffer_size=1024;
		try
		{
			byte[] bytes=new byte[buffer_size];
			for(;;)
			{
				int count=is.read(bytes, 0, buffer_size);
				if(count==-1)
					break;
				os.write(bytes, 0, count);
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	 

	/*public static void setLoginUsername(SharedPreferences pref, String uniq) {
		pref.edit().putString(Preferences.LOGGED_IN_USERNAME, uniq).commit();
	}

	public static String getLoginUsername(SharedPreferences pref) {
		return pref.getString(Preferences.LOGGED_IN_USERNAME,
				Preferences.LOGGED_IN_USERNAME_VALUE);

	}*/
	
	public static Boolean isNetWorking(Context context) {
		try {
			ConnectivityManager ConMgr = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = ConMgr.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnected()) {

				return true;
			}
			return false;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * Show Alert Dialog
	 */

	@SuppressWarnings("deprecation")
	public static AlertDialog getAlertDialog(Context context, String msg,Handler handler) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle("Mark Webber" + "");
		alertDialog.setIcon(R.drawable.ic_launcher);

		alertDialog.setMessage(msg);
		alertDialog.setCancelable(true);
		alertDialog.setButton("OK", Message.obtain(handler, 1000));

		return alertDialog;
	}

	/*
	 * Show Toast
	 */
	public static void show_Toast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	/*
	 * Show Progress Dialog
	 */

	private static ProgressDialog dialog;
	public static boolean listIsFullyDisplayed=false;
	public static boolean listIsFullyDisplayedForPhotos=false;
	public static ProgressDialog showDialog(String message, Context context) {
		dialog = new ProgressDialog(context);
		dialog.setMessage(message);
		dialog.setCancelable(true);
		dialog.setIndeterminate(false);
		dialog.show();
		return dialog;
	}

	/*
	 * Dismiss Dialog
	 */
	public static void dismissDialog(ProgressDialog dialog) {
		if (dialog != null && dialog.isShowing()) {
			dialog.cancel();
		}

	}
	/*
	 * Print Logcat with String
	 */
	public static void printLocCat(String TAG, String message) {
		Log.e(TAG, message);
	}

	/*
	 * Print Log Cat with Value
	 */
	public static void printLocCatValue(String TAG, String message, String value) {
		Log.e(TAG, message + value);

	}
	
	public final static boolean isValidEmail(CharSequence target) {
		  return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}


	public static Typeface getAvailableFont(Context context) {
		return Typeface.createFromAsset(context.getAssets(), "Pacifico_0.ttf");

	}

	/*public static Typeface getAvailableFontBold(Context context) {
		return Typeface.createFromAsset(context.getAssets(), "textfont.ttf");

	}*/
	
	public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			if (listItem instanceof ViewGroup) {
				listItem.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		Log.e("divider height", listView.getDividerHeight()+"=");
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
	public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {

		// TODO Auto-generated method stub
		int targetWidth = 120;
		int targetHeight = 120;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
				Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
				Path.Direction.CCW);

		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
				sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
						targetHeight), null);
		return targetBitmap;
	}
	
	public class RoundedCornerImageView extends ImageView {
	    private int radius = 10;

	    public RoundedCornerImageView(Context context) {
	        super(context);
	    }

	    protected void onDraw(Canvas canvas) {
	        Path clipPath = new Path();
	        int w = this.getWidth();
	        int h = this.getHeight();
	        clipPath.addRoundRect(new RectF(0, 0, w, h), radius, radius, Path.Direction.CW);
	        canvas.clipPath(clipPath);
	        super.onDraw(canvas);
	    }

	    public RoundedCornerImageView(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	    }

	    public RoundedCornerImageView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	    }

	    public void setRadius(int radius){
	        this.radius = radius;
	        this.invalidate();
	    }
	}

	public static Bitmap getRoundedShape1(Bitmap scaleBitmapImage, ImageView img) {

		// TODO Auto-generated method stub
		int targetWidth = 120;
		int targetHeight = 120;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
				Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 7,
				(Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
				Path.Direction.CCW);

		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
				sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
						targetHeight), null);
		img.getLayoutParams().height = 100;
		img.getLayoutParams().width = 95;
		img.setImageBitmap(targetBitmap);
		return targetBitmap;
	}

	public static void setListViewHeightBasedOnChildrenForPhotos(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
		int totalCountOfAdapter=listAdapter.getCount();
		int temp=Utils.listChild1+10;
		if(temp>=totalCountOfAdapter)
		{
			temp=totalCountOfAdapter;
			Utils.listIsFullyDisplayedForPhotos=true;
		}else{
			listChild1=temp;
			Utils.listIsFullyDisplayedForPhotos=false;
		}

		for (int i = 0; i < temp; i++) {
			View listItem = listAdapter.getView(i, null, listView);
			if (listItem instanceof ViewGroup) {
				listItem.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}
			listItem.measure(0, 0);
			totalHeight += listItem.getPaddingBottom();
			totalHeight += listItem.getPaddingTop();
			totalHeight += listItem.getMeasuredHeight();
			Log.e("divider he =", listItem.getPaddingBottom()+"bottom"+listItem.getPaddingTop()+"top"+listItem.getMeasuredHeight()+"=");
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		Log.e("divider height", listView.getDividerHeight()+"=");
		params.height = totalHeight +  (listView.getDividerHeight() * (temp - 1));
		listView.setLayoutParams(params);
	}










	public static void setListViewHeightBasedOnChildrenForStatus(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
		int totalCountOfAdapter=listAdapter.getCount();
		int temp=Utils.listChild+10;
		if(temp>=totalCountOfAdapter)
		{
			temp=totalCountOfAdapter;
			Utils.listIsFullyDisplayed=true;
		}else{
			Utils.listChild=temp;
			Utils.listIsFullyDisplayed=false;
		}

		for (int i = 0; i < temp; i++) {
			View listItem = listAdapter.getView(i, null, listView);
			if (listItem instanceof ViewGroup) {
				listItem.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}
			listItem.measure(0, 0);
			totalHeight += listItem.getPaddingBottom();
			totalHeight += listItem.getPaddingTop();

			totalHeight += listItem.getMeasuredHeight();
			Log.e("divider he =", listItem.getPaddingBottom()+"bottom"+listItem.getPaddingTop()+"top"+listItem.getMeasuredHeight()+"=");
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		Log.e("divider height", listView.getDividerHeight()+"=");
		params.height = totalHeight + (listView.getDividerHeight() * (temp - 1));
		listView.setLayoutParams(params);
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap output = null;
		try {
			output = Bitmap.createBitmap(bitmap.getWidth(),   bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final int color = 0xff424242;

			BitmapShader shader = new BitmapShader (bitmap,  TileMode.CLAMP, TileMode.CLAMP);
			Paint paint = new Paint();
			paint.setShader(shader);
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			final RectF rectF = new RectF(rect);
			final float roundPx = bitmap.getHeight()/2;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF,roundPx,roundPx, paint);

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

			canvas.drawBitmap(bitmap, rect, rect, paint);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return output;
	}



	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,int roundPixelSize) { 
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888); 
		Canvas canvas = new Canvas(output); 
		final Paint paint = new Paint(); 
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()); 
		final RectF rectF = new RectF(rect); 
		final float roundPx = roundPixelSize;
		paint.setAntiAlias(true);
		canvas.drawRoundRect(rectF,roundPx,roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint); 
		return output; 
	}


	/*public static void clearPreferences(SharedPreferences pref) {
		set_login_uniq_id(pref, "");
		setLoginUsername(pref, "");


	}
	 */

	/*	

	AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
	builder.setMessage("Exiting");
	builder.setCancelable(false);
	builder.setTitle("No Network available");
	builder.setPositiveButton("Try again", 
			new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface arg0, int arg1) {  
			callApi();
		}
	});
	builder.setNegativeButton("OK",
			new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface arg0, int arg1) {  
			HomeActivity.this.finish();

		}
	});
	AlertDialog alert = builder.create();
	alert.show();*/

}
