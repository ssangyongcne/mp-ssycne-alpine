package kr.co.sscm.push.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.sscm.common.base.BaseController;
import kr.co.sscm.common.dto.ApiRequestDto;
import kr.co.sscm.common.dto.ApiResponseDto;
import kr.co.sscm.common.dto.MsgSendDto;
import kr.co.sscm.common.resolver.ApiRequest;
import kr.co.sscm.push.dto.PushResponseDto;
import kr.co.sscm.push.service.PushService;

@Controller
@RequestMapping("/push")
public class PushController extends BaseController{

	@Autowired
	PushService pushService;

	/**
	 * push 발송
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value="/sendPush", produces = "application/json; charset=utf8")
	public @ResponseBody PushResponseDto sendPush(
			MsgSendDto dto,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("############ {sendPush dto} : " + ToStringBuilder.reflectionToString(dto, ToStringStyle.JSON_STYLE));

		PushResponseDto resDto = pushService.pushSend(dto);

		return resDto;
	}

	/**
	 * PUSH 수신동의 여부 확인
	 * @param requestDto
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value="/pushRegiInfo", produces = "application/json; charset=utf8")
	public @ResponseBody ApiResponseDto pushRegiInfo(
			@ApiRequest ApiRequestDto requestDto,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {


		ApiResponseDto responseDto = new ApiResponseDto(requestDto);

		Map<String, Object> requestMap = requestDto.reqBodyMap;

		int cnt = pushService.pushRegiInfo(requestMap);

		logger.info("############ {cnt} : " + cnt );

		responseDto.putBody("count", cnt);

		return responseDto;

	}
}
