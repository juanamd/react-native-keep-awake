declare module "react-native-keep-awake" {
	export default class {
		static activate(wakelockMs?: number): Promise<void>;
		static deactivate(): Promise<void>;
	}
}
