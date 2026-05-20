package kr.co.sscm.gw.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.sscm.common.Constants;
import kr.co.sscm.common.base.XmlBaseService;
import kr.co.sscm.common.dto.ApiRequestDto;
import kr.co.sscm.common.dto.ApiResponseDto;
import kr.co.sscm.common.dto.LoginInfoDto;
import kr.co.sscm.common.exception.ApiException;
import kr.co.sscm.common.exception.ValidationException;
import kr.co.sscm.common.util.AES256Util;
import kr.co.sscm.common.util.CommonUtils;
import kr.co.sscm.common.util.SessionUtils;
import kr.co.sscm.common.util.SoapUtil;

/**
 * @FileName JmaService.java
 * @comment EAI interface 사용 service
 * @author AJH
 */
@Service
public class JmaService extends XmlBaseService{
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("${eai.endpoint.mobileLoginProcess}")
    private String mobileLoginProcess;				// 로그인

	@Value("${eai.endpoint.bgtCMPRProcess}")
    private String bgtCMPRProcess;					// 목표대비실적(월간)

	@Value("${eai.endpoint.bgtCMPRYearProcess}")
    private String bgtCMPRYearProcess;				// 목표대비실적(년간)

	@Value("${eai.endpoint.fldSiteQTYProcess}")
    private String fldSiteQTYProcess;				// 거래처출하실적

	@Value("${eai.endpoint.fldSiteQTYDTLProcess}")
    private String fldSiteQTYDTLProcess;			// 출하내역상세

	@Value("${eai.endpoint.fwrdREQProcess}")
    private String fwrdREQProcess;					// 출하의뢰내역조회

	@Value("${eai.endpoint.fwrdREQDTLProcess}")
    private String fwrdREQDTLProcess;				// 출하의뢰 상세내역 조회

	@Value("${eai.endpoint.fwrdREQEXCUTEProcess}")
    private String fwrdREQEXCUTEProcess;			// 출하의뢰내역 입력 수정 삭제 처리

	@Value("${eai.endpoint.fwrdSTOCKALLProcess}")
    private String fwrdSTOCKALLProcess;				// 생산출하재고현황(공장코드 미입력 시)

	@Value("${eai.endpoint.fwrdSTOCK1Process}")
    private String fwrdSTOCK1Process;				// 생산출하재고현황(제주지사, 부산공장)

	@Value("${eai.endpoint.fwrdSTOCK2Process}")
    private String fwrdSTOCK2Process;				// 생산출하재고현황(제주지사, 부산공장 외 공장코드)

	@Value("${eai.endpoint.fwrdTIMEProcess}")
    private String fwrdTIMEProcess;					// 시간대별내수 출하현황
	
	@Value("${eai.endpoint.rcptListProcess}")
    private String rcptListProcess;					// 민수입금입력 조회
	
	@Value("${eai.endpoint.rcptListDtlProcess}")
    private String rcptListDtlProcess;			    // 민수입금입력 상세조회
	
	@Value("${eai.endpoint.rcptListEXCUTElProcess}")
    private String rcptListEXCUTElProcess;			    // 민수입금입력 저장

	@Value("${eai.endpoint.noteNoListProcess}")
    private String noteNoListProcess;			    // 어음번호 조회
	
	@Value("${eai.endpoint.rcptAcctListProcess}")
    private String rcptAcctListProcess;			    // 은행별 입금내역 조회	

	@Value("${eai.endpoint.vrtlAcctNoListProcess}")
    private String vrtlAcctNoListProcess;			    // 가상계좌 입금내역 조회
	
	@Value("${eai.endpoint.asgnListProcess}")
    private String asgnListProcess;			        // 배정등록 조회
	
	@Value("${eai.endpoint.asgnSaveProcess}")
    private String asgnSaveProcess;			        // 배정등록 저장

	@Value("${eai.endpoint.hmatSTOCKProcess}")
    private String hmatSTOCKProcess;				// H원료현황 조회
	
	@Value("${eai.endpoint.patrolListProcess}")
    private String patrolListProcess;				// 안전순찰일지 조회

	@Value("${eai.endpoint.patrolDtlProcess}")
	private String patrolDtlProcess;				// 안전순찰일지 상세조회

	@Value("${eai.endpoint.patrolInsertProcess}")
	private String patrolInsertProcess;				// 안전순찰일지 입력

	@Value("${eai.endpoint.patrolUpdateProcess}")
	private String patrolUpdateProcess;				// 안전순찰일지 수정
	
	@Value("${eai.endpoint.patrolDeleteProcess}")
	private String patrolDeleteProcess;				// 안전순찰일지 삭제

	@Value("${eai.endpoint.authProcess}")
    private String authProcess;						// 화면별 권한 조회

	@Value("${eai.endpoint.commonCode}")
    private String commonCode;						// 공통코드 조회(여신그룹, 거래처, 사이트, 현장, 영업단위, 수송사, 영업사원, 공장, 부서, 제품)

	@Value("${eai.endpoint.voycntListProcess}") 
	private String voycntListProcess;        		// 부원료항차관리 조회 
	
	@Value("${eai.endpoint.voycntUpdateProcess}") 
	private String voycntUpdateProcess;        		// 부원료항차관리 수정 

	@Value("${eai.endpoint.exprsMatListProcess}") 
	private String exprsMatListProcess;        		// 속보현황 조회 
	
	@Value("${eai.endpoint.historyInsertProcess}")
	private String historyInsertProcess;

	@Value("${login.length}")
    private int loginLength;						//

	public boolean isMatched(String plainPassword, String encodedPassword) {
	   return this.passwordEncoder.matches(plainPassword, encodedPassword);
	}

	public String encodePassword(String plainPassword) throws Exception {
	   return this.passwordEncoder.encode(plainPassword);
	}
	
