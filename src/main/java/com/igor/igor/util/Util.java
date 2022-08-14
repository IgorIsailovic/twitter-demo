package com.igor.igor.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;


public class Util {
	
	private StringBuilder next= new StringBuilder();

	public URL urlFormater(HttpServletRequest request, Pageable paging, List<String> hashTag) {
		 URL url=null;
		 next.append(request.getRequestURL().toString()); //popravi
		 next.append("?");
		 if(request.getParameter("limit")!=null) {
			  next.append("limit="+request.getParameter("limit")+"&");
		 }
		 if(request.getParameter("offset")!=null) {		  
		 next.append("offset="+paging.next().getPageNumber());
		 }
		 if(hashTag!=null) {
		 for(String h : hashTag) {
			 next.append("&hashTag=%23");
			 next.append(h.substring(1));
			 
		 }
		 }
			try {
				url = new URL(next.toString());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		
			return url;
		
	}

}
