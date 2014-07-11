package kr.ssm.sniffing.calling;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

public class CallObserver extends ContentObserver{

	Context mContext;
	
	public CallObserver(Handler handler, Context context) {
		super(handler);
		mContext = context;
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		
		Log.i("Call state test", "Call state chanage");
	}
	
	

}
