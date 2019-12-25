package com.github.hicolors.swagger.apiui.config;

import com.github.hicolors.swagger.apiui.filter.ApiDocPostHandlerFilter;
import com.github.hicolors.swagger.apiui.filter.ApiDocPreHandlerFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZuulFilterConfiguration {
	
	@Bean
	public ApiDocPreHandlerFilter apiDocPreHandlerFilter(){
		return new ApiDocPreHandlerFilter();
	}

	@Bean
	public ApiDocPostHandlerFilter apiDocHandlerFilter(){
		return new ApiDocPostHandlerFilter();
	}
}
