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
					activity.getWindow().addFlags(
						WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
						WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
						WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
					);
					Log.i("KCKeepAwake", "activate(): Added window flags");
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
					activity.getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
						WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
						WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
					);
					Log.i("KCKeepAwake", "deactivate(): Cleared window flags");
				}
			});
		} else {
			Log.e("KCKeepAwake", "deactivate(): No current activity found");
		}
	}
}
