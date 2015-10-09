package com.github.sinsinpub.doc.web.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.render.RenderFactory;
import com.jfinal.validate.Validator;

/**
 * 强行把校验结果用JSON渲染的扩展抽象验证器.
 * 
 * @author sin_sin
 * @version $Date: Oct 9, 2015 $
 */
public abstract class JsonResponseValidator extends Validator {

    protected static final String ERROR_KEY_PREFIX = "validator.";

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
        c.renderError(isResponseStatusOk() ? 200 : 400, RenderFactory.me().getJsonRender(errorMsgs));
    }

    protected boolean isResponseStatusOk() {
        return false;
    }

    protected void validateRequiredStringValue(String str, String errorKey, String errorMessage) {
        if (StrKit.isBlank(str))
            addError(errorKey, errorMessage);
    }

}
