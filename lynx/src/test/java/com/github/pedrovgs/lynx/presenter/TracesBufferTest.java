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

import com.github.pedrovgs.lynx.model.Trace;
import com.github.pedrovgs.lynx.model.TraceLevel;
import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * @author Pedro Vicente Gómez Sánchez.
 */
public class TracesBufferTest {

  private static final int ANY_BUFFER_SIZE = 20;

  private TraceBuffer traceBuffer;

  @Before public void setUp() {
    this.traceBuffer = new TraceBuffer(ANY_BUFFER_SIZE);
  }

  @Test public void shouldReturnAnEmptyListByDefault() {
    List<Trace> traces = traceBuffer.getTraces();

    assertTrue(traces.isEmpty());
  }

  @Test public void shouldAddTracesIfTheNumberOfTracesIsLowerThanTheBufferSize() {
    List<Trace> traces = generateTraces(ANY_BUFFER_SIZE - 1);

    traceBuffer.add(traces);

    assertEquals(traces, traceBuffer.getTraces());
  }

  @Test public void shouldAddAllTracesIfTheSizeOfTheListIsEqualsToTheBufferSize() {
    List<Trace> traces = generateTraces(ANY_BUFFER_SIZE);

    traceBuffer.add(traces);

    assertEquals(traces, traceBuffer.getTraces());
  }

  @Test
  public void shouldAddAllTracesIfAddTwoListsAndTheSumOfSizesIsEqualsOrLowerThanTheBuffersize() {
    List<Trace> firstTraces = generateTraces(ANY_BUFFER_SIZE / 2);
    List<Trace> secondTraces = generateTraces(ANY_BUFFER_SIZE / 2);

    traceBuffer.add(firstTraces);
    traceBuffer.add(secondTraces);

    List<Trace> expectedTraces = new LinkedList<Trace>(firstTraces);
    expectedTraces.addAll(secondTraces);
    assertEquals(expectedTraces, traceBuffer.getTraces());
  }

  @Test public void shouldAddJustTheLastTracesIfTheSizeOfTheInputListIsBiggerThanTheBufferSize() {
    TraceBuffer traceBuffer = new TraceBuffer(20);
    List<Trace> traces = generateTraces(30);

    traceBuffer.add(traces);

    List<Trace> expectedTraces = generateTraces(10, 30);
    assertEquals(expectedTraces, traceBuffer.getTraces());
  }

  @Test public void shouldKeepTracesIfNewBufferSizeIsBiggerThanThePreviousOne() {
    List<Trace> traces = generateTraces(20);

    traceBuffer.add(traces);
    int newBufferSize = ANY_BUFFER_SIZE * 2;
    traceBuffer.setBufferSize(newBufferSize);

    assertEquals(traces, traceBuffer.getTraces());
  }

  @Test public void shouldDiscardExceededTracesOnBufferSizedChangedToAMinValue() {
    List<Trace> traces = generateTraces(20);

    traceBuffer.add(traces);
    int newBufferSize = ANY_BUFFER_SIZE / 2;
    traceBuffer.setBufferSize(newBufferSize);

    List<Trace> expectedTraces = generateTraces(10, 20);
    assertEquals(newBufferSize, traceBuffer.getTraces().size());
    assertEquals(expectedTraces, traceBuffer.getTraces());
  }

  @Test public void shouldReturnZeroIfThereAreNoRemovedTracesOnAdd() {
    List<Trace> traces = generateTraces(5);

    int removedTraces = traceBuffer.add(traces);

    assertEquals(0, removedTraces);
  }

  @Test public void shouldReturnNumberOfTracesRemovedOnAdd() {
    TraceBuffer traceBuffer = new TraceBuffer(20);
    List<Trace> traces = generateTraces(20);

    traceBuffer.add(traces);
    traces = generateTraces(20);
    int removedTraces = traceBuffer.add(traces);

    assertEquals(20, removedTraces);
  }

  @Test public void shouldRemoveAllTracesOnClear() {
    List<Trace> traces = generateTraces(20);

    traceBuffer.add(traces);
    traceBuffer.clear();

    assertEquals(0, traceBuffer.getCurrentNumberOfTraces());
  }

  private List<Trace> generateTraces(int numberOfTraces) {
    return generateTraces(0, numberOfTraces);
  }

  private List<Trace> generateTraces(int initialValue, int finalValue) {
    List<Trace> traces = new LinkedList<Trace>();
    for (int i = initialValue; i < finalValue; i++) {
      Trace generatedTrace = new Trace(TraceLevel.DEBUG, String.valueOf(i));
      traces.add(generatedTrace);
    }
    return traces;
  }
}
