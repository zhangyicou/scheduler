package com.shengjing.ibd.scheduler.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by roy on 2017/1/16.
 * 属性类
 */
@Component
public class AppProperties {

    /**
     * 服务
     *
     perm 权限
     ent 企业配置
     sp 用户和租户
     plan 生产计划、工单、任务
     base 基础服务
     ov 在线验证
     proc 生产配置工序线
     notify 终端后台服务
     data_api 数据服务
     */
    private static final String DEP_SVS_NAME_PERM = "perm";
    private static final String DEP_SVS_NAME_ENT = "ent";
    private static final String DEP_SVS_NAME_SP = "sp";
    private static final String DEP_SVS_NAME_PLAN = "plan";
    private static final String DEP_SVS_NAME_BASE = "base";
    private static final String DEP_SVS_NAME_OV = "ov";
    private static final String DEP_SVS_NAME_PROC = "proc";
    private static final String DEP_SVS_NAME_NOTIFY = "notify";
    private static final String DEP_SVS_NAME_DATA_API = "data_api";

    private String permUrl = null;
    private String entUrl = null;
    private String spUrl = null;
    private String planUrl = null;
    private String baseUrl = null;
    private String ovUrl = null;
    private String procUrl = null;
    private String notifyUrl = null;
    private String dataApiUrl = null;

    private static String jdbcUrl;
    private static String partCnt;
    private static String dbUser;
    private static String minConnPerPart;
    private static String maxConnPerPart;
    private static String acquireIncrement;

    @Inject
    Environment env;

    @Autowired
    public AppProperties(ApplicationArguments args) {
//        boolean debug = args.containsOption("ibd.notify");
//        List<String> files = args.getNonOptionArgs();
    }

    public static String getDbUser() {
        return dbUser;
    }

    public static void setDbUser(String dbUser) {
        AppProperties.dbUser = dbUser;
    }

    public static String getJdbcUrl() {
        return jdbcUrl;
    }

    public static void setJdbcUrl(String jdbcUrl) {
        AppProperties.jdbcUrl = jdbcUrl;
    }

    public static String getPartCnt() {
        return partCnt;
    }

    public static void setPartCnt(String partCnt) {
        AppProperties.partCnt = partCnt;
    }

    public static String getMinConnPerPart() {
        return minConnPerPart;
    }

    public static void setMinConnPerPart(String minConnPerPart) {
        AppProperties.minConnPerPart = minConnPerPart;
    }

    public static String getMaxConnPerPart() {
        return maxConnPerPart;
    }

    public static void setMaxConnPerPart(String maxConnPerPart) {
        AppProperties.maxConnPerPart = maxConnPerPart;
    }

    public static String getAcquireIncrement() {
        return acquireIncrement;
    }

    public static void setAcquireIncrement(String acquireIncrement) {
        AppProperties.acquireIncrement = acquireIncrement;
    }

    /**
     * 获取URL 权限
     *
     * @return
     */
    public String getUrlPerm() {
        if (null != permUrl) {
            return permUrl;
        }

        String domain = env.getProperty("url.perm.domain");
        domain = getDomain(domain, DEP_SVS_NAME_PERM);

        String url = env.getProperty("url.perm");
        url = url.replace("{domain}", domain);

        permUrl = url;

        return url;
    }

    /**
     * 获取URL 企业配置
     *
     * @return
     */
    public String getUrlEnt() {
        if (null != entUrl) {
            return entUrl;
        }

        String domain = env.getProperty("url.ent.domain");
        domain = getDomain(domain, DEP_SVS_NAME_ENT);

        String url = env.getProperty("url.ent");
        url = url.replace("{domain}", domain);

        entUrl = url;

        return url;
    }

    /**
     * 获取URL 用户和租户
     *
     * @return
     */
    public String getUrlSp() {
        if (null != spUrl) {
            return spUrl;
        }

        String domain = env.getProperty("url.sp.domain");
        domain = getDomain(domain, DEP_SVS_NAME_SP);

        String url = env.getProperty("url.sp");
        url = url.replace("{domain}", domain);

        spUrl = url;

        return url;
    }

    /**
     * 获取URL 生产计划、工单、任务
     *
     * @return
     */
    public String getUrlPlan() {
        if (null != planUrl) {
            return planUrl;
        }

        String domain = env.getProperty("url.plan.domain");
        domain = getDomain(domain, DEP_SVS_NAME_PLAN);

        String url = env.getProperty("url.plan");
        url = url.replace("{domain}", domain);

        planUrl = url;

        return url;
    }

    /**
     * 获取URL 基础服务
     *
     * @return
     */
    public String getUrlBase() {
        if (null != baseUrl) {
            return baseUrl;
        }

        String domain = env.getProperty("url.base.domain");
        domain = getDomain(domain, DEP_SVS_NAME_BASE);

        String url = env.getProperty("url.base");
        url = url.replace("{domain}", domain);

        baseUrl = url;

        return url;
    }

    /**
     * 获取URL 在线验证
     *
     * @return
     */
    public String getUrlOv() {
        if (null != ovUrl) {
            return ovUrl;
        }

        String domain = env.getProperty("url.ov.domain");
        domain = getDomain(domain, DEP_SVS_NAME_OV);

        String url = env.getProperty("url.ov");
        url = url.replace("{domain}", domain);

        ovUrl = url;

        return url;
    }

    /**
     * 获取URL 生产配置工序线
     *
     * @return
     */
    public String getUrlProc() {
        if (null != procUrl) {
            return procUrl;
        }

        String domain = env.getProperty("url.proc.domain");
        domain = getDomain(domain, DEP_SVS_NAME_PROC);

        String url = env.getProperty("url.proc");
        url = url.replace("{domain}", domain);

        procUrl = url;

        return url;
    }

    /**
     * 获取URL 终端后台服务
     *
     * @return
     */
    public String getUrlNotify() {
        if (null != notifyUrl) {
            return notifyUrl;
        }

        String domain = env.getProperty("url.notify.domain");
        domain = getDomain(domain, DEP_SVS_NAME_NOTIFY);

        String url = env.getProperty("url.notify");
        url = url.replace("{domain}", domain);

        notifyUrl = url;

        return url;
    }

    /**
     * 获取URL 数据服务
     *
     * @return
     */
    public String getUrlDataApi() {
        if (null != dataApiUrl) {
            return dataApiUrl;
        }

        String domain = env.getProperty("url.data_api.domain");
        domain = getDomain(domain, DEP_SVS_NAME_DATA_API);

        String url = env.getProperty("url.data_api");
        url = url.replace("{domain}", domain);

        notifyUrl = url;

        return url;
    }

    private String getDomain(String domainOriginal, String serviceName) {
        String domain = domainOriginal;

        CustomProperties.DepService depService = CustomProperties.getInstance().getDepSvs().get(serviceName);
        if (depService != null) {
            if (StringUtils.isNotBlank(depService.getHost())) {
                domain = depService.getHost().trim();
            }
            if (StringUtils.isNotBlank(depService.getPort())) {
                String port = depService.getPort().trim();
                int idx = StringUtils.indexOf(domain, ":");
                if (idx >= 0) {
                    domain = domain.substring(0, idx);
                }
                domain = domain + ":" + port;
            }
        }

        return domain;
    }
}
