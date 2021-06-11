package dateutil.demo.utils;



import dateutil.demo.common.DateRange;
import dateutil.demo.enums.TimeExtentEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 日期工具类
 *
 * @author hanmeng
 */
public class DateUtil extends DateUtils {
    /**
     * 常用变量
     */
    public static final String DATE_TIME_PATTERN1 = "yyyy-MM-dd'T'HH:mm:ss.SSS Z";
    public static final String DATE_TIME_yyyyMMdd = "yyyyMMdd";
    public static final String DATE_TIME_yyyyMM = "yyyyMM";

    public static final String DATE_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YM = "yyyy-MM";
    public static final String DATE_FORMAT_Y = "yyyy";
    public static final String DATE_FORMAT_HMS = "HH:mm:ss";
    public static final String DATE_FORMAT_HM = "HH:mm";
    public static final String DATE_FORMAT_YMDHM = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_YMDHMS = "yyyyMMddHHmmss";
    public static final long ONE_DAY_MILLS = 3600000 * 24;
    public static final int WEEK_DAYS = 7;
    private static final int dateLength = DATE_FORMAT_YMDHM.length();

    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_FULL);
    private static final SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_TIME_PATTERN1);
    private static final SimpleDateFormat sdf_yyyyMMdd = new SimpleDateFormat(DATE_TIME_yyyyMMdd);
    private static final SimpleDateFormat sdf_yyyy_MM_dd = new SimpleDateFormat(DATE_FORMAT_YMD);
    private static final SimpleDateFormat sdf_yyyyMM = new SimpleDateFormat(DATE_TIME_yyyyMM);
    private static final SimpleDateFormat sdf_yyyy_MM = new SimpleDateFormat(DATE_FORMAT_YM);
    private static final SimpleDateFormat sdf_yyyy = new SimpleDateFormat(DATE_FORMAT_Y);

    private static final Pattern ymd_pattern = Pattern.compile("^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}$");
    private static final Pattern ym_pattern = Pattern.compile("^[0-9]{4}-[0-9]{1,2}$");
    private static final Pattern y_pattern = Pattern.compile("^[0-9]{4}$");

    /**
     * 获取当前日期的UTC时间 不包含时分秒
     *
     * @return
     */
    public static Long getCurrentDateUtc() {
        java.time.LocalDate localDate = java.time.LocalDate.now();
        ZoneId zone = ZoneId.systemDefault();
        java.time.Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant).getTime();
    }

    /**
     * 获取当前日期 不包含时分秒
     *
     * @return
     */
    public static Date getCurrentDate() {
        java.time.LocalDate localDate = java.time.LocalDate.now();
        ZoneId zone = ZoneId.systemDefault();
        java.time.Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * 将时间格式化成 yyyy-MM-dd HH:mm:ss 字符串
     *
     * @param date
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String dateTime2String(Date date) {
        if (date == null) {
            return null;
        }
        return formatDateToString(date, DATE_FORMAT_FULL);
    }

    /**
     * 将时间格式化成 yyyyMMddHHmmss 字符串
     *
     * @param date
     * @return
     */
    public static String date2yMdHmsString(Date date) {
        return formatDateToString(date, DATE_FORMAT_YMDHMS);
    }

    /**
     * 将yyyyMMddHHmmss字符串类型时间格式化成Date类型
     *
     * @param date
     * @return
     */
    public static Date yMdHmsString2Date(String date) {
        return formatStringToDate(date, DATE_FORMAT_YMDHMS);
    }

    public static Date yMdString2Date(String date) {
        return formatStringToDate(date, DATE_FORMAT_YMD);
    }

    /**
     * 字符串转换为制定格式日期
     * (注意：当你输入的日期是2014-12-21 12:12，format对应的应为yyyy-MM-dd HH:mm
     * 否则异常抛出)
     *
     * @param date
     * @param format
     * @return
     * @
     */
    public static Date formatStringToDate(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.toString());
        }
    }

    /**
     * 日期转换为制定格式字符串
     *
     * @param time
     * @param format
     * @return
     */
    public static String formatDateToString(Date time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }

    /**
     * 判断一个日期是否属于两个时段内
     *
     * @param time
     * @param timeRange
     * @return
     */
    public static boolean isTimeInRange(Date time, Date[] timeRange) {
        return (!time.before(timeRange[0]) && !time.after(timeRange[1]));
    }

    /**
     * 从完整的时间截取精确到分的时间
     *
     * @param fullDateStr
     * @return
     */
    public static String getDateToMinute(String fullDateStr) {
        return fullDateStr == null ? null
                : (fullDateStr.length() >= dateLength ? fullDateStr.substring(
                0, dateLength) : fullDateStr);
    }

    /**
     * 返回指定年度的所有周。List中包含的是String[2]对象 string[0]本周的开始日期,string[1]是本周的结束日期。
     * 日期的格式为YYYY-MM-DD 每年的第一个周，必须包含星期一且是完整的七天。
     * 例如：2009年的第一个周开始日期为2009-01-05，结束日期为2009-01-11。 星期一在哪一年，那么包含这个星期的周就是哪一年的周。
     * 例如：2008-12-29是星期一，2009-01-04是星期日，哪么这个周就是2008年度的最后一个周。
     *
     * @param year 格式 YYYY ，必须大于1900年度 小于9999年
     * @return @
     */
    public static List<String[]> getWeeksByYear(final int year) {
        int weeks = getWeekNumOfYear(year);
        List<String[]> result = new ArrayList<String[]>(weeks);
        int start = 1;
        int end = 7;
        for (int i = 1; i <= weeks; i++) {
            String[] tempWeek = new String[2];
            tempWeek[0] = getDateForDayOfWeek(year, i, start);
            tempWeek[1] = getDateForDayOfWeek(year, i, end);
            result.add(tempWeek);
        }
        return result;
    }

    /**
     * 计算指定年、周的上一年、周
     *
     * @param year
     * @param week
     * @return @
     */
    public static int[] getLastYearWeek(int year, int week) {
        if (week <= 0) {
            throw new IllegalArgumentException("周序号不能小于1！！");
        }
        int[] result = {week, year};
        if (week == 1) {
            // 上一年
            result[1] -= 1;
            // 最后一周
            result[0] = getWeekNumOfYear(result[1]);
        } else {
            result[0] -= 1;
        }
        return result;
    }

    /**
     * 下一个[周，年]
     *
     * @param year
     * @param week
     * @return @
     */
    public static int[] getNextYearWeek(int year, int week) {
        if (week <= 0) {
            throw new IllegalArgumentException("周序号不能小于1！！");
        }
        int[] result = {week, year};
        int weeks = getWeekNumOfYear(year);
        if (week == weeks) {
            // 下一年
            result[1] += 1;
            // 第一周
            result[0] = 1;
        } else {
            result[0] += 1;
        }
        return result;
    }

    /**
     * 计算指定年度共有多少个周。(从周一开始)
     *
     * @param year
     * @return @
     */
    public static int getWeekNumOfYear(final int year) {
        return getWeekNumOfYear(year, Calendar.MONDAY);
    }

    /**
     * 计算指定年度共有多少个周。
     *
     * @param year yyyy
     * @return @
     */
    public static int getWeekNumOfYear(final int year, int firstDayOfWeek) {
        // 每年至少有52个周 ，最多有53个周。
        int minWeeks = 52;
        int maxWeeks = 53;
        int result = minWeeks;
        int sIndex = 4;
        String date = getDateForDayOfWeek(year, maxWeeks, firstDayOfWeek);
        // 判断年度是否相符，如果相符说明有53个周。
        if (date.substring(0, sIndex).equals(year)) {
            result = maxWeeks;
        }
        return result;
    }

    public static int getWeeksOfWeekYear(final int year) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setMinimalDaysInFirstWeek(WEEK_DAYS);
        cal.set(Calendar.YEAR, year);
        return cal.getWeeksInWeekYear();
    }

    /**
     * 获取指定年份的第几周的第几天对应的日期yyyy-MM-dd(从周一开始)
     *
     * @param year
     * @param weekOfYear
     * @param dayOfWeek
     * @return yyyy-MM-dd 格式的日期 @
     */
    public static String getDateForDayOfWeek(int year, int weekOfYear, int dayOfWeek) {
        return getDateForDayOfWeek(year, weekOfYear, dayOfWeek, Calendar.MONDAY);
    }

    /**
     * 获取指定年份的第几周的第几天对应的日期yyyy-MM-dd，指定周几算一周的第一天（firstDayOfWeek）
     *
     * @param year
     * @param weekOfYear
     * @param dayOfWeek
     * @param firstDayOfWeek 指定周几算一周的第一天
     * @return yyyy-MM-dd 格式的日期
     */
    public static String getDateForDayOfWeek(int year, int weekOfYear,
                                             int dayOfWeek, int firstDayOfWeek) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(firstDayOfWeek);
        cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        cal.setMinimalDaysInFirstWeek(WEEK_DAYS);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
        return formatDateToString(cal.getTime(), DATE_FORMAT_YMD);
    }

    /**
     * 获取指定日期星期几
     *
     * @param datetime
     * @
     */
    public static int getWeekOfDate(String datetime) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setMinimalDaysInFirstWeek(WEEK_DAYS);
        Date date = formatStringToDate(datetime, DATE_FORMAT_YMD);
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);

    }

    /**
     * 计算某年某周内的所有日期(从周一开始 为每周的第一天)
     *
     * @param yearNum
     * @param weekNum
     * @return @
     */
    public static List<String> getWeekDays(int yearNum, int weekNum) {
        return getWeekDays(yearNum, weekNum, Calendar.MONDAY);
    }

    /**
     * 计算某年某周内的所有日期(七天)
     *
     * @return yyyy-MM-dd 格式的日期列表
     */
    public static List<String> getWeekDays(int year, int weekOfYear,
                                           int firstDayOfWeek) {
        List<String> dates = new ArrayList<String>();
        int dayOfWeek = firstDayOfWeek;
        for (int i = 0; i < WEEK_DAYS; i++) {
            dates.add(getDateForDayOfWeek(year, weekOfYear, dayOfWeek++,
                    firstDayOfWeek));
        }
        return dates;
    }

    /**
     * 获取目标日期的上周、或本周、或下周的年、周信息
     *
     * @param queryDate      传入的时间
     * @param weekOffset     -1:上周 0:本周 1:下周
     * @param firstDayOfWeek 每周以第几天为首日
     * @return
     */
    public static int[] getWeekAndYear(String queryDate, int weekOffset,
                                       int firstDayOfWeek) {

        Date date = formatStringToDate(queryDate, DATE_FORMAT_YMD);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(firstDayOfWeek);
        calendar.setMinimalDaysInFirstWeek(WEEK_DAYS);
        int year = calendar.getWeekYear();
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int[] result = {week, year};
        switch (weekOffset) {
            case 1:
                result = getNextYearWeek(year, week);
                break;
            case -1:
                result = getLastYearWeek(year, week);
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 计算个两日期的天数
     *
     * @param startDate 开始日期字串
     * @param endDate   结束日期字串
     * @return
     */
    public static int getDaysBetween(String startDate, String endDate) {
        int dayGap = 0;
        if (startDate != null && startDate.length() > 0 && endDate != null
                && endDate.length() > 0) {
            Date end = formatStringToDate(endDate, DATE_FORMAT_YMD);
            Date start = formatStringToDate(startDate, DATE_FORMAT_YMD);
            dayGap = getDaysBetween(start, end);
        }
        return dayGap;
    }

    private static int getDaysBetween(Date startDate, Date endDate) {
        return (int) ((endDate.getTime() - startDate.getTime()) / ONE_DAY_MILLS);
    }

    public static int getSecondsBetween(Date startDate, Date endDate) {
        return (int) ((endDate.getTime() - startDate.getTime()) / 1000);
    }

    /**
     * 计算两个日期之间的天数差
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getDaysGapOfDates(Date startDate, Date endDate) {
        int date = 0;
        if (startDate != null && endDate != null) {
            date = getDaysBetween(startDate, endDate);
        }
        return date;
    }

    /**
     * 计算两个日期之间的年份差距
     *
     * @param firstDate
     * @param secondDate
     * @return
     */

    public static int getYearGapOfDates(Date firstDate, Date secondDate) {
        if (firstDate == null || secondDate == null) {
            return 0;
        }
        Calendar helpCalendar = Calendar.getInstance();
        helpCalendar.setTime(firstDate);
        int firstYear = helpCalendar.get(Calendar.YEAR);
        helpCalendar.setTime(secondDate);
        int secondYear = helpCalendar.get(Calendar.YEAR);
        return secondYear - firstYear;
    }

    /**
     * 计算两个日期之间的月份差距
     *
     * @param firstDate
     * @param secondDate
     * @return
     */
    public static int getMonthGapOfDates(Date firstDate, Date secondDate) {
        if (firstDate == null || secondDate == null) {
            return 0;
        }

        return (int) ((secondDate.getTime() - firstDate.getTime())
                / ONE_DAY_MILLS / 30);

    }

    /**
     * 计算是否包含当前日期
     *
     * @return
     */
    public static boolean isContainCurrent(List<String> dates) {
        boolean flag = false;
        SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT_YMD);
        Date date = new Date();
        String dateStr = fmt.format(date);
        for (int i = 0; i < dates.size(); i++) {
            if (dateStr.equals(dates.get(i))) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 从date开始计算time天后的日期
     *
     * @param time
     * @return
     */
    public static String getCalculateDateToString(String startDate, int time) {
        String resultDate = null;
        if (startDate != null && startDate.length() > 0) {
            Date date = formatStringToDate(startDate, DATE_FORMAT_YMD);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.DAY_OF_YEAR, time);
            date = c.getTime();
            resultDate = formatDateToString(date, DATE_FORMAT_YMD);
        }
        return resultDate;
    }

    /**
     * 获取从某日期开始计算，指定的日期所在该年的第几周
     *
     * @param date
     * @param admitDate
     * @return
     */
    public static int[] getYearAndWeeks(String date, String admitDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(formatStringToDate(admitDate, DATE_FORMAT_YMD));
        int time = c.get(Calendar.DAY_OF_WEEK);
        return getWeekAndYear(date, 0, time);
    }

    /**
     * 获取指定日期refDate，前或后一周的所有日期
     *
     * @param refDate    参考日期
     * @param weekOffset -1:上周 0:本周 1:下周
     * @param startDate  哪天算一周的第一天
     * @return yyyy-MM-dd 格式的日期
     */
    public static List<String> getWeekDaysAroundDate(String refDate, int weekOffset, String startDate) {
        // 以startDate为一周的第一天
        Calendar c = Calendar.getInstance();
        c.setTime(formatStringToDate(startDate, DATE_FORMAT_YMD));
        int firstDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        // 获取相应周
        int[] weekAndYear = getWeekAndYear(refDate, weekOffset, firstDayOfWeek);
        // 获取相应周的所有日期
        return getWeekDays(weekAndYear[1], weekAndYear[0], firstDayOfWeek);
    }


    /**
     * 获取本周一
     *
     * @return yyyy-MM-dd 格式的周一
     */
    public static String getMonday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        //设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        //获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayWeek == 1) {
            dayWeek = 8;
        }

        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);
        Date mondayDate = cal.getTime();
        return sdf.format(mondayDate);

    }

    /**
     * 获取本周日
     *
     * @return yyyy-MM-dd格式的周日
     */
    public static String getWeekend() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        //设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        //获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayWeek == 1) {
            dayWeek = 8;
        }
        cal.add(Calendar.DATE, 4 + cal.getFirstDayOfWeek());
        Date sundayDate = cal.getTime();
        return sdf.format(sundayDate);
    }

    /**
     * 根据时间点获取时间区间
     *
     * @param hours
     * @return
     */
    public static List<String[]> getTimePointsByHour(int[] hours) {
        List<String[]> hourPoints = new ArrayList<String[]>();
        String sbStart = ":00:00";
        String sbEnd = ":59:59";
        for (int i = 0; i < hours.length; i++) {
            String[] times = new String[2];
            times[0] = hours[i] + sbStart;
            times[1] = (hours[(i + 1 + hours.length) % hours.length] - 1)
                    + sbEnd;
            hourPoints.add(times);
        }
        return hourPoints;
    }

    /**
     * 根据指定的日期，增加或者减少天数
     *
     * @param date
     * @param amount
     * @return
     */
    public static Date addDays(Date date, int amount) {
        return add(date, Calendar.DAY_OF_MONTH, amount);
    }

    public static Date addSecends(Date date, int amount) {
        return add(date, Calendar.SECOND, amount);
    }

    /**
     * 根据指定的日期，类型，增加或减少数量
     *
     * @param date
     * @param calendarField
     * @param amount
     * @return
     */
    public static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    /**
     * 获取当前日期的最大日期 时间2014-12-21 23:59:59
     *
     * @return
     */
    public static Date getCurDateWithMaxTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }

    /**
     * 获取当前日期的最小日期时间 2014-12-21 00:00:00
     *
     * @return
     */
    public static Date getCurDateWithMinTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static String Date2StringFormat_yyyyMMdd(Date date) {
        if (date != null) {
            return sdf_yyyyMMdd.format(date);
        } else {
            throw new NullPointerException("DateUtil formatDefaultDate input null date");
        }
    }

    public static String Date2StringFormat_yyyy_MM_dd(Date date) {
        if (date != null) {
            return sdf_yyyy_MM_dd.format(date);
        } else {
            throw new NullPointerException("DateUtil formatDefaultDate input null date");
        }
    }

    public static String formatDefaultDateTime(Date date) {
        if (date != null) {
            return sdf.format(date);
        } else {
            throw new NullPointerException("DateUtil getFormattedDateTimeStr input null date");
        }
    }

    public static String formatDateTimeForDynamoDB(Date date) {
        if (date != null) {
            return sdf1.format(date);
        } else {
            throw new NullPointerException("DateUtil getFormattedDateTimeStr input null date");
        }
    }

    public static Date parseDateTimeStr(String dateTimeStr) {
        if (StringUtils.isBlank(dateTimeStr)) {
            return null;
        }
        ParsePosition pos = new ParsePosition(0);
        Date date = sdf.parse(dateTimeStr, pos);
        return date;
    }


    /**
     * time 为(yyyy-MM-dd'T'HH:mm:ss.SSSZZ)格式的字符串时间
     * 例如
     * "2012-05-25T14:59:38.237-07:00"
     * "2012-06-19T01:07:52.000Z"
     *
     * @return java.util.Date   当前Locale的Date
     * @throws UnsupportedOperationException if parsing is not supported
     * @throws IllegalArgumentException      if the time to parse is invalid
     */
    public static Date parseISOFormatToDate(String time) {
        DateTimeFormatter parser2 = ISODateTimeFormat.dateTime();
        return parser2.parseDateTime(time).toDate();
    }
	/*public static Date getUTCTime(Long time){
		//1、取得本地时间：
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);

		//2、取得时间偏移量：
		final int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);

		//3、取得夏令时差：
		final int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);

		//4、从本地时间里扣除这些差量，即可以取得UTC时间：
		cal.add(Calendar.MILLISECOND, - (zoneOffset + dstOffset));
		return cal.getTime();
	}*/


    /**
     * eg: 20140101
     *
     * @param date
     * @return
     */
    public static String getYearQuarter(Date date, boolean next) {
        Date useDate = date != null ? date : new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(useDate);

        int year = cal.get(Calendar.YEAR);
        int quarter = getQuarter(cal);
        if (next) {
            if (quarter == 4) {
                year += year;
                quarter = 1;
            } else {
                quarter += 1;
            }
        }
        String str = String.format("%s_%s", year, quarter);
        return str;
    }

    /**
     * 获取当前时间所属季度
     *
     * @param offset 偏移量
     * @return
     */
    public static int getQuarter(int offset) {
        int month = Integer.parseInt(getYearMonth(0, "MM"));
        int quarter;
        switch (month) {
            case 1:
            case 2:
            case 3:
                quarter = 1;
                break;
            case 4:
            case 5:
            case 6:
                quarter = 2;
                break;
            case 7:
            case 8:
            case 9:
                quarter = 3;
                break;
            case 10:
            case 11:
            case 12:
                quarter = 4;
                break;
            default:
                quarter = 0;
                break;
        }
        return quarter + offset;
    }

    /**
     * 得到一天是一年的第几个季度
     *
     * @return
     */
    private static int getQuarter(Calendar cal) {
        int month = cal.get(Calendar.MONTH) + 1;
        int quarter;
        switch (month) {
            case 1:
            case 2:
            case 3:
                quarter = 1;
                break;
            case 4:
            case 5:
            case 6:
                quarter = 2;
                break;
            case 7:
            case 8:
            case 9:
                quarter = 3;
                break;
            case 10:
            case 11:
            case 12:
                quarter = 4;
                break;
            default:
                quarter = 0;
                break;
        }
        return quarter;
    }

    /**
     * utc时间转DateTime
     *
     * @param utcTime utc时间
     * @return
     */
    public static DateTime utc2DateTime(Long utcTime) {
        DateTime dateTime = new DateTime(utcTime, DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT+8")));
        return dateTime;
    }

    /**
     * 获取plusDays前后的日期 格式为YYYY-MM-DD
     *
     * @param plusDays
     * @return
     */
    public static DateTime getDateTime(Integer plusDays) {
        DateTime dateTime = new DateTime();
        //plusDays前的日期
        dateTime = dateTime.plusDays(plusDays);
        return dateTime.dayOfWeek().roundFloorCopy();
    }

    /**
     * 获取plusDays前的日期utc时间 若plusDays=0 则获取当天日期的utc时间(注 不包括时分秒)
     *
     * @param plusDays
     * @return
     */
    public static Long getDateUTCTime(Integer plusDays) {
        return getDateTime(plusDays).getMillis();
    }

    /**
     * 获取当前的小时数
     *
     * @return
     */
    public static int getHourOfDay() {
        DateTime dateTime = new DateTime();
        return dateTime.getHourOfDay();
    }

    /**
     * 获取当前的小时数
     *
     * @return
     */
    public static int getMinuteOfHour() {
        DateTime dateTime = new DateTime();
        return dateTime.getMinuteOfHour();
    }

    /**
     * 获取2个时间间隔的天数
     *
     * @param endTime  结束时间
     * @param starTime 开始时间
     * @return
     */
    public static int getDaysBetweenV2(Date endTime, Date starTime) {
        DateTime start = new DateTime(starTime);
        DateTime end = new DateTime(endTime);
        return Math.abs(Days.daysBetween(end, start).getDays());
    }

    /**
     * 获取2个时间之间的小时数
     *
     * @param starTime 开始时间
     * @param endTime  结束时间
     * @return
     */
    public static int getHoursBetweenDays(Date endTime, Date starTime) {
        DateTime start = new DateTime(starTime);
        DateTime end = new DateTime(endTime);
        return Math.abs(Hours.hoursBetween(end, start).getHours());
    }

    /**
     * 获取2个时间之间的小时数
     *
     * @param endTime  结束时间
     * @param starTime 开始时间
     * @return
     */
    public static int getMinutesBetweenDays(Date endTime, Date starTime) {
        DateTime start = new DateTime(starTime);
        DateTime end = new DateTime(endTime);
        return Math.abs(Minutes.minutesBetween(end, start).getMinutes());
    }

    public static int getHoursBetweenDays2(Date starTime, Date endTime) {
        Long t = endTime.getTime() - starTime.getTime();
        return (int) (t / (60 * 60 * 1000));
    }

    /**
     * 获取指定年月第一天的utc时间
     *
     * @param year  年
     * @param month 月
     * @return
     */
    public static long getMonthFirstDay(Integer year, Integer month) {
        DateTime dateTime;
        if (null == year || null == month) {
            dateTime = new DateTime();
        } else {
            dateTime = new DateTime(year, month, 1, 0, 0);
        }
        return dateTime.monthOfYear().roundFloorCopy().getMillis();
    }

    public static String yestodayDate() {
        LocalDate localDate = LocalDate.now().minusDays(1);
        return Date2StringFormat_yyyy_MM_dd(localDate.toDate());
    }

    /*public static Map<String, String> getDetailDate(LocalDate localDate) {
        Map<String, String> map = Maps.newHashMap();
        String date = Date2StringFormat_yyyy_MM_dd(localDate.toDate());
        map.put("date",date);
        String year = String.valueOf(localDate.getYear());
        map.put("year",year);
        String month = String.valueOf(localDate.monthOfYear().get());
        map.put("month",month);
        return map;
    }*/
    //上周同一天 往前推7天
    public static String lastWeekDate(String date) {
        LocalDate localDate = LocalDate.parse(date);
        localDate = localDate.minusDays(7);
        return Date2StringFormat_yyyy_MM_dd(localDate.toDate());
    }

    //上个月同一天 如果本月天数大于上个月的天数 则多天返回null
    public static String lastMonthDate(String date) {
        LocalDate lastMonth = lastMonthLocalDate(date);
        if (null == lastMonth) {
            return null;
        }
        return Date2StringFormat_yyyy_MM_dd(lastMonth.toDate());
    }

    public static LocalDate lastMonthLocalDate(String date) {
        //31天的为大月  小于31天的月份为小月
        Integer[] days31Month = {1, 3, 5, 7, 8, 10, 12};//大月月份数组
        LocalDate localDate = LocalDate.parse(date);
        LocalDate lastMonth = localDate.minusMonths(1);
        int lastMonthOfYear = lastMonth.getMonthOfYear();
        //判断月份是否为大月
        if (!Arrays.stream(days31Month).anyMatch(p -> p == lastMonthOfYear)) {
            int dayOfMonth = localDate.getDayOfMonth();
            if (2 == lastMonthOfYear) {
                java.time.LocalDate localDate1 = java.time.LocalDate.ofYearDay(lastMonth.getYear(), lastMonth.dayOfYear().get());
                //判断是否为闰年
                if (localDate1.isLeapYear()) {
                    if (dayOfMonth > 29) {
                        return null;
                    }
                } else {
                    if (dayOfMonth > 28) {
                        return null;
                    }
                }
            } else {
                if (dayOfMonth > 30) {
                    return null;
                }
            }

        }
        return lastMonth;
    }

    //（T-1）天往前推7天
    public static List<String> getBefore7Dates(LocalDate date) {
        List<String> indexDates = new ArrayList<>();
        date = date.minusDays(1);//T-1
        indexDates.add(Date2StringFormat_yyyy_MM_dd(date.toDate()));
        for (int i = 1; i < 7; i++) {
            indexDates.add(Date2StringFormat_yyyy_MM_dd(date.minusDays(i).toDate()));
        }
        return indexDates;
    }

    public static List<String> lastWeekDates(LocalDate date) {
        List<String> indexDates = new ArrayList<>();
        date = date.minusDays(1);//T-1
        indexDates.add(Date2StringFormat_yyyy_MM_dd(date.minusDays(7).toDate()));
        for (int i = 1; i < 7; i++) {
            indexDates.add(Date2StringFormat_yyyy_MM_dd(date.minusDays(i + 7).toDate()));
        }
        return indexDates;
    }

    public static List<String> lastMonthDates(LocalDate date) {
        List<String> indexDates = new ArrayList<>();
        List<String> before = getBefore7Dates(date);
        String lastMonthDate;
        for (String p : before) {
            lastMonthDate = lastMonthDate(p);
            if (null == lastMonthDate) {
                continue;
            }
            indexDates.add(lastMonthDate);
        }
        return indexDates;
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        long lt = new Long(seconds);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param date_str 字符串日期
     * @param format   如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 取得当天0点的时间戳
     * offset 可设置偏移量 昨天为-1
     *
     * @param offset
     * @return
     */
    public static String zeroTimeStamp(int offset) {
        long current = System.currentTimeMillis();
        long zero = (current / (1000 * 3600 * 24) + offset) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
        return String.valueOf(zero);
    }

    /**
     * 获取相对偏移量年份
     *
     * @param offset
     * @return
     */
    public static String getYear(int offset) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, offset);
        Date y = c.getTime();
        String time = format.format(y);
        return time;
    }

    /**
     * 获取相对偏移量年月
     *
     * @param offset
     * @param pattern
     * @return
     */
    public static String getYearMonth(int offset, String pattern) {
        if (StringUtils.isBlank(pattern)) {
            pattern = "yyyy-MM";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, offset);
        Date y = c.getTime();
        String time = format.format(y);
        return time;
    }

    /**
     * 获取相对偏移量年月日
     *
     * @param offset
     * @param pattern
     * @return
     */
    public static String getDay(int offset, String pattern) {
        if (StringUtils.isBlank(pattern)) {
            pattern = "yyyy-MM-dd";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, offset);
        Date y = c.getTime();
        String time = format.format(y);
        return time;
    }

    /**
     * 获取当前日期
     *
     * @param pattern
     * @return
     */
    public static String getCurrentTime(String pattern) {
        if (StringUtils.isBlank(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        Date y = c.getTime();
        String time = format.format(y);
        return time;
    }

    /**
     * 获取上周日日期
     *
     * @param
     * @return
     */
    public static String getLastWeekDate() {
        Calendar calendar = Calendar.getInstance();
        int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);
        //判断是否是周天，老外周天是第一天
        if (Calendar.SUNDAY == dayWeek) {
            calendar.add(Calendar.DATE, 0 - Calendar.DAY_OF_WEEK);
            return sdf_yyyy_MM_dd.format(calendar.getTime());
        }
        calendar.add(Calendar.DATE, Calendar.SUNDAY - dayWeek);
        return sdf_yyyy_MM_dd.format(calendar.getTime());
    }

    /**
     * 获取上上周日期
     *
     * @param
     * @return
     */
    public static String getLastTwoWeekDate() {
        Calendar calendar = Calendar.getInstance();
        int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);
        //判断是否是周天，老外周天是第一天
        if (Calendar.SUNDAY == dayWeek) {
            calendar.add(Calendar.DATE, 0 - 2 * Calendar.DAY_OF_WEEK);
            return sdf_yyyy_MM_dd.format(calendar.getTime());
        }
        calendar.add(Calendar.DATE, Calendar.SUNDAY - dayWeek - Calendar.DAY_OF_WEEK);
        return sdf_yyyy_MM_dd.format(calendar.getTime());
    }

    /**
     * 获指定日期取前30天日期
     *
     * @param
     * @return
     */
    public static String getThirtyDayBeforeDate(String dateString) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf_yyyy_MM_dd.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
            calendar.setTime(DateUtil.getCurrentDate());
        }
        calendar.add(Calendar.DATE, 0 - 30);
        return sdf_yyyy_MM_dd.format(calendar.getTime());
    }

    /**
     * 获取当天日期
     *
     * @param
     * @return
     */
    public static String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        return sdf_yyyy_MM_dd.format(calendar.getTime());
    }

    /**
     * 获取日期 String
     *
     * @param
     * @return
     */
    public static String getDateString(Date Date) {
        return sdf_yyyy_MM_dd.format(Date);
    }

    /**
     * 获取日期 String
     *
     * @param
     * @return
     */
    public static Date getDate(String DateString) {
        try {
            return sdf_yyyy_MM_dd.parse(DateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取指定日期的后N天
     *
     * @param
     * @return
     */
    public static Date getAfterNumDate(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, num);
        return calendar.getTime();
    }

    /**
     * 验证字符串是否符合yyyy-MM-dd、yyyy-MM、yyyy格式
     *
     * @param date
     * @return
     */
    public static boolean ymdStrPattern(String date) {
        if (StringUtils.isEmpty(date)) {
            return false;
        }
        return ymd_pattern.matcher(date).matches();
    }

    /**
     * 获取时间区间
     *
     * @param date
     * @return
     */
    public static String getTimeExtent(Date date) {
        if (date == null) {
            return TimeExtentEnum.ONE_YEAR_AGO.getDesc();
        }
        Date currentDate = new Date();
        if (date.after(getBeforeNumDate(currentDate, Calendar.HOUR, 1))) {
            return TimeExtentEnum.ONE_HOUR.getDesc();
        } else if (date.after(getBeforeNumDate(currentDate, Calendar.DATE, 1))) {
            return TimeExtentEnum.ONE_DAY.getDesc();
        } else if (date.after(getBeforeNumDate(currentDate, Calendar.DATE, 7))) {
            return TimeExtentEnum.ONE_WEEK.getDesc();
        } else if (date.after(getBeforeNumDate(currentDate, Calendar.MONTH, 1))) {
            return TimeExtentEnum.ONE_MONTH.getDesc();
        } else if (date.after(getBeforeNumDate(currentDate, Calendar.MONTH, 6))) {
            return TimeExtentEnum.ONE_MONTH_AGO.getDesc();
        } else if (date.after(getBeforeNumDate(currentDate, Calendar.MONTH, 12))) {
            return TimeExtentEnum.HALF_YEAR_AGO.getDesc();
        } else {
            return TimeExtentEnum.ONE_YEAR_AGO.getDesc();
        }
    }


    /**
     * 获取指定时间前
     *
     * @param date
     * @return
     */
    public static Date getBeforeNumDate(Date date, int type, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(type, 0 - num);
        return calendar.getTime();
    }


    /**
     * 验证字符串是否符合yyyy-MM格式
     *
     * @param date
     * @return
     */
    public static boolean ymStrPattern(String date) {
        if (StringUtils.isEmpty(date)) {
            return false;
        }
        return ym_pattern.matcher(date).matches();
    }

    /**
     * 验证字符串是否符合yyyy格式
     *
     * @param date
     * @return
     */
    public static boolean yearStrPattern(String date) {
        if (StringUtils.isEmpty(date)) {
            return false;
        }
        return y_pattern.matcher(date).matches();
    }

    /**
     * 获取日期 String
     *
     * @param
     * @return
     */
    public static String getYmDateString(Date Date) {
        return sdf_yyyy_MM.format(Date);
    }

    /**
     * 获取日期 String
     *
     * @param
     * @return
     */
    public static Date getYmDate(String DateString) {
        try {
            return sdf_yyyy_MM.parse(DateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取上个季度的时间范围
     *
     * @return
     */
    public static DateRange getLastQuarter(Date date) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(date);
        startCalendar.set(Calendar.MONTH, ((int) startCalendar.get(Calendar.MONTH) / 3 - 1) * 3);
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        setMinTime(startCalendar);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(date);
        endCalendar.set(Calendar.MONTH, ((int) endCalendar.get(Calendar.MONTH) / 3 - 1) * 3 + 2);
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setMaxTime(endCalendar);
        return new DateRange(startCalendar.getTime(), endCalendar.getTime());
    }

    private static void setMinTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void setMaxTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));
    }

    public static void main(String[] args) throws Exception {
        System.out.print(getTimeInterval(new Date(1596340357000L), 2));
    }

    /**
     * 获取指定时间指定周(1+amount)的日期 yyyy-MM-dd
     *
     * @param date
     * @param amount
     * @return
     */
    public static String getTimeInterval(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        cal.add(Calendar.DATE, amount);
        String result = sdf_yyyy_MM_dd.format(cal.getTime());
        return result;
    }

    /**
     * 获取两个时间之间的所有月份及前一个月份
     *
     * @param minDate 开始时间
     * @param maxDate 结束时间
     * @return
     */
    public static List<String> getMonthListBetween(String minDate, String maxDate) throws ParseException {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        min.add(Calendar.MONTH, -1);
        min.setTime(sdf.parse(minDate));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(sdf.parse(maxDate));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }

        return result;

    }

    /****
     * 传入具体日期 ，返回具体日期减少一个月。
     * @param date
     * @return
     * @throws ParseException
     */
    public static String subMonth(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date dt = sdf.parse(date);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.MONTH, -1);
        Date dt1 = rightNow.getTime();
        String reStr = sdf.format(dt1);
        return reStr;

    }

    /****
     * 传入具体日期 ，返回具体日期增加一个月。
     * @param date
     * @return
     * @throws ParseException
     */
    public static String addMonth(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = sdf.parse(date);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.MONTH, 1);
        Date dt1 = rightNow.getTime();
        String reStr = sdf.format(dt1);
        return reStr;

    }
    /**
     * 加上1天
     * @param date
     * @return
     */
    public static String addDay(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = sdf.parse(date);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.DATE, 1);
        Date dt1 = rightNow.getTime();
        String reStr = sdf.format(dt1);
        return reStr;

    }

    /****
     * 传入具体日期 ，返回具体日期增加 或减少 n 个月。
     * @param date
     * @return
     * @throws ParseException
     */
    public static String addMonth(String date, int n) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date dt = sdf.parse(date);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.MONTH, n);
        Date dt1 = rightNow.getTime();
        String reStr = sdf.format(dt1);
        return reStr;

    }

    /**
     * 获取上一个月月份信息
     *
     * @param time yyyy-MM
     * @return
     * @throws ParseException
     */
    public static String subOnlyMonth(String time) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        if (StringUtils.isNotBlank(time)) {
            date = format.parse(time);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        date = calendar.getTime();
        String accDate = format.format(date);
        return accDate;
    }

    /**
     * 获取指定年份上一年最后一个月份
     *
     * @return
     * @throws ParseException
     */
    public static String subYearMonth(String year) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Date date = format.parse(year);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
        date = calendar.getTime();
        String accDate = format.format(date);
        return accDate + "-12";
    }

    /**
     * 获取去年的同一月
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static String subLastYearMonth(String time) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date date = format.parse(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
        date = calendar.getTime();
        String accDate = format.format(date);
        return accDate;
    }

    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayWeek = cal.get(7);
        if (1 == dayWeek) {
            cal.add(5, -1);
        }

        cal.setFirstDayOfWeek(2);
        int day = cal.get(7);
        cal.add(5, cal.getFirstDayOfWeek() - day);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        return cal.getTime();
    }
    public static Date getThisMonthDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

}

