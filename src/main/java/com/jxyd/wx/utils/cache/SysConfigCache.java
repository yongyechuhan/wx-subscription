package com.jxyd.wx.utils.cache;

import com.jxyd.wx.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class SysConfigCache {
    private static final Logger logger = LoggerFactory.getLogger(SysConfigCache.class);

    private static Map<String, String> props;

    private static boolean inited = false;

    private static final String PROPERTIES_FILE_PATH = "sysConfig.properties";

    public static synchronized void init(){
        if(inited){
            return;
        }

        try {
            //读取配置文件信息
            props =
                    PropertiesUtil.readPropertiesFile(PROPERTIES_FILE_PATH);
        } catch (Exception e) {
            logger.error("初始化系统配置失败", e);
        }
    }

    public static String getProp(String key){
        if(!inited){
            init();
        }
        return props.get(key);
    }
}
