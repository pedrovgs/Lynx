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

package com.github.pedrovgs.lynx;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Main library view. This ViewGroup extension shows all the information
 *
 * @author Pedro Vicente Gómez Sánchez.
 */
public class LynxView extends RelativeLayout {

  private ListView lv_traces;

  public LynxView(Context context) {
    super(context);
    initializeLynxView(null);
  }

  public LynxView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initializeLynxView(attrs);
  }

  public LynxView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initializeLynxView(attrs);
  }

  private void initializeLynxView(AttributeSet attrs) {
    if (attrs == null) {
      initializeConfiguration(attrs);
    }
    inflateView();
  }

  private void initializeConfiguration(AttributeSet attrs) {
    //Obtain configuration
  }

  private void inflateView() {
    Context context = getContext();
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    layoutInflater.inflate(R.layout.lynx_view, this);
    mapGui();
    hookListeners();
  }

  private void mapGui() {
    lv_traces = (ListView) findViewById(R.id.lv_traces);
  }

  private void hookListeners() {
    lv_traces.setOnScrollListener(new AbsListView.OnScrollListener() {
      @Override public void onScrollStateChanged(AbsListView view, int scrollState) {

      }

      @Override public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
          int totalItemCount) {

      }
    });
  }
}
