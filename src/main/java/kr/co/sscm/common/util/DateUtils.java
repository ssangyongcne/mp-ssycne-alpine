package kr.co.sscm.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	/**
	 * 날짜를 문자열로 전달
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDateToStr( Date date, String format) {
		Date sDate = date;
		if(sDate == null) {
			sDate = new Date();
		}
		DateFormat dateFormat = new SimpleDateFormat(format);
		String strDate = dateFormat.format(sDate);
		return strDate;
	}

}
