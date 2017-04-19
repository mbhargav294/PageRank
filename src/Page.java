import java.util.*;

/**
 * 
 */

/**
 * @author vyshnavi_bandi
 *
 */
public class Page implements Comparable<Page>{
	String text;
	int docNo;
	double oldRankScore;
	double newRankScore;
	int numberOfLinks;
	Set<String> words;
	Set<Page> links;
	
	Page(int d, String t, Set<String> stopWords)
	{
		text = t;
		docNo = d;
		oldRankScore = 1.0;
		newRankScore = 1.0;
		numberOfLinks = 0;
		words  = new HashSet<>();
		links = new HashSet<>();
		
		String[] wordsInText = text.toLowerCase().replaceAll("[^a-z0-9 ]", " ").split(" ");
		for(String word : wordsInText)
			if(word.length() > 1 && !stopWords.contains(word))
				words.add(word);
	}
	
	void calculateScore(Set<Page> links)
	{
		this.links = links;
		numberOfLinks = links.size();
		for(Page p : links)
		{
			p.newRankScore += oldRankScore/(double)numberOfLinks;
		}
	}
	
	@Override
	public int hashCode()
	{
		return this.docNo;
	}

	@Override
	public int compareTo(Page p) {
		double x = this.newRankScore - p.newRankScore;
		if(x < 0)
			return 1;
		else if(x == 0)
			return 0;
		else
			return -1;
	}
}