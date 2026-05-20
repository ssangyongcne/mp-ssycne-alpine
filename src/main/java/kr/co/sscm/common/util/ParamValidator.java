package kr.co.sscm.common.util;

import java.util.Map;

import kr.co.sscm.common.exception.ValidationException;

public class ParamValidator {
	
	public static String requireParam(Map<String, Object> map, String key, String displayName) throws Exception {
        String value = CommonUtils.isToString(map.get(key));
        if (!CommonUtils.isExist(value)) {
            throw new ValidationException(displayName);
        }
        return value;
    }

	public static String requireIn(Map<String, Object> map, String key, String displayName, String... allowed) throws Exception {
	    String value = CommonUtils.isToString(map.get(key));
	    if (!CommonUtils.isExist(value)) {
	        throw new ValidationException(displayName);
	    }

	    for (String a : allowed) {
	        if (a.equals(value)) {
	            return value;
	        }
	    }
	    throw new Exception(displayName + " 값이 유효하지 않습니다");
	}


	public static Integer requireIntInRange(Map<String, Object> map, String key, String displayName, int min, int max) throws Exception {
	    String value = CommonUtils.isToString(map.get(key));
	    if (!CommonUtils.isExist(value)) {
	        throw new ValidationException(displayName);
	    }
	    try {
	        int num = Integer.parseInt(value);
	        if (num < min || num > max) {
	            throw new Exception(displayName + "은(는) " + min + "~" + max + " 범위여야 합니다.");
	        }
	        return num;
	    } catch (NumberFormatException e) {
	        throw new Exception(displayName + "은(는) 숫자여야 합니다.");
	    }
	}

}
