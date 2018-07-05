package com.jxyd.wx.domain;

import com.alibaba.dubbo.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WxServerRespInfo {
    private String signature;
    private String timestamp;
    private String nonce;
    private String echostr;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getEchostr() {
        return echostr;
    }

    public void setEchostr(String echostr) {
        this.echostr = echostr;
    }

    public boolean paramEmptyCheck(){
        return StringUtils.isBlank(signature) || StringUtils.isBlank(timestamp)
                || StringUtils.isBlank(nonce) || StringUtils.isBlank(echostr);
    }

    public List<String> getSortList(){
        List<String> sortList = new ArrayList<>();
        sortList.add(signature);
        sortList.add(timestamp);
        sortList.add(nonce);
        sortList.add(echostr);

        Collections.sort(sortList);
        return sortList;
    }
}
