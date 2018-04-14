package PreProcess;

public class WordTokenizer {
	//you can add essential private methods or variables
	private char[] chArray;
	private int len;
	private int index;
	
	
	// YOU MUST IMPLEMENT THIS METHOD
	public WordTokenizer( char[] texts ) {
		// this constructor will tokenize the input texts (usually it is a char array for a whole document)
		chArray = texts;
		len = chArray.length;
		index = 0;
	}
	
	// YOU MUST IMPLEMENT THIS METHOD
	public char[] nextWord() {
		// read and return the next word of the document
		// or return null if it is the end of the document
		
		String s = "";
		// before reaching the end of text
		if (index < len) { 
			// loop through text
			for (int i = index; i < len; i++) {	
				index++;
				// exclude space and punctuation
				if (chArray[i] != ' ' && Character.isLetter(chArray[i])) {	
					 s = s + chArray[i];
				} 
				// tokenize when hitting a space or hitting the end of text
				if ((chArray[i] == ' ' || i == len - 1) && s != "") {
					char[] e = s.toCharArray();
					return e;
				}
			}
		}
		return null;
	}
	
}
