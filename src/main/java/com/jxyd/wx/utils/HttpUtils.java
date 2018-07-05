package com.jxyd.wx.utils;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 
 * @author yufeng
 *
 */
public class HttpUtils {

	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

	/**
	 * 发送http请求
	 * 
	 * @param url
	 * @param msg
	 * @param timeout
	 * @return json:respCode,respDesc,respBody
	 */
	public static JSONObject execute(String url, String msg, int timeout) {

		HttpClient httpClient = HttpClients.createDefault();
		HttpPost method = new HttpPost(url);

		StringEntity entity = new StringEntity(msg, "utf-8");// 解决中文乱码问题
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		method.setEntity(entity);

		JSONObject respJson = new JSONObject();
		String respString = null;
		try {
			// 设置超时时间
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
					.build();
			method.setConfig(requestConfig);

			HttpResponse resp = httpClient.execute(method);
			int statusCode = resp.getStatusLine().getStatusCode();
			respString = EntityUtils.toString(resp.getEntity(), "UTF-8");
			// 通讯成功
			if (HttpStatus.SC_OK == statusCode) {
				respJson.put("respCode", "SUCCESS");
				respJson.put("respBody", respString);
				return respJson;
			}
			respJson.put("respCode", statusCode);
			respJson.put("respDesc", respString);
			return respJson;
		} catch (Exception e) {
			logger.error("发送http(s)发生异常：", e);
			respJson.put("respCode", "EXCEPTION");
			respJson.put("respDesc", e.getMessage());
			return respJson;
		}
	}

	/**
	 * 短信发送http接口
	 * @param url
	 * @param tos	手机号，用“,”分隔
	 * @param content
	 * @return
	 */
	public static JSONObject execute(String url, String tos, String content){
		JSONObject resp = new JSONObject();
		if (StringUtils.isBlank(tos)){
			resp.put("respCode", "FAIL");
			resp.put("respDesc", "手机号为空！");
			return resp;
		}
		url += "?tos="+ tos + "&";
		if (StringUtils.isBlank(content)){
			resp.put("respCode", "FAIL");
			resp.put("respDesc", "短信内容为空！");
			return resp;
		}
		try {
			content = URLEncoder.encode(content, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("短信content转义异常:", e);
			resp.put("respCode", "FAIL");
			resp.put("respDesc", "短信content转义异常！");
			return resp;
		}
		url += "content="+ content;
		return execute(url, "", 6000);
	}

	public static void main(String [] args){
		String url = "http://222.66.72.115:9096/sms/falconSend"; //141接口
//		String url = "http://localhost:8080/sms/falconSend";

		JSONObject jsonObject = HttpUtils.execute(url, "18288888881", "系统当前未入账笔数【1】,策略标签【】未入账笔数   1");
		if ("SUCCESS".equalsIgnoreCase(jsonObject.get("respCode").toString())){
			String respBody = jsonObject.get("respBody").toString();
			System.out.println(respBody);

			JSONObject body = JSONObject.parseObject(respBody);
			if ("0000".equals(body.get("retCode"))){
				System.out.println("短信发送成功");
			}else {
				System.out.println("短信发送失败");
			}
			System.out.println(body.get("retMsg"));
		}
	}
}
