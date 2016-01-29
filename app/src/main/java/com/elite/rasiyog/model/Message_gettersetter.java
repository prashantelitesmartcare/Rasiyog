package com.elite.rasiyog.model;

public class Message_gettersetter {
	
	
	String name,message,type,date,time,image,attachment;

	public Message_gettersetter(String name, String message, String type,
			String date, String time, String image, String attachment) {
		super();
		this.name = name;
		this.message = message;
		this.type = type;
		this.date = date;
		this.time = time;
		this.image = image;
		this.attachment = attachment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	
}
