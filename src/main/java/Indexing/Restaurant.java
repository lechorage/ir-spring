package Indexing;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Collections;
import java.util.HashMap;

public class Restaurant {
	
	double averageScore;
	int id;
	int totalReviews;
	HashMap<Integer, Review> reviews = new HashMap<Integer, Review>();
	
	public Restaurant(int id, HashMap<Integer, Review> reviews, int totalReviews) {
		this.id = id;
		this.reviews.putAll(reviews);
		this.totalReviews = totalReviews;
	}
	
	public double getRestaurantScore() {
		double totalScore = 0;
		for (Entry <Integer, Review> entry: reviews.entrySet()) {
			totalScore += entry.getValue().getScore();
		}
		return totalScore/totalReviews;
	}
	
	public HashMap<Integer, Review> getReviews(){
		return reviews;
	}
	
	public int getId() {
		return id;
	}
	
	public int getTotalReview() {
		return totalReviews;
	}
	
	public TreeMap<Integer, String> getTopKReviews(int TopK, Map <Integer, String[]> reviewTable, Map<Integer, String> originalReviews){
		TreeMap<Integer, String> reviewMap = new TreeMap (Collections.reverseOrder());
		TreeMap<Integer, String> result = new TreeMap (Collections.reverseOrder());
		
		for (Entry <Integer, Review> entry: reviews.entrySet()) {
			int reviewId = entry.getKey();
			if (reviewId >= 48277) { // number 48277 in the original review table are all stop words, therefore is deleted in stemmed review map
				reviewId += 1;;
			}
			int date = Integer.parseInt(reviewTable.get(reviewId)[3]);
		
			reviewMap.put(date, originalReviews.get(reviewId));
		}
		
		int count = 0;
		for (Entry <Integer, String> entry: reviewMap.entrySet()) {
			result.put(entry.getKey(), entry.getValue());
			count++;
			if (count == TopK) break;
		}
		return result;	
	}
	
	
	
	
	
	
}







