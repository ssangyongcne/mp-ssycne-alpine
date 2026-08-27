package kr.co.sscm.alpine.event.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sscm.alpine.event.dao.AlpineEventDao;
import kr.co.sscm.alpine.event.dto.AlpineEventCountResponse;
import kr.co.sscm.alpine.event.dto.AlpineEventListResponse;
import kr.co.sscm.alpine.event.dto.AlpineEventSummaryResponse;
import kr.co.sscm.common.base.BaseService;

@Service
public class AlpineEventService extends BaseService {
	
	@Autowired
	private AlpineEventDao eventDao;

	/** TODO: 사용자 목록 조회 DAO/SQL 연결 예정. */
	public AlpineEventListResponse getEventList(Long eventNo) {
		List<AlpineEventCountResponse> countList = eventDao.selectEventCount(eventNo);
		List<AlpineEventSummaryResponse> list = eventDao.selectEventList(eventNo);
		AlpineEventListResponse response = new AlpineEventListResponse();
		
		int totalCount = 0;
		for (AlpineEventCountResponse count : countList) {
			if (count != null && count.getcnt() != null) {
				totalCount += count.getcnt().intValue();
			}
		}
		response.setTotalCount(Integer.valueOf(totalCount));
		response.setCountList(countList);
		response.setList(list);
	
		/*
		 * Map<String, Object> param = createSearchParam(request);
		 * List<AlpineEventResponse> list = eventDao.selectEventList(param);
		 * 
		 * AlpineEventListResponse response = new AlpineEventListResponse();
		 * response.setList(list);
		 */
		return response;
	}

}
