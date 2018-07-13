package com.jxyd.wx.service;

import com.jxyd.wx.dao.PicInfoMapper;
import com.jxyd.wx.model.PicInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PicInfoDaoService {
    @Autowired
    private PicInfoMapper picInfoMapper;

    private static final Logger logger = LoggerFactory.getLogger(PicInfoDaoService.class);

    /**
     * 将提交图片的信息入库
     */
    public boolean saveUploadPic(PicInfo picInfo){
        try{
            picInfoMapper.insert(picInfo);
            return true;
        } catch (Exception e) {
            logger.error("保存图片信息失败", e);
        }
        return false;
    }
}
