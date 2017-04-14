package com.example;

import com.example.filter.SessionFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;

@SpringBootApplication
@MapperScan("com.example.mapper")
public class UcenterApplication {


	public static void main(String[] args) {
		SpringApplication.run(UcenterApplication.class, args);
	}


	/**
	 * 登陆过滤器
	 * @return
	 */
	@Bean
	public FilterRegistrationBean someFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(sessionFilter());
		registration.setName("sessionFilter");
		registration.setOrder(Integer.MIN_VALUE);
		return registration;
	}

	@Bean(name = "sessionFilter")
	public Filter sessionFilter() {
		return new SessionFilter();
	}

}
