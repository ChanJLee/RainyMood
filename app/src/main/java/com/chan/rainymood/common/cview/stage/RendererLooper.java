package com.chan.rainymood.common.cview.stage;

import android.graphics.Canvas;
import android.graphics.Color;
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
		mRain = new Rain(width / 2, height / 2, 10, Color.WHITE);
	}

	@Override
	public void run() {
		Canvas canvas = mHolder.getSurface().lockCanvas(null);
		mRain.render(canvas, mWidth / 2, mHeight * 0.8F);
		mHolder.getSurface().unlockCanvasAndPost(canvas);
	}
}
