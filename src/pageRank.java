import java.io.*;
import java.util.*;

/**
 * 
 */

/**
 * @author mbhar
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
		Map<Integer, Page> documents = new HashMap<>();
		
		while(scan.hasNext())
			stopWords.add(scan.next().toLowerCase());
		
		f = new File("./story.txt");
		scan = new Scanner(f);
		int docNo = 1;
		while(scan.hasNextLine())
		{
			Page page = new Page(docNo, scan.nextLine(), stopWords);
			documents.put(docNo++, page);
		}
	}
}
