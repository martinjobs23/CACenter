package com.ceit.desktop.utils;

public class Constants {
    //Mysql常量类
    public static final String JDBC_DRIVER = PropertUtil.getValueByKey("jdbc.driver");
    public static final String JDBC_URL = PropertUtil.getValueByKey("jdbc.url");
    public static final String JDBC_USER = PropertUtil.getValueByKey("jdbc.user");
    public static final String JDBC_PASSWORD = PropertUtil.getValueByKey("jdbc.pwd");
    public static final String JDBC_CORECONN = PropertUtil.getValueByKey("jdbc.coreConnectionCount");
    public static final String JDBC_MAXCONN = PropertUtil.getValueByKey("jdbc.maxConnectionCount");
    public static final String JDBC_DEFAULTCONN = PropertUtil.getValueByKey("jdbc.defaultConnectionTimeout");
}
