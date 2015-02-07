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

import com.github.pedrovgs.lynx.exception.IllegalTraceException;
import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Pedro Vicente Gómez Sánchez.
 */
public class LynxTest {

  private static final long NOW = 100;
  private static final String ANY_DEBUG_TRACE = "D/Any debug trace";
  private static final String ANY_ERROR_TRACE = "E/Any error trace";
  private static final String ANY_WTF_TRACE = "F/Any WTF trace";

  private Lynx lynx;

  @Mock private Lynx.Listener listener;
  @Mock private Logcat logcat;
  @Mock private TimeProvider timeProvider;

  @Before public void setUp() {
    MockitoAnnotations.initMocks(this);
    MainThread mainThread = new FakeMainThread();
    lynx = new Lynx(logcat, mainThread, timeProvider);
    lynx.registerListener(listener);
  }

  @Test public void shouldRegisterListenerOnStart() {
    lynx.startReading();

    verify(logcat).setListener(any(Logcat.Listener.class));
  }

  @Test public void shouldStartLogcatOnStart() {
    lynx.startReading();

    verify(logcat).start();
  }

  @Test public void shouldStopLogcatOnStop() {
    lynx.startReading();

    lynx.stopReading();

    verify(logcat).stopReading();
  }

  @Test public void shouldNotifyFirstTrace() throws IllegalTraceException {
    givenCurrentTime();

    Logcat.Listener logcatListener = startLogcat();
    logcatListener.onTraceRead(ANY_DEBUG_TRACE);

    List<Trace> expectedTraces = generateTraces(ANY_DEBUG_TRACE);
    verify(listener).onNewTraces(expectedTraces);
  }

  @Test public void shouldNotifyJustOnceIfTheSecondEventAppearsLessThan10msAfter()
      throws IllegalTraceException {
    givenCurrentTimes(NOW, NOW + 5, NOW + 15, NOW + 20);

    Logcat.Listener logcatListener = startLogcat();
    logcatListener.onTraceRead(ANY_DEBUG_TRACE);
    logcatListener.onTraceRead(ANY_ERROR_TRACE);

    List<Trace> expectedTraces = generateTraces(ANY_DEBUG_TRACE);
    verify(listener).onNewTraces(expectedTraces);
  }

  @Test public void shouldNotifyTwiceEvenIfThereAreOtherEvents10msAfter()
      throws IllegalTraceException {
    givenCurrentTimes(NOW, NOW + 5, NOW + 15, NOW + 20);

    Logcat.Listener logcatListener = startLogcat();
    logcatListener.onTraceRead(ANY_DEBUG_TRACE);
    logcatListener.onTraceRead(ANY_ERROR_TRACE);
    logcatListener.onTraceRead(ANY_WTF_TRACE);

    List<Trace> expectedTraces = generateTraces(ANY_DEBUG_TRACE);
    verify(listener).onNewTraces(expectedTraces);
    expectedTraces = generateTraces(ANY_ERROR_TRACE, ANY_WTF_TRACE);
    verify(listener).onNewTraces(expectedTraces);
  }

  private void givenCurrentTimes(long t1, long t2, long t3, long t4) {
    when(timeProvider.getCurrentTimeMillis()).thenReturn(t1, t2, t3, t4);
  }

  private List<Trace> generateTraces(String... traces) throws IllegalTraceException {
    List<Trace> listOfTraces = new LinkedList<Trace>();
    for (String trace : traces) {
      listOfTraces.add(Trace.fromString(trace));
    }
    return listOfTraces;
  }

  private Logcat.Listener startLogcat() {
    ArgumentCaptor<Logcat.Listener> listener = ArgumentCaptor.forClass(Logcat.Listener.class);
    lynx.startReading();
    verify(logcat).setListener(listener.capture());
    return listener.getValue();
  }

  private void givenCurrentTime() {
    when(timeProvider.getCurrentTimeMillis()).thenReturn(NOW);
  }
}
