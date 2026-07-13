package kr.co.sscm.alpine.admin.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		AdminUserListResponse response = new AdminUserListResponse();
		response.setTotalCount(0);
		response.setList(new ArrayList<AdminUserResponse>());
		return response;
	}

	/** TODO: 패스워드 초기화 DAO/SQL 연결 예정. */
	public Boolean resetPassword(AdminPasswordResetRequest request) {
		return Boolean.FALSE;
	}
}
