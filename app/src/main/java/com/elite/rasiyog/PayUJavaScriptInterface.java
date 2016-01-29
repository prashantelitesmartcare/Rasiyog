package com.elite.rasiyog;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

/**
 * Created by Sarbjyot_2 on 1/27/2016.
 */
public class PayUJavaScriptInterface {
    Context mContext;
    Handler mHandler;


    PayUJavaScriptInterface(Context c) {
        mContext = c;
    }


    public void success(long id, String paymentId){
        Log.e("PAYMET_ID-----", "" + paymentId);
        Intent i = new Intent(mContext,Second_Activity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.putExtra("Status", "success");
        i.putExtra("Paymet_id", paymentId);
        mContext.startActivity(i);


    }

    public void success1(long id,  String paymentId){
        Intent i = new Intent(mContext,Third.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mContext.startActivity(i);

    }

}
