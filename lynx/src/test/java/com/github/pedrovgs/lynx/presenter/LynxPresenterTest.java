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

package com.github.pedrovgs.lynx.presenter;

import com.github.pedrovgs.lynx.model.Lynx;
import com.github.pedrovgs.lynx.model.Trace;
import com.github.pedrovgs.lynx.model.TraceLevel;
import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * @author Pedro Vicente Gómez Sánchez.
 */
public class LynxPresenterTest {

  private static final int MAX_NUMBER_OF_TRACES = 10;

  @Mock private Lynx lynx;
  @Mock private LynxPresenter.View view;

  private LynxPresenter presenter;

  @Before public void setUp() {
    MockitoAnnotations.initMocks(this);
    presenter = new LynxPresenter(lynx, view, MAX_NUMBER_OF_TRACES);
  }

  @Test public void shouldRegisterItselfAsLynxListenerOnResume() {
    presenter.resume();

    verify(lynx).registerListener(presenter);
  }

  @Test public void shouldUnregisterItselfAsLynxListenerOnPause() {
    presenter.resume();

    presenter.pause();

    verify(lynx).unregisterListener(presenter);
  }

  @Test public void shouldStartLynxOnResume() {
    presenter.resume();

    verify(lynx).startReading();
  }

  @Test public void shouldStopLynxOnPause() {
    presenter.resume();

    presenter.pause();

    verify(lynx).stopReading();
  }

  @Test public void shouldShowTracesNotifiedFromLynx() {
    List<Trace> traces = generateTraces(MAX_NUMBER_OF_TRACES);

    presenter = new LynxPresenter(lynx, view, MAX_NUMBER_OF_TRACES);
    presenter.resume();
    presenter.onNewTraces(traces);

    verify(view).showTraces(eq(traces));
  }

  private List<Trace> generateTraces(int numberOfTraces) {
    List<Trace> traces = new LinkedList<Trace>();
    for (int i = 0; i < numberOfTraces; i++) {
      Trace dummyTrace = new Trace(TraceLevel.VERBOSE, String.valueOf(i));
      traces.add(dummyTrace);
    }
    return traces;
  }
}
