package kr.co.sscm.alpine.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import kr.co.sscm.alpine.auth.dao.AlpineAuthDao;
import kr.co.sscm.alpine.auth.dto.AlpineLoginRequest;
import kr.co.sscm.alpine.auth.dto.AlpineLoginResponse;
import kr.co.sscm.alpine.auth.dto.AlpineUserDto;
import kr.co.sscm.common.base.BaseService;

@Service
public class AlpineAuthService extends BaseService {

	@Autowired
	private AlpineAuthDao alpineAuthDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public AlpineLoginResponse login(AlpineLoginRequest request) {
		// TEMP: Remove after frontend login payload verification. Do not keep password logs in production.
		System.out.println("[ALPINE_LOGIN_REQUEST] userNo=" + (request == null ? null : request.getUserNo())
				+ ", pw=" + (request == null ? null : request.getPw()));

		if (request == null || !StringUtils.hasText(request.getUserNo()) || !StringUtils.hasText(request.getPw())) {
			return null;
		}

		AlpineUserDto user = alpineAuthDao.selectLoginUser(request.getUserNo());
		if (user == null || !StringUtils.hasText(user.getPw())) {
			return null;
		}

		if (!passwordEncoder.matches(request.getPw(), user.getPw())) {
			return null;
		}

		alpineAuthDao.updateLoginDdtm(user.getUserNo());

		AlpineLoginResponse response = new AlpineLoginResponse();
		response.setUserNo(user.getUserNo());
		response.setUserNm(user.getUserNm());
		response.setDeptNm(user.getDeptNm());
		response.setDutyNm(user.getDutyNm());
		response.setPhone(user.getPhone());
		response.setAuth(user.getAuth());
		return response;
	}

}





