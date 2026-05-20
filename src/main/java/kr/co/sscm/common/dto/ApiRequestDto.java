package kr.co.sscm.common.dto;

import java.util.Map;

/**
 * @FileName ApiRequestDto.java
 * @comment Morpheus 기본 request 구조 DTO
 * @author AJH
 */
public class ApiRequestDto {

	public Map<String,Object> reqMap =  null;
    //클라이언트에서 넘어온 공통 헤더 맵정보
	public Map<String,Object> reqHeadMap = null;
    //클라이언트에서 넘긴 파라미터 맵정보
	public Map<String,Object> reqBodyMap = null;

    public ApiRequestDto(
    		Map<String,Object> reqMap,
    		Map<String,Object> reqHeadMap,
    		Map<String,Object> reqBodyMap
    		) {

    	this.reqMap = reqMap;
    	this.reqHeadMap = reqHeadMap;
    	this.reqBodyMap = reqBodyMap;
    }
}
