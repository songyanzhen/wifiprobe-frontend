package frontend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class testDateCHA {
	public static void main(String[]args) throws ParseException
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   java.util.Date now = df.parse("2004-03-26 13:31:40");
		   java.util.Date date=df.parse("2004-03-26 13:31:24");
		   long l=now.getTime()-date.getTime();
//		   long day=l/(24*60*60*1000);
//		   long hour=(l/(60*60*1000)-day*24);
//		   long min=((l/(60*1000))-day*24*60-hour*60);
//		   long s=(l/1000-day*24*60*60-hour*60*60-min*60);
		   System.out.println(l/1000);
		   
		   Date date1 = new Date("Wed Apr 05 2017 00:00:00 GMT+0800 (�й���׼ʱ��)");
			System.out.println(date1.toString());
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
			System.out.println(sdf.format(date1));
			String strMonth = "2017/04/20";
			int month = Integer.parseInt(strMonth.substring(5, 7));
			System.out.println(month);
	}
	
}
