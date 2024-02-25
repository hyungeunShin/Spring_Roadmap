package hello.itemservice;

import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import hello.itemservice.web.argumentresolver.LoginMemberArgumentResolver;
import hello.itemservice.web.filter.LogFilter;
import hello.itemservice.web.filter.LoginCheckFilter;
import hello.itemservice.web.interceptor.LogInterceptor;
import hello.itemservice.web.interceptor.LoginCheckInterceptor;
import jakarta.servlet.Filter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	//@Bean
	public FilterRegistrationBean<Filter> logFilter() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(new LogFilter());
		filterRegistrationBean.setOrder(1);
		filterRegistrationBean.addUrlPatterns("/*");

		return filterRegistrationBean;
	}

	//@Bean
	public FilterRegistrationBean<Filter> loginCheckFilter() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(new LoginCheckFilter());
		filterRegistrationBean.setOrder(2);
		filterRegistrationBean.addUrlPatterns("/*");
		
		return filterRegistrationBean;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LogInterceptor())
			.order(1)
			.addPathPatterns("/**")
			.excludePathPatterns("/css/**", "/*.ico", "/error");
		
		registry.addInterceptor(new LoginCheckInterceptor())
			.order(2)
			.addPathPatterns("/**")
			.excludePathPatterns("/", "/members/add", "/login", "/logout",
					"/css/**", "/*.ico", "/error");
	}
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new LoginMemberArgumentResolver());
	}
}
