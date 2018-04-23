package Indexing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class IndexWriter {
	String dir;
	
	public IndexWriter (String dir) throws IOException {
		this.dir = dir;
	}
	
	public void writeTermDictionary(int topN) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(dir));
		Map <String, int[]> tokenMap = new HashMap<String, int[]> ();
		Map <Integer, String> restuarantMap = new HashMap<Integer, String> ();
		Map <Integer, String[]> reviewMap = new HashMap<Integer, String[]> ();
		Map <String, Map<Integer, Integer>> postingList = new HashMap<String, Map<Integer, Integer>> ();
		List<Term> myList = new ArrayList<Term>();
		int restuarantCount = 0;
		int reviewId = 0;
		int termCount = 0;
		String line = "";
		
		while ((line = br.readLine()) != null) {
			if (line.contains("Restuarant:")) {
				
				// create restaurant map
				restuarantCount++;
				restuarantMap.put(restuarantCount, line.split(" ")[1]);
				
			} else if (!line.contains("Restuarant:") && line.split("@@").length >= 3) {
				
				// create review map
				String[] oneReview = line.split("@@");
				int length = oneReview[2].split(" ").length;
				String [] reviewDetail = {restuarantCount+"", oneReview[0], oneReview[2], oneReview[1], length+""};
				reviewId++;
				reviewMap.put(reviewId, reviewDetail);
				
				// create df map and posting list
				String[] tokens = oneReview[2].split(" ");
				Map <String, Integer> tokenFreq = new HashMap<String, Integer>();
				for (String token: tokens) {
					if (tokenFreq.containsKey(token)) {
						int docFreq = tokenFreq.get(token);
						docFreq++;
						tokenFreq.put(token, docFreq);
					} else {
						tokenFreq.put(token, 1);
					}
				}
				
				for (Entry<String, Integer> entry: tokenFreq.entrySet()) {
					String token = new String(entry.getKey());
					int docFreq = entry.getValue();
					
					// create token map
					if (tokenMap.containsKey(token)){
						int[] iddf = tokenMap.get(token);
						iddf[1] = iddf[1] + 1;
						tokenMap.put(token, iddf);
					} else {
						termCount++;
						int[] iddf = {termCount, 1};
						tokenMap.put(token, iddf);
					}
					
					// create posting list
					Map <Integer, Integer> frequencyMap = new HashMap<Integer, Integer>();
					
					if (postingList.containsKey(token)) {
						frequencyMap = postingList.get(token);
						frequencyMap.put(reviewId, docFreq);
						postingList.put(token, frequencyMap);
					} else {
						frequencyMap.put(reviewId, docFreq);
						postingList.put(token, frequencyMap);
					}
				}
				
			} else {
				continue;
			}
		}
		br.close();
		
		// total review each restaurant has
		Map<Integer, Integer> restTotalReview = new HashMap<Integer, Integer>();
		for (int restaurantId: restuarantMap.keySet()) {
			int n = 0;
			for (String[] review: reviewMap.values()) {
				if ((Integer.parseInt(review[0])) == restaurantId) {
					n++;
				}
			}
			restTotalReview.put(restaurantId, n);
		}
		
		// write restaurant dictionary txt
		FileWriter frRestaurant = new FileWriter("data/restaurant dictionary.txt");
		for(Entry<Integer, String> entry: restuarantMap.entrySet()) {
			frRestaurant.write(entry.getKey() + " " + entry.getValue() + " " + restTotalReview.get(entry.getKey()) + "\n");
		}
		frRestaurant.close();
		
		// write reviews dictionary txt
		FileWriter frReview = new FileWriter("data/review dictionary.txt");
		for(Entry<Integer, String[]> entry: reviewMap.entrySet()) {
			frReview.write(entry.getKey() + "@@" + entry.getValue()[0] + "@@" + entry.getValue()[1] + "@@" + entry.getValue()[2] + "@@" + entry.getValue()[3] + "@@" + entry.getValue()[4] +"\n");
		}
		frReview.close();
		
		
		
		
		// write token dictionary txt
		FileWriter frToken = new FileWriter("data/token dictionary.txt");
		for(Entry<String, int[]> entry: tokenMap.entrySet()) {
			frToken.write(entry.getKey() + " " + entry.getValue()[0] + " " + entry.getValue()[1] + "\n");
		}
		frToken.close();
		
		// write posting list txt
		FileWriter frPostingList = new FileWriter("data/posting list.txt");
		for(Entry<String, Map<Integer, Integer>> entry: postingList.entrySet()) {
			frPostingList.write(entry.getKey()+"@@");
			for (Entry<Integer, Integer> e: entry.getValue().entrySet()) {
				frPostingList.write(e.getKey() + " " + e.getValue() + "@@");
			}
			frPostingList.write("\n");
		}
		frPostingList.close();
	}
	
}


/*
		FileWriter fr = new FileWriter("data/term dictionary.txt");
		for(Entry<String, int[]> entry: termMap.entrySet()) {
			fr.write(entry.getKey() + " " + entry.getValue()[0] + " " + entry.getValue()[1] + "\n");
			Term term = new Term(entry.getKey(), entry.getValue()[0], entry.getValue()[1]);
			myList.add(term);
		}
		fr.close();
		 Collections.sort(myList, Collections.reverseOrder(new Comparator<Term>() {
	            public int compare(Term a, Term b) {
	                return Double.compare(a.freq(), b.freq());
	            }
	     }));
		
		 FileWriter fr2 = new FileWriter("data/top 100 terms.txt");
		 for (Term term: myList.subList(0, topN)) {
			 fr2.write(term.word() + " " + term.freq() + "\n");
		 }
		 fr2.close();





*/