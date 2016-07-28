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

package com.github.pedrovgs.sample;

import android.app.Application;
import com.github.pedrovgs.lynx.LynxConfig;
import com.github.pedrovgs.lynx.LynxShakeDetector;

/**
 * Application extension created to show how to initialize LynxShakeDetector to start LynxActivity
 * on when the user shakes the device.
 *
 * @author Pedro Vicente Gomez Sanchez.
 */
public class LynxApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    LynxShakeDetector lynxShakeDetector = new LynxShakeDetector(this);
    LynxConfig lynxConfig = new LynxConfig();
    lynxConfig.setMaxNumberOfTracesToShow(4000).setFilter("LynxFilter");
    lynxShakeDetector.init(lynxConfig);
  }
}
