package com.chan.rainymood.common.android;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

/**
 * Created by chan on 2018/7/8.
 */

public class RainyMoodApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
	}
}
