package kr.ewhapp.kjw.bunobuno.edit;

import kr.ewhapp.kjw.bunobuno.NumberData;
import kr.ewhapp.kjw.bunobuno.db.DBScheme;
import kr.ewhapp.kjw.bunobuno.db.EditDBRequest;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class AddDBRequest extends EditDBRequest{
	
	private NumberData data;
	
	private String flag;
	
	
	public AddDBRequest(NumberData data, String flag) {
		this.data = data;
		this.flag = flag;
	}

	@Override
	public boolean onExecuteSQL(SQLiteDatabase database) {

		try {
			if(flag.equals("Account")) 
				database.execSQL(DBScheme.addAccountInfo(data));
			else if (flag.equals("Site")) {
				database.execSQL(DBScheme.addSiteInfo(data));
				Log.i("AddDBRequest", DBScheme.addSiteInfo(data));
			}
			else if (flag.equals("Custom")) {
				database.execSQL(DBScheme.addCustomInfo(data));
				Log.i("AddDBRequest", DBScheme.addSiteInfo(data));
			}
			else 
				database.execSQL(DBScheme.addPhotoInfo(data));
			
			return true;
		} catch (SQLiteConstraintException e) {
			return false;
		}

	}
	
}
