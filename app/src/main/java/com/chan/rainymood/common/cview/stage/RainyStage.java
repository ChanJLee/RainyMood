package com.chan.rainymood.common.cview.stage;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by chan on 2018/7/9.
 */

public class RainyStage extends View {
	private static final int MIN_RAINS = 40;

	private List<Rain> mRains = new ArrayList<>();
	private float mOffsetX = 0;
	private float mOffsetY = 0;
	private Random mRandom;

	public RainyStage(Context context) {
		this(context, null);
	}

	public RainyStage(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RainyStage(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private ValueAnimator mValueAnimator;

	private void init() {
		mRandom = new Random(SystemClock.elapsedRealtime());
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO test code
				if (mValueAnimator != null) {
					mValueAnimator.cancel();
				}

				mValueAnimator = ValueAnimator.ofFloat(0, 1);
				mValueAnimator.setDuration(500000);
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
		});
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		float destX = getWidth() / 2;
		float destY = getHeight() * 0.8f;

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
					mRandom.nextInt(60) + 10,
					mRandom.nextInt(10) + 5,
					Color.WHITE
			);
			mRains.add(rain);
		}
	}

	public void offsetRains(float offsetX, float offsetY) {
		mOffsetX = offsetX;
		mOffsetY = offsetY;
	}
}
