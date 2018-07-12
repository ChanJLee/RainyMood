package com.chan.rainymood.common.cview.stage;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by chan on 2018/7/9.
 */

public class Rain {
	private float mStartX;
	private float mStartY;
	private float mSpeed;
	private float mLength;
	private boolean mEnd = false;
	private Paint mPaint;

	public Rain(float x, float y, float speed, float length, float width, int color) {
		mStartX = x;
		mStartY = y;
		mSpeed = speed;
		mLength = length;

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(color);
		mPaint.setStrokeWidth(width);
	}

	public void render(Canvas canvas, float destX, float destY, float offsetX, float offsetY) {
		if (canvas == null) {
			return;
		}

		if (isEnd()) {
			return;
		}

		float deltaX = mStartX - destX;
		float deltaY = mStartY - destY;
		float distance = (float) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

		if (distance == 0 || distance <= mLength || mStartY >= destY) {
			mEnd = true;
			return;
		}

		float endX = mStartX + mLength / distance * deltaX;
		float endY = mStartY + mLength / distance * deltaY;
		canvas.drawLine(mStartX, mStartY, endX, endY, mPaint);

		mStartX = endX;
		mStartY = endY;
	}

	public boolean isEnd() {
		return mEnd;
	}
}
