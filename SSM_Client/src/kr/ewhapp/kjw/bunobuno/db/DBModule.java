package kr.ewhapp.kjw.bunobuno.db;

import java.util.ArrayList;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;

public class DBModule extends Thread {

	private static DBModule instance;
	
	private ArrayList<DBRequest> requests = new ArrayList<DBRequest>();
	
	private Handler	handler;
	
	private boolean	isStarted	= false;
	
	private SQLiteOpenHelper sqLiteOpenHelper;
	
	private DBModule() {
		
	}

	public static DBModule getInstance() {
	
		if(instance == null)
			instance = new DBModule();
		
		return instance;
	}
	
	public synchronized boolean startThread( SQLiteOpenHelper sqLiteOpenHelper, Handler handler ) {
		
		if ( this.isStarted )
			return false;
		
		this.sqLiteOpenHelper = sqLiteOpenHelper;
		this.handler = handler;
		
		this.isStarted = true;
		this.start();
		
		return true;
	}
	
	public synchronized boolean endThread() {
		
		if ( isStarted == false )
			return false;
		
		isStarted = false;
		notifyAll();
		
		return true;
	}
	
	public synchronized boolean enqueue( DBRequest request ) {
	
		synchronized ( requests ) {
			requests.add( request );
		}
		
		notifyAll();
		return true;
	}
	
	public synchronized DBRequest dequeue() {
		
		DBRequest request;
		
		if ( requests.size() == 0 ) {
			try {
				wait();
			}
			catch ( InterruptedException e ) {
				e.printStackTrace();
				return null;
			}
		}
		synchronized ( requests ) {
			request = requests.remove( 0 );
		}
		
		return request;
	}
	
	@Override
	public void run() {
	
		while ( isStarted ) {
			
			DBRequest request = dequeue();
			if ( request == null ) {
				continue;
			}
			
			SQLiteDatabase database;
			if ( request.isWritable() )
				database = sqLiteOpenHelper.getWritableDatabase();
			else
				database = sqLiteOpenHelper.getReadableDatabase();
			
			request.process( database, handler );
			database.close();
		}
	}
	
}
