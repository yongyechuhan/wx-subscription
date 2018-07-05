package com.jxyd.wx.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 用于读取properties文件的工具类
 */
public class PropertiesUtil {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static final String log = "配置文件读取--";

    public static Map<String, String> readPropertiesFile(String filePath){
        Map<String, String> props = new HashMap<>();
        if(StringUtils.isBlank(filePath)){
            logger.error(log.concat("文件路径不能为空"));
            return props;
        }

        InputStream is =
                PropertiesUtil.class.getClassLoader().getResourceAsStream(filePath);
        if(is == null){
            logger.error(log.concat("读取{}文件失败"), filePath);
            return props;
        }

        Properties properties = new Properties();
        try {
            properties.load(is);
            Set<String> propNames = properties.stringPropertyNames();

            for(String propName : propNames){
                props.put(propName, properties.getProperty(propName));
            }
        } catch (Exception e) {
            logger.error(log.concat("载入{}文件失败"), filePath);
            return props;
        }

        return props;
    }
}
