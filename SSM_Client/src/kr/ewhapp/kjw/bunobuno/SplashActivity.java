package kr.ewhapp.kjw.bunobuno;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import kr.bunobuno.util.TimeRequest;
import kr.ewhapp.kjw.bunobuno.db.DBError;
import kr.ewhapp.kjw.bunobuno.db.DBHelper;
import kr.ewhapp.kjw.bunobuno.db.DBModule;
import kr.ewhapp.kjw.bunobuno.db.EditDBRequest;
import kr.ewhapp.kjw.bunobuno.db.EditDBRequest.OnEditDBProcessListener;
import kr.ewhapp.kjw.bunobuno.db.GetDBRequest;
import kr.ewhapp.kjw.bunobuno.db.GetDBRequest.OnGetDBRequestListener;
import kr.ewhapp.kjw.bunobuno.main.MainActivity;
import kr.ewhapp.kjw.bunobuno.network.NetworkModel;
import kr.ewhapp.kjw.bunobuno.network.PostNetworkRequest;
import kr.ewhapp.kjw.bunobuno.network.PostNetworkRequest.OnPostMethodProcessListener;
import kr.ewhapp.kjw.bunobuno.password.PasswordActivity;
import kr.ssm.sniffing.userinfo.UserData;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class SplashActivity extends Activity implements OnPostMethodProcessListener<UserData>, OnEditDBProcessListener, Callback {

	public String date = TimeRequest.getCurrentDate(System.currentTimeMillis());

	// GCM Variables
	public static final String EXTRA_MESSAGE = "message";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private static final String SENDER_ID = "263732540891";
	private GoogleCloudMessaging gcm;

	private String regId = "";

	private Context context;

	private Handler handler;

	private UserData userData;
	private String phoneNumber = "";
	private int passwordFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_splash);

		userData = SingletonData.getInstance().getUserData();

		context = getApplicationContext();

		handler = new Handler(this);
		DBModule.getInstance().startThread(new DBHelper(SplashActivity.this), handler);

