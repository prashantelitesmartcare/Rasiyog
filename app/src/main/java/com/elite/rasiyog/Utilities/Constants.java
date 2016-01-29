package com.elite.rasiyog.Utilities;

public class Constants {


	//private static String HOST = "http://202.164.59.106/mobile/horoscope/";
   private static String HOST = "http://rasiyog.com/";

	
	
	// =========================================================//
	// ======================= All URL'S ======================//
	// =======================================================//
	
	
	//=============Get horoscope url===============//
	public static String GET_HOROSCOPE = HOST+"horoscope/get_horoscope?";//sunsign=libra&action=week
	
	//===================Login url=================//
	public static String LOGIN_URL = HOST+"user/sign_in?";//email=admin@gmail.com&password=admin
	
	//================ Fb Login Url =========================//
	public static String FB_LOGIN = HOST+"fb_login_user/fb_user?";//fb_id=1&name=gurwinder&&email=s@gmail.com
	
	//================ Google+ login ========================//
	public static String G_LOGIN = HOST+"google_login/google_user?";//google_id=22&name=gurjants&email=gurjants@gmail.com
	
	//==================Sign up url================//
	public static String SIGN_UP_URL = HOST+"user/sign_up?";//username=ram&dob=25/5/2013&pob=mohali&phoneno=8872852004&pass=123&email=a@gmail.com&gender=male&hrs=4&min=45
	
	//==================Get profile url================//
	public static String GET_PROFILE = HOST+"user/get_profile?";//
	
	//==================Edit profile url================//
	public static String EDIT_PROFILE = HOST+"user/edit_profile?";//id=321&username=anju&email=dasdd@gmail.com&phoneno=12334&dob=20/10/2015&pob=mohali
	
	//==================Change Password url================//
    public static String CHANGE_PASSWORD = HOST+"user/change_password?";//id=329&oldpass=111&newpass=123

   //==================About url================//
   public static String ABOUT_URL = HOST+"about/get_about?";//id=329
   
   //==================Horoscope Making url================//
   public static String HORODCOPE_MAKING_URL = HOST+"horoscope_making/add_horoscope_making?";//username=anju&gender=2&dob=28/4/2012&pob=mohali&phoneno=12345&pin_code=140307&address1=mohali&address2=landran&birth_hour=3a.m&birth_min=45&country=7&state=22&city=4&email=admin@gmail.com
  
   
   //==================Question Making url================//
   public static String QUESTION_URL = HOST+"question_answer/add_question_answer?";//name=gurii&email=s@gmail.com&phone_number=12345&gender=1&dob=7-jan-1993&place_of_birth=mohali&birth_hour=2a.m&birth_mint=45&question=how%20r%20u
   
   //================ Vastu type Url =========================//
   public static String TYPE_VASTU = HOST+"vastu/get_vastu_type";
   
   //================ Vastu Add Url =========================//
   public static String VASTU_URL = HOST+"vastu/add_vastu?";//vastu_type=home&dob=25-2-1988&description=saddsasdasd&place_of_birth=mohali&birth_hour=1a.m&birth_mint=25
   
   //================ Forgot Password Url =========================//
   public static String FORGOT_PASSWORD = HOST+"user/forgot_password?";
   
   //================ Match Making Url =========================//
   public static String MATCH_MAKING = HOST+"match_making/add_match_making?";//boy_fullname=gurii&boy_dob=22-2-1976&boy_birth_place=mohali&boy_birth_hour=2a.m&boy_birth_mint=45&girl_fullname=saby&girl_dob=2212-1978&girl_birth_place=mohali&girl_birth_hour=4a.m&girl_birth_mint=45
   
  
 //================Get Message Url =========================//
   public static String GET_MSG = HOST+"message/get_message?";//user_id
   
   //================Payment store Url =========================//
   public static String PAYMENT_URL = HOST+"user/payment_detail?";//user_id
   
}