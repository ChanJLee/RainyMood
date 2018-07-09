package com.chan.rainymood.common.cview.stage;

import android.graphics.Canvas;

/**
 * Created by chan on 2018/7/9.
 */

public class Rain {
	private static final float MAX_Z = 100;

	private float mX;
	private float mY;
	private float mZ;
	private float mSpeed;
	private int mColor;

	public Rain(float x, float y, float z, float speed, int color) {
		mX = x;
		mY = y;
		mZ = z;
		mSpeed = speed;
		mColor = color;
	}

	public void render(Canvas canvas, float eyeX, float eyeY, float frameDuration) {
		mZ += (mSpeed * frameDuration);
		float direction = (float) (Math.pow(eyeX - mX, 2) + Math.pow(eyeY - mY, 2));
		float mLength =
	}
}
