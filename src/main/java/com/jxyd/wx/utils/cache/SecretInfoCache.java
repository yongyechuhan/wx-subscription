package com.jxyd.wx.utils.cache;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.jxyd.wx.domain.TokenInfo;
import com.jxyd.wx.utils.HttpUtils;
import com.jxyd.wx.utils.PropertiesUtil;
import com.jxyd.wx.utils.PropertyField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SecretInfoCache {

    private static Logger logger = LoggerFactory.getLogger(SecretInfoCache.class);

    private static final String log = "token信息初始化--";

    private static Map<String, TokenInfo> secretMap = new HashMap<>();

    private static boolean inited = false;

    private static final String TOKEN_URL = "token_url";

    private static final String APP_ID_KEY = "app_id";

    private static final String APP_SECRET_KEY = "app_secret";

    private static final String PROPERTIES_FILE_PATH = "sysConfig.properties";

    public static synchronized void init(){
        if(inited){
            return;
        }
        //读取配置文件信息
        Map<String, String> props =
                PropertiesUtil.readPropertiesFile(PROPERTIES_FILE_PATH);

        if (props.size() == 0) {
            return;
        }

        String url = props.get(TOKEN_URL);
        String appId = props.get(APP_ID_KEY);
        String appSecret = props.get(APP_SECRET_KEY);

        if(StringUtils.isBlank(url) || StringUtils.isBlank(appId) || StringUtils.isBlank(appSecret)){
            logger.error(log.concat("获取access_token关键信息读取失败"));
        }

        url = url.concat("&appid="+appId+"&secret="+appSecret);
        JSONObject httpRes = HttpUtils.execute(url, "", 2000);

        if("SUCCESS".equals(httpRes.get("respCode"))){
            String resp = (String)httpRes.get("respBody");
            JSONObject respMsg = JSONObject.parseObject(resp);
            String accessToken = respMsg.getString("access_token");
            if(StringUtils.isNotEmpty(accessToken)){
                String invalidTime = respMsg.getString("expires_in");
                Integer time = StringUtils.isNotEmpty(invalidTime) ? Integer.parseInt(invalidTime) : 0;

                TokenInfo tokenInfo = new TokenInfo();
                tokenInfo.setToken(accessToken);
                tokenInfo.setCreateTime(new Date());
                tokenInfo.setInvalidTime(time);

                secretMap.put(PropertyField.ACCESS_TOKEN, tokenInfo);
            } else {
                logger.error(log.concat("微信服务器应答结果:") + resp);
                return;
            }
        }
    }

    public static String getToken(String tokenType){
        if(inited){
            TokenInfo tokenInfo = secretMap.get(tokenType);
            if(tokenInfo != null){
                Date createTime = tokenInfo.getCreateTime();
                Date now = new Date();

                long second = (now.getTime() - createTime.getTime()) / 1000;
                if(second >= tokenInfo.getInvalidTime()){
                    inited = false;
                }
                secretMap.remove(tokenType);
            } else {
                inited = false;
            }
        }
        init();
        TokenInfo tokenInfo = secretMap.get(tokenType);
        if(tokenInfo == null)
            return null;
        else
            return tokenInfo.getToken();
    }
}
