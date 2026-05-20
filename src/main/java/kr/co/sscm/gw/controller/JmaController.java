package kr.co.sscm.gw.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.sscm.common.base.BaseController;
import kr.co.sscm.common.dto.ApiRequestDto;
import kr.co.sscm.common.dto.ApiResponseDto;
import kr.co.sscm.common.exception.ApiException;
import kr.co.sscm.common.resolver.ApiRequest;
import kr.co.sscm.common.util.CommonUtils;
import kr.co.sscm.common.util.SessionUtils;
import kr.co.sscm.gw.service.FwrdService;
import kr.co.sscm.gw.service.GwService;
import kr.co.sscm.gw.service.JmaService;

/**
 * @FileName JmaController.java
 * @comment
 * @author AJH
 */
@Controller
@RequestMapping("/gw")
public class JmaController extends BaseController{

	@Autowired
	JmaService jmaService;

	@Autowired
	FwrdService fwrdService;
	
	@Autowired
	GwService gwService;

	@Value("${db.use}")
    private boolean dbUse;				// DB 사용 여부

	/**
	 * EAI 호출  수정
	 * @param action
	 * @param requestDto
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/{action}")
	public @ResponseBody ApiResponseDto action(
			@PathVariable String action,
			@ApiRequest ApiRequestDto requestDto,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception  {

		ApiResponseDto responseDto = new ApiResponseDto(requestDto);

		// EAI interface 사용
		if( !dbUse ) {

			String uzrIp = CommonUtils.getClientIP(request);
			String comptAddr = CommonUtils.getUserOS(request);
			requestDto.reqBodyMap.put("uzrIp", uzrIp);
			requestDto.reqBodyMap.put("comptAddr", comptAddr);

			if("JMA101Process".equals(action)) {
				responseDto = jmaService.getJMA101Process(requestDto);
			}else if("JMA104Process".equals(action)) {
				responseDto = jmaService.getJMA104Processs(requestDto);
			}
			else if("JMA105Process".equals(action) ) {
				responseDto = jmaService.getJMA105Process(requestDto);
			}
			else if("JMA108Process".equals(action) ) {
				responseDto = jmaService.getJMA108Process(requestDto);
			}
			else if("JMA111Process".equals(action)) {
				responseDto = jmaService.getJMA111Process(requestDto);
			}
			else if("JMA121Process".equals(action)) {
				responseDto = jmaService.getJMA121Process(requestDto);
			}
			else if("JMA122Process".equals(action)) {
				responseDto = jmaService.getJMA122Process(requestDto);
			}
			else if("JMA123Process".equals(action)) {
				responseDto = jmaService.getJMA123Process(requestDto);
			}
			else if("JMA124Process".equals(action)) {
				responseDto = jmaService.getJMA124Process(requestDto);
			}
			else if("JMA125Process".equals(action)) {
				responseDto = jmaService.getJMA125Process(requestDto);
			}
			else if("JMA126Process".equals(action)) {
				responseDto = jmaService.getJMA126Process(requestDto);
			}
			else if("JMA127Process".equals(action)) {
				//responseDto = jmaService.getJMA127Process(requestDto);
				List<Map<String, Object>> result = fwrdService.getJMA127Process(requestDto);
				responseDto.putBody("result", result);
			}
			else if("JMA128Process".equals(action)) {
				responseDto = jmaService.getJMA128Process(requestDto);
			}
			else if("JMA129Process".equals(action)) {
				responseDto = jmaService.getJMA129Process(requestDto);
			}
			else if("JMA130Process".equals(action)) {
				responseDto = jmaService.getJMA130Process(requestDto);
			}
			else if("JMA131Process".equals(action)) {
				responseDto = jmaService.getJMA131Process(requestDto);
			}
			else if("JMA132Process".equals(action)) {
				responseDto = jmaService.getJMA132Process(requestDto);
			}
			else if("JMA133Process".equals(action)) {
				responseDto = jmaService.getJMA133Process(requestDto);
			}
			else if("JMA134Process".equals(action)) {
				responseDto = jmaService.getJMA134Process(requestDto);
			}
			else if("JMA135Process".equals(action)) {
				responseDto = jmaService.getJMA135Process(requestDto);
			}
			else if("JMA136Process".equals(action)) {
				responseDto = jmaService.getJMA136Process(requestDto);
			}
			else if("JMA137Process".equals(action)) {
				responseDto = jmaService.getJMA137Process(requestDto);
			}
			else if("JMA138Process".equals(action)) {
				responseDto = jmaService.getJMA138Process(requestDto);
			}
			else if("JMA139Process".equals(action)) {
				responseDto = jmaService.getJMA139Process(requestDto);
			}
			else if("JMA140Process".equals(action)) {
				responseDto = jmaService.getJMA140Process(requestDto);
			}
			else if("JMA141Process".equals(action)) {
				responseDto = jmaService.getJMA141Process(requestDto);
			}
			else if("JMA142Process".equals(action)) {
				responseDto = jmaService.getJMA142Process(requestDto);
			}
			else if("JMA143Process".equals(action)) {
				responseDto = jmaService.getJMA143Process(requestDto);
			}
			else if("JMA144Process".equals(action)) {
				responseDto = jmaService.getJMA144Process(requestDto);
			}
			else if("JMA145Process".equals(action)) {
				responseDto = jmaService.getJMA145Process(requestDto);
			}
			else if("JMA146Process".equals(action)) {
				responseDto = jmaService.getJMA146Process(requestDto);
			}
			else {
				throw new ApiException("존재하지 않는 I/F입니다.");
			}

		// DB 사용
		}else {
			if("JMA127Process".equals(action)) {
				List<Map<String, Object>> result = fwrdService.getJMA127Process(requestDto);
				responseDto.putBody("result", result);
			}else {
				throw new ApiException("존재하지 않는 I/F입니다.");
			}
		}

		logger.info("############# responseDto #################");
		logger.info("############ responseDto : " + ToStringBuilder.reflectionToString(responseDto, ToStringStyle.JSON_STYLE));

		return responseDto;
	}

	/**
	 * 로그아웃
	 * @param requestDto
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/logout") 
	public @ResponseBody ApiResponseDto test(
			@ApiRequest ApiRequestDto requestDto,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception  {

		ApiResponseDto responseDto = new ApiResponseDto(requestDto);

		HttpSession session = request.getSession(false);
		if (session != null) {

			logger.info("########################################");
			logger.info("##### logout empNo : " + SessionUtils.getEmpNo());
			logger.info("########################################");

			session.invalidate();
		}

		return responseDto;
	}
	
	/**
	 * 설비고장 신고 목록 조회
	 * @param requestDto
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getFaultReportsList") 
	public @ResponseBody ApiResponseDto faultReportsList(
			@ApiRequest ApiRequestDto requestDto,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception  {

		ApiResponseDto responseDto = new ApiResponseDto(requestDto);

		List<Map<String, Object>> result = gwService.getFaultReportsList(requestDto);
		responseDto.putBody("output", result);
		
		return responseDto;
	}
	
	/**
	 * 설비고장 신고 상세 조회
	 * @param requestDto
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getFaultReportsDetail") 
	public @ResponseBody ApiResponseDto faultReportsDetail(
			@ApiRequest ApiRequestDto requestDto,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception  {

		ApiResponseDto responseDto = new ApiResponseDto(requestDto);

		responseDto = gwService.getFaultReportsDetail(requestDto);
		
		return responseDto;
	}
	
	/**
	 * 설비고장 신고 수정
	 * @param requestDto
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value= "/saveFaultReports", produces = "application/json; charset=utf8")
	public @ResponseBody ApiResponseDto saveFaultReports(
			@ApiRequest ApiRequestDto requestDto,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception  {

		ApiResponseDto responseDto = new ApiResponseDto(requestDto);

		responseDto = gwService.saveFaultReports(requestDto);
		
		return responseDto;
	}
}
