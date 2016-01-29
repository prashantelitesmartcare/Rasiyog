package com.elite.rasiyog.Adapters;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.elite.rasiyog.R;
import com.elite.rasiyog.RoundedTransformation;
import com.elite.rasiyog.model.Message_gettersetter;
import com.squareup.picasso.Picasso;

import java.util.List;


public class Message_Adpter extends ArrayAdapter<Message_gettersetter> {
	 
	 
	 Activity context;
	 LayoutInflater inflater;
	 List<Message_gettersetter> list;
	 SharedPreferences myPref;
	 String real_user_type;
	 
	 public Message_Adpter(Activity context, int resourceId,
	   List<Message_gettersetter> list) {
	  super(context, resourceId, list);
	  this.context = context;
	  this.list = list;
	  inflater = LayoutInflater.from(context);
	
	 }


	 @Override
	 public int getCount() {
	  // TODO Auto-generated method stub
	  return list.size();
	 }

	 public List<Message_gettersetter> getWorldPopulation() {
	  return list;
	 }

	 @Override
	 public long getItemId(int arg0) {
	  // TODO Auto-generated method stub
	  return 0;
	 }

	 
	 ViewHolderItem viewHolder;
	 @Override
	 public View getView(final int position, View view, ViewGroup arg2) {
	   ViewHolderItem holder;
	 
	   holder = new ViewHolderItem();
	  
	   
	
	    view = inflater.inflate(R.layout.message_view, null);
	 
	   // Locate the TextViews in listview_item.xml
	   holder.message = (TextView) view.findViewById(R.id.desc);
	   holder.name = (TextView) view.findViewById(R.id.name);
	  // holder.attachment = (TextView) view.findViewById(R.id.attachment);
	   holder.user_image = (ImageView) view.findViewById(R.id.user_image);
	   view.setTag(holder);

	   holder = (ViewHolderItem) view.getTag();
	   
	 /*  holder.attachment.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent i = new Intent(context,DatashowScreen.class);
			i.putExtra("url", list.get(position).getAttachment());
			context.startActivity(i);
			context.overridePendingTransition(0, 0);
		}
	});*/
	   
	   
	   holder.message.setText(list.get(position).getMessage());
	   holder.name.setText(list.get(position).getName());
	  if(list.get(position).getImage().equalsIgnoreCase(""))
	  {}
	  else
	  {
		  Picasso.with(context).load(list.get(position).getImage()).transform(new RoundedTransformation(50, 4))
			.resize(110, 110)
			.centerCrop().into(holder.user_image);
	  }
	  // holder.user_image.setBackgroundResource(list.get(position).getImage());
	 
	  return view;
	 }
	 
	 static class ViewHolderItem {
	  ImageView  user_image;
	  TextView message,time,name,date,attachment;
	 }

	}