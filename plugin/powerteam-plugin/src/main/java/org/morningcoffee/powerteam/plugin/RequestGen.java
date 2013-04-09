package org.morningcoffee.powerteam.plugin;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;


public class RequestGen {
	
	@SerializedName("start_time")
	private long startTime;
	@SerializedName("end_time")
	private long endTime;
	@SerializedName("test_result")
	private String testResult;
	@SerializedName("user_name")
	private String userName;
	@SuppressWarnings("unused")
	private String type = "plugin";
	
	public RequestGen (long startTime, long endTime, String testResult, String userName) 
	{
		this.startTime = startTime;
		this.endTime = endTime;
		this.testResult = testResult;
		this.userName = userName;
	}
	
	public String getJSON()
	{
		Gson gson = new Gson();
		
		return gson.toJson(this);
	}
}