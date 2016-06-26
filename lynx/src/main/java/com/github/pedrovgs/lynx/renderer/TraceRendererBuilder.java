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
import com.pedrogomez.renderers.Renderer;
import com.pedrogomez.renderers.RendererBuilder;
import java.util.LinkedList;
import java.util.List;

/**
 * Renderer builder implementation created to return {@code Renderer<Trace>} instances based on the
 * trace level. This builder will use six different {@code Renderer<Trace>} implementations, one for
 * each TraceLevel type.
 *
 * To learn more about Renderers library take a look to the repository:
 * https://github.com/pedrovgs/Renderers
 *
 * @author Pedro Vicente Gomez Sanchez.
 */
public class TraceRendererBuilder extends RendererBuilder<Trace> {

  public TraceRendererBuilder(LynxConfig lynxConfig) {
    List<Renderer<Trace>> prototypes = new LinkedList<Renderer<Trace>>();
    prototypes.add(new TraceRenderer(lynxConfig));
    prototypes.add(new AssertTraceRenderer(lynxConfig));
    prototypes.add(new DebugTraceRenderer(lynxConfig));
    prototypes.add(new InfoTraceRenderer(lynxConfig));
    prototypes.add(new WarningTraceRenderer(lynxConfig));
    prototypes.add(new ErrorTraceRenderer(lynxConfig));
    prototypes.add(new WtfTraceRenderer(lynxConfig));
    setPrototypes(prototypes);
  }

  @Override protected Class getPrototypeClass(Trace content) {
    Class rendererClass = null;
    TraceLevel traceLevel = content.getLevel();
    switch (traceLevel) {
      case ASSERT:
        rendererClass = AssertTraceRenderer.class;
        break;
      case DEBUG:
        rendererClass = DebugTraceRenderer.class;
        break;
      case INFO:
        rendererClass = InfoTraceRenderer.class;
        break;
      case WARNING:
        rendererClass = WarningTraceRenderer.class;
        break;
      case ERROR:
        rendererClass = ErrorTraceRenderer.class;
        break;
      case WTF:
        rendererClass = WtfTraceRenderer.class;
        break;
      default:
        rendererClass = TraceRenderer.class;
    }
    return rendererClass;
  }
}
