package com.github.sinsinpub.doc.web.controller.base;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.github.sinsinpub.doc.hint.Prototype;
import com.github.sinsinpub.doc.web.interceptor.JsonExceptionRenderer;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.jfinal.validate.Validator;

/**
 * 强行把校验结果用JSON渲染的扩展抽象验证器.
 * 
 * @author sin_sin
 * @version $Date: Oct 9, 2015 $
 */
@Prototype
public abstract class JsonResponseValidator extends Validator {

    protected static final String ERROR_KEY_PREFIX = "validator.";
    protected final Logger logger = Logger.getLogger(getClass());

    @Override
    protected void handleError(Controller c) {
        Map<String, String> errorMsgs = new HashMap<String, String>();
        errorMsgs.put(JsonExceptionRenderer.ERROR_JSON_KEY, "Bad request parameters by validator");
        Enumeration<String> attrNames = c.getAttrNames();
        while (attrNames.hasMoreElements()) {
            String attr = attrNames.nextElement();
            if (attr.startsWith(ERROR_KEY_PREFIX)) {
                errorMsgs.put(attr, c.getAttrForStr(attr));
            }
        }
        logger.info("Validator reports: " + JsonKit.toJson(errorMsgs));
        if (!isResponseStatusOk()) {
            c.getResponse().setStatus(400);
        }
        c.renderJson(errorMsgs);
        return;
    }

    protected boolean isResponseStatusOk() {
        return false;
    }

    protected void validateRequiredStringValue(String str, String errorKey, String errorMessage) {
        if (StrKit.isBlank(str)) {
            addError(errorKey, errorMessage);
        }
    }

}
