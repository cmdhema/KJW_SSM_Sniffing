package kr.ewhapp.kjw.bunobuno.main;

import java.util.ArrayList;

import kr.ewhapp.kjw.bunobuno.NumberData;
import kr.ewhapp.kjw.bunobuno.R;
import kr.ewhapp.kjw.bunobuno.adapter.NumberListAdapter;
import kr.ewhapp.kjw.bunobuno.adapter.PhotoGridAdapter;
import kr.ewhapp.kjw.bunobuno.db.DBError;
import kr.ewhapp.kjw.bunobuno.db.DBModule;
import kr.ewhapp.kjw.bunobuno.db.GetDBRequest;
import kr.ewhapp.kjw.bunobuno.db.GetDBRequest.OnGetDBRequestListener;
import kr.ewhapp.kjw.bunobuno.edit.EditActivity;
import kr.ewhapp.kjw.bunobuno.network.NetworkModel;
import kr.ewhapp.kjw.bunobuno.setting.SettingActivity;
import kr.ssm.sniffing.calling.CallReceiver;
import kr.ssm.sniffing.sms.SMSObserver;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends Activity implements OnClickListener, OnItemClickListener, OnGetDBRequestListener<ArrayList<NumberData>> {

	private SMSObserver smsObserver;
	private Handler handler;
	private ContentResolver resolver;
	
	private int SUCCESS = 0;

	private ImageView addBtn;
	private ImageView settingBtn;

	private ListView accountListView;
	private ListView siteListView;
	private ListView customListView;
	private GridView photoGridView;

	private NumberListAdapter accountListAdapter;
	private NumberListAdapter siteListAdapter;
	private NumberListAdapter customListAdapter;
	private PhotoGridAdapter photoGridAdapter;

	private TitleRequest request;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);

		addBtn = (ImageView) findViewById(R.id.add_btn);
		settingBtn = (ImageView) findViewById(R.id.setting_btn);
		accountListView = (ListView) findViewById(R.id.accountlist);
		siteListView = (ListView) findViewById(R.id.passwordlist);
		customListView = (ListView) findViewById(R.id.customlist);
		photoGridView = (GridView) findViewById(R.id.card_grid);
		
		addBtn.setOnClickListener(this);
		settingBtn.setOnClickListener(this);
		accountListView.setOnItemClickListener(this);
		siteListView.setOnItemClickListener(this);
		customListView.setOnItemClickListener(this);

		registerAboutSniffing();
	}

	private void registerAboutSniffing() {
		resolver = getContentResolver();
		handler = new Handler();
		smsObserver = new SMSObserver(handler, getApplicationContext());
		resolver.registerContentObserver(Uri.parse("content://sms/"), true, smsObserver);
	}

	@Override
	public void onItemClick(AdapterView<?> parentView, View arg1, int position, long arg3) {
		Intent intent = new Intent(this, EditActivity.class);

		intent.putExtra("editFlag", "Edit");

		if (parentView == accountListView) {
			intent.putExtra("categoryFlag", "Account");
			intent.putExtra("Title", (String) accountListView.getItemAtPosition(position));
		} else if (parentView == siteListView) {
			intent.putExtra("categoryFlag", "Site");
			intent.putExtra("Title", (String) siteListView.getItemAtPosition(position));
		} else if (parentView == customListView) {
			intent.putExtra("categoryFlag", "Custom");
			intent.putExtra("Title", (String) customListView.getItemAtPosition(position));
		}

		startActivityForResult(intent, SUCCESS);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == SUCCESS) {
			accountListAdapter.notifyDataSetChanged();
			siteListAdapter.notifyDataSetChanged();
			customListAdapter.notifyDataSetChanged();
			photoGridAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		if (v == addBtn) {

			Intent intent = new Intent(this, EditActivity.class);
			intent.putExtra("editFlag", "Add");
			startActivity(intent);

		} else if (v == settingBtn) {
			startActivity(new Intent(this, SettingActivity.class));
		}
	}

	@Override
	public void onGetSuccess(GetDBRequest<ArrayList<NumberData>> request) {
		ArrayList<NumberData> dataList = request.getResult();

		accountListAdapter = new NumberListAdapter(this, dataList.get(0).accountTitleList);
		accountListView.setAdapter(accountListAdapter);
		setListViewHeight(accountListView, accountListAdapter);

		siteListAdapter = new NumberListAdapter(this, dataList.get(0).siteTitleList);
		siteListView.setAdapter(siteListAdapter);
		setListViewHeight(siteListView, siteListAdapter);
		
		customListAdapter = new NumberListAdapter(this, dataList.get(0).customTitleList);
		customListView.setAdapter(customListAdapter);
		setListViewHeight(customListView, customListAdapter);

		photoGridAdapter = new PhotoGridAdapter(this, dataList.get(0).photoUriList);
		photoGridView.setAdapter(photoGridAdapter);

	}

	private void setListViewHeight(ListView listView, NumberListAdapter listAdapter) {
		int height = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);

		for (int i = 0; i < listAdapter.getCount(); i++) {
			View view = listAdapter.getView(i, null, listView);
			view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			height += view.getMeasuredHeight();
		}

		// setting listview item in adapter
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = height + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);

		// layout view 모양이 바꼇다고 알려준다. onlayout 이 호출 된다.
		listView.requestLayout();
		
	}

	@Override
	public void onGetError(DBError error) {

	}

	@Override
	protected void onResume() {
		super.onResume();

		request = new TitleRequest(new ArrayList<NumberData>(), "");
		request.setOnGetDBRequest(this);
		DBModule.getInstance().enqueue(request);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		getContentResolver().unregisterContentObserver(smsObserver);
		
		NetworkModel.getInstance().cleanUpRequest(this);
		
	}

	
}
