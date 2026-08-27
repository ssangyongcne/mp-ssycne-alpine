package kr.co.sscm.alpine.event.dto;

import java.util.List;

/** 관리자 사용자 목록 응답 DTO. */
public class AlpineEventListResponse {

	private Integer totalCount;
	private List<AlpineEventCountResponse> countList;
	private List<AlpineEventSummaryResponse> list;

	public Integer getTotalCount() { return totalCount; }
	public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }
	public List<AlpineEventCountResponse> getCountList() { return countList; }
	public void setCountList(List<AlpineEventCountResponse> countList) { this.countList = countList; }
	public List<AlpineEventSummaryResponse> getList() { return list; }
	public void setList(List<AlpineEventSummaryResponse> list) { this.list = list; }
}

