package com.iryelp;

import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import Executing.FileLoader;
import Executing.QueryRestaurants;
import Executing.SimilarityCalculator;
import Indexing.IndexReader;
import Indexing.IndexWriter;
import Indexing.Restaurant;
import Indexing.Review;
import PreProcess.ProcessDocuments;

public class AlternativeMain {

	public static void main(String[] args) throws IOException {
		
		// your input
		String userQuery = "awesome pizza";
		double alpha = 0.5;	// review-star weight			
		int topN = 5; 		// top N restaurants display
		int topK = 3;		// latest topK reviews display
		
		/*  
		 * Part 1: Load into memory when web page opens to reduce searching time
		 */
		
		FileLoader fl = new FileLoader();
		Map <String, HashMap<Integer, Integer>> postingList = fl.loadPostingList();	// posting list map
		Map <String, Integer> tokenDict = fl.loadTokenDict();							// token dict map
		Map <Integer, String[]> reviewTable = fl.loadReviewTable();					// reviews dict map
		Map <Integer, String[]> restaurantTable = fl.loadrestaurantTable();			// restaurant map
		Map <String, String[]> urlMap = fl.loadUrlMap();								// url map
		Map <String, HashSet<String>> synonymsMap = fl.loadSynonymsMap();
	
		/*
		 * Part 2: Review-score calculation and restaurant ranking	   
		 */
		
		List <Restaurant> myList = new ArrayList<Restaurant>();
		Map<Integer, Restaurant> finalRestaurantMap = new HashMap<Integer, Restaurant>();
		IndexReader reader = new IndexReader(userQuery, synonymsMap, tokenDict);
		HashSet<String[]> queries = reader.getQueries();

		// loop all queries 
		for (String[] query: queries) {
			System.out.print("Your query is: ");
			for (String s: query)
				System.out.print(s + " ");
			System.out.print("\n");
			
			QueryRestaurants qr = new QueryRestaurants(tokenDict, reviewTable, postingList, restaurantTable, reader);
			Map<Integer, Restaurant> restaurantMap = qr.getRestaurants(query, alpha);

			// combine restaurants of multiple queries
			for (Entry <Integer, Restaurant> entry: restaurantMap.entrySet()) {
				int restId = entry.getKey();
				Restaurant rest = entry.getValue();
				
				if (finalRestaurantMap.containsKey(restId)) {
					HashMap<Integer, Review> reviews = rest.getReviews();
					Restaurant existingRest = finalRestaurantMap.get(restId);
					HashMap<Integer, Review> existingReviews = existingRest.getReviews();
					for (Entry<Integer, Review> ety: reviews.entrySet()) {
						int reviewid = ety.getKey();
						if (existingReviews.containsKey(reviewid)) {
							double oldScore = existingReviews.get(reviewid).getScore();
							double newScore = ety.getValue().getScore();
							if (newScore > oldScore) {
								existingReviews.put(reviewid, ety.getValue());
							}
						}
					}
				} else {
					finalRestaurantMap.put(rest.getId(), rest);
				}
			}
		} // loop all queries end 
		
		// put restaurants into a list
		for (Entry <Integer, Restaurant> entry: finalRestaurantMap.entrySet()) {
			myList.add(entry.getValue());
		}
		
		// rank restaurants
		Collections.sort(myList, Collections.reverseOrder(new Comparator<Restaurant>() {
            public int compare(Restaurant a, Restaurant b) {
                return Double.compare(a.getRestaurantScore(), b.getRestaurantScore());
            }
	     }));
		
		// select and print topN restaurants
		if (!(myList.size() == 0)) {
			for (Restaurant r: myList.subList(0, topN)) {
				String restId = restaurantTable.get(r.getId())[0];
				String[] info = urlMap.get(restId);
				String name = info[0];
				String address = info[1];
				String url = info[2];
				Map<Integer, String> reviewMap = r.getTopKReviews(topK, reviewTable);
				
				System.out.println(r.getRestaurantScore() + " " + name + " " + address + " " + url);
				
				for (Entry<Integer, String> entry: reviewMap.entrySet())
					System.out.println("\t" + entry.getKey() + " " + entry.getValue());	
			}
		}
		
	}
	
}

/*
 * Part 0: Text file constructor. Don't execute unless you mess up the text files in data folder
 * 		   Need to delete 'reviews document.txt' each time before running      
 *
 */
	//ProcessDocuments pd = new ProcessDocuments();
	//pd.PreProcessDoc("data/id_reviews.txt");

	//IndexWriter writer = new IndexWriter("data/reviews document.txt");
	//writer.writeTermDictionary(100);



//	HashSet<String[]> queries = new HashSet<String[]>();
//	queries.add(new String[]{"awesom", "pizza"});
//	queries.add(new String[]{"great", "pizza"});


