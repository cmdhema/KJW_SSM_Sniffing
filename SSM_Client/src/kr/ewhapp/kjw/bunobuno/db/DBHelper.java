package kr.ewhapp.kjw.bunobuno.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String	DB_NAME		= "bunobuno.db";
	private static final int	DB_VERSION	= 1;
	
	public DBHelper( Context context ) {
	
		super( context, DB_NAME, null, DB_VERSION );
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DBScheme.createUserInfoTable());
		db.execSQL(DBScheme.createAccountTable());
		db.execSQL(DBScheme.createCustomTable());
		db.execSQL(DBScheme.createPhotoTable());
		db.execSQL(DBScheme.createSiteTable());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}
