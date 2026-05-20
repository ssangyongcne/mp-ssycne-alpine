package kr.co.sscm.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Component;


/**
 * @FileName SoapUtil.java
 * @comment SOAP util
 * @author AJH
 */
@Component
public class SoapUtil {

	private static Logger logger = LoggerFactory.getLogger(SoapUtil.class);

	public static String baseUrl;

	@Value("${eai.endpoint.base}")
	public void setBaseUrl(String url) {
		baseUrl = url;
	}

	/**
	 * EAI 통신을 위한 soap
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String sendSoapServer(String inftUrl, String xml) throws Exception{

		logger.info("############# REQUEST #################");
		logger.info("##### request url : " +  inftUrl );
		logger.info("##### request xml : " +  xml );

		OutputStreamWriter  wr = null;
		BufferedReader in = null;
		URL url = new URL(baseUrl + inftUrl);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.addRequestProperty("Content-Type",  "text/xml; charset=utf-8");
		wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(xml);
		wr.flush();
		int code = conn.getResponseCode();

		String inputLine = null;
		StringBuffer buffer = new StringBuffer();

		logger.info("##### response code : " +  code );
		logger.info("##### response msg : " +  conn.getResponseMessage() );

		if(code != HttpStatus.OK.value()) {

			in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			while((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
				buffer.append("\n");
			}
			logger.info("##### response buffer : " +  buffer );

		}else {

			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
				buffer.append("\n");
			}
		}

		return buffer.toString();
    }


	/**
	 * EAI 통신을 위한 XML 생성
	 * @param parmas
	 * @return
	 * @throws Exception
	 */
	public static String getSoapXmlString( Element parmas ) throws Exception {

		Document doc = new Document();
		Namespace nsxis = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		Namespace nsxsd = Namespace.getNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
		Namespace nssoap = Namespace.getNamespace("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
		Element root = new Element("Envelope", nssoap);
		root.addNamespaceDeclaration(nsxis);
		root.addNamespaceDeclaration(nsxsd);
		root.addNamespaceDeclaration(nssoap);

		Element body = new Element("Body", nssoap);
		body.addContent(parmas);

		root.addContent(body);
		doc.setRootElement(root);

		XMLOutputter serializer = new XMLOutputter();
		serializer.setEncoding("UTF-8");
		String xmlString = serializer.outputString(doc);
		logger.debug("xmlString:"+xmlString);
		return xmlString;
    }
	
	/**
	 * EAI 통신을 위한 XML 생성
	 * @param parmas
	 * @return
	 * @throws Exception
	 */
	public static String getSoapXmlStringTest( Element parmas ) throws Exception {

	    Namespace nsSoap = Namespace.getNamespace("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
	    Namespace nsSsy = Namespace.getNamespace("ssy", "http://erpdev01/SSYCNE_JMA_01.JMA_016.ws:JMA_016_P");

	    Element envelope = new Element("Envelope", nsSoap);
	    envelope.addNamespaceDeclaration(nsSsy);

	    Element header = new Element("Header", nsSoap);
	    Element body = new Element("Body", nsSoap);

	    Element ssyRequest = new Element("Request", nsSsy);
	    ssyRequest.addContent(parmas);

	    body.addContent(ssyRequest);
	    envelope.addContent(header);
	    envelope.addContent(body);

	    Document doc = new Document(envelope);
	    XMLOutputter serializer = new XMLOutputter();
	    serializer.setEncoding("UTF-8");
	    return serializer.outputString(doc);
    }
	
	/**
	 * EAI 통신을 위한 XML 생성
	 * @param parmas
	 * @return
	 * @throws Exception
	 */
	public static String getSoapXmlStringNew( Element parmas, String url ) throws Exception {

	    Namespace nsSoap = Namespace.getNamespace("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
	    Namespace nsSsy = Namespace.getNamespace("ssy", "http://erpdev01/"+url);

	    Element envelope = new Element("Envelope", nsSoap);
	    envelope.addNamespaceDeclaration(nsSsy);

	    Element header = new Element("Header", nsSoap);
	    Element body = new Element("Body", nsSoap);

	    Element ssyRequest = new Element("Request", nsSsy);
	    ssyRequest.addContent(parmas);

	    body.addContent(ssyRequest);
	    envelope.addContent(header);
	    envelope.addContent(body);

	    Document doc = new Document(envelope);
	    XMLOutputter serializer = new XMLOutputter();
	    serializer.setEncoding("UTF-8");
	    return serializer.outputString(doc);
    }

	public static Element getElement(String key, Object value) {
		Element param = new Element(key, "http://www.openuri.org/");
		param.setText(String.valueOf(value));
		return param;
	}
	
	public static Element getElementNew(String key, Object value) {
		Element param = new Element(key);
		param.setText(String.valueOf(value));
		return param;
	}

	/**
	 * EAI 통신 후 response 결과 Map 으로 전달
	 * @param inftUrl
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> sendSoapServerMap(String inftUrl, String xml) throws Exception{

		Map<String, Object> test = new HashMap<String, Object>();

		logger.info("############# REQUEST #################");
		logger.info("##### request url : " +  baseUrl + inftUrl );
		logger.info("##### request xml : " +  xml );

		OutputStreamWriter  wr = null;
		BufferedReader in = null;
		URL url = new URL(baseUrl + inftUrl);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.addRequestProperty("Content-Type",  "text/xml; charset=utf-8");
		wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(xml);
		wr.flush();
		int code = conn.getResponseCode();

		String inputLine = null;
		StringBuffer buffer = new StringBuffer();

		logger.info("##### response code : " +  code );
		logger.info("##### response msg : " +  conn.getResponseMessage() );

		if(code != HttpStatus.OK.value()) {

			in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			while((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
				buffer.append("\n");
			}
		}else {

			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
				buffer.append("\n");
			}
		}

		test.put("code", code);
		test.put("buffer", buffer.toString());

		return test;
    }
	
	
	
	/**
	 * EAI 통신 후 response 결과 Map 으로 전달
	 * @param inftUrl
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> sendSoapServerMapTest(String inftUrl, String xml) throws Exception{

		Map<String, Object> test = new HashMap<String, Object>();

		logger.info("############# REQUEST #################");
		logger.info("##### request url : " +  "http://150.2.170.51:7200/ws/SSYCNE_JMA_01.JMA_016.ws:JMA_016_P" );
		logger.info("##### request xml : " +  xml );

		OutputStreamWriter  wr = null;
		BufferedReader in = null;
		URL url = new URL("http://150.2.170.51:7200/ws/SSYCNE_JMA_01.JMA_016.ws:JMA_016_P");
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.addRequestProperty("Content-Type",  "text/xml; charset=utf-8");
		wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(xml);
		wr.flush();
		int code = conn.getResponseCode();

		String inputLine = null;
		StringBuffer buffer = new StringBuffer();

		logger.info("##### response code : " +  code );
		logger.info("##### response msg : " +  conn.getResponseMessage() );

		if(code != HttpStatus.OK.value()) {

			in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			while((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
				buffer.append("\n");
			}
		}else {

			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
				buffer.append("\n");
			}
		}

		test.put("code", code);
		test.put("buffer", buffer.toString());

		return test;
    }
	
	/**
	 * EAI 통신 후 response 결과 Map 으로 전달
	 * @param inftUrl
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> sendSoapServerMapNew(String base, String inftUrl, String xml) throws Exception{

		
		//추후 base값 custom.xml에서 가져올것
		base = "http://150.2.170.51:7200/ws/";
		
		
		Map<String, Object> test = new HashMap<String, Object>();

		logger.info("############# REQUEST #################");
		logger.info("##### request url : " +  base+inftUrl );
		logger.info("##### request xml : " +  xml );

		OutputStreamWriter  wr = null;
		BufferedReader in = null;
		URL url = new URL(base+inftUrl);
		//URL url = new URL("http://150.2.170.51:7200/ws/SSYCNE_JMA_01.JMA_016.ws:JMA_016_P");
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.addRequestProperty("Content-Type",  "text/xml; charset=utf-8");
		wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(xml);
		wr.flush();
		int code = conn.getResponseCode();

		String inputLine = null;
		StringBuffer buffer = new StringBuffer();

		logger.info("##### response code : " +  code );
		logger.info("##### response msg : " +  conn.getResponseMessage() );

		if(code != HttpStatus.OK.value()) {

			in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			while((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
				buffer.append("\n");
			}
		}else {

			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
				buffer.append("\n");
			}
		}

		test.put("code", code);
		test.put("buffer", buffer.toString());

		return test;
    }

}
