package kr.ewhapp.kjw.bunobuno.edit;

import kr.ewhapp.kjw.bunobuno.NumberData;
import kr.ewhapp.kjw.bunobuno.db.DBScheme;
import kr.ewhapp.kjw.bunobuno.db.GetDBRequest;
import kr.ewhapp.kjw.bunobuno.encryption.Encrypt;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class SelectCustomDBRequest extends GetDBRequest<NumberData>{

	public SelectCustomDBRequest(NumberData list, String flag) {
		super(list, flag);
	}

	@Override
	public boolean onExecuteQuery(SQLiteDatabase database, String query, NumberData result) {
		
		Cursor cursor = database.rawQuery(DBScheme.selectCustomInfo(query), null);
		
		while( (cursor.moveToNext())) {
			result.title = cursor.getString(cursor.getColumnIndex("Title"));
			result.name = cursor.getString(cursor.getColumnIndex("Name"));
			result.number = Encrypt.decode(cursor.getString(cursor.getColumnIndex("Password")));
		}
		return true;
	}

}
