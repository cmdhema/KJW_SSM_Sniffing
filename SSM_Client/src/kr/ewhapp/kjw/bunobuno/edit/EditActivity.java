package kr.ewhapp.kjw.bunobuno.edit;

import kr.ewhapp.kjw.bunobuno.NumberData;
import kr.ewhapp.kjw.bunobuno.R;
import kr.ewhapp.kjw.bunobuno.SingletonData;
import kr.ewhapp.kjw.bunobuno.db.DBError;
import kr.ewhapp.kjw.bunobuno.db.DBModule;
import kr.ewhapp.kjw.bunobuno.db.EditDBRequest;
import kr.ewhapp.kjw.bunobuno.db.EditDBRequest.OnEditDBProcessListener;
import kr.ewhapp.kjw.bunobuno.db.GetDBRequest;
import kr.ewhapp.kjw.bunobuno.db.GetDBRequest.OnGetDBRequestListener;
import kr.ewhapp.kjw.bunobuno.network.MultiPartNetworkRequest;
import kr.ewhapp.kjw.bunobuno.network.MultiPartNetworkRequest.OnMultiPartProcessListener;
import kr.ewhapp.kjw.bunobuno.network.NetworkModel;
import kr.ewhapp.kjw.bunobuno.network.PostNetworkRequest;
import kr.ewhapp.kjw.bunobuno.network.PostNetworkRequest.OnPostMethodProcessListener;
import kr.ssm.sniffing.photo.AddPhotoURIDBRequest;
import kr.ssm.sniffing.photo.SendPhotoNetworkRequest;
import kr.ssm.sniffing.userinfo.UserData;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class EditActivity extends Activity implements TextWatcher, OnClickListener, OnEditDBProcessListener, OnCheckedChangeListener,
		OnGetDBRequestListener<NumberData>, OnItemSelectedListener, OnPostMethodProcessListener<NumberData> {

	private UserData userData;
	
	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private Uri fileUri;

	private int checkOn = R.drawable.check_on;
	private int checkOff = R.drawable.check_off;

	private NumberData data;

	private Spinner bankSpinner;

	private CheckBox accountCheckBox;
	private CheckBox siteCheckBox;
	private CheckBox customCheckBox;

	private EditText titleEt;

	private EditText accountNumEt;
	private EditText accountNameEt;

	private EditText siteNameEt;
	private EditText sitePasswordEt;

	private EditText customNameEt;
	private EditText customPasswordEt;

	private ImageView titleIv;

	private ImageView titleCheckIv;
	private ImageView accountNumCheckIv;
	private ImageView accountNameCheckIv;
	private ImageView siteNameCheckIv;
	private ImageView sitePasswordCheckIv;
	private ImageView customNameCheckIv;
	private ImageView customPasswordCheckIv;

	private ImageView saveBtn;
	private ImageView cancelBtn;

	private ImageView camearaBtn;
	private ImageView albumBtn;

	private UpdateDBRequest updateRequest;
	private AddDBRequest addRequest;
	private SelectAccountDBRequest accountRequest;
	private SelectSiteDBRequest siteRequest;
	private SelectCustomDBRequest customRequest;

	private String title;
	private String accountNum;
	private String accountName;
	private String siteName;
	private String sitePassword;
	private String customName;
	private String customPassword;

	private String editFlag;
	private String categoryFlag;
	private String queryTitle;

	private int bankPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_edit);

		titleIv = (ImageView) findViewById(R.id.menutitle_iv);
		bankSpinner = (Spinner) findViewById(R.id.bank_spinner);
		titleEt = (EditText) findViewById(R.id.title_et);
		accountCheckBox = (CheckBox) findViewById(R.id.account_cb);
		siteCheckBox = (CheckBox) findViewById(R.id.password_cb);
		customCheckBox = (CheckBox) findViewById(R.id.custom_cb);
		// accountLayout = (LinearLayout) findViewById(R.id.accountlayout);
		// siteLayout = (LinearLayout) findViewById(R.id.sitelayout);
		// customLayout = (LinearLayout) findViewById(R.id.customlayout);
		accountNameEt = ((EditText) findViewById(R.id.accountname_et));
		accountNumEt = ((EditText) findViewById(R.id.accountnum_et));
		siteNameEt = ((EditText) findViewById(R.id.sitename_et));
		sitePasswordEt = ((EditText) findViewById(R.id.sitepassword_et));
		customNameEt = ((EditText) findViewById(R.id.customtitle_et));
		customPasswordEt = ((EditText) findViewById(R.id.custompassword_et));
		accountNumCheckIv = (ImageView) findViewById(R.id.accountnum_iv);
		accountNameCheckIv = (ImageView) findViewById(R.id.accountname_iv);
		siteNameCheckIv = (ImageView) findViewById(R.id.sitename_iv);
		sitePasswordCheckIv = (ImageView) findViewById(R.id.sitepassword_iv);
		customNameCheckIv = (ImageView) findViewById(R.id.customtitle_iv);
		customPasswordCheckIv = (ImageView) findViewById(R.id.custompassword_iv);
		titleCheckIv = (ImageView) findViewById(R.id.title_iv);
		saveBtn = (ImageView) findViewById(R.id.save_btn);
		cancelBtn = (ImageView) findViewById(R.id.cancel_btn);
		albumBtn = (ImageView) findViewById(R.id.edit_album_btn);
		camearaBtn = (ImageView) findViewById(R.id.edit_camera_btn);

		camearaBtn.setOnClickListener(this);
		albumBtn.setOnClickListener(this);
		titleEt.addTextChangedListener(this);
		accountNameEt.addTextChangedListener(this);
		accountNumEt.addTextChangedListener(this);
		siteNameEt.addTextChangedListener(this);
		sitePasswordEt.addTextChangedListener(this);
		customPasswordEt.addTextChangedListener(this);
		customNameEt.addTextChangedListener(this);
		saveBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		bankSpinner.setOnItemSelectedListener(this);
		accountCheckBox.setOnCheckedChangeListener(this);
		siteCheckBox.setOnCheckedChangeListener(this);
		customCheckBox.setOnCheckedChangeListener(this);
		
		String bankList[] = { "신한은행", "우리은행", "외환은행", "국민은행", "농협" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bankList);
		bankSpinner.setAdapter(adapter);

		editFlag = getIntent().getStringExtra("editFlag");
		categoryFlag = getIntent().getStringExtra("categoryFlag");
		queryTitle = getIntent().getStringExtra("Title");

		if (editFlag.equals("Edit")) {
			titleIv.setImageResource(R.drawable.modify);

			if (categoryFlag.equals("Account")) {

				accountRequest = new SelectAccountDBRequest(new NumberData(), queryTitle);
				accountRequest.setOnGetDBRequest(this);
				DBModule.getInstance().enqueue(accountRequest);

			} else if (categoryFlag.equals("Site")) {

				siteRequest = new SelectSiteDBRequest(new NumberData(), queryTitle);
				siteRequest.setOnGetDBRequest(this);
				DBModule.getInstance().enqueue(siteRequest);

			} else if (categoryFlag.equals("Custom")) {
				customRequest = new SelectCustomDBRequest(new NumberData(), queryTitle);
				customRequest.setOnGetDBRequest(this);
				DBModule.getInstance().enqueue(customRequest);
			}
		} else
			titleIv.setImageResource(R.drawable.add);
	}

	@Override
	public void afterTextChanged(Editable arg0) {

		if (titleEt.getText().length() > 0)
			titleCheckIv.setImageResource(checkOn);
		else
			titleCheckIv.setImageResource(checkOff);

		if (accountNameEt.getText().length() > 0)
			accountNameCheckIv.setImageResource(checkOn);
		else
			accountNameCheckIv.setImageResource(checkOff);

		if (accountNumEt.getText().length() > 0)
			accountNumCheckIv.setImageResource(checkOn);
		else
			accountNumCheckIv.setImageResource(checkOff);

		if (siteNameEt.getText().length() > 0)
			siteNameCheckIv.setImageResource(checkOn);
		else
			siteNameCheckIv.setImageResource(checkOff);

		if (sitePasswordEt.getText().length() > 0)
			sitePasswordCheckIv.setImageResource(checkOn);
		else
			sitePasswordCheckIv.setImageResource(checkOff);

		if (customPasswordEt.getText().length() > 0)
			customPasswordCheckIv.setImageResource(checkOn);
		else
			customPasswordCheckIv.setImageResource(checkOff);

		if (customNameEt.getText().length() > 0)
			customNameCheckIv.setImageResource(checkOn);
		else
			customNameCheckIv.setImageResource(checkOff);
	}

	@Override
	public void onClick(View v) {
		if (v == saveBtn) {
			title = titleEt.getText().toString();

			if (accountCheckBox.isChecked()) {
				accountName = accountNameEt.getText().toString();
				accountNum = accountNumEt.getText().toString();
			} else if (siteCheckBox.isChecked()) {
				siteName = siteNameEt.getText().toString();
				sitePassword = sitePasswordEt.getText().toString();
			} else if (customCheckBox.isChecked()) {
				customName = customNameEt.getText().toString();
				customPassword = customPasswordEt.getText().toString();
			}

			if (editFlag.equals("Edit"))
				updateList();
			else
				addList();


		} else if (v == cancelBtn) {
			finish();
		}  else if (v == camearaBtn) {
			doTakePhotoAction();
		} else if (v == albumBtn) {
			doTakeAlbumAction();
		}
	}

	private void doTakeAlbumAction() {
		// 앨범 호출
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
		// finish();
		startActivityForResult(intent, PICK_FROM_ALBUM);

	}

	private void doTakePhotoAction() {

		String timeStamp = String.valueOf(System.currentTimeMillis());

		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, "IMG_" + timeStamp + ".jpg");

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		// fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file
		// to save the image (this doesn't work at all for images)
		fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values); 
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		// start the image capture Intent
		startActivityForResult(intent, PICK_FROM_CAMERA);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == PICK_FROM_CAMERA) {
			if (resultCode == RESULT_OK) {

				if (fileUri != null) {
					uploadPhoto();
					addPhotoDB();
				}

			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed, advise user
			}
		}

	}

	private void uploadPhoto() {
		NumberData photoData = new NumberData();
		photoData.uri = getFilePath(fileUri);
		photoData.phoneNumber = userData.phoneNumber;
		
		SendPhotoNetworkRequest sendPhotoRequest = new SendPhotoNetworkRequest(photoData);
		sendPhotoRequest.setOnMultiPartProcessListener(new OnMultiPartProcessListener<NumberData>() {

			@Override
			public void onMultiPartProcessSuccess(MultiPartNetworkRequest<NumberData> request) {
				
			}

			@Override
			public void onMultiPartProcessError() {
				
			}
		});
		
		NetworkModel.getInstance().sendPhotoData(this, sendPhotoRequest);
	}

	private void addPhotoDB() {
		NumberData data = new NumberData();
		data.uri = fileUri.toString();
		AddPhotoURIDBRequest request = new AddPhotoURIDBRequest(data);
		request.setOnEditDBRequestListener(new OnEditDBProcessListener() {
			
			@Override
			public void onEditSuccess(EditDBRequest request) {
				
			}
			
			@Override
			public void onEditError(DBError error) {
				
			}
		});
		
		DBModule.getInstance().enqueue(request);
		
	}
	
	protected String getFilePath(Uri uri) {

		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	@Override
	public void onCheckedChanged(CompoundButton v, boolean arg1) {

		if (v == accountCheckBox) {

		} else if (v == siteCheckBox) {

		} else if (v == customCheckBox) {

		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
		bankPosition = position;
	}

	@Override
	public void onGetSuccess(GetDBRequest<NumberData> request) {

		data = request.getResult();

		if (request instanceof SelectAccountDBRequest) {
			titleEt.setText(data.title);
			accountNameEt.setText(data.name);
			accountNumEt.setText(data.number);
			bankSpinner.setSelection(data.bank);
			accountCheckBox.setChecked(true);
			setEditTextImage(checkOn, checkOn, checkOn, checkOff, checkOff, checkOff, checkOff);
		} else if (request instanceof SelectSiteDBRequest) {
			titleEt.setText(data.title);
			siteNameEt.setText(data.name);
			sitePasswordEt.setText(data.number);
			siteCheckBox.setChecked(true);
			setEditTextImage(checkOn, checkOff, checkOff, checkOn, checkOn, checkOff, checkOff);

		} else if (request instanceof SelectCustomDBRequest) {
			titleEt.setText(data.title);
			customNameEt.setText(data.name);
			customPasswordEt.setText(data.number);
			customCheckBox.setChecked(true);
			setEditTextImage(checkOn, checkOff, checkOff, checkOff, checkOff, checkOn, checkOn);
		}
	}

	@Override
	public void onGetError(DBError error) {

	}

	@Override
	public void onEditSuccess(EditDBRequest request) {
		sendToServer();
		finish();
	}

	@Override
	public void onEditError(DBError error) {
		Toast.makeText(getApplicationContext(), "중복된 Title입니다, 다시 입려해주세요!", Toast.LENGTH_LONG).show();
	}

	public void setEditTextImage(int title, int accountNum, int accountName, int siteName, int sitePassword, int customTitle, int customPassword) {

		titleCheckIv.setImageResource(title);
		accountNameCheckIv.setImageResource(accountName);
		accountNumCheckIv.setImageResource(accountNum);
		siteNameCheckIv.setImageResource(siteName);
		sitePasswordCheckIv.setImageResource(sitePassword);
		customNameCheckIv.setImageResource(customTitle);
		customPasswordCheckIv.setImageResource(customPassword);
	}

	private void sendToServer() {
		if (siteCheckBox.isChecked()) {
			SendSiteNetworkRequest sendRequest = new SendSiteNetworkRequest(data);
			sendRequest.setOnPostMethodProcessListener(this);
			NetworkModel.getInstance().sendNetworkData(this, sendRequest);
		} else if (customCheckBox.isChecked()) {
			SendCustomNetworkRequest sendRequest = new SendCustomNetworkRequest(data);
			sendRequest.setOnPostMethodProcessListener(this);
			NetworkModel.getInstance().sendNetworkData(this, sendRequest);
		}
	}

	private void addList() {
		data = new NumberData();
		data.phoneNumber = SingletonData.getInstance().getUserData().phoneNumber;
		if (accountCheckBox.isChecked()) {
			categoryFlag = "Account";
			data.title = title;
			data.name = accountName;
			data.number = accountNum;
			data.bank = bankPosition;
			Log.i("EditActivity", bankPosition + "");
		} else if (siteCheckBox.isChecked()) {
			categoryFlag = "Site";
			data.title = title;
			data.name = siteName;
			data.number = sitePassword;
		} else if (customCheckBox.isChecked()) {
			categoryFlag = "Custom";
			data.title = title;
			data.number = customPassword;
			data.name = customName;
		}

		addRequest = new AddDBRequest(data, categoryFlag);
		addRequest.setOnEditDBRequestListener(this);
		DBModule.getInstance().enqueue(addRequest);
	}

	private void updateList() {

		data = new NumberData();

		if (accountCheckBox.isChecked()) {
			data.title = title;
			data.name = accountName;
			data.number = accountNum;
			data.bank = bankPosition;
		} else if (siteCheckBox.isChecked()) {
			data.title = title;
			data.name = siteName;
			data.number = sitePassword;
		} else if (customCheckBox.isChecked()) {
			data.title = title;
			data.number = customPassword;
			data.name = customName;
		}

		updateRequest = new UpdateDBRequest(data, categoryFlag);
		updateRequest.setOnEditDBRequestListener(this);
		DBModule.getInstance().enqueue(updateRequest);
	}

	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public void onPostMethodProcessSuccess(PostNetworkRequest<NumberData> request) {

	}

	@Override
	public void onPostMethodProcessError() {

	}

	@Override
	protected void onResume() {
		super.onResume();
		userData = SingletonData.getInstance().getUserData();
	}
	
	
}
