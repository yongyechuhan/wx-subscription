package com.jxyd.wx.busi;

import com.alibaba.fastjson.JSONObject;
import com.jxyd.wx.utils.PropertyField;
import com.jxyd.wx.utils.SHA1Util;
import com.jxyd.wx.utils.WxServerReqUtil;
import com.jxyd.wx.utils.cache.SysConfigCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public String downloadPaingtingByServerId(String serverId){
        // 请求服务器
        JSONObject httpRes = WxServerReqUtil.downloadServerPic(serverId);
        if(!PropertyField.SUCCESS.equals(httpRes.get("respCode"))){
            return PropertyField.FAIL;
        }

        InputStream is = (InputStream) httpRes.get("respBody");
        String picName = SHA1Util.create_nonce_str();
        String picSavePath = SysConfigCache.getProp("wx_pic_path");
        File file = new File(picSavePath);
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
