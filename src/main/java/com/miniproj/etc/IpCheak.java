package com.miniproj.etc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class IpCheak {

	private static String ipAddr;
	
	public static String getIpAddr() throws IOException {
		URL ipCheakURL = new URL("https://checkip.amazonaws.com/");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(ipCheakURL.openStream()));
		
		ipAddr = br.readLine();
		
		return ipAddr;
		
	}
}
