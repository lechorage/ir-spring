package Indexing;

public class Review {
	int review;
	double score;
	
	public Review (int review, double score) {
		this.review = review;
		this.score = score;
	}
	
	public Review () {
		review = 0;
		score = 0;
	}
	
	public int getReviewId() {
		return review;
	}
	
	public double getScore() {
		return score;
	}
}
