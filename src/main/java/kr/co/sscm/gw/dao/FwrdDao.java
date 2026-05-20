package kr.co.sscm.gw.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import kr.co.sscm.common.base.BaseDao;
import kr.co.sscm.gw.dto.FwrdReqDto;

/**
 * @FileName FwrdDao.java
 * @comment
 * @author AJH
 */
@Repository
public class FwrdDao extends BaseDao{

	private String nameSpace = "ssyc.FwrdDao";

	public List<Map<String, Object>> getJMA127Process(FwrdReqDto fwrdReqDto) {

		return sqlSession.selectList(nameSpace+".getJMA127Process", fwrdReqDto);
	}

}
