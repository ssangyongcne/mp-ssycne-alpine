package kr.co.sscm.alpine.auth.dao;

import org.springframework.stereotype.Repository;

import kr.co.sscm.alpine.auth.dto.AlpineUserDto;
import kr.co.sscm.common.base.BaseDao;

@Repository
public class AlpineAuthDao extends BaseDao {

	private String nameSpace = "ssyc.AlpineAuthDao";

	public AlpineUserDto selectLoginUser(String userNo) {
		return gwSqlSession.selectOne(nameSpace + ".selectLoginUser", userNo);
	}

	public int updateLoginDdtm(String userNo) {
		return gwSqlSession.update(nameSpace + ".updateLoginDdtm", userNo);
	}
}
