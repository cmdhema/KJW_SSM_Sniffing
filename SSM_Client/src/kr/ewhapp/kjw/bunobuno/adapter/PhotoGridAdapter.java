package kr.ewhapp.kjw.bunobuno.adapter;

import java.util.ArrayList;

import kr.ewhapp.kjw.bunobuno.view.PhotoView;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class PhotoGridAdapter extends BaseAdapter {

	private ArrayList<String> dataList;
	private Context context;
	
	public PhotoGridAdapter(Context context, ArrayList<String> photoUriList) {
		this.context = context;
		this.dataList = photoUriList;
		
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

		PhotoView view;
		
		if(convertView == null)
			view = new PhotoView(context);
		else
			view = (PhotoView) convertView;
		
		view.setPhoto(dataList.get(position));
		
		return view;
	}

}
