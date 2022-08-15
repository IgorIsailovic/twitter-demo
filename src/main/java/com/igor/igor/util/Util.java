package com.igor.igor.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;


public class Util {
	
	private StringBuilder next= new StringBuilder();

	public URI uriFormater(HttpServletRequest request, Pageable paging, List<String> hashTag, List<String> username) {
		 URI uri = null;
		 
		 next.append(request.getRequestURL().toString()); //popravi
	
		 next.append("?limit="+paging.getPageSize()+"&");
		  
		 next.append("offset="+paging.next().getPageNumber());
		 
		 if(hashTag!=null) {
		 for(String h : hashTag) {
			 next.append("&hashTag=%23");
			 next.append(h.substring(1));
			 
		 }
		 }
		 if(username!=null) {
			 for(String u : username) {
				 next.append("&username=");
				 next.append(u.substring(1));
				
			 }
			 }
			try {
				uri = new URI(next.toString());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		
			return uri;
		
	}

}
