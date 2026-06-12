package kr.co.sscm.alpine.board.dto;

public class AlpineBoardSearchRequest {

	private String year;
	private String keyword;

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
