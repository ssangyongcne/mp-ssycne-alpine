package kr.co.sscm.common.base;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @FileName BaseDao.java
 * @comment 서비스용 기본 dao
 * @author AJH
 */
public class BaseDao {

	protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired(required=true)
    @Qualifier("sqlSession2")
	protected SqlSession sqlSession;
    
    @Autowired(required=true)
    @Qualifier("sqlSession1")
	protected SqlSession gwSqlSession;

}
