package com.shanlin.android.autostore.common.utils;

import android.annotation.SuppressLint;

import com.shanlin.android.autostore.App;
import com.shanlin.autostore.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	public static final String DATE_FORMAT_Y_M_D_H_M_S = "yyyy-MM-dd  HH:mm:ss";
	public static final String DATE_FORMAT_Y_M_D = "yyyy-MM-dd";
	public static final String DATE_FORMAT_YM = "yyyy年MM月";
	@SuppressLint("SimpleDateFormat")
	public static String formatTime(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
		return formatter.format(new Date(time/* + 1000*/));//TODO 这个地方加 1000 导致和比下面的详情时间多1秒 ,先注释掉
	}
	
	public static String getCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
		String currentDate = format.format(new Date(System
				.currentTimeMillis()));
		return currentDate;
	}
	public static String getDateStringWithFormate(Date date, String formate) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat dateFormat = null;
		if (StrUtils.isEmpty(formate)) {
			dateFormat = new SimpleDateFormat(DATE_FORMAT_Y_M_D_H_M_S);
		} else {
			dateFormat = new SimpleDateFormat(formate);
		}
		return dateFormat.format(date);
	}
	/**
	 * 格式化时间戳
	 */
	public static String formatTimeMillion(long time) {
		Date date = new Date(time);
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_Y_M_D);
		String str = format.format(date);
		return str;
	}
	/**
	 * 
	 * @return xx小时前。xx分钟前转换
	 */
	public static String howLongAgo(long time) {
		long currentTime = System.currentTimeMillis()/1000;
		if (String.valueOf(time).length()==13)
		{
			time = time / 1000;
		}

		long timeAgo = currentTime - time; //过去了多少秒

		if (timeAgo<60) {
			return App.getInstance().getString(R.string.me_just);
		}
		if (timeAgo < 60 * 60) {
			return App.getInstance().getString(R.string.me_minutes_ago, timeAgo / 60);
		}
		if (timeAgo < 24*60*60) {
			return App.getInstance().getString(R.string.me_hour_ago,timeAgo / (60 * 60));
		}
		if (timeAgo < 30*24*60*60) {
			return App.getInstance().getString(R.string.me_day_ago,timeAgo / (60 * 60 *24));
		}
		if (timeAgo<12*30*24*60*60) {
			return App.getInstance().getString(R.string.me_month_ago,timeAgo / (60 * 60 *24 *30));
		}

		return App.getInstance().getString(R.string.me_year_ago,timeAgo / (60 * 60 * 365));
	}
	
	
	public static String getMonth(String time) {
		if(StrUtils.isEmpty(time)){
			return "";
		}
		try {
			Date d1 = new SimpleDateFormat(DATE_FORMAT_YM).parse(time);//定义起始日期
			SimpleDateFormat yFormat = new SimpleDateFormat("yyyy");
			String year = yFormat.format(d1);
			if(!year.equals(getCurrentYear()) ){
				return time;
			}
			SimpleDateFormat mFormat = new SimpleDateFormat("M");
			String month = mFormat.format(d1);
			if(month.equals(getCurrentMonth())){
				return "本月";
			}
			return month +"月";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
    public static String getCurrentYear(){
    	Calendar a= Calendar.getInstance();
    	return a.get(Calendar.YEAR)+"";
    }
    public static String getCurrentMonth(){
    	Calendar a= Calendar.getInstance();
    	return (a.get(Calendar.MONTH)+1)+"";
    }


	/**
	 * 判断2个日期是不是同一天
	 * @param date1
	 * @param date2
     * @return
     */
	public static boolean isSameDate(Calendar date1, Calendar date2) {


		boolean isSameYear  = date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR);
		boolean isSameMonth = date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH);
		boolean isSameDay  = date1.get(Calendar.DAY_OF_MONTH) == date2.get(Calendar.DAY_OF_MONTH);

		return isSameYear && isSameMonth && isSameDay;
	}


	public static Date transformDate(String date, String format){
		SimpleDateFormat strFormat = new SimpleDateFormat(format);
		try {
			return  strFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}


}
