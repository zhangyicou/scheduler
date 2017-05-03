package com.shengjing.ibd.scheduler.common;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by roy on 2017/1/16.
 * 自定义属性（通过程序启动参数args传入）
 *  --ibd.notify="{\"logger\": \"-p d:\\logs2 -l info\", \"dbMysql\":\"-h 192.168.92.176 -P 3306 -u root -p 123456\",\"dbConnPool\":\"-partCnt 2 -minConnPerPart 2 -maxConnPerPart 5 -acquireIncrement 2\"}"
 */
public class CustomProperties {
    private static CustomProperties instance = new CustomProperties();

    /** 日志路径 */
    private String loggerPath;

    /** TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF */
    private String loggerLevel;

//    /** 权限服务 Url */
//    private String permUrl;
//    /** 企业配置服务 Url */
//    private String entUrl;
//    /** 用户和租户 Url */
//    private String spUrl;
//    /** 生产计划、工单、任务 Url */
//    private String planUrl;
//    /** 基础服务 Url */
//    private String baseUrl;
//    /** 在线验证服务 Url */
//    private String ovUrl;
//    /** 生产配置工序线服务 Url */
//    private String procUrl;
//    /** 终端后台服务 Url */
//    private String notifyUrl;

    Map<String, DepService> depSvs = new HashMap<>();
    Map<String, DbConf> dbConf = new HashMap<>();
    Map<String, DbConnConf> dbConnConf = new HashMap<>();

    private CustomProperties() {
    }

    public static CustomProperties getInstance() {
        return instance;
    }

    public String getLoggerPath() {
        return loggerPath;
    }

    public void setLoggerPath(String loggerPath) {
        this.loggerPath = loggerPath;
    }

    public String getLoggerLevel() {
        return loggerLevel;
    }

    public void setLoggerLevel(String loggerLevel) {
        this.loggerLevel = loggerLevel;
    }

    public Map<String, DbConf> getDbConf() {
        return dbConf;
    }

    public void setDbConf(Map<String, DbConf> dbConf) {
        this.dbConf = dbConf;
    }

    public Map<String, DbConnConf> getDbConnConf() {
        return dbConnConf;
    }

    public void setDbConnConf(Map<String, DbConnConf> dbConnConf) {
        this.dbConnConf = dbConnConf;
    }

    public Map<String, DepService> getDepSvs() {
        return depSvs;
    }

    public void setDepSvs(Map<String, DepService> depSvs) {
        this.depSvs = depSvs;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public static class DbConf {
        private String dbUsername;
        private String dbPassword;
        private String dbHost;
        private String dbPort;
        private String dbDatabase;

        public String getDbUsername() {
            return dbUsername;
        }

        public void setDbUsername(String dbUsername) {
            this.dbUsername = dbUsername;
        }

        public String getDbPassword() {
            return dbPassword;
        }

        public void setDbPassword(String dbPassword) {
            this.dbPassword = dbPassword;
        }

        public String getDbHost() {
            return dbHost;
        }

        public void setDbHost(String dbHost) {
            this.dbHost = dbHost;
        }

        public String getDbPort() {
            return dbPort;
        }

        public void setDbPort(String dbPort) {
            this.dbPort = dbPort;
        }

        public String getDbDatabase() {
            return dbDatabase;
        }

        public void setDbDatabase(String dbDatabase) {
            this.dbDatabase = dbDatabase;
        }
    }

    public static class DbConnConf {
        /** Min number of connections per partition. */
        private int minConnectionsPerPartition = 0;
        /** Max number of connections per partition. */
        private int maxConnectionsPerPartition = 0;
        /** Number of new connections to create in 1 batch. */
        private int acquireIncrement = 0;
        /** Number of partitions. */
        private int partitionCount = 0;

        public int getMinConnectionsPerPartition() {
            return minConnectionsPerPartition;
        }

        public void setMinConnectionsPerPartition(int minConnectionsPerPartition) {
            this.minConnectionsPerPartition = minConnectionsPerPartition;
        }

        public int getMaxConnectionsPerPartition() {
            return maxConnectionsPerPartition;
        }

        public void setMaxConnectionsPerPartition(int maxConnectionsPerPartition) {
            this.maxConnectionsPerPartition = maxConnectionsPerPartition;
        }

        public int getAcquireIncrement() {
            return acquireIncrement;
        }

        public void setAcquireIncrement(int acquireIncrement) {
            this.acquireIncrement = acquireIncrement;
        }

        public int getPartitionCount() {
            return partitionCount;
        }

        public void setPartitionCount(int partitionCount) {
            this.partitionCount = partitionCount;
        }
    }

    public static class DepService {
        private String name;
        private String host;
        private String port;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }
    }


}
