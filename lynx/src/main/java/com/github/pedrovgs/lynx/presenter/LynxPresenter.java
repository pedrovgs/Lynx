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
import java.util.List;

/**
 * Presenter created to decouple Lynx library view implementations from Lynx model. This presenter
 * responsibility is related to all the presentation logic to Lynx UI implementations. Lynx UI
 * implementations have to implement LynxPresenter.View interface.
 *
 * @author Pedro Vicente Gómez Sánchez.
 */
public class LynxPresenter {

  private final View view;

  public LynxPresenter(View view) {
    this.view = view;
  }

  public void resume() {

  }

  public void pause() {

  }

  public interface View {

    void showTraces(List<Trace> traces);
  }
}
