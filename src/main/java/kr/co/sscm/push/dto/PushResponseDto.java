package kr.co.sscm.push.dto;

import java.util.Map;

/**
 * @author dngkg
 *
 */
public class PushResponseDto {

	private Map<String,Object> HEADER;

	private Map<String,Object> BODY;

	public Map<String, Object> getHEADER() {
		return HEADER;
	}

	public void setHEADER(Map<String, Object> hEADER) {
		HEADER = hEADER;
	}

	public Map<String, Object> getBODY() {
		return BODY;
	}

	public void setBODY(Map<String, Object> bODY) {
		BODY = bODY;
	}
}


