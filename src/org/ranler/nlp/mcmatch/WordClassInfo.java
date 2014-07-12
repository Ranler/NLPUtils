package org.ranler.nlp.mcmatch;

public class WordClassInfo {

	private String _className;
	private String _word;
	private String _sentence;
	private int _offset;

	
	public WordClassInfo(String className, String word) {
		_className = className;
		_word = word;
	}
	
	public WordClassInfo(String className, String word, int offset, String sentence) {
		_className = className;
		_word = word;
		_offset = offset;
		_sentence = sentence;
	}
	
	public String getClassName() {
		return _className;
	}

	public String getWord() {
		return _word;
	}

	public int getOffset() {
		return _offset;
	}
	
	public String getSentence() {
		return _sentence;
	}
	
	public String toString() {
		StringBuilder strBuf = new StringBuilder();
		strBuf.append(_className);
		strBuf.append(":");
		strBuf.append(_word);
		strBuf.append(":");
		strBuf.append(_offset);
		strBuf.append("\n");
		if (_sentence != null) {
			strBuf.append(_sentence);
			strBuf.append("\n");
			for (int i=0; i<_offset;i++) {
				strBuf.append(" ");
			}
			for (int i=0; i<_word.length();i++) {
				strBuf.append("^");
			}
		}
		return strBuf.toString();
		
	}

}
