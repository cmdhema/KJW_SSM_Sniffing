package kr.ewhapp.kjw.bunobuno.view;

import kr.ewhapp.kjw.bunobuno.R;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class PhotoView extends FrameLayout {

	private ImageView gridImageView;
	
	public PhotoView(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.layout_grid_item, this);
		
		gridImageView = (ImageView) findViewById(R.id.grid_row);
	}

	public void setPhoto(String photoData) {
		gridImageView.setImageURI(Uri.parse(photoData));
	}

}
