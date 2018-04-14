package Indexing;

public class Term {
	private String word; 
	private int id;
	private int freq;
	
	public Term(String t, int id, int freq) {
		word =t;
		this.id = id;
		this.freq = freq;
	}
	
	public String word() {
		return word;
	}
	
	public int id() {
		return id;
	}
	
	public int freq() {
		return freq;
	}
	
	
}
