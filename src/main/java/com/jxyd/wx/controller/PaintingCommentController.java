package com.jxyd.wx.controller;

import com.jxyd.wx.domain.WeixinJDK;
import com.jxyd.wx.utils.SHA1Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PaintingCommentController {
    @RequestMapping("/showPaingtingIndex.do")
    public String showPaingtingIndex(){
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
}
