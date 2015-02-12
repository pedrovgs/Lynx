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

import com.github.pedrovgs.lynx.LynxConfig;
import com.github.pedrovgs.lynx.model.Trace;
import com.github.pedrovgs.lynx.model.TraceLevel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Pedro Vicente Gómez Sánchez.
 */
public class TraceRendererBuilderTest {

  private static final String ANY_TRACE_MESSAGE = "Any log trace";
  private static final LynxConfig ANY_LYNX_CONFIG = new LynxConfig();

  private TraceRendererBuilder traceRendererBuilder;

  @Before public void setUp() {
    traceRendererBuilder = new TraceRendererBuilder(ANY_LYNX_CONFIG);
  }

  @Test public void shouldReturnAssertTraceRendererAsPrototypeIfTraceLevelEqualsToVerbose() {
    Trace trace = new Trace(TraceLevel.ASSERT, ANY_TRACE_MESSAGE);

    Class traceRendererClass = traceRendererBuilder.getPrototypeClass(trace);

    assertEquals(traceRendererClass, AssertTraceRenderer.class);
  }

  @Test public void shouldReturnTraceRendererAsPrototypeIfTraceLevelEqualsToVerbose() {
    Trace trace = new Trace(TraceLevel.VERBOSE, ANY_TRACE_MESSAGE);

    Class traceRendererClass = traceRendererBuilder.getPrototypeClass(trace);

    assertEquals(traceRendererClass, TraceRenderer.class);
  }

  @Test public void shouldReturnDebugTraceRendererAsPrototypeIfTraceLevelEqualsToVerbose() {
    Trace trace = new Trace(TraceLevel.DEBUG, ANY_TRACE_MESSAGE);

    Class traceRendererClass = traceRendererBuilder.getPrototypeClass(trace);

    assertEquals(traceRendererClass, DebugTraceRenderer.class);
  }

  @Test public void shouldReturnInfoTraceRendererAsPrototypeIfTraceLevelEqualsToVerbose() {
    Trace trace = new Trace(TraceLevel.INFO, ANY_TRACE_MESSAGE);

    Class traceRendererClass = traceRendererBuilder.getPrototypeClass(trace);

    assertEquals(traceRendererClass, InfoTraceRenderer.class);
  }

  @Test public void shouldReturnWarningTraceRendererAsPrototypeIfTraceLevelEqualsToVerbose() {
    Trace trace = new Trace(TraceLevel.WARNING, ANY_TRACE_MESSAGE);

    Class traceRendererClass = traceRendererBuilder.getPrototypeClass(trace);

    assertEquals(traceRendererClass, WarningTraceRenderer.class);
  }

  @Test public void shouldReturnErrorTraceRendererAsPrototypeIfTraceLevelEqualsToVerbose() {
    Trace trace = new Trace(TraceLevel.ERROR, ANY_TRACE_MESSAGE);

    Class traceRendererClass = traceRendererBuilder.getPrototypeClass(trace);

    assertEquals(traceRendererClass, ErrorTraceRenderer.class);
  }
}
