import java.util.*;

/**
 * 
 */

/**
 * @author mbhar
 *
 */
public class Page {
	String text;
	int docNo;
	Set<String> words;
	
	Page(int d, String t, Set<String> stopWords)
	{
		text = t;
		docNo = d;
		words  = new HashSet<>();
		
		String[] wordsInText = text.toLowerCase().replaceAll("[^a-z0-9 ]", " ").split(" ");
		for(String word : wordsInText)
			if(word.length() > 0 && !stopWords.contains(word))
				words.add(word);
		
		//System.out.print(docNo + "->");
		//for(String word : words)
		//	System.out.print(word + " ");
		//System.out.println();
	}
}