package kr.co.sscm.alpine.member.dto;

import java.util.List;

/** 관리자 사용자 목록 응답 DTO. */
public class AlpineMemberListResponse {

	private Integer totalCount;
	private List<AlpineMemberResponse> list;

	public Integer getTotalCount() { return totalCount; }
	public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }
	public List<AlpineMemberResponse> getList() { return list; }
	public void setList(List<AlpineMemberResponse> list) { this.list = list; }
}

