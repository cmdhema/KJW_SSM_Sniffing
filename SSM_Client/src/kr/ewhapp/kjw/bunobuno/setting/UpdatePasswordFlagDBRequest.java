package kr.ewhapp.kjw.bunobuno.setting;

import android.database.sqlite.SQLiteDatabase;
import kr.ewhapp.kjw.bunobuno.db.DBScheme;
import kr.ewhapp.kjw.bunobuno.db.EditDBRequest;
import kr.ssm.sniffing.userinfo.UserData;

public class UpdatePasswordFlagDBRequest extends EditDBRequest {

	
	private UserData data;

	public UpdatePasswordFlagDBRequest(UserData data) {
		this.data = data;
	}

	@Override
	public boolean onExecuteSQL(SQLiteDatabase database) {

		database.execSQL(DBScheme.updatePasswordFlag(data));
		return true;
	}

}
