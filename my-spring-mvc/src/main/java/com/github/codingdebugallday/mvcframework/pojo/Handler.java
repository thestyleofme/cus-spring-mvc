package com.github.codingdebugallday.mvcframework.pojo;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * <p>
 * handler方法封装
 * </p>
 *
 * @author isaac 2020/9/14 2:56
 * @since 1.0.0
 */
public class Handler {

    private Method method;
    /**
     * 这里的controller就是method.invoke(obj, args)中的obj
     */
    private Object controller;
    /**
     * spring中的url是支持正则的
     */
    private Pattern pattern;
    /**
     * method的参数，为了做参数绑定
     * key：参数名
     * value：参数的顺序
     */
    private Map<String, Integer> paramsIndexMapping;

    public Handler(Object controller,Method method, Pattern pattern) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
        this.paramsIndexMapping = new HashMap<>();
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Map<String, Integer> getParamsIndexMapping() {
        return paramsIndexMapping;
    }

    public void setParamsIndexMapping(Map<String, Integer> paramsIndexMapping) {
        this.paramsIndexMapping = paramsIndexMapping;
    }
}
