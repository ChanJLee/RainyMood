package com.chan.rainymood;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.chan.rainymood.biz.media.IMediaService;
import com.chan.rainymood.biz.media.Listener;
import com.chan.rainymood.biz.media.MediaError;
import com.chan.rainymood.biz.media.MediaItem;
import com.chan.rainymood.biz.media.MediaService;
import com.chan.rainymood.common.android.BaseActivity;
import com.chan.rainymood.common.cview.stage.RainyStage;
import com.chan.rainymood.common.utils.SpannableBuilder;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends BaseActivity implements View.OnClickListener, SensorEventListener {
	private static final int DURATION_ANIMATION = 500;

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
	private TextView mTvStartLabel;
	private TextView mTvPrompt;
	private ValueAnimator mStartAnimator;
	private Sensor mAccelerometerSensor;
	private SensorManager mSensorManager;
	private RainyStage mRainyStage;
	private Handler mHandler;
	private PromptRunnable mPromptRunnable = new PromptRunnable();
	private long mLastStartTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// init view
		mBtnPause = findViewById(R.id.pause);
		mBtnPlay = findViewById(R.id.play);

		mRainyStage = findViewById(R.id.rain_stage);
		mTvPrompt = findViewById(R.id.prompt);
		mTvStartLabel = findViewById(R.id.start_label);
		mBtnPlay.setOnClickListener(this);
		mBtnPause.setOnClickListener(this);
		mRainyStage.start();

		// init animator
		mStartAnimator = ValueAnimator.ofFloat(0, 1);
		mStartAnimator.setInterpolator(new AccelerateInterpolator());
		mStartAnimator.setDuration(DURATION_ANIMATION);
		mStartAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float alpha = (float) animation.getAnimatedValue();
				mBtnPlay.setAlpha(1 - alpha);
				mBtnPause.setAlpha(alpha);
				mTvStartLabel.setAlpha(1 - alpha);
				mTvPrompt.setAlpha(alpha);
			}
		});

		// init media service
		Intent intent = MediaService.createIntent(this);
		bindService(intent, mConnection, BIND_AUTO_CREATE);

		// init accelerometer sensor
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		if (mSensorManager != null) {
			mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		}

		mHandler = new Handler();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mSensorManager != null) {
			mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
		}
	}

	@Override
	protected void onStop() {
		if (mSensorManager != null) {
			mSensorManager.unregisterListener(this);
		}
		super.onStop();
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
		if (mService == null) {
			return;
		}

		mHandler.removeCallbacks(mPromptRunnable);
		mBtnPlay.setClickable(true);
		mBtnPause.setClickable(false);
		try {
			mService.pause();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void play() {
		if (mService == null) {
			return;
		}

		mLastStartTime = System.currentTimeMillis();
		mHandler.post(mPromptRunnable);
		mBtnPlay.setClickable(false);
		mBtnPause.setClickable(true);
		MediaItem mediaItem = new MediaItem();
		mediaItem.looping = true;
		mediaItem.url = "file:///android_asset/audio/rainy.m4a";
		try {
			mService.play(mediaItem, new Listener.Stub() {
				@Override
				public void onPlay() throws RemoteException {
					mStartAnimator.start();
				}

				@Override
				public void onPause() throws RemoteException {
					mStartAnimator.reverse();
				}

				@Override
				public void onStop() throws RemoteException {
					mStartAnimator.reverse();
				}

				@Override
				public void onEnd() throws RemoteException {
					mStartAnimator.reverse();
				}

				@Override
				public void onError(MediaError error) throws RemoteException {
					Toast.makeText(MainActivity.this, "发生错误: " + error.code, Toast.LENGTH_SHORT).show();
					mStartAnimator.reverse();
				}
			});
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float[] values = event.values;
		mRainyStage.offsetRains(filterSensorValue(-values[0]), filterSensorValue(values[1]));
	}

	private static float filterSensorValue(float value) {
		return Math.abs(value) < 0.5f ? 0f : value * 50;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		/* do nothing */
	}

	private class PromptRunnable implements Runnable {

		private int mLastHour = -1;
		private int mLastMinute = -1;

		@Override
		@SuppressLint("DefaultLocale")
		public void run() {
			Calendar calendar = Calendar.getInstance();
			int hour = calendar.get(Calendar.HOUR);
			int minute = calendar.get(Calendar.MINUTE);
			if (hour != mLastHour && minute != mLastMinute) {
				mLastHour = hour;
				mLastMinute = minute;
				SpannableBuilder spannableBuilder = new SpannableBuilder(String.format("%02d : %02d\n", hour, minute));
				if (mLastStartTime > 0) {
					calendar.setTime(new Date(mLastStartTime));
					spannableBuilder.nextSpannable(
							String.format("上次启动时间：%02d : %02d\n", calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE)))
							.setTextSize((int) (mTvPrompt.getTextSize() / 2.5f));
				}
				mTvPrompt.setText(spannableBuilder.finish());
			}

			mHandler.postDelayed(mPromptRunnable, 1000);
		}
	}
}
