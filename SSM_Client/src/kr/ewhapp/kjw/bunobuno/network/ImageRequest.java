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
	
	//이전 요청과 같은 요청을 저장하는 리스트
	public ArrayList<ImageRequest> mSameList = new ArrayList<ImageRequest>();
	
	//자신의 download를 취소하게 되면 mSameList에 있는 같은 요청들도 이미지를 받을 수 없기 때문에 
	//이미지를 가져오는 작업은 진행하고 마지막에 자신에게 이미지를 보낼 때 보내지 못하도록 하기 위해 설정한 변수.
	public boolean isCancelable;

	public ImageRequest(String urlString) {
		imageURL = urlString;
	}
	
	//URL을 얻는 메소드
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

	//큐에 같은 요청이 있나 확인하는 메소드
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
	
	//리스트에 보여줄 이미지 설정
	public void setResult(Bitmap bm) {
		mResult = bm;
	}
	
	//리스트에 보여줄 이미지를 전송한다.
	@Override	
	public void sendResult() {
		
		if(isCancelable)
			super.isCancel();
		
		super.sendResult();
		
		// 같은 요청이 있는 리스트에 이미지를 뿌린다.
		for(ImageRequest request : mSameList) {
			if(!request.isCanceled) {
				request.setResult(mResult);
				request.sendResult();
			}
		}
	}

	
	//전송 실패 시 에러 전송 메소드
	@Override
	public void sendError(int code) {
		super.sendError(code);
		
		for(ImageRequest request : mSameList) {
			if(!request.isCanceled)
				request.sendError(code);
		}
	}

	
//	//작업이 취소 됐을 경우 요청을 제거하는 메소드
	@Override
	public void setCancel() {
		
		//같은 요청이 있는지 확인
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
