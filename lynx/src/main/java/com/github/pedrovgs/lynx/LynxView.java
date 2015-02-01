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
import com.github.pedrovgs.lynx.model.Lynx;
import com.github.pedrovgs.lynx.model.Trace;
import com.github.pedrovgs.lynx.presenter.LynxPresenter;
import com.github.pedrovgs.lynx.renderer.TraceRendererBuilder;
import com.pedrogomez.renderers.ListAdapteeCollection;
import com.pedrogomez.renderers.RendererAdapter;
import com.pedrogomez.renderers.RendererBuilder;
import java.util.List;

/**
 * Main library view. This RelativeLayout extension shows all the information printed in your
 * device log. Add this view to your layouts if you want to show your Logcat traces and configure
 * it using styleable attributes.
 *
 * @author Pedro Vicente Gómez Sánchez.
 */
public class LynxView extends RelativeLayout implements LynxPresenter.View {

  private LynxPresenter presenter;
  private LynxConfig lynxConfig;

  private ListView lv_traces;
  private RendererAdapter<Trace> adapter;

  public LynxView(Context context) {
    this(context, null);
  }

  public LynxView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public LynxView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    if (attrs != null) {
      initializeConfiguration(attrs);
    }
    lynxConfig = new LynxConfig();
    inflateView();
    initializePresenter();
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    presenter.resume();
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    presenter.pause();
  }

  public void setLynxConfig(LynxConfig lynxConfig) {
    this.lynxConfig = lynxConfig;
  }

  public LynxConfig getLynxConfig() {
    return lynxConfig;
  }

  @Override public void showTraces(List<Trace> traces) {
    adapter.clear();
    adapter.addAll(traces);
    adapter.notifyDataSetChanged();
  }

  private void initializeConfiguration(AttributeSet attrs) {
    //Obtain Lynx configuration from attrs
    lynxConfig = new LynxConfig();
  }

  private void inflateView() {
    Context context = getContext();
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    layoutInflater.inflate(R.layout.lynx_view, this);
    mapGui();
    hookListeners();
    initializeRenderers();
  }

  private void mapGui() {
    lv_traces = (ListView) findViewById(R.id.lv_traces);
  }

  private void initializeRenderers() {
    RendererBuilder<Trace> tracesRendererBuilder = new TraceRendererBuilder();
    Context context = getContext();
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    adapter = new RendererAdapter<Trace>(layoutInflater, tracesRendererBuilder,
        new ListAdapteeCollection<Trace>());
    lv_traces.setAdapter(adapter);
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

  private void initializePresenter() {
    Lynx lynx = new Lynx();
    presenter = new LynxPresenter(lynx, this, lynxConfig.getMaxNumberOfTracesToShow());
  }
}
