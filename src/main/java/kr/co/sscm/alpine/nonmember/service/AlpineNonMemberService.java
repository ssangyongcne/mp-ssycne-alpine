package kr.co.sscm.alpine.nonmember.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sscm.alpine.nonmember.dao.AlpineNonMemberDao;
import kr.co.sscm.alpine.nonmember.dto.AlpineNonMemberListResponse;
import kr.co.sscm.alpine.nonmember.dto.AlpineNonMemberRequest;
import kr.co.sscm.alpine.nonmember.dto.AlpineNonMemberResponse;
import kr.co.sscm.common.base.BaseService;

@Service
public class AlpineNonMemberService extends BaseService {
	
	@Autowired
	private AlpineNonMemberDao AlpineNonMemberDao;

	/** TODO: 사용자 목록 조회 DAO/SQL 연결 예정. */
	public AlpineNonMemberListResponse getUserList(AlpineNonMemberRequest request) {
		Map<String, Object> param = createSearchParam(request);
		List<AlpineNonMemberResponse> list = AlpineNonMemberDao.selectUserList(param);

		AlpineNonMemberListResponse response = new AlpineNonMemberListResponse();
		response.setList(list);
		return response;
	}
	

	
	/** Create the MyBatis search parameter map. */
	private Map<String, Object> createSearchParam(AlpineNonMemberRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		if (request != null) {
		}
		return param;
	}
}
