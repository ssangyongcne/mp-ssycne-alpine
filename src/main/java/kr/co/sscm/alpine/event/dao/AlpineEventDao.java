package kr.co.sscm.alpine.event.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import kr.co.sscm.alpine.event.dto.AlpineEventCountResponse;
import kr.co.sscm.alpine.event.dto.AlpineEventSummaryResponse;
import kr.co.sscm.common.base.BaseDao;

/** 관리자 사용자 관리 DAO. 실제 SQL은 기능 확정 후 연결한다. */
@Repository
public class AlpineEventDao extends BaseDao {

	private String nameSpace = "ssyc.AlpineEventDao";

	/** 산행참여자 인원수 count */
	public List<AlpineEventCountResponse> selectEventCount(Long eventNo) {
		return gwSqlSession.selectList(nameSpace + ".selectEventCount", eventNo);
	}
	
	/** TODO: 산행참여자 목록 조회 쿼리 연결 예정. */
	public List<AlpineEventSummaryResponse> selectEventList(Long eventNo) {
		return gwSqlSession.selectList(nameSpace + ".selectEventList", eventNo);
		// reserved
	}

}
