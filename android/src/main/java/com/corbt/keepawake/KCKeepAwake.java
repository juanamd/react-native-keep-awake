// Adapted from
// https://github.com/gijoehosaphat/react-native-keep-screen-on

package com.corbt.keepawake;

import android.app.Activity;
import android.util.Log;
import android.view.WindowManager;
import android.os.Build;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.content.Context;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class KCKeepAwake extends ReactContextBaseJavaModule {

	private static final String TAG = "KCKeepAwake";
	private static WakeLock wakeLock;

	public KCKeepAwake(ReactApplicationContext reactContext) {
		super(reactContext);
	}

	@Override
	public String getName() {
		return TAG;
	}

	@ReactMethod
	public void activate() {
		final Activity activity = getCurrentActivity();

		if (activity != null) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					useWindowFlags(activity, true);
					useActivityScreenMethods(activity, true);
					usePowerManagerWakeup(activity, true);
				}
			});
		} else {
			Log.e(TAG, "activate(): No current activity found");
		}
	}

	@ReactMethod
	public void deactivate() {
		final Activity activity = getCurrentActivity();

		if (activity != null) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					useWindowFlags(activity, false);
					useActivityScreenMethods(activity, false);
					usePowerManagerWakeup(activity, false);
				}
			});
		} else {
			Log.e(TAG, "deactivate(): No current activity found");
		}
	}

	private static void useWindowFlags(final Activity activity, boolean activate) {
		if (activate == true) {
			activity.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
			);
			Log.i(TAG, "useWindowFlags(): Added window flags");
		} else {
			activity.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
			);
			Log.i(TAG, "useWindowFlags(): Cleared window flags");
		}
	}

	private static void useActivityScreenMethods(final Activity activity, boolean activate) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
			try {
				activity.setTurnScreenOn(activate);
				activity.setShowWhenLocked(activate);
				Log.i(TAG, "useActivityScreenMethods(): Set screen methods to " + activate);
			} catch (Exception e) {
				Log.e(TAG, "Enable setTurnScreenOn and setShowWhenLocked is not present on device!");
			}
		}
	}

	private static void usePowerManagerWakeup(final Activity activity, boolean acquire) {
		if (acquire == true) {
			try {
				PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
				wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG);
				wakeLock.acquire(5000);
				Log.i(TAG, "usePowerManagerWakeup(): Acquired wakeLock");
			} catch (Exception e) {
				Log.e(TAG, "usePowerManagerWakeup(): Wakelock error: " + e.toString());
			}
		} else if (wakeLock != null) {
			try {
				if (wakeLock.isHeld()) {
					wakeLock.release();
					Log.i(TAG, "usePowerManagerWakeup(): Released wakeLock");
				}
			} catch (Exception e) {
				Log.e(TAG, "usePowerManagerWakeup(): Wakelock release error: " + e.toString());
			}
		}
	}
}
