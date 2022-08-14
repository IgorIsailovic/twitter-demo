package com.igor.igor.util;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;


public class Util {
	
	public URL urlFormater(HttpServletRequest request) {

		 URL url=null;
		 String limit="";
		 int off=0;
		 String offset="";
		 String urlBase = request.getRequestURL().toString()+"?";
		 if(request.getParameter("limit")!=null) {
			 limit = "limit="+request.getParameter("limit")+"&";
		 }
		 if(request.getParameter("offset")!=null) {		  
		 off = Integer.parseInt(request.getParameter("offset"))+1;
		 offset =  "offset=" + off;
		 }
		
		 
			try {
				url = new URL(urlBase + limit + offset);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		
			return url;
		
	}

}
