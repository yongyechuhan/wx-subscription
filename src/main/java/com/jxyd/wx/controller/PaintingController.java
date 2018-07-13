package com.jxyd.wx.controller;

import com.jxyd.wx.busi.OpenUserInfoBusi;
import com.jxyd.wx.busi.ServerPaingtingBusi;
import com.jxyd.wx.domain.PicInfForm;
import com.jxyd.wx.domain.SessionUser;
import com.jxyd.wx.domain.WeixinJDK;
import com.jxyd.wx.utils.PropertyField;
import com.jxyd.wx.utils.SHA1Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PaintingController {

    private static final Logger logger = LoggerFactory.getLogger(PaintingController.class);

    private static final String log = "画作分享--";

    @Autowired
    private ServerPaingtingBusi serverPaingtingBusi;

    @Autowired
    private OpenUserInfoBusi openUserInfoBusi;

    @RequestMapping("/showPaingtingIndex.do")
    public String showPaingtingIndex(HttpServletRequest request, String code){
        openUserInfoBusi.initOpenUserInfo(request, code);
        return "painting-list";
    }

    @RequestMapping("/showPaingtingComment.do")
    public String showPaingtingComment(){
        return "painting-comment";
    }

    @RequestMapping("/uploadPaingting.do")
    public ModelAndView uploadPaingting(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();

        // 获取jsapi必要的配置参数
        WeixinJDK weixinJDK = SHA1Util.jsApiSign(request.getRequestURL().toString());
        mav.addObject("weixinJDK", weixinJDK);

        mav.setViewName("painting-upload");
        return mav;
    }

    @ResponseBody
    @RequestMapping("/receivePaingting.do")
    public String receivePaingting(HttpServletRequest request, PicInfForm picInfForm){
        if(picInfForm == null){
            logger.error(log.concat("上传接收对象为空！"));
            return PropertyField.FAIL;
        }

        if(StringUtils.isEmpty(picInfForm.getServerId())){
            logger.error(log.concat("上传图片的服务器id为空！"));
            return PropertyField.FAIL;
        }

        // 从服务器拉取上传的图片
        String picId =
                serverPaingtingBusi.downloadPaingtingByServerId(picInfForm.getServerId());
        if(PropertyField.FAIL.equals(picId)){
            return PropertyField.FAIL;
        }

        String openId = SessionUser.getSessionUser(request).getOpenId();
        boolean saveRes = serverPaingtingBusi.savePaingting(picInfForm, picId, openId);
        return saveRes ? PropertyField.SUCCESS : PropertyField.FAIL;
    }
}
