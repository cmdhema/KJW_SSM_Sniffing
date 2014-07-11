package kr.ewhapp.kjw.bunobuno.network;

import java.io.InputStream;
import java.net.URL;

import org.json.JSONObject;

public abstract class PostNetworkRequest<T> extends NetworkRequest{

	private T result;
	
	private OnPostMethodProcessListener<T> listener;
	
	public abstract boolean parsingPostReqeuest(InputStream is, T result);
	public abstract JSONObject getRequestJSONObject();
	public abstract URL getServerURL();

	
	public T getResult() {
		return result;
	}
	
	public PostNetworkRequest(T data) {
		this.result = data;
	}

	@Override
	public URL getURL() {
		return getServerURL();
	}

	@Override
	public boolean parsing(InputStream is) {
		return parsingPostReqeuest(is, result);
	}

	@Override
	public String getRequestMethod() {
		return "POST";
	}

	@Override
	public void onSuccess() {
		listener.onPostMethodProcessSuccess(this);
	}

	@Override
	public void onError() {
		listener.onPostMethodProcessError();
	}
	
	public void setOnPostMethodProcessListener(OnPostMethodProcessListener<T> listener) {
		this.listener = listener;
	}

	public interface OnPostMethodProcessListener<T> {
		public void onPostMethodProcessSuccess(PostNetworkRequest<T> request);
		public void onPostMethodProcessError();
	}
}
