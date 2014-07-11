package kr.kjw.ssm.masterprogram.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class NetworkModel {
	private static NetworkModel instance;

	public static final int MAX_THREAD_COUNT = 5;

	private ArrayList<NetworkRequest> requestQueue = new ArrayList<NetworkRequest>();

	public static NetworkModel getInstance() {
		if (instance == null)
			instance = new NetworkModel();

		return instance;
	}
	
	public NetworkModel() {
		new Thread(new NetworkGetProcess()).start();
	}

	public synchronized boolean enqueue(NetworkRequest request) {
		
		synchronized (requestQueue) {
			requestQueue.add(request);
		}
		
		notifyAll();
		return true;
	}
	
	public synchronized NetworkRequest dequeue() {
		NetworkRequest request;
		
		if( requestQueue.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return null;
			}
		}
		synchronized (requestQueue ) {
			request = requestQueue.remove(0);
		}
		
		return request;
	}
	
	private class NetworkGetProcess implements Runnable {

		boolean isRunning = true;
		
		@SuppressWarnings("rawtypes")
		@Override
		public void run() {
			NetworkGetRequest request = null;
			while ( isRunning ) {
				request = (NetworkGetRequest) dequeue();
				if(request == null)
					continue;
				URL url = request.getURL();
				int retry = 3;
				
				while ( retry > 0) {
					try {
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setRequestMethod(request.getRequestMethod());
						conn.setConnectTimeout(request.getConnectionTimeout());
						conn.setReadTimeout(request.getReadTimeout());
						int resCode = conn.getResponseCode();
						if(resCode == HttpURLConnection.HTTP_OK) {
							InputStream is = conn.getInputStream();
							request.parsing(is);
							retry = 0;
							request.onSuccess();
							requestQueue.remove(request);
						} else {
							request.onError();
							retry = 0;
							requestQueue.remove(request);
						}
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					retry--;
				}
			}
		}
		
	}
}
