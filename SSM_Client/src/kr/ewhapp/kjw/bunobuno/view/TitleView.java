package kr.ewhapp.kjw.bunobuno.view;

import kr.ewhapp.kjw.bunobuno.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class TitleView extends FrameLayout implements OnClickListener {

	private TextView titleTv;
	private ImageView kakaoTalkIv;
	
	public TitleView(Context context) {
		super(context);

		LayoutInflater.from(context).inflate(R.layout.layout_list_row, this);
		
		titleTv = (TextView) findViewById(R.id.title_tv);
		kakaoTalkIv = (ImageView) findViewById(R.id.kakao_btn);
		
		titleTv.setOnClickListener(this);
		kakaoTalkIv.setOnClickListener(this);
		
	}

	public void setTitleText(String title) {
		titleTv.setText(title);
	}

	@Override
	public void onClick(View v) {
		
	}

}
