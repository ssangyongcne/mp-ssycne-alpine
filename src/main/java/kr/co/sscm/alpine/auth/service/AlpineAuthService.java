package kr.co.sscm.alpine.auth.service;

import org.springframework.stereotype.Service;

import kr.co.sscm.alpine.auth.dto.AlpineLoginRequest;
import kr.co.sscm.alpine.auth.dto.AlpineLoginResponse;
import kr.co.sscm.common.base.BaseService;

@Service
public class AlpineAuthService extends BaseService {

	private static final String TEST_EMP_NO = "2232021";

	public AlpineLoginResponse login(AlpineLoginRequest request) {
		// TODO: Replace this temporary member lookup with DB-backed authentication.
		if (request == null || !TEST_EMP_NO.equals(request.getEmpNo())) {
			return null;
		}

		// TODO: Add password verification when the authentication policy is finalized.
		AlpineLoginResponse response = new AlpineLoginResponse();
		response.setEMPNO(TEST_EMP_NO);
		response.setUZR_NM("강민규");
		response.setDEPT_NM("IT혁신팀");
		response.setDUTY_NM("사원");
		response.setPHONE("010-1234-5678");
		response.setADMIN_YN("N");
		return response;
	}
}
