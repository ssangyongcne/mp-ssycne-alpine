package kr.co.sscm.alpine.admin.dao;

import org.springframework.stereotype.Repository;

import kr.co.sscm.common.base.BaseDao;

/** 관리자 사용자 관리 DAO. 실제 SQL은 기능 확정 후 연결한다. */
@Repository
public class AdminDao extends BaseDao {

	private String nameSpace = "ssyc.AdminDao";

	/** TODO: 사용자 목록 조회 쿼리 연결 예정. */
	public void selectUserList() {
		// reserved
	}

	/** TODO: 패스워드 초기화 쿼리 연결 예정. */
	public void resetPassword() {
		// reserved
	}
}
