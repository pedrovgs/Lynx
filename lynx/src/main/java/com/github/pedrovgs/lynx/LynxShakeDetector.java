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
 * Shake detector wrapper based on Square Seismic library: https://github.com/square/seismic
 *
 * @author Pedro Vicente Gómez Sánchez.
 */
public class LynxShakeDetector {

  private final Context context;

  private static boolean isEnabled = true;

  public LynxShakeDetector(Context context) {
    this.context = context;
  }

  public void init() {
    ShakeDetector shakeDetector = new ShakeDetector(new ShakeDetector.Listener() {
      @Override public void hearShake() {
        if (isEnabled) {
          openLynxActivity();
        }
      }
    });
    SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    shakeDetector.start(sensorManager);
  }

  private void openLynxActivity() {
    Intent lynxActivityIntent = new Intent(context, LynxActivity.class);
    lynxActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(lynxActivityIntent);
  }

  static void enable() {
    isEnabled = true;
  }

  static void disable() {
    isEnabled = false;
  }
}
