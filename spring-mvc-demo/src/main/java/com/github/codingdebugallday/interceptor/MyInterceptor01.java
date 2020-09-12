package com.github.codingdebugallday.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/12 23:48
 * @since 1.0.0
 */
public class MyInterceptor01 implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(MyInterceptor01.class);

    /**
     * 会在handler方法业务逻辑执行之前执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("MyInterceptor01 preHandle......");
        // 放行
        return true;
    }

    /**
     * 会在handler方法业务逻辑执行之后，尚未跳转页面时执行
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.debug("MyInterceptor01 postHandle......");
    }

    /**
     * 页面已经渲染完毕之后执行
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.debug("MyInterceptor01 afterCompletion......");
    }
}
