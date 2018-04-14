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
	
	public TreeMap<Integer, String> getTopKReviews(int TopK, Map <Integer, String[]> reviewTable){
		TreeMap<Integer, String> reviewMap = new TreeMap (Collections.reverseOrder());
		TreeMap<Integer, String> result = new TreeMap (Collections.reverseOrder());
		
		for (Entry <Integer, Review> entry: reviews.entrySet()) {
			int reviewId = entry.getKey();
			int data = Integer.parseInt(reviewTable.get(reviewId)[3]);
			reviewMap.put(data, reviewTable.get(reviewId)[2]);
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







