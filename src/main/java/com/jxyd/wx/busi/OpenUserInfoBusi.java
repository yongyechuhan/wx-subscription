package com.jxyd.wx.busi;

import com.alibaba.fastjson.JSONObject;
import com.jxyd.wx.domain.SessionUser;
import com.jxyd.wx.domain.UserOpenInfo;
import com.jxyd.wx.utils.PropertyField;
import com.jxyd.wx.utils.WxServerReqUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 用于初始化登录公众号的用户信息
 */
@Service
public class OpenUserInfoBusi {
    private Logger logger = LoggerFactory.getLogger(OpenUserInfoBusi.class);

    private static final String log = "初始化登录用户信息--";

    public boolean initOpenUserInfo(HttpServletRequest request, String code){
        // 通过code获取用户access_token
        JSONObject httpRes = WxServerReqUtil.reqGetUserToken(code);

        if(httpRes == null || !PropertyField.SUCCESS.equals(httpRes.get("respCode"))){
            logger.error(log.concat("获取oper_id异常"));
            return false;
        }

        String respBody = (String)httpRes.get("respBody");
        JSONObject res = JSONObject.parseObject(respBody);

        String accessToken = res.getString("access_token");
        if(StringUtils.isBlank(accessToken)){
            logger.error(log.concat("获取oper_id失败{}"), res.toJSONString());
            return false;
        }
        String expiresIn = res.getString("expires_in");
        String refreshToken = res.getString("refresh_token");
        String openId = res.getString("openid");

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

        // 构造SessionUser
        SessionUser sessionUser = new SessionUser(openId, nickName, headimgurl,
                accessToken, Long.valueOf(expiresIn), refreshToken);
        SessionUser.pushUser(request, sessionUser);
logger.info(log.concat(JSONObject.toJSONString(sessionUser)));
        UserOpenInfo userOpenInfo = new UserOpenInfo();
        userOpenInfo.setNickName(nickName);
        userOpenInfo.setProvince(province);
        userOpenInfo.setSex(sex);
        userOpenInfo.setCity(city);
        userOpenInfo.setCountry(country);
        userOpenInfo.setHeadimgurl(headimgurl);
        userOpenInfo.setUnionid(unionid);
        logger.info(log.concat(JSONObject.toJSONString(userOpenInfo)));
        request.getSession().setAttribute(UserOpenInfo.class.getName(), userOpenInfo);
        return true;
    }
}
