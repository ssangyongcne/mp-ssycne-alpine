package kr.co.sscm.alpine.admin.dto;

import java.util.List;

/** 관리자 사용자 목록 응답 DTO. */
public class AdminUserListResponse {

	private Integer totalCount;
	private List<AdminUserResponse> list;

	public Integer getTotalCount() { return totalCount; }
	public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }
	public List<AdminUserResponse> getList() { return list; }
	public void setList(List<AdminUserResponse> list) { this.list = list; }
}

