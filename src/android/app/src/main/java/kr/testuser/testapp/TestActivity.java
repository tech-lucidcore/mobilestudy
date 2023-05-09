package kr.testuser.testapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class TestActivity extends Activity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FrameLayout view = new FrameLayout(this);
		view.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT));
		setContentView(view);
		TextView testView = new TextView(this);
		testView.setTextColor(0xFF000000);
		testView.setTextSize(30);
		testView.setText("Test");
		view.addView(testView, new FrameLayout.LayoutParams(300, 300, Gravity.CENTER));
	}
}
