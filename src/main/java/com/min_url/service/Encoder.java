package com.min_url.service;

public interface Encoder {
	public String encode(int id);
	
	public int decode(String minUrl);
}
