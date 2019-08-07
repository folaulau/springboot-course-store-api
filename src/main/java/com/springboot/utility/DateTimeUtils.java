package com.springboot.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtils {

	// Millisecond Based
	public static final long MILLISECOND = 1000L;
	public static final long SECOND = MILLISECOND;
	public static final long MINUTE = SECOND * 60;
	public static final long HOUR = MINUTE * 60;
	public static final long DAY = HOUR * 24;
	public static final long WEEK = DAY * 7;
	
	public static final int NUMBER__OF_DAYS_TIL_START_COVERAGE = 13;
	
	public static final String UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	
	public static final String UTC_FORMAT_WITHOUT_TAG = "yyyy-MM-dd HH:mm:ss";
	
	public static final String MONTH_DATE_YEAR = "MMMM dd, yyyy";
	
	public static final String DAYOFWEEK_MONTH_DATE_YEAR = "E, MMMM dd, yyyy";
	
	/**
	 * Based on Milliseconds
	 * @param hr
	 * @return hours in milliseconds
	 */
	public static long getHoursInMilliseconds(long hr) {
		return HOUR * hr;
	}
	
	public static String getTimeStamp() {
		DateFormat formmatter = new SimpleDateFormat("M-dd-yyyy h:mm:ss a");
		return formmatter.format(new Date());
	}
	
	public static String getFormattedDate(Date date, String format) {
		DateFormat formmatter = new SimpleDateFormat(format);
		return formmatter.format(date);
	}
	
	public static Date getUTCDateFromMysqlString(String strDate) throws ParseException {
		return new SimpleDateFormat(UTC_FORMAT_WITHOUT_TAG).parse(strDate);
	}
	
	public static int getDiffMonths(Date start, Date end) {
		LocalDate startDate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate endDate = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		Period period = Period.between(startDate,endDate);
		
		int numOfYears = period.getYears();
		
		int numOfMonths = period.getMonths();
		
		if(numOfYears>=1) {
			numOfMonths += (numOfYears*12);
		}
		return numOfMonths;
	}

	public static int getDiffHours(Date start, Date end) {
		long different = end.getTime() - start.getTime();
		return (int) (different/HOUR);
	}

	public static int getDiffDays(Date start, Date end) {
		long different = end.getTime() - start.getTime();
		return (int) (different/DAY);
	}
	
	public static String getFormattedDate(Date date) {
		DateFormat formmatter = new SimpleDateFormat("M-dd-yyyy h:mm:ss.SSS a");
		return formmatter.format(date);
	}
	
	public static String getUTCFormattedDate(Date date) {
		DateFormat formmatter = new SimpleDateFormat(UTC_FORMAT);
		return formmatter.format(date);
	}
	
	public static String getDOBAsText(Date dob) {
		DateFormat formmatter = new SimpleDateFormat("yyyy-MM-dd");
		return formmatter.format(dob);
	}
	
	public static Date getLastSecondOfDayDateTime(Date date) {
		Calendar startDateCalendar = Calendar.getInstance();
		startDateCalendar.setTime(date);
		
		startDateCalendar.set(Calendar.HOUR_OF_DAY, startDateCalendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		startDateCalendar.set(Calendar.MINUTE, startDateCalendar.getActualMaximum(Calendar.MINUTE));
		startDateCalendar.set(Calendar.SECOND, startDateCalendar.getActualMaximum(Calendar.SECOND));
		startDateCalendar.set(Calendar.MILLISECOND, startDateCalendar.getActualMaximum(Calendar.MILLISECOND));

		return startDateCalendar.getTime();
	}
	
	public static Date getFirstSecondOfDayDateTime(Date date) {
		Calendar startDateCalendar = Calendar.getInstance();
		startDateCalendar.setTime(date);
		
		startDateCalendar.set(Calendar.HOUR_OF_DAY, startDateCalendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		startDateCalendar.set(Calendar.MINUTE, startDateCalendar.getActualMinimum(Calendar.MINUTE));
		startDateCalendar.set(Calendar.SECOND, startDateCalendar.getActualMinimum(Calendar.SECOND));
		startDateCalendar.set(Calendar.MILLISECOND, startDateCalendar.getActualMinimum(Calendar.MILLISECOND));

		return startDateCalendar.getTime();
	}
	
	public static boolean isValidStartDate(Date startDate) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		
		Date now = new Date();
		
		int numOfDays = DateTimeUtils.getDiffDays(now, startDate);
		
		return (numOfDays>=NUMBER__OF_DAYS_TIL_START_COVERAGE) ? true : false;
	}
	
	public static Date getBeginingOfDate(Date date) {
		Calendar startDateCalendar = Calendar.getInstance();
		startDateCalendar.setTime(date);

		startDateCalendar.set(Calendar.HOUR_OF_DAY, startDateCalendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		startDateCalendar.set(Calendar.MINUTE, startDateCalendar.getActualMinimum(Calendar.MINUTE));
		startDateCalendar.set(Calendar.SECOND, startDateCalendar.getActualMinimum(Calendar.SECOND));
		startDateCalendar.set(Calendar.MILLISECOND, startDateCalendar.getActualMinimum(Calendar.MILLISECOND));

		return startDateCalendar.getTime();
	}
	
	public static Date getEndingOfDate(Date date) {
		Calendar endDateCalendar = Calendar.getInstance();
		endDateCalendar.setTime(date);

		endDateCalendar.set(Calendar.HOUR_OF_DAY, endDateCalendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		endDateCalendar.set(Calendar.MINUTE, endDateCalendar.getActualMaximum(Calendar.MINUTE));
		endDateCalendar.set(Calendar.SECOND, endDateCalendar.getActualMaximum(Calendar.SECOND));
		endDateCalendar.set(Calendar.MILLISECOND, endDateCalendar.getActualMaximum(Calendar.MILLISECOND));

		return endDateCalendar.getTime();
	}
	
	public static int calculateAge(Date birthDate) {
		if (birthDate == null) {
			return 0;
		}
		Calendar birth = Calendar.getInstance();
		birth.setTime(birthDate);
		Calendar today = Calendar.getInstance();

		int yearDifference = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR);

		if (today.get(Calendar.MONTH) < birth.get(Calendar.MONTH)) {
			yearDifference--;
		} else {
			if (today.get(Calendar.MONTH) == birth.get(Calendar.MONTH)
					&& today.get(Calendar.DAY_OF_MONTH) < birth.get(Calendar.DAY_OF_MONTH)) {
				yearDifference--;
			}
		}
		return yearDifference < 0 ? 0 : yearDifference;
	}
	
	/**
	 * get date and set it to date at start of day on depicted timezone
	 * 
	 * @param date
	 * @param zone
	 * @return
	 */
	public static Date getDateAtStartOfDay(Date date, ZoneId zone) {
		if (date != null && zone != null) {
			return Date.from(date.toInstant().atZone(zone).toLocalDate().atStartOfDay(zone).toInstant());
		}
		return date;
	}

	/**
	 * get date and set it to date at end of day for specified timezone
	 * 
	 * @param date
	 * @param zone
	 * @return
	 */
	public static Date getDateAtEndOfDay(Date date, ZoneId zone) {
		if (date != null && zone != null) {
			return Date
					.from(date.toInstant().atZone(zone).toLocalDate().atTime(LocalTime.MAX).atZone(zone).toInstant());
		}
		return date;
	}

	/**
	 * return date as UTC date string for database yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String toStringForDb(Date date) {
		if (date != null) {
			Instant instant = Instant.ofEpochMilli(date.getTime());
			LocalDateTime startLocalDate = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			return startLocalDate.format(formatter);
		}
		return null;
	}
	
	/**
	 * return zone, default to system if failing
	 * 
	 * @param date
	 * @return
	 */
	public static ZoneId getZoneDefault(String timezone) {
		try {
			return ZoneId.of(timezone);
		} catch (Exception e) {
			// ignore return default
		}
		return ZoneId.systemDefault();
	}
	
	/**
	 * reset to the start of the hour for a date in timezone
	 * 
	 * @param date
	 * @param zone
	 * @return
	 */
	public static Date getDateAtStartOfTheHour(Date date, ZoneId zone) {
		if (date != null && zone != null) {
			LocalDateTime localDateTime = date.toInstant().atZone(zone).toLocalDateTime();
			localDateTime = localDateTime.truncatedTo(ChronoUnit.HOURS);
			return Date.from(localDateTime.atZone(zone).toInstant());
		}
		return date;
	}
	
	/**
	 * reset to the start of the hour before for a date in timezone
	 * 
	 * @param date
	 * @param zone
	 * @return
	 */
	public static Date getDateAtStartOfThePreviousHour(Date date, ZoneId zone) {
		if (date != null && zone != null) {
			LocalDateTime localDateTime = date.toInstant().atZone(zone).toLocalDateTime();
			localDateTime = localDateTime.truncatedTo(ChronoUnit.HOURS);
			localDateTime = localDateTime.minusHours(1);
			return Date.from(localDateTime.atZone(zone).toInstant());
		}
		return date;
	}

	/**
	 * reset to the max of the hour for a date in timezone
	 * 
	 * @param date
	 * @param zone
	 * @return
	 */
	public static Date getDateAtEndOfTheHour(Date date, ZoneId zone) {
		if (date != null && zone != null) {
			LocalDateTime localDateTime = date.toInstant().atZone(zone).toLocalDateTime();
			localDateTime = localDateTime.withMinute(59).withSecond(59).withNano(999_999_999);
			return Date.from(localDateTime.atZone(zone).toInstant());
		}
		return date;
	}
}
