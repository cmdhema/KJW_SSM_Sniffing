package kr.ssm.sniffing.calling;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import kr.ewhapp.kjw.bunobuno.network.NetworkConstant;
import kr.ewhapp.kjw.bunobuno.network.PostNetworkRequest;
import kr.ssm.sniffing.userinfo.UserData;

import org.json.JSONException;
import org.json.JSONObject;


public class CallingNetworkRequest extends PostNetworkRequest<UserData>{

	private UserData data;
	
	public CallingNetworkRequest(UserData data) {
		super(data);
		this.data = data;
	}

	@Override
	public boolean parsingPostReqeuest(InputStream is, UserData result) {
		return true;
	}

	@Override
	public JSONObject getRequestJSONObject() {
		JSONObject object = new JSONObject();
		JSONObject dataObject = new JSONObject();
		
		try {
			dataObject.put("user_number", data.phoneNumber);
			dataObject.put("other_number", data.otherNumber);
			dataObject.put("other_name", data.otherName);
			dataObject.put("date", data.date);
			object.put("object", dataObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}

	@Override
	public URL getServerURL() {
		try {
			return new URL(NetworkConstant.serverURL + "/call");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}



}