	public String factConvert(String factCd) throws Exception {
		
		String cFactCd = "";

		if(factCd == "0002") {
			cFactCd = "2100";
		}else if(factCd == "0003") {
			cFactCd = "2200";
		}else if(factCd == "0004") {
			cFactCd = "2300";
		}else if(factCd == "2100") {
			cFactCd = "0002";
		}else if(factCd == "2200") {
			cFactCd = "0003";
		}else if(factCd == "2300") {
			cFactCd = "0004";
		}else if(factCd == "0001") {
			cFactCd = "1000";
		}else if(factCd == "1000") {
			cFactCd = "0001";
		}
		
		return factCd;
		
	}
	
	
	/**
	 * 로그인
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA101Process(ApiRequestDto requestDto) throws Exception {

	      ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
	      Map<String, Object> result = new HashMap();
	      String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));
	      String uzrPw = CommonUtils.isToString(requestDto.reqBodyMap.get("uzrPw"));
	      String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));
	      String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));
	      String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));
	      String uzrIp = CommonUtils.isToString(requestDto.reqBodyMap.get("uzrIp"));
	      if (!CommonUtils.isExist(empNo)) {
	         throw new ValidationException("사원번호");
	      } else if (!CommonUtils.isExist(uzrPw)) {
	         throw new ValidationException("사용자 암호");
	      } else {
	         uzrPw = AES256Util.decrypt(uzrPw);
	         Element eleAction = new Element("clientRequestwithReturn");
	         eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
	         eleAction.addContent(SoapUtil.getElementNew("UZR_PW", this.encodePassword(uzrPw)));
	         eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
	         eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
	         eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));
	         eleAction.addContent(SoapUtil.getElementNew("UZR_IP", uzrIp));
	         Map<String, Object> resString = SoapUtil.sendSoapServerMapNew("dummyUrl", this.mobileLoginProcess, SoapUtil.getSoapXmlStringNew(eleAction, this.mobileLoginProcess));
	         String apiResult = super.xmlMapToStringNew(resString);
	         apiResult = apiResult.substring(1);
	         String[] row = apiResult.split("\\|\\|", -1);
	         boolean pwdChk = false;
	         if (this.isMatched(uzrPw, row[5])) {
	            pwdChk = true;
	         }

	         this.logger.debug("login length : " + row.length);
	         if (row.length == this.loginLength && !row[0].equals("XX") && pwdChk) {
        	
        	
        	
        	result.put("EMPNO"			, row[0]);	// 사원번호
			result.put("UZR_NM"			, row[1]);	// 사원이름
			result.put("COMB_ORG_CD"	, row[2]);	// 조직코드
			result.put("COMB_NM"		, row[3]);	// 조직이름
			result.put("PW_CHK"			, row[4]);	// 패스워드확인
			result.put("SAL_UT_CD"		, row[5]);	// 소속영업단위
			result.put("AUTH_ID"		, row[6]);	// 전략회의사용자
			result.put("AUTH_ID2"		, row[7]);	// 집행위원회의 사용자
			result.put("AUTH_ID3"		, row[8]);	// 경영회의사용자
			result.put("AUTH_ID4"		, row[9]);	// 실적점검사용자
			result.put("MEET_CLOSE1"	, row[10]);	// 전략회의 마감여부
			result.put("MEET_CLOSE2"	, row[11]);	// 실적점 검 마감여부
			result.put("RETRV_SAL_UT"	, row[12]);	// 검색가능영업단위
			result.put("SAL_PART_CD"	, row[13]);	// 영업부문
			result.put("MAT_RCVPAY_ORG_CD"	, row[14]);	
			result.put("MAT_RCVPAY_ORG_NM"	, row[15]);	
			result.put("UPP_BGT_ORG_CD"	, row[16]);	// 소속 사업장 코드

			
			
			
			
			LoginInfoDto loginInfo = new LoginInfoDto();
            loginInfo.setEmpNo(row[1]);
            loginInfo.setUzrNm(row[2]);
            loginInfo.setCombOrgCd(row[3]);
            loginInfo.setCombNm(row[4]);
            loginInfo.setPwChk(row[5]);
            loginInfo.setSalUtCd(row[6]);
            loginInfo.setMeetClose1(row[7]);
            loginInfo.setMeetClose2(row[8]);
            loginInfo.setRetrvSalUt(row[9]);
            loginInfo.setSalPartCd(row[10]);
            loginInfo.setIp(uzrIp);
            SessionUtils.setLoginInfo(loginInfo);
            this.getJMA029Processs(empNo, scrId, wrkTyp, comptAddr);
            apiResponse.putBody("result", result);
            return apiResponse;
         } else if (row.length > 1) {
            if (row[1].equals("XX") && row[2].equals("XX")) {
               throw new ApiException("사원정보가 존재하지 않습니다.");
            } else if (!row[1].equals("XX") && !pwdChk) {
               throw new ApiException("비밀번호가 틀립니다");
            } else {
               throw new ApiException("사원번호와 비밀번호를 확인하십시오." + row.length);
            }
         } else {
            throw new ApiException("로그인 오류입니다.");
         }
      }
	}
	
	


	/**
	 * 목표대비실적(월간)
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA104Processs(ApiRequestDto requestDto) throws Exception {

		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String stdDt = CommonUtils.isToString(requestDto.reqBodyMap.get("stdDt"));			// 기준 일자
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));			// 사원번호
		String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));			// 화면ID
		String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));		// 작업유형
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));	// MAC주소

		// validation check
		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}

		Element eleAction = new Element(Constants.CLIENT_REQUEST);
		eleAction.addContent(SoapUtil.getElementNew("STD_DT", stdDt));
		eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));

		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMapNew("dummyUrl",bgtCMPRProcess, SoapUtil.getSoapXmlStringNew( eleAction, bgtCMPRProcess ));

		// EAI 통신 결과 String 변환
		String apiResult = super.xmlMapToStringNew(resString);

		apiResult = apiResult.substring(1);
		String[] rows = apiResult.substring(apiResult.indexOf("||") + 2).split("\n");

		for (int i = 0; i < rows.length; ++i) {
			if (!CommonUtils.isExist(rows[i])) {
					logger.debug("Skip empty row : " + i);
			}else{

				String[] row = rows[i].split("\\|\\|", -1);
				Map<String, Object> data = new HashMap<String, Object>();

    			int co = 0;
    			data.put("SAL_UT_CD"		, row[co++]);	// 영업 단위 코드
				data.put("SAL_UT_NM"		, row[co++]);	// 영업 단위 명
				data.put("DD_FWRD_QTY"		, row[co++]);	// 일 출하 수량
				data.put("YY_BGT_QTY"		, row[co++]);	// 시행계획
				data.put("YY_FWRD_QTY"		, row[co++]);	// 월실적
				data.put("YY_BGT_CMPR_RATE"	, row[co++]);	// 추세(%)
    			list.add(data);
			}
		}

		apiResponse.putBody("result", list);
		return apiResponse;
	}

	/**
	 * 목표대비실적(년간)
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA105Process(ApiRequestDto requestDto) throws Exception {

		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String stdDt = CommonUtils.isToString(requestDto.reqBodyMap.get("stdDt"));			// 기준 일자
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));			// 사원번호
		String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));			// 화면ID
		String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));		// 작업유형
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));	// MAC주소

		// validation check
		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}

		Element eleAction = new Element(Constants.CLIENT_REQUEST);
		eleAction.addContent(SoapUtil.getElementNew("STD_DT", stdDt));
		eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));

		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMapNew("dummyUrl",bgtCMPRYearProcess, SoapUtil.getSoapXmlStringNew( eleAction,bgtCMPRYearProcess ));

		// EAI 통신 결과 String 변환
		String apiResult = super.xmlMapToStringNew(resString);

		apiResult = apiResult.substring(1);
		String[] rows = apiResult.substring(apiResult.indexOf("||") + 2).split("\n");

		for (int i = 0; i < rows.length; ++i) {
			if (!CommonUtils.isExist(rows[i])) {
					logger.debug("Skip empty row : " + i);
			}else{

				String[] row = rows[i].split("\\|\\|", -1);
				Map<String, Object> data = new HashMap<String, Object>();

    			int co = 0;
				data.put("SAL_UT_CD"		, row[co++]);	// 영업 단위 코드
				data.put("SAL_UT_NM"		, row[co++]);	// 영업 단위 명
				data.put("DD_FWRD_QTY"		, row[co++]);	// 일 출하 수량
				data.put("YY_BGT_QTY"		, row[co++]);	// 시행계획
				data.put("YY_FWRD_QTY"		, row[co++]);	// 년실적
				data.put("YY_BGT_CMPR_RATE"	, row[co++]);	// 추세(%)
    			list.add(data);
			}
		}

		apiResponse.putBody("result", list);
		return apiResponse;
	}

	/**
	 * 거래처출하실적
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA108Process(ApiRequestDto requestDto) throws Exception {

		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String stdDt = CommonUtils.isToString(requestDto.reqBodyMap.get("stdDt"));			// 기준 일자
		String salUtCd = CommonUtils.isToString(requestDto.reqBodyMap.get("salUtCd"));		// 영업 단위 코드
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));			// 사원번호
		String kpFg = CommonUtils.isToString(requestDto.reqBodyMap.get("kpFg"));			// KP 구분
		String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));			// 화면ID
		String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));		// 작업유형
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));	// MAC주소

		// validation check
		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}

		Element eleAction = new Element(Constants.CLIENT_REQUEST, "http://www.openuri.org/");
		eleAction.addContent(SoapUtil.getElementNew("STD_DT", stdDt));
		eleAction.addContent(SoapUtil.getElementNew("SAL_UT_CD", salUtCd));
		eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		eleAction.addContent(SoapUtil.getElementNew("KP_FG", kpFg));
		eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));

		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMapNew("dummyUrl",fldSiteQTYProcess, SoapUtil.getSoapXmlStringNew( eleAction,fldSiteQTYProcess));

		// EAI 통신 결과 String 변환
		String apiResult = super.xmlMapToStringNew(resString);

		apiResult = apiResult.substring(1);
		String[] rows = apiResult.substring(apiResult.indexOf("||") + 2).split("\n");

		for (int i = 0; i < rows.length; ++i) {
			if (!CommonUtils.isExist(rows[i])) {
					logger.debug("Skip empty row : " + i);
			}else{

				String[] row = rows[i].split("\\|\\|", -1);
				Map<String, Object> data = new HashMap<String, Object>();

    			int co = 0;
    			data.put("SAL_UT_CD"	, row[co++]);	// 영업 단위 코드
				data.put("SAL_UT_NM"	, row[co++]);	// 영업 단위 명
				data.put("PROD_CD"		, row[co++]);	// 판매 형태 코드
				data.put("PROD_NM"		, row[co++]);	// 판매 형태 명
				data.put("FLD_SITE_CD"	, row[co++]);	// 현장 사이트 코드
				data.put("FLD_NM"		, row[co++]);	// 현장 명
				data.put("DD_QTY"		, row[co++]);	// 일 출하량
				data.put("MM_QTY"		, row[co++]);	// 월 출하량
				data.put("YY_QTY"		, row[co++]);	// 년 출하량
				//data.put("TOT_QTY"		, row[co++]);
    			list.add(data);
			}
		}

		apiResponse.putBody("result", list);
		return apiResponse;
	}

	/**
	 * 출하내역상세
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA111Process(ApiRequestDto requestDto) throws Exception {

		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String stdDt = CommonUtils.isToString(requestDto.reqBodyMap.get("stdDt"));			// 기준 일자
		String salUtCd = CommonUtils.isToString(requestDto.reqBodyMap.get("salUtCd"));		// 영업 단위 코드
		String prodCd = CommonUtils.isToString(requestDto.reqBodyMap.get("prodCd"));		// 제품 코드
		String fldNm = CommonUtils.isToString(requestDto.reqBodyMap.get("fldNm"));			// 현장 명
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));			// 사원번호
		String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));			// 화면ID
		String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));		// 작업유형
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));	// MAC주소

		// validation check
		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}

		String action = Constants.CLIENT_REQUEST;

		Element eleAction = new Element(action, "http://www.openuri.org/");
		eleAction.addContent(SoapUtil.getElementNew("STD_DT", stdDt));
		eleAction.addContent(SoapUtil.getElementNew("SAL_UT_CD", salUtCd));
		eleAction.addContent(SoapUtil.getElementNew("PROD_CD", prodCd));
		eleAction.addContent(SoapUtil.getElementNew("FLD_NM", fldNm));
		eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));

		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMap(fldSiteQTYDTLProcess, SoapUtil.getSoapXmlString( eleAction ));

		// EAI 통신 결과 String 변환
		String apiResult = super.xmlMapToStringNew(resString);

		apiResult = apiResult.substring(1);
		String[] rows = apiResult.split("\n");

		for (int i = 0; i < rows.length; ++i) {
			if (!CommonUtils.isExist(rows[i])) {
					logger.debug("Skip empty row : " + i);
			}else{

				String[] row = rows[i].split("\\|\\|", -1);
				Map<String, Object> data = new HashMap<String, Object>();

    			int co = 0;
    			data.put("FWRD_NO"		, row[co++]);	// 출하 번호
    			data.put("FLD_SITE_CD"	, row[co++]);	// 현장 사이트 코드
    			data.put("FLD_NM"		, row[co++]);	// 현장 명
    			data.put("FWRD_FACT_CD"	, row[co++]);	// 출하 공장 코드
    			data.put("FWRD_FACT_NM"	, row[co++]);	// 출하 공장 명
				data.put("PROD_CD"		, row[co++]);	// 제품 코드
				data.put("PROD_NM"		, row[co++]);	// 제품 명
				data.put("TRSP_MEAN_CD"	, row[co++]);	// 수송 수단 코드
				data.put("TRSP_MEAN_NM"	, row[co++]);	// 수송 수단 명
				data.put("FWRD_TM"		, row[co++]);	// 출하 시간
				data.put("FWRD_QTY"		, row[co++]);	// 출하 량
				data.put("CAR_NO"		, row[co++]);	// 차량 번호
				data.put("OPERR_NM"		, row[co++]);	// 운영자 성명
    			list.add(data);
			}
		}

		apiResponse.putBody("result", list);
		return apiResponse;
	}

	/**
	 * 출하의뢰내역조회
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA121Process(ApiRequestDto requestDto) throws Exception {

		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String fwrdReqDt = CommonUtils.isToString(requestDto.reqBodyMap.get("fwrdReqDt"));	// 출하 의뢰 일자
		//String fwrdReqDt = "20250722";	// 출하 의뢰 일자
		//String salUtCd = CommonUtils.isToString(requestDto.reqBodyMap.get("salUtCd"));		// 영업 단위 코드
		String salUtCd = "0015";		// 영업 단위 코드
		String custNm = CommonUtils.isToString(requestDto.reqBodyMap.get("custNm"));		// 거래처명
		String factCd = factConvert(CommonUtils.isToString(requestDto.reqBodyMap.get("factCd")));		// 공장명 > 추가함 S0017
		String prodCd = CommonUtils.isToString(requestDto.reqBodyMap.get("prodCd"));		// 제품명 > 추가함 S0017
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));			// 사원번호
		String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));			// 화면ID
		String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));		// 작업유형
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));	// MAC주소

		// validation check
		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}

		Element eleAction = new Element(Constants.CLIENT_REQUEST);
		eleAction.addContent(SoapUtil.getElementNew("FWRD_REQ_DT", fwrdReqDt));
		eleAction.addContent(SoapUtil.getElementNew("SAL_UT_CD", salUtCd));
		eleAction.addContent(SoapUtil.getElementNew("CUST_NM", custNm));
		eleAction.addContent(SoapUtil.getElementNew("FWRD_FACT_CD", factCd)); // 공장명 > 추가함 S0017
		eleAction.addContent(SoapUtil.getElementNew("PROD_CD", prodCd));      // 제품명 > 추가함 S0017		
		eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));

		//String url = "SSYCNE_JMA_01.JMA_008.ws:JMA_008_P";
		
		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMapNew("dummyUrl",fwrdREQProcess, SoapUtil.getSoapXmlStringNew( eleAction,fwrdREQProcess ));

		// EAI 통신 결과 String 변환
		String apiResult = super.xmlMapToStringNew(resString);


		logger.debug(apiResult);
		
		apiResult = apiResult.substring(1);
		String[] rows = apiResult.substring(apiResult.indexOf("||") + 2).split("\n");
		
		for (int i = 0; i < rows.length; ++i) {
			if (!CommonUtils.isExist(rows[i])) {
					logger.debug("Skip empty row : " + i);
			}else{

				String[] row = rows[i].split("\\|\\|", -1);
				Map<String, Object> data = new HashMap<String, Object>();

    			int co = 0;
    			
    			data.put("FWRD_REQNO"      , row[co++]);	// 출하 의뢰번호
				data.put("FWRD_FACT_CD"    , row[co++]);	// 출하 공장 코드
				data.put("FWRD_FACT_NM"    , row[co++]);	// 출하 공장 명
				data.put("FLD_SITE_CD"     , row[co++]);	// 현장 사이트 코드
				data.put("FLD_NM"          , row[co++]);	// 현장 명
				data.put("PROD_CD"         , row[co++]);	// 제품 코드
				data.put("PROD_NM"         , row[co++]);	// 제품 명
				data.put("SLTYP"           , row[co++]);	// 판매형태 (new)
				data.put("SAL_FORM_CD"     , row[co++]);	// 판매 형태 코드
				data.put("SAL_FG_CD"       , row[co++]);	// 판매 구분 코드
				data.put("SAL_FG_NM"       , row[co++]);	// 판매 구분 명
				data.put("TRSP_MEAN_CD"    , row[co++]);	// 수송 수단 코드
				data.put("TRSP_MENA_NM"    , row[co++]);	// 수송 수단 명
				data.put("TRSPCOM_CD"      , row[co++]);	// 수송사 코드
				data.put("TRSPCOM_NM"      , row[co++]);	// 수송사 명
				data.put("FWRD_REQTY"      , row[co++]);	// 출하 의뢰량
				data.put("VRKME"	       , row[co++]);	// 오더단뒤(new)
				data.put("DLIVY_STAT_CD"   , row[co++]);	// 출고 상태 코드
				data.put("DLIVY_STAT_NM"   , row[co++]);	// 출고 상태 명
				data.put("DETAIL"          , row[co++]);	// 세부내역
				data.put("FLAG"            , row[co++]);	// 플래그
    			list.add(data);
			}
		}

		apiResponse.putBody("result", list);
		return apiResponse;
	}

	/**
	 * 출하의뢰 상세내역 조회
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA122Process(ApiRequestDto requestDto) throws Exception {

		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String fwrdReqno = CommonUtils.isToString(requestDto.reqBodyMap.get("fwrdReqno"));	// 출하 의뢰 번호
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));			// 사원번호
		String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));			// 화면ID
		String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));		// 작업유형
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));	// MAC주소

		// validation check
		if (!CommonUtils.isExist(fwrdReqno)) {
			throw new ValidationException(Constants.FWRD_REQNO);
		}

		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}

		Element eleAction = new Element(Constants.CLIENT_REQUEST);
		eleAction.addContent(SoapUtil.getElementNew("FWRD_REQNO", fwrdReqno));
		eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));

		
		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMapNew("dummyUrl",fwrdREQDTLProcess, SoapUtil.getSoapXmlStringNew( eleAction,fwrdREQDTLProcess ));

		// EAI 통신 결과 String 변환
		String apiResult = super.xmlMapToStringNew(resString);

		apiResult = apiResult.substring(1);
		//String[] rows = apiResult.split("\n");
		String[] rows = apiResult.substring(apiResult.indexOf("||") + 2).split("\n");

		for (int i = 0; i < rows.length; ++i) {
			if (!CommonUtils.isExist(rows[i])) {
					logger.debug("Skip empty row : " + i);
			}else{

				String[] row = rows[i].split("\\|\\|", -1);
				Map<String, Object> data = new HashMap<String, Object>();

    			int co = 0;
                data.put("FWRD_REQNO", row[co]);
                data.put("FWRD_REQ_DT", row[co++]);
                data.put("SAL_SAL_UT_CD", row[co++]);
                data.put("SAL_SAL_UT_NM", row[co++]);
                data.put("FLD_SITE_CD", row[co++]);
                data.put("FLD_NM", row[co++]);
                data.put("PROD_CD", row[co++]);
                data.put("PROD_NM", row[co++]);
                data.put("SAL_FORM_CD", row[co++]);
                data.put("SAL_FORM_NM", row[co++]);
                data.put("FWRD_FACT_CD", row[co++]);
                data.put("FWRD_FACT_NM", row[co++]);
                data.put("SAL_FG_CD", row[co++]);
                data.put("SAL_FG_NM", row[co++]);
                data.put("PREFWRD_SAL_DT", row[co++]);
                data.put("TRSP_MEAN_CD", row[co++]);
                data.put("TRSP_MEAN_NM", row[co++]);
                data.put("DLIVY_PLC_CD", row[co++]);
                data.put("DLIVY_PLC_NM", row[co++]);
                data.put("TRSPCOM_CD", row[co++]);
                data.put("TRSPCOM_NM", row[co++]);
                data.put("FWRD_REQTY", row[co++]);
                data.put("FWRD_REQ_UTPRI", row[co++]);
                data.put("FWRD_REQ_AMT", row[co++]);
                data.put("DLIVY_STAT_CD", row[co++]);
                data.put("DLIVY_STAT_NM", row[co++]);
                data.put("REMAIN_QTY", row[co++]);
                data.put("FWRD_QTY", row[co++]);
    			list.add(data);
			}
		}

		apiResponse.putBody("result", list);
		return apiResponse;
	}

	/**
	 * 출하의뢰내역 입력 수정 삭제 처리
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA123Process(ApiRequestDto requestDto) throws Exception {

		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		Map<String, Object> result = new HashMap<String, Object>();

		String fwrdReqno = CommonUtils.isToString(requestDto.reqBodyMap.get("fwrdReqno"));			// 출하 의뢰번호
		String fwrdReqDt = CommonUtils.isToString(requestDto.reqBodyMap.get("fwrdReqDt"));			// 출하 의뢰 일자
		String prefwrdSalDt = CommonUtils.isToString(requestDto.reqBodyMap.get("prefwrdSalDt"));	// 선출하 판매일자
		String fwrdReqty = CommonUtils.isToString(requestDto.reqBodyMap.get("fwrdReqty"));			// 출하 의뢰수량
		String fwrdReqUtpri = CommonUtils.isToString(requestDto.reqBodyMap.get("fwrdReqUtpri"));	// 출하 의뢰단가
		String flag = CommonUtils.isToString(requestDto.reqBodyMap.get("flag"));					// 처리FLAGID
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));					// 사원번호
		//String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));				// 화면ID
		//String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));				// 작업유형
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));			// MAC주소

		// validation check
		if (!CommonUtils.isExist(fwrdReqno)) {
			throw new ValidationException(Constants.FWRD_REQNO);
		}

		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}

		Element eleAction = new Element("clientRequestwithReturn");
		eleAction.addContent(SoapUtil.getElementNew("FWRD_REQNO", fwrdReqno));
		eleAction.addContent(SoapUtil.getElementNew("FWRD_REQ_DT", fwrdReqDt));
		eleAction.addContent(SoapUtil.getElementNew("PREFWRD_SAL_DT", prefwrdSalDt));
		eleAction.addContent(SoapUtil.getElementNew("FWRD_REQTY", fwrdReqty));
		eleAction.addContent(SoapUtil.getElementNew("FWRD_REQ_UTPRI", fwrdReqUtpri));
		eleAction.addContent(SoapUtil.getElementNew("FLAG", flag));
		eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		//eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		//eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));

		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMapNew("dummyURL", fwrdREQEXCUTEProcess, SoapUtil.getSoapXmlStringNew(eleAction, fwrdREQEXCUTEProcess));

		String apiResult = super.xmlMapToStringNew(resString);

		String rtnFG = apiResult.substring(0,1);
        apiResult    = apiResult.substring(1);
		String[] row = apiResult.split("\\|\\|", -1);

		if ( row != null ) {

			// rtn이 1이고 정상 MSG rtn시
			if (rtnFG.equals("1") && row.length == 2 && row[0].length() == 17 )	{

            	result.put("MESSAGE", row[0]);			// 출하의뢰번호
    			result.put("SUCCESS", true);

            // 삭제
			} else if(rtnFG.equals("1") && row.length == 2 && row[0].length() != 17 )	{

            	result.put("MESSAGE", fwrdReqno);		// 출하의뢰번호
    			result.put("SUCCESS", true);

            // EAI rtn 오류
			}else if(rtnFG.equals("0")){

				result.put("MESSAGE", row[0]);			// 출하의뢰번호
    			result.put("SUCCESS", false);
            }
		}else{
			throw new ApiException("오류입니다.");
		}

        apiResponse.putBody("result", result);
		return apiResponse;
	}

	/**
	 * 생산출하재고현황(공장코드 미입력 시)
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA124Process(ApiRequestDto requestDto) throws Exception {

		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String fwrdDt = CommonUtils.isToString(requestDto.reqBodyMap.get("fwrdDt"));		// 출하 일자
		String prodFg = CommonUtils.isToString(requestDto.reqBodyMap.get("prodFg"));		// 제품 구분
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));			// 사원번호
		String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));			// 화면ID
		String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));		// 작업유형
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));	// MAC주소

		// validation check
		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}

		if (!CommonUtils.isExist(fwrdDt)) {
			throw new ValidationException(Constants.FWRD_DT);
		}

		Element eleAction = new Element("clientRequestwithReturn");
		eleAction.addContent(SoapUtil.getElementNew("FWRD_DT", fwrdDt));
		eleAction.addContent(SoapUtil.getElementNew("PROD_FG", prodFg));
		eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));

		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMapNew("dummbyUrl", fwrdSTOCKALLProcess, SoapUtil.getSoapXmlStringNew(eleAction, fwrdSTOCKALLProcess));

		// EAI 통신 결과 String 변환
		String apiResult = super.xmlMapToStringNew(resString);

		apiResult = apiResult.substring(1);
		String[] rows = apiResult.substring(apiResult.indexOf("||") + 2).split("\n");
		logger.debug("rows length : " + rows.length);

		for (int i = 0; i < rows.length; ++i) {
			if (!CommonUtils.isExist(rows[i])) {
					logger.debug("Skip empty row : " + i);
			}else{

				String[] row = rows[i].split("\\|\\|", -1);
				Map<String, Object> data = new HashMap<String, Object>();

    			int co = 0;
    			data.put("SRT_ORD"      , row[co++]);	// 정렬
    			data.put("FACT_NM"		, row[co++]);   // 공장 명
    			data.put("PROD_QTY"		, row[co++]);   // 생산
    			data.put("CONSU_QTY"   	, row[co++]);   // 소비
    			data.put("TKIN_QTY"     , row[co++]);   // 반입
    			data.put("TRNS_QTY"     , row[co++]);   // 이송량
    			data.put("EXPT_QTY"     , row[co++]);   // 수출량
    			data.put("DOM_QTY"      , row[co++]);   // 내수량
    			data.put("FWRD_QTY"     , row[co++]);
    			data.put("STOCK_QTY"    , row[co++]);	// 출하계(이송량+수출량+내수량)
    			list.add(data);
			}
		}

		apiResponse.putBody("result", list);
		return apiResponse;
	}

	/**
	 * 생산출하재고현황(제주지사, 부산공장)
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA125Process(ApiRequestDto requestDto) throws Exception {

		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String fwrdDt = CommonUtils.isToString(requestDto.reqBodyMap.get("fwrdDt"));		// 출하 일자
		String prodFg = CommonUtils.isToString(requestDto.reqBodyMap.get("prodFg"));		// 제품 구분
		String factCd = CommonUtils.isToString(requestDto.reqBodyMap.get("factCd"));		// 공장 코드
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));			// 사원번호
		String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));			// 화면ID
		String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));		// 작업유형
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));	// MAC주소

		// validation check
		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}

		if (!CommonUtils.isExist(fwrdDt)) {
			throw new ValidationException(Constants.FWRD_DT);
		}

		Element eleAction = new Element("clientRequestwithReturn");
		eleAction.addContent(SoapUtil.getElementNew("FWRD_DT", fwrdDt));
		eleAction.addContent(SoapUtil.getElementNew("PROD_FG", prodFg));
		eleAction.addContent(SoapUtil.getElementNew("FACT_CD", factCd));
		eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));

		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMapNew("dummyURL", fwrdSTOCK1Process, SoapUtil.getSoapXmlStringNew(eleAction, fwrdSTOCK1Process));

		// EAI 통신 결과 String 변환
		String apiResult = super.xmlMapToStringNew(resString);

		apiResult = apiResult.substring(1);
		String[] rows = apiResult.substring(apiResult.indexOf("||") + 2).split("\n");

		for (int i = 0; i < rows.length; ++i) {
			if (!CommonUtils.isExist(rows[i])) {
					logger.debug("Skip empty row : " + i);
			}else{

				String[] row = rows[i].split("\\|\\|", -1);
				Map<String, Object> data = new HashMap<String, Object>();

    			int co = 0;
    			data.put("SRT_ORD"		, row[co++]);	// 정렬
    			data.put("PROD_CD"      , row[co++]);   // 제품 코드
    			data.put("FACT_NM"      , row[co++]);
    			data.put("PROD_QTY"     , row[co++]);   // 생산
    			data.put("CONSU_QTY"    , row[co++]);   // 소비
    			data.put("TKIN_QTY"     , row[co++]);   // 이송량
    			data.put("TRNS_QTY"     , row[co++]);   // 반입
    			data.put("EXPT_QTY"     , row[co++]);   // 수출량
    			data.put("DOM_QTY"      , row[co++]);   // 수출량
    			data.put("FWRD_QTY"     , row[co++]);
    			data.put("STOCK_QTY"    , row[co++]);   // 출하계(이송량+수출량+내수량)
    			list.add(data);
			}
		}

		apiResponse.putBody("result", list);
		return apiResponse;
	}

	/**
	 * 생산출하재고현황(제주지사, 부산공장 외 공장코드)
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA126Process(ApiRequestDto requestDto) throws Exception {

		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String fwrdDt = CommonUtils.isToString(requestDto.reqBodyMap.get("fwrdDt"));		// 출하 일자
		String prodFg = CommonUtils.isToString(requestDto.reqBodyMap.get("prodFg"));		// 제품 구분
		String factCd = CommonUtils.isToString(requestDto.reqBodyMap.get("factCd"));		// 공장 코드
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));			// 사원번호
		String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));			// 화면ID
		String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));		// 작업유형
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));	// MAC주소

		// validation check
		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}

		if (!CommonUtils.isExist(fwrdDt)) {
			throw new ValidationException(Constants.FWRD_DT);
		}

		String action = Constants.CLIENT_REQUEST;

		Element eleAction = new Element(action);
		eleAction.addContent(SoapUtil.getElementNew("FWRD_DT", fwrdDt));
		eleAction.addContent(SoapUtil.getElementNew("PROD_FG", prodFg));
		eleAction.addContent(SoapUtil.getElementNew("FACT_CD", factCd));
		eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));

		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMapNew("dummyUrl", fwrdSTOCK2Process, SoapUtil.getSoapXmlStringNew(eleAction, fwrdSTOCK2Process));

		// EAI 통신 결과 String 변환
		String apiResult = super.xmlMapToStringNew(resString);

		apiResult = apiResult.substring(1);
		String[] rows = apiResult.substring(apiResult.indexOf("||") + 2).split("\n");

		for (int i = 0; i < rows.length; ++i) {
			if (!CommonUtils.isExist(rows[i])) {
					logger.debug("Skip empty row : " + i);
			}else{

				String[] row = rows[i].split("\\|\\|", -1);
				Map<String, Object> data = new HashMap<String, Object>();

    			int co = 0;
    			data.put("FACT_NM"		, row[co++]);	// 공장 명(또는 합계 문구)
    			data.put("PROD_QTY"     , row[co++]);   // 생산
    			data.put("CONSU_QTY"    , row[co++]);   // 소비
    			data.put("TKIN_QTY"     , row[co++]);   // 반입
    			data.put("TRNS_QTY"     , row[co++]);   // 이송량
    			data.put("EXPT_QTY"     , row[co++]);   // 수출량
    			data.put("DOM_QTY"      , row[co++]);   // 내수량
    			data.put("FWRD_QTY"     , row[co++]);
    			data.put("STOCK_QTY"    , row[co++]);	// 출하계(이송량+수출량+내수량)
    			list.add(data);
			}
		}

		apiResponse.putBody("result", list);
		return apiResponse;
	}

	/**
	 * 시간대별내수 출하현황
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA127Process(ApiRequestDto requestDto) throws Exception {

		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String fwrdDt = CommonUtils.isToString(requestDto.reqBodyMap.get("fwrdDt"));		// 출하 일자
		String factCd = CommonUtils.isToString(requestDto.reqBodyMap.get("factCd"));		// 공장 코드
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));			// 사원번호
		String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));			// 화면ID
		String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));		// 작업유형
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));	// MAC주소

		// validation check
		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}

		if (!CommonUtils.isExist(fwrdDt)) {
			throw new ValidationException(Constants.FWRD_DT);
		}

		Element eleAction = new Element(Constants.CLIENT_REQUEST);
		eleAction.addContent(SoapUtil.getElementNew("FWRD_DT", fwrdDt));
		eleAction.addContent(SoapUtil.getElementNew("FACT_CD", factCd));
		eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));

		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMapNew("dummy",fwrdTIMEProcess, SoapUtil.getSoapXmlStringNew( eleAction,fwrdTIMEProcess ));

		// EAI 통신 결과 String 변환
		String apiResult = super.xmlMapToStringNew(resString);

		apiResult = apiResult.substring(1);
		String[] rows = apiResult.substring(apiResult.indexOf("||") + 2).split("\n");

        for(int i = 0; i < rows.length; ++i) {
            if (!CommonUtils.isExist(rows[i])) {
               this.logger.debug("Skip empty row : " + i);
            } else {
               String[] row;
               HashMap data;
               String tbGbn;
               int var22;
               label73: {
                  row = rows[i].split("\\|\\|", -1);
                  data = new HashMap();
                  int co = 0;
                  String tmGbn = "";
                  var22 = co + 1;
                  String time = row[co];
                  tbGbn = "";
                  switch(time.hashCode()) {
                  case 1537:
                     if (time.equals("01")) {
                        tbGbn = "6시 이전";
                        break label73;
                     }
                     break;
                  case 1538:
                     if (time.equals("02")) {
                        tbGbn = "~8시";
                        break label73;
                     }
                     break;
                  case 1539:
                     if (time.equals("03")) {
                        tbGbn = "~10시";
                        break label73;
                     }
                     break;
                  case 1540:
                     if (time.equals("04")) {
                        tbGbn = "~12시";
                        break label73;
                     }
                     break;
                  case 1541:
                     if (time.equals("05")) {
                        tbGbn = "~13시";
                        break label73;
                     }
                     break;
                  case 1542:
                     if (time.equals("06")) {
                        tbGbn = "~14시";
                        break label73;
                     }
                     break;
                  case 1543:
                     if (time.equals("07")) {
                        tbGbn = "~15시";
                        break label73;
                     }
                     break;
                  case 1544:
                     if (time.equals("08")) {
                        tbGbn = "~16시";
                        break label73;
                     }
                     break;
                  case 1545:
                     if (time.equals("09")) {
                        tbGbn = "~17시";
                        break label73;
                     }
                     break;
                  case 1567:
                     if (time.equals("10")) {
                        tbGbn = "~18시";
                        break label73;
                     }
                     break;
                  case 1568:
                     if (time.equals("11")) {
                        tbGbn = "18시 이후";
                        break label73;
                     }
                  }

                  tbGbn = "기타";
               }

               data.put("TM_GBN", tbGbn);
               data.put("NOW_QTY", row[var22++]);
               data.put("NOW_RAT", row[var22++]);
               data.put("ACC_NOW_QTY", row[var22++]);
               data.put("YESTER_RAT", row[var22++]);
               list.add(data);
            }
         }
		apiResponse.putBody("result", list);
		return apiResponse;
	}

	/**
	 * H원료현황 조회
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA128Process(ApiRequestDto requestDto) throws Exception {

		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String fwrdDt = CommonUtils.isToString(requestDto.reqBodyMap.get("fwrdDt"));		// 출하 일자
		String factCd = CommonUtils.isToString(requestDto.reqBodyMap.get("factCd"));		// 공장 코드
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));			// 사원번호
		String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));			// 화면ID
		String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));		// 작업유형
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));	// MAC주소

		// validation check
		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}

		if (!CommonUtils.isExist(fwrdDt)) {
			throw new ValidationException(Constants.FWRD_DT);
		}

		Element eleAction = new Element(Constants.CLIENT_REQUEST);
		eleAction.addContent(SoapUtil.getElementNew("FWRD_DT", fwrdDt));
		eleAction.addContent(SoapUtil.getElementNew("FACT_CD", factCd));
		eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));

		// EAI 통신
		//Map<String, Object> resString = SoapUtil.sendSoapServerMap(hmatSTOCKProcess, SoapUtil.getSoapXmlString( eleAction ));
		
		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMapNew("dummyUrl",hmatSTOCKProcess, SoapUtil.getSoapXmlStringNew( eleAction,hmatSTOCKProcess ));
		

		// EAI 통신 결과 String 변환
		String apiResult = super.xmlMapToStringNew(resString);

		apiResult = apiResult.substring(1);
		String[] rows = apiResult.substring(apiResult.indexOf("||") + 2).split("\n");

		for (int i = 0; i < rows.length; ++i) {
			if (!CommonUtils.isExist(rows[i])) {
					logger.debug("Skip empty row : " + i);
			}else{

				String[] row = rows[i].split("\\|\\|", -1);
				Map<String, Object> data = new HashMap<String, Object>();

    			int co = 0;
    			data.put("FACT_NM"		, row[co++]);	// 공장명
    			data.put("TEAM_NM"		, row[co++]);
    			data.put("HOPER1" 		, row[co++]);   // 호퍼1
    			data.put("HOPER2" 		, row[co++]);   // 호퍼2
    			data.put("SILO1"  		, row[co++]);   // 싸이로1
    			data.put("SILO2"  		, row[co++]);   // 싸이로2
    			data.put("WAIT_CAR_CNT"	, row[co++]);   // 대기 차량 수
    			list.add(data);
			}
		}

		apiResponse.putBody("result", list);
		return apiResponse;
	}

	/**
	 * 화면별 권한 조회
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA129Process(ApiRequestDto requestDto) throws Exception {

		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));			// 사원번호
		String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));			// 화면ID
		String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));		// 작업유형
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));	// MAC주소

		// validation check
		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}

		Element eleAction = new Element("clientRequestwithReturn");
		eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));

		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMapNew("dummyUrl", authProcess, SoapUtil.getSoapXmlStringNew(eleAction, authProcess));

		// EAI 통신 결과 String 변환
		String apiResult = super.xmlMapToStringNew(resString);

		apiResult = apiResult.substring(1);
		String[] rows = apiResult.substring(apiResult.indexOf("||") + 2).split("\n");

		for (int i = 0; i < rows.length; ++i) {
			if (!CommonUtils.isExist(rows[i])) {
					logger.debug("Skip empty row : " + i);
			}else{

				String[] row = rows[i].split("\\|\\|", -1);
				Map<String, Object> data = new HashMap<String, Object>();

    			int co = 0;
    			data.put("COMB_ORG_CD"		, row[co++]);	// 통합 조직 코드
    			data.put("COMB_ORG_NM"		, row[co++]);	// 통합 조직 명
    			data.put("UZR_NM"      		, row[co++]);	// 사용자 성명
    			data.put("SAL_UT_CD"     	, row[co++]);	// 영업 단위 코드
    			data.put("SCR_ID"  			, row[co++]);	// 화면 ID
    			data.put("SCR_NM"   		, row[co++]);	// 화면 명
    			data.put("SCR_AUTH"   		, row[co++]);	// 화면 권한 (S:조회, U:조회/입력/수정, D:조회/입력/수정/삭제, X:사용제한)
    			data.put("SCR_AUTH_DTL"   	, row[co++]);	// 화면 권한 상세
    			list.add(data);
			}
		}

		apiResponse.putBody("result", list);

		return apiResponse;
	}

	/**
	 * 공통코드 조회(여신그룹, 거래처, 사이트, 현장, 영업단위, 수송사, 영업사원, 공장, 부서, 제품)
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA130Process(ApiRequestDto requestDto) throws Exception {
		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String name = CommonUtils.isToString(requestDto.reqBodyMap.get("name"));			// 조회 코드
		String fg = CommonUtils.isToString(requestDto.reqBodyMap.get("fg"));				// 조회 구분
		String option = CommonUtils.isToString(requestDto.reqBodyMap.get("option"));				// 조회 구분
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));		// 회원 ID
		String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));			// 화면 ID
		String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));		// 작업유형
		String uzrIp = CommonUtils.isToString(requestDto.reqBodyMap.get("uzrIp"));			// 사용자IP
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));	// MAC주소

		// validation check
		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}
		
		
		

		Element eleAction = new Element("clientRequestwithReturn");
		eleAction.addContent(SoapUtil.getElementNew("NAME", name));
		eleAction.addContent(SoapUtil.getElementNew("FG", fg));
		eleAction.addContent(SoapUtil.getElementNew("OPTION", option));
		eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));
		eleAction.addContent(SoapUtil.getElementNew("UZR_IP", uzrIp));

		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMapNew("dummyUrl", commonCode, SoapUtil.getSoapXmlStringNew(eleAction, commonCode));
		
		// EAI 통신 결과 String 변환
		String apiResult = super.xmlMapToStringNew(resString);

		apiResult = apiResult.substring(1);
		String[] rows = apiResult.substring(apiResult.indexOf("||") + 2).split("\n");

		for (int i = 0; i < rows.length; ++i) {
			if (!CommonUtils.isExist(rows[i])) {
					logger.debug("Skip empty row : " + i);
			}else{

				String[] row = rows[i].split("\\|\\|", -1);
				Map<String, Object> data = new HashMap<String, Object>();

    			int co = 0;
    			data.put("CODE"		, row[co++]);	// 조회 코드
    			data.put("NAME"		, row[co++]);	// 조회 코드명
    			data.put("IDX1" 	, row[co++]);	// 조회 구분별 데이터 값
    			data.put("IDX2"    	, row[co++]);	// 조회 구분별 데이터 값
    			data.put("IDX3" 	, row[co++]);	// 조회 구분별 데이터 값
    			data.put("IDX4" 	, row[co++]);	// 조회 구분별 데이터 값
    			data.put("IDX5" 	, row[co++]);	// 조회 구분별 데이터 값
    			data.put("IDX6"   	, row[co++]);	// 조회 구분별 데이터 값
    			list.add(data);
			}
		}

		apiResponse.putBody("result", list);

		return apiResponse;
	}
	
	/**
	 * 민수입금입력 조회
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA131Process(ApiRequestDto requestDto) throws Exception {

		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String rcptDt = CommonUtils.isToString(requestDto.reqBodyMap.get("rcptDt"));		// 입금일자
		String salUtCd = CommonUtils.isToString(requestDto.reqBodyMap.get("salUtCd"));		// 영업 단위 코드
		String custNm = CommonUtils.isToString(requestDto.reqBodyMap.get("custNm"));		// 거래처명
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));			// 사원번호
		String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));			// 화면ID
		String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));		// 작업유형
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));	// MAC주소

		// validation check
		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}

		Element eleAction = new Element(Constants.CLIENT_REQUEST, "http://www.openuri.org/");
		eleAction.addContent(SoapUtil.getElementNew("RCPT_DT", rcptDt));
		eleAction.addContent(SoapUtil.getElementNew("SAL_UT_CD", salUtCd));
		eleAction.addContent(SoapUtil.getElementNew("CUST_NM", custNm));
		eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));

		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMap(rcptListProcess, SoapUtil.getSoapXmlString( eleAction ));

		// EAI 통신 결과 String 변환
		String apiResult = super.xmlMapToStringNew(resString);


		apiResult = apiResult.substring(1);
		String[] rows = apiResult.split("\n");

		for (int i = 0; i < rows.length; ++i) {
			if (!CommonUtils.isExist(rows[i])) {
					logger.debug("Skip empty row : " + i);
			}else{

				String[] row = rows[i].split("\\|\\|", -1);
				Map<String, Object> data = new HashMap<String, Object>();

    			int co = 0;
    			data.put("RCPT_NO"      , row[co++]);	// 입금번호
				data.put("SAL_FORM_NM"    , row[co++]);	// 판매형태명
				data.put("FLD_NM"    , row[co++]);	// 거래처명
				data.put("PROD_NM"     , row[co++]);	// 제품명
				data.put("RCPT_SAL_UT_NM"          , row[co++]);	// 입금 영업단위명
				data.put("SAL_SAL_UT_NM"         , row[co++]);	// 판매 영업단위명
				data.put("RCPT_CHRGR_EMPNM"         , row[co++]);	// 입금 담당자명
				data.put("RCPT_FORM_NM"     , row[co++]);	// 입금 형태명
				data.put("FUND_FG_NM"       , row[co++]);	// 자금 구분명
				data.put("RCPT_AMT"       , row[co++]);	// 입금액
    			list.add(data);
			}
		}

		apiResponse.putBody("result", list);
		return apiResponse;
	}
	
	
	/**
	 * 민수입금입력 상세조회
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA132Process(ApiRequestDto requestDto) throws Exception {

		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String rcptno = CommonUtils.isToString(requestDto.reqBodyMap.get("rcptNo"));		// 입금번호
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));			// 사원번호
		String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));			// 화면ID
		String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));		// 작업유형
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));	// MAC주소

		// validation check
		if (!CommonUtils.isExist(rcptno)) {
			throw new ValidationException(Constants.RCPT_NO);
		}

		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}

		Element eleAction = new Element(Constants.CLIENT_REQUEST, "http://www.openuri.org/");
		eleAction.addContent(SoapUtil.getElementNew("RCPT_NO", rcptno));
		eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));

		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMap(rcptListDtlProcess, SoapUtil.getSoapXmlString( eleAction ));

		// EAI 통신 결과 String 변환
		String apiResult = super.xmlMapToStringNew(resString);

		apiResult = apiResult.substring(1);
		String[] rows = apiResult.split("\n");

		for (int i = 0; i < rows.length; ++i) {
			if (!CommonUtils.isExist(rows[i])) {
					logger.debug("Skip empty row : " + i);
			}else{

				String[] row = rows[i].split("\\|\\|", -1);
				Map<String, Object> data = new HashMap<String, Object>();

    			int co = 0;
    			data.put("RCPT_NO"         , row[co++]);	// 입금 번호 
				data.put("RCPT_DT"        , row[co++]); 	// 입금 일자
				data.put("SAL_FORM_CD"        , row[co++]); // 판매 형태 코드
				data.put("SAL_FORM_NM"        , row[co++]); // 판매 형태 명
				data.put("FLD_SITE_CD"      , row[co++]); // 현장 사이트 코드
				data.put("FLD_NM"        , row[co++]); // 현장 명
				data.put("PROD_CD"            , row[co++]); // 제품 코드
				data.put("PROD_NM"            , row[co++]); // 제품 명
				data.put("RCPTBL_NO"       , row[co++]); // 입금표 번호
				data.put("RCPT_SAL_UT_CD"       , row[co++]); // 입금 영업 단위
				data.put("RCPT_SAL_UT_NM"         , row[co++]); // 입금 영업 단위 명
				data.put("SAL_SAL_UT_CD"         , row[co++]); // 판매 영업 단위
				data.put("SAL_SAL_UT_NM"          , row[co++]); // 판매 영업 단위 명
				data.put("RCPT_CHRGR_EMPNO"          , row[co++]); // 입금 담당자 사원번호
				data.put("RCPT_CHRGR_EMPNM"     , row[co++]); // 입금 담당자 명
				data.put("RCPT_FORM_CD"       , row[co++]); // 입금 형태 코드
                data.put("RCPT_FORM_NM"       , row[co++]); // 입금 형태 명
				data.put("FUND_FG_CD"       , row[co++]); // 자금 구분 코드
				data.put("FUND_FG_NM"       , row[co++]); // 자금 구분 명
				data.put("NOTE_NO"         , row[co++]); // 어음 번호
				data.put("NOTE_SERNO"         , row[co++]); // 어음 일련번호
				data.put("NOTE_AMT"         , row[co++]); // 어음 금액
				data.put("NOTE_BAL"     , row[co++]); // 어음 잔액
				data.put("PUB_DT"     , row[co++]); // 발행 일자 
				data.put("PUB_BASS"       , row[co++]); // 발행 근거
				data.put("SETT_DT"   , row[co++]); // 결제 일자
				data.put("PAY_BANK_CD"      , row[co++]); // 지급 은행 코드
				data.put("PAY_BANK_NM"      , row[co++]); // 지급 은행 명
				data.put("PAY_BRA"           , row[co++]); // 지급 지점
				data.put("PUBR"    , row[co++]); // 발행자
				data.put("SOSIGN_FG_CD"         , row[co++]); // 자타수 구분 코드
				data.put("SOSIGN_FG_NM"           , row[co++]); // 자타수 구분 명
				data.put("NOTE_STAT_CD"               , row[co++]); // 어음 상태 코드
				data.put("NOTE_STAT_NM"               , row[co++]); // 어음 상태 명
				data.put("RCPT_ACCT"               , row[co++]); // 입금 계좌
				data.put("RCPT_BANK_CD"               , row[co++]); // 입금 은행 코드
				data.put("RCPT_BANK_NM"               , row[co++]); // 입금 은행 명
				data.put("RCPT_SBST_YN"               , row[co++]); // 입금 대체 여부
				data.put("RCPT_AMT"               , row[co++]); // 입금 금액
				data.put("RCPT_AMT_ORG"               , row[co++]); // 입금 금액
				data.put("SLIP_NO"               , row[co++]); // 결의서 번호
				data.put("INPR_EMPNO"               , row[co++]); // 입력자 사원번호
				data.put("ETC_RCPT_FORM_CD"               , row[co++]); // 기타 입금 형태 코드
				data.put("ETC_RCPT_AMT"               , row[co++]); // 기타 입금 금액
				data.put("CNNC_RCPT_NO"               , row[co++]); // 연결 입금 번호
				data.put("VRTL_ACCT_UZ_YN"               , row[co++]); // 가상 계좌 사용 여부
				data.put("VRTL_ACCT"               , row[co++]); // 가상 계좌
				data.put("CMS_KEY"               , row[co++]); // CMS KEY
				data.put("APRV_YN"               , row[co++]); // 승인여부
				data.put("APRV_EMPNO"               , row[co++]); // 승인 사원번호
				data.put("APRV_EMPNO2"               , row[co++]); // 승인 사원번호2
    			list.add(data);
			}
		}

		apiResponse.putBody("result", list);
		return apiResponse;
	}
	
	/**
	 * 민수입금입력 저장 처리
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA133Process(ApiRequestDto requestDto) throws Exception {

		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		Map<String, Object> result = new HashMap<String, Object>();

		String rcptNo = CommonUtils.isToString(requestDto.reqBodyMap.get("rcptNo"));			// 입금 번호
		String rcptDt = CommonUtils.isToString(requestDto.reqBodyMap.get("rcptDt"));			// 입금 일자
		String rcptAmt = CommonUtils.isToString(requestDto.reqBodyMap.get("rcptAmt"));			// 입금 금액
		String noteSerno = CommonUtils.isToString(requestDto.reqBodyMap.get("noteSerno"));		// 어음 일련번호
		String bankCd = CommonUtils.isToString(requestDto.reqBodyMap.get("bankCd"));			// 은행 코드
		String acctNo = CommonUtils.isToString(requestDto.reqBodyMap.get("acctNo"));			// 계좌 번호
		String cmsKey = CommonUtils.isToString(requestDto.reqBodyMap.get("cmsKey"));			// CMS KEY
		String vrtlAcctNo = CommonUtils.isToString(requestDto.reqBodyMap.get("vrtlAcctNo"));	// 가상 계좌 번호
		String rcptblNo = CommonUtils.isToString(requestDto.reqBodyMap.get("rcptblNo"));		// 입금표 번호
		String flag = CommonUtils.isToString(requestDto.reqBodyMap.get("flag"));				// 처리FLAG 
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));				// 사원번호
		//String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));			// 화면ID
		//String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));			// 작업유형
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));		// MAC주소

		// validation check
		if (!CommonUtils.isExist(rcptNo)) {
			throw new ValidationException(Constants.RCPT_NO);
		}

		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}

		Element eleAction = new Element(Constants.CLIENT_REQUEST, "http://www.openuri.org/");
		eleAction.addContent(SoapUtil.getElementNew("RCPT_NO", rcptNo));
		eleAction.addContent(SoapUtil.getElementNew("RCPT_DT", rcptDt));
		eleAction.addContent(SoapUtil.getElementNew("RCPT_AMT", rcptAmt));
		eleAction.addContent(SoapUtil.getElementNew("NOTE_SERNO", noteSerno));
		eleAction.addContent(SoapUtil.getElementNew("BANK_CD", bankCd));
		eleAction.addContent(SoapUtil.getElementNew("ACCT_NO", acctNo));
		eleAction.addContent(SoapUtil.getElementNew("CMS_KEY", cmsKey));
		eleAction.addContent(SoapUtil.getElementNew("VRTL_ACCT_NO", vrtlAcctNo));
		eleAction.addContent(SoapUtil.getElementNew("RCPTBL_NO", rcptblNo));
		eleAction.addContent(SoapUtil.getElementNew("FLAG", flag));
		eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		//eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		//eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));

		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMap(rcptListEXCUTElProcess, SoapUtil.getSoapXmlString( eleAction ));

		String apiResult = super.xmlMapToStringNew(resString);

		String rtnFG = apiResult.substring(0,1);
        apiResult    = apiResult.substring(1);
		String[] row = apiResult.split("\\|\\|", -1);

		if ( row != null ) {

			// rtn이 1이고 정상 MSG rtn시
			if (rtnFG.equals("1") && row.length == 2 && row[0].length() == 17 )	{

            	result.put("MESSAGE", row[0]);			// 입금번호
    			result.put("SUCCESS", true);

            // 삭제
			} else if(rtnFG.equals("1") && row.length == 2 && row[0].length() != 17 )	{

            	result.put("MESSAGE", rcptNo);		// 입금번호
    			result.put("SUCCESS", true);

            // EAI rtn 오류
			}else if(rtnFG.equals("0")){

				result.put("MESSAGE", row[0]);			// 출하의뢰번호
    			result.put("SUCCESS", false);
            }
		}else{
			throw new ApiException("오류입니다.");
		}

        apiResponse.putBody("result", result);
		return apiResponse;
	}

	/**
	 * 어음정보 조회
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA134Process(ApiRequestDto requestDto) throws Exception {

		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String fldSiteCd = CommonUtils.isToString(requestDto.reqBodyMap.get("fldSiteCd"));	// 현장 사이트 코드
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));			// 사원번호
		String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));			// 화면ID
		String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));		// 작업유형
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));	// MAC주소

		// validation check
		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}

		Element eleAction = new Element(Constants.CLIENT_REQUEST, "http://www.openuri.org/");
		eleAction.addContent(SoapUtil.getElementNew("FLD_SITE_CD", fldSiteCd));
		eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));

		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMap(noteNoListProcess, SoapUtil.getSoapXmlString( eleAction ));

		// EAI 통신 결과 String 변환
		String apiResult = super.xmlMapToStringNew(resString);


		apiResult = apiResult.substring(1);
		String[] rows = apiResult.split("\n");

		for (int i = 0; i < rows.length; ++i) {
			if (!CommonUtils.isExist(rows[i])) {
					logger.debug("Skip empty row : " + i);
			}else{

				String[] row = rows[i].split("\\|\\|", -1);
				Map<String, Object> data = new HashMap<String, Object>();

    			int co = 0;
    			data.put("NOTE_NO"      , row[co++]);	// 어음 번호
    			data.put("NOTE_SERNO"      , row[co++]);	// 어음 일련번호
				data.put("NOTE_AMT"    , row[co++]);	// 어음 금액 
				data.put("NOTE_BAL"     , row[co++]);	// 어음 잔액
				data.put("PUB_DT"     , row[co++]);	// 발행 일자
				data.put("PUB_BASS"     , row[co++]);	// 발행 근거
				data.put("SETT_DT"     , row[co++]);	// 결제 일자 
				data.put("PAY_BANK_CD"     , row[co++]);	// 지급 은행 코드
				data.put("PAY_BANK_NM"     , row[co++]);	// 지급 은행 명
				data.put("PAY_BRA"     , row[co++]);	// 지급 지점
				data.put("PUBR"     , row[co++]);	// 발행자
				data.put("SOSIGN_FG_CD"     , row[co++]);	// 자타수 구분 코드
				data.put("SOSIGN_FG_NM"     , row[co++]);	// 자타수 구분 명
				data.put("NOTE_STAT_CD"     , row[co++]);	// 어음 상태 코드
				data.put("NOTE_STAT_NM"     , row[co++]);	// 어음 상태 명
    			list.add(data);
			}
		}

		apiResponse.putBody("result", list);
		return apiResponse;
	}
	
	/**
	 * 은행별 입금내역 조회
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA135Process(ApiRequestDto requestDto) throws Exception {

		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String rcptDt = CommonUtils.isToString(requestDto.reqBodyMap.get("rcptDt"));		// 입금 일자
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));			// 사원번호
		String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));			// 화면ID
		String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));		// 작업유형
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));	// MAC주소

		// validation check
		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}

		Element eleAction = new Element(Constants.CLIENT_REQUEST, "http://www.openuri.org/");
		eleAction.addContent(SoapUtil.getElementNew("RCPT_DT", rcptDt));
		eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));

		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMap(rcptAcctListProcess, SoapUtil.getSoapXmlString( eleAction ));

		// EAI 통신 결과 String 변환
		String apiResult = super.xmlMapToStringNew(resString);


		apiResult = apiResult.substring(1);
		String[] rows = apiResult.split("\n");

		for (int i = 0; i < rows.length; ++i) {
			if (!CommonUtils.isExist(rows[i])) {
					logger.debug("Skip empty row : " + i);
			}else{

				String[] row = rows[i].split("\\|\\|", -1);
				Map<String, Object> data = new HashMap<String, Object>();

    			int co = 0;
    			data.put("BANK_CD"      , row[co++]);	// 은행 코드
    			data.put("BANK_NM"      , row[co++]);	// 은행 명
				data.put("ACCT_NO"      , row[co++]);		// 계좌 번호
				data.put("DEAL_AMT"     , row[co++]);	// 거래 금액
				data.put("BAL_AMT"      , row[co++]);	// 잔액
				data.put("NM"     		, row[co++]);	// 이름
				data.put("CMS_KEY"      , row[co++]);	// CMS KEY
    			list.add(data);
			}
		}

		apiResponse.putBody("result", list);
		return apiResponse;
	}
	
	/**
	 * 가상계좌 입금내역 조회
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA136Process(ApiRequestDto requestDto) throws Exception {

		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String rcptDt = CommonUtils.isToString(requestDto.reqBodyMap.get("rcptDt"));		// 입금 일자
		String vrtlAcctNo = CommonUtils.isToString(requestDto.reqBodyMap.get("vrtlAcctNo"));// 가상 계좌번호
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));			// 사원번호
		String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));			// 화면ID
		String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));		// 작업유형
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));	// MAC주소

		// validation check
		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}

		Element eleAction = new Element(Constants.CLIENT_REQUEST, "http://www.openuri.org/");
		eleAction.addContent(SoapUtil.getElementNew("RCPT_DT", rcptDt));
		eleAction.addContent(SoapUtil.getElementNew("VRTL_ACCT_NO", vrtlAcctNo));
		eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));

		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMap(vrtlAcctNoListProcess, SoapUtil.getSoapXmlString( eleAction ));

		// EAI 통신 결과 String 변환
		String apiResult = super.xmlMapToStringNew(resString);


		apiResult = apiResult.substring(1);
		String[] rows = apiResult.split("\n");

		for (int i = 0; i < rows.length; ++i) {
			if (!CommonUtils.isExist(rows[i])) {
					logger.debug("Skip empty row : " + i);
			}else{

				String[] row = rows[i].split("\\|\\|", -1);
				Map<String, Object> data = new HashMap<String, Object>();

    			int co = 0;
    			data.put("VRTL_ACCT_NO"     , row[co++]);	 
    			data.put("BIZ_CD"      		, row[co++]);	
    			data.put("CUST_SITE_CD"     , row[co++]);	
    			data.put("BIZR_REG_NO"      , row[co++]);	
    			data.put("CUST_NM"      	, row[co++]);	
    			data.put("SAL_UT_CD"      	, row[co++]);	
    			data.put("SAL_UT_NM"      	, row[co++]);	
    			data.put("ACCT_UT_CD"      	, row[co++]);	
				data.put("ACCT_UT_NM"    	, row[co++]);	
				data.put("CNFIRM_FG"    	, row[co++]);	
				data.put("DEAL_AMT"     	, row[co++]);	
				data.put("RCPT_AMT"     	, row[co++]);	
				data.put("DEP_SUM"     		, row[co++]);	
				data.put("BAL_AMT"     		, row[co++]);	
				data.put("REAL_RCPT_AMT"    , row[co++]);	
				data.put("DEAL_DT"     		, row[co++]);	
				data.put("WRITNG_DT"     	, row[co++]);	
    			list.add(data);
			}
		}

		apiResponse.putBody("result", list);
		return apiResponse;
	}	
	
	
	/**
	 * 배정등록 조회
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA137Process(ApiRequestDto requestDto) throws Exception {

		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String asgnDt = CommonUtils.isToString(requestDto.reqBodyMap.get("asgnDt"));		// 배정 일자
		//String salUtCd = CommonUtils.isToString(requestDto.reqBodyMap.get("salUtCd"));		// 영업 단위 코드
		String salUtCd = "0023";		// 영업 단위 코드
		//String factCd = CommonUtils.isToString(requestDto.reqBodyMap.get("factCd"));		// 공장 코드
		String factCd = "3120";
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));			// 사원 번호		
		String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));			// 화면ID
		String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));		// 작업유형
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));	// MAC주소

		// validation check
		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}

		
		//Element eleAction = new Element(Constants.CLIENT_REQUEST, "http://www.openuri.org/");
		Element eleAction = new Element(Constants.CLIENT_REQUEST);
		
		eleAction.addContent(SoapUtil.getElementNew("ASGN_DT", asgnDt));
		eleAction.addContent(SoapUtil.getElementNew("SAL_UT_CD", salUtCd));
		eleAction.addContent(SoapUtil.getElementNew("FACT_CD", factCd));
		eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));
		
		// EAI 통신
		//Map<String, Object> resString = SoapUtil.sendSoapServerMap(asgnListProcess, SoapUtil.getSoapXmlString( eleAction ));
		Map<String, Object> resString = SoapUtil.sendSoapServerMapNew("dummyUrl",asgnListProcess, SoapUtil.getSoapXmlStringNew( eleAction,asgnListProcess ));

		
		
		// EAI 통신 결과 String 변환
		String apiResult = super.xmlMapToStringNew(resString);


		apiResult = apiResult.substring(1);
		String[] rows = apiResult.split("\n");

		for (int i = 0; i < rows.length; ++i) {
			if (!CommonUtils.isExist(rows[i])) {
					logger.debug("Skip empty row : " + i);
			}else{

				String[] row = rows[i].split("\\|\\|", -1);
				Map<String, Object> data = new HashMap<String, Object>();
				
				//차세대개발 백업
				//int co = 0;

				int co = 1;
    			   			
				data.put("SERNO"     		, row[co++]);	
				data.put("ASGN_DT"      	, row[co++]);	
				data.put("SAL_UT_CD"     	, row[co++]);
    			data.put("SAL_CHRGR_EMP_NM" , row[co++]);	
    			data.put("FLD_SITE_CD"     	, row[co++]);	
    		//	data.put("CLSS_NM"      	, row[co++]);	
    			data.put("FLD_NM"      		, row[co++]);	
    			data.put("FWRD_FACT_CD"    	, row[co++]);	
				data.put("FACT_NM"    		, row[co++]);	
				data.put("SAL_FORM_CD"    	, row[co++]);	
				data.put("SAL_FORM_NM"     	, row[co++]);	
				data.put("PROD_CD"     		, row[co++]);	
				data.put("PROD_NM"     		, row[co++]);	
				data.put("TRSP_MEAN_CD"    	, row[co++]);	
				data.put("TRSP_MEAN_NM"    	, row[co++]);	
				data.put("DLIVY_PLC_CD"     , row[co++]);	
				data.put("DLIVY_PLC_NM"     , row[co++]);	
				data.put("TRSPCOM_CD"     	, row[co++]);	
				data.put("TRSPCOM_NM"     	, row[co++]);	
			//	data.put("TRNS_TEAM_FG_CD"  , row[co++]);	
				data.put("ASGN_QTY"     	, row[co++]);	
				data.put("REM_QTY"     		, row[co++]);	
				data.put("VRKME"     		, row[co++]);	
				data.put("LMT_FWRD_YN"     	, row[co++]);	
				data.put("DESCR"     		, row[co++]);
    			
    			
    			//차세대 개발 백업
/*    			data.put("SERNO"     		, row[co++]);	
    			data.put("ASGN_DT"      	, row[co++]);	
    			data.put("SAL_UT_CD"     	, row[co++]);	
    			data.put("SAL_CHRGR_EMP_NM" , row[co++]);	
    			data.put("FLD_SITE_CD"     	, row[co++]);	
    			data.put("CLSS_NM"      	, row[co++]);	
    			data.put("FLD_NM"      		, row[co++]);	
    			data.put("FWRD_FACT_CD"    	, row[co++]);	
				data.put("FACT_NM"    		, row[co++]);	
				data.put("SAL_FORM_CD"    	, row[co++]);	
				data.put("SAL_FORM_NM"     	, row[co++]);	
				data.put("PROD_CD"     		, row[co++]);	
				data.put("PROD_NM"     		, row[co++]);	
				data.put("TRSP_MEAN_CD"    	, row[co++]);	
				data.put("TRSP_MEAN_NM"    	, row[co++]);	
				data.put("DLIVY_PLC_CD"     , row[co++]);	
				data.put("DLIVY_PLC_NM"     , row[co++]);	
				data.put("TRSPCOM_CD"     	, row[co++]);	
				data.put("TRSPCOM_NM"     	, row[co++]);	
				data.put("TRNS_TEAM_FG_CD"  , row[co++]);	
				data.put("ASGN_QTY"     	, row[co++]);	
				data.put("REM_QTY"     		, row[co++]);	
				data.put("LMT_FWRD_YN"     	, row[co++]);	
				data.put("DESCR"     		, row[co++]);	*/
				
