package com.chan.rainymood.biz.media;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by chan on 2018/7/7.
 */

public class MediaService extends Service {

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
