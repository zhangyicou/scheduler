package com.shengjing.ibd.scheduler.config;

import com.dexcoder.dal.spring.JdbcDaoImpl;
import com.jolbox.bonecp.BoneCPDataSource;
import com.shengjing.ibd.scheduler.common.CustomProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DataSourceConfig {

    private static Logger log = Logger.getLogger(DataSourceConfig.class);

    @Value("${ds.bonecp.idleConnectionTestPeriodInMinutes}")
    private long idleConnectionTestPeriod;
    @Value("${ds.bonecp.idleMaxAgeInMinutes}")
    private long idleMaxAge;
    @Value("${ds.bonecp.statementsCacheSize}")
    private int statementsCacheSize;
    @Value("${ds.bonecp.mysql.driverClass}")
    private String driverMysql;
    @Value("${ds.bonecp.minConnectionsPerPartition}")
    private int minConnectionsPerPartition;
    @Value("${ds.bonecp.maxConnectionsPerPartition}")
    private int maxConnectionsPerPartition;
    @Value("${ds.bonecp.acquireIncrement}")
    private int acquireIncrement;
    @Value("${ds.bonecp.partitionCount}")
    private int partitionCount;
    @Value("${ds.bonecp.master.username}")
    private String username;
    @Value("${ds.bonecp.master.password}")
    private String password;
    @Value("${ds.bonecp.master.host}")
    private String host;
    @Value("${ds.bonecp.master.port}")
    private String port;
    @Value("${ds.bonecp.master.database}")
    private String database;
    @Value("${ds.bonecp.master.jdbcUrl}")
    private String dbUrl;
    @Value("${jdbcTemplate.fetchSize}")
    private int fetchSize;

    @Bean
    @Scope("singleton")
    public JdbcTemplate getJdbcTemplate(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSourceMaster());
        jdbcTemplate.setFetchSize(fetchSize);

        return jdbcTemplate;
    }

    @Bean
    @Scope("singleton")
    public JdbcDaoImpl getJdbcDaoImpl(){
        JdbcDaoImpl jdbcTemplate = new JdbcDaoImpl();
        jdbcTemplate.setJdbcTemplate(getJdbcTemplate());

        return jdbcTemplate;
    }

    @Bean
    @Scope("singleton")
    public SchedulerFactoryBean getSchedulerFactoryBean(){
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setDataSource(dataSourceMaster());
        //读取配置文件
        scheduler.setConfigLocation(new ClassPathResource("quartz.properties"));

        scheduler.setSchedulerName("ClusterScheduler");
        /**----必须的，QuartzScheduler 延时启动，应用启动完后 QuartzScheduler 再启动----*/
        scheduler.setStartupDelay(30);
        scheduler.setApplicationContextSchedulerContextKey("applicationContextKey");
        /**----可选，QuartzScheduler 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了 ----*/
        scheduler.setOverwriteExistingJobs(true);
        /**----设置自动启动 ----*/
        scheduler.setAutoStartup(true);
        return scheduler;
    }


    /**
     * 主数据源
     *
     * @return
     */
    @Bean
    @Scope("singleton")
    public DataSource dataSourceMaster() {


        String name = "master";

        CustomProperties.DbConf dbConf = CustomProperties.getInstance().getDbConf().get(name);
        if (dbConf != null) {
            // 有自定义（参数传进来）的，则使用自定义的。
            if (StringUtils.isNotBlank(dbConf.getDbUsername())) {
                username = dbConf.getDbUsername();
            }
            if (StringUtils.isNotBlank(dbConf.getDbPassword())) {
                password = dbConf.getDbPassword();
            }
            if (StringUtils.isNotBlank(dbConf.getDbHost())) {
                host = dbConf.getDbHost();
            }

            if (StringUtils.isNotBlank(dbConf.getDbPort())) {
                port = dbConf.getDbPort();
            }
            if (StringUtils.isNotBlank(dbConf.getDbDatabase())) {
                database = dbConf.getDbDatabase();
            }

        }

        CustomProperties.DbConnConf dbConnConf = CustomProperties.getInstance().getDbConnConf().get(name);
        if (dbConnConf != null) {
            if (dbConnConf.getAcquireIncrement() > 0) {
                acquireIncrement = dbConnConf.getAcquireIncrement();
            }
            if (dbConnConf.getPartitionCount() > 0) {
                partitionCount = dbConnConf.getPartitionCount();
            }
            if (dbConnConf.getMinConnectionsPerPartition() > 0) {
                minConnectionsPerPartition = dbConnConf.getMinConnectionsPerPartition();
            }
            if (dbConnConf.getMaxConnectionsPerPartition() > 0) {
                maxConnectionsPerPartition = dbConnConf.getMaxConnectionsPerPartition();
            }
        }

        String jdbcUrl = dbUrl.replace("{host}", host).replace("{port}", port).replace("{database}", database);

        BoneCPDataSource dataSource = new BoneCPDataSource();
        dataSource.setDriverClass(driverMysql);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaxConnectionsPerPartition(maxConnectionsPerPartition);
        dataSource.setMinConnectionsPerPartition(minConnectionsPerPartition);
        dataSource.setPartitionCount(partitionCount);
        dataSource.setAcquireIncrement(acquireIncrement);
        dataSource.setIdleConnectionTestPeriodInMinutes(idleConnectionTestPeriod);
        dataSource.setIdleMaxAgeInMinutes(idleMaxAge);
        dataSource.setStatementsCacheSize(statementsCacheSize);

        log.debug("master dataSource.hash = " + dataSource.hashCode());

        return dataSource;
    }
}
