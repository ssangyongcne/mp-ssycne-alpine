package kr.co.sscm.common.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @FileName HttpUtils.java
 * @comment HTTP util
 * @author AJH
 */
@Component
public class HttpUtils {

	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);


	/**
	 * 푸시발송용 httpPost
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> httpPost(String url, MultiValueMap<String, Object> params) throws Exception{

		Map<String, Object> responseMap =  new HashMap<String, Object>();

		// RestTemplate 기본 설정을 위한 Factory 생성
	    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
	    factory.setConnectTimeout(5000);
	    factory.setReadTimeout(10000);
	    RestTemplate restTemplate = new RestTemplate(factory);

	    try {

	    	ObjectMapper mapper = new ObjectMapper();
		    String json = mapper.writeValueAsString(params);

	        logger.info("####################################################################");
	        logger.info("############ {http post url} : " + url );
	        logger.info("############ {http post params} : " + json );

	        ResponseEntity<Map> resp = restTemplate.postForEntity( url, params, Map.class );
	        responseMap = resp.getBody();

	        logger.debug("############ {header} : {}", resp.getHeaders() );
	        logger.debug("############ {body} : {}", resp.getBody() );
	        logger.debug("############ {code} : {}", resp.getStatusCode() );
	        logger.info("############ {http post resultMap} : " + responseMap );
	        logger.info("####################################################################");

	    } catch (HttpStatusCodeException e) {
	        HttpStatus errorHttpStatus = HttpStatus.valueOf(e.getStatusCode().value());
	        String errorResponse = e.getResponseBodyAsString();
	        logger.error("############ {http post errorHttpStatus} : " + errorHttpStatus);
	        logger.error("############ {http post errorResponse} : " + errorResponse);
	        logger.info("####################################################################");
	        logger.debug("HTTP POST Exception", e);

	        responseMap.put("resultMsg", e.toString());
	        responseMap.put("resultCode", "9999");					// 레거시 에러
	    } catch (Exception e) {
	    	logger.error("############ {http post Exception} : " + e.toString());
	        logger.info("####################################################################");
	        logger.debug("HTTP POST Exception", e);

	        responseMap.put("resultMsg", e.getMessage());
	        responseMap.put("resultCode", "9998");					// 레거시 에러
	    }

	    return responseMap;
	}

}
