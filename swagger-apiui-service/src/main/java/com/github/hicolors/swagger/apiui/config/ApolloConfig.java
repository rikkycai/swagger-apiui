package com.github.hicolors.swagger.apiui.config;

import com.github.hicolors.swagger.apiui.SwaggerButlerProperties;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * apollo配置多个配置文件 例:@EnableApolloConfig(value = {"application","datasource"})
 */
@Configuration
@EnableApolloConfig(value={"application","swagger-api-route"})
public class ApolloConfig implements ApplicationContextAware{

    private Logger log = LoggerFactory.getLogger(ApolloConfig.class);
    
    private ApplicationContext applicationContext;

    @Autowired
    private RouteLocator routeLocator;
    
    @Autowired
    private SwaggerButlerProperties swaggerButlerProperties;

    /**
     * 配置文件刷新
     * 监听多个配置文件 例：@ApolloConfigChangeListener(value = {"application","datasource"})
     * @param changeEvent
     */
    @ApolloConfigChangeListener(value={"application","swagger-api-route"})
    private void onChange(ConfigChangeEvent changeEvent) {
    	boolean zuulPropertiesChanged = false;
    	swaggerButlerProperties.clear();
        for(String changeKey : changeEvent.changedKeys()) {
            ConfigChange change = changeEvent.getChange(changeKey);
            log.info("Found change - key {}, oldvalue {}, newvalue {}, changeType {}",
                    changeKey,change.getOldValue(),change.getNewValue(),change.getChangeType());
            if (changeKey.startsWith("zuul.")) {
                zuulPropertiesChanged = true;
                routeLocator.getRoutes().clear();
                break;
            }
        }
        this.applicationContext.publishEvent(new EnvironmentChangeEvent(changeEvent.changedKeys()));
        if (zuulPropertiesChanged) {
        	this.applicationContext.publishEvent(new RoutesRefreshedEvent(routeLocator));
        }
    }
    
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = applicationContext;
	}
}
