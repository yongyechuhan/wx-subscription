package com.jxyd.wx.busi;

import com.alibaba.fastjson.JSONObject;
import com.jxyd.wx.domain.PicInfForm;
import com.jxyd.wx.model.PicInfo;
import com.jxyd.wx.service.PicInfoDaoService;
import com.jxyd.wx.utils.PropertyField;
import com.jxyd.wx.utils.SHA1Util;
import com.jxyd.wx.utils.WxServerReqUtil;
import com.jxyd.wx.utils.cache.SysConfigCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 负责从服务器获取图片流，储存到本地及数据库
 */
@Service
public class ServerPaingtingBusi {

    private Logger logger = LoggerFactory.getLogger(ServerPaingtingBusi.class);

    private static final String log = "上传图片处理--";

    @Autowired
    private PicInfoDaoService picInfoDaoService;

    @Value("#{sysConfig.wx_pic_path}")
    private String picSavePath;

    public boolean savePaingting(PicInfForm picInfForm, String picId, String openId){
        PicInfo picInfo = new PicInfo();
        picInfo.setOpenId(openId);
        picInfo.setPicDesc(picInfForm.getPicDesc());
        picInfo.setPicName(picInfForm.getPicName());
        picInfo.setPicId(picId);
        picInfo.setPicSrc(SysConfigCache.getProp("wx_pic_path").concat(picId));

        return picInfoDaoService.saveUploadPic(picInfo);
    }

    public String downloadPaingtingByServerId(String serverId){
        // 请求服务器
        JSONObject httpRes = WxServerReqUtil.downloadServerPic(serverId);
        if(httpRes == null || !PropertyField.SUCCESS.equals(httpRes.get("respCode"))){
            return PropertyField.FAIL;
        }

        InputStream is = (InputStream) httpRes.get("respBody");
        String picName = SHA1Util.create_nonce_str().concat("jpg");
        String picSavePath = SysConfigCache.getProp("wx_pic_path");
        File file = new File(picSavePath.concat(picName));
        FileOutputStream fos = null;
        try{
            if(is == null){
                logger.error(log.concat("从服务器拉取图片为空"));
            }
            fos = new FileOutputStream(file);
            byte[] picArray = new byte[1024];
            int readLen = -1;
            while((readLen = is.read(picArray, 0, picArray.length)) != -1){
                fos.write(picArray, 0, readLen);
            }
            is.close();
            fos.flush();
            return picName;
        } catch (Exception e) {
            logger.error(log.concat("保存图片失败"), e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null){
                    fos.close();
                }
            } catch (Exception e) {
                logger.error(log.concat("流关闭失败"), e);
            }
        }
        return PropertyField.FAIL;
    }
}
