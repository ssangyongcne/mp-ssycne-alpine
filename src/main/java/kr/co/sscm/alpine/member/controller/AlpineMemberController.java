package kr.co.sscm.alpine.member.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.sscm.alpine.member.dto.AlpineMemberListResponse;
import kr.co.sscm.alpine.member.dto.AlpineMemberRequest;
import kr.co.sscm.alpine.member.service.AlpineMemberService;
import kr.co.sscm.common.base.BaseController;

@Controller
@RequestMapping("/api/alpine")
public class AlpineMemberController extends BaseController {

	@Autowired
	private AlpineMemberService alpineMemberService;

	@PostMapping(value = "/member/getUserList", produces = "application/json; charset=utf8")
	public @ResponseBody Map<String, Object> getUserList(@RequestBody(required = false) Map<String, Object> requestMap, HttpServletRequest httpRequest){
		Map<String, Object> bodyMap = getBodyMap(requestMap);
		
		logger.info("조회 시작");

		AlpineMemberRequest request = new AlpineMemberRequest();
		AlpineMemberListResponse response = alpineMemberService.getUserList(request);
		return createMspResponse(requestMap, httpRequest, "200", "success", response);
	}
	
	private Map<String, Object> createMspResponse(Map<String, Object> requestMap, HttpServletRequest httpRequest, String resultCode, String resultMsg, Object result) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> headMap = new HashMap<String, Object>();
		Map<String, Object> bodyMap = new HashMap<String, Object>();

		headMap.put("result_code", resultCode);
		headMap.put("result_msg", resultMsg);
		headMap.put("screen_id", getScreenId(requestMap, httpRequest));

		bodyMap.put("resultCode", resultCode);
		bodyMap.put("resultMsg", resultMsg);
		bodyMap.put("result", result);

		responseMap.put("head", headMap);
		responseMap.put("body", bodyMap);
		return responseMap;
	}
	
	@SuppressWarnings("unchecked")
	private String getScreenId(Map<String, Object> requestMap, HttpServletRequest httpRequest) {
		if (requestMap != null) {
			Object head = requestMap.get("head");
			if (head instanceof Map) {
				Object screenId = ((Map<String, Object>) head).get("screen_id");
				if (screenId != null) {
					return String.valueOf(screenId);
				}
			}
		}

		String screenId = httpRequest.getHeader("screen_id");
		return screenId == null ? "" : screenId;
	}


	@SuppressWarnings("unchecked")
	private Map<String, Object> getBodyMap(Map<String, Object> requestMap) {
		if (requestMap == null) {
			return new HashMap<String, Object>();
		}
		Object body = requestMap.get("body");
		if (body instanceof Map) {
			return (Map<String, Object>) body;
		}
		return requestMap;
	}

	private String toString(Object value) {
		return value == null ? null : String.valueOf(value);
	}
}
