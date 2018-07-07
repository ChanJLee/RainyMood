package com.chan.rainymood;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chan.rainymood.biz.setting.SettingsActivity;
import com.chan.rainymood.common.android.BaseActivity;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = SettingsActivity.createIntent(v.getContext());
				startActivity(intent);
			}
		});
	}
}
