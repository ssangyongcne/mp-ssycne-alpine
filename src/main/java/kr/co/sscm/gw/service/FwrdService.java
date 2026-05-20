package kr.co.sscm.gw.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sscm.common.base.BaseService;
import kr.co.sscm.common.dto.ApiRequestDto;
import kr.co.sscm.common.util.CommonUtils;
import kr.co.sscm.gw.dao.FwrdDao;
import kr.co.sscm.gw.dto.FwrdReqDto;

/**
 * @FileName FwrdService.java
 * @comment DB 사용 service
 * @author AJH
 */
@Service
public class FwrdService extends BaseService{

	@Autowired
	FwrdDao fwrdDao;

	/**
	 * 시간대별내수 출하현황
	 * @param requestDto
	 * @return
	 */
	public List<Map<String, Object>> getJMA127Process(ApiRequestDto requestDto) {

		Map<String, Object> params = requestDto.reqBodyMap;

		FwrdReqDto fwrdReqDto = new FwrdReqDto();

		String fwrdDt = CommonUtils.isToString(params.get("fwrdDt"));		// 출하 일자
		String factCd = CommonUtils.isToString(params.get("factCd"));		// 공장 코드
		String empNo = CommonUtils.isToString(params.get("empNo"));			// 사원번호

		fwrdReqDto.setEmpNo(empNo);
		fwrdReqDto.setFactCd(factCd);
		fwrdReqDto.setFwrdDt(fwrdDt);

		List<Map<String, Object>> result = fwrdDao.getJMA127Process(fwrdReqDto);

		return result;
	}

}
