package kr.co.sscm.alpine.review.dto;

import java.util.List;

public class AlpineReviewListResponse {

	private Integer totalCount;
	private List<AlpineReviewSummaryResponse> list;

	public Integer getTotalCount() { return totalCount; }
	public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }
	public List<AlpineReviewSummaryResponse> getList() { return list; }
	public void setList(List<AlpineReviewSummaryResponse> list) { this.list = list; }
}
