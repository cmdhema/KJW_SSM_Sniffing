package kr.ewhapp.kjw.bunobuno.edit;

import kr.ewhapp.kjw.bunobuno.NumberData;
import kr.ewhapp.kjw.bunobuno.db.DBScheme;
import kr.ewhapp.kjw.bunobuno.db.GetDBRequest;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class SelectAccountDBRequest extends GetDBRequest<NumberData>{

	public SelectAccountDBRequest(NumberData list, String flag) {
		super(list, flag);
	}

	@Override
	public boolean onExecuteQuery(SQLiteDatabase database, String query, NumberData result) {

		Cursor cursor = database.rawQuery(DBScheme.selectAccountInfo(query), null);
		Log.i("SelectAccountDBRequest", cursor.getCount()+"");
		while ( ( cursor.moveToNext() ) ) {
			result.title = (cursor.getString(cursor.getColumnIndex(("Title"))));
			result.name = (cursor.getString(cursor.getColumnIndex("Name")));
			result.bank = (cursor.getInt((cursor.getColumnIndex("Bank"))));
			result.number = (cursor.getString(cursor.getColumnIndex("Number")));
		}
		
		return true;
	}
	
	

}
