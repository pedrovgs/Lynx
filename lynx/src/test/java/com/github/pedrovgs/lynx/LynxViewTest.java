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

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.github.pedrovgs.lynx.model.Trace;
import com.github.pedrovgs.lynx.model.TraceLevel;
import com.github.pedrovgs.lynx.presenter.LynxPresenter;
import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * @author Pedro Vicente Gómez Sánchez.
 */
@Config(emulateSdk = 18) @RunWith(RobolectricTestRunner.class) public class LynxViewTest {

  private static final String ANY_TRACE_MESSAGE = "02-07 17:45:33.014 D/ Any trace message";
  private static final String SHARE_INTENT_TYPE = "text/plain";
  public static final int ANY_TEXT_SIZE = 300;

  private LynxView lynxView;
  private Activity activity;

  @Mock private LynxPresenter presenter;

  @Before public void setUp() {
    MockitoAnnotations.initMocks(this);
    activity = Robolectric.buildActivity(Activity.class).create().resume().get();
    lynxView = new LynxView(activity);
    lynxView.setPresenter(presenter);
  }

  @Test public void shouldShowListViewAsVisible() {
    ListView lv_traces = getLvTraces();

    assertEquals(View.VISIBLE, lv_traces.getVisibility());
  }

  @Test public void shouldShowEditTextForInputFilterAsVisible() {
    View et_filter = lynxView.findViewById(R.id.et_filter);

    assertEquals(View.VISIBLE, et_filter.getVisibility());
  }

  @Test public void shouldShowShareImageButtonAsVisible() {
    View ib_share = lynxView.findViewById(R.id.ib_share);

    assertEquals(View.VISIBLE, ib_share.getVisibility());
  }

  @Test public void shouldResumePresenterOnAttachedToWindowAndViewIsVisible() {
    lynxView.onAttachedToWindow();

    verify(presenter).resume();
  }

  @Test public void shouldNotResumePresenterOnAttachedToWindowIfViewIsNotVisible() {
    lynxView.setVisibility(View.INVISIBLE);

    lynxView.onAttachedToWindow();

    verify(presenter, never()).resume();
  }

  @Test public void shouldPausePresenterOnDetachedFromWindow() {
    lynxView.onAttachedToWindow();

    lynxView.onDetachedFromWindow();

    verify(presenter).pause();
  }

  @Test public void shouldResumePresenterOnVisibilityChangedToVisible() {
    lynxView.onAttachedToWindow();

    lynxView.setVisibility(View.VISIBLE);

    verify(presenter).resume();
  }

  @Test public void shouldPausePresenterOnVisibilityChangedToNoVisible() {
    lynxView.onAttachedToWindow();

    lynxView.setVisibility(View.GONE);

    verify(presenter).pause();
  }

  @Test public void shouldUseDefaultLynxConfigIfThereIsNoStyleableAttributesConfigured() {
    LynxConfig lynxConfig = lynxView.getLynxConfig();

    LynxConfig defaultLynxConfig = new LynxConfig();
    assertEquals(defaultLynxConfig, lynxConfig);
  }

  @Test public void shouldShowTracesPassedAsParameter() {
    int numberOfTracesToShow = 10;
    List<Trace> traces = givenAnyListOfTraces(numberOfTracesToShow);

    lynxView.showTraces(traces, 0);

    ListView lv_traces = getLvTraces();
    int tracesCount = lv_traces.getAdapter().getCount();
    assertEquals(numberOfTracesToShow, tracesCount);
    assertTracesRendered(traces, lv_traces);
  }

  @Test public void shouldReplaceOldTracesAndUseTheNewOne() {
    List<Trace> traces = givenAnyListOfTraces(10);
    List<Trace> newTraces = givenAnyListOfTraces(20);

    lynxView.showTraces(traces, 0);
    lynxView.showTraces(newTraces, 0);

    ListView lv_traces = getLvTraces();
    int tracesCount = lv_traces.getAdapter().getCount();
    assertEquals(20, tracesCount);
    assertTracesRendered(newTraces, lv_traces);
  }

  @Test public void shouldResetListViewStateOnClear() {
    List<Trace> traces = givenAnyListOfTraces(10);

    lynxView.showTraces(traces, 0);
    lynxView.clear();

    ListView lv_traces = getLvTraces();
    assertEquals(0, lv_traces.getAdapter().getCount());
  }

  @Test public void shouldApplyNewConfigJustIfIsDifferentOfTheCurrentOne() {
    LynxConfig newLynxConfig = new LynxConfig().withTextSizeInPx(ANY_TEXT_SIZE);

    lynxView.setLynxConfig(newLynxConfig);

    verify(presenter).setLynxConfig(newLynxConfig);
  }

  @Test public void shouldNotApplyNewConfigIfIsEqualsToThePreviousOne() {
    LynxConfig defaultConfig = new LynxConfig();

    lynxView.setLynxConfig(defaultConfig);

    verify(presenter, never()).setLynxConfig(defaultConfig);
  }


  private void assertTracesRendered(List<Trace> traces, ListView lv_traces) {
    for (int i = 0; i < traces.size(); i++) {
      Trace trace = traces.get(i);
      Trace renderedTrace = (Trace) lv_traces.getAdapter().getItem(i);
      assertEquals(trace, renderedTrace);
    }
  }

  private List<Trace> givenAnyListOfTraces(int tracesCount) {
    List<Trace> traces = new LinkedList<Trace>();
    for (int i = 0; i < tracesCount; i++) {
      traces.add(new Trace(TraceLevel.DEBUG, ANY_TRACE_MESSAGE));
    }
    return traces;
  }

  private ListView getLvTraces() {
    return (ListView) lynxView.findViewById(R.id.lv_traces);
  }
}
