package kr.ewhapp.kjw.bunobuno.edit;

import kr.ewhapp.kjw.bunobuno.NumberData;
import kr.ewhapp.kjw.bunobuno.db.DBScheme;
import kr.ewhapp.kjw.bunobuno.db.EditDBRequest;
import android.database.sqlite.SQLiteDatabase;


public class UpdateDBRequest extends EditDBRequest {

	private NumberData data;
	private String flag;
	
	public UpdateDBRequest(NumberData data, String flag) {
		this.data = data;
		this.flag = flag;
	}

	@Override
	public boolean onExecuteSQL(SQLiteDatabase database) {

		if(flag.equals("Account"))
			database.execSQL(DBScheme.updateAccountInfo(data));
		else if (flag.equals("Site"))
			database.execSQL(DBScheme.updateSiteInfo(data));
		else if (flag.equals("Custom")) 
			database.execSQL(DBScheme.updateCustomInfo(data));

		return true;
	}

	
}
