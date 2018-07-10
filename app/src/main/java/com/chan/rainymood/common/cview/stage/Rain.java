package com.chan.rainymood.common.cview.stage;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by chan on 2018/7/9.
 */

public class Rain {

	private float mX;
	private float mY;

	private float mStartX;
	private float mStartY;

	private float mSpeed;
	private int mColor;
	private int mRenderCount;
	private boolean mEnd = false;

	public Rain(float x, float y, float speed, int color) {
		mX = mStartX = x;
		mY = mStartY = y;
		mSpeed = speed;
		mColor = color;
	}

	public void render(Canvas canvas, float destX, float destY) {
		if (!isValid()) {
			return;
		}

		if (isEnd(destX, destY)) {
			mEnd = true;
			return;
		}

		float leftDistance = (float) Math.sqrt(Math.pow(destX - mStartX, 2) + Math.pow(destY - mStartY, 2));
		float x = mSpeed * (destX - mStartX) / leftDistance;
		float y = mSpeed * (destY - mStartY) / leftDistance;
	}

	private boolean isEnd(float destX, float destY) {
		if (mX >= destX) {
			if (mY >= destY && mStartY <= destY) {
				return true;
			} else if (mY <= destY && mStartY >= destY) {
				return true;
			}
		} else {
			if (mY >= destY && mStartY <= destY) {
				return true;
			} else if (mY <= destY && mStartY >= destY) {
				return true;
			}
		}

		return false;
	}

	public boolean isValid() {
		return mRenderCount >= 0 || mEnd;
	}
}
