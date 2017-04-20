import java.io.*;
import java.util.*;
import java.util.Map.Entry;

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
		Map<String, Integer> words = new TreeMap<>();
		
		while(scan.hasNext())
			stopWords.add(scan.next().toLowerCase());
		
		f = new File("./story.txt");
		scan = new Scanner(f);
		int docNo = 0;
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
		
		for(String word : wordLinks.keySet())
			words.put(word, wordLinks.get(word).size());
		
		System.out.println("Top 10 key words are:");
		int x = 0;
		for(Entry<String, Integer> s : entriesSortedByValues(words))
		{
			System.out.println(s.getKey() + " " + s.getValue());
			if(++x > 10)
				break;
		}
		
		double[][] matrix = new double[docNo][docNo];
		double[] ranks = new double[docNo];
		Arrays.fill(ranks, 1.0);
		
		for(Map.Entry<Integer, Page> e : documents.entrySet())
		{
			Page p = e.getValue();
			Set<Page> links = new HashSet<>();
			for(String s : p.words)
				for(Page l : wordLinks.get(s))
					links.add(l);
			links.remove(p);
			p.assignLinks(links);
			for(Page s : links)
				matrix[e.getKey()][s.docNo] = 1.0/p.numberOfLinks;
		}
		
		for(int itr = 0; itr < 10; itr++)
		{
			double[] newRanks = new double[docNo];
			for(int i = 0; i < matrix.length; i++)
			{
				for(int j = 0; j < matrix[i].length; j++)
				{
					newRanks[i] += matrix[i][j] * ranks[j];
				}
			}
			for(int i = 0; i < docNo; i++)
				documents.get(i).rankScore = newRanks[i];
			ranks = Arrays.copyOf(newRanks, newRanks.length);
		}
		
		Page[] pgs = new Page[documents.size()];
		pgs = documents.values().toArray(pgs);
		Arrays.sort(pgs);
		x = 0;
		for(Page p : pgs)
		{
			System.out.println(p.docNo + " -> " + p.rankScore);
			if(x++ > 10)
				break;
		}
	}
	
	static <K,V extends Comparable<? super V>>
	SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
	    SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
	        new Comparator<Map.Entry<K,V>>() {
	            @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
	                int res = e2.getValue().compareTo(e1.getValue());
	                return res != 0 ? res : 1;
	            }
	        }
	    );
	    sortedEntries.addAll(map.entrySet());
	    return sortedEntries;
	}
}
