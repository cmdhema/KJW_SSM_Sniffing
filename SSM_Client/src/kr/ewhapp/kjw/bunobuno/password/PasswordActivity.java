package kr.ewhapp.kjw.bunobuno.password;

import kr.ewhapp.kjw.bunobuno.R;
import kr.ewhapp.kjw.bunobuno.SingletonData;
import kr.ewhapp.kjw.bunobuno.db.DBError;
import kr.ewhapp.kjw.bunobuno.db.DBModule;
import kr.ewhapp.kjw.bunobuno.db.EditDBRequest;
import kr.ewhapp.kjw.bunobuno.db.EditDBRequest.OnEditDBProcessListener;
import kr.ewhapp.kjw.bunobuno.main.MainActivity;
import kr.ssm.sniffing.userinfo.UserData;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class PasswordActivity extends Activity implements OnClickListener {

	private UserData userData;
	
	private ImageView num0;
	private ImageView num1;
	private ImageView num2;
	private ImageView num3;
	private ImageView num4;
	private ImageView num5;
	private ImageView num6;
	private ImageView num7;
	private ImageView num8;
	private ImageView num9;
	private ImageView hint;
	private ImageView delete;
	
	private ImageView titleIv;
	private ImageView inputText;
	
	private EditText passwordInput;
	
	private String password="";

	private String flag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_password);
		
		num0 = (ImageView) findViewById(R.id.password_0);
		num1 = (ImageView) findViewById(R.id.password_1);
		num2 = (ImageView) findViewById(R.id.password_2);
		num3 = (ImageView) findViewById(R.id.password_3);
		num4 = (ImageView) findViewById(R.id.password_4);
		num5 = (ImageView) findViewById(R.id.password_5);
		num6 = (ImageView) findViewById(R.id.password_6);
		num7 = (ImageView) findViewById(R.id.password_7);
		num8 = (ImageView) findViewById(R.id.password_8);
		num9 = (ImageView) findViewById(R.id.password_9);
		hint = (ImageView) findViewById(R.id.password_hint);
		delete = (ImageView) findViewById(R.id.password_delete);
		passwordInput = (EditText) findViewById(R.id.password_input_et);
		inputText = (ImageView) findViewById(R.id.password_input_tv);
		
		num0.setOnClickListener(this);
		num1.setOnClickListener(this);
		num2.setOnClickListener(this);
		num3.setOnClickListener(this);
		num4.setOnClickListener(this);
		num5.setOnClickListener(this);
		num6.setOnClickListener(this);
		num7.setOnClickListener(this);
		num8.setOnClickListener(this);
		num9.setOnClickListener(this);
		hint.setOnClickListener(this);
		delete.setOnClickListener(this);
	
		flag = getIntent().getStringExtra("flag");
		if(flag.equals("SettingActivity_New")) {
			inputText.setImageResource(R.drawable.password_input_text);
			hint.setBackgroundResource(R.drawable.blank_selector);
		} else if (flag.equals("SettingActivity_Edit")) {
			inputText.setImageResource(R.drawable.password_edit_input_text);
			hint.setBackgroundResource(R.drawable.blank_selector);
		} else if (flag.equals("SplashActivity")) {
			inputText.setImageResource(R.drawable.password_input_original_text);
			hint.setBackgroundResource(R.drawable.hint_selector);
		}
		
		
	}

	@Override
	public void onClick(View v) {
		if ( v == num0) {
			password += "0";
			passwordInput.setText(password);
		} else if ( v == num1) {
			password += "1";
			passwordInput.setText(password);
		} else if ( v == num2) {
			password += "2";
			passwordInput.setText(password);
		} else if ( v == num3) {
			password += "3";
			passwordInput.setText(password);
		} else if ( v == num4) {
			password += "4";
			passwordInput.setText(password);
		} else if ( v == num5) {
			password += "5";
			passwordInput.setText(password);
		} else if ( v == num6) {
			password += "6";
			passwordInput.setText(password);
		} else if ( v == num7) {
			password += "7";
			passwordInput.setText(password);
		} else if ( v == num8) {
			password += "8";
			passwordInput.setText(password);
		} else if ( v == num9) {
			password += "9";
			passwordInput.setText(password);
		} else if ( v == hint) {
			if(flag.equals("SettingActivity_New") || flag.equals("SettingActivity_Edit"))
				updatePassword();
			else if (flag.equals("SplashActivity"))
				comparePassword();
		} else if ( v == delete) {
			if(password.length() > 0)
				password = password.substring(0, password.length()-1);
			passwordInput.setText(password);
		}
	}

	private void comparePassword() {
		if(password.equals(userData.password)) {
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
		else
			Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다. 다시 입력해주세요", Toast.LENGTH_SHORT).show();
	}

	private void updatePassword() {
		UserData data = new UserData();
		data.password = password;
		UpdatePasswordDBRequest request = new UpdatePasswordDBRequest(data);
		request.setOnEditDBRequestListener(new OnEditDBProcessListener() {
			
			@Override
			public void onEditSuccess(EditDBRequest request) {
				userData.password = password;
				PasswordActivity.this.finish();
			}
			
			@Override
			public void onEditError(DBError error) {
				
			}
		});
		
		DBModule.getInstance().enqueue(request);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		userData = SingletonData.getInstance().getUserData();
	}

	
}
