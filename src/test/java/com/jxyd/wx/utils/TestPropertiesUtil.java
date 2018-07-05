package com.jxyd.wx.utils;

import com.alibaba.fastjson.JSON;
import com.jxyd.wx.utils.cache.SecretInfoCache;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class TestPropertiesUtil {

    private Logger logger = LoggerFactory.getLogger(TestPropertiesUtil.class);

    @Test
    public void testGetProperties(){
        Map<String, String> props =
                PropertiesUtil.readPropertiesFile("sysConfig.properties");
        logger.info("解析配置文件结果："+JSON.toJSONString(props));
    }

    @Test
    public void testGetAccessToken(){
        String token = SecretInfoCache.getToken(PropertyField.ACCESS_TOKEN);
        logger.info("当前access_token为{}", token);
    }
}
