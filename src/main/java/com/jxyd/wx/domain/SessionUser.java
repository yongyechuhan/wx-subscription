package com.jxyd.wx.domain;

import com.jxyd.wx.utils.WxServerReqUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 用于缓存当前登录的公众号信息
 */
public class SessionUser {
    private String openId;
    private String nickName;
    private String headImgUrl;
    private String accessToken;
    private String refreshToken;
    private long expireIn;
    private long createTime;

    public SessionUser(String openId, String nickName, String headImgUrl,
                       String accessToken, long expireIn, String refreshToken){
        setOpenId(openId);
        setNickName(nickName);
        setHeadImgUrl(headImgUrl);
        setAccessToken(accessToken);
        setExpireIn(expireIn);
        setRefreshToken(refreshToken);
        setCreateTime(System.currentTimeMillis() / 1000);
    }

    public static void pushUser(HttpServletRequest request, SessionUser sessionUser){
        request.getSession().setAttribute(SessionUser.class.getName(), sessionUser);
    }

    public static void popUser(HttpServletRequest request){
        request.getSession().removeAttribute(SessionUser.class.getName());
    }

    public static SessionUser getSessionUser(HttpServletRequest request){
        SessionUser sessionUser = (SessionUser)
                request.getSession().getAttribute(SessionUser.class.getName());
        if(!checkValid(sessionUser)){
            UserOpenInfo userOpenInfo = (UserOpenInfo)
                    request.getSession().getAttribute(UserOpenInfo.class.getName());
            refreshSessionUser(sessionUser, userOpenInfo);
        }
        return sessionUser;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(long expireIn) {
        this.expireIn = expireIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public static boolean checkValid(SessionUser sessionUser){
        long now = System.currentTimeMillis() / 1000;
        if(now - sessionUser.getCreateTime() >= sessionUser.getExpireIn()){
            return false;
        } else {
            return true;
        }
    }

    public static boolean refreshSessionUser(SessionUser sessionUser, UserOpenInfo userOpenInfo){
        return WxServerReqUtil.refreshUserOpenInfo(sessionUser, userOpenInfo);
    }
}
