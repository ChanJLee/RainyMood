package com.chan.rainymood.common.cview.stage;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by chan on 2018/7/9.
 */

public class RainyStage extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = "RainyStage";

	private Thread mRendererThread;

	public RainyStage(Context context) {
		this(context, null);
	}

	public RainyStage(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RainyStage(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		getHolder().addCallback(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (mRendererThread != null) {
			try {
				mRendererThread.interrupt();
			} catch (Exception e) {
				e.printStackTrace();
				e("interrupt thread failed");
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		mRendererThread = new Thread(new RendererLooper(width, height, holder));
		mRendererThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mRendererThread != null) {
			try {
				mRendererThread.interrupt();
			} catch (Exception e) {
				e.printStackTrace();
				e("interrupt thread failed");
			}
		}
	}

	private static void e(String msg) {
		Log.e(TAG, msg);
	}
}
