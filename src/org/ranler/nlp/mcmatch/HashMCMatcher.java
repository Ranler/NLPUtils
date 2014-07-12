package org.ranler.nlp.mcmatch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class HashMCMatcher implements MCMatcherInterface {
	
	
	private List<String> _classNames;
	private List<HashMap<String, List<String>>> _classWords; 

	public HashMCMatcher() {
		_classNames = new ArrayList<String>();
		_classWords = new ArrayList<HashMap<String, List<String>>>();
	}

	@Override
	public void appendClass(String className, String filename) {
		HashMap<String, List<String>> classWord = new HashMap<String, List<String>>();
        try {
        	BufferedReader br = new BufferedReader(new FileReader(filename));
			String line = null;
			while ((line = br.readLine()) != null) {
				line = line.replaceAll("(\\r|\\n)", "").trim();
				String[] words = line.split(" ");
				List<String> prefixWords = classWord.get(words[0]);
				if (prefixWords != null) {
					// Insert LinkedList by string size
					int i = 0;
					for (String pw: prefixWords) {
						if (line.length() > pw.length()) {
							prefixWords.add(i, line);
							break;
						}
						i++;
					}
					if (i == prefixWords.size()) {
						prefixWords.add(line);
					}
				}
				else {
					prefixWords = new LinkedList<String>();
					prefixWords.add(line);
					classWord.put(words[0], prefixWords);
				}

			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        _classNames.add(className);
        _classWords.add(classWord);
        
	}


	@Override
	public List<WordClassInfo> matchWords(String str) {
		// split the str and get offset for every word
		str = str.replaceAll("(,|\\.|')", " ").replaceAll("(\\r|\\n)", "");
		String[] words = str.split(" ");
		int[] offsets = new int[words.length];
		for (int i=1; i<offsets.length; ++i) {
			offsets[i] = offsets[i-1] + words[i-1].length() + 1; 
		}
		
		List<WordClassInfo> wordClassInfos = new LinkedList<WordClassInfo>();
		for (int i=0; i<_classWords.size(); ++i) {
			// for every class
			HashMap<String, List<String>> classWord = _classWords.get(i);
			for (int j=0; j<words.length; ++j) {
				// for every word in str
				List<String> prefixWords = classWord.get(words[j]);
				if (prefixWords != null) {
					// find word in this class's prefix words
					for (String word : prefixWords) {
						String testStr = null;
						try {
							testStr = str.substring(offsets[j], offsets[j]+word.length());	
						} catch (Exception e) {
							continue;
						}
						if (word.equals(testStr)) {				
							wordClassInfos.add(new WordClassInfo(_classNames.get(i),
																 word,
									                             offsets[j],
									                             str
																));
							j += word.split(" ").length - 1;
							break;
						}
					}
				}
			}
			
		}
		return wordClassInfos;
	}


	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HashMCMatcher m = new HashMCMatcher();
		m.appendClass("job", "data/JOB_TITLE.txt");
		m.appendClass("death", "data/CAUSE_DEATH.txt");
		m.appendClass("charges", "data/CHARGES.txt");
		m.appendClass("religion", "data/RELIGION.txt");
		
		String sen1 = "He is an England writer, he is a teacher, he likes his wife. she is a Teacher of Remedial Reading and Writing.";
		List<WordClassInfo> wordClassInfos = m.matchWords(sen1);
		for (WordClassInfo wci: wordClassInfos) {
			System.out.println(wci);
		}
		
		String sen2 = "He is an England writer, he is a teacher, he likes his wife. she is a Teacher of Remedial Reading and Writing. he died of strep throat.He is charged of slander and libel.";		
		wordClassInfos = m.matchWords(sen2);
		for (WordClassInfo wci: wordClassInfos) {
			System.out.println(wci);
		}
		
		long beginTime = System.currentTimeMillis();
		for (int i=0; i<10000;i++)
			m.matchWords(sen2);
		System.out.println((System.currentTimeMillis() - beginTime)+"ms");


	}
}
