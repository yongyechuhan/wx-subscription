package com.jxyd.wx.utils.cache;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.jxyd.wx.domain.TokenInfo;
import com.jxyd.wx.utils.HttpUtils;
import com.jxyd.wx.utils.PropertiesUtil;
import com.jxyd.wx.utils.PropertyField;
import com.jxyd.wx.utils.WxServerReqUtil;
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

    private static final String[] tokenInitUrlArray = {"token_url", "jsapi_ticket_url"};

    public static synchronized void init(){
        if(inited){
            return;
        }

        for(String reqUrl : tokenInitUrlArray){
            JSONObject httpRes = null;
            String tokenName = "";
            if("token_url".equals(reqUrl)){
                tokenName = PropertyField.ACCESS_TOKEN_NAME;
                httpRes = WxServerReqUtil.reqGetToken();
            }
            if("jsapi_ticket_url".equals(reqUrl)){
                tokenName = PropertyField.JSAPI_TICKET_NAME;
                httpRes = WxServerReqUtil.reqGetJdkApiTicket();
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
