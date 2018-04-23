package Executing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class FileLoader {

	public Map <String, HashMap<Integer, Integer>> loadPostingList() throws IOException{
		// posting list map
		Map <String, HashMap<Integer, Integer>> postingList = new HashMap<String, HashMap<Integer, Integer>>();
		BufferedReader br = new BufferedReader(new FileReader("data/posting list.txt"));
		String line;
		while((line = br.readLine()) != null) {
			String[] arr = line.split("@@");
			String token = arr[0];
			HashMap<Integer, Integer> docFreq = new HashMap<Integer, Integer>();
			for (int i = 1; i < arr.length; i++) {
				docFreq.put(Integer.parseInt(arr[i].split(" ")[0]), Integer.parseInt(arr[i].split(" ")[1]));
			}
			postingList.put(token, docFreq);
		}
		br.close();
		return postingList;
	}

	public Map <String, Integer> loadTokenDict() throws IOException {
		// token dictionary map
		Map <String, Integer> tokenDict = new HashMap<String, Integer>();
		BufferedReader br2 = new BufferedReader(new FileReader("data/token dictionary.txt"));
		String line = "";
		while((line = br2.readLine()) != null) {
			String[] arr = line.split(" ");
			tokenDict.put(arr[0], Integer.parseInt(arr[2]));
		}
		br2.close();
		return tokenDict;
	}

	public Map <Integer, String[]> loadReviewTable() throws IOException{
		// reviews dict map
		Map <Integer, String[]> reviewTable = new HashMap<Integer, String[]>();
		BufferedReader br3 = new BufferedReader(new FileReader("data/review dictionary.txt"));
		String line = "";
		while((line = br3.readLine()) != null) {
			String[] arr = line.split("@@");
			String[] reviewInfo = {arr[1], arr[2], arr[3], arr[4], arr[5]};
			reviewTable.put(Integer.parseInt(arr[0]), reviewInfo);
		}
		br3.close();
		return reviewTable;
	}

	public Map <Integer, String[]> loadrestaurantTable() throws IOException{
		// restaurant map
		Map <Integer, String[]> restaurantTable = new HashMap<Integer, String[]>();
		BufferedReader br4 = new BufferedReader(new FileReader("data/restaurant dictionary.txt"));
		String line = "";
		while((line = br4.readLine()) != null) {
			String[] arr = line.split(" ");
			String[] details = {arr[1], arr[2]};
			restaurantTable.put(Integer.parseInt(arr[0]), details);
		}
		br4.close();
		return restaurantTable;
	}

	public Map <String, String[]> loadUrlMap() throws IOException{
		// url map
		Map <String, String[]> urlMap = new HashMap<String, String[]>();
		BufferedReader br5 = new BufferedReader(new FileReader("data/restaurant_url.txt"));
		String line = "";
		while((line = br5.readLine()) != null) {
			String[] arr = line.split(" @@ ");
			String[] restaurantInfo = {arr[1], arr[2], arr[3]};
			urlMap.put(arr[0], restaurantInfo);
		}
		br5.close();
		return urlMap;
	}

	public Map <String, HashSet<String>> loadSynonymsMap() throws IOException{
		// synonyms map
		Map <String, HashSet<String>> synonymsMap = new HashMap<String, HashSet<String>>();
		BufferedReader br6 = new BufferedReader(new FileReader("data/synonyms.txt"));
		String line = "";
		while((line = br6.readLine()) != null) {
			String[] arr = line.split("@@");
			HashSet<String> synonyms = new HashSet<String>();

			for (int i = 1; i < arr.length; i++) {
				String st = arr[i].replaceAll("\\s+","");
				synonyms.add(st);
			}
			String st = arr[0].replaceAll("\\s+","");
			synonymsMap.put(st, synonyms);
		}
		br6.close();
		return synonymsMap;
	}

	public Map <Integer, String> loadOriginalReviewMap() throws IOException{
		// original review map
		Map <Integer, String> originalReviews = new HashMap<Integer, String>();
		BufferedReader br7 = new BufferedReader(new FileReader("data/original review.txt"));
		String line = "";
		while((line = br7.readLine()) != null) {
			String[] arr = line.split("@@");
			originalReviews.put(Integer.parseInt(arr[0]), arr[1]);
		}
		br7.close();
		return originalReviews;
	}


}
