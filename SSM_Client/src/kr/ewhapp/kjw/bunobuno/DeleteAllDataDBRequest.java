package kr.ewhapp.kjw.bunobuno;

import android.database.sqlite.SQLiteDatabase;
import kr.ewhapp.kjw.bunobuno.db.DBScheme;
import kr.ewhapp.kjw.bunobuno.db.EditDBRequest;

public class DeleteAllDataDBRequest extends EditDBRequest {

	@Override
	public boolean onExecuteSQL(SQLiteDatabase database) {
//		database.execSQL(DBScheme.deleteUserData());
		database.execSQL(DBScheme.deleteAccountData());
		database.execSQL(DBScheme.deleteCustomData());
		database.execSQL(DBScheme.deleteSiteData());
		database.execSQL(DBScheme.deletePhotoData());
		return true;
	}

}
