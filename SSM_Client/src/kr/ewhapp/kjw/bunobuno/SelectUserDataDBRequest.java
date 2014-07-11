package kr.ewhapp.kjw.bunobuno;

import kr.ewhapp.kjw.bunobuno.db.DBScheme;
import kr.ewhapp.kjw.bunobuno.db.GetDBRequest;
import kr.ewhapp.kjw.bunobuno.encryption.Encrypt;
import kr.ssm.sniffing.userinfo.UserData;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SelectUserDataDBRequest extends GetDBRequest<UserData> {

	public SelectUserDataDBRequest(UserData list, String flag) {
		super(list, flag);
	}

	@Override
	public boolean onExecuteQuery(SQLiteDatabase database, String query, UserData result) {
		
		Cursor cursor = database.rawQuery(DBScheme.selectUserInfo(), null);
		Log.i("SelectUserDataDBRequest",DBScheme.selectUserInfo());
		Log.i("SelectuserData", cursor.getCount()+"°³");
		while ( ( cursor.moveToNext())) {
			result.phoneNumber = cursor.getString(cursor.getColumnIndex("PhoneNumber"));
			result.regId = Encrypt.decode(cursor.getString(cursor.getColumnIndex("GCMRegId")));
			result.password = Encrypt.decode(cursor.getString(cursor.getColumnIndex("Password")));
			result.hint = cursor.getString(cursor.getColumnIndex("Hint"));
			result.passwordFlag = cursor.getInt(cursor.getColumnIndex("PasswordFlag"));
		}
		return true;
	}
	
}
