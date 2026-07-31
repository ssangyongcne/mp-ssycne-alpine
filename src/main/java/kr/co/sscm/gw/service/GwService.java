package kr.co.sscm.gw.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import kr.co.sscm.common.base.BaseService;
import kr.co.sscm.common.dto.ApiRequestDto;
import kr.co.sscm.common.dto.ApiResponseDto;
import kr.co.sscm.common.util.CommonUtils;
import kr.co.sscm.common.util.ParamValidator;
import kr.co.sscm.gw.dao.GwDao;

/**
 * @FileName FwrdService.java
 * @comment DB 사용 service
 * @author AJH
 */
@Service
public class GwService extends BaseService{

	@Autowired
	GwDao gwDao;

	/**
	 * 설비고장 신고 목록 조회
	 * @param requestDto
	 * @return
	 */
	public List<Map<String, Object>> getFaultReportsList(ApiRequestDto requestDto) {

		List<Map<String, Object>> result = gwDao.getFaultReportsList();

		return result;
	}

	/**
	 * 설비고장 신고 상세 조회
	 * @param requestDto
	 * @return
	 * @throws Exception 
	 */
	public ApiResponseDto getFaultReportsDetail(ApiRequestDto requestDto) throws Exception {
		
		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		
		Map<String, Object> requestMap = requestDto.reqBodyMap;
		
		// validation check
		ParamValidator.requireParam(requestMap, "factCd", "공장코드"); 

		// 설비고장 신고 상세 조회
		Map<String, Object> result = gwDao.getFaultReportsDetail(requestMap);

		// 설비고장 신고 설비그룹별 상태 조회
		List<Map<String, Object>> equipGrpList = gwDao.getEquipGrpList(requestMap);
		
		apiResponse.putAllBoady(result);
		apiResponse.putBody("equipGrpList", equipGrpList);
		
		return apiResponse;
	}

	/**
	 * 설비고장 신고 수정
	 * @param requestDto
	 * @return
	 * @throws Exception 
	 */
	public ApiResponseDto saveFaultReports(ApiRequestDto requestDto) throws Exception {
		
		ApiResponseDto apiResponse = new ApiResponseDto(requestDto);
		
		Map<String, Object> requestMap = requestDto.reqBodyMap;

		// validation check
		ParamValidator.requireParam(requestMap, "factCd", "공장코드"); 
		ParamValidator.requireIn(requestMap, "overallAvailCd", "출하가능여부", "O", "X");
		ParamValidator.requireIn(requestMap, "progressCd", "진행단계", "NONE", "CAUSE", "REPAIR");
		ParamValidator.requireIn(requestMap, "breakHourYn", "고장발생시간 여부", "Y", "N");
		ParamValidator.requireIntInRange(requestMap, "breakHour", "고장발생시간", 0, 23);
		ParamValidator.requireIn(requestMap, "expectHourYn", "출하가능시간 여부", "Y", "N");
		ParamValidator.requireIntInRange(requestMap, "expectHour", "출하가능시간", 0, 23);
		ParamValidator.requireIn(requestMap, "expectUnknownYn", "출하가능시간 미정 여부", "Y", "N");
		
		List<Map<String, Object>> equipGrpList = (List<Map<String, Object>>) requestMap.get("equipGrpList");

		String factCd = CommonUtils.isToString(requestMap.get("factCd"));		// 공장코드
		int factCnt = gwDao.checkFactCd(factCd);
		
		if( factCnt < 1 ) {
			throw new Exception("등록되지 않은 공장코드입니다.");
		}
		
		int result = gwDao.saveFaultReports(requestMap);
		
		if( equipGrpList.size() > 0 ) {
			int cnt = gwDao.saveEquipGrpList(requestMap);
		}
		
		return apiResponse;
	}

	/** Load an image file from disk. */
	public byte[] loadImage(String imagePath) throws IOException {
		return loadImage(new File(imagePath));
	}

	/** Load an image file from disk. */
	public byte[] loadImage(File imageFile) throws IOException {
		logger.info("loadImage() image file load...");
		if (!imageFile.exists()) {
			throw new FileNotFoundException("File not found: " + imageFile.getAbsolutePath());
		}
		return Files.readAllBytes(imageFile.toPath());
	}

	/** Detect MIME type from image bytes. */
	public MediaType detectContentType(byte[] bytes) throws IOException {
		return detectContentType(null, bytes);
	}

	/** Detect MIME type from image file path or bytes. */
	public MediaType detectContentType(File imageFile, byte[] bytes) throws IOException {
		if (imageFile != null) {
			String mime = Files.probeContentType(imageFile.toPath());
			if (mime != null) {
				return MediaType.parseMediaType(mime);
			}
		}

		InputStream is = new ByteArrayInputStream(bytes);
		try {
			String mime = URLConnection.guessContentTypeFromStream(is);
			if (mime == null) {
				return MediaType.APPLICATION_OCTET_STREAM;
			}
			return MediaType.parseMediaType(mime);
		} finally {
			is.close();
		}
	}
}
