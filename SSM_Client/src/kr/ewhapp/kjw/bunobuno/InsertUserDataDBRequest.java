package kr.ewhapp.kjw.bunobuno;

import kr.ewhapp.kjw.bunobuno.db.DBScheme;
import kr.ewhapp.kjw.bunobuno.db.EditDBRequest;
import kr.ssm.sniffing.userinfo.UserData;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class InsertUserDataDBRequest extends EditDBRequest{

	private UserData data;
	
	public InsertUserDataDBRequest(UserData data) {
		this.data = data;
	}

	@Override
	public boolean onExecuteSQL(SQLiteDatabase database) {

		database.execSQL(DBScheme.addUserInfo(data));
		Log.i("InsertUserData", DBScheme.addUserInfo(data));
		return true;
	}

}
