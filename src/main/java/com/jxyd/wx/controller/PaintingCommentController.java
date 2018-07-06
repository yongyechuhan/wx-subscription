package com.jxyd.wx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PaintingCommentController {
    @RequestMapping("/showPaingtingIndex.do")
    public String showPaingtingIndex(){
        return "painting-comment";
    }
}
