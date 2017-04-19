import java.io.*;
import java.util.*;

/**
 * 
 */

/**
 * @author madhu_bhargav
 *
 */
public class pageRank {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		File f = new File("./stops.txt");
		Scanner scan = new Scanner(f);
		Set<String> stopWords = new HashSet<>();
		Map<Integer, Page> documents = new TreeMap<>();
		Map<String, Set<Page>> wordLinks = new HashMap<>();
		
		while(scan.hasNext())
			stopWords.add(scan.next().toLowerCase());
		
		f = new File("./story.txt");
		scan = new Scanner(f);
		int docNo = 1;
		while(scan.hasNextLine())
		{
			Page page = new Page(docNo, scan.nextLine(), stopWords);
			documents.put(docNo, page);
			
			for(String word : page.words)
			{
				if(wordLinks.containsKey(word))
					wordLinks.get(word).add(documents.get(docNo));
				else
				{
					Set<Page> docSet = new HashSet<>();
					docSet.add(documents.get(docNo));
					wordLinks.put(word, docSet);
				}
			}
			docNo++;
		}
		
		for(int i = 0; i < 22; i++)
		{
			reset(documents);
			for(Map.Entry<Integer, Page> e : documents.entrySet())
			{
				Page p = e.getValue();
				Set<Page> links = new HashSet<>();
				for(String s : p.words)
					for(Page l : wordLinks.get(s))
						links.add(l);
				links.remove(p);
				p.calculateScore(links);
			}
		}
		
		
		Page[] pgs = new Page[documents.size()];
		pgs = documents.values().toArray(pgs);
		Arrays.sort(pgs);
		int x = 0;
		for(Page p : pgs)
		{
			System.out.println(p.docNo + " -> " + p.newRankScore);
			if(x++ > 10)
				break;
		}
	}
	
	public static void reset(Map<Integer, Page> documents)
	{
		for(Map.Entry<Integer, Page> e : documents.entrySet())
		{
			Page p = e.getValue();
			p.oldRankScore = p.newRankScore;
			p.newRankScore = 1.0;
		}
	}
}
