package kr.ewhapp.kjw.bunobuno.password;

import kr.ewhapp.kjw.bunobuno.db.DBScheme;
import kr.ewhapp.kjw.bunobuno.db.EditDBRequest;
import kr.ssm.sniffing.userinfo.UserData;
import android.database.sqlite.SQLiteDatabase;

public class UpdatePasswordDBRequest extends EditDBRequest{

	private UserData data;
	
	public UpdatePasswordDBRequest(UserData data) {
		this.data = data;
	}

	@Override
	public boolean onExecuteSQL(SQLiteDatabase database) {
		database.execSQL(DBScheme.updatePassword(data));
		return true;
	}

	
}