//		deleteUserData();
//		deleteAllDBData();
		
		getUserPhoneNumber();
		getUserLocation();
		getUserData();

	}

	private void getUserData() {

		SelectUserDataDBRequest selectRequest = new SelectUserDataDBRequest(new UserData(), "");
		selectRequest.setOnGetDBRequest(new OnGetDBRequestListener<UserData>() {

			@Override
			public void onGetSuccess(GetDBRequest<UserData> request) {
				UserData result = request.getResult();
				regId = result.regId;
				passwordFlag = result.passwordFlag;
				userData.hint = result.hint;
				userData.date = date;
				userData.passwordFlag = result.passwordFlag;
				userData.password = result.password;
				registerGCM();
			}

			@Override
			public void onGetError(DBError error) {

			}
		});
		DBModule.getInstance().enqueue(selectRequest);
	}

	private void addUserData() {

		InsertUserDataDBRequest insertRequest = new InsertUserDataDBRequest(userData);
		insertRequest.setOnEditDBRequestListener(new OnEditDBProcessListener() {

			@Override
			public void onEditSuccess(EditDBRequest request) {
				Log.i("SplashActivity, addUserData", "Insert Success" + userData.regId + "," + userData.phoneNumber);
			}

			@Override
			public void onEditError(DBError error) {

			}
		});
		DBModule.getInstance().enqueue(insertRequest);
	}

	private void getUserPhoneNumber() {

		TelephonyManager telManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

		if (telManager.getLine1Number().startsWith("+82"))
			phoneNumber = telManager.getLine1Number().replace("+82", "0");

		userData.agency = telManager.getNetworkOperatorName();
		userData.phoneNumber = phoneNumber;
		
	}

	@SuppressWarnings("static-access")
	private void getUserLocation() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		String locationProvider;
		if (locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER) == true) {
			locationProvider = LocationManager.NETWORK_PROVIDER;
		} else {
			locationProvider = LocationManager.GPS_PROVIDER;
		}

		LocationListener locationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {

				if (location != null) {
					userData.latitude = location.getLatitude();
					userData.longitude = location.getLongitude();
					getAddress(location.getLatitude(), location.getLongitude());
				}
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}

		};

		locationManager.requestLocationUpdates(locationProvider, 0, Integer.MAX_VALUE, locationListener);

		Location lastKnowLocation = locationManager.getLastKnownLocation(locationProvider);

		userData.latitude = lastKnowLocation.getLatitude();
		userData.longitude = lastKnowLocation.getLongitude();
		getAddress(lastKnowLocation.getLatitude(), lastKnowLocation.getLongitude());

	}

	protected void getAddress(double latitude, double longitude) {
		Geocoder gCoder = new Geocoder(context, Locale.getDefault());
		List<Address> addrList;
		try {
			addrList = gCoder.getFromLocation(latitude, longitude, 1);
			if (addrList.size() > 0) {
				Address addr = addrList.get(0);

				userData.countryName = addr.getCountryName();
				userData.cityName = addr.getLocality();
				userData.townName = addr.getThoroughfare();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void registerGCM() {

		if (checkPlayServices()) {
			if (regId.equals("gNz45f")) {
				Log.i("SplashActivity", "RegId Null");
				getRegistrationId();
			} else {
				userData.regId = regId;
				Log.i("SplashActivity registerGCM", regId);
				Log.i("SplashActivity registerGCM", "Send To Server");
				SendUserDataRequest request = new SendUserDataRequest(userData);
				request.setOnPostMethodProcessListener(SplashActivity.this);
				NetworkModel.getInstance().sendNetworkData(SplashActivity.this, request);
			}
		}
	}

	private void getRegistrationId() {
		if (gcm == null)
			gcm = GoogleCloudMessaging.getInstance(this);
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					regId = gcm.register(SENDER_ID);
					userData.regId = regId;
					userData.phoneNumber = phoneNumber;
					addUserData();
					SendUserDataRequest request = new SendUserDataRequest(userData);
					request.setOnPostMethodProcessListener(SplashActivity.this);
					NetworkModel.getInstance().sendNetworkData(SplashActivity.this, request);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i("SplashActivity", "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	@Override
	public void onPostMethodProcessSuccess(PostNetworkRequest<UserData> request) {
		Log.i("SplashActivity", "Post Success");
		SplashActivity.this.finish();
		UserData data = request.getResult();
		if (request instanceof SendUserDataRequest) {
			if (data.resultCode != 1) {
				Log.i("SplashActivity, PostMethodSuccess", "사용자 Error");
				deleteAllDBData();
			} else {
				Log.i("SplashActivity, PostMethodSuccess", "사용자OK");
				if (passwordFlag != 1)
					startActivity(new Intent(SplashActivity.this, MainActivity.class));
				else {
					Intent intent = new Intent(SplashActivity.this, PasswordActivity.class);
					intent.putExtra("flag", "SplashActivity");
					startActivity(intent);
				}
			}
		}
	}

	private void deleteUserData() {
		DeleteUserData deleteUserRequest = new DeleteUserData();
		deleteUserRequest.setOnEditDBRequestListener(this);
		DBModule.getInstance().enqueue(deleteUserRequest);
	}

	private void deleteAllDBData() {
		DeleteAllDataDBRequest deleteRequest = new DeleteAllDataDBRequest();
		deleteRequest.setOnEditDBRequestListener(this);
		DBModule.getInstance().enqueue(deleteRequest);
	}

	@Override
	public void onPostMethodProcessError() {

	}

	@Override
	public void onEditSuccess(EditDBRequest request) {
		Log.i("SplashActivity", "Edit Success");

		// if (passwordFlag != 1)
		// startActivity(new Intent(SplashActivity.this, MainActivity.class));
		// else
		// startActivity(new Intent(SplashActivity.this,
		// PasswordActivity.class));
	}

	@Override
	public void onEditError(DBError error) {

	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}
}
