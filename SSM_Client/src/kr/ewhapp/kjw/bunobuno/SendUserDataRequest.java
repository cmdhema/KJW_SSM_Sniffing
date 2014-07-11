package kr.ewhapp.kjw.bunobuno;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import kr.ewhapp.kjw.bunobuno.network.NetworkConstant;
import kr.ewhapp.kjw.bunobuno.network.PostNetworkRequest;
import kr.ssm.sniffing.userinfo.UserData;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SendUserDataRequest extends PostNetworkRequest<UserData> {

	private UserData data;

	public SendUserDataRequest(UserData data) {
		super(data);
		this.data = data;
	}

	@Override
	public boolean parsingPostReqeuest(InputStream is, UserData result) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder jsonBuf =  new StringBuilder();
		
		String line = "";
		
		try {
			while((line = br.readLine()) != null)
				jsonBuf.append(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Log.i("SendUserDataRequest", jsonBuf.toString());
		
		try {
			JSONObject jData = new JSONObject(jsonBuf.toString());
			result.resultCode = jData.getInt("result");
		} catch (JSONException je) {
			Log.e("SendUserDataRequest", "JSON파싱중 에러 발생", je);
		}
		return true;
	}

	@Override
	public JSONObject getRequestJSONObject() {
		
		JSONObject object = new JSONObject();
		JSONObject userObject = new JSONObject();

		try {
			userObject.put("number", data.phoneNumber);
			userObject.put("agency", data.agency);
			userObject.put("country", data.countryName);
			userObject.put("city", data.cityName);
			userObject.put("town", data.townName);
			userObject.put("longitude", data.longitude);
			userObject.put("latitude", data.latitude);
			userObject.put("date", data.date);
			userObject.put("gcm_key", data.regId);
			object.put("object", userObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return object;
	}

	@Override
	public URL getServerURL() {
		try {
			return new URL(NetworkConstant.serverURL + "/user/");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
