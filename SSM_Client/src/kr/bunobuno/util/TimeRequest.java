package kr.bunobuno.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeRequest {

	public static String getCurrentDate(long time) {
		Calendar cal = Calendar.getInstance();
		
		cal.setTimeInMillis(time);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");	

		String date = format.format(cal.getTime());//현재 날짜 출력 년도 별로 하고 싶으면 -로 세퍼레이트 해주면된다.
		 
		return date;
	}
}
