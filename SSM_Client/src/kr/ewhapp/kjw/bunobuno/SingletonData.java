package kr.ewhapp.kjw.bunobuno;

import java.util.ArrayList;

import kr.ssm.sniffing.userinfo.UserData;

public class SingletonData {
	
	private static SingletonData instance;
	
	private ArrayList<NumberData> accountNumberList;
	private ArrayList<NumberData> sitePasswordList;
	private ArrayList<NumberData> customNumberList;
	private UserData userData;
	
	private SingletonData () {
		accountNumberList = new ArrayList<NumberData>();
		sitePasswordList = new ArrayList<NumberData>();
		customNumberList = new ArrayList<NumberData>();
		userData = new UserData();
	}
	
	public static SingletonData getInstance () {
		if( instance == null) 
			instance = new SingletonData();	
		return instance;
	}

	public UserData getUserData() {
		return userData;
	}

	public ArrayList<NumberData> getAccountNumberList() {
		return accountNumberList;
	}

	public ArrayList<NumberData> getSitePasswordList() {
		return sitePasswordList;
	}

	public ArrayList<NumberData> getCustomNumberList() {
		return customNumberList;
	}
	
	
}
