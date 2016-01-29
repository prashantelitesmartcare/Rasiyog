package com.elite.rasiyog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.widget.RemoteViews;

import com.groboot.pushapps.PushAppsMessageInterface;
import com.groboot.pushapps.PushAppsRegistrationInterface;
import com.groboot.pushapps.PushManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Splash extends Activity {

	String User_id;
	public static final String GOOGLE_API_PROJECT_NUMBER = "984039123005"; // your
	// public static final String PUSHAPPS_APP_TOKEN =
	// "AIzaSyBMBM_bzRKkrbpNks0FAesjrwbNYpsumbE";
	public static final String PUSHAPPS_APP_TOKEN = "4fc81c81-3071-48a1-b7d7-ca912af67737";

	String push_tokens, android_id;
	private Handler handler;
	private SharedPreferences myPref;
	private String loginId, user_type,  push_token = "";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		// Add code to print out the key hash
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"com.elite.rasiyog",
					PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				System.out.print("==========hashe key========="+Base64.encodeToString(md.digest(), Base64.DEFAULT));
				Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (PackageManager.NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}


		// get the login user id from Preference
		myPref = PreferenceManager.getDefaultSharedPreferences(Splash.this);
		User_id = myPref.getString("User_login", "");
		System.out.println("=======suerid====" + User_id);
		push_token = myPref.getString("TOKEN_ID", "");
		android_id = Secure.getString(getBaseContext().getContentResolver(),
				Secure.ANDROID_ID);

		System.out.println("===ANDROID_DEvice id is=======" + android_id);
		if (push_token.equalsIgnoreCase("")) {

			// PushManager in your main activity.
			PushManager.init(getBaseContext(), GOOGLE_API_PROJECT_NUMBER,
					PUSHAPPS_APP_TOKEN);
			PushManager.getInstance(getApplicationContext())
					.setShouldStartIntentAsNewTask(false);
			// these methods are both optional and used for the notification
			// customization
			PushManager.getInstance(getApplicationContext())
					.setShouldStackNotifications(true);

			// register for message events
			PushManager.getInstance(getApplicationContext())
					.registerForMessagesEvents(new PushAppsMessageInterface() {

						// This method will get called every time the device
						// will
						// receive a push message
						@SuppressLint("NewApi")
						@Override
						public void onMessage(Context ctx, Intent intent) {
							String titleTxt = intent
									.getStringExtra(PushManager.NOTIFICATION_TITLE_KEY); // Get
																							// the
																							// title
																							// string
																							// from
																							// the
							String subTitleTxt = intent
									.getStringExtra(PushManager.NOTIFICATION_MESSAGE_KEY); // Get
																							// the
																							// message
																							// string
																							// from
																							// the
							// The intent to start, when the user clicks the
							// notification
							Intent resultIntent = new Intent(
									getApplicationContext(), Message.class);
							TaskStackBuilder stackBuilder = TaskStackBuilder
									.create(getApplicationContext());
							System.out.println("--stackBuilder---"
									+ stackBuilder);

							// stackBuilder.addParentStack(FranchiseeViewMessage.class);
							stackBuilder.addNextIntent(resultIntent);
							PendingIntent resultPendingIntent = stackBuilder
									.getPendingIntent(0,
											PendingIntent.FLAG_UPDATE_CURRENT);

							// The custom view
							RemoteViews expandedView = new RemoteViews(
									getApplicationContext().getPackageName(),
									R.layout.custom_notification);
							expandedView.setTextViewText(
									R.id.notification_title, titleTxt);
							expandedView.setTextViewText(
									R.id.notification_subtitle, subTitleTxt);

							// Building the notification that will show up in
							// the
							// notification center
							Notification notification = new NotificationCompat.Builder(
									getApplicationContext())
									.setSmallIcon(R.drawable.ic_launcher)
									//.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
									.setDefaults(Notification.DEFAULT_VIBRATE)
									.setAutoCancel(true)
									.setContentIntent(resultPendingIntent)
									.setContentTitle(titleTxt)
									.setContentText(subTitleTxt).build();
							setListeners(notification.bigContentView);// look at
																		// step
																		// 3

							notification.bigContentView = expandedView;

							NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
							mNotificationManager.notify(1, notification);
						}

						private void setListeners(RemoteViews bigContentView) {
							// TODO Auto-generated method stub
							// HelperActivity will be shown at step 4

							System.out
									.println("----clicked---bigContentView---"
											+ bigContentView);
						}
					});

			// Get notified on registration events
			PushManager.getInstance(getApplicationContext())
					.registerForRegistrationEvents(
							new PushAppsRegistrationInterface() {

								@Override
								public void onUnregistered(Context arg0,
										String arg1) {
									Log.d("PushAppsDemo",
											"Unregistered successfully");
								}

								@Override
								public void onRegistered(Context arg0,
										String arg1) {
									Log.e("PUSH TOKEN", arg1);
									push_tokens = arg1;
									// new loader_subjects().execute();
									System.out.println("===push token==="
											+ push_tokens);
									SharedPreferences.Editor prefEditors = myPref
											.edit();
									prefEditors.putString("DEVICE_ID",android_id);
									prefEditors.putString("TOKEN_ID",push_tokens);
									prefEditors.commit();
								}

								@Override
								public void onError(Context paramContext,
										String errorMessage) {
									Log.d("PushAppsDemo",
											"We encountered some error during the registration process: "
													+ errorMessage);
								}
							});
		} else {
			SharedPreferences.Editor prefEditors = myPref.edit();
			prefEditors.putString("DEVICE_ID", android_id);
			prefEditors.putString("TOKEN_ID", push_token);
			prefEditors.commit();

		}
		
		try {

			loginId = myPref.getString("USER_ID", "");
			user_type = myPref.getString("USER_TYPE", "");
			System.out.println("====--gvhgvhhg====--" + loginId);
			System.out.println("====--USER_TYPE====--" + user_type);
		} catch (Exception e1) {

			e1.printStackTrace();
		}

		handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				if (User_id.equalsIgnoreCase("")) {
					Intent intent = new Intent(getApplicationContext(),LoginScreen.class);
					startActivity(intent);
					overridePendingTransition(0, 0);
					finish();
				} else {
					Intent intent = new Intent(getApplicationContext(),
							Dashboard.class);
					startActivity(intent);
					overridePendingTransition(0, 0);
					finish();
				}

			}
		}, 3000);
	}
}
