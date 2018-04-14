package Executing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Indexing.Restaurant;

public class SimilarityCalculator {

	private String[] query;
	private final int N = 1000000;
	HashMap<String, Integer> df = new HashMap<String, Integer>();
	HashMap<Integer, Map<String, Integer>> tf = new HashMap<Integer, Map<String, Integer>>();

	
	public SimilarityCalculator (String[] q, HashMap<String, Integer> df, HashMap<Integer, Map<String, Integer>> tf) {
		this.query = q;
		this.tf = tf;
		this.df = df;
	}
	
	public double getWeight(String term, int reviewId) {
		if (df.get(term) == null) {
			return 0;
		}
		int docFreq = df.get(term);
		int termFreq;
		
		if (tf.get(reviewId).get(term) == null) {
			return 0;
		} else {
			termFreq = tf.get(reviewId).get(term);
		}
		
		return (1 + Math.log(termFreq)) * Math.log10((N/docFreq));
	}
	
	public double[] getNumeratorDocVector(int reviewId) {
		double[] result = new double[query.length];
		for (int i = 0; i < query.length; i++) {
			result[i] = getWeight(query[i], reviewId);
		}
		return result;
	}
	
	public double[] getDenominatorDocVector (int reviewId, Map <Integer, String[]> reviewTable) {
		int length = Integer.parseInt(reviewTable.get(reviewId)[4]);
		double[] result = new double[length];
		String[] docContent =  reviewTable.get(reviewId)[2].split(" ");
		
		for (int i = 0; i < length; i++) {
			result[i] = getWeight(docContent[i], reviewId);			
		}
		return result;
	}
		
	public double[] getQueryVector() {
		double[] result = new double[query.length];
		Map<String, Integer> termFreqMap = new HashMap<String, Integer>();
		
		// query term frequency map
		for (String e: query) {
			if (termFreqMap.containsKey(e)) {
				int freq = termFreqMap.get(e);
				freq++;
				termFreqMap.put(e, freq);
			} else {
				termFreqMap.put(e, 1);
			}
		}
		// query weight
		for (int i = 0; i < query.length; i++) {
			if (df.get(query[i]) == null) {
				result[i] = 0;
				continue;
			}
			int docFreq = df.get(query[i]);
			int termFreq = termFreqMap.get(query[i]);
			result[i] = (1 + Math.log(termFreq)) * Math.log10((N/docFreq));
		}
		return result;
	}
	
	public double getSimilarity (double[] numeratorDocVector, double[] demonimatorDocVector, double[] queryVector) {		
		double docDen = 0;
		double queryDen = 0;
		double numerator = 0;
		
		for (int i = 0; i < queryVector.length; i++) {
			numerator += queryVector[i] * numeratorDocVector[i];
			queryDen += queryVector[i] *queryVector[i];
		}
		queryDen = Math.sqrt(queryDen);
		
		for (int i = 0; i < demonimatorDocVector.length; i++) {
			docDen += demonimatorDocVector[i] + demonimatorDocVector[i];
		}
		docDen = Math.sqrt(docDen);
				
		return numerator/(queryDen*docDen);
	}
	
	
	
}
