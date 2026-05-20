package kr.co.sscm.push.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import kr.co.sscm.common.base.BaseService;
import kr.co.sscm.common.dto.MsgSendDto;
import kr.co.sscm.common.exception.PushException;
import kr.co.sscm.common.util.CommonUtils;
import kr.co.sscm.common.util.FileUploadUtils;
import kr.co.sscm.common.util.HttpUtils;
import kr.co.sscm.push.dao.PushDao;
import kr.co.sscm.push.dto.PushResponseDto;


@Service
public class PushService extends BaseService{

	@Value("${push.url}")
	private String SERVER_URL_PUSH;

	@Value("${gw.url}")
	private String GW_URL;

	@Value("${app.id}")
	private String APP_ID;

	@Value("${push.upload.dir}")
	private String PUSH_DIR;


	@Autowired
	HttpUtils httpUtils;

	@Autowired
	FileUploadUtils fileUpload;

	@Autowired
	PushDao pushDao;

	/**
	 * push 발송
	 * @param msgSendDto
	 * @return
	 * @throws Exception
	 */
	public PushResponseDto pushSend(MsgSendDto msgSendDto) throws Exception {

		String newExt = "";
		PushResponseDto resDto = new PushResponseDto();
		Map<String, Object> imgMap = new HashMap<String, Object>();
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<String, Object>();

		String msg = CommonUtils.isToString(msgSendDto.getMessage());	// 메시지 내용
		List<String> cuids = msgSendDto.getCuids();

		if( !CommonUtils.isExist(msg) ) {
			throw new PushException("메세지가 없습니다.");
		}

		if( !CommonUtils.isExist(cuids) ) {
			throw new PushException("수신자 아이디가 없습니다.");
		}

		String pushCalUrl = SERVER_URL_PUSH + "/rcv_register_message.ctl";

		// JSON 배열형태
		//JSONArray CUID = new JSONArray(msgSendDto.getCuids());

		param.add( "MESSAGE",		msgSendDto.getMessage());		// 메시지 내용
        param.add( "TYPE", 			"E" );						// 개별발송 : E, 시스템 개별대량발송일 경우 : S
        param.add( "SOUNDFILE", 	"alert.aif");				// 사운드파일명
        param.add( "BADGENO",		"1");						// 뱃지번호
        param.add( "PRIORITY",		"3");						// 우선순위
        param.add( "SENDERCODE", 	"admin" );					// APPID
        param.add( "SERVICECODE", 	"ALL" );					// 발송정책
        param.add( "DB_IN", 		"Y" );						// DB 테이블에 저장 ( Y : 저장, N : 저장하지 않음 )
    	param.add( "RESERVEDATE", 	"" );						// 예약발송일시
    	param.add( "APP_ID", 		APP_ID );					// 발송 대상 APPID
		param.add( "CUID",			msgSendDto.getCuids());		// 수신자 아이디

		// Image 파일 업로드 IMAGE_FILE
        if( msgSendDto.getImageFile() != null && msgSendDto.getImageFile().getSize() > 0){
        	imgMap = fileUpload.pushFileUpload(msgSendDto.getImageFile());

        	if( CommonUtils.isExist(msgSendDto.getVideoUrl()) ) {
        		newExt = messageFormat("", "2", msgSendDto.getVideoUrl(), CommonUtils.isToString(GW_URL + imgMap.get("imageUploadUrl")));
        	}else {
        		newExt = messageFormat("", "3", "", CommonUtils.isToString(GW_URL + imgMap.get("imageUploadUrl")));
        	}
        }else {
        	if( CommonUtils.isExist(msgSendDto.getVideoUrl()) ) {
        		newExt = messageFormat("", "2", msgSendDto.getVideoUrl(), "");
        	}
        }
    	param.add( "EXT", 			newExt);					// EXT

		Map<String, Object> pushResult = httpUtils.httpPost(pushCalUrl, param);				// 푸시 발송
		Map<String, Object> body = (Map<String, Object>) pushResult.get("BODY");
		Map<String, Object> header = (Map<String, Object>) pushResult.get("HEADER");

		resDto.setBODY(body);
		resDto.setHEADER(header);

		return resDto;
	}

	/**
	 * 사용자가 선택한 구분값에 따라 각 내용을 구분자 | 를 이용하여 조합한 문자열을 리턴
	 * @param ext : Notification 내용
	 * @param webEditUrl : WEB PAGE 에 작성된 내용이 저장된 HTML파일이 업로드되어있는 URL ( gw 통한 push 발송은 webEditUrl 없음 )
	 * @param category : 구분
	 * @param videoUrl : 동영상(URL)
	 * @param imageUrl : 업로드한 IMAGE 파일이 저장되어있는 URL
	 * @param inboxId : 수신함 아이디
	 * @return
	 * @noti
	 *      0 : 커스텀을 위한 경우
	 *      2 : img + 동영상을 함께 보내는 경우
	 *      3 : img 를 함께 보내는 경우
	 */
	public String messageFormat(String ext, String category, String videoUrl, String imageUrl){
		String result = "";
		switch(Integer.parseInt(category)){
		case 0:
			result = category+"|"+ext+"|";
			break;
		case 2:
			result = category+"|"+ext+"|"+imageUrl+"|"+videoUrl;
			break;
		case 3:
			result = category+"|"+ext+"|"+imageUrl;
			break;
		}
		return result;
	}

	/**
	 * PUSH 수신동의 여부 확인
	 * @param requestMap
	 * @return
	 * @throws Exception
	 */
	public int pushRegiInfo(Map<String, Object> requestMap) throws Exception {

		String psid = CommonUtils.isToString(requestMap.get("psid"));
		String cuid = CommonUtils.isToString(requestMap.get("cuid"));

		logger.info("############ {psid} : " + psid );
		logger.info("############ {cuid} : " + cuid );

		if( !CommonUtils.isExist(psid) ) {
			throw new PushException("PSID가 없습니다.");
		}

		if( !CommonUtils.isExist(cuid) ) {
			throw new PushException("cuid가 없습니다.");
		}

		logger.info("############ {requestMap} : " + requestMap );

		return pushDao.pushRegiInfo(requestMap);
	}
}
