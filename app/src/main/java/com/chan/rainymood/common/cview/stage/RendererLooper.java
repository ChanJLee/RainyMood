package com.chan.rainymood.common.cview.stage;

import android.view.SurfaceHolder;

/**
 * Created by chan on 2018/7/9.
 */

public class RendererLooper implements Runnable {
	private float mWidth;
	private float mHeight;
	private SurfaceHolder mHolder;

	public RendererLooper(float width, float height, SurfaceHolder holder) {
		mWidth = width;
		mHeight = height;
		mHolder = holder;
	}

	@Override
	public void run() {

	}
}
