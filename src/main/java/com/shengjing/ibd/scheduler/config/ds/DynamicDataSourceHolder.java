package com.shengjing.ibd.scheduler.config.ds;

public class DynamicDataSourceHolder {
    public static final String DATA_SOURCE_MASTER = "master";
    public static final String DATA_SOURCE_SLAVE = "slave";
//    public static final String DATA_SOURCE_CM = "cm";

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setDataSource(String name) {
        contextHolder.set(name);
    }

    public static String getDataSource() {
        return contextHolder.get();
    }

    public static void clearDataSource() {
        contextHolder.remove();
    }
}