package kr.ewhapp.kjw.bunobuno.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

public abstract class DBRequest {

	private boolean		isProcessSuccess	= false;
	
	protected DBError	dbError;
	
	public abstract boolean isWritable();
	
	public boolean process( SQLiteDatabase sqLiteDatabase, Handler handler ) {
		
		isProcessSuccess = onExecuteSQL( sqLiteDatabase );
		
		handler.post( new Runnable() {
			
			@Override
			public void run() {
			
				if ( isProcessSuccess )
					onDBProcessed();
				else
					onDBProcessError();
			}
		} );
		
		return false;
	}
	
	public abstract boolean onExecuteSQL( SQLiteDatabase database );
	
	public void setDBError( DBError dbError ) {
	
		this.dbError = dbError;
	}
	
	public abstract void onDBProcessed();
	
	public abstract void onDBProcessError();
	
	
}
