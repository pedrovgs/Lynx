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

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.pedrovgs.lynx.LynxConfig;
import com.github.pedrovgs.lynx.R;
import com.github.pedrovgs.lynx.model.Trace;
import com.github.pedrovgs.lynx.model.TraceLevel;
import com.pedrogomez.renderers.Renderer;

/**
 * Base Renderer<Trace> used to show Trace objects inside a ListView using TraceLevel and Trace
 * message as main information to show. This Renderer<Trace> is used as the base of other
 * Renderers<Trace> and to show verbose TraceLevel traces.
 *
 * To learn more about Renderers library take a look to the repository:
 * https://github.com/pedrovgs/Renderers
 *
 * @author Pedro Vicente Gomez Sanchez.
 */
class TraceRenderer extends Renderer<Trace> {

  private final LynxConfig lynxConfig;

  private TextView tv_trace;

  TraceRenderer(LynxConfig lynxConfig) {
    this.lynxConfig = lynxConfig;
  }

  @Override protected View inflate(LayoutInflater inflater, ViewGroup parent) {
    return inflater.inflate(R.layout.trace_row, parent, false);
  }

  @Override protected void setUpView(View rootView) {
    tv_trace = (TextView) rootView.findViewById(R.id.tv_trace);
    tv_trace.setTypeface(Typeface.MONOSPACE);
    if (lynxConfig.hasTextSizeInPx()) {
      float textSize = lynxConfig.getTextSizeInPx();
      tv_trace.setTextSize(textSize);
    }
  }

  @Override protected void hookListeners(View rootView) {
    //Empty
  }

  @Override public void render() {
    Trace trace = getContent();
    String traceMessage = trace.getMessage();
    Spannable traceRepresentation = getTraceVisualRepresentation(trace.getLevel(), traceMessage);
    tv_trace.setText(traceRepresentation);
  }

  protected int getTraceColor() {
    return Color.GRAY;
  }

  private Spannable getTraceVisualRepresentation(TraceLevel level, String traceMessage) {
    traceMessage = " " + level.getValue() + "  " + traceMessage;
    Spannable traceRepresentation = new SpannableString(traceMessage);
    int traceColor = getTraceColor();
    traceRepresentation.setSpan(new BackgroundColorSpan(traceColor), 0, 3,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    return traceRepresentation;
  }
}
