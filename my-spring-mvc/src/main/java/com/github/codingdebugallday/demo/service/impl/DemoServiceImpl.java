package com.github.codingdebugallday.demo.service.impl;

import com.github.codingdebugallday.demo.service.DemoService;
import com.github.codingdebugallday.mvcframework.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/13 21:08
 * @since 1.0.0
 */
@Service
public class DemoServiceImpl implements DemoService {

    private final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Override
    public String get(String name) {
        logger.debug("service 实现类中name参数: {}", name);
        return name;
    }
}
