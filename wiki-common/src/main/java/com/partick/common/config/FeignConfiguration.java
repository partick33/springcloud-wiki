package com.partick.common.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author partick_peng
 */
@Configuration
public class FeignConfiguration implements RequestInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(FeignConfiguration.class);

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes==null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("token");
        if (token == null) {
            LOG.info("token为空，请求被拦截");
            return;
        }
        requestTemplate.header("token", token);
    }
}
