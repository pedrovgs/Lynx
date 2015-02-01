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
import android.view.ViewGroup;
import com.github.pedrovgs.lynx.model.Trace;
import com.pedrogomez.renderers.Renderer;

/**
 * Base Renderer<Trace> used to show Trace objects inside a ListView using TraceLevel and Trace
 * message as main information to show. This Renderer<Trace> is used as the base of other
 * Renderers<Trace> and to show verbose TraceLevel traces.
 *
 * To learn more about Renderers library take a look to the repository:
 * https://github.com/pedrovgs/Renderers
 *
 * @author Pedro Vicente Gómez Sánchez.
 */
class TraceRenderer extends Renderer<Trace> {

  @Override protected View inflate(LayoutInflater inflater, ViewGroup parent) {
    return null;
  }

  @Override protected void setUpView(View rootView) {

  }

  @Override protected void hookListeners(View rootView) {

  }

  @Override public void render() {

  }
}
