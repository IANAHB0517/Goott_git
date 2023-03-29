package com.miniproj.etc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class IPCheck {
	
	private static String ipAddr;
	
	public static String getIPAddr() throws IOException {
		
		URL ipCheckURL = new URL("https://checkip.amazonaws.com/");
		
		BufferedReader br =  new BufferedReader(new InputStreamReader(ipCheckURL.openStream()));
		
		ipAddr = br.readLine();
		
		return ipAddr;
	}
}
