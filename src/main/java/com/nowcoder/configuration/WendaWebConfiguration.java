package com.nowcoder.configuration;

import com.nowcoder.Interceptor.LoginRequredInterceptor;
import com.nowcoder.Interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by LZF on 2017/6/7.
 */
@Component
public class WendaWebConfiguration extends WebMvcConfigurerAdapter{

    @Autowired
    PassportInterceptor passportInterceptor;
    @Autowired
    LoginRequredInterceptor loginRequredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(passportInterceptor);//拦截器添加顺序表明其优先级,优先级越高越先添加
        registry.addInterceptor(loginRequredInterceptor).addPathPatterns("/user/*"); //表明访问/user/*页面的时候 经过这个拦截器
        super.addInterceptors(registry);
    }
}
