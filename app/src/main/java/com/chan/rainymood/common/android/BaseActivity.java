package com.chan.rainymood.common.android;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.chan.rainymood.common.utils.RomUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by chan on 2018/7/7.
 */
public class BaseActivity extends AppCompatActivity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window window = getWindow();
		if (window == null) {
			return;
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			//for new api versions. api >= LOLLIPOP
			if (!RomUtils.isEMUI3_1()) {
				// fix emui 3.1's bug
				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			}

			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);

			// set text color. black
			//fitStatusBarColor();
		}
	}

	public void fitStatusBarColor() {
		Window window = getWindow();
		if (window == null) {
			return;
		}

		// 6.0 设置状态栏字体颜色
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			View decor = window.getDecorView();
			decor.setSystemUiVisibility(decor.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
			return;
		}

		if (RomUtils.isMiui()) {
			fitMiuiStatusBarColor();
		} else if (RomUtils.isFlyme()) {
			fitFlymeStatusBarColor();
		}
	}

	private void fitMiuiStatusBarColor() {
		Window window = getWindow();
		if (window == null) {
			return;
		}

		Class<?> clazz = window.getClass();
		try {
			Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
			Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
			int darkModeFlag = field.getInt(layoutParams);
			Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
			extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void fitFlymeStatusBarColor() {
		try {
			Window window = getWindow();
			if (window == null) {
				return;
			}

			WindowManager.LayoutParams lp = window.getAttributes();
			Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
			Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
			darkFlag.setAccessible(true);
			meizuFlags.setAccessible(true);
			int bit = darkFlag.getInt(null);
			int value = meizuFlags.getInt(lp);
			value |= bit;
			meizuFlags.setInt(lp, value);
			window.setAttributes(lp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
