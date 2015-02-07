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
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Pedro Vicente Gómez Sánchez.
 */
public class TraceTest {

  private static final String VERBOSE_TRACE_MESSAGE = "Any dverbose message";
  private static final String VERBOSE_TRACE = "V/" + VERBOSE_TRACE_MESSAGE;
  private static final String DEBUG_TRACE_MESSAGE = "Any debug message";
  private static final String DEBUG_TRACE = "D/" + DEBUG_TRACE_MESSAGE;
  private static final String ASSERT_TRACE_MESSAGE = "Any assert message";
  private static final String ASSERT_TRACE = "A/" + ASSERT_TRACE_MESSAGE;
  private static final String INFO_TRACE_MESSAGE = "Any info message";
  private static final String INFO_TRACE = "I/" + INFO_TRACE_MESSAGE;
  private static final String WARNING_TRACE_MESSAGE = "Any warning message";
  private static final String WARNING_TRACE = "W/" + WARNING_TRACE_MESSAGE;
  private static final String ERROR_TRACE_MESSAGE = "Any error message";
  private static final String ERROR_TRACE = "E/" + ERROR_TRACE_MESSAGE;
  private static final String WTF_TRACE_MESSAGE = "Any wtf message";
  private static final String WTF_TRACE = "F/" + WTF_TRACE_MESSAGE;

  @Test(expected = IllegalTraceException.class) public void shouldThrowExceptionIfTraceInputIsNull()
      throws IllegalTraceException {
    Trace.fromString(null);
  }

  @Test(expected = IllegalTraceException.class) public void shouldThrowExceptionIfTraceHasNoLevel()
      throws IllegalTraceException {
    Trace.fromString("Any trace without trace level indicated");
  }

  @Test(expected = IllegalTraceException.class)
  public void shouldThrowExceptionIfTraceCointaisJustLevel() throws IllegalTraceException {
    Trace.fromString("D");
  }

  @Test(expected = IllegalTraceException.class)
  public void shouldThrowExceptionIfTraceHasNotTraceLevelSeparator() throws IllegalTraceException {
    Trace.fromString("D Any trace message");
  }

  @Test public void shouldCreateVerboseTraceFromStringTrace() throws IllegalTraceException {
    Trace trace = Trace.fromString(VERBOSE_TRACE);

    assertEquals(TraceLevel.VERBOSE, trace.getLevel());
    assertEquals(VERBOSE_TRACE_MESSAGE, trace.getMessage());
  }

  @Test public void shouldCreateDebugTraceFromStringTrace() throws IllegalTraceException {
    Trace trace = Trace.fromString(DEBUG_TRACE);

    assertEquals(TraceLevel.DEBUG, trace.getLevel());
    assertEquals(DEBUG_TRACE_MESSAGE, trace.getMessage());
  }

  @Test public void shouldCreateAssertTraceFromStringTrace() throws IllegalTraceException {
    Trace trace = Trace.fromString(ASSERT_TRACE);

    assertEquals(TraceLevel.ASSERT, trace.getLevel());
    assertEquals(ASSERT_TRACE_MESSAGE, trace.getMessage());
  }

  @Test public void shouldCreateInfotTraceFromStringTrace() throws IllegalTraceException {
    Trace trace = Trace.fromString(INFO_TRACE);

    assertEquals(TraceLevel.INFO, trace.getLevel());
    assertEquals(INFO_TRACE_MESSAGE, trace.getMessage());
  }

  @Test public void shouldCreateWarningTraceFromStringTrace() throws IllegalTraceException {
    Trace trace = Trace.fromString(WARNING_TRACE);

    assertEquals(TraceLevel.WARNING, trace.getLevel());
    assertEquals(WARNING_TRACE_MESSAGE, trace.getMessage());
  }

  @Test public void shouldCreateErrorTraceFromStringTrace() throws IllegalTraceException {
    Trace trace = Trace.fromString(ERROR_TRACE);

    assertEquals(TraceLevel.ERROR, trace.getLevel());
    assertEquals(ERROR_TRACE_MESSAGE, trace.getMessage());
  }

  @Test public void shouldCreateWtfTraceFromStringTrace() throws IllegalTraceException {
    Trace trace = Trace.fromString(WTF_TRACE);

    assertEquals(TraceLevel.WTF, trace.getLevel());
    assertEquals(WTF_TRACE_MESSAGE, trace.getMessage());
  }
}
