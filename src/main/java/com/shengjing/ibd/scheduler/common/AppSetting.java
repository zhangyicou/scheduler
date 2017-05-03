package com.shengjing.ibd.scheduler.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.shengjing.ibd.scheduler.utils.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by roy on 2017/1/16.
 */
public class AppSetting {
    private static final String IBD_PARAM = "--ibd.args.env=";

    private static final String LOGGER_TYPE_PATH = "p";
    private static final String LOGGER_TYPE_LEVEL = "l";

    /**
     * 服务地址解析
     *
    perm 权限
    ent 企业配置
    sp 用户和租户
    plan 生产计划、工单、任务
    base 基础服务
    ov 在线验证
    proc 生产配置工序线
    notify 终端后台服务
    data_api 终端后台服务
    */
    private static final String DEP_SVS_TYPE_NAME = "n";            // 名称（自定义，用于解析时使用。）
    private static final String DEP_SVS_TYPE_SERVICE = "s";         // 域名或IP地址
    private static final String DEP_SVS_TYPE_PORT = "P";            // 端口

    private static final String DB_MYSQL_TYPE_USERNAME = "u";
    private static final String DB_MYSQL_TYPE_PASSWORD = "p";
    private static final String DB_MYSQL_TYPE_HOST = "h";
    private static final String DB_MYSQL_TYPE_NAME = "n";
    private static final String DB_MYSQL_TYPE_PORT = "P";
    private static final String DB_MYSQL_TYPE_DATABASE = "D";

    private static final String DB_CONN_TYPE_PART_COUNT = "partCnt";
    private static final String DB_CONN_TYPE_MIN_CONN_PER_PART = "minConnPerPart";
    private static final String DB_CONN_TYPE_MAX_CONN_PER_PART = "maxConnPerPart";
    private static final String DB_CONN_TYPE_ACQUIRE_INCREMENT = "acquireIncrement";

    private static final String TYPE_SPLIT_CHAR = " -";
    private static final String PARAM_SPLIT_CHAR = " ";

    private static Logger log = Logger.getLogger(AppSetting.class);

    private enum ParamType {
        PARAM_LOGGER,
        PARAM_DB,
        PARAM_DB_CONN,
        PARAM_DEP_SVS,
    }

    /**
     * 解析参数
     *
     * @param args
     */
    public static void parseArgs(String[] args) {
        // "{\"logger\": \"-p /data/logs -l info\", \"dbMysql\":\"-h 192.168.92.176 -P 3306 -u root -p 123456\"}"
//        ProArgs proArgs = JacksonUtils.parse(ProArgs.class, args);

        for (int i = 0; i < args.length; i++) {
            log.info(String.format("args[%s]=%s", i, args[i]));
            if (StringUtils.startsWithIgnoreCase(args[i], IBD_PARAM)) {
                String[] tmp = args[i].split(IBD_PARAM);
                if (tmp.length > 1) {
                    ProArgs proArgs = JacksonUtils.parse(ProArgs.class, tmp[1]);
                    setCustomProperties(proArgs);
                }
            }
        }
    }

    /**
     * 解析传入参数
     *
     * @param proArgs
     */
    private static void setCustomProperties(ProArgs proArgs) {
        // 解析日志配置
        setCustomProperties(proArgs.getLogger(), ParamType.PARAM_LOGGER);

        // 解析数据库配置
        if (StringUtils.isNotBlank(proArgs.getDb())) {
            String[] paramArr = proArgs.getDb().split("\\|");
            for (int i = 0; i < paramArr.length; i++) {
                setCustomProperties(paramArr[i], ParamType.PARAM_DB);
            }
        }

        // 解析连接池配置
        if (StringUtils.isNotBlank(proArgs.getDbConn())) {
            String[] paramArr = proArgs.getDbConn().split("\\|");
            for (int i = 0; i < paramArr.length; i++) {
                setCustomProperties(paramArr[i], ParamType.PARAM_DB_CONN);
            }
        }

        // 解析依赖服务
        if (StringUtils.isNotBlank(proArgs.getDepSvs())) {
            String[] paramArr = proArgs.getDepSvs().split("\\|");
            for (int i = 0; i < paramArr.length; i++) {
                setCustomProperties(paramArr[i], ParamType.PARAM_DEP_SVS);
            }
        }

        CustomProperties customProperties = CustomProperties.getInstance();
        log.info("customProperties=" + customProperties);
    }

