package com.github.codingdebugallday.mvcframework.pojo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    /**
     * 自定义功能，跟自定义springmvc框架无关
     * 定义注解@Security（有value属性，接收String数组）
     * 该注解用于添加在Controller类或者Handler方法上，表明哪些用户拥有访问该Handler方法的权限（注解配置用户名）
     */
    private List<String> securityUserList;

    public Handler(Object controller,Method method, Pattern pattern) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
        this.paramsIndexMapping = new HashMap<>();
        this.securityUserList = new ArrayList<>();
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

    public List<String> getSecurityUserList() {
        return securityUserList;
    }

    public void setSecurityUserList(List<String> securityUserList) {
        this.securityUserList = securityUserList;
    }
}
