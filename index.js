import React, { Component } from 'react';

import NativeKCKeepAwake from './specs/NativeKCKeepAwake';

export default class KeepAwake extends Component {
  static activate(wakelockMs = -1) {
    return NativeKCKeepAwake.activate(wakelockMs);
  }

  static deactivate() {
    return NativeKCKeepAwake.deactivate();
  }

  componentDidMount() {
    KeepAwake.activate();
  }

  componentWillUnmount() {
    KeepAwake.deactivate();
  }

  render() {
    return null;
  }
}
