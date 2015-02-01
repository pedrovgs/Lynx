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
import java.util.List;

/**
 * Presenter created to decouple Lynx library view implementations from Lynx model. This presenter
 * responsibility is related to all the presentation logic to Lynx UI implementations. Lynx UI
 * implementations have to implement LynxPresenter.View interface.
 *
 * @author Pedro Vicente Gómez Sánchez.
 */
public class LynxPresenter implements Lynx.LynxListener {

  private final Lynx lynx;
  private final View view;
  private final TraceBuffer traceBuffer;

  public LynxPresenter(Lynx lynx, View view, int maxNumberOfTracesToShow) {
    validateNumberOfTracesConfiguration(maxNumberOfTracesToShow);
    this.lynx = lynx;
    this.view = view;
    this.traceBuffer = new TraceBuffer(maxNumberOfTracesToShow);
  }

  public void resume() {
    lynx.registerListener(this);
    lynx.startReading();
  }

  public void pause() {
    lynx.unregisterListener(this);
    lynx.stopReading();
  }

  @Override public void onNewTraces(List<Trace> traces) {
    updateTraceBuffer(traces);
    List<Trace> tracesToNotify = getCurrentTraces();
    view.showTraces(tracesToNotify);
  }

  private void updateTraceBuffer(List<Trace> traces) {
    traceBuffer.add(traces);
  }

  private List<Trace> getCurrentTraces() {
    return traceBuffer.getTraces();
  }

  private void validateNumberOfTracesConfiguration(long maxNumberOfTracesToShow) {
    if (maxNumberOfTracesToShow <= 0) {
      throw new IllegalArgumentException(
          "You can't pass a zero or negative number of traces to show.");
    }
  }

  public interface View {

    void showTraces(List<Trace> traces);
  }
}
