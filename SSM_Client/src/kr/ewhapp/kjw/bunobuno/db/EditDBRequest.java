package kr.ewhapp.kjw.bunobuno.db;



public abstract class EditDBRequest extends DBRequest {

	private OnEditDBProcessListener listener;
	
	@Override
	public boolean isWritable() {
		return true;
	}

	@Override
	public void onDBProcessed() {
		listener.onEditSuccess(this);
	}

	@Override
	public void onDBProcessError() {
		listener.onEditError(dbError);
	}
	
	public void setOnEditDBRequestListener( OnEditDBProcessListener  request) {
		if( request != null)
			this.listener = request;
	}
	
	public interface OnEditDBProcessListener  {
		
		public void onEditSuccess(EditDBRequest request);
		
		public void onEditError(DBError error);
		
	}
}
