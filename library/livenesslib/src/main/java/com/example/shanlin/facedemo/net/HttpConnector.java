package com.example.shanlin.facedemo.net;

import org.xutils.common.Callback;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import java.io.File;

public interface HttpConnector {
	/**
	 * 
	 * @param params 参数集
	 * @param savePath 下载文件放置的位置
	 * @param autoResume 自动断点续传
	 * @param callback 回调
	 */
	public void download(RequestParams params, File savePath, boolean autoResume, CommonCallback<?> callback);
	/**
	 * 
	 * @param params 参数集
	 * @param diskFile 等待上传文件放置的位置
	 * @param callback 回调
	 */
	public void upload(RequestParams params, File diskFile, CommonCallback<?> callback);
	/**
	 *
	 * @param params 参数集
	 * @param callBack 回调
	 */
	public void post(RequestParams params, CommonCallback<?> callBack);/**

	/**
	 * 
	 * @param params 参数集
	 * @param callBack 回调
	 */
	public void get(RequestParams params, CommonCallback<?> callBack);
//	/**
//	 * @param callback 回调
//	 * @param url 等待上传的url
//	 * @param body 上传的json体
//	 * @param callBack 回调
//	 */
//	public void post(String url,String body,CommonCallback<?> callBack);
	public void get(RequestParams params);

	public Callback.Cancelable postCancelable(RequestParams params, CommonCallback<?> callBack);

	public Callback.Cancelable getCancelable(RequestParams params, CommonCallback<?> callBack);
}
