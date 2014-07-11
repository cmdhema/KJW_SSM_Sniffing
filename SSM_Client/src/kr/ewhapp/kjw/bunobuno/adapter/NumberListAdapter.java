package kr.ewhapp.kjw.bunobuno.adapter;

import java.util.ArrayList;

import kr.ewhapp.kjw.bunobuno.view.TitleView;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class NumberListAdapter extends BaseAdapter {

	private Context context;
	
	private ArrayList<String> dataList;
	
	public NumberListAdapter(Context context, ArrayList<String> dataList) {
		this.context = context;
		this.dataList = dataList;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		TitleView v;
		
		if (convertView == null)
			v = new TitleView(context);
		else
			v = (TitleView) convertView;
		
		String title = (String) dataList.get(position);
		v.setTitleText(title);

		return v;
	}

}
