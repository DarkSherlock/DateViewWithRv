package com.liang.tind.dateviewdemo.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class UIutils {
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);

	}

	public static int getWindowHeight(Activity activity) {
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(mDisplayMetrics);

		return mDisplayMetrics.heightPixels;
	}

	public static int getWindowWidth(Activity activity) {
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(mDisplayMetrics);

		return mDisplayMetrics.heightPixels;
	}
	public static View getHorizantalLineView(Context context) {
		// 横线
		View line = new View(context);
		line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				UIutils.dip2px(context, 0.8f)));
		line.setBackgroundColor(context.getResources()
				.getColor(Color.WHITE));

		return line;
	}

	public static int getStatusBarHeight(Context context) {
		int resourceId = context.getResources().getIdentifier(
				"status_bar_height", "dimen", "android");
		return context.getResources().getDimensionPixelSize(resourceId);
	}
}
