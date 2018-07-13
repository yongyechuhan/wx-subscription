package com.jxyd.wx.utils;

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
	public static JSONObject execute(String url, String msg, int timeout, String reqType) {

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
				respJson.put("respCode", PropertyField.SUCCESS);
				if("file".equals(reqType)){
					respJson.put("respBody", resp.getEntity().getContent());
				} else {
					respJson.put("respBody", respString);
				}
				return respJson;
			}
			respJson.put("respCode", statusCode);
			respJson.put("respDesc", respString);
			return respJson;
		} catch (Exception e) {
			logger.error("发送http(s)发生异常：", e);
			respJson.put("respCode", PropertyField.EXCEPTION);
			respJson.put("respDesc", e.getMessage());
			return respJson;
		}
	}
}
