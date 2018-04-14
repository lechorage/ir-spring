package PreProcess;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class StopWordRemover {
	//you can add essential private methods or variables.
	private FileReader fr;
	private BufferedReader br;
	private Set<String> stopSet;
	String line = "";
	
	public StopWordRemover( ) throws IOException {
		// load and store the stop words from the fileinputstream with appropriate data structure
		// that you believe is suitable for matching stop words.
		// address of stopword.txt should be Path.StopwordDir
		
		fr = new FileReader("data/stopword.txt");
		br = new BufferedReader(fr);
		stopSet = new HashSet<String>();
		
		// load all stop words into an arraylist
		while((line = br.readLine()) != null){
			//arrList.add(line);
			stopSet.add(line);
		}
		fr.close();
		br.close();
	}
	
	public boolean isStopword( char[] word ) {
		// return true if the input word is a stopword, or false if not
		String b = new String(word);
		if (stopSet.contains(b))    //find if the word is a stop word
			return true;
		else 
			return false;
	}
}
