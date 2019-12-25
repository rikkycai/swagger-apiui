package com.github.hicolors.swagger.apiui;

import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({SwaggerButlerAutoConfig.class})
@EnableZuulProxy
public @interface EnableSwaggerButler {
}
