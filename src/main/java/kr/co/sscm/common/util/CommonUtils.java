package kr.co.sscm.common.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CommonUtils {

	private static Logger logger = LoggerFactory.getLogger(CommonUtils.class);

	public static boolean isExist(Object object){
		boolean result= false;
		if(object != null && !"".equals(object)) result=true;
		return result;
	}

	public static boolean isExist(List<Object> objectList){
		boolean result= false;
		if(objectList != null && objectList.size()>0) result=true;
		return result;
	}
	//문자화
    public static String isToString(Object object){
    	String result= "";
    	if(object != null){
    		if(isCastNum(object)) result = object+"";
    		if("null".equals(object)) result="";
    		result = object+"";
    	}
    	return result;
    }
    //숫자화
    public static Integer isToInt(Object object){
    	int result= 0;
    	if(object != null){
    		String str = object+"";
    		if(!"".equals(str)){
    			if(isCastNum(object)) result = Integer.parseInt(str);
    			if("null".equals(object)) result=0;
    			if("".equals(object)) result=0;
    		}
    	}
    	return result;
    }
    //float
    public static Float isToFloat(Object object){
    	float result= 0.0f;
    	String str = object+"";
    	if(object != null && !"".equals(str)){
    		if(isCastNum(object)) result = Float.parseFloat(str);
    		if("null".equals(object)) result=0;
    		if("".equals(object)) result=0;
    	}
    	return result;
    }

    //double
    public static double isToDouble(Object object){
    	double result= 0.0;
    	String str = object+"";
    	if(object != null && !"".equals(str)){
    		if(isCastNum(object)) result = Double.parseDouble(str);
    		if("null".equals(object)) result=0.0;
    		if("".equals(object)) result=0.0;
    	}
    	return result;
    }

    //문자열이 숫자(정수, 실수)인지 아닌지 판별한다.
    public static boolean isCastNum(Object object) {
      char tempCh;
      int dotCount = 0;	//실수일 경우 .의 개수를 체크하는 변수
      boolean result = true;
      String str = object+"";

      for (int i=0; i<str.length(); i++){
        tempCh= str.charAt(i);	//입력받은 문자열을 문자단위로 검사
        //아스키 코드 값이 48 ~ 57사이면 0과 9사이의 문자이다.
        if ((int)tempCh < 48 || (int)tempCh > 57){
          //만약 0~9사이의 문자가 아닌 tempCh가 .도 아니거나
          //.의 개수가 이미 1개 이상이라면 그 문자열은 숫자가 아니다.
          if(tempCh!='.' || dotCount > 0){
            result = false;
            break;
          }else{
            //.일 경우 .개수 증가
            dotCount++;
          }
        }
      }
      return result;
    }

    public static String cleanXSS(String value) {
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        return value;
    }

    public static String getClientIP(HttpServletRequest request) {

		String ip		= request.getHeader("HTTP_X_FORWARDED_FOR");

	    if ( StringUtils.isNotEmpty(ip) && ip.indexOf(",") != -1 ) {
	    	int idx			= ip.indexOf(",");
	    	String ipTemp	= ip.substring(0, idx);
	    	ip				= ipTemp;
	    }

		if (ip == null) {
			ip = request.getHeader("X-Forwarded-For");
		}

	    if (ip == null) {
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if (ip == null) {
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if (ip == null) {
	        ip = request.getHeader("HTTP_CLIENT_IP");
	    }
	    if (ip == null) {
	        ip = request.getRemoteAddr();
	    }

	    if ( ip.indexOf(",") != -1 ) {
	    	int idx			= ip.indexOf(",");
	    	String ipTemp	= ip.substring(0, idx);
	    	ip				= ipTemp;
	    }

        return ip;
	}

	public static String getUserOS(HttpServletRequest request) {

		String strUserOs = "";
		String struserAgent = request.getHeader("User-Agent");

		if (struserAgent.indexOf("Window") > -1){
			strUserOs = "WINDOWS";
		}else if (struserAgent.indexOf("iPhone") > -1){
			strUserOs = "iPhone";
		}else if (struserAgent.indexOf("iPad") > -1){
			strUserOs = "iPad";
		}else if (struserAgent.indexOf("Android") > -1){
			strUserOs = "Android";
		}

		return strUserOs;
	}
}
