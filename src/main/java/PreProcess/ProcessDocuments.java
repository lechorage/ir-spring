package PreProcess;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class ProcessDocuments {
	
	public static void main(String[] args) throws IOException {
		getOriginalReviews("data/id_reviews.txt");
		
	}
	
	public static void getOriginalReviews(String dir) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(dir));
		FileWriter wr = new FileWriter("data/original review.txt");
		String line = "";
		int count = 0;
		
		while ((line = br.readLine()) != null) {
			String [] reviews = line.split(" @@ ");
			String docNo = reviews[0];
			
			for (int i = 1; i < reviews.length; i++) {
				count++;
				int idx = reviews[i].indexOf(",", 13) + 2;
				int lastIdx = reviews[i].lastIndexOf("'");
				String review = new String();
				if (lastIdx == -1 || lastIdx < idx)
					review = reviews[i].substring(idx);	
				else 
					review = reviews[i].substring(idx, lastIdx);	
				
				String new1 = new String();
				String newR = new String();
				new1 = review.replaceAll("\\\\'", "'");
				newR = new1.replaceAll("\\\\n", "");
				
				wr.write(count + "@@" + newR + "\n");
			}
		}
		br.close();
		wr.close();
	}
	
	
	public void PreProcessDoc(String dir) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(dir));
		String line = "";
		
		while ((line = br.readLine()) != null) {
			String [] reviews = line.split(" @@ ");
			String docNo = reviews[0];
			FileWriter wr = new FileWriter("data/reviews document.txt", true);
			wr.append("Restuarant: " + docNo + "\n");
			
			for (int i = 1; i < reviews.length; i++) {
				
				int idx = reviews[i].indexOf(",", 13) + 2;
				String rating = reviews[i].substring(0, 1);
				String day = reviews[i].substring(3, 13).replace("-", "");
				char[] review = reviews[i].substring(idx).toCharArray();
				
				wr.append(rating + "@@" + day + "@@");
				
				WordTokenizer tokenizer = new WordTokenizer(review);
				StopWordRemover stopwordRemover = new StopWordRemover();
				WordNormalizer normalizer = new WordNormalizer();
				char[] word = null;
				
				while ((word = tokenizer.nextWord()) != null) {
					word = normalizer.lowercase(word);
					if (!stopwordRemover.isStopword(word)) {
						wr.append(normalizer.stem(word) + " ");
					}
				}
				wr.append("\n");
			}
			wr.close();
		}
		br.close();
	}
	
	public String[] PreProcessQuery(String query) throws IOException {
		String[] terms = query.split(" ");
		String[] results = new String[terms.length];
		
		for (int i = 0; i < terms.length; i++) {
			
			WordTokenizer tokenizer = new WordTokenizer(terms[i].toCharArray());
			StopWordRemover stopwordRemover = new StopWordRemover();
			WordNormalizer normalizer = new WordNormalizer();
			char[] word = null;
			
			String result = "";
			while ((word = tokenizer.nextWord()) != null) {
				word = normalizer.lowercase(word);
				if (!stopwordRemover.isStopword(word)) {
					result = result + normalizer.stem(word);
				}
			}
			results[i] = result;
		}
		return results;
	}
	
	
	
	
	
}
