package kr.co.sscm.alpine.member.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import kr.co.sscm.alpine.member.dto.AlpineMemberResponse;
import kr.co.sscm.common.base.BaseDao;

/** 관리자 사용자 관리 DAO. 실제 SQL은 기능 확정 후 연결한다. */
@Repository
public class AlpineMemberDao extends BaseDao {

	private String nameSpace = "ssyc.AlpineMemberDao";

	/** TODO: 사용자 목록 조회 쿼리 연결 예정. */
	public List<AlpineMemberResponse> selectUserList(Map<String, Object> param) {
		return gwSqlSession.selectList(nameSpace + ".selectMemberList", param);
		// reserved
	}

}
