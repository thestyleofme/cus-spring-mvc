package com.github.codingdebugallday.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.github.codingdebugallday.pojo.QueryVo;
import com.github.codingdebugallday.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/12 1:33
 * @since 1.0.0
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    private final Logger logger = LoggerFactory.getLogger(DemoController.class);

    // SpringMVC的异常处理机制（异常处理器）
    // 注意：写在这里只会对当前controller类生效
    /*@ExceptionHandler(ArithmeticException.class)
    public void handleException(ArithmeticException exception, HttpServletResponse response) {
        // 异常处理逻辑
        try {
            response.getWriter().write(exception.getMessage());
        } catch (IOException e) {
            logger.error("IOException", e);
        }
    }*/

    /**
     * SpringMVC在handler方法上传入Map、Model和ModelMap参数，并向这些参数中保存数据（放入到请求域），都可以在页面获取到
     * <p>
     * 它们之间是什么关系？
     * 运行时的具体类型都是BindingAwareModelMap，相当于给BindingAwareModelMap中保存的数据都会放在请求域中
     * <p>
     * Map(jdk中的接口)        Model（spring的接口）
     * <p>
     * ModelMap(class,实现Map接口)
     * <p>
     * BindingAwareModelMap继承了ExtendedModelMap，ExtendedModelMap继承了ModelMap,实现了Model接口
     */
    @RequestMapping("/handle01")
    public ModelAndView handle01(@ModelAttribute("name") String name) {
        // int c = 1/0;
        LocalDateTime localDateTime = LocalDateTime.now();
        // 封装了数据和页面信息的 ModelAndView
        ModelAndView modelAndView = new ModelAndView();
        // addObject 其实是向请求域中request.setAttribute("date",date)
        modelAndView.addObject("date", localDateTime);
        // 视图信息(封装跳转的页面信息) 逻辑视图名
        modelAndView.setViewName("success");
        return modelAndView;
    }

    /*@RequestMapping("/handle01")
    public ModelAndView handle01(ModelAndView modelAndView) {
        LocalDateTime localDateTime = LocalDateTime.now();
        modelAndView.addObject("date", localDateTime);
        // ======modelAndView: class org.springframework.web.servlet.ModelAndView
        System.out.println("======modelAndView: "+modelAndView.getClass());
        modelAndView.setViewName("success");
        return modelAndView;
    }

    @RequestMapping("/handle01")
    public String handle01(ModelMap modelMap) {
        LocalDateTime localDateTime = LocalDateTime.now();
        modelMap.addAttribute("date", localDateTime);
        // ======modelMap: class org.springframework.validation.support.BindingAwareModelMap
        System.out.println("======modelMap: "+modelMap.getClass());
        return "success";
    }

    @RequestMapping("/handle01")
    public String handle01(Model model) {
        LocalDateTime localDateTime = LocalDateTime.now();
        model.addAttribute("date", localDateTime);
        // ======model: class org.springframework.validation.support.BindingAwareModelMap
        System.out.println("======model: "+model.getClass());
        return "success";
    }

    @RequestMapping("/handle01")
    public String handle01(Map<String, Object> map) {
        LocalDateTime localDateTime = LocalDateTime.now();
        // ======map: class org.springframework.validation.support.BindingAwareModelMap
        System.out.println("======map: "+map.getClass());
        map.put("date", localDateTime);
        return "success";
    }*/


    @RequestMapping("/handle02")
    public ModelAndView handle02(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        return justTest();
    }

    @RequestMapping("/handle03")
    public ModelAndView handle03(@RequestParam("id") Integer id, Boolean flag) {
        return justTest();
    }

    @RequestMapping("/handle04")
    public ModelAndView handle04(User user) {
        return justTest();
    }

    /**
     * url：/demo/handle05?user.id=1&user.username=zhangsan
     */
    @RequestMapping("/handle05")
    public ModelAndView handle05(QueryVo queryVo) {
        return justTest();
    }

    /**
     * 绑定日期类型参数
     * 定义一个SpringMVC的类型转换器  接口，扩展实现接口接口，注册你的实现
     */
    @RequestMapping("/handle06")
    public ModelAndView handle06(Date birthday) {
        return justTest();
    }

    @GetMapping(value = "/handle/{id}")
    public ModelAndView handleGet(@PathVariable("id") Integer id) {
        return justTest();
    }

    @PostMapping(value = "/handle")
    public ModelAndView handlePost(String username) {
        return justTest();
    }

    @PutMapping(value = "/handle/{id}/{name}")
    public ModelAndView handlePut(@PathVariable("id") Integer id, @PathVariable("name") String username) {
        return redirectTest();
    }

    @DeleteMapping(value = "/handle/{id}")
    public ModelAndView handleDelete(@PathVariable("id") Integer id) {
        return redirectTest();
    }

    @RequestMapping(value = "/success")
    public ModelAndView toSuccess() {
        return justTest();
    }

    private ModelAndView redirectTest() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("date", LocalDateTime.now());
        // fix tomcat8/9 not support put/delete method, only tomcat7 support
        modelAndView.setViewName("redirect:success");
        return modelAndView;
    }

    private ModelAndView justTest() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("date", LocalDateTime.now());
        modelAndView.setViewName("success");
        return modelAndView;
    }

    /**
     * 添加@ResponseBody之后，不再走视图解析器那个流程，而是等同于response直接输出数据
     */
    @PostMapping(value = "/handle07")
    @ResponseBody
    public User handle07(@RequestBody User user) {
        user.setName("张三丰");
        return user;
    }

    @PostMapping(value = "/upload")
    public ModelAndView upload(MultipartFile uploadFile, HttpSession session) throws IOException {
        if (uploadFile.isEmpty()) {
            throw new IllegalStateException("file is empty");
        }
        // 处理上传文件 重命名
        // 原始名称
        String originalFilename = uploadFile.getOriginalFilename();
        // 扩展名  jpg
        assert originalFilename != null;
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String newName = UUID.randomUUID().toString() + "." + ext;

        // 存储,要存储到指定的文件夹，/uploads/yyyy-MM-dd，考虑文件过多的情况按照日期，生成一个子文件夹
        String realPath = session.getServletContext().getRealPath("/uploads");
        String datePath = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        File folder = new File(realPath + File.separator + datePath);

        if (!folder.exists() && folder.mkdirs()) {
            throw new IOException("mkdirs error");
        }

        /*
         * 存储文件到目录
         * idea使用tomcat启动
         * 路径为out/artifacts/spring_mvc_demo_war_exploded/uploads/2020-09-13/uuid.txt
         */
        uploadFile.transferTo(new File(folder, newName));

        // todo 文件磁盘路径要更新到数据库字段
        return justTest();
    }

    /**
     * SpringMVC 重定向时参数传递的问题
     * 转发：A 找 B 借钱400，B没有钱但是悄悄的找到C借了400块钱给A
     *      url不会变,参数也不会丢失,一个请求
     * 重定向：A 找 B 借钱400，B 说我没有钱，你找别人借去，那么A 又带着400块的借钱需求找到C
     *      url会变,参数会丢失需要重新携带参数,两个请求
     */
    @RequestMapping("/handleRedirect")
    public String handleRedirect(String name, RedirectAttributes redirectAttributes) {
        // 拼接参数安全性、参数长度都有局限
        // return "redirect:handle01?name=" + name
        // addFlashAttribute方法设置了一个flash类型属性，该属性会被暂存到session中，在跳转到页面之后该属性销毁
        redirectAttributes.addFlashAttribute("name",name);
        return "redirect:handle01";

    }
}
