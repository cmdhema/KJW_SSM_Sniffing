package kr.bunobuno.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeRequest {

	public static String getCurrentDate(long time) {
		Calendar cal = Calendar.getInstance();
		
		cal.setTimeInMillis(time);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");	

		String date = format.format(cal.getTime());//���� ��¥ ��� �⵵ ���� �ϰ� ������ -�� ���۷���Ʈ ���ָ�ȴ�.
		 
		return date;
	}
}
