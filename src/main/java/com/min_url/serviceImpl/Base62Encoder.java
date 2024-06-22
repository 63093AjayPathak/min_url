package com.min_url.serviceImpl;

import org.springframework.stereotype.Component;

import com.min_url.service.Encoder;

@Component
public class Base62Encoder implements Encoder {

	@Override
	public String encode(int id) {
		return this.base10toBase62(id);
	}
	
	private String base10toBase62(int id) {
		String elements = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		StringBuilder sb = new StringBuilder();
		
		while(id!=0) {
			sb.insert(0, elements.charAt(id%62));
			id/=62;
		}
		
		while(sb.length()<7){
			sb.insert(0, '0');
		}
		return sb.toString();
	}
	
	private int base62ToBase10(String minUrl) {
		int res=0;
		for(int i=0; i<minUrl.length(); i++) {
			int val = this.converCharToInt(minUrl.charAt(i));
			
			if(val==-1) return -1; // check for foreign characters in minURL
			
			res = res*62 + val;
		}
		
		return res;
	}
	
	private int converCharToInt(char c) {
		if(c>='0' && c<='9') return c-'0';
		else if(c>='a' && c<='z') return c-'a';
		else if(c>='A' && c<='Z') return c-'A';
		
		return -1;
	}

	@Override
	public int decode(String minUrl) {
		return this.base62ToBase10(minUrl);
	}

}
