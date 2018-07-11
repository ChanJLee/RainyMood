package com.chan.rainymood.common.cview.stage;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by chan on 2018/7/9.
 */

public class RendererLooper implements Runnable {
	private float mWidth;
	private float mHeight;
	private SurfaceHolder mHolder;
	private Rain mRain;

	public RendererLooper(float width, float height, SurfaceHolder holder) {
		mWidth = width;
		mHeight = height;
		mHolder = holder;
		mRain = new Rain(width / 2, height / 2, 5, Color.WHITE);
	}

	@Override
	public void run() {

		long timestamp = 0;
		while (true) {
			Canvas canvas = mHolder.lockCanvas(null);

			if (canvas != null) {
				// clear stage
				canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

				// TODO render rain, now only one
				// 缺陷，surface view 效率跟不上
				mRain.render(canvas, mWidth / 2, mHeight * 0.8F, 0, 0);
			}

			try {
				mHolder.unlockCanvasAndPost(canvas);
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			}

			timestamp = SystemClock.elapsedRealtime() - timestamp;
			try {
				if (timestamp < 16) {
					Thread.sleep(16 - timestamp);
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
}
