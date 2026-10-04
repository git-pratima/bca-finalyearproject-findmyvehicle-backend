package com.pratima.bca.findmyvehicle.config;

import org.apache.catalina.session.StandardManager;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatSessionConfig {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> disableSessionPersistence() {
        return factory -> factory.addContextCustomizers(context -> {
            StandardManager manager = new StandardManager();
            manager.setPathname(null);
            context.setManager(manager);
        });
    }
}
