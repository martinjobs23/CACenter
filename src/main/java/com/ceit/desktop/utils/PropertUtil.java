package com.ceit.desktop.utils;

import java.util.ResourceBundle;

public class PropertUtil {
    //封装Mysql，读取Mysql的配置
    static ResourceBundle resource;
    static{
        resource = ResourceBundle.getBundle("application");
    }
    public static String getValueByKey(String key){
        return resource.getString(key);
    }
}

