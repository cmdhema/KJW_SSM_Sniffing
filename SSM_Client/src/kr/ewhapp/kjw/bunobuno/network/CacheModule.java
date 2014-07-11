package kr.ewhapp.kjw.bunobuno.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class CacheModule {

	// 캐시 모듈을 관리할 instance
	private static CacheModule instance;
	
	//메모리 캐시를 저장할 HashMap
	HashMap<String, WeakReference<Bitmap>> mMemCache = new HashMap<String, WeakReference<Bitmap>>();
	
	
	public static CacheModule getInstance() {
		if(instance == null)
			instance = new CacheModule();
		
		return instance;
	}
	
	//메모리 캐시 설정
	public void setCache(String key, Bitmap bitmap) {
		WeakReference<Bitmap> ref = new WeakReference<Bitmap>(bitmap);
		mMemCache.put(key,ref);
	}
	
	
	public InputStream saveFile(String key, InputStream is) {
		File file = new File(Environment.getExternalStorageDirectory(),key);
		FileInputStream in = null;
		try {
			FileOutputStream out = new FileOutputStream(file);
			byte[] buffer = new byte[4096];
			int len;
			
			while( (len = is.read(buffer)) > 0) 
				out.write(buffer,0,len);
			
			out.close();
			is.close();
			
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return in;
	}
	
	
	public Bitmap getFileCache(String key) {
		File file = new File(Environment.getExternalStorageDirectory(), key);
		Bitmap bm = null;
		
		if(!file.exists())
			return null;
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			bm = BitmapFactory.decodeStream(fis);
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bm;
	}
	
	public Bitmap getMemCache(String key) {
		WeakReference<Bitmap> ref = mMemCache.get(key);
		
		if(ref == null)
			return null;
		Bitmap bm = ref.get();
		if( bm!= null)
			return bm;
		mMemCache.remove(key);
		return null;
	}
	
	public Bitmap getCache(ImageRequest request) {
		Bitmap bm = getMemCache(request.getKey());
		
		if(bm!= null)
			return bm;
		
		bm = getFileCache(request.getKey());
		if(bm == null)
			return null;
		
		setCache(request.getKey(),bm);
		return bm;
		
	}
}
