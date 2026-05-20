package kr.co.sscm.common.base;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.HttpStatus;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import kr.co.sscm.common.exception.EaiException;
import kr.co.sscm.common.util.CommonUtils;

/**
 * @FileName XmlBaseService.java
 * @comment XML parsing을 위한 service
 * @author AJH
 */
public class XmlBaseService extends BaseService{

	/**
	 * NOde 에서 TEXT 만 조회
	 *
	 * @param node
	 * @return
	 */
	protected Object getTextContents(Node node) {
		String contents = null;
		logger.info("getTextContents: nodeName : " + node.getNodeName());
		try {
			if(node.getFirstChild() instanceof Text) {
				Text txt = (Text)node.getFirstChild();
				if(txt != null) {
					contents = txt.getNodeValue();
				}
				return contents;
			}
			else {
				List<String> strList = new ArrayList<String>();
				NodeList childNodes = node.getChildNodes();
				for(int i = 0; i < childNodes.getLength(); i++) {
					strList.add((String)this.getTextContents( childNodes.item(i)));
				}
				return strList;
			}
		}
		catch(Exception e) {
			logger.error("getTextContents Exception", e);
		}
		return null;
	}

	/**
	 * Map으로 전달받은 xml을 String 으로 변환
	 * @param xmlMap
	 * @return
	 * @throws Exception
	 */
	protected String xmlMapToString(Map<String, Object> xmlMap) throws Exception {

		String result = "";
		int code = CommonUtils.isToInt(xmlMap.get("code"));
		String xmlString = CommonUtils.isToString(xmlMap.get("buffer"));

		JSONObject json = XML.toJSONObject(xmlString);
		logger.info("############# RESPONSE #################");

		Map<String, Object> jsonMap = json.toMap();
		Map<String, Object> envelopeMap = (Map<String, Object>)jsonMap.get("SOAP-ENV:Envelope");
		Map<String, Object> bodyMap = (Map<String, Object>)envelopeMap.get("SOAP-ENV:Body");

		if( code != HttpStatus.OK.value() ) {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new ByteArrayInputStream(xmlString.getBytes()));
			doc.getDocumentElement().normalize();
			NodeList resultNode = doc.getElementsByTagName("faultstring");
			if(resultNode.getLength() > 0 ) {
				result = (String)this.getTextContents(resultNode.item(0));
			}
			logger.info("##### EAI ERROR MSG : " +  xmlString );
			logger.info("########################################");

			throw new EaiException("EAI ERROR MSG : " + result);

		}else {
			Map<String, Object> returnResponseMap = (Map<String, Object>)bodyMap.get("ns:clientRequestwithReturnResponse");

			result = String.valueOf(returnResponseMap.get("ns:clientRequestwithReturnResult"));

			logger.info("##### response result : " +  xmlString );
			logger.info("########################################");

			if( result.contains("ORA-") ) {

				logger.info("########################################");
				logger.info("####### ORA ERROR MSG : " + returnResponseMap );
				logger.info("########################################");
				throw new EaiException(result);
			}
		}

		return result;

	}
	/**
	 * Map으로 전달받은 xml을 String 으로 변환
	 * @param xmlMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected String xmlMapToStringNew(Map<String, Object> xmlMap) throws Exception {

	    String result = "";
	    int code = CommonUtils.isToInt(xmlMap.get("code"));
	    String xmlString = CommonUtils.isToString(xmlMap.get("buffer"));

	    logger.info("############# RESPONSE #################");

	    JSONObject json = XML.toJSONObject(xmlString);
	    Map<String, Object> jsonMap = json.toMap();

	    Map<String, Object> envelopeMap = getMap(jsonMap, "SOAP-ENV:Envelope");
	    Map<String, Object> bodyMap = getMap(envelopeMap, "SOAP-ENV:Body");

	    if (code != HttpStatus.OK.value()) {
	        // faultstring 추출 (에러 응답 처리)
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8)));
	        doc.getDocumentElement().normalize();
	        NodeList resultNode = doc.getElementsByTagName("faultstring");

	        Object faultObj = (resultNode.getLength() > 0) ? getTextContents2(resultNode.item(0)) : null;
	        result = (faultObj != null) ? faultObj.toString().trim() : "알 수 없는 오류";

	        logger.error("##### EAI ERROR MSG : {}", result);
	        throw new EaiException("EAI ERROR MSG : " + result);
	    }

	    // 정상 응답 파싱
	    Map<String, Object> responseWrapper = findMapByKeyContains(bodyMap, "Response");
	    Map<String, Object> returnResponseMap = findMapByKeyContains(responseWrapper, "clientRequestwithReturnResponse");

	    if (returnResponseMap == null) {
	        logger.error("SOAP 응답 구조 오류: clientRequestwithReturnResponse 노드 없음\n원본: {}", xmlString);
	        throw new EaiException("검색 결과가 없습니다.");
	    }

	    Object resultObj = findValueByKeyContains(returnResponseMap, "clientRequestwithReturnResult");
	    if (resultObj == null) {
	        logger.error("SOAP 응답 구조 오류: clientRequestwithReturnResult 노드 없음\n원본: {}", xmlString);
	        throw new EaiException("검색 결과가 없습니다.");
	    }

	    result = resultObj.toString().trim();

	    logger.info("##### response result : {}", result);
	    logger.info("########################################");

	    if (result.contains("ORA-")) {
	        logger.error("####### ORA ERROR MSG : {}", result);
	        throw new EaiException(result);
	    }

	    return result;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> getMap(Map<String, Object> map, String exactKey) {
	    if (map == null || exactKey == null) return null;
	    Object value = map.get(exactKey);
	    return (value instanceof Map) ? (Map<String, Object>) value : null;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> findMapByKeyContains(Map<String, Object> map, String keyPart) {
	    if (map == null || keyPart == null) return null;
	    for (Map.Entry<String, Object> entry : map.entrySet()) {
	        if (entry.getKey().contains(keyPart) && entry.getValue() instanceof Map) {
	            return (Map<String, Object>) entry.getValue();
	        }
	    }
	    return null;
	}

	private Object findValueByKeyContains(Map<String, Object> map, String keyPart) {
	    if (map == null || keyPart == null) return null;
	    for (Map.Entry<String, Object> entry : map.entrySet()) {
	        if (entry.getKey().contains(keyPart)) {
	            return entry.getValue();
	        }
	    }
	    return null;
	}

	/**
	 * XML 노드로부터 텍스트 콘텐츠 추출 (DOM 방식 사용 시)
	 */
	protected Object getTextContents2(Node node) {
	    return (node != null) ? node.getTextContent() : null;
	}
}