    /**
     * 解析具体的一行数据
     *
     * @param paramString
     * @param paramType
     */
    private static void setCustomProperties(String paramString, ParamType paramType) {
        if (StringUtils.isBlank(paramString)) {
            return;
        }

        String tmp;
        String type;
        String value;
        String[] tmpArr;

        String name = null;
        Map<String, String> paramMap = new HashMap<>();

        tmp = PARAM_SPLIT_CHAR + paramString.trim();
        tmpArr = tmp.split(TYPE_SPLIT_CHAR);
        for (int i = 0; i < tmpArr.length; i++) {
            tmp = tmpArr[i].trim();
            if (StringUtils.isBlank(tmp)) {
                continue;
            }

            if (StringUtils.contains(tmp, PARAM_SPLIT_CHAR)) {
                String[] paramArr = tmp.split(PARAM_SPLIT_CHAR);
                if (paramArr.length < 2) {
                    continue;
                }
                type = paramArr[0].trim();
                value = paramArr[1].trim();
            } else {
                // 截取第一位，作为type
                type = tmp.substring(0, 1).trim();
                value = tmp.substring(1).trim();
            }

            if (StringUtils.isBlank(type) || StringUtils.isBlank(value)) {
                continue;
            }

            switch (paramType) {
                case PARAM_LOGGER:
                    setCustomPropertiesLogger(type, value);
                    break;
                case PARAM_DB:
                case PARAM_DB_CONN:
                case PARAM_DEP_SVS:
                    // 因为目前名称都用 -n ，所以就判断一个就可以了，如果有其他的定义出现，再分开处理。
                    if (DB_MYSQL_TYPE_NAME.equals(type)) {
                        name = value;
                    } else {
                        paramMap.put(type, value);
                    }
                    break;
            }
        }

        if (StringUtils.isBlank(name) || paramMap.size() == 0) {
            return;
        }

        switch (paramType) {
            case PARAM_DB:
                setCustomPropertiesDbConf(name, paramMap);
                break;
            case PARAM_DB_CONN:
                setCustomPropertiesDbConnConf(name, paramMap);
                break;
            case PARAM_DEP_SVS:
                setCustomPropertiesDependencyService(name, paramMap);
                break;
        }
    }

    private static void setCustomPropertiesLogger(String type, String value) {
        if (LOGGER_TYPE_PATH.equals(type)) {
            CustomProperties.getInstance().setLoggerPath(value);
        } else if (LOGGER_TYPE_LEVEL.equals(type)) {
            CustomProperties.getInstance().setLoggerLevel(value.toUpperCase());
        }
    }

    private static void setCustomPropertiesDbConf(String name, Map<String, String> paramMap) {
        CustomProperties.DbConf dbConf = new CustomProperties.DbConf();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            if (DB_MYSQL_TYPE_HOST.equals(entry.getKey())) {
                dbConf.setDbHost(entry.getValue());
            } else if (DB_MYSQL_TYPE_PORT.equals(entry.getKey())) {
                dbConf.setDbPort(entry.getValue());
            } else if (DB_MYSQL_TYPE_USERNAME.equals(entry.getKey())) {
                dbConf.setDbUsername(entry.getValue());
            } else if (DB_MYSQL_TYPE_PASSWORD.equals(entry.getKey())) {
                dbConf.setDbPassword(entry.getValue());
            } else if (DB_MYSQL_TYPE_DATABASE.equals(entry.getKey())) {
                dbConf.setDbDatabase(entry.getValue());
            }
        }
        CustomProperties.getInstance().getDbConf().put(name, dbConf);
    }

    private static void setCustomPropertiesDbConnConf(String name, Map<String, String> paramMap) {
        CustomProperties.DbConnConf dbConnConf = new CustomProperties.DbConnConf();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            try {
                if (DB_CONN_TYPE_PART_COUNT.equals(entry.getKey())) {
                    dbConnConf.setPartitionCount(Integer.valueOf(entry.getValue()));
                } else if (DB_CONN_TYPE_MIN_CONN_PER_PART.equals(entry.getKey())) {
                    dbConnConf.setMinConnectionsPerPartition(Integer.valueOf(entry.getValue()));
                } else if (DB_CONN_TYPE_MAX_CONN_PER_PART.equals(entry.getKey())) {
                    dbConnConf.setMaxConnectionsPerPartition(Integer.valueOf(entry.getValue()));
                } else if (DB_CONN_TYPE_ACQUIRE_INCREMENT.equals(entry.getKey())) {
                    dbConnConf.setAcquireIncrement(Integer.valueOf(entry.getValue()));
                }
            } catch (Exception e) {
                log.error(String.format("parse param error for dbConnConf. type=%s, value=%s", entry.getKey(), entry.getValue()), e);
            }
        }
        CustomProperties.getInstance().getDbConnConf().put(name, dbConnConf);
    }

    private static void setCustomPropertiesDependencyService(String name, Map<String, String> paramMap) {
        CustomProperties.DepService depService = new CustomProperties.DepService();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            try {
                if (DEP_SVS_TYPE_SERVICE.equals(entry.getKey())) {
                    depService.setHost(entry.getValue());
                } else if (DEP_SVS_TYPE_PORT.equals(entry.getKey())) {
                    depService.setPort(entry.getValue());
                }
            } catch (Exception e) {
                log.error(String.format("parse param error for depSvs. type=%s, value=%s", entry.getKey(), entry.getValue()), e);
            }
        }

        CustomProperties.getInstance().getDepSvs().put(name, depService);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProArgs {
        private String logger;
        private String db;
        private String dbConn;
        private String depSvs;

        public String getLogger() {
            return logger;
        }

        /**
         * 日志配置项
         *
         * @param logger 例子：-p /data/logs -l info 说明：-p 路径 -l 级别（ALL TRACE DEBUG INFO WARN ERROR FATAL OFF 不区分大小写）
         */
        public void setLogger(String logger) {
            this.logger = logger;
        }

        public String getDb() {
            return db;
        }

        public void setDb(String db) {
            this.db = db;
        }

        public String getDbConn() {
            return dbConn;
        }

        public void setDbConn(String dbConn) {
            this.dbConn = dbConn;
        }

        public String getDepSvs() {
            return depSvs;
        }

        public void setDepSvs(String depSvs) {
            this.depSvs = depSvs;
        }
    }
}
