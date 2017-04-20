import java.io.*;
import java.util.*;

/**
 * @author madhu_bhargav
 * this is the main class and this
 * class uses the Page class to represent 
 * each sentence in the document as a page
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
		
		//creating a set of all stop words
		while(scan.hasNext())
			stopWords.add(scan.next().toLowerCase());
		
		f = new File("./story.txt");
		scan = new Scanner(f);
		int docNo = 0;
		//scan through the story file and 
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
		
		//building word counts
		for(String word : wordLinks.keySet())
			words.put(word, wordLinks.get(word).size());
		
		//matrices for PageRank
		double[][] matrix = new double[docNo][docNo];
		double[] ranks = new double[docNo];
		Arrays.fill(ranks, 1.0);
		
		//building the initial matrix
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
		
		SortedSet<Integer> topTenDocs = new TreeSet<Integer>();
		//repeat the matrix multiplication until convergence
		while(true)
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
			Page[] pgs = new Page[documents.size()];
			pgs = documents.values().toArray(pgs);
			Arrays.sort(pgs);
			int x = 0;
			
			SortedSet<Integer> tempDocs = new TreeSet<Integer>();
			for(Page p : pgs)
			{
				tempDocs.add(p.docNo);
				if(++x >= 10)
					break;
			}
			
			int diff = 0;
			for(int p : tempDocs)
			{
				if(!topTenDocs.contains(p))
					diff++;
			}
			
			topTenDocs = tempDocs;
			//check for convergence
			if(diff == 0)
				break;
		}
		
		System.out.println("PageRank Algorithm");
		System.out.println("------------------");
		
		//Print statements to print the 10 highest ranked sentences, in the order they appear in the text
		System.out.printf("\na) The 10 highest ranked sentences, in the order they appear in the text:");
		System.out.printf("\n-------------------------------------------------------------------------\n");
		for(int d:topTenDocs)
			System.out.printf("%-5d: %s\n", d, documents.get(d).text);
		
		
		//Print statements to print the 10 highest ranked keywords, ordered by rank, highest ranked first
		System.out.printf("\n\nb) The 10 highest ranked keywords, ordered by rank, highest ranked first:");
		System.out.printf("\n-------------------------------------------------------------------------\n");
		int x = 0;
		for(Map.Entry<String, Integer> s : entriesSortedByValues(words))
		{
			System.out.printf("%-10s: %d\n", s.getKey(), s.getValue());
			if(++x > 10)
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
