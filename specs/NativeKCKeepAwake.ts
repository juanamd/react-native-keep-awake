import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  activate(wakelockMs: number): Promise<void>;
  deactivate(): Promise<void>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('KCKeepAwake');
