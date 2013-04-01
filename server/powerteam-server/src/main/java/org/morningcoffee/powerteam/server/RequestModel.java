package org.morningcoffee.powerteam.server;

import com.google.gson.annotations.SerializedName;

public class RequestModel {
	@SerializedName("start_time")
	public long startTime;
	@SerializedName("end_time")
	public long endTime;
	@SerializedName("test_result")
	public String testResult;
	@SerializedName("user_name")
	public String userName;
	public String date;
	public String hash;
	public String type;
}
