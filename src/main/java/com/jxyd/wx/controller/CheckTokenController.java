package com.jxyd.wx.controller;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.jxyd.wx.domain.WxServerRespInfo;
import com.jxyd.wx.exception.AesException;
import com.jxyd.wx.utils.SHA1Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CheckTokenController {

    private Logger logger = LoggerFactory.getLogger(CheckTokenController.class);

    private static final String token = "youxiu13717891108";

    private static String log = "微信token校验--";

    @ResponseBody
    @RequestMapping("/checkToken.do")
    private String checkWxServerToken(WxServerRespInfo wxSendInf) throws AesException {
        if(wxSendInf == null){
            logger.error(log.concat("非官方服务器返回，消息可能被篡改"));
            return null;
        }

        if(wxSendInf.paramEmptyCheck()){
            logger.error(log.concat("非官方服务器返回，消息可能被篡改"));
            return null;
        }

        String hashCode = SHA1Util.getSHA1(token, wxSendInf.getTimestamp(),
                wxSendInf.getNonce(), "");
        if(StringUtils.isNotEmpty(hashCode) && wxSendInf.getSignature().equals(hashCode)){
            return wxSendInf.getEchostr();
        } else {
            logger.error(log.concat("token验证失败，消息可能被篡改"));
            throw new AesException(AesException.ValidateSignatureError);
        }
    }
}
