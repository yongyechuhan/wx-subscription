package com.jxyd.wx.model;

import javax.persistence.*;

@Table(name = "pic_info")
public class PicInfo {
    /**
     * 图片唯一编号
     */
    @Id
    @Column(name = "pic_id")
    private String picId;

    /**
     * 图片名称
     */
    @Column(name = "pic_name")
    private String picName;

    /**
     * 图片路径
     */
    @Column(name = "pic_src")
    private String picSrc;

    /**
     * 微信公开号
     */
    @Column(name = "open_id")
    private String openId;

    /**
     * 上传时间
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * 获取图片唯一编号
     *
     * @return pic_id - 图片唯一编号
     */
    public String getPicId() {
        return picId;
    }

    /**
     * 设置图片唯一编号
     *
     * @param picId 图片唯一编号
     */
    public void setPicId(String picId) {
        this.picId = picId;
    }

    /**
     * 获取图片名称
     *
     * @return pic_name - 图片名称
     */
    public String getPicName() {
        return picName;
    }

    /**
     * 设置图片名称
     *
     * @param picName 图片名称
     */
    public void setPicName(String picName) {
        this.picName = picName;
    }

    /**
     * 获取图片路径
     *
     * @return pic_src - 图片路径
     */
    public String getPicSrc() {
        return picSrc;
    }

    /**
     * 设置图片路径
     *
     * @param picSrc 图片路径
     */
    public void setPicSrc(String picSrc) {
        this.picSrc = picSrc;
    }

    /**
     * 获取微信公开号
     *
     * @return open_id - 微信公开号
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * 设置微信公开号
     *
     * @param openId 微信公开号
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * 获取上传时间
     *
     * @return create_time - 上传时间
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * 设置上传时间
     *
     * @param createTime 上传时间
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}