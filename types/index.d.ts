import { Component } from 'react';

declare class KeepAwake extends Component {
  static activate(wakelockMs?: number): Promise<void>;
  static deactivate(): Promise<void>;
}

export default KeepAwake;
