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

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Logcat abstraction created to be able to read from the device log output. This implementation is
 * based on a BufferReader connected to the process InputStream you can obtain executing a command
 * using Android Runtime object.
 *
 * This class will notify listeners configured previously about new traces sent to the device and
 * will be reading and notifying traces until stopReading() method be invoked.
 *
 * To be able to read from a process InputStream without block the thread where we were, this class
 * extends from Thread and all the code inside the run() method will be executed in a background
 * thread.
 *
 * @author Pedro Vicente Gómez Sánchez.
 */
public class Logcat extends Thread implements Cloneable {
  private static final String LOGTAG = "Logcat";

  private Process process;
  private BufferedReader bufferReader;
  private Listener listener;
  private boolean continueReading = true;

  /**
   * Configures a listener to be notified with new traces read from the application logcat.
   */
  public void setListener(Listener listener) {
    this.listener = listener;
  }

  /**
   * Obtains the current Logcat listener.
   */
  public Listener getListener() {
    return listener;
  }

  /**
   * Starts reading traces from the application logcat and notifying listeners if needed.
   */
  @Override public void run() {
    super.run();
    try {
      process = Runtime.getRuntime().exec("logcat -v time");
    } catch (IOException e) {
      Log.e(LOGTAG, "IOException executing logcat command.", e);
    }
    readLogcat();
  }

  /**
   * Stops reading from the application logcat and notifying listeners.
   */
  public void stopReading() {
    continueReading = false;
  }

  private void readLogcat() {
    BufferedReader bufferedReader = getBufferReader();
    try {
      String trace = bufferedReader.readLine();
      while (trace != null && continueReading) {
        notifyListener(trace);
        trace = bufferedReader.readLine();
      }
    } catch (IOException e) {
      Log.e(LOGTAG, "IOException reading logcat trace.", e);
    }
  }

  private void notifyListener(String trace) {
    if (listener != null) {
      listener.onTraceRead(trace);
    }
  }

  private BufferedReader getBufferReader() {
    if (bufferReader == null) {
      bufferReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    }
    return bufferReader;
  }

  @Override public Object clone() {
    return new Logcat();
  }

  interface Listener {

    void onTraceRead(String logcatTrace);
  }
}
