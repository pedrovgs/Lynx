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
import java.util.LinkedList;
import java.util.List;

/**
 * Buffer created to keep a max number of traces and be able to configure the size of the buffer.
 *
 * @author Pedro Vicente Gomez Sanchez.
 */
class TraceBuffer {

  private int bufferSize;
  private final List<Trace> traces;

  TraceBuffer(int bufferSize) {
    this.bufferSize = bufferSize;
    traces = new LinkedList<Trace>();
  }

  /**
   * Configures the max number of traces to keep inside the buffer
   */
  void setBufferSize(int bufferSize) {
    this.bufferSize = bufferSize;
    removeExceededTracesIfNeeded();
  }

  /**
   * Adds a list of traces to the buffer, if the buffer is full your new traces will be added and
   * the previous one will be removed.
   */
  int add(List<Trace> traces) {
    this.traces.addAll(traces);
    return removeExceededTracesIfNeeded();
  }

  /**
   * Returns the current list of traces stored in the buffer.
   */
  List<Trace> getTraces() {
    return traces;
  }

  /**
   * Returns the number of traes stored in the buffer.
   */
  public int getCurrentNumberOfTraces() {
    return traces.size();
  }

  /**
   * Removes traces stored in the buffer.
   */
  public void clear() {
    traces.clear();
  }

  private int removeExceededTracesIfNeeded() {
    int tracesToDiscard = getNumberOfTracesToDiscard();
    if (tracesToDiscard > 0) {
      discardTraces(tracesToDiscard);
    }
    return tracesToDiscard;
  }

  private int getNumberOfTracesToDiscard() {
    int currentTracesSize = this.traces.size();
    int tracesToDiscard = currentTracesSize - bufferSize;
    tracesToDiscard = tracesToDiscard < 0 ? 0 : tracesToDiscard;
    return tracesToDiscard;
  }

  private void discardTraces(int tracesToDiscard) {
    for (int i = 0; i < tracesToDiscard; i++) {
      traces.remove(0);
    }
  }
}
