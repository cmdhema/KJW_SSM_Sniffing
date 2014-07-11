package kr.ssm.sniffing.photo;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import kr.ewhapp.kjw.bunobuno.NumberData;
import kr.ewhapp.kjw.bunobuno.network.MultiPartNetworkRequest;
import kr.ewhapp.kjw.bunobuno.network.NetworkConstant;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;


public class SendPhotoNetworkRequest extends MultiPartNetworkRequest<NumberData> {

	private NumberData data;
	
	public SendPhotoNetworkRequest(NumberData list) {
		super(list);
		data = list;
	}

	@Override
	public MultipartEntity getMultipartEntity() {
		
		MultipartEntity multiEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, null);
		File file = new File(data.uri);
		FileBody fileBody = new FileBody(file);

		try {
			multiEntity.addPart("phoneNumber", new StringBody(data.phoneNumber));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		multiEntity.addPart("photo", fileBody);
		
		return multiEntity;
	}

	@Override
	public boolean parsingMultiPartRequest(InputStream is, NumberData result) {
		
		return true;
	}

	@Override
	public URL getServerURL() {
		try {
			return new URL(NetworkConstant.serverURL + "/photo");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
