package kr.kjw.ssm.masterprogram.calloff;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import kr.kjw.ssm.masterprogram.MasterData;
import kr.kjw.ssm.masterprogram.network.NetworkConstant;
import kr.kjw.ssm.masterprogram.network.NetworkGetRequest;

public class CallOffRequest extends NetworkGetRequest<ArrayList<MasterData>>{

	private MasterData queryData;
	public CallOffRequest(ArrayList<MasterData> data, MasterData queryData) {
		super(data);
		this.queryData = queryData;
	}

	@Override
	public boolean parsingGetRequest(InputStream is, ArrayList<MasterData> result) {
		return false;
	}

	@Override
	public URL getServerURL() {
		try {
			System.out.println(NetworkConstant.serverURL + "/calloff?num=" + queryData.phoneNumber + "&regid=" + queryData.gcmKey);
			return new URL(NetworkConstant.serverURL + "/calloff?num=" + queryData.phoneNumber + "&regid=" + queryData.gcmKey);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
