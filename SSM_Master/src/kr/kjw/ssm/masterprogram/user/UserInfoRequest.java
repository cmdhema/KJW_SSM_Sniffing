package kr.kjw.ssm.masterprogram.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import kr.kjw.ssm.masterprogram.MasterData;
import kr.kjw.ssm.masterprogram.network.NetworkConstant;
import kr.kjw.ssm.masterprogram.network.NetworkGetRequest;

public class UserInfoRequest extends NetworkGetRequest<ArrayList<MasterData>>{

	private String query;
	
	public UserInfoRequest(ArrayList<MasterData> data, String query) {
		super(data);
		this.query= query;
	}

	@Override
	public boolean parsingGetRequest(InputStream is, ArrayList<MasterData> result) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder jsonBuf = new StringBuilder();

		String line = "";

		try {
			while ((line = br.readLine()) != null)
				jsonBuf.append(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JSONArray jArray = (JSONArray) JSONValue.parse(jsonBuf.toString());
		for(int i=0;i<jArray.size();i++) {
			JSONObject jObject = (JSONObject) jArray.get(i);
			MasterData userData = new MasterData();
			userData.phoneNumber = (String) jObject.get("userNumber");
			userData.agency = (String) jObject.get("agency");
			userData.countryName = (String) jObject.get("country");
			userData.cityName = (String) jObject.get("city");
			userData.townName = (String) jObject.get("town");
			userData.longitude = (String) jObject.get("longitude");
			userData.latitude = (String) jObject.get("latitude");
			userData.gcmKey = (String) jObject.get("GCM_Key");
			userData.date = (String) jObject.get("date");
			
			result.add(userData);
		}
		return true;
	}

	@Override
	public URL getServerURL() {
		try {
			return new URL(NetworkConstant.serverURL + "/info?num=" + query);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
