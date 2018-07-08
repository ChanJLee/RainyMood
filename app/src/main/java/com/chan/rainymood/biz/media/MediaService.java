package com.chan.rainymood.biz.media;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by chan on 2018/7/7.
 */

public class MediaService extends Service {
	private IMediaService.Stub mService;

	@Override
	public void onCreate() {
		super.onCreate();
		if (mService != null) {
			try {
				mService.release();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		mService = new MediaServiceImpl(this);
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return mService;
	}

	@Override
	public void onDestroy() {
		if (mService != null) {
			try {
				mService.release();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			mService = null;
		}
		super.onDestroy();
	}

	public static Intent createIntent(Context context) {
		return new Intent(context, MediaService.class);
	}
}
