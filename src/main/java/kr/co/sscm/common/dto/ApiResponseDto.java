package kr.co.sscm.common.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.msp.constant.Const;

/**
 * @FileName ApiResponseDto.java
 * @comment Morpheus 기본 reponse 구조 DTO
 * @author AJH
 */
public class ApiResponseDto {

	Map<String, Object> head = new HashMap<String, Object>();
	Map<String, Object> body = new HashMap<String, Object>();
	private String encrypt;

	public ApiResponseDto() {
		this.head.clear();
		this.body.clear();
	}
	public ApiResponseDto(ApiRequestDto dto) {
		this.setHead(dto.reqHeadMap);
		this.setResult(Const.OK, Const.SUCCESS);
	}

	public void setResultDefaultOk(){
		this.putBody("resultCode", 	Const.OK);
		this.putBody("resultMsg", 	Const.SUCCESS);
	}

	public void setResultDefaultOk(Object resultCode, Object resultMsg){
		if(resultCode == null){
			this.putBody("resultCode", 	Const.OK);
			this.putBody("resultMsg", 	Const.SUCCESS);
		}else{
			this.putBody("resultCode", 	resultCode);
			this.putBody("resultMsg", 	resultMsg);
		}
	}

	public void setResult(String resultCode, String resultMsg) {
		this.putHead(Const.RESULT_CODE, resultCode);
		this.putHead(Const.RESULT_MESSAGE, resultMsg);
		setResultBody(resultCode,resultMsg);
	}

	public void setResultBody(String resultCode, String resultMsg){
		this.putBody("resultCode", resultCode);
		this.putBody("resultMsg", resultMsg);
	}
	public Map<String, Object> getHead() {
		return head;
	}
	public Map<String, Object> getBody() {
		return body;
	}
	public void setHead(Map<String,Object> head) {
		this.head = head;
	}
	public void putHead(String key, Object value) {
		this.head.put(key, value);
	}
	public void Head(String key, Object value) {
		this.head.put(key, value);
	}
	public void setBody(Map<String, Object> body) {
		this.body = body;
	}
	public void putBody(String key, Object value) {
		this.body.put(key, value);
	}
	public void putAllBoady(Map<String, Object> body) {
		if(body != null)
			this.body.putAll(body);
	}
	public void putBodyResultList(String key, List<?> objs) {
		//Map<String, List<?>> resultList = new HashMap<String, List<?>>();
		Map<String, Object> resultList = null;
		resultList = (Map<String, Object>)this.body.get("resultList");
		if(resultList == null) {
			resultList = new HashMap<String, Object>();
		}
		resultList.put(key, objs);
		this.body.put("resultList", resultList);
	}
	// result 항목하위에 결과 등록
	public void putBodyResult(Map<String, Object> resultMap) {
		if(resultMap != null)
			putBody("result", resultMap);
	}
	// result 항목하위에 결과 등록
	public void putBodyResultOne(String key, Object value ) {

		if(key != null) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put(key, value);
			putBody("result", resultMap);

		}
	}
	public String getEncrypt() {
		return encrypt;
	}
	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}


}
