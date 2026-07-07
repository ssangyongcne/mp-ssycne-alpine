package kr.co.sscm.alpine.auth.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import kr.co.sscm.alpine.auth.dao.AlpineAuthDao;
import kr.co.sscm.alpine.auth.dto.AlpineLoginRequest;
import kr.co.sscm.alpine.auth.dto.AlpineLoginResponse;
import kr.co.sscm.alpine.auth.dto.AlpinePasswordChangeRequest;
import kr.co.sscm.alpine.auth.dto.AlpineUserDto;
import kr.co.sscm.common.base.BaseService;

@Service
public class AlpineAuthService extends BaseService {

	public static final String CHANGE_SUCCESS = "SUCCESS";
	public static final String CHANGE_INVALID_CURRENT_PASSWORD = "INVALID_CURRENT_PASSWORD";
	public static final String CHANGE_INVALID_POLICY = "INVALID_POLICY";
	public static final String CHANGE_SERVER_ERROR = "SERVER_ERROR";

	@Autowired
	private AlpineAuthDao alpineAuthDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public AlpineLoginResponse login(AlpineLoginRequest request) {
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
		response.setFirstLogin(StringUtils.hasText(user.getFirstLogin()) ? user.getFirstLogin() : "N");
		return response;
	}

	@Transactional
	public String changePassword(AlpinePasswordChangeRequest request) {
		if (request == null || !StringUtils.hasText(request.getNewPw()) || request.getNewPw().length() < 8) {
			return CHANGE_INVALID_POLICY;
		}

		if (!StringUtils.hasText(request.getUserNo()) || !StringUtils.hasText(request.getCurrentPw())) {
			return CHANGE_INVALID_CURRENT_PASSWORD;
		}

		AlpineUserDto user = alpineAuthDao.selectLoginUser(request.getUserNo());
		if (user == null || !StringUtils.hasText(user.getPw())
				|| !passwordEncoder.matches(request.getCurrentPw(), user.getPw())) {
			return CHANGE_INVALID_CURRENT_PASSWORD;
		}

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userNo", user.getUserNo());
		param.put("pw", passwordEncoder.encode(request.getNewPw()));

		int updateCount = alpineAuthDao.updatePassword(param);
		return updateCount > 0 ? CHANGE_SUCCESS : CHANGE_SERVER_ERROR;
	}

}