package com.chan.rainymood.biz.media;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.RemoteException;

import java.io.IOException;

/**
 * Created by chan on 2018/7/8.
 */

public class MediaServiceImpl extends IMediaService.Stub {
	private static final String URL_ASSETS_PREFIX = "file:///android_asset/";

	private MediaPlayer mMediaPlayer;
	private Context mContext;
	private Listener mListener;

	public MediaServiceImpl(Context context) {
		mContext = context;
	}

	@Override
	public void play(MediaItem item, Listener listener) throws RemoteException {
		release();

		// just support asset
		if (!item.url.startsWith(URL_ASSETS_PREFIX)) {
			return;
		}

		mListener = listener;
		try {
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					if (mListener != null) {
						try {
							mListener.onEnd();
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
				}
			});
			mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					if (mListener != null) {
						MediaError mediaError = new MediaError();
						mediaError.code = MediaError.ERROR_CORE;
						mediaError.msg = "media core";
						try {
							mListener.onError(mediaError);
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
					return false;
				}
			});

			// just play assert resource
			String file = item.url.replace(URL_ASSETS_PREFIX, "");
			AssetFileDescriptor descriptor = mContext.getAssets().openFd(file);
			mMediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();
			mMediaPlayer.prepare();
			mMediaPlayer.setLooping(item.looping);
			mMediaPlayer.start();
			if (mListener != null) {
				mListener.onPlay();
			}
		} catch (IOException e) {
			if (mListener != null) {
				MediaError mediaError = new MediaError();
				mediaError.code = MediaError.ERROR_IO;
				mediaError.msg = e.getMessage();
				mListener.onError(mediaError);
			}
		}
	}

	@Override
	public void pause() throws RemoteException {
		if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
			mMediaPlayer.pause();
			if (mListener != null) {
				mListener.onPause();
			}
		}
	}

	@Override
	public void stop() throws RemoteException {
		if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
			mMediaPlayer.stop();
			if (mListener != null) {
				mListener.onStop();
			}
		}
	}

	@Override
	public void release() throws RemoteException {
		stop();
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
		}
	}

	@Override
	public boolean isPlaying() throws RemoteException {
		return mMediaPlayer != null && mMediaPlayer.isPlaying();
	}
}
