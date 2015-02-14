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
import com.github.pedrovgs.lynx.LynxConfig;

/**
 * TraceRenderer implementation used to render Trace objects with TraceLevel.INFO trace level.
 *
 * To learn more about Renderers library take a look to the repository:
 * https://github.com/pedrovgs/Renderers
 *
 * @author Pedro Vicente Gomez Sanchez.
 */
class InfoTraceRenderer extends TraceRenderer {

  InfoTraceRenderer(LynxConfig lynxConfig) {
    super(lynxConfig);
  }

  @Override protected int getTraceColor() {
    return Color.rgb(255, 215, 0);
  }
}
