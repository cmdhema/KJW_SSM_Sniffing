package kr.ewhapp.kjw.bunobuno.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class NetworkModel {

	// �̱����� ���� instance
	private static NetworkModel instance;

	// ��Ʈ��ũ ��û�� ������ HashMap
	public HashMap<Context, ArrayList<NetworkRequest>> mRequestMap = new HashMap<Context, ArrayList<NetworkRequest>>();

	// �̹����� �ε��� �����带 �ִ� 5������ ����
	public static final int MAX_THREAD_COUNT = 5;

	// �̹����� �ѷ��ֱ� ���� Handler
	private Handler mHandler;

	// �̹��� ��û�� ������ ť
	ArrayList<ImageRequest> mQueue = new ArrayList<ImageRequest>();

	// ���� �������� �̹��� ��û �� ���� ť
	ArrayList<ImageRequest> mRunningQueue = new ArrayList<ImageRequest>();

	// �̱��� Instance�� ����
	public static NetworkModel getInstance() {
		if (instance == null)
			instance = new NetworkModel();

		return instance;
	}

	public NetworkModel() {

		// Handler�� main Thread�� Handler�� ����� ���� Main Thread�� Looper�� �̿��Ͽ�
		// Handler�� ����
		mHandler = new Handler(Looper.getMainLooper());

		for (int i = 0; i < MAX_THREAD_COUNT; i++)
			// �̹��� �ٿ�ε带 ���� ������ 5�� ����
			new Thread(new ImageDownTask()).start();
	}

	// �������� ������ ������ ��û�� ó���ϴ� ������ ����
	public void getNetworkData(Context context, NetworkRequest request) {
		request.context = context;
		ArrayList<NetworkRequest> list = mRequestMap.get(context);

		if (list == null) { // ���� ��Ʈ��ũ ��û�� ���� ���
			list = new ArrayList<NetworkRequest>();
			mRequestMap.put(context, list);
		}
		list.add(request);

		// ������ ������ ������ ����
		new GetInfoAsyncTask(request).execute(request);

	}

	// ������ ������ ������ ��û�� ó���ϴ� ������ ����
	public void sendNetworkData(Context context, NetworkRequest request) {
		request.context = context;
		ArrayList<NetworkRequest> list = mRequestMap.get(context);

		if (list == null) { // ���� ��Ʈ��ũ ��û�� ���� ���
			list = new ArrayList<NetworkRequest>();
			mRequestMap.put(context, list);
		}
		list.add(request);

		// ������ ������ ������ ����
		new SendToServerAsyncTask(request).execute(request);

	}

	public void sendPhotoData(Context context, NetworkRequest request) {
		request.context = context;
		ArrayList<NetworkRequest> list = mRequestMap.get(context);

		if (list == null) { // ���� ��Ʈ��ũ ��û�� ���� ���
			list = new ArrayList<NetworkRequest>();
			mRequestMap.put(context, list);
		}
		list.add(request);

		// ������ ������ ������ ����
		new SendToServerWithMultiPartTask().execute(request);
	}

	public void getImageData(Context context, ImageRequest request) {

		// ���� �̹����� ĳ�� ó���� ���ִ��� Ȯ���Ѵ�.
		Bitmap bm = CacheModule.getInstance().getCache(request);

		// ĳ�� �� �̹����� ���� ��� �� �̹����� �ѷ���
		if (bm != null) {
			request.setResult(bm);
			request.sendResult();
			return;
		}

		// ĳ�� ó���� �̹����� ���� ��� ��û�� �ؽ��ʿ� �ִ´�.
		request.context = context;
		ArrayList<NetworkRequest> list = mRequestMap.get(context);
		if (list == null) {
			list = new ArrayList<NetworkRequest>();
			mRequestMap.put(context, list);
		}
		list.add(request);

		// ť�� ������ ��û�� ���� ��� ��û ����
		for (ImageRequest req : mQueue) {
			if (req.isSameRequest(request))
				return;
		}

		// ť�� ������ ��û�� ���� ��� ��û ����
		for (ImageRequest req : mRunningQueue) {
			if (req.isSameRequest(request))
				return;
		}

		// ��û�� ť�� �ִ´�.
		enqueue(request);
	}

	public void removeImageRequest(ImageRequest request) {
		mQueue.remove(request);
	}

	public void cleanUpRequest(Context context) {
		ArrayList<NetworkRequest> list = mRequestMap.get(context);
		if (list != null) {
			for (NetworkRequest request : list) {
				request.setCancel();
			}
		}
	}

	// �̹��� ��û�� ť�� �ִ� �޼ҵ�
	public synchronized void enqueue(ImageRequest request) {
		mQueue.add(request);
		notify();
	}

	// �̹��� ��û�� ť���� �������� �޼ҵ�
	public synchronized ImageRequest dequeue() {
		while (mQueue.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		ImageRequest request = mQueue.remove(0);

		if (request != null)
			mRunningQueue.add(request);

		return request;

	}

	private class GetInfoAsyncTask extends AsyncTask<NetworkRequest, Integer, Boolean> {

		NetworkRequest mRequest;

		public GetInfoAsyncTask(NetworkRequest request) {
			mRequest = request;
		}

		@Override
		protected Boolean doInBackground(NetworkRequest... params) {
			NetworkRequest request = params[0];
			mRequest = request;
			URL url = request.getURL();
			int retry = 3;

			// �ִ� 3ȸ���� �� ������ �õ��Ѵ�.
			while (retry > 0) {
				try {
					// GET ������� �����ϱ� ���� ����
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod(request.getRequestMethod());
					conn.setConnectTimeout(request.getConnectionTimeout());
					conn.setReadTimeout(request.getReadTimeout());

					// ����ڰ� �������� ��� �۾� ����
					if (request.isCancel())
						return false;

					// ���� ����
					int resCode = conn.getResponseCode();
					if (request.isCancel())
						return false;

					// ���ӿ� �������� ���
					if (resCode == HttpURLConnection.HTTP_OK) {
						InputStream is = conn.getInputStream();

						request.process(is);
						return true;
					} else
						return false;
				} catch (IOException e) {
					e.printStackTrace();
				}

				// ���� �������� ��� ������ Ƚ�� 1 ����
				retry--;
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			if (result)
				mRequest.sendResult();
			else
				mRequest.sendError(0);

			ArrayList<NetworkRequest> list = mRequestMap.get(mRequest.context);
			list.remove(mRequest);

			super.onPostExecute(result);
		}

	}
	
	@SuppressWarnings("rawtypes")
	private class SendToServerAsyncTask extends AsyncTask<NetworkRequest, Integer, Boolean> {

		PostNetworkRequest mRequest;
		JSONObject mObject;

		public SendToServerAsyncTask(NetworkRequest request) {
//			mRequest = request;
		}

		@Override
		protected Boolean doInBackground(NetworkRequest... params) {

			NetworkRequest request = params[0];
			mRequest = (PostNetworkRequest) request;
			URL url = request.getURL();
			int retry = 3;

			// �ִ� 3ȸ���� �� ������ �õ��Ѵ�.
			while (retry > 0) {
				try {
					// POST ������� �����ϱ� ���� ����
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod(request.getRequestMethod());
					conn.setConnectTimeout(request.getConnectionTimeout());
					conn.setReadTimeout(request.getReadTimeout());
					conn.setDoOutput(true);
					conn.setDoInput(true);
					request.setRequestProertpy(conn);
					request.setOutput(conn);

					DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
					wr.write(mRequest.getRequestJSONObject().toString().getBytes());
					wr.flush();
					wr.close();
//					Log.e("JSONTest", mObject.toString());
					// ����ڰ� �������� ��� �۾� ����
					if (request.isCancel())
						return false;

					// ���� ����
					int resCode = conn.getResponseCode();
					if (request.isCancel())
						return false;
					// ���ӿ� �������� ���
					if (resCode == HttpURLConnection.HTTP_OK) {
						InputStream is = conn.getInputStream();
						request.process(is);
						Log.i("Send Object", "Success");
						return true;
					} else {
						Log.i("Send Object", resCode + "");
//						Log.e("JsonTest", mObject.toString());
						return false;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				// ���� �������� ��� ������ Ƚ�� 1 ����
				retry--;
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			if (result)
				mRequest.sendResult();
			else
				mRequest.sendError(0);

			ArrayList<NetworkRequest> list = mRequestMap.get(mRequest.context);
			list.remove(mRequest);

			super.onPostExecute(result);
		}
	}

	@SuppressWarnings("rawtypes")
	private class SendToServerWithMultiPartTask extends AsyncTask<NetworkRequest, Integer, Boolean> {

		MultiPartNetworkRequest mRequest;

		@Override
		protected Boolean doInBackground(NetworkRequest... params) {

			NetworkRequest request = params[0];
			mRequest = (MultiPartNetworkRequest) request;
			DefaultHttpClient httpClient = null;

			int retry = 3;

			StringBuilder jsonBuf = new StringBuilder();

			while (retry > 0) {
				try {
					httpClient = new DefaultHttpClient();
					HttpPost upLoadPost = new HttpPost(mRequest.getServerURL().toString());
					upLoadPost.setHeader("Connection", "Keep-Alive");
					upLoadPost.setHeader("Accept_Charset", "UTF-8");
					upLoadPost.setHeader("enctype", "multipart/form-data");
					MultipartEntity multiEntity = ((MultiPartNetworkRequest) request).getMultipartEntity();

					upLoadPost.setEntity(multiEntity);

					if (request.isCancel())
						return false;

					HttpResponse httpResponse = httpClient.execute(upLoadPost);

					if (request.isCancel())
						return false;

					int resultCode = httpResponse.getStatusLine().getStatusCode();

					if (request.isCancel())
						return false;

					if (resultCode == HttpStatus.SC_OK || resultCode == HttpStatus.SC_ACCEPTED) {
						HttpEntity responseBody = null;
						responseBody = httpResponse.getEntity();
						InputStream is = responseBody.getContent();

						BufferedReader rd = new BufferedReader(new InputStreamReader(is));
						String line;

						while ((line = rd.readLine()) != null) {
							jsonBuf.append(line);
						}

						Log.i("HttpHelperHandlerClass", "���ε� ����");
						rd.close();
						return true;
					} else {
						Log.i("HttpHelperHandlerClass", "���ε� ����" + resultCode);
						return false;
					}

				} catch (Exception e) {
					Log.e("UPLOAD", "���ε��� ���� �߻�", e);
				} finally {
					httpClient.getConnectionManager().shutdown();
				}

				retry--;

			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			if (result)
				mRequest.sendResult();
			else
				mRequest.sendError(0);

			ArrayList<NetworkRequest> list = mRequestMap.get(mRequest.context);
			list.remove(mRequest);

			super.onPostExecute(result);
		}
	}

	private class ImageDownTask implements Runnable {

		boolean isRunning = true;

		@Override
		public void run() {
			ImageRequest request = null;

			while (isRunning) {
				request = dequeue();
				if (request == null)
					continue;
				URL url = request.getURL();
				int retry = 3;

				while (retry > 0) {
					try {
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setRequestMethod(request.getRequestMethod());
						conn.setConnectTimeout(request.getConnectionTimeout());
						conn.setReadTimeout(request.getReadTimeout());
						request.setRequestHeader(conn);

						if (request.isCancel()) {
							retry = 0;
							continue;
						}
						request.setOutput(conn);
						if (request.isCancel()) {
							retry = 0;
							continue;
						}
						int resCode = conn.getResponseCode();
						if (resCode == HttpURLConnection.HTTP_OK) {
							InputStream is = conn.getInputStream();
							request.process(is);
							publishProgress(request);
							retry = 0;
						}

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}

		protected void publishProgress(final ImageRequest... request) {
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					onProgressUpdate(request);
				}
			});
		}

		protected void onProgressUpdate(ImageRequest... values) {
			ImageRequest request = values[0];
			if (request != null) {
				request.sendResult();
				ArrayList<NetworkRequest> list = mRequestMap.get(request.context);
				list.remove(request);
			}
			mRunningQueue.remove(request);
		}

	}
}