    			list.add(data);
			}
		}

		apiResponse.putBody("result", list);
		return apiResponse;
	}	
	
	
	

	
	/**
	 * 배정등록 저장
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA138Process(ApiRequestDto requestDto) throws Exception {

		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		Map<String, Object> result = new HashMap<String, Object>();

		String serno = CommonUtils.isToString(requestDto.reqBodyMap.get("serno"));		
		String asgnDt = CommonUtils.isToString(requestDto.reqBodyMap.get("asgnDt"));		
		String salUtCd = CommonUtils.isToString(requestDto.reqBodyMap.get("salUtCd"));		
		String factCd = CommonUtils.isToString(requestDto.reqBodyMap.get("factCd"));				
		String fldSiteCd = CommonUtils.isToString(requestDto.reqBodyMap.get("fldSiteCd"));			
		String salFormCd = CommonUtils.isToString(requestDto.reqBodyMap.get("salFormCd"));	
		String prodCd = CommonUtils.isToString(requestDto.reqBodyMap.get("prodCd"));		
		String dlivyPlcCd = CommonUtils.isToString(requestDto.reqBodyMap.get("dlivyPlcCd"));		
		String trspmeanCd = CommonUtils.isToString(requestDto.reqBodyMap.get("trspmeanCd"));		
		String trspcomCd = CommonUtils.isToString(requestDto.reqBodyMap.get("trspcomCd"));		
		String trnsTeamFg = CommonUtils.isToString(requestDto.reqBodyMap.get("trnsTeamFg"));	
		String lmtFwrdYn = CommonUtils.isToString(requestDto.reqBodyMap.get("lmtFwrdYn"));		
		String asgnQty = CommonUtils.isToString(requestDto.reqBodyMap.get("asgnQty"));		
		String descr = CommonUtils.isToString(requestDto.reqBodyMap.get("descr"));		
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));				
		String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));			
		String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));		
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));



		//Element eleAction = new Element(Constants.CLIENT_REQUEST, "http://www.openuri.org/");
		Element eleAction = new Element(Constants.CLIENT_REQUEST);
		
		eleAction.addContent(SoapUtil.getElementNew("SERNO", serno));
		eleAction.addContent(SoapUtil.getElementNew("ASGN_DT", asgnDt));
		eleAction.addContent(SoapUtil.getElementNew("SAL_UT_CD", salUtCd));
		eleAction.addContent(SoapUtil.getElementNew("FWRD_FACT_CD", factCd));
		eleAction.addContent(SoapUtil.getElementNew("FLD_SITE_CD", fldSiteCd));
		eleAction.addContent(SoapUtil.getElementNew("SAL_FORM_CD", salFormCd));
		eleAction.addContent(SoapUtil.getElementNew("PROD_CD", prodCd));
		eleAction.addContent(SoapUtil.getElementNew("DLIVY_PLC_CD", dlivyPlcCd));
		eleAction.addContent(SoapUtil.getElementNew("TRSP_MEAN_CD", trspmeanCd));
		eleAction.addContent(SoapUtil.getElementNew("TRSPCOM_CD", trspcomCd));
		eleAction.addContent(SoapUtil.getElementNew("TRNS_TEAM_FG_CD", trnsTeamFg));
		eleAction.addContent(SoapUtil.getElementNew("LMT_FWRD_YN", lmtFwrdYn));
		eleAction.addContent(SoapUtil.getElementNew("ASGN_QTY", asgnQty));
		eleAction.addContent(SoapUtil.getElementNew("DESCR", descr));
		eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr)); 
		
		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMapNew("dummyUrl",asgnSaveProcess, SoapUtil.getSoapXmlStringNew( eleAction,asgnSaveProcess ));

		String apiResult = super.xmlMapToStringNew(resString);

		String rtnFG = apiResult.substring(0,1);
        apiResult    = apiResult.substring(1);
		String[] row = apiResult.split("\\|\\|", -1);

		if ( row != null ) {

			// rtn이 1이고 정상 MSG rtn시
			if (rtnFG.equals("1") &&  row[0].length() == 10 )	{

            	result.put("MESSAGE", row[0]);			// 일련번호
    			result.put("SUCCESS", true);

			}else if(rtnFG.equals("0")){

				result.put("MESSAGE", row[0]);			// 일련번호
    			result.put("SUCCESS", false);
            }
		}else{
			throw new ApiException("오류입니다.");
		}

        apiResponse.putBody("result", result);
		return apiResponse;
	}
	
	/**
	 * 안전순찰일지 조회
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA139Process(ApiRequestDto requestDto) throws Exception {
		
		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		//검색조건(날짜, 사업장, 부서명, 순찰자, 재해유형)
		String factCd = CommonUtils.isToString(requestDto.reqBodyMap.get("factCd"));			// 공장코드
		String deptCd = CommonUtils.isToString(requestDto.reqBodyMap.get("deptCd"));			// 부서코드
		String patrolEmpno = CommonUtils.isToString(requestDto.reqBodyMap.get("patrolEmpno"));	// 순찰자 사번
		String patrolEmpnm = CommonUtils.isToString(requestDto.reqBodyMap.get("patrolEmpnm"));	// 순찰자 성명
		String fromDt = CommonUtils.isToString(requestDto.reqBodyMap.get("fromDt"));			// 순찰일자
		String toDt = CommonUtils.isToString(requestDto.reqBodyMap.get("toDt"));				// 순찰일자
		String dstCd = CommonUtils.isToString(requestDto.reqBodyMap.get("dstCd"));				// 재해유형코드
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));		// MAC주소
		
		// validation check
		if (!CommonUtils.isExist(patrolEmpno)) {
			throw new ValidationException(Constants.EMPNO);
		}
		
		Element eleAction = new Element(Constants.CLIENT_REQUEST);
		eleAction.addContent(SoapUtil.getElementNew("FACT_CD", factCd));
		eleAction.addContent(SoapUtil.getElementNew("DEPT_CD", deptCd));
		eleAction.addContent(SoapUtil.getElementNew("PATROL_EMPNO", patrolEmpno));
		eleAction.addContent(SoapUtil.getElementNew("PATROL_EMPNM", patrolEmpnm));
		eleAction.addContent(SoapUtil.getElementNew("FROM_DT", fromDt));
		eleAction.addContent(SoapUtil.getElementNew("TO_DT", toDt));
		eleAction.addContent(SoapUtil.getElementNew("DST_CD", dstCd));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));
		
		// EAI 통신  
		//Map<String, Object> resString = SoapUtil.sendSoapServerMapNew(patrolListProcess, SoapUtil.getSoapXmlString( eleAction ));
		
		String url = "SSYCNE_JMA_01.JMA_031.ws:JMA_031_P";
		
		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMapNew("dummyUrl",patrolListProcess, SoapUtil.getSoapXmlStringNew( eleAction,patrolListProcess ));
		
		
		// EAI 통신 결과 String 변환
		String apiResult = super.xmlMapToStringNew(resString);
		
		apiResult = apiResult.substring(1);
		String[] rows = apiResult.split("\n");
		
		for (int i = 0; i < rows.length; ++i) {
			if (!CommonUtils.isExist(rows[i])) {
					logger.debug("Skip empty row : " + i);
			}else{

				String[] row = rows[i].split("\\|\\|", -1);
				Map<String, Object> data = new HashMap<String, Object>();

    			int co = 0;
    			//조회될 항목(날짜, 사업장, 부서명, 순찰자, 위험요인)
    			data.put("PATROL_NO"	, row[co++]);	// 순찰 번호
    			data.put("FACT_CD"		, row[co++]);	// 공장코드
    			data.put("DEPT_CD"		, row[co++]);	// 부서코드
    			data.put("FACT_NM"		, row[co++]);	// 공장명
    			data.put("DEPT_NM"		, row[co++]);	// 부서명
    			data.put("PATROL_EMPNO"	, row[co++]);	// 순찰자 사번
    			data.put("PATROL_EMPNM"	, row[co++]);	// 순찰자 성명
    			data.put("PATROL_DT"	, row[co++]);	// 순찰일자
				data.put("WORK_NM_CD"	, row[co++]);	// 작업명 코드
				data.put("WORK_PLACE_CD", row[co++]);	// 작업명 내용
				data.put("DST_CD"		, row[co++]);	// 재해유형 코드
				data.put("HAZARD_CD"	, row[co++]);	// 위험요인 코드
				data.put("SAFE_RSLT_CD"	, row[co++]);	// 조치결과 코드
				data.put("DST_NM"		, row[co++]);	// 재해유형 내용
				data.put("HAZARD_NM"	, row[co++]);	// 위험요인 내용
				data.put("SAFE_DESC"	, row[co++]);	// 조치내용
				
    			list.add(data);
			}
		}

		apiResponse.putBody("result", list);
		return apiResponse;
				
			
	}
	
	/** 
	 * 안전순찰일지 상세조회 
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA140Process(ApiRequestDto requestDto) throws Exception {
		ApiResponseDto apiResponse = new ApiResponseDto(requestDto); 
		  List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
		 
		  String patrolNo = CommonUtils.isToString(requestDto.reqBodyMap.get("patrolNo"));  // 기준 일자 
		  String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));   // 사원번호 
		  String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));   // 화면ID 
		  String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));  // 작업유형 
		  String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr")); // MAC주소 
		 
		  // validation check 
		  if (!CommonUtils.isExist(empNo)) { 
		   throw new ValidationException(Constants.EMPNO); 
		  } 
		 
		  String action = Constants.CLIENT_REQUEST; 
		 
		  Element eleAction = new Element(action); 
		  eleAction.addContent(SoapUtil.getElementNew("PATROL_NO", patrolNo)); 
		  eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo)); 
		  eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId)); 
		  eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp)); 
		  eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr)); 
		 
		  // EAI 통신 
		  Map<String, Object> resString = SoapUtil.sendSoapServerMapNew("dummbyUrl",patrolDtlProcess, SoapUtil.getSoapXmlStringNew( eleAction,patrolDtlProcess )); 
		 
		  // EAI 통신 결과 String 변환 
		  String apiResult = super.xmlMapToStringNew(resString); 
		 
		  //apiResult = apiResult.substring(1); 
		  String[] rows = apiResult.split("\n"); 
		 
		  for (int i = 0; i < rows.length; ++i) { 
		   if (!CommonUtils.isExist(rows[i])) { 
		     logger.debug("Skip empty row : " + i); 
		   }else{ 
		 
		    String[] row = rows[i].split("\\|\\|", -1); 
		    Map<String, Object> data = new HashMap<String, Object>(); 
		 
		       int co = 0; 
		       data.put("PATROL_NO"  , row[co++]); // 순찰 번호 
		       data.put("FACT_CD" , row[co++]);   // 공장코드 
		       data.put("DEPT_CD"  , row[co++]);  // 부서코드 
		       data.put("PATROL_EMPNO"  , row[co++]); // 순찰자 사번 
		       data.put("PATROL_EMPNM"  , row[co++]); // 순찰자 사번 
		       data.put("PATROL_DT"  , row[co++]); // 순찰일자 
		       data.put("WORK_NM_CD" , row[co++]);  // 작업명코드 
		       data.put("WORK_PLACE_CD" , row[co++]); // 작업장소코드 
		       data.put("HAZARD_CD"  , row[co++]); // 위험요인코드 
		       data.put("DST_CD"  , row[co++]);  // 재해유형코드 
		       data.put("SAFE_DESC" , row[co++]);  // 안전조치내용
		       
		       
		       System.out.println("※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※");
		       
		       System.out.println(row[co]);
		       
		       data.put("WORK_DESC" , row[co++]);  // 작업명
		       
		       System.out.println(row[co]);
		       
		       data.put("PATROL_TM" , row[co++]);  // 순찰시간
		       System.out.println(row[co]);
		       data.put("ORD_DEPT_CD" , row[co++]);  // 발주부서코드
		       System.out.println(row[co]);
		       data.put("HAZARD_DESC" , row[co++]);  // 위반내용
		       System.out.println(row[co]);
		       data.put("SAFE_RSLT_CD" , row[co++]);  // 조치결과 코드
		       System.out.println(row[co]);
		       data.put("HAZARD_FG_CD" , row[co++]);  // 위반자 코드
		       System.out.println(row[co]);
		       data.put("HLP_COM_CD" , row[co++]);  // 협력사 코드
		       
		       
		       System.out.println("※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※");
		       
		       
		       
		       
		       list.add(data); 
		   } 
		  } 
		 
		  apiResponse.putBody("result", list); 
		  return apiResponse;
	}
	
	/**
	 * 안전순찰일지 입력 처리
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA141Process(ApiRequestDto requestDto) throws Exception {

		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		Map<String, Object> result = new HashMap<String, Object>();

		String factCd = CommonUtils.isToString(requestDto.reqBodyMap.get("factCd"));			// 공장 코드
		String deptCd = CommonUtils.isToString(requestDto.reqBodyMap.get("deptCd"));			// 부서 코드
		String patrolEmpno = CommonUtils.isToString(requestDto.reqBodyMap.get("patrolEmpno"));	// 순찰자 사번
		String patrolDt = CommonUtils.isToString(requestDto.reqBodyMap.get("patrolDt"));		// 순찰 일자
		String workNmCd = CommonUtils.isToString(requestDto.reqBodyMap.get("workNmCd"));		// 순찰명 코드
		String workPlaceCd = CommonUtils.isToString(requestDto.reqBodyMap.get("workPlaceCd"));	// 
		String hazardCd = CommonUtils.isToString(requestDto.reqBodyMap.get("hazardCd"));		// 기준 일자
		String dstCd = CommonUtils.isToString(requestDto.reqBodyMap.get("dstCd"));				// 기준 일자
		String safeDesc = CommonUtils.isToString(requestDto.reqBodyMap.get("safeDesc"));		// 기준 일자
		
		String patrolTm = CommonUtils.isToString(requestDto.reqBodyMap.get("patrolTm"));		// 기준 일자
		String workDesc = CommonUtils.isToString(requestDto.reqBodyMap.get("workDesc"));		// 기준 일자
		String hlpComCd = CommonUtils.isToString(requestDto.reqBodyMap.get("hlpComCd"));		// 기준 일자
		String ordDeptCd = CommonUtils.isToString(requestDto.reqBodyMap.get("ordDeptCd"));		// 기준 일자
		String hazardDesc = CommonUtils.isToString(requestDto.reqBodyMap.get("hazardDesc"));		// 기준 일자
		String safeRsltCd = CommonUtils.isToString(requestDto.reqBodyMap.get("safeRsltCd"));		// 기준 일자
		String hazardFgCd = CommonUtils.isToString(requestDto.reqBodyMap.get("hazardFgCd"));		// 기준 일자
		
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));				// 사원번호
		String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));				// 화면ID
		String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));			// 작업유형
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));		// MAC주소

		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}

		Element eleAction = new Element(Constants.CLIENT_REQUEST);
		eleAction.addContent(SoapUtil.getElementNew("FACT_CD", factCd));
		eleAction.addContent(SoapUtil.getElementNew("DEPT_CD", deptCd));
		eleAction.addContent(SoapUtil.getElementNew("PATROL_EMPNO", patrolEmpno));
		eleAction.addContent(SoapUtil.getElementNew("PATROL_DT", patrolDt));
		eleAction.addContent(SoapUtil.getElementNew("WORK_NM_CD", workNmCd));
		eleAction.addContent(SoapUtil.getElementNew("WORK_PLACE_CD", workPlaceCd));
		eleAction.addContent(SoapUtil.getElementNew("HAZARD_CD", hazardCd));
		eleAction.addContent(SoapUtil.getElementNew("DST_CD", dstCd));
		eleAction.addContent(SoapUtil.getElementNew("SAFE_DESC", safeDesc.replace("\n", " ")));
		
		
		eleAction.addContent(SoapUtil.getElementNew("PATROL_TM", patrolTm));
		eleAction.addContent(SoapUtil.getElementNew("WORK_DESC", workDesc.replace("\n", " ")));
		eleAction.addContent(SoapUtil.getElementNew("HLP_COM_CD", hlpComCd));
		eleAction.addContent(SoapUtil.getElementNew("ORD_DEPT_CD", ordDeptCd));
		eleAction.addContent(SoapUtil.getElementNew("HAZARD_DESC", hazardDesc.replace("\n", " ")));
		eleAction.addContent(SoapUtil.getElementNew("SAFE_RSLT_CD", safeRsltCd));
		eleAction.addContent(SoapUtil.getElementNew("HAZARD_FG_CD", hazardFgCd));
		
		
		eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		//eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		//eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));

		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMapNew("dummbyUrl",patrolInsertProcess, SoapUtil.getSoapXmlStringNew( eleAction,patrolInsertProcess ));

		String apiResult = super.xmlMapToStringNew(resString);

		String rtnFG = apiResult.substring(0,1);
        apiResult    = apiResult.substring(1);
		String[] row = apiResult.split("\\|\\|", -1);

		if ( row != null ) {

			// rtn이 1이고 정상 MSG rtn시
			if (rtnFG.equals("1") && row.length == 2 && row[0].length() == 17 )	{

    			result.put("SUCCESS", true);
    			
			}else if(rtnFG.equals("0")){

    			result.put("SUCCESS", false);
            }
		}else{
			throw new ApiException("오류입니다.");
		}

        apiResponse.putBody("result", result);
		return apiResponse;
	}
	
	/**
	 * 안전순찰일지 수정 처리
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA142Process(ApiRequestDto requestDto) throws Exception {
		
		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		Map<String, Object> result = new HashMap<String, Object>();
		
		String patrolNo = CommonUtils.isToString(requestDto.reqBodyMap.get("patrolNo"));		// 기준 일자
		String factCd = CommonUtils.isToString(requestDto.reqBodyMap.get("factCd"));			// 기준 일자
		String deptCd = CommonUtils.isToString(requestDto.reqBodyMap.get("deptCd"));			// 기준 일자
		String patrolEmpno = CommonUtils.isToString(requestDto.reqBodyMap.get("patrolEmpno"));	// 기준 일자
		String patrolDt = CommonUtils.isToString(requestDto.reqBodyMap.get("patrolDt"));		// 기준 일자
		String workNmCd = CommonUtils.isToString(requestDto.reqBodyMap.get("workNmCd"));		// 기준 일자
		String workPlaceCd = CommonUtils.isToString(requestDto.reqBodyMap.get("workPlaceCd"));	// 기준 일자
		String hazardCd = CommonUtils.isToString(requestDto.reqBodyMap.get("hazardCd"));		// 기준 일자
		String dstCd = CommonUtils.isToString(requestDto.reqBodyMap.get("dstCd"));				// 기준 일자
		String safeDesc = CommonUtils.isToString(requestDto.reqBodyMap.get("safeDesc"));		// 기준 일자
		
		String patrolTm = CommonUtils.isToString(requestDto.reqBodyMap.get("patrolTm"));		// 기준 일자
		String workDesc = CommonUtils.isToString(requestDto.reqBodyMap.get("workDesc"));		// 기준 일자
		String hlpComCd = CommonUtils.isToString(requestDto.reqBodyMap.get("hlpComCd"));		// 기준 일자
		String ordDeptCd = CommonUtils.isToString(requestDto.reqBodyMap.get("ordDeptCd"));		// 기준 일자
		String hazardDesc = CommonUtils.isToString(requestDto.reqBodyMap.get("hazardDesc"));		// 기준 일자
		String safeRsltCd = CommonUtils.isToString(requestDto.reqBodyMap.get("safeRsltCd"));		// 기준 일자
		String hazardFgCd = CommonUtils.isToString(requestDto.reqBodyMap.get("hazardFgCd"));		// 기준 일자
		
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));			// 사원번호
		String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));			// 화면ID
		String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));		// 작업유형
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));	// MAC주소
		String flag = CommonUtils.isToString(requestDto.reqBodyMap.get("flag"));					// 처리FLAGID
		
		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}
		
		Element eleAction = new Element(Constants.CLIENT_REQUEST);
		eleAction.addContent(SoapUtil.getElementNew("FACT_CD", factCd));
		eleAction.addContent(SoapUtil.getElementNew("DEPT_CD", deptCd));
		eleAction.addContent(SoapUtil.getElementNew("PATROL_EMPNO", patrolEmpno));
		eleAction.addContent(SoapUtil.getElementNew("PATROL_DT", patrolDt));
		eleAction.addContent(SoapUtil.getElementNew("WORK_NM_CD", workNmCd));
		eleAction.addContent(SoapUtil.getElementNew("WORK_PLACE_CD", workPlaceCd));
		eleAction.addContent(SoapUtil.getElementNew("HAZARD_CD", hazardCd));
		eleAction.addContent(SoapUtil.getElementNew("DST_CD", dstCd));
		eleAction.addContent(SoapUtil.getElementNew("SAFE_DESC", safeDesc.replace("\n", " ")));
		
		eleAction.addContent(SoapUtil.getElementNew("PATROL_TM", patrolTm));
		eleAction.addContent(SoapUtil.getElementNew("WORK_DESC", workDesc.replace("\n", " ")));
		eleAction.addContent(SoapUtil.getElementNew("HLP_COM_CD", hlpComCd));
		eleAction.addContent(SoapUtil.getElementNew("ORD_DEPT_CD", ordDeptCd));
		eleAction.addContent(SoapUtil.getElementNew("HAZARD_DESC", hazardDesc.replace("\n", " ")));
		eleAction.addContent(SoapUtil.getElementNew("SAFE_RSLT_CD", safeRsltCd));
		eleAction.addContent(SoapUtil.getElementNew("HAZARD_FG_CD", hazardFgCd));
		
		
		
		eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		//eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		//eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));
		eleAction.addContent(SoapUtil.getElementNew("PATROL_NO", patrolNo));
		
		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMapNew("dummbyUrl",patrolUpdateProcess, SoapUtil.getSoapXmlStringNew( eleAction,patrolUpdateProcess ));
		
		String apiResult = super.xmlMapToStringNew(resString);
		
		String rtnFG = apiResult.substring(0,1);
		apiResult    = apiResult.substring(1);
		String[] row = apiResult.split("\\|\\|", -1);
		
		if ( row != null ) {
			
			// rtn이 1이고 정상 MSG rtn시
			if (rtnFG.equals("1") && row.length == 2 && row[0].length() == 17 )	{
				
				result.put("SUCCESS", true);
				
				// 삭제
			} else if(rtnFG.equals("1") && row.length == 2 && row[0].length() != 17 )	{
				
				result.put("SUCCESS", true);
				
				// EAI rtn 오류
			}else if(rtnFG.equals("0")){
				
				result.put("SUCCESS", false);
			}
		}else{
			throw new ApiException("오류입니다.");
		}
		
		apiResponse.putBody("result", result);
		return apiResponse;
	}
	
	/**
	 * 안전순찰일지 삭제 처리
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA143Process(ApiRequestDto requestDto) throws Exception {
		
		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		Map<String, Object> result = new HashMap<String, Object>();
		
		String patrolNo = CommonUtils.isToString(requestDto.reqBodyMap.get("patrolNo"));		// 기준 일자
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));			// 사원번호
		String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));		// 작업유형
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));	// MAC주소
		
		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}
		
		Element eleAction = new Element(Constants.CLIENT_REQUEST);
		eleAction.addContent(SoapUtil.getElementNew("PATROL_NO", patrolNo));
		
		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMapNew("dummyUrl",patrolDeleteProcess, SoapUtil.getSoapXmlStringNew( eleAction,patrolDeleteProcess ));
		
		String apiResult = super.xmlMapToStringNew(resString);
		
		String rtnFG = apiResult.substring(0,1);
		apiResult    = apiResult.substring(1);
		String[] row = apiResult.split("\\|\\|", -1);
		
		if ( row != null ) {
			
			// rtn이 1이고 정상 MSG rtn시
			if (rtnFG.equals("1") && row.length == 2 && row[0].length() == 17 )	{
				
				result.put("SUCCESS", true);
				
				// 삭제
			} else if(rtnFG.equals("1") && row.length == 2 && row[0].length() != 17 )	{
				
				result.put("SUCCESS", true);
				
				// EAI rtn 오류
			}else if(rtnFG.equals("0")){
				
				result.put("SUCCESS", false);
			}
		}else{
			throw new ApiException("오류입니다.");
		}
		
		apiResponse.putBody("result", result);
		return apiResponse;
	}
	
	/**
	 * 부원료항차관리 조회
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA144Process(ApiRequestDto requestDto) throws Exception {
		
		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		//검색조건(년월, 항차, 부원료)
		String yymm = CommonUtils.isToString(requestDto.reqBodyMap.get("yymm"));			// 년월
		String voycnt = CommonUtils.isToString(requestDto.reqBodyMap.get("voycnt"));		// 항차
		String submatCd = CommonUtils.isToString(requestDto.reqBodyMap.get("submatCd"));	// 부원료코드
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));			// 사원번호
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));		// MAC주소
		
		// validation check
		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}
		
		Element eleAction = new Element(Constants.CLIENT_REQUEST);
		eleAction.addContent(SoapUtil.getElementNew("YYMM", yymm));
		eleAction.addContent(SoapUtil.getElementNew("VOYCNT", voycnt));
		eleAction.addContent(SoapUtil.getElementNew("SUBMAT_CD", submatCd));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));
		
		// EAI 통신  
		Map<String, Object> resString = SoapUtil.sendSoapServerMapNew("dummyUrl",voycntListProcess, SoapUtil.getSoapXmlStringNew( eleAction,voycntListProcess ));
		
		// EAI 통신 결과 String 변환
		String apiResult = super.xmlMapToStringNew(resString);
		
		apiResult = apiResult.substring(1);
		String[] rows = apiResult.split("\n");
		
		for (int i = 0; i < rows.length; ++i) {
			if (!CommonUtils.isExist(rows[i])) {
					logger.debug("Skip empty row : " + i);
			}else{

				String[] row = rows[i].split("\\|\\|", -1);
				Map<String, Object> data = new HashMap<String, Object>();

    			int co = 0;
    			
    			data.put("YYMM"			, row[co++]);	// 년월
    			data.put("VOYCNT"		, row[co++]);	// 항차
    			data.put("SUBMAT_CD"	, row[co++]);	// 부원료코드
    			data.put("SUBMAT_NM"	, row[co++]);	// 부원료명
    			data.put("SHIP_NM"		, row[co++]);	// 선박명
    			data.put("PROPLC"		, row[co++]);	// 산지
    			data.put("QTY"			, row[co++]);	// B/L총량(Wet)
    			data.put("WHARK_NO"		, row[co++]);	// 선석
    			data.put("LC_NO1"		, row[co++]);	// LC(동해)
    			data.put("LC_NO2"		, row[co++]);	// LC(영월)
    			data.put("DONGH_QTY"	, row[co++]);	// 동해B/L(Wet)
    			data.put("YOUNGW_QTY"	, row[co++]);	// 영월B/L(Wet)
    			data.put("ARRV_DT"		, row[co++]);	// 하역일자
    			data.put("PRCH_NO1"		, row[co++]);	// 구매번호(동해)
    			data.put("PRCH_NO2"		, row[co++]);	// 구매번호(영월)
    			data.put("BL_MOIST"		, row[co++]);	// BL수분
    			data.put("SURV_MOIST0"	, row[co++]);	// 측정수분1
    			data.put("SURV_MOIST"	, row[co++]);	// 측정수분2
    			data.put("BL_QTY"		, row[co++]);	//

    			data.put("SURV_QTY"		, row[co++]);	// 
    			data.put("BL_QTY1"		, row[co++]);	// 
    			data.put("BL_QTY2"		, row[co++]);	// 
    			data.put("SURV_QTY1"	, row[co++]);	// 
    			data.put("SURV_QTY2"	, row[co++]);	// 
    			data.put("DESCR"		, row[co++]);	// 
    			data.put("STOCK_NO"		, row[co++]);	// 재고번호
				data.put("ITM_CD"		, row[co++]);	// 품목코드
				data.put("ITM_SERNO"	, row[co++]);	// 속보코드
				data.put("ITM_NM"		, row[co++]);	// 품목명
				
				
				
				
				
				data.put("SCAWGT"		, row[co++]);	// 총 계중(Wet)
				data.put("SCAWGT1"		, row[co++]);	// 동해계중(Wet)
				data.put("SCAWGT2"		, row[co++]);	// 영월계중(Wet)
				
				data.put("SCAWGT_DRY"	, row[co++]);	// 총 계중(Dry)
				data.put("SCAWGT1_DRY"	, row[co++]);	// 동해계중(Dry) 
				data.put("SCAWGT2_DRY"	, row[co++]);	// 영월계중(Dry)
				data.put("SCA_CLO_YN1"	, row[co++]);	// 항차종결(동해)
				data.put("SCA_CLO_YN2"	, row[co++]);	// 항차종결(영월)
				
				data.put("QTY1_SCAWGT1"	, row[co++]);	// 동해B/L-계중(Wet)
				data.put("QTY2_SCAWGT2"	, row[co++]);	// 영월/L-계중(Wet) 
				data.put("QTY_SCAWGT"	, row[co++]);	// 총/L-계중(Wet) 영월/L-계중(Wet) 
				
				//data.put("EMPNO"		, row[co++]);	// 
				
    			list.add(data);
			}
		}

		apiResponse.putBody("result", list);
		return apiResponse;
				
			
	}

	
	
	/**
	 * 부원료항차관리 수정 처리
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA145Process(ApiRequestDto requestDto) throws Exception {
		
		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		Map<String, Object> result = new HashMap<String, Object>();

		String yymm = CommonUtils.isToString(requestDto.reqBodyMap.get("yymm"));		// 기준 일자
		String stockNo = CommonUtils.isToString(requestDto.reqBodyMap.get("stockNo"));		// 기준 일자
		String arrvDt = CommonUtils.isToString(requestDto.reqBodyMap.get("arrvDt"));		// 기준 일자
		String voycnt = CommonUtils.isToString(requestDto.reqBodyMap.get("voycnt"));		// 기준 일자
		String submatCd = CommonUtils.isToString(requestDto.reqBodyMap.get("submatCd"));		// 기준 일자
		String donghQty = CommonUtils.isToString(requestDto.reqBodyMap.get("donghQty"));		// 기준 일자
		String youngwQty = CommonUtils.isToString(requestDto.reqBodyMap.get("youngwQty"));		// 기준 일자
		String qty = CommonUtils.isToString(requestDto.reqBodyMap.get("qty"));		// 기준 일자
		
		String proplc = CommonUtils.isToString(requestDto.reqBodyMap.get("proplc"));		// 기준 일자
		String itmCd = CommonUtils.isToString(requestDto.reqBodyMap.get("itmCd"));			// 기준 일자
		String itmSerno = CommonUtils.isToString(requestDto.reqBodyMap.get("itmSerno"));			// 기준 일자
		String wharkNo = CommonUtils.isToString(requestDto.reqBodyMap.get("wharkNo"));	// 기준 일자
		String blMoist = CommonUtils.isToString(requestDto.reqBodyMap.get("blMoist"));		// 기준 일자
		String survMoist0 = CommonUtils.isToString(requestDto.reqBodyMap.get("survMoist0"));		// 기준 일자
		String survMoist = CommonUtils.isToString(requestDto.reqBodyMap.get("survMoist"));	// 기준 일자
		String lcNo1 = CommonUtils.isToString(requestDto.reqBodyMap.get("lcNo1"));		// 기준 일자
		String lcNo2 = CommonUtils.isToString(requestDto.reqBodyMap.get("lcNo2"));				// 기준 일자
		String prchNo1 = CommonUtils.isToString(requestDto.reqBodyMap.get("prchNo1"));		// 기준 일자
		
		String prchNo2 = CommonUtils.isToString(requestDto.reqBodyMap.get("prchNo2"));		// 기준 일자
		String scaCloYn1 = CommonUtils.isToString(requestDto.reqBodyMap.get("scaCloYn1"));		// 기준 일자
		String scaCloYn2 = CommonUtils.isToString(requestDto.reqBodyMap.get("scaCloYn2"));		// 기준 일자
		String scawgt1 = CommonUtils.isToString(requestDto.reqBodyMap.get("scawgt1"));		// 기준 일자
		String scawgt2 = CommonUtils.isToString(requestDto.reqBodyMap.get("scawgt2"));		// 기준 일자
		String scawgt = CommonUtils.isToString(requestDto.reqBodyMap.get("scawgt"));		// 기준 일자
		String scawgt1Dry = CommonUtils.isToString(requestDto.reqBodyMap.get("scawgt1Dry"));		// 기준 일자
		String scawgt2Dry = CommonUtils.isToString(requestDto.reqBodyMap.get("scawgt2Dry"));		// 기준 일자
		String scawgtDry = CommonUtils.isToString(requestDto.reqBodyMap.get("scawgtDry"));		// 기준 일자
		
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));			// 사원번호
		//String scrId = CommonUtils.isToString(requestDto.reqBodyMap.get("scrId"));			// 화면ID
		//String wrkTyp = CommonUtils.isToString(requestDto.reqBodyMap.get("wrkTyp"));		// 작업유형
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));	// MAC주소
		String flag = CommonUtils.isToString(requestDto.reqBodyMap.get("flag"));					// 처리FLAGID
		
		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}
		
		Element eleAction = new Element(Constants.CLIENT_REQUEST, "http://www.openuri.org/");
		eleAction.addContent(SoapUtil.getElementNew("YYMM", yymm));
		eleAction.addContent(SoapUtil.getElementNew("STOCK_NO", stockNo));
		eleAction.addContent(SoapUtil.getElementNew("ARRV_DT", arrvDt));
		eleAction.addContent(SoapUtil.getElementNew("VOYCNT", voycnt));
		eleAction.addContent(SoapUtil.getElementNew("SUBMAT_CD", submatCd));
		eleAction.addContent(SoapUtil.getElementNew("DONGH_QTY", donghQty));
		eleAction.addContent(SoapUtil.getElementNew("YOUNGW_QTY", youngwQty));
		eleAction.addContent(SoapUtil.getElementNew("QTY", qty));
		
		eleAction.addContent(SoapUtil.getElementNew("PROPLC", proplc));
		eleAction.addContent(SoapUtil.getElementNew("ITM_CD", itmCd));
		eleAction.addContent(SoapUtil.getElementNew("ITM_SERNO", itmSerno));
		eleAction.addContent(SoapUtil.getElementNew("WHARK_NO", wharkNo));
		eleAction.addContent(SoapUtil.getElementNew("BL_MOIST", blMoist));
		eleAction.addContent(SoapUtil.getElementNew("SURV_MOIST0", survMoist0));
		eleAction.addContent(SoapUtil.getElementNew("SURV_MOIST", survMoist));
		eleAction.addContent(SoapUtil.getElementNew("LC_NO1", lcNo1));
		eleAction.addContent(SoapUtil.getElementNew("LC_NO2", lcNo2));
		eleAction.addContent(SoapUtil.getElementNew("PRCH_NO1", prchNo1));
		eleAction.addContent(SoapUtil.getElementNew("PRCH_NO2", prchNo2));
		eleAction.addContent(SoapUtil.getElementNew("SCA_CLO_YN1", scaCloYn1));
		eleAction.addContent(SoapUtil.getElementNew("SCA_CLO_YN2", scaCloYn2));
		eleAction.addContent(SoapUtil.getElementNew("SCAWGT1", scawgt1));
		eleAction.addContent(SoapUtil.getElementNew("SCAWGT2", scawgt2));
		eleAction.addContent(SoapUtil.getElementNew("SCAWGT", scawgt));
		eleAction.addContent(SoapUtil.getElementNew("SCAWGT1_DRY", scawgt1Dry));
		eleAction.addContent(SoapUtil.getElementNew("SCAWGT2_DRY", scawgt2Dry));
		eleAction.addContent(SoapUtil.getElementNew("SCAWGT_DRY", scawgtDry));
		
		eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		//eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		//eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));
		
		// EAI 통신
		Map<String, Object> resString = SoapUtil.sendSoapServerMap(voycntUpdateProcess, SoapUtil.getSoapXmlString( eleAction ));
		
		String apiResult = super.xmlMapToStringNew(resString);
		
		String rtnFG = apiResult.substring(0,1);
		apiResult    = apiResult.substring(1);
		String[] row = apiResult.split("\\|\\|", -1);
		
		if ( row != null ) {
			
			// rtn이 1이고 정상 MSG rtn시
			if (rtnFG.equals("1") && row.length == 2 && row[0].length() == 17 )	{
				
				result.put("SUCCESS", true);
				
				// 삭제
			} else if(rtnFG.equals("1") && row.length == 2 && row[0].length() != 17 )	{
				
				result.put("SUCCESS", true);
				
				// EAI rtn 오류
			}else if(rtnFG.equals("0")){
				
				result.put("SUCCESS", false);
			}
		}else{
			throw new ApiException("오류입니다.");
		}
		
		apiResponse.putBody("result", result);
		return apiResponse;
	}
	

	 /**
	 * 속보현황 조회
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResponseDto getJMA146Process(ApiRequestDto requestDto) throws Exception {
		
		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		//검색조건(년월, 항차, 부원료)
		String exprsFgCd = CommonUtils.isToString(requestDto.reqBodyMap.get("exprsFgCd"));	// 속보구분코드
		String factCd = CommonUtils.isToString(requestDto.reqBodyMap.get("factCd"));		// 공장
		String exprsDt = CommonUtils.isToString(requestDto.reqBodyMap.get("exprsDt"));			// 발생일
		String sitmCd = CommonUtils.isToString(requestDto.reqBodyMap.get("sitmCd"));			// 발생일
		String dataGubn = CommonUtils.isToString(requestDto.reqBodyMap.get("dataGubn"));			// 발생일
		String empNo = CommonUtils.isToString(requestDto.reqBodyMap.get("empNo"));			// 사원번호
		String comptAddr = CommonUtils.isToString(requestDto.reqBodyMap.get("comptAddr"));		// MAC주소
		
		// validation check
		if (!CommonUtils.isExist(empNo)) {
			throw new ValidationException(Constants.EMPNO);
		}
		
		Element eleAction = new Element(Constants.CLIENT_REQUEST, "http://www.openuri.org/");
		eleAction.addContent(SoapUtil.getElementNew("EXPRS_FG_CD", exprsFgCd));
		eleAction.addContent(SoapUtil.getElementNew("FACT_CD", factCd));
		eleAction.addContent(SoapUtil.getElementNew("EXPRS_DT", exprsDt));
		eleAction.addContent(SoapUtil.getElementNew("SITM_CD", sitmCd));
		eleAction.addContent(SoapUtil.getElementNew("DATA_GUBN", dataGubn));
		eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));
		
		// EAI 통신  
		Map<String, Object> resString = SoapUtil.sendSoapServerMap(exprsMatListProcess, SoapUtil.getSoapXmlString( eleAction ));
		
		// EAI 통신 결과 String 변환
		String apiResult = super.xmlMapToStringNew(resString);
		
		apiResult = apiResult.substring(1);
		String[] rows = apiResult.split("\n");
		
		for (int i = 0; i < rows.length; ++i) {
			if (!CommonUtils.isExist(rows[i])) {
					logger.debug("Skip empty row : " + i);
			}else{

				String[] row = rows[i].split("\\|\\|", -1);
				Map<String, Object> data = new HashMap<String, Object>();

    			int co = 0;

    			data.put("FACT_CD", row[co++]);
    			data.put("EXPRS_FG_CD", row[co++]);
    			data.put("ITM_CD", row[co++]);
    			data.put("EXPRS_SERNO", row[co++]);
    			data.put("EXPRS_DT", row[co++]);
    			data.put("ITMNM", row[co++]);
    			data.put("STOCK_QTY", row[co++]);
    			data.put("ARRV_QTY", row[co++]);
    			data.put("DLIVY_QTY", row[co++]);
    			data.put("CAR_CNT", row[co++]);
    			data.put("TRAIN_CNT", row[co++]);
    			data.put("MON_RECEIPT_QUANT", row[co++]);
    			data.put("MON_DEL_QUANT", row[co++]);
    			data.put("P_STOCK_QTY", row[co++]);
    			data.put("L_FLAG", row[co++]);
    			data.put("MOIST_INCLS_ARRV_QTY", row[co++]);
    			data.put("MOIST_RATE", row[co++]);
    			data.put("MON_WET_BASE_QUANT", row[co++]);
    			data.put("STOCK_NO", row[co++]);
    			data.put("SUBMAT_CD", row[co++]);
    			data.put("YYMM", row[co++]);
    			data.put("VOYCNT", row[co++]);
    			data.put("VOYCNT_SCAWGT", row[co++]);
    			data.put("MM_CLO_YN", row[co++]);
    			data.put("SCA_CLO_YN", row[co++]);
    			data.put("PRCH_NO", row[co++]);
    			data.put("LC_NO", row[co++]);
    			data.put("SURV_MOIST", row[co++]);
    			data.put("PRODT_ITM_CD", row[co++]);
    			data.put("PRODT_ITM_SERNO", row[co++]);
    			data.put("DRY_ORD_UTPRI", row[co++]);
    			data.put("WHOU_AMT", row[co++]);
    			data.put("MM_CLO_QTY", row[co++]);
    			data.put("DESCR", row[co++]);
    			data.put("MOIST_INCLS_ARRV_QTY2", row[co++]);
    			data.put("CHECK_CK", row[co++]);
    			data.put("SUBMAT_CD_2", row[co++]);
    			data.put("SAL_QTY", row[co++]);
    			data.put("MON_SAL_QTY", row[co++]);
    			data.put("UZ_YN", row[co++]);
    			data.put("COMPND_TRSP_QTY", row[co++]);
    			data.put("STATES", row[co++]);
    		    
    			list.add(data);
			}
		}

		apiResponse.putBody("result", list);
		return apiResponse;
				
			
	}

	public void getJMA029Processs(String empNo, String scrId, String wrkTyp, String comptAddr) throws Exception {
		Element eleAction = new Element("clientRequestwithReturn");
	    eleAction.addContent(SoapUtil.getElementNew("EMPNO", empNo));
		      eleAction.addContent(SoapUtil.getElementNew("SCR_ID", scrId));
		      eleAction.addContent(SoapUtil.getElementNew("WRK_TYP", wrkTyp));
		      eleAction.addContent(SoapUtil.getElementNew("COMPT_ADDR", comptAddr));
		      Map<String, Object> resString = SoapUtil.sendSoapServerMapNew("dummyUrl", this.historyInsertProcess, SoapUtil.getSoapXmlStringNew(eleAction, this.historyInsertProcess));
	}

}



