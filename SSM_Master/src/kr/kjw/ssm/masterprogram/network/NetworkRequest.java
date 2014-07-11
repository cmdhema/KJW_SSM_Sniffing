package kr.kjw.ssm.masterprogram.network;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class NetworkRequest {

	private boolean mResult;
	public HttpURLConnection conn;
	public boolean isCanceled;

	public abstract URL getURL();

	public abstract boolean parsing(InputStream is);

	public abstract String getRequestMethod();

	public abstract void onSuccess();

	public abstract void onError();

	public void process(InputStream is) {
		mResult = parsing(is);
	}

	public void setOutput(HttpURLConnection conn) {
		conn.setDoOutput(true);
		conn.setDoInput(true);
	}

	public void setRequestProertpy(HttpURLConnection conn) {
		conn.setRequestProperty("Cache-Control", "no-cache");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
	}

	public int getConnectionTimeout() {
		return 30000;
	}

	public int getReadTimeout() {
		return 30000;
	}

	public void setCancel() {
		isCanceled = true;
		if (conn != null)
			conn.disconnect();
	}

	public void sendResult() {
		if (isCanceled)
			return;
		if (mResult)
			onSuccess();
		else
			onError();
	}

	public boolean isCancel() {
		return isCanceled;
	}
}
