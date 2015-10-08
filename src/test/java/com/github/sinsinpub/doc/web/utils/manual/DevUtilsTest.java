package com.github.sinsinpub.doc.web.utils.manual;

import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.github.sinsinpub.doc.utils.DatetimeFormatUtils;
import com.jfinal.log.Logger;

public class DevUtilsTest {

    private static final Logger logger = Logger.getLogger(DevUtilsTest.class);

    @Test
    public void testStringFormat() {
        Date d = new Date();
        logger.info("ISO:  " + DatetimeFormatUtils.formatIso(d));
        logger.info("Date: " + DatetimeFormatUtils.formatDateOnly(d));
        logger.info("Date: " + DatetimeFormatUtils.formatDateCompact(d));
        logger.info("Time: " + DatetimeFormatUtils.formatTimeOnly(d));
        logger.info("Time: " + DatetimeFormatUtils.formatTimeCompact(d));
        logger.info("US:   " + DatetimeFormatUtils.format("ddMMMyy", d, Locale.US));
    }

}
