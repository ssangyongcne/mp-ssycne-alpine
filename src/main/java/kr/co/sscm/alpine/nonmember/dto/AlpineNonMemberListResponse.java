package kr.co.sscm.alpine.nonmember.dto;

import java.util.List;

/** 관리자 사용자 목록 응답 DTO. */
public class AlpineNonMemberListResponse {

	private Integer totalCount;
	private List<AlpineNonMemberResponse> list;

	public Integer getTotalCount() { return totalCount; }
	public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }
	public List<AlpineNonMemberResponse> getList() { return list; }
	public void setList(List<AlpineNonMemberResponse> list) { this.list = list; }
}

