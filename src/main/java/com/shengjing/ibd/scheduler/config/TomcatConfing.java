package com.shengjing.ibd.scheduler.config;

import org.apache.catalina.valves.AccessLogValve;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.inject.Inject;
import java.io.File;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * Created by roy on 2017/1/16.
 */
@Configuration
public class TomcatConfing {
    @Value("${ibd.server.port}")
    private int port;
    @Value("${ibd.server.context-path}")
    private String contextPath;
    @Value("${ibd.server.connection-timeout}")
    private int timeout;
    @Value("${logging.path}")
    private String loggingPath;

    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {

        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        AccessLogValve accessLogValve=new AccessLogValve();
        accessLogValve.setPattern("[%{yyyy-MM-dd HH:mm:ss.SSS}t] [info] acc -%a - \"%r\" \"%{User-Agent}i\" \"%U\" %s %F %b ms");
        accessLogValve.setDirectory("");
        accessLogValve.setEnabled(true);
        accessLogValve.setPrefix("acc.");
        accessLogValve.setSuffix(".log");
        accessLogValve.setFileDateFormat("YYYYMMdd");
        tomcat.addEngineValves(accessLogValve);

        tomcat.setUriEncoding(Charset.forName("UTF-8"));
        tomcat.setPort(port);
        tomcat.setContextPath(contextPath);

        // LOG_PATH在org.springframework.boot.logging.LogFile源码中已经修改，spring框架会同步到System属性中。
        String baseDir = loggingPath + File.separatorChar + "acc";
        tomcat.setBaseDirectory(new File(baseDir));
        tomcat.setSessionTimeout(timeout, TimeUnit.MILLISECONDS);
        return tomcat;
    }
}
