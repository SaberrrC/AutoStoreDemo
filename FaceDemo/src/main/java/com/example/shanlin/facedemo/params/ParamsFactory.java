package com.example.shanlin.facedemo.params;

import org.xutils.http.RequestParams;

public class ParamsFactory {
	
	private static ParamsFactory factory = null;
	private ParamsFactory(){}
	public static ParamsFactory getInstance(){
		if(factory == null)
			factory = new ParamsFactory();
		return factory;
	}
	
	public RequestParams getParams(String uri){
		return new RequestParams(uri);
	}
	
}
