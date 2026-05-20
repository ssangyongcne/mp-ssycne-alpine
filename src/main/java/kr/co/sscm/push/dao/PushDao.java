package kr.co.sscm.push.dao;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class PushDao {

	protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired(required=true)
    @Qualifier("sqlSession1")
	protected SqlSession sqlSession;

    private String nameSpace = "ssyc.PushDao";

	/**
	 * PUSH 수신동의 여부 확인
	 * @param requestMap
	 * @return
	 */
	public int pushRegiInfo(Map<String, Object> requestMap) {

		return sqlSession.selectOne(nameSpace+".pushRegiInfo", requestMap);
	}
}
