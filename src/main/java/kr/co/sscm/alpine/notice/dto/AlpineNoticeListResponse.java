package kr.co.sscm.alpine.notice.dto;

import java.util.List;

public class AlpineNoticeListResponse {

	private Integer totalCount;
	private List<AlpineNoticeSummaryResponse> list;

	public Integer getTotalCount() { return totalCount; }
	public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }
	public List<AlpineNoticeSummaryResponse> getList() { return list; }
	public void setList(List<AlpineNoticeSummaryResponse> list) { this.list = list; }
}
