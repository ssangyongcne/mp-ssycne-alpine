package kr.co.sscm.alpine.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.sscm.alpine.admin.dao.AdminDao;
import kr.co.sscm.alpine.admin.dto.AdminPasswordResetRequest;
import kr.co.sscm.alpine.admin.dto.AdminUserListResponse;
import kr.co.sscm.alpine.admin.dto.AdminUserResponse;
import kr.co.sscm.alpine.admin.dto.AdminUserSearchRequest;
import kr.co.sscm.common.base.BaseService;

@Service
public class AdminService extends BaseService {

	@Autowired
	private AdminDao adminDao;

	/** TODO: 사용자 목록 조회 DAO/SQL 연결 예정. */
	public AdminUserListResponse getUserList(AdminUserSearchRequest request) {
		Map<String, Object> param = createSearchParam(request);
		List<AdminUserResponse> list = adminDao.selectUserList(param);

		AdminUserListResponse response = new AdminUserListResponse();
		response.setList(list);
		return response;
	}

	/** TODO: 패스워드 초기화 DAO/SQL 연결 예정. */
	@Transactional(transactionManager = "transactionManager")
	public AdminPasswordResetRequest resetPassword(AdminPasswordResetRequest request) {
		Map<String, Object> param = createSearchParam(request);
		List<AdminPasswordResetRequest> list = adminDao.resetPassword(param);

		AdminPasswordResetRequest response = new AdminPasswordResetRequest();
		response.setList(list);
		return response;
	}
	
	/** Create the MyBatis search parameter map. */
	private Map<String, Object> createSearchParam(AdminUserSearchRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		if (request != null) {
		}
		return param;
	}
	
	/** Create the MyBatis search parameter map. */
	private Map<String, Object> createSearchParam(AdminPasswordResetRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		if (request != null) {
			  param.put("userNo", request.getUserNo());
		}
		return param;
	}
}
