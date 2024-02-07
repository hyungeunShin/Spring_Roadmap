package hello.http;

import java.time.Duration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		CacheControl cacheControl = CacheControl.maxAge(Duration.ofSeconds(10)).mustRevalidate();
		
		registry.addResourceHandler("/resources/**")
        	.addResourceLocations("classpath:/static/")
        	.setCacheControl(cacheControl);
	}
}
