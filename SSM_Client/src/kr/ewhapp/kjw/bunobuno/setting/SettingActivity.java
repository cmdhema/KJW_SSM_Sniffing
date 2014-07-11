package kr.ewhapp.kjw.bunobuno.setting;

import kr.ewhapp.kjw.bunobuno.R;
import kr.ewhapp.kjw.bunobuno.SingletonData;
import kr.ewhapp.kjw.bunobuno.db.DBError;
import kr.ewhapp.kjw.bunobuno.db.DBModule;
import kr.ewhapp.kjw.bunobuno.db.EditDBRequest;
import kr.ewhapp.kjw.bunobuno.db.EditDBRequest.OnEditDBProcessListener;
import kr.ewhapp.kjw.bunobuno.password.HintActivity;
import kr.ewhapp.kjw.bunobuno.password.PasswordActivity;
import kr.ssm.sniffing.userinfo.UserData;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SettingActivity extends Activity implements OnCheckedChangeListener, OnClickListener, OnEditDBProcessListener {

	private RelativeLayout pwLockSettingBtn;
	private ImageView pwSettingBtn;
	private ImageView hintSettingBtn;
	private ImageView aboutBtn;
	private CheckBox pwLockCb;
	
	private UserData userData;
	
	private int passwordFlag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_setting);

		pwLockSettingBtn = (RelativeLayout) findViewById(R.id.setting_pw_lock_btn);
		pwSettingBtn = (ImageView) findViewById(R.id.setting_pw_setting_btn);
		hintSettingBtn = (ImageView) findViewById(R.id.setting_hint_setting_btn);
		aboutBtn = (ImageView) findViewById(R.id.setting_about_btn);
		pwLockCb = (CheckBox) findViewById(R.id.setting_pw_checkbox);

		pwLockCb.setOnCheckedChangeListener(this);
		pwLockSettingBtn.setOnClickListener(this);
		hintSettingBtn.setOnClickListener(this);
		aboutBtn.setOnClickListener(this);
		pwSettingBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == pwLockSettingBtn) {

		} else if (v == pwSettingBtn) {
			Intent intent = new Intent(SettingActivity.this, PasswordActivity.class);
			intent.putExtra("flag", "SettingActivity_Edit");
			startActivity(intent);		
		} else if (v == hintSettingBtn) {
			Intent intent = new Intent(SettingActivity.this, HintActivity.class);
//			intent.putExtra("flag", "SettingActivity");
			startActivity(intent);					
		} else if (v == aboutBtn) {

		}
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
		UserData data = new UserData();

		if (isChecked) {
			passwordFlag = 1;
			data.passwordFlag = 1;
			if(userData.password.equals("NONE")) {
				Intent intent = new Intent(SettingActivity.this, PasswordActivity.class);
				intent.putExtra("flag", "SettingActivity_New");
				startActivity(intent);
			}
		}

		else {
			passwordFlag = 0;
			data.passwordFlag = 0;
		}
		
		UpdatePasswordFlagDBRequest request = new UpdatePasswordFlagDBRequest(data);
		request.setOnEditDBRequestListener(this);
		DBModule.getInstance().enqueue(request);
	}

	@Override
	public void onEditSuccess(EditDBRequest request) {
		userData.passwordFlag = passwordFlag;
	}

	@Override
	public void onEditError(DBError error) {
		
	}

	@Override
	protected void onResume() {
		super.onResume();

		userData = SingletonData.getInstance().getUserData();
		
		if(userData.passwordFlag == 1)
			pwLockCb.setChecked(true);
		else
			pwLockCb.setChecked(false);
	}
	
	

}
