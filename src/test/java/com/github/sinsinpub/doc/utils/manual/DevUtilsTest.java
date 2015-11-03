package com.github.sinsinpub.doc.utils.manual;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.sinsinpub.doc.web.i18n.MessageResources;
import com.jfinal.ext.kit.DateKit;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.Ret;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Model;

/**
 * 很多工具并不一定像我们想象那样工作.
 */
public class DevUtilsTest {

    @Test
    public void testStringFormat() {
        Date d = new Date();
        logger.info("ISO:  " + DateKit.formatIso(d));
        logger.info("Date: " + DateKit.formatDateOnly(d));
        logger.info("Date: " + DateKit.formatDateCompact(d));
        logger.info("Time: " + DateKit.formatTimeOnly(d));
        logger.info("Time: " + DateKit.formatTimeCompact(d));
        logger.info("US:   " + DateKit.format("ddMMMyy", d, Locale.US));
        logger.info("But common-lang3's ISO:   "
                + DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(d));
        logger.info("So our ISO is not actual: "
                + DateFormatUtils.format(d, "yyyy-MM-dd HH:mm:ss Z"));
    }

    @Test
    public void testMessageResources() {
        String str = "zh-CN variant";
        logger.info("String:    " + str);
        logger.info("Split:     " + Arrays.toString(StringUtils.splitPreserveAllTokens(str, "-_ ")));
        logger.info("Locale:    " + MessageResources.parseLocaleString(str));
        logger.info("ResBundle: "
                + MessageResources.format(str, "service.exception.userNotFound", new Date()));
    }

    @Test
    public void testJsonKit() throws Exception {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("key", "value");
        logger.info("Map.toJson():        " + JsonKit.toJson(map));

        // Fail, underlying Map not accessible
        Ret tuple = Ret.create("key", "value");
        logger.info("Ret.toJson():        " + JsonKit.toJson(tuple));

        Cat cat = new Cat();
        cat.put("key", new Timestamp(System.currentTimeMillis()));
        String catJson = cat.toJson();
        logger.info("Cat.toJson():        " + catJson);
        // The same
        logger.info("JsonKit.toJson(Cat): " + JsonKit.toJson(cat));

        Map<String, Object> catMap = (JSONObject) JSON.parse(catJson);
        catMap.put("newKey", new Timestamp(System.currentTimeMillis()));
        logger.info("Cat.Map:             " + catMap);
        cat.put(catMap);
        logger.info("Cat.toJson():        " + cat.toJson());
        // Real timestamp long will out
        logger.info("JSON.toJSONString(): "
                + JSON.toJSONString(FieldUtils.readField(cat, "attrs", true)));
        // The same with JsonKit
        logger.info("JSON.toJSONString(): "
                + JSON.toJSONString(FieldUtils.readField(cat, "attrs", true),
                        SerializerFeature.WriteDateUseDateFormat));
    }

    protected static final Logger logger = Logger.getLogger(DevUtilsTest.class);

    @SuppressWarnings("serial")
    public static class Cat extends Model<Cat> {
    }

    @Rule
    public TestName testName = new TestName();

    @Before
    public void before() {
        logger.info(String.format("/**** TESTING METHOD [%s] */", testName.getMethodName()));
    }

}
