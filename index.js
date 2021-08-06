import React, { Component } from 'react';
import { NativeModules } from 'react-native';

export default class KeepAwake extends Component {
  static activate() {
    return NativeModules.KCKeepAwake.activate();
  }

  static deactivate() {
    return NativeModules.KCKeepAwake.deactivate();
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
