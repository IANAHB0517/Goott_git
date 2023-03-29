package com.miniproj.vodto;

public class SearchCriteria {
	private String searchType;
	private String searchWord;
	
	public SearchCriteria(String searchType, String searchWord) {
		super();
		this.searchType = searchType;
		this.searchWord = searchWord;
	}
	
	public String getSearchType() {
		return searchType;
	}
	public String getSearchWord() {
		return searchWord;
	}

	@Override
	public String toString() {
		return "SearchCriteria [searchType=" + searchType + ", searchWord=" + searchWord + "]";
	}
	
	
}
