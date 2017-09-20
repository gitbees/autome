package com.test.stepDefs;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;

public enum PackageInfo {
	
	CHASE("ChaseHomePage","com.auto.pageObject.Chase"),
	AMAZON("AmazonHomePage","com.auto.pageObject.Amazon");
	
	private static Map<String, String> MESSAGE_MAPPING = new HashMap<String, String>(100);
	
	private static Map<String, PackageInfo> CODE_MAPPING = new HashMap<String, PackageInfo>(
			100);
	
	static{
		
		for(PackageInfo e : PackageInfo.values())
		{
			String[] codes = e.getCodes();
			
			for(String statusCode : codes){
				CODE_MAPPING.put(statusCode, e);
				String desc = e.getDescription();
				MESSAGE_MAPPING.put(statusCode, desc);
			}
		}
	}
	
	private final String code;
	
	private final String description;
	
	private final String[] searchCodes;
	
	
	private PackageInfo(String code, String description, String...searchCodes){
		this.code=code;
		this.description=description;
		this.searchCodes = searchCodes;
	}
	
	
	public String getCode(){
		return code;
	}
	
	
	public String[] getCodes(){
		return (String[]) ArrayUtils.add(searchCodes, code);
		
	}
	
	public String getDescription(){
		return description;
	}
	
	public static final String getDescription(String code){
		return MESSAGE_MAPPING.get(code);
	}
	
	public static final PackageInfo fromCode(String code){
		return CODE_MAPPING.get(code);
		
	}
	
	public static final String getCode(String status){
		PackageInfo pgInfo= PackageInfo.valueOf(status);
		if(pgInfo!=null)
			return pgInfo.getCode();
		return null;
		
	}
	
}
