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

    private static  Map<String, String> props;

    private static boolean inited = false;

    private static final String[] tokenInitUrlArray = {"token_url", "jsapi_ticket_url"};

    private static final String APP_ID_KEY = "app_id";

    private static final String APP_SECRET_KEY = "app_secret";

    private static String APP_ID = "";

    private static final String PROPERTIES_FILE_PATH = "sysConfig.properties";

    public static synchronized void init(){
        if(inited){
            return;
        }
        //读取配置文件信息
        props =
                PropertiesUtil.readPropertiesFile(PROPERTIES_FILE_PATH);

        if (props.size() == 0) {
            return;
        }

        for(String reqUrl : tokenInitUrlArray){
            JSONObject httpRes = null;
            String tokenName = "";
            if("token_url".equals(reqUrl)){
                tokenName = PropertyField.ACCESS_TOKEN;
                httpRes = reqGetToken(reqUrl);
            }
            if("jsapi_ticket_url".equals(reqUrl)){
                tokenName = PropertyField.JSAPI_TICKET;
                TokenInfo tokenInfo = secretMap.get(PropertyField.ACCESS_TOKEN);
                httpRes = reqGetJdkApiTicket(reqUrl, tokenInfo.getToken());
            }

            if(httpRes == null){
                return;
            }

            if("SUCCESS".equals(httpRes.get("respCode"))){
                String resp = (String)httpRes.get("respBody");
                JSONObject respMsg = JSONObject.parseObject(resp);
                String accessToken = respMsg.getString(tokenName);
                if(StringUtils.isNotEmpty(accessToken)){
                    String invalidTime = respMsg.getString("expires_in");
                    Integer time = StringUtils.isNotEmpty(invalidTime) ? Integer.parseInt(invalidTime) : 0;

                    TokenInfo tokenInfo = new TokenInfo();
                    tokenInfo.setToken(accessToken);
                    tokenInfo.setCreateTime(new Date());
                    tokenInfo.setInvalidTime(time);

                    secretMap.put(tokenName, tokenInfo);
                } else {
                    logger.error(log.concat("微信服务器应答结果:") + resp);
                    return;
                }
            }
        }
    }

    private static JSONObject reqGetToken(String tokenUrlKey){
        String url = props.get(tokenUrlKey);
        String appId = props.get(APP_ID_KEY);
        String appSecret = props.get(APP_SECRET_KEY);
        APP_ID = appId;

        if(StringUtils.isBlank(url) || StringUtils.isBlank(appId) || StringUtils.isBlank(appSecret)){
            logger.error(log.concat("获取access_token关键信息读取失败"));
        }

        url = url.concat("&appid="+appId+"&secret="+appSecret);
        return HttpUtils.execute(url, "", 2000);
    }

    private static JSONObject reqGetJdkApiTicket(String tokenUrlKey, String accessToken){
        String url = props.get(tokenUrlKey);

        url = url.concat("?access_token="+accessToken+"&type=jsapi");
        return HttpUtils.execute(url, "", 2000);
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

    public static String getAppId(){
        if(inited){
            return APP_ID;
        }
        init();
        return APP_ID;
    }
}
