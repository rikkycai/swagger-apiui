package com.github.hicolors.swagger.apiui.filter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

public class ApiDocPreHandlerFilter extends ZuulFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiDocPreHandlerFilter.class);

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext context = RequestContext.getCurrentContext();
		logger.info("zuul request headers=={}",context.getZuulRequestHeaders());
		String hostfor = context.getZuulRequestHeaders().get("x-forwarded-host");
		String protofor = context.getZuulRequestHeaders().get("x-forwarded-proto");
		String portfor = context.getZuulRequestHeaders().get("x-forwarded-port");
		if(StringUtils.isNotBlank(hostfor) && hostfor.contains(",")){
			context.addZuulRequestHeader("X-Forwarded-Host", hostfor.split(",")[0]);
		}
		if(StringUtils.isNotBlank(protofor) && protofor.contains(",")){
			context.addZuulRequestHeader("X-Forwarded-Proto", protofor.split(",")[0]);
		}
		if(StringUtils.isNotBlank(portfor) && portfor.contains(",")){
			context.addZuulRequestHeader("X-Forwarded-Port", portfor.split(",")[0]);
		}
		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return FilterConstants.PRE_DECORATION_FILTER_ORDER+1;
	}

}
