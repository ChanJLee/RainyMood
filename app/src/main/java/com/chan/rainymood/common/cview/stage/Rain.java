package com.chan.rainymood.common.cview.stage;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by chan on 2018/7/9.
 */

public class Rain {
	private float mStartX;
	private float mStartY;

	private float mSpeed;
	private boolean mEnd = false;
	private Paint mPaint;

	public Rain(float x, float y, float speed, int color) {
		mStartX = x;
		mStartY = y;
		mSpeed = speed;

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(color);
		mPaint.setStrokeWidth(20);
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

		if (distance < mSpeed || distance == 0f) {
			mEnd = true;
			return;
		}

		long x = SystemClock.elapsedRealtime();
		float endX = deltaX / distance * mSpeed + mStartX + offsetX;
		float endY = deltaY / distance * mSpeed + mStartY + offsetY;

		canvas.drawLine(mStartX, mStartY, endX, endY, mPaint);
		mStartX = endX;
		mStartY = endY;

		if (SystemClock.elapsedRealtime() - x > 200) {
			Log.d("chan_debug", "data" + mStartX + " " + mStartY + " " + endX + " " + endY);
		}
	}

	public boolean isEnd() {
		return mEnd;
	}
}
