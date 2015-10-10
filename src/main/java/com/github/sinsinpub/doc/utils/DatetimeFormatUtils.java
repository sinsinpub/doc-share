package com.github.sinsinpub.doc.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Rapid format date time with String.format instead of SimpleDateFormat.
 * <p>
 * String.format pattern details: <a
 * href="http://docs.oracle.com/javase/6/docs/api/java/util/Formatter.html#syntax">Formatter</a>
 * 
 * @see org.apache.commons.lang3.time.DateFormatUtils
 * @author sin_sin
 * @version $Date: Oct 8, 2015 $
 */
public abstract class DatetimeFormatUtils {

    private DatetimeFormatUtils() {
    }

    /**
     * String.format("%tF %tT %tz", ...
     * 
     * @param date
     * @return yyyy-MM-dd HH:mm:ss Z
     */
    public static final String formatIso(Date date) {
        return String.format("%tF %tT %tz", date, date, date);
    }

    /**
     * String.format("%tF", ...
     * 
     * @param date
     * @return yyyy-MM-dd
     */
    public static final String formatDateOnly(Date date) {
        return String.format("%tF", date);
    }

    /**
     * String.format("%tT", ...
     * 
     * @param date
     * @return HH:mm:ss
     */
    public static final String formatTimeOnly(Date date) {
        return String.format("%tT", date);
    }

    /**
     * String.format("%tY%tm%td", ...
     * 
     * @param date
     * @return yyyyMMdd
     */
    public static final String formatDateCompact(Date date) {
        return String.format("%tY%tm%td", date, date, date);
    }

    /**
     * String.format("%tH%tM%tS", ...
     * 
     * @param date
     * @return HHmmss
     */
    public static final String formatTimeCompact(Date date) {
        return String.format("%tH%tM%tS", date, date, date);
    }

    /**
     * A shortcut for SimpleDateFormat.
     * 
     * @param pattern
     * @param date
     * @return formatted
     */
    public static final String format(String pattern, Date date) {
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * A shortcut for SimpleDateFormat with locale.
     * 
     * @param pattern
     * @param date
     * @param locale
     * @return formatted
     */
    public static final String format(String pattern, Date date, Locale locale) {
        return new SimpleDateFormat(pattern, locale).format(date);
    }

}
