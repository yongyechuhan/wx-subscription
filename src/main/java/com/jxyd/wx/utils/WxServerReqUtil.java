package com.jxyd.wx.utils;

import com.alibaba.fastjson.JSONObject;
import com.jxyd.wx.domain.SessionUser;
import com.jxyd.wx.domain.UserOpenInfo;
import com.jxyd.wx.utils.cache.SecretInfoCache;
import com.jxyd.wx.utils.cache.SysConfigCache;
import org.apache.commons.lang3.StringUtils;
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

    private static final String openIdUrlKey = "open_id_url";

    private static final String openUserUrlKey = "open_user_url";

    private static final String refreshUserUrlKey = "refresh_open_token_url";

    /**
     * 获取全局授权access_token
     * @return
     */
    public static JSONObject reqGetToken(){
        String url = SysConfigCache.getProp(tokenUrlKey);
        String appId = SysConfigCache.getProp(PropertyField.APP_ID_KEY);
        String appSecret = SysConfigCache.getProp(PropertyField.APP_SECRET_KEY);

        if(StringUtils.isBlank(url) || StringUtils.isBlank(appId) || StringUtils.isBlank(appSecret)){
            logger.error(log.concat("获取access_token的关键信息读取失败"));
            return null;
        }

        url = url.concat("&appid="+appId+"&secret="+appSecret);
        return HttpUtils.execute(url, "", 2000, "text");
    }

    public static JSONObject reqGetUserToken(String code){
        String url = SysConfigCache.getProp(openIdUrlKey);
        String appId = SysConfigCache.getProp(PropertyField.APP_ID_KEY);
        String appSecret = SysConfigCache.getProp(PropertyField.APP_SECRET_KEY);

        if(StringUtils.isBlank(url) || StringUtils.isBlank(appId)
                || StringUtils.isBlank(appSecret) || StringUtils.isBlank(code)){
            logger.error(log.concat("获取用户open_id的关键信息读取失败"));
            return null;
        }

        url = url.concat("appid="+appId+"&secret="+appSecret+"&code="+code
                +"&grant_type=authorization_code");
        return HttpUtils.execute(url, "", 2000, "text");
    }

    public static JSONObject reqGetUserOpenInfo(String accessToken, String openId){
        String url = SysConfigCache.getProp(openUserUrlKey);

        if(StringUtils.isBlank(url) || StringUtils.isBlank(accessToken) || StringUtils.isBlank(openId)){
            logger.error(log.concat("获取用户开放信息的关键信息读取失败"));
            return null;
        }

        url = url.concat("access_token="+accessToken+"&openid="+openId+
                "&lang=zh_CN");
        return HttpUtils.execute(url, "", 2000, "text");
    }

    public static boolean refreshUserOpenInfo(SessionUser sessionUser, UserOpenInfo userOpenInfo){
        String url = SysConfigCache.getProp(refreshUserUrlKey);
        String appId = SysConfigCache.getProp(PropertyField.APP_ID_KEY);
        String refreshToken = sessionUser.getRefreshToken();

        if(StringUtils.isBlank(url) || StringUtils.isBlank(appId) || StringUtils.isBlank(refreshToken)){
            logger.error(log.concat("刷新用户开放信息的关键信息读取失败"));
            return false;
        }

        url = url.concat("appid="+appId+"&refresh_token="+refreshToken+
                "&grant_type=refresh_token");
        JSONObject httpRes = HttpUtils.execute(url, "", 2000, "text");
        if(httpRes == null || !PropertyField.SUCCESS.equals(httpRes.get("respCode"))){
            logger.error(log.concat("刷新用户开放信息异常"));
            return false;
        }
        String respBody = (String)httpRes.get("respBody");
        JSONObject res = JSONObject.parseObject(respBody);
        String accessToken = res.getString("access_token");
        if(StringUtils.isBlank(accessToken)){
            logger.error(log.concat("刷新用户token失败{}"), res.toJSONString());
            return false;
        }
        String expiresIn = res.getString("expires_in");
        refreshToken = res.getString("refresh_token");
        String openId = res.getString("openid");

        // 刷新用户所有基本信息
        httpRes = WxServerReqUtil.reqGetUserOpenInfo(accessToken, openId);
        if(httpRes == null || !PropertyField.SUCCESS.equals(httpRes.get("respCode"))){
            logger.error(log.concat("获取用户开放信息异常"));
            return false;
        }
        respBody = (String)httpRes.get("respBody");
        res = JSONObject.parseObject(respBody);

        openId = res.getString("openid");
        if(StringUtils.isBlank(openId)){
            logger.error(log.concat("获取用户开放信息失败{}"), res.toJSONString());
            return false;
        }
        String nickName = res.getString("nickname");
        String sex = res.getString("sex");
        String province = res.getString("province");
        String city = res.getString("city");
        String country = res.getString("country");
        String headimgurl = res.getString("headimgurl");
        String unionid = res.getString("unionid");

        // 刷新SessionUser
        sessionUser.setCreateTime(System.currentTimeMillis() / 1000);
        sessionUser.setNickName(nickName);
        sessionUser.setHeadImgUrl(headimgurl);
        sessionUser.setExpireIn(Long.valueOf(expiresIn));
        sessionUser.setOpenId(openId);
        sessionUser.setAccessToken(accessToken);
        sessionUser.setRefreshToken(refreshToken);

        // 刷新UserOpenInfo
        userOpenInfo.setOpenId(openId);
        userOpenInfo.setSex(sex);
        userOpenInfo.setProvince(province);
        userOpenInfo.setCity(city);
        userOpenInfo.setCountry(country);
        userOpenInfo.setHeadimgurl(headimgurl);
        userOpenInfo.setUnionid(unionid);
        return true;
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
            return null;
        }

        url = url.concat("?access_token="+accessToken+"&media_id="+serverId);
        return HttpUtils.execute(url, "", 2000, "file");
    }
}
