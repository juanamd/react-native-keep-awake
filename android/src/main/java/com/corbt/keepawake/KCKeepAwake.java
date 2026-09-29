// Adapted from
// https://github.com/gijoehosaphat/react-native-keep-screen-on

package com.corbt.keepawake;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.WindowManager;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;

public class KCKeepAwake extends NativeKCKeepAwakeSpec {
	public static final String NAME = "KCKeepAwake";
	private static final String TAG = NAME;
	private static WakeLock wakeLock;

	public KCKeepAwake(ReactApplicationContext reactContext) {
		super(reactContext);
	}

	@Override
	public String getName() {
		return TAG;
	}

	@Override
	public void activate(double wakelockMs, Promise promise) {
		setActive(true, (int) wakelockMs, promise);
	}

	@Override
	public void deactivate(Promise promise) {
		setActive(false, -1, promise);
	}

	private void setActive(final boolean activate, final int wakelockMs, final Promise promise) {
		final Activity activity = getCurrentActivity();
		if (activity != null) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					try {
						useWindowFlags(activity, activate);
						useActivityScreenMethods(activity, activate);
						usePowerManagerWakeup(activity, activate, wakelockMs);
						promise.resolve(null);
					} catch (Exception e) {
						Log.e(TAG, "Unable to update keep-awake state", e);
						promise.reject(e);
					}
				}
			});
		} else {
			promise.reject(new Exception("No current activity found"));
		}
	}

	private static void useWindowFlags(final Activity activity, final boolean activate) {
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

	private static void useActivityScreenMethods(final Activity activity, final boolean activate) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
			try {
				activity.setTurnScreenOn(activate);
				activity.setShowWhenLocked(activate);
				Log.i(TAG, "useActivityScreenMethods(): Set screen methods to " + activate);
			} catch (Exception e) {
				Log.e(TAG, "Error using activity methods. Activate: " + activate, e);
			}
		}
	}

	private static void usePowerManagerWakeup(final Activity activity, final boolean acquire, final int ms) {
		if (acquire && ms > 0) {
			try {
				PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
				wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
				wakeLock.acquire(ms);
				Log.i(TAG, "usePowerManagerWakeup(): Acquired wakeLock");
			} catch (Exception e) {
				Log.e(TAG, "usePowerManagerWakeup(): Wakelock error", e);
			}
		} else if (wakeLock != null) {
			try {
				if (wakeLock.isHeld()) {
					wakeLock.release();
					Log.i(TAG, "usePowerManagerWakeup(): Released wakeLock");
				}
			} catch (Exception e) {
				Log.e(TAG, "usePowerManagerWakeup(): Wakelock release error", e);
			}
		}
	}
}
