package com.chan.rainymood;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

import com.chan.rainymood.biz.media.IMediaService;
import com.chan.rainymood.biz.media.Listener;
import com.chan.rainymood.biz.media.MediaError;
import com.chan.rainymood.biz.media.MediaItem;
import com.chan.rainymood.biz.media.MediaService;
import com.chan.rainymood.common.android.BaseActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

	private IMediaService mService;
	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = IMediaService.Stub.asInterface(service);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		}
	};
	private View mBtnPlay;
	private View mBtnPause;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mBtnPause = findViewById(R.id.pause);
		mBtnPlay = findViewById(R.id.play);
		mBtnPlay.setOnClickListener(this);
		mBtnPause.setOnClickListener(this);

		Intent intent = MediaService.createIntent(this);
		bindService(intent, mConnection, BIND_AUTO_CREATE);
	}

	@Override
	protected void onDestroy() {
		unbindService(mConnection);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if (v == mBtnPlay) {
			play();
		} else if (v == mBtnPause) {
			pause();
		}
	}

	private void pause() {
		if (mService != null) {
			try {
				mService.pause();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	private void play() {
		if (mService == null) {
			return;
		}

		MediaItem mediaItem = new MediaItem();
		mediaItem.url = "file:///android_asset/audio/rainy.m4a";
		try {
			mService.play(mediaItem, new Listener.Stub() {
				@Override
				public void onPlay() throws RemoteException {
					mBtnPause.setVisibility(View.VISIBLE);
					mBtnPlay.setVisibility(View.INVISIBLE);
				}

				@Override
				public void onPause() throws RemoteException {
					mBtnPause.setVisibility(View.INVISIBLE);
					mBtnPlay.setVisibility(View.VISIBLE);
				}

				@Override
				public void onStop() throws RemoteException {
					mBtnPause.setVisibility(View.INVISIBLE);
					mBtnPlay.setVisibility(View.VISIBLE);
				}

				@Override
				public void onEnd() throws RemoteException {
					mBtnPause.setVisibility(View.INVISIBLE);
					mBtnPlay.setVisibility(View.VISIBLE);
				}

				@Override
				public void onError(MediaError error) throws RemoteException {
					Toast.makeText(MainActivity.this, "发生错误: " + error.code, Toast.LENGTH_SHORT).show();
					mBtnPause.setVisibility(View.INVISIBLE);
					mBtnPlay.setVisibility(View.VISIBLE);
				}
			});
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
