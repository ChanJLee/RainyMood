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

		float deltaX = mStartX - destX;
		float deltaY = mStartY - destY;
		float distance = (float) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

		if (distance == 0 || distance <= mMinDistance) {
			mEnd = true;
			return;
		}

		float endX = filterOffsetValue(mStartX - mLength / distance * deltaX, offsetX);
		float endY = filterOffsetValue(mStartY - mLength / distance * deltaY, offsetY);
		canvas.drawLine(filterOffsetValue(mStartX, offsetX), filterOffsetValue(mStartY, offsetY), endX, endY, mPaint);

		mStartX = filterOffsetValue(mStartX - mSpeed / distance * deltaX, offsetX);
		mStartY = filterOffsetValue(mStartY - mSpeed / distance * deltaY, offsetY);
	}

	private float filterOffsetValue(float value, float offset) {
		return offset >= value ? value : value + offset;
	}

	public boolean isEnd() {
		return mEnd;
	}
}
