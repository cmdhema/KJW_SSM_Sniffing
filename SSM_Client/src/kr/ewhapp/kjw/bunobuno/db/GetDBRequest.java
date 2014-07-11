package kr.ewhapp.kjw.bunobuno.db;

import android.database.sqlite.SQLiteDatabase;


public abstract class GetDBRequest<T> extends DBRequest {

	private OnGetDBRequestListener<T> listener;

	private T result;

	private String query;
	
	public GetDBRequest(T list, String flag) {
		this.result = list;
		this.query = flag;
	}

	@Override
	public boolean isWritable() {
		return false;
	}

	@Override
	public void onDBProcessed() {
		listener.onGetSuccess(this);
	}

	@Override
	public void onDBProcessError() {
		listener.onGetError(dbError);
	}

	@Override
	public boolean onExecuteSQL(SQLiteDatabase database) {
		return onExecuteQuery(database, query, result);
	}

	public T getResult() {
		return result;
	}

	public abstract boolean onExecuteQuery(SQLiteDatabase database, String query, T result);

	public void setOnGetDBRequest(OnGetDBRequestListener<T> listener) {
//		if(this.listener != null)
			this.listener = listener;
	}
	
	public interface OnGetDBRequestListener<T> {
		
		public void onGetSuccess(GetDBRequest<T> request);
		
		public void onGetError(DBError error);
		
	}
}
