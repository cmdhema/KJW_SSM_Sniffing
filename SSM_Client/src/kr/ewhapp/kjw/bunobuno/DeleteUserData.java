package kr.ewhapp.kjw.bunobuno;

import android.database.sqlite.SQLiteDatabase;
import kr.ewhapp.kjw.bunobuno.db.DBScheme;
import kr.ewhapp.kjw.bunobuno.db.EditDBRequest;

public class DeleteUserData extends EditDBRequest {

	@Override
	public boolean onExecuteSQL(SQLiteDatabase database) {
		database.execSQL(DBScheme.deleteUserData());
		return true;
	}

}
