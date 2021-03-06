package com.chan.rainymood.common.cview.stage;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by chan on 2018/7/9.
 */

public class Rain {
	private float mStartX;
	private float mStartY;
	private float mLength;
	private float mSpeed;
	private boolean mEnd = false;
	private Paint mPaint;
	private float mMinDistance;

	public Rain(float x, float y, float length, float width, int color) {
		mStartX = x;
		mStartY = y;
		mSpeed = length * 0.3f + 0.2f * width;
		mLength = length;
		mMinDistance = 10 + 3.5f * mLength;

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

		float deltaX = mStartX - (destX + offsetX);
		float deltaY = mStartY - (destY + offsetY);
		float distance = (float) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

		if (distance == 0 || distance <= mMinDistance) {
			mEnd = true;
			return;
		}

		float endY = mStartY - mLength / distance * deltaY;
		float endX = mStartX - mLength / distance * deltaX;
		canvas.drawLine(mStartX, mStartY, endX, endY, mPaint);

		mStartX = mStartX - mSpeed / distance * deltaX;
		mStartY = mStartY - mSpeed / distance * deltaY;
	}

	public boolean isEnd() {
		return mEnd;
	}
}
