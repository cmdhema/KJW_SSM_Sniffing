package kr.ewhapp.kjw.bunobuno.edit;

import kr.ewhapp.kjw.bunobuno.NumberData;
import kr.ewhapp.kjw.bunobuno.db.DBScheme;
import kr.ewhapp.kjw.bunobuno.db.EditDBRequest;
import android.database.sqlite.SQLiteDatabase;


public class DeleteDBRequest extends EditDBRequest {

	private NumberData data;
	
	private String flag;
	
	
	
	public DeleteDBRequest(NumberData data, String flag) {
		this.data = data;
		this.flag = flag;
	}



	@Override
	public boolean onExecuteSQL(SQLiteDatabase database) {
		
		if(flag.equals("Account"))
			database.execSQL(DBScheme.deleteAccontInfo(data));
		else if (flag.equals("Site"))
			database.execSQL(DBScheme.deleteSiteInfo(data));
		else if (flag.equals("Custom")) 
			database.execSQL(DBScheme.deleteCustomInfo(data));
		else 
			database.execSQL(DBScheme.deletePhotoInfo(data));
		
		return true;
	}

}
