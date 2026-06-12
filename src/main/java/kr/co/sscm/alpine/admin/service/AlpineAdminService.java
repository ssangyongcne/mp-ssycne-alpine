package kr.co.sscm.alpine.admin.service;

import org.springframework.stereotype.Service;

import kr.co.sscm.alpine.admin.dto.AlpinePasswordResetRequest;
import kr.co.sscm.common.base.BaseService;

@Service
public class AlpineAdminService extends BaseService {

	public Boolean resetPassword(AlpinePasswordResetRequest request) {
		// TODO: Implement admin password reset.
		return Boolean.FALSE;
	}
}
