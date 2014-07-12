package org.ranler.nlp.mcmatch;

import java.util.List;

public interface MCMatcherInterface {
	
	public void appendClass(String className, String filename);
		
	public List<WordClassInfo> matchWords(String str);
		

}
