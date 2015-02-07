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

import com.github.pedrovgs.lynx.LynxConfig;
import com.github.pedrovgs.lynx.exception.IllegalTraceException;
import java.util.LinkedList;
import java.util.List;

/**
 * Main business logic class for this project. Lynx responsibility is related to listen Logcat
 * events and notify it to the Lynx listeners transforming all the information from a plain String
 * to a Trace with all the information needed.
 *
 * @author Pedro Vicente Gómez Sánchez.
 */
public class Lynx {

  private static final int MIN_NOTIFICATION_TIME_FREQUENCY = 10;

  private final Logcat logcat;
  private final MainThread mainThread;
  private final TimeProvider timeProvider;

  private final List<Listener> listeners;
  private final List<Trace> tracesToNotify;
  private long lastNotificationTime;
  private LynxConfig lynxConfig = new LynxConfig();

  public Lynx(Logcat logcat, MainThread mainThread, TimeProvider timeProvider) {
    this.listeners = new LinkedList<Listener>();
    this.tracesToNotify = new LinkedList<Trace>();
    this.logcat = logcat;
    this.mainThread = mainThread;
    this.timeProvider = timeProvider;
  }

  public void setConfig(LynxConfig lynxConfig) {
    this.lynxConfig = new LynxConfig();
  }

  public void startReading() {
    logcat.setListener(new Logcat.Listener() {
      @Override public void onTraceRead(String logcatTrace) {
        try {
          addTraceToTheBuffer(logcatTrace);
        } catch (IllegalTraceException e) {
          return;
        }
        notifyNewTraces();
      }
    });
    logcat.start();
  }

  public void stopReading() {
    logcat.stopReading();
  }

  public void registerListener(Listener lynxPresenter) {
    listeners.add(lynxPresenter);
  }

  public void unregisterListener(Listener lynxPresenter) {
    listeners.remove(lynxPresenter);
  }

  private void addTraceToTheBuffer(String logcatTrace) throws IllegalTraceException {
    Trace trace = Trace.fromString(logcatTrace);
    tracesToNotify.add(trace);
  }

  private void notifyNewTraces() {
    if (shouldNotifyListeners()) {
      final List<Trace> traces = new LinkedList<Trace>(tracesToNotify);
      tracesToNotify.clear();
      notifyListeners(traces);
    }
  }

  private boolean shouldNotifyListeners() {
    long now = timeProvider.getCurrentTimeMillis();
    long timeFromLastNotification = now - lastNotificationTime;
    return timeFromLastNotification > MIN_NOTIFICATION_TIME_FREQUENCY;
  }

  private void notifyListeners(final List<Trace> traces) {
    mainThread.post(new Runnable() {
      @Override public void run() {
        for (Listener listener : listeners) {
          listener.onNewTraces(traces);
        }
      }
    });
    lastNotificationTime = timeProvider.getCurrentTimeMillis();
  }

  public interface Listener {

    void onNewTraces(List<Trace> traces);
  }
}
