package kr.co.sscm.alpine.member.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sscm.alpine.member.dao.AlpineMemberDao;
import kr.co.sscm.alpine.member.dto.AlpineMemberListResponse;
import kr.co.sscm.alpine.member.dto.AlpineMemberRequest;
import kr.co.sscm.alpine.member.dto.AlpineMemberResponse;
import kr.co.sscm.common.base.BaseService;

@Service
public class AlpineMemberService extends BaseService {
	
	@Autowired
	private AlpineMemberDao AlpineMemberDao;

	/** TODO: 사용자 목록 조회 DAO/SQL 연결 예정. */
	public AlpineMemberListResponse getUserList(AlpineMemberRequest request) {
		Map<String, Object> param = createSearchParam(request);
		List<AlpineMemberResponse> list = AlpineMemberDao.selectUserList(param);

		AlpineMemberListResponse response = new AlpineMemberListResponse();
		response.setList(list);
		return response;
	}
	

	
	/** Create the MyBatis search parameter map. */
	private Map<String, Object> createSearchParam(AlpineMemberRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		if (request != null) {
		}
		return param;
	}
}
