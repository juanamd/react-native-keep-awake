package com.corbt.keepawake;

import androidx.annotation.Nullable;

import com.facebook.react.BaseReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.model.ReactModuleInfo;
import com.facebook.react.module.model.ReactModuleInfoProvider;

import java.util.HashMap;
import java.util.Map;

public class KCKeepAwakePackage extends BaseReactPackage {
	@Nullable
	@Override
	public NativeModule getModule(String name, ReactApplicationContext reactContext) {
		if (name.equals(KCKeepAwake.NAME)) {
			return new KCKeepAwake(reactContext);
		}

		return null;
	}

	@Override
	public ReactModuleInfoProvider getReactModuleInfoProvider() {
		return new ReactModuleInfoProvider() {
			@Override
			public Map<String, ReactModuleInfo> getReactModuleInfos() {
				Map<String, ReactModuleInfo> map = new HashMap<>();

				map.put(KCKeepAwake.NAME, new ReactModuleInfo(
					KCKeepAwake.NAME,
					KCKeepAwake.NAME,
					false,
					false,
					false,
					true
				));

				return map;
			}
		};
	}
}
