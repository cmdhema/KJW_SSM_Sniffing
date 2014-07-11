package kr.kjw.ssm.masterprogram.photo;

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

public class PhotoRequest extends NetworkGetRequest<ArrayList<MasterData>>{

	private String query;
	public PhotoRequest(ArrayList<MasterData> data, String query) {
		super(data);
		this.query = query;
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
			userData.phoneNumber = (String) jObject.get("phoneNumber");
			String photoName = (String) jObject.get("photoName");
			userData.photoUrl = NetworkConstant.serverURL + "/user/photo?name=" + photoName;
			result.add(userData);
		}
		return true;
	}

	@Override
	public URL getServerURL() {
		try {
			return new URL(NetworkConstant.serverURL + "/photo?num=" + query);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
