# react-native-keep-awake

Keep the Android screen from going to sleep while a React Native screen is active.

This package is an Android-only React Native Turbo Native Module and requires the New Architecture. It preserves the original `KeepAwake` component API and the `activate()` / `deactivate()` static methods.

## Requirements

- React Native 0.81 or newer
- React Native New Architecture enabled
- Android

Legacy architecture and iOS are not supported by this version.

## Installation

```sh
npm install https://github.com/juanamd/react-native-keep-awake.git
```

or

```sh
yarn add https://github.com/juanamd/react-native-keep-awake.git
```

React Native autolinking handles the Android package and Codegen integration. No manual changes to `MainApplication`, `settings.gradle`, or `app/build.gradle` are required for a standard React Native app.

After installing or changing the package's native code, rebuild the Android application.

## Usage

### Component

Mount `KeepAwake` in a component tree to keep the current Activity awake for as long as the component is mounted.

```tsx
import React from 'react';
import { Text, View } from 'react-native';
import KeepAwake from 'react-native-keep-awake';

export default function MyScreen() {
  return (
    <View>
      <KeepAwake />
      <Text>This screen will stay awake.</Text>
    </View>
  );
}
```

When the component unmounts, the keep-awake state is disabled.

### Manual control

```tsx
import KeepAwake from 'react-native-keep-awake';

await KeepAwake.activate();

await KeepAwake.deactivate();
```

`activate()` accepts an optional timeout in milliseconds for the Android `WakeLock` used by the module. The default is `-1`, which does not acquire a timed partial wake lock and preserves the normal screen-on behavior of the package.

```tsx
await KeepAwake.activate(30_000);
```

Both methods return a `Promise<void>`. The promise is rejected when there is no current Android Activity.

## TypeScript

Type definitions are included with the package.

## How it works

The JavaScript layer accesses the native module through `TurboModuleRegistry`. Codegen generates `NativeKCKeepAwakeSpec`, which the Android implementation extends. The package registers the implementation as a TurboModule using `BaseReactPackage`.

The Android implementation keeps the existing behavior of using window flags and, when a positive timeout is supplied, an Android partial `WakeLock`.

## Development

The native code is generated as part of the consuming React Native application's Codegen process.

To manually run Android Codegen from a React Native application using this package:

```sh
cd android
./gradlew generateCodegenArtifactsFromSchema
```

Then rebuild the application.

## License

MIT
