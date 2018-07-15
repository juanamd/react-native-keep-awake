// Adapted from
// https://github.com/gijoehosaphat/react-native-keep-screen-on

package com.corbt.keepawake;

import android.app.Activity;
import android.util.Log;
import android.view.WindowManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class KCKeepAwake extends ReactContextBaseJavaModule {

	public KCKeepAwake(ReactApplicationContext reactContext) {
		super(reactContext);
	}

	@Override
	public String getName() {
		return "KCKeepAwake";
	}

	@ReactMethod
	public void activate() {
		final Activity activity = getCurrentActivity();

		if (activity != null) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (android.os.Build.VERSION.SDK_INT >= 27) {
						activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
						activity.setShowWhenLocked(true);
						activity.setTurnScreenOn(true);
						Log.i("KCKeepAwake", "activate(): Added KEEP_SCREEN_ON flag and api 27 methods");
					} else {
						activity.getWindow().addFlags(
							WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
							WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
							WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						);
						Log.i("KCKeepAwake", "activate(): Added window flags");
					}
				}
			});
		} else {
			Log.e("KCKeepAwake", "activate(): No current activity found");
		}
	}

	@ReactMethod
	public void deactivate() {
		final Activity activity = getCurrentActivity();

		if (activity != null) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (android.os.Build.VERSION.SDK_INT >= 27) {
						activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
						activity.setShowWhenLocked(false);
						activity.setTurnScreenOn(false);
						Log.i("KCKeepAwake", "deactivate(): Removed KEEP_SCREEN_ON flag and api 27 methods");
					} else {
						activity.getWindow().clearFlags(
							WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
							WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
							WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						);
						Log.i("KCKeepAwake", "deactivate(): Removed window flags");
					}
				}
			});
		} else {
			Log.e("KCKeepAwake", "deactivate(): No current activity found");
		}
	}
}
