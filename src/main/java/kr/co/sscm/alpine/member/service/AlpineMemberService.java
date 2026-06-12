package kr.co.sscm.alpine.member.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.sscm.alpine.member.dto.AlpineMemberResponse;
import kr.co.sscm.common.base.BaseService;

@Service
public class AlpineMemberService extends BaseService {

	public List<AlpineMemberResponse> getMemberList() {
		// TODO: Implement member directory lookup.
		return Collections.emptyList();
	}
}
