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

package com.github.pedrovgs.lynx.renderer;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.github.pedrovgs.lynx.BuildConfig;
import com.github.pedrovgs.lynx.LynxConfig;
import com.github.pedrovgs.lynx.R;
import com.github.pedrovgs.lynx.model.Trace;
import com.github.pedrovgs.lynx.model.TraceLevel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

/**
 * @author Pedro Vicente Gómez Sánchez.
 */
@Config(constants = BuildConfig.class, sdk = 18)
@RunWith(RobolectricTestRunner.class)
public class TraceRendererTest {

  private static final TraceLevel ANY_TRACE_LEVEL = TraceLevel.WTF;
  private static final String ANY_TRACE_MESSAGE =
      "02-08 19:54:30.145 Lynx﹕ WTF!!! My app is not working!!";
  private static final LynxConfig ANY_LYNX_CONFIG = new LynxConfig();

  private TraceRenderer traceRenderer;

  @Before public void setUp() {
    traceRenderer = new TraceRenderer(ANY_LYNX_CONFIG);
  }

  @Test public void shouldRenderFullTracePlusTraceLevelInformation() {
    Trace anyTrace = givenAnyTrace();

    View view = renderTrace(anyTrace);
    String traceRendered = ((TextView) view.findViewById(R.id.tv_trace)).getText().toString();

    String expectedTrace = " " + anyTrace.getLevel().getValue() + "  " + anyTrace.getMessage();
    assertEquals(expectedTrace, traceRendered);
  }

  private Trace givenAnyTrace() {
    return new Trace(ANY_TRACE_LEVEL, ANY_TRACE_MESSAGE);
  }

  private View renderTrace(Trace anyTrace) {
    LayoutInflater layoutInflater = LayoutInflater.from(RuntimeEnvironment.application);
    traceRenderer.onCreate(anyTrace, layoutInflater, null);
    traceRenderer.render();
    return traceRenderer.getRootView();
  }
}
