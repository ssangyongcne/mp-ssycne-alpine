package kr.co.sscm.alpine.event.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.sscm.alpine.event.dto.AlpineEventListResponse;
import kr.co.sscm.alpine.event.service.AlpineEventService;
import kr.co.sscm.common.base.BaseController;

@Controller
@RequestMapping("/api/alpine")
public class AlpineEventController extends BaseController {

	@Autowired
	private AlpineEventService eventService;

	@PostMapping(value = { "/event", "/event/getEventList" }, produces = "application/json; charset=utf8")
	public @ResponseBody Map<String, Object> getEventList(@RequestBody(required = false) Map<String, Object> requestMap, HttpServletRequest httpRequest){
		Map<String, Object> bodyMap = getBodyMap(requestMap);

		Object eventNoValue = bodyMap.get("eventNo");
		Long eventNo;
		try {
			eventNo = toLong(eventNoValue);
		} catch (NumberFormatException e) {
			return createMspResponse(requestMap, httpRequest, "400", "Invalid EventNo.", null);
		}
		if (eventNo == null) {
			return createMspResponse(requestMap, httpRequest, "200", "success", null);
		}

		AlpineEventListResponse response = eventService.getEventList(eventNo);
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

	private Long toLong(Object value) {
	    if (value instanceof Number) {
	        return Long.valueOf(((Number) value).longValue());
	    }

	    String stringValue = toString(value);
	    if (stringValue == null ||
	        stringValue.trim().length() == 0) {
	        return null;
	    }

	    return Long.valueOf(stringValue.trim());
	}
	
	private String toString(Object value) 
	{ 
		return value == null ? null :String.valueOf(value); 
	}
	 
}
