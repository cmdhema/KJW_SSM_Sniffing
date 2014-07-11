package kr.ssm.sniffing.calling;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;

public class CallerInfo {

	public static String getCallerName(Context context, String number) {
		String name;
		
		String[] projection = new String[] {PhoneLookup.DISPLAY_NAME};
		
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
		
		Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
		
		if(cursor.moveToFirst()) {
			int nameIdx = cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			name = cursor.getString(nameIdx);
		} else {
			name = "NONE";
		}
		
		return name;
	}
}
