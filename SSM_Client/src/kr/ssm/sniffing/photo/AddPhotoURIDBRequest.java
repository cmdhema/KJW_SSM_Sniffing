package kr.ssm.sniffing.photo;

import kr.ewhapp.kjw.bunobuno.NumberData;
import kr.ewhapp.kjw.bunobuno.db.DBScheme;
import kr.ewhapp.kjw.bunobuno.db.EditDBRequest;
import android.database.sqlite.SQLiteDatabase;

public class AddPhotoURIDBRequest extends EditDBRequest{

	private NumberData data;

	public AddPhotoURIDBRequest(NumberData data) {
		this.data = data;
	}

	@Override
	public boolean onExecuteSQL(SQLiteDatabase database) {
		database.execSQL(DBScheme.addPhotoInfo(data));
		return true;
	}

}
