package com.chan.rainymood.common.cview.stage;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;

import com.chan.rainymood.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by chan on 2018/7/9.
 */

public class RainyStage extends View {
	private static final int MIN_RAINS = 80;

	private List<Rain> mRains = new ArrayList<>();
	private float mOffsetX = 0;
	private float mOffsetY = 0;
	private Random mRandom;
	private int mColors[];

	public RainyStage(Context context) {
		this(context, null);
	}

	public RainyStage(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RainyStage(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private ValueAnimator mValueAnimator;

	private void init(Context context) {
		mRandom = new Random(SystemClock.elapsedRealtime());
		mColors = new int[5];
		mColors[0] = ContextCompat.getColor(context, R.color.colorAccent);
		mColors[1] = Color.WHITE;
		mColors[2] = Color.WHITE;
		mColors[3] = Color.WHITE;
		mColors[4] = Color.WHITE;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int width = getWidth();
		if (width == 0) {
			return;
		}

		int height = getHeight();
		if (height == 0) {
			return;
		}

		float destX = width / 2;
		float destY = height * 1.2f;

		inflateRain();
		Iterator<Rain> iterator = mRains.iterator();
		while (iterator.hasNext()) {
			Rain rain = iterator.next();
			rain.render(canvas, destX, destY, mOffsetX, mOffsetY);
			if (rain.isEnd()) {
				iterator.remove();
			}
		}
	}

	private void inflateRain() {
		if (mRains.size() >= MIN_RAINS) {
			return;
		}

		int width = getWidth();
		int height = getHeight();
		int size = MIN_RAINS - mRains.size();
		for (int i = 0; i < size; ++i) {
			Rain rain = new Rain(
					mRandom.nextInt(width),
					mRandom.nextInt(height),
					mRandom.nextInt(80) + 10,
					mRandom.nextInt(5) + 5,
					mColors[mRandom.nextInt(mColors.length)]
			);
			mRains.add(rain);
		}
	}

	public void offsetRains(float offsetX, float offsetY) {
		mOffsetX = offsetX;
		mOffsetY = offsetY;
	}

	public void start() {
		if (getWidth() < 0 || getHeight() < 0) {
			getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
				@Override
				public void onDraw() {
					getViewTreeObserver().removeOnDrawListener(this);
					start();
				}
			});
			return;
		}

		cancel();
		mValueAnimator = ValueAnimator.ofInt(0, Integer.MAX_VALUE);
		mValueAnimator.setDuration(Long.MAX_VALUE);
		mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				invalidate();
			}
		});
		mValueAnimator.setInterpolator(new LinearInterpolator());
		mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
		mValueAnimator.start();
	}

	public void cancel() {
		if (mValueAnimator != null) {
			mValueAnimator.cancel();
		}
	}
}
