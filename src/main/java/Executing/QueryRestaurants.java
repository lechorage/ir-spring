package Executing;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import Indexing.IndexReader;
import Indexing.Restaurant;
import Indexing.Review;

public class QueryRestaurants {
	
	Map <String, Integer> tokenDict = new HashMap<String, Integer>();
	Map <Integer, String[]> reviewTable = new HashMap <Integer, String[]>();
	Map <String, HashMap<Integer, Integer>> postingList = new HashMap <String, HashMap<Integer, Integer>>();
	Map <Integer, String[]> restaurantTable = new HashMap <Integer, String[]>();
	IndexReader reader;
	
	public QueryRestaurants(Map <String, Integer> tokenDict, Map <Integer, String[]> reviewTable, Map <String, HashMap<Integer, Integer>> postingList, Map <Integer, String[]> restaurantTable, IndexReader reader) {
		this.tokenDict.putAll(tokenDict);
		this.reviewTable.putAll(reviewTable);
		this.postingList.putAll(postingList);
		this.restaurantTable.putAll(restaurantTable);
		this.reader = reader;
	}
	
	public HashMap<Integer, Restaurant> getRestaurants(String[] query, double alpha) throws IOException{
		HashMap<Integer, Restaurant> restaurantMap = new HashMap<Integer, Restaurant>();
		HashMap<String, Integer> df = reader.getDocumentFrequency(tokenDict, query);			// get document frequency map
		HashMap<Integer, Map<String, Integer>> tf = reader.getTermFrequency(postingList, query);  // get term frequency map

		// calculate tf-idf score of each review and add to restaurant map
		SimilarityCalculator sc = new SimilarityCalculator(query, df, tf);		
		double[] queryVector = sc.getQueryVector();
		
		for (Entry <Integer, Map<String, Integer>> entry: tf.entrySet()) {
			int reviewId = entry.getKey();
			String[] arr = reviewTable.get(reviewId);
			int restaurantId = Integer.parseInt(arr[0]);
			int totalReviews = Integer.parseInt(restaurantTable.get(restaurantId)[1]);
			int star = Integer.parseInt(arr[1]);
			
			double[] numeratorDocVector = sc.getNumeratorDocVector(reviewId);
			double[] denominatorDocVector = sc.getDenominatorDocVector(reviewId, reviewTable);
			double similarity = sc.getSimilarity(numeratorDocVector, denominatorDocVector, queryVector);

			double score = (1 - alpha)*similarity + alpha*(star/5);
			Review review = new Review(reviewId, score);
			
			// calculate restaurant score (total review score / number of reviews)
			if (restaurantMap.containsKey(restaurantId)) {
				Restaurant oldRest = restaurantMap.get(restaurantId);
				HashMap<Integer, Review> reviews = oldRest.getReviews();
				reviews.put(review.getReviewId(), review);
				int ttReview = oldRest.getTotalReview();
				Restaurant newRest = new Restaurant(restaurantId, reviews, ttReview);
				restaurantMap.put(restaurantId, newRest);
			} else {
				HashMap<Integer, Review> reviews = new HashMap<Integer, Review>();
				reviews.put(review.getReviewId(), review);
				Restaurant newRest = new Restaurant(restaurantId, reviews, totalReviews);
				restaurantMap.put(restaurantId, newRest);
			}
		}
		
		return restaurantMap;
	}
}
