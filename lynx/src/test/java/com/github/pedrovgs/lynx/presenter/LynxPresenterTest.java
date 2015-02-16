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

import com.github.pedrovgs.lynx.LynxConfig;
import com.github.pedrovgs.lynx.model.Lynx;
import com.github.pedrovgs.lynx.model.Trace;
import com.github.pedrovgs.lynx.model.TraceLevel;
import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Pedro Vicente Gómez Sánchez.
 */
public class LynxPresenterTest {

  private static final int MAX_NUMBER_OF_TRACES = 10;
  private static final String ANY_FILTER = "filter";

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

    verify(view).showTraces(eq(traces), eq(0));
  }

  @Test(expected = IllegalArgumentException.class) public void shouldNotAcceptNullLynxConfigs() {
    presenter.setLynxConfig(null);
  }

  @Test public void shouldUpdateLynxConfigOnSetLynxConfig() {
    LynxConfig lynxConfig = new LynxConfig();

    presenter.setLynxConfig(lynxConfig);

    verify(lynx).setConfig(lynxConfig);
  }

  @Test
  public void shouldDisableAutoScrollOnScrollCalledWithLastVisiblePositionDifferentFromThreeLastPositions() {
    List<Trace> traces = generateTraces(10);

    presenter.resume();
    presenter.onNewTraces(traces);
    presenter.onScrollToPosition(5);

    verify(view).disableAutoScroll();
  }

  @Test public void shouldEnableAutoScrollOnScrollCalledWithOneOfTheLastThreePositions() {
    List<Trace> traces = generateTraces(10);

    presenter.resume();
    presenter.onNewTraces(traces);
    presenter.onScrollToPosition(9);

    verify(view).enableAutoScroll();
  }

  @Test public void shouldApplyNewConfigToLynxOnFilterUpdated() {
    givenAPreviusLynxConfig();
    ArgumentCaptor<LynxConfig> lynxConfigArgumentCaptor = ArgumentCaptor.forClass(LynxConfig.class);

    presenter.resume();
    presenter.updateFilter(ANY_FILTER);

    verify(lynx).setConfig(lynxConfigArgumentCaptor.capture());
    LynxConfig lynxConfig = lynxConfigArgumentCaptor.getValue();
    assertEquals(ANY_FILTER, lynxConfig.getFilter());
    assertEquals(TraceLevel.VERBOSE, lynxConfig.getFilterTraceLevel());
  }

  @Test public void shouldApplyNewConfigToLynxOnFilterTraceLevelUpdated() {
    givenAPreviusLynxConfig();
    ArgumentCaptor<LynxConfig> lynxConfigArgumentCaptor = ArgumentCaptor.forClass(LynxConfig.class);

    presenter.resume();
    presenter.updateFilterTraceLevel(TraceLevel.DEBUG);

    verify(lynx).setConfig(lynxConfigArgumentCaptor.capture());
    LynxConfig lynxConfig = lynxConfigArgumentCaptor.getValue();
    assertEquals("", lynxConfig.getFilter());
    assertEquals(TraceLevel.DEBUG, lynxConfig.getFilterTraceLevel());
  }

  @Test public void shouldClearViewOnFilterUpdated() {
    givenAPreviusLynxConfig();

    presenter.resume();
    presenter.updateFilter(ANY_FILTER);

    verify(view).clear();
  }

  @Test public void shouldClearViewOnFilterTraceLevelUpdated() {
    givenAPreviusLynxConfig();

    presenter.resume();
    presenter.updateFilterTraceLevel(TraceLevel.DEBUG);

    verify(view).clear();
  }

  @Test public void shouldShowNewTracesAfterOnFilterUpdated() {
    givenAPreviusLynxConfig();
    List<Trace> traces = generateTraces(3);

    presenter.resume();
    presenter.onNewTraces(traces);
    presenter.updateFilter(ANY_FILTER);
    List<Trace> newTraces = generateTraces(5);
    presenter.onNewTraces(newTraces);

    verify(view, times(2)).showTraces(newTraces, 0);
  }

  @Test public void shouldShowNewTracesAfterOnFilterTraceLevelUpdated() {
    givenAPreviusLynxConfig();
    List<Trace> traces = generateTraces(3);

    presenter.resume();
    presenter.onNewTraces(traces);
    presenter.updateFilterTraceLevel(TraceLevel.DEBUG);
    List<Trace> newTraces = generateTraces(5);
    presenter.onNewTraces(newTraces);

    verify(view, times(2)).showTraces(newTraces, 0);
  }

  @Test public void shouldShareAPlainRepresentationOfTheCurrentBufferTraces() {
    List<Trace> traces = generateTraces(30);

    presenter.resume();
    presenter.onNewTraces(traces);
    presenter.onShareButtonClicked();

    traces = removeFirstTraces(20, traces);
    String expectedTraces = generatePlainTracesToShare(traces);
    verify(view).shareTraces(expectedTraces);
  }

  @Test public void shouldNotStartReadingFromLynxIfPresenterIsAlreadyInitialized() {
    presenter.resume();

    presenter.resume();

    verify(lynx).startReading();
    verify(lynx).registerListener(presenter);
  }

  @Test public void shouldStopLynxJustItWasInitializedBefore() {
    presenter.pause();

    verify(lynx, never()).stopReading();
    verify(lynx, never()).unregisterListener(presenter);
  }

  @Test public void shouldNotUpdateFilterIfPresenterIsNotInitialized() {
    presenter.updateFilter(ANY_FILTER);

    verify(lynx, never()).setConfig(any(LynxConfig.class));
    verify(lynx, never()).restart();
  }

  @Test public void shouldReturnCurrentTraces() {
    List<Trace> traces = generateTraces(5);

    presenter.resume();
    presenter.onNewTraces(traces);

    assertEquals(traces, presenter.getCurrentTraces());
  }

  private List<Trace> removeFirstTraces(int tracesToRemove, List<Trace> traces) {
    for (int i = 0; i < tracesToRemove; i++) {
      traces.remove(0);
    }
    return traces;
  }

  private String generatePlainTracesToShare(List<Trace> traces) {
    StringBuilder sb = new StringBuilder();
    for (Trace trace : traces) {
      String traceLevel = trace.getLevel().getValue();
      String traceMessage = trace.getMessage();
      sb.append(traceLevel);
      sb.append("/ ");
      sb.append(traceMessage);
      sb.append("\n");
    }
    return sb.toString();
  }

  private void givenAPreviusLynxConfig() {
    when(lynx.getConfig()).thenReturn(new LynxConfig());
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
