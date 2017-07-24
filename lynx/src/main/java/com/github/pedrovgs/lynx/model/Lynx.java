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
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.util.Log;

/**
 * Main business logic class for this project. Lynx responsibility is related to listen Logcat
 * events and notify it to the Lynx listeners transforming all the information from a plain String
 * to a Trace with all the information needed.
 *
 * Given a LynxConfig object the sample rating used to notify Lynx clients about new traces can be
 * modified on demand. LynxConfig object will be used to filter traces if any filter has been
 * previously configured. Filtering will remove traces that contains given string or that match a
 * regular expression specified as filter.
 *
 * @author Pedro Vicente Gomez Sanchez.
 */
public class Lynx {

  private static final String LOGTAG = "Lynx";

  private Logcat logcat;
  private final MainThread mainThread;
  private final TimeProvider timeProvider;
  private final List<Trace> tracesToNotify;
  private final List<Listener> listeners;

  private LynxConfig lynxConfig = new LynxConfig();
  private long lastNotificationTime;

  private String lowerCaseFilter = "";
  private Pattern regexpFilter;

  public Lynx(Logcat logcat, MainThread mainThread, TimeProvider timeProvider) {
    this.listeners = new LinkedList<>();
    this.tracesToNotify = new LinkedList<>();
    this.logcat = logcat;
    this.mainThread = mainThread;
    this.timeProvider = timeProvider;
    setFilters();
  }

  /**
   * Indicates a custom LynxConfig object.
   *
   * @param lynxConfig a custom LynxConfig object
   */
  public synchronized void setConfig(LynxConfig lynxConfig) {
    this.lynxConfig = lynxConfig;
    setFilters();
  }

  /**
   * Returns a copy of the current LynxConfig object.
   *
   * @return a copy of the current LynxConfig object
   */
  public LynxConfig getConfig() {
    return (LynxConfig) lynxConfig.clone();
  }

  /**
   * Configures a Logcat.Listener and initialize Logcat dependency to read traces from the OS log.
   */
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
    boolean logcatWasNotStarted = Thread.State.NEW.equals(logcat.getState());
    if (logcatWasNotStarted) {
      logcat.start();
    }
  }

  /**
   * Stops Logcat dependency to stop receiving logcat traces.
   */
  public void stopReading() {
    logcat.stopReading();
    logcat.interrupt();
  }

  /**
   * Stops the configured Logcat dependency and creates a clone to restart using Logcat and
   * LogcatListener configured previously.
   */
  public synchronized void restart() {
    Logcat.Listener previousListener = logcat.getListener();
    logcat.stopReading();
    logcat.interrupt();
    logcat = (Logcat) logcat.clone();
    logcat.setListener(previousListener);
    lastNotificationTime = 0;
    tracesToNotify.clear();
    logcat.start();
  }

  /**
   * Adds a Listener to the listeners collection to be notified with new Trace objects.
   *
   * @param lynxPresenter a lynx listener
   */
  public synchronized void registerListener(Listener lynxPresenter) {
    listeners.add(lynxPresenter);
  }

  /**
   * Removes a Listener to the listeners collection.
   *
   * @param lynxPresenter a lynx listener
   */
  public synchronized void unregisterListener(Listener lynxPresenter) {
    listeners.remove(lynxPresenter);
  }

  private void setFilters() {
    lowerCaseFilter = lynxConfig.getFilter().toLowerCase();
    try {
      regexpFilter = Pattern.compile(lowerCaseFilter);
    } catch (PatternSyntaxException exception) {
      regexpFilter = null;
      Log.d(LOGTAG, "Invalid regexp filter!");
    }
  }

  private synchronized void addTraceToTheBuffer(String logcatTrace) throws IllegalTraceException {
    if (shouldAddTrace(logcatTrace)) {
      Trace trace = Trace.fromString(logcatTrace);
      tracesToNotify.add(trace);
    }
  }

  private boolean shouldAddTrace(String logcatTrace) {
    boolean hasFilterConfigured = lynxConfig.hasFilter();
    return !hasFilterConfigured || traceMatchesFilter(logcatTrace);
  }

  private synchronized boolean traceMatchesFilter(String logcatTrace) {
    return traceStringMatchesFilter(logcatTrace)
            && containsTraceLevel(logcatTrace, lynxConfig.getFilterTraceLevel());
  }

  private boolean traceStringMatchesFilter(String logcatTrace) {
    String lowerCaseLogcatTrace = logcatTrace.toLowerCase();
    boolean matchesFilter = lowerCaseLogcatTrace.contains(lowerCaseFilter);
    if (!matchesFilter && regexpFilter != null) {
      matchesFilter = regexpFilter.matcher(lowerCaseLogcatTrace).find();
    }
    return matchesFilter;
  }

  private boolean containsTraceLevel(String logcatTrace, TraceLevel levelFilter) {
    return levelFilter.equals(TraceLevel.VERBOSE) || hasTraceLevelEqualOrHigher(logcatTrace,
        levelFilter);
  }

  private boolean hasTraceLevelEqualOrHigher(String logcatTrace, TraceLevel levelFilter) {
    TraceLevel level = TraceLevel.getTraceLevel(logcatTrace.charAt(Trace.TRACE_LEVEL_INDEX));
    return level.ordinal() >= levelFilter.ordinal();
  }

  private synchronized void notifyNewTraces() {
    if (shouldNotifyListeners()) {
      final List<Trace> traces = new LinkedList<>(tracesToNotify);
      tracesToNotify.clear();
      notifyListeners(traces);
    }
  }

  private synchronized boolean shouldNotifyListeners() {
    long now = timeProvider.getCurrentTimeMillis();
    long timeFromLastNotification = now - lastNotificationTime;
    boolean hasTracesToNotify = tracesToNotify.size() > 0;
    return timeFromLastNotification > lynxConfig.getSamplingRate() && hasTracesToNotify;
  }

  private synchronized void notifyListeners(final List<Trace> traces) {
    mainThread.post(new Runnable() {
      @Override public void run() {
        for (Listener listener : listeners) {
          listener.onNewTraces(traces);
        }
        lastNotificationTime = timeProvider.getCurrentTimeMillis();
      }
    });
  }

  public interface Listener {

    void onNewTraces(List<Trace> traces);
  }
}
