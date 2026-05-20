package kr.co.sscm.gw.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import kr.co.sscm.common.base.BaseDao;

/**
 * @FileName GwDao.java
 * @comment
 * @author AJH
 */
@Repository
public class GwDao extends BaseDao{
	
	private String nameSpace = "ssyc.GwDao";

	/**
	 * 설비고장 신고 목록 조회
	 * @return
	 */
	public List<Map<String, Object>> getFaultReportsList() {
		
		return gwSqlSession.selectList(nameSpace+".selectFaultReportsList");
	}

	/**
	 * 설비고장 신고 상세 조회
	 * @param requestMap
	 * @return
	 */
	public Map<String, Object> getFaultReportsDetail(Map<String, Object> requestMap) {
		
		return gwSqlSession.selectOne(nameSpace+".selectFaultReportsDetail", requestMap);
	}

	/**
	 * 설비고장 신고 설비그룹별 상태 조회
	 * @param requestMap
	 * @return
	 */
	public List<Map<String, Object>> getEquipGrpList(Map<String, Object> requestMap) {
		
		return gwSqlSession.selectList(nameSpace+".selectEquipGrpList", requestMap);
	}

	/**
	 * 설비고장 신고 수정
	 * @param requestMap
	 * @return
	 */
	public int saveFaultReports(Map<String, Object> requestMap) {
		
		return gwSqlSession.update(nameSpace+".updateFaultReports", requestMap);
	}

	/**
	 * 설비그룹별 상태 등록 or 수정
	 * @param requestMap
	 * @return
	 */
	public int saveEquipGrpList(Map<String, Object> requestMap) {
		
		return gwSqlSession.insert(nameSpace+".upsertEquipGrpList", requestMap);
	}

	/**
	 * 등록된 공장 코드인지 확인
	 * @param factCd
	 * @return
	 */
	public int checkFactCd(String factCd) {
		
		return gwSqlSession.selectOne(nameSpace+".selectFactCdCount", factCd);
	}


}
