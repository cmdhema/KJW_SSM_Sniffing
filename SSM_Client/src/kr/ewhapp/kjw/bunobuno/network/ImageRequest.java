package kr.ewhapp.kjw.bunobuno.network;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;



public class ImageRequest extends NetworkRequest{

	private Bitmap mResult;
	
	public String imageURL;
	
	//���� ��û�� ���� ��û�� �����ϴ� ����Ʈ
	public ArrayList<ImageRequest> mSameList = new ArrayList<ImageRequest>();
	
	//�ڽ��� download�� ����ϰ� �Ǹ� mSameList�� �ִ� ���� ��û�鵵 �̹����� ���� �� ���� ������ 
	//�̹����� �������� �۾��� �����ϰ� �������� �ڽſ��� �̹����� ���� �� ������ ���ϵ��� �ϱ� ���� ������ ����.
	public boolean isCancelable;

	public ImageRequest(String urlString) {
		imageURL = urlString;
	}
	
	//URL�� ��� �޼ҵ�
	@Override
	public URL getURL() {
		URL url = null;
		try {
			url = new URL(imageURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	}

	
	public boolean parsing(InputStream is) {
		InputStream fis;
		fis = CacheModule.getInstance().saveFile(getKey(), is);
		Bitmap bm = BitmapFactory.decodeStream(fis);
		CacheModule.getInstance().setCache(getKey(), bm);
		return true;
	}

	//ť�� ���� ��û�� �ֳ� Ȯ���ϴ� �޼ҵ�
	public boolean isSameRequest(ImageRequest request) {
		if(!isCancel() && imageURL.equals(request.imageURL)) {
			mSameList.add(request);
			return true;
		}
		return false;
	}
	
	public String getKey() {
		try {
			return URLEncoder.encode(imageURL, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	//����Ʈ�� ������ �̹��� ����
	public void setResult(Bitmap bm) {
		mResult = bm;
	}
	
	//����Ʈ�� ������ �̹����� �����Ѵ�.
	@Override	
	public void sendResult() {
		
		if(isCancelable)
			super.isCancel();
		
		super.sendResult();
		
		// ���� ��û�� �ִ� ����Ʈ�� �̹����� �Ѹ���.
		for(ImageRequest request : mSameList) {
			if(!request.isCanceled) {
				request.setResult(mResult);
				request.sendResult();
			}
		}
	}

	
	//���� ���� �� ���� ���� �޼ҵ�
	@Override
	public void sendError(int code) {
		super.sendError(code);
		
		for(ImageRequest request : mSameList) {
			if(!request.isCanceled)
				request.sendError(code);
		}
	}

	
//	//�۾��� ��� ���� ��� ��û�� �����ϴ� �޼ҵ�
	@Override
	public void setCancel() {
		
		//���� ��û�� �ִ��� Ȯ��
		if(mSameList.size() == 0) {
			super.setCancel();
			NetworkModel.getInstance().removeImageRequest(this);
		} else
			isCancelable = true;
	}


	@Override
	public String getRequestMethod() {
		return "GET";
	}

	@Override
	public void onSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError() {
		// TODO Auto-generated method stub
		
	}
}
