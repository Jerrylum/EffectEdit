package io.Jerry.EffectEdit.Util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {
	private static ResourceBundle res = null;
	//private static Map<String, MessageFormat> messageFormatCache = new HashMap<String, MessageFormat>();
	
	public static void run(String locale){
		if(locale.indexOf("-1") == -1){
			res = ResourceBundle.getBundle("messages" , new Locale(locale));
		}else{
			String[] str = locale.split("_");
			res = ResourceBundle.getBundle("messages" , new Locale(str[0],str[1]));
		}
		
		//res = ResourceBundle.getBundle("messages", new Locale("en", "US"));
	}
	
	public static String t(String str, Object... obj){
		String format = res.getString(str);
		MessageFormat messageFormat;
//		MessageFormat messageFormat = (MessageFormat)messageFormatCache.get(format);
//	    if (messageFormat == null){
	    	try{
	    		messageFormat = new MessageFormat(format);
	    	}catch (IllegalArgumentException e){
	    		format = format.replaceAll("\\{(\\D*?)\\}", "\\[$1\\]");
	        	messageFormat = new MessageFormat(format);
	    	}
//	    	messageFormatCache.put(format, messageFormat);
//	   }
	   return messageFormat.format(obj);
	}
}
