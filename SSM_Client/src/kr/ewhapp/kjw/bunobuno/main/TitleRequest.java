package kr.ewhapp.kjw.bunobuno.main;

import java.util.ArrayList;

import kr.ewhapp.kjw.bunobuno.NumberData;
import kr.ewhapp.kjw.bunobuno.db.DBScheme;
import kr.ewhapp.kjw.bunobuno.db.GetDBRequest;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class TitleRequest extends GetDBRequest<ArrayList<NumberData>>{

	public TitleRequest(ArrayList<NumberData> list, String flag) {
		super(list, flag);
	}

	@Override
	public boolean onExecuteQuery(SQLiteDatabase database, String query, ArrayList<NumberData> result) {
		
		
		Cursor accountCursor = database.rawQuery(DBScheme.selectAccountTitle(), null);
		Cursor siteCursor = database.rawQuery(DBScheme.selectSiteTitle(), null);
		Cursor customCursor = database.rawQuery(DBScheme.selectCustomTitle(), null);
		Cursor photoCursor = database.rawQuery(DBScheme.selectPhotoUri(), null);
		
		NumberData data = new NumberData();
		
		data.accountTitleList = new ArrayList<String>();
		data.siteTitleList = new ArrayList<String>();
		data.customTitleList = new ArrayList<String>();
		data.photoUriList = new ArrayList<String>();
		
		while ( (accountCursor.moveToNext()) ) 
			data.accountTitleList.add(accountCursor.getString(accountCursor.getColumnIndex("Title")));

		while ( (siteCursor.moveToNext()) ) 
			data.siteTitleList.add(siteCursor.getString(siteCursor.getColumnIndex("Title")));

		while ( (customCursor.moveToNext()) ) 
			data.customTitleList.add(customCursor.getString(customCursor.getColumnIndex("Title")));

		while ( (photoCursor.moveToNext()) ) 
			data.photoUriList.add(photoCursor.getString(photoCursor.getColumnIndex("Uri")));

		result.add(data);
		
		return true;
	}



}
