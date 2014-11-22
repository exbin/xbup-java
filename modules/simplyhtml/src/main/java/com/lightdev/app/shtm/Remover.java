package com.lightdev.app.shtm;

import java.util.Locale;

class Remover {
	final private String searchedText;
	private String searchedSubstring;
	private int begin;
	private int end;
	private int removedCharacterNumber;
	private String processedText;
	
	public String getProcessedText() {
		return processedText;
	}
	public Remover(String text) {
		super();
		this.processedText = text;
		this.searchedText = text.toLowerCase(Locale.ENGLISH);
		begin = end = removedCharacterNumber = 0;
	}
	public int getBegin() {
		return begin;
	}
	public int getEnd() {
		return end;
	}
	
	private String createSearchedSubstring(final String substring) {
		return "<" + substring.toLowerCase(Locale.ENGLISH);
	}
	
	public boolean findFirst(){
		begin = removedCharacterNumber;
		return findNext();
	}
	
	public boolean findNext(){
		begin = searchedText.indexOf(searchedSubstring, begin);
		return findEndOfElement();
	}

	public boolean findLast(){
		begin = searchedText.lastIndexOf(searchedSubstring);
		return findEndOfElement();
	}
	
	private boolean findEndOfElement() {
		if(begin == -1)
		{
			end = -1;
			return false;
		}
		end = searchedText.indexOf('>', begin + searchedSubstring.length());
		if(end == -1){
			begin = -1;
			return false;
		}
		end++;
		return true;
	}
	
	public int getWhiteSpaceBefore(){
		if(begin <= 0)
			return begin;
		for(int i = begin - 1; i > 0; i--){
			if(! Character.isWhitespace(searchedText.charAt(i)))
				return i + 1;
		}
		return 0;
	}
	
	public int getWhiteSpaceAfter(){
		if(end == -1)
			return -1;
		for(int i = end + 1; i < searchedText.length(); i++){
			if(! Character.isWhitespace(searchedText.charAt(i)))
				return i;
		}
		return searchedText.length();
	}
	
	public Remover removeFirstAndBefore(String element){
		searchedSubstring = createSearchedSubstring(element);
		if(findFirst()){
			final int lastIndex = getWhiteSpaceAfter() - removedCharacterNumber;
			processedText = processedText.substring(lastIndex);
			removedCharacterNumber += lastIndex;
		}
		return this;
	}
	
	public Remover removeLastAndAfter(String element){
		searchedSubstring = createSearchedSubstring(element);
		if(findLast()){
			final int firstIndex = getWhiteSpaceBefore() - removedCharacterNumber;
			processedText = processedText.substring(0, firstIndex);
		}
		return this;
	}
	
	static public void main(String[] argv){
		String text = "\n\t<body> 1 </body>";
		final Remover removeFirstAndBefore = new Remover(text).removeFirstAndBefore("body");
		String removeLastAndAfter = removeFirstAndBefore.removeLastAndAfter("/body").getProcessedText();
		System.out.println ('"' + removeLastAndAfter + '"');
		// assert removeLastAndAfter.equals("1");
	}

}
