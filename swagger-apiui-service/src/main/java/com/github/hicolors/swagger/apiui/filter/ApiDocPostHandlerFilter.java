package com.github.hicolors.swagger.apiui.filter;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.util.StreamUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * 文档接口path处理
 */
public class ApiDocPostHandlerFilter extends ZuulFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiDocPostHandlerFilter.class);
	
	@Autowired
	private RouteLocator routeLocator;

	@Override
	public Object run() throws ZuulException {
		RequestContext context = RequestContext.getCurrentContext();
		Route route = routeLocator.getMatchingRoute(context.getRequest().getRequestURI().replaceAll(context.getRequest().getContextPath(), ""));
		if(route==null) return null;
        try {
        	String body = "";
        	InputStream stream = context.getResponseDataStream();
        	if(context.getResponseGZipped()){
        		GZIPInputStream gzipIn = new GZIPInputStream(stream);
        		body = StreamUtils.copyToString(gzipIn, Charset.forName("UTF-8"));
        	}else{
        		body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
        	}
            if(StringUtils.isNotBlank(body) && body.startsWith("{") && body.endsWith("}")){
            	Gson gson = new Gson();
            	Map<String,Object> map = gson.fromJson(body, new TypeToken<Map<String,Object>>(){}.getType());
            	if(map.containsKey("swagger")){
            		map.put("basePath", context.getRequest().getContextPath()+route.getPrefix());
            		context.setResponseBody(gson.toJson(map));
            		context.getResponse().setContentType("application/json;charset=utf-8");
            	}else{
            		context.setResponseBody(body);
            		context.getResponse().setContentType("application/json;charset=utf-8");
            	}
            }else{
            	context.setResponseBody(body);
        		context.getResponse().setContentType("application/json;charset=utf-8");
            }
        }
        catch (IOException e) {
        	logger.error("ERROR::",e);
        }
		return null;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public int filterOrder() {
		return 999;
	}

	@Override
	public String filterType() {
		return "post";
	}

}
