package com.rearwave.blog.config;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.rearwave.blog.component.spring.handler.BlogArgumentMethodResolver;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * web程序的核心配置，配置拦截器、解析器、序列化工具等
 * @author sunyi
 */
@Log4j2
@Configuration
public class RearWaveApplicationConfigure implements WebMvcConfigurer {


    /**
     * 添加自定义拦截器
     * @param registry
     */

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

    }

    /**
     * 添加自定义参数解析器
     * @param argumentResolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        /* 添加登录相关接口参数解析器 */
        argumentResolvers.add(new BlogArgumentMethodResolver());
    }

    /**
     * 自定义返回数据处理器
     * @param handlers
     */
    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {

    }

    /**
     * 跨域支持
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("GET","POST","OPTIONS")
                .exposedHeaders("access-control-allow-headers",
                        "access-control-allow-methods",
                        "access-control-allow-origin",
                        "access-control-max-age",
                        "X-Frame-Options")
                .allowCredentials(false)
                .maxAge(3600);
    }

    /**
     * 自定义消息序列化工具 本项目使用Gson作为序列化工具
     * @param converters 消息转换器
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
        gsonHttpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);
        //设置支持的媒体类型
        gsonHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON
                ,MediaType.APPLICATION_FORM_URLENCODED,MediaType.TEXT_PLAIN));

       converters.add(gsonHttpMessageConverter);
    }

    @Bean
    public DefaultKaptcha getDefaultKaptcha(){
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "yes");
        properties.setProperty("kaptcha.border.color", "gray");
        properties.setProperty("kaptcha.textproducer.font.color", "orange");
        properties.setProperty("kaptcha.image.width", "120");
        properties.setProperty("kaptcha.image.height", "39");
        properties.setProperty("kaptcha.noise.color","white");
        properties.setProperty("kaptcha.textproducer.font.size", "30");
        properties.setProperty("kaptcha.session.key", "code");
        properties.setProperty("kaptcha.textproducer.char.length", "6");
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }


    @Bean
    @ConditionalOnClass(PaginationInterceptor.class)
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }
}
