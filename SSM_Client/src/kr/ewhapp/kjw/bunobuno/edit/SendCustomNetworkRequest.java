package kr.ewhapp.kjw.bunobuno.edit;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import kr.ewhapp.kjw.bunobuno.NumberData;
import kr.ewhapp.kjw.bunobuno.network.NetworkConstant;
import kr.ewhapp.kjw.bunobuno.network.PostNetworkRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class SendCustomNetworkRequest extends PostNetworkRequest<NumberData>{

	private NumberData data;
	
	public SendCustomNetworkRequest(NumberData data) {
		super(data);
		this.data = data;
	}

	@Override
	public boolean parsingPostReqeuest(InputStream is, NumberData result) {
		return true;
	}

	@Override
	public JSONObject getRequestJSONObject() {

		JSONObject object = new JSONObject();
		JSONObject userObject = new JSONObject();
		
		try {
			userObject.put("number", data.phoneNumber);
			userObject.put("name", data.name);
			userObject.put("password", data.number);
			object.put("object", userObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return object;
	}

	@Override
	public URL getServerURL() {
		try {
			return new URL(NetworkConstant.serverURL + "/custom");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
