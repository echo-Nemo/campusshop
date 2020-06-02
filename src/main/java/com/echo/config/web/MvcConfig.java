package com.echo.config.web;

import com.echo.inteceptor.ShopLoginInteceptor;
import com.echo.inteceptor.ShopPermissionInteceptor;
import org.springframework.beans.BeansException;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


@Configuration
//开启springmvc注解模式
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer, ApplicationContextAware {


    //spring容器
    private ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    //开始页面的设置
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/templates/html/frontend/index.html");
        //设置过滤的优先级最高
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    //静态资源的处理
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //图片的处理
        //windows下的图片路径配置
        String os = System.getProperty("os.name");
        String basePath = "";
        if (os.toLowerCase().startsWith("win")) {
            registry.addResourceHandler("/upload/**").addResourceLocations("file:/javaEE/ShopImgs/upload/");
        } else {
            //linux下的图片路径配置
            registry.addResourceHandler("/upload/**").addResourceLocations("file:/home/ShopImg/Imgs/upload/");
        }

        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("/templates/**")
                .addResourceLocations("classpath:/templates/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    //定义默认的请求处理器
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    //创建视图解析器
    @Bean("viewResolver")
    public ViewResolver createViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        //设置spring 容器
        viewResolver.setApplicationContext(this.applicationContext);
        //取消缓存
        viewResolver.setCache(false);
        //设置解析的前缀
        viewResolver.setPrefix("/templates/html/");
        viewResolver.setSuffix(".html");
        return viewResolver;
    }


    //显示声明CommonsMultipartResolver为mutipartResolver
    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        resolver.setResolveLazily(true);//resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
        resolver.setMaxInMemorySize(40960);
        resolver.setMaxUploadSize(50 * 1024 * 1024);//上传文件大小 50M 50*1024*1024
        return resolver;
    }

    //拦截器的设置
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*
        店家管理系统拦截的部分
         */
        String interceptPath = "/shop/**";

        //注册拦截器拦截所有
        InterceptorRegistration loginIR = registry.addInterceptor(new ShopLoginInteceptor());
        //配置拦截的路径
        loginIR.addPathPatterns(interceptPath);

        //对不是该店家的店铺进行拦截
        InterceptorRegistration permissionIR = registry.addInterceptor(new ShopPermissionInteceptor());

        //配置拦截的路径
        permissionIR.addPathPatterns(interceptPath);

        //配置不拦截的路径

        //店铺的注册和初始化页面
        permissionIR.excludePathPatterns("/shop/registershop");
        permissionIR.excludePathPatterns("/shop/getshopinitfo");
        permissionIR.excludePathPatterns("/shop/shopoperation");
        //管理员不用拦截
        permissionIR.excludePathPatterns("/shop/admshoplist");
        permissionIR.excludePathPatterns("/shop/modifyadminshop");

        //获取所有店铺的页面
        permissionIR.excludePathPatterns("/shop/getshoplist");
        permissionIR.excludePathPatterns("/shop/shoplist");

        //奖品兑换的页面
        permissionIR.excludePathPatterns("http://www.daisyecho.cn/shop/exchangeaward");

        //商铺区域的获取
        permissionIR.excludePathPatterns("/shop/getareas");
        permissionIR.excludePathPatterns("/shop/addarea");

        //进入店铺的管理页面
        permissionIR.excludePathPatterns("/shop/shopmanagementinfo");
        permissionIR.excludePathPatterns("/shop/shopmanagement");


    }
}
