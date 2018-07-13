package com.jxyd.wx.utils;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.jxyd.wx.utils.cache.SecretInfoCache;
import com.jxyd.wx.utils.cache.SysConfigCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于集中所有需要和微信服务器交互的工具类
 */
public class WxServerReqUtil {
    private static final Logger logger = LoggerFactory.getLogger(WxServerReqUtil.class);

    private static final String log = "发起微信服务请求--";

    private static final String tokenUrlKey = "token_url";

    private static final String jsApiTicketUrlKey = "jsapi_ticket_url";

    private static final String downloadMediaUrlKey = "download_media_url";

    /**
     * 获取全局授权access_token
     * @return
     */
    public static JSONObject reqGetToken(){
        String url = SysConfigCache.getProp(tokenUrlKey);
        String appId = SysConfigCache.getProp(PropertyField.APP_ID_KEY);
        String appSecret = SysConfigCache.getProp(PropertyField.APP_SECRET_KEY);

        if(StringUtils.isBlank(url) || StringUtils.isBlank(appId) || StringUtils.isBlank(appSecret)){
            logger.error(log.concat("获取access_token关键信息读取失败"));
        }

        url = url.concat("&appid="+appId+"&secret="+appSecret);
        return HttpUtils.execute(url, "", 2000, "text");
    }

    /**
     * 获取微信js调用票据 jsapi_ticket
     * @return
     */
    public static JSONObject reqGetJdkApiTicket(){
        String url = SysConfigCache.getProp(jsApiTicketUrlKey);
        String accessToken = SecretInfoCache.getToken(PropertyField.ACCESS_TOKEN_NAME);

        if(StringUtils.isBlank(url) || StringUtils.isEmpty(accessToken)){
            logger.error(log.concat("获取js票据的关键信息为空!"));
        }

        url = url.concat("?access_token="+accessToken+"&type=jsapi");
        return HttpUtils.execute(url, "", 2000, "text");
    }

    /**
     * 获取上传给微信服务器的图片
     * @param serverId
     * @return
     */
    public static JSONObject downloadServerPic(String serverId){
        String url = SysConfigCache.getProp(downloadMediaUrlKey);
        String accessToken = SecretInfoCache.getToken(PropertyField.ACCESS_TOKEN_NAME);
        if(StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(serverId)){
            logger.error(log.concat("获取微信服务器图片的关键信息为空!"));
        }

        url = url.concat("?access_token="+accessToken+"&media_id="+serverId);
        return HttpUtils.execute(url, "", 2000, "file");
    }
}
