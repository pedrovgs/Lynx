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

package com.github.pedrovgs.lynx.model;

import android.os.Handler;
import android.os.Looper;

/**
 * MainThread implementation based on Android Handler and Looper classes. This class is used to
 * post Runnable objects over the UI.
 *
 * @author Pedro Vicente Gomez Sanchez.
 */
public class AndroidMainThread implements MainThread {

  private final Handler handler;

  public AndroidMainThread() {
    handler = new Handler(Looper.getMainLooper());
  }

  public void post(Runnable runnable) {
    handler.post(runnable);
  }
}
