package com.github.codingdebugallday.mvcframework.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.codingdebugallday.mvcframework.annotations.AutoWired;
import com.github.codingdebugallday.mvcframework.annotations.Controller;
import com.github.codingdebugallday.mvcframework.annotations.RequestMapping;
import com.github.codingdebugallday.mvcframework.annotations.Service;
import com.github.codingdebugallday.mvcframework.pojo.Handler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/13 20:07
 * @since 1.0.0
 */
public class DispatcherServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    /**
     * spring mvc配置文件springmvc.properties
     */
    private final Properties properties = new Properties();
    /**
     * 存放扫描到的类的全限定类名
     */
    private final List<String> classNames = new ArrayList<>();
    /**
     * 声明IOC容器
     */
    private final Map<String, Object> ioc = new HashMap<>();
    /**
     * url method
     */
    // private final Map<String, Method> handlerMapping = new HashMap<>()

    private final List<Handler> handlerMapping = new ArrayList<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        // 1 加载配置文件 springmvc.properties
        String contextConfigLocation = config.getInitParameter("contextConfigLocation");
        doLoadConfig(contextConfigLocation);
        // 2 扫描相关的类，扫描注解
        doScan(properties.getProperty("scanPackage"));
        // 3 初始化bean对象（实现ioc容器，基于注解）
        doInstance();
        // 4 实现依赖注入
        doAutoWired();
        // 5 构造一个HandlerMapping处理器映射器，将配置好的url和method建立映射关系
        initHandlerMapping();
        logger.debug("my spring mvc 初始化完成......");
    }

    private void doLoadConfig(String contextConfigLocation) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("load springmvc.properties error", e);
        }
    }

    private void doScan(String scanPackage) {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("");
        String classpath = Objects.requireNonNull(resource).getPath();
        String scanPackagePath = classpath + scanPackage.replaceAll("\\.", "/");
        File packageFile = new File(scanPackagePath);
        for (File file : Objects.requireNonNull(packageFile.listFiles())) {
            if (file.isDirectory()) {
                doScan(scanPackage + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String className = scanPackage + "." + file.getName().replace(".class", "");
                classNames.add(className);
            }
        }
    }

    private void doInstance() {
        if (classNames.isEmpty()) {
            return;
        }
        for (String className : classNames) {
            try {
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(Controller.class)) {
                    String beanName = toLowerFirstCase(clazz.getSimpleName());
                    Object instance = Thread.currentThread()
                            .getContextClassLoader()
                            .loadClass(clazz.getName())
                            .getDeclaredConstructor()
                            .newInstance();
                    ioc.put(beanName, instance);
                } else if (clazz.isAnnotationPresent(Service.class)) {
                    Service serviceAnnotation = clazz.getAnnotation(Service.class);
                    String beanName = serviceAnnotation.value().trim();
                    if ("".equals(beanName)) {
                        beanName = toLowerFirstCase(clazz.getSimpleName());
                    }
                    Object instance = Thread.currentThread()
                            .getContextClassLoader()
                            .loadClass(clazz.getName())
                            .getDeclaredConstructor()
                            .newInstance();
                    ioc.put(beanName, instance);
                    // service层往往是有接口的，面向接口开发，此时再以接口名为id，放入一份对象到ioc中，便于后期根据接口类型注入
                    for (Class<?> interfaceClazz : clazz.getInterfaces()) {
                        String name = interfaceClazz.getName();
                        if (ioc.containsKey(name)) {
                            throw new Exception("The bean name is exists!");
                        }
                        ioc.put(name, instance);
                    }
                }
            } catch (Exception e) {
                logger.error("doInstance error", e);
            }
        }
    }

    private String toLowerFirstCase(String simpleName) {
        final char[] chars = simpleName.toCharArray();
        if (chars[0] >= 'A' && chars[0] <= 'Z') {
            chars[0] += 32;
        }
        return String.valueOf(chars);
    }

    private void doAutoWired() {
        if (ioc.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            try {
                Field[] fields = entry.getValue().getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(AutoWired.class)) {
                        AutoWired autoWired = field.getAnnotation(AutoWired.class);
                        String beanName = autoWired.value().trim();
                        if ("".equals(beanName)) {
                            beanName = field.getType().getName();
                        }
                        field.setAccessible(true);
                        field.set(entry.getValue(), ioc.get(beanName));
                    }
                }
            } catch (Exception e) {
                logger.error("doAutoWired error", e);
            }
        }
    }

    private void initHandlerMapping() {
        if (ioc.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            final Class<?> clazz = entry.getValue().getClass();
            if (clazz.isAnnotationPresent(Controller.class)) {
                String baseUrl = "";
                if (clazz.isAnnotationPresent(RequestMapping.class)) {
                    baseUrl = clazz.getAnnotation(RequestMapping.class).value();
                }
                for (Method method : clazz.getMethods()) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        String url = ("/" + baseUrl + "/" + method.getAnnotation(RequestMapping.class).value()).replaceAll("/+", "/");
                        // 把method所有信息以及url封装为Handler
                        Handler handler = new Handler(entry.getValue(), method, Pattern.compile(url));
                        // 处理参数
                        Map<String, Integer> paramsIndexMapping = handler.getParamsIndexMapping();
                        Parameter[] parameters = method.getParameters();
                        for (int i = 0, size = parameters.length; i < size; i++) {
                            Parameter parameter = parameters[i];
                            Class<?> parameterType = parameter.getType();
                            // 如果是HttpServletRequest或HttpServletResponse 参数名为HttpServletRequest或HttpServletResponse
                            if (parameterType == HttpServletRequest.class || parameterType == HttpServletResponse.class) {
                                paramsIndexMapping.put(parameterType.getSimpleName(), i);
                            } else {
                                // 其他的普通类型
                                // 为了拿到形参名 idea需要设置java compile 加上 -parameters
                                // https://stackoverflow.com/questions/39217830/how-to-use-parameters-javac-option-in-intellij
                                // 或者使用maven-compiler-plugin插件设置编译参数
                                paramsIndexMapping.put(parameter.getName(), i);
                            }
                        }
                        handlerMapping.add(handler);
                    }
                }
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        doDispatch(req, resp);
    }

    private void doDispatch(HttpServletRequest request, HttpServletResponse response) {
        Handler handler = getHandlerByHttpServletRequest(request);
        if (handler == null) {
            try {
                response.getWriter().write("404 not found!");
            } catch (IOException e) {
                logger.error("doDispatch error", e);
            }
            return;
        }
        // 以下是参数绑定过程
        Method method = handler.getMethod();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Map<String, Integer> paramsIndexMapping = handler.getParamsIndexMapping();
        // 根据上述参数类型数组创建这里方法调用的参数数组
        Object[] args = new Object[parameterTypes.length];
        // 前台传的参数 （填充的是普通参数）
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> param : parameterMap.entrySet()) {
            if (paramsIndexMapping.containsKey(param.getKey())) {
                // name=1&name=2 => 1,2
                String value = StringUtils.join(param.getValue(), ",");
                // 找到参数位置并赋值到args
                Integer index = paramsIndexMapping.get(param.getKey());
                args[index] = value;
            }
        }
        // 对HttpServletRequest和HttpServletResponse 单独处理
        if (paramsIndexMapping.containsKey(HttpServletRequest.class.getSimpleName())) {
            Integer requestIndex = paramsIndexMapping.get(HttpServletRequest.class.getSimpleName());
            args[requestIndex] = request;
        }
        if (paramsIndexMapping.containsKey(HttpServletResponse.class.getSimpleName())) {
            Integer responseIndex = paramsIndexMapping.get(HttpServletResponse.class.getSimpleName());
            args[responseIndex] = response;
        }
        // 调用方法
        try {
            Object ret = method.invoke(handler.getController(), args);
            response.getWriter().write("response: " + ret);
        } catch (IOException | IllegalAccessException | InvocationTargetException e) {
            logger.error("doDispatch error", e);
        }
    }

    private Handler getHandlerByHttpServletRequest(HttpServletRequest request) {
        if (handlerMapping.isEmpty()) {
            return null;
        }
        String url = request.getRequestURI();
        for (Handler handler : handlerMapping) {
            Matcher matcher = handler.getPattern().matcher(url);
            if (matcher.matches()) {
                return handler;
            }
        }
        return null;
    }
}
