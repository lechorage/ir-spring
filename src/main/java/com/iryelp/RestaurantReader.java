package com.iryelp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class RestaurantReader {
	
	public RestaurantReader() throws IOException{
		
	}
	
	public Map<String, String> loadNameWebAddress() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("data/name_webaddress.txt"));
		String line = "";
		Map<String, String> map = new HashMap<>();
		int count = 0;
		
		while((line = br.readLine())!=null && count < 50) {
			count++;
			String[] arr = line.split("@@");
			map.put(arr[1], arr[2]);
			
		//	System.out.println("map size is: " + map.size());
		//	System.out.println("Has read " + count + " lines");
			
			if (map.size()!=count) {
				for (String e: arr) {
					System.out.println(e);
				}
			}
			
		}
		br.close();
		return map;
	}
}
