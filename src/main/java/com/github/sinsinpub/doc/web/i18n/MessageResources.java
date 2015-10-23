package com.github.sinsinpub.doc.web.i18n;

import java.text.MessageFormat;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.github.sinsinpub.doc.hint.Utilities;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;

/**
 * 测试一下后台i18n消息的提取、格式化. JFinal给的实现简陋得有点怕怕.
 * <p>
 * 顺便建议处理i18n properties文件用专用编辑插件ResourceBundle Editor.
 * 
 * @author sin_sin
 * @version $Date: Oct 12, 2015 $
 */
@Utilities
public abstract class MessageResources {

    public static final String I18N_BASENAME = "com.github.sinsinpub.doc.web.i18n.messages";

    /**
     * Get the message value from ResourceBundle of the related Locale.
     * 
     * @param locale locale instance
     * @param key message key
     * @return message value
     */
    public static String get(Locale locale, String key) {
        final Res res = I18n.use(I18N_BASENAME, locale.toString());
        return res.get(key);
    }

    /**
     * Get the message value from ResourceBundle of the related Locale.
     * 
     * @param locale locale string
     * @param key message key
     * @return message value
     */
    public static String get(String locale, String key) {
        return get(parseLocaleString(locale), key);
    }

    /**
     * Get the message value from ResourceBundle by the key then format with the arguments.
     * <p>
     * Example: In resource file : msg=Hello {0}, today is {1}.<br>
     * In Java code : res.format("msg", "james", new Date());<br>
     * The result is : Hello james, today is 15-04-14. (?)
     * 
     * @param locale locale instance
     * @param key message key
     * @param arguments arguments
     * @return message value
     */
    public static String format(Locale locale, String key, Object... arguments) {
        final Res res = I18n.use(I18N_BASENAME, locale.toString());
        // Res的实现明显有问题。MessageFormat不用Locale初始化，那格式化个毛的i18n
        return new MessageFormat(res.get(key), locale).format(arguments);
    }

    /**
     * Get the message value from ResourceBundle by the key then format with the arguments.
     * <p>
     * Example: In resource file : msg=Hello {0}, today is{1}.<br>
     * In java code : res.format("msg", "james", new Date());<br>
     * The result is : Hello james, today is 2015-04-14.
     * 
     * @param locale locale string
     * @param key message key
     * @param arguments arguments
     * @return message value
     */
    public static String format(String locale, String key, Object... arguments) {
        return format(parseLocaleString(locale), key, arguments);
    }

    /**
     * Parse the given <code>localeString</code> into a {@link Locale}.
     * <p>
     * This is the inverse operation of {@link Locale#toString Locale's toString}.
     * 
     * @param localeString the locale string, following <code>Locale's</code> <code>toString()</code>
     *            format ("en", "en_UK", etc); also accepts spaces as separators, as an alternative
     *            to underscores
     * @return a corresponding <code>Locale</code> instance
     */
    public static Locale parseLocaleString(String localeString) {
        final String delims = "-_ ";
        String[] parts = StringUtils.splitPreserveAllTokens(localeString, delims);
        String language = (parts.length > 0 ? parts[0] : StringUtils.EMPTY);
        String country = (parts.length > 1 ? parts[1] : StringUtils.EMPTY);
        String variant = StringUtils.EMPTY;
        if (parts.length >= 2) {
            // There is definitely a variant, and it is everything after the country code sans the
            // separator between the country code and the variant.
            int endIndexOfCountryCode = localeString.indexOf(country) + country.length();
            // Strip off any leading '_', '-' and whitespace, what's left is the variant.
            variant = StringUtils.stripStart(localeString.substring(endIndexOfCountryCode), delims);
        }
        return (language.length() > 0 ? new Locale(language, country, variant) : null);
    }

}
