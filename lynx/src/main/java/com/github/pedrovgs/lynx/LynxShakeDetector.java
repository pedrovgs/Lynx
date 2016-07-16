/*
 * Copyright (C) 2015 Pedro Vicente Gomez Sanchez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.pedrovgs.lynx;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import com.squareup.seismic.ShakeDetector;

/**
 * Identifies when the user has shaken the device to open LynxActivity. This sake detector is a
 * wrapper of Square Seismic library: https://github.com/square/seismic
 *
 * @author Pedro Vicente Gomez Sanchez.
 */
public class LynxShakeDetector {

  private final Context context;

  private static boolean isEnabled = true;

  public LynxShakeDetector(Context context) {
    this.context = context;
  }

  /**
   * Starts listening shakes to open LynxActivity if a shake is detected and if the ShakeDetector is
   * enabled, with a default Lynx configuration. It's a shortcut for {@link #init(LynxConfig) with a null value.}
   */
  public void init() {
    init(null);
  }

  /**
   * Starts listening shakes to open LynxActivity if a shake is detected and if the ShakeDetector is
   * enabled.
   */
  public void init(final LynxConfig lynxConfig) {
    ShakeDetector shakeDetector = new ShakeDetector(new ShakeDetector.Listener() {
      @Override public void hearShake() {
        if (isEnabled) {
          openLynxActivity(lynxConfig);
        }
      }
    });
    SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    shakeDetector.start(sensorManager);
  }

  /**
   * Enables shake detector to open LynxActivity on shake.
   */
  static void enable() {
    isEnabled = true;
  }

  /**
   * Disables shake detector to open LynxActivity on shake.
   */
  static void disable() {
    isEnabled = false;
  }

  private void openLynxActivity(LynxConfig config) {
    Intent lynxActivityIntent = LynxActivity.getIntent(context, config);
    lynxActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(lynxActivityIntent);
  }
}
