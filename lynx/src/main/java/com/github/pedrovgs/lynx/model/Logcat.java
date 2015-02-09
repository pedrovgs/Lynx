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
 * Logcat abstraction created to be able to read from the device log output.
 *
 * @author Pedro Vicente Gómez Sánchez.
 */
public class Logcat extends Thread implements Cloneable{
  private static final String LOGTAG = "Logcat";

  private Process process;
  private BufferedReader bufferReader;
  private Listener listener;
  private boolean continueReading = true;

  public void setListener(Listener listener) {
    this.listener = listener;
  }

  public Listener getListener() {
    return listener;
  }

  @Override public void run() {
    super.run();
    try {
      process = Runtime.getRuntime().exec("logcat -v time");
    } catch (IOException e) {
      Log.e(LOGTAG, "IOException executing logcat command.", e);
    }
    readLogcat();
  }

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
