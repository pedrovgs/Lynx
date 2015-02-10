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
import android.content.Intent;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.pedrovgs.lynx.model.AndroidMainThread;
import com.github.pedrovgs.lynx.model.Logcat;
import com.github.pedrovgs.lynx.model.Lynx;
import com.github.pedrovgs.lynx.model.TimeProvider;
import com.github.pedrovgs.lynx.model.Trace;
import com.github.pedrovgs.lynx.presenter.LynxPresenter;
import com.github.pedrovgs.lynx.renderer.TraceRendererBuilder;
import com.pedrogomez.renderers.ListAdapteeCollection;
import com.pedrogomez.renderers.RendererAdapter;
import com.pedrogomez.renderers.RendererBuilder;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Main library view. This RelativeLayout extension shows all the information printed in your
 * device log. Add this view to your layouts if you want to show your Logcat traces and configure
 * it using styleable attributes.
 *
 * @author Pedro Vicente Gómez Sánchez.
 */
public class LynxView extends RelativeLayout implements LynxPresenter.View {

  private static final String LOGTAG = "LynxView";
  private static final String SHARE_INTENT_TYPE = "text/plain";
  private static final CharSequence SHARE_INTENT_TITLE = "Application Logcat";

  private LynxPresenter presenter;
  private LynxConfig lynxConfig;

  private ListView lv_traces;
  private EditText et_filter;
  private ImageButton ib_share;

  private RendererAdapter<Trace> adapter;
  private int transcriptMode;
  private int lastFirstVisibleItem = -1;

  public LynxView(Context context) {
    this(context, null);
  }

  public LynxView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public LynxView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initializeConfiguration(attrs);
    initializePresenter();
    initializeView();
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    presenter.resume();
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    presenter.pause();
  }

  @Override protected void onVisibilityChanged(View changedView, int visibility) {
    super.onVisibilityChanged(changedView, visibility);
    if (visibility == View.VISIBLE) {
      int lastPosition = adapter.getCount() - 1;
      lv_traces.setSelection(lastPosition);
    }
  }

  public void setLynxConfig(LynxConfig lynxConfig) {
    validateLynxConfig(lynxConfig);
    this.lynxConfig = (LynxConfig) lynxConfig.clone();
    updateFilterText();
    presenter.setLynxConfig(lynxConfig);
  }

  public LynxConfig getLynxConfig() {
    return lynxConfig;
  }

  @Override public void showTraces(List<Trace> traces, int removedTraces) {
    adapter.clear();
    adapter.addAll(traces);
    disableTranscriptMode();
    adapter.notifyDataSetChanged();
    updateScrollPosition(removedTraces);
    recoverTranscriptMode();
  }

  @Override public void disableAutoScroll() {
    transcriptMode = ListView.TRANSCRIPT_MODE_DISABLED;
  }

  @Override public void enableAutoScroll() {
    transcriptMode = ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL;
  }

  @Override public void clear() {
    transcriptMode = ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL;
    adapter.clear();
    adapter.notifyDataSetChanged();
  }

  @Override public void shareTraces(String plainTraces) {
    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
    sharingIntent.setType(SHARE_INTENT_TYPE);
    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, plainTraces);
    getContext().startActivity(Intent.createChooser(sharingIntent, SHARE_INTENT_TITLE));
  }

  private void initializeConfiguration(AttributeSet attrs) {
    lynxConfig = new LynxConfig();
    if (attrs != null) {
      TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.lynx);
      int maxTracesToShow = attributes.getInteger(R.styleable.lynx_max_traces_to_show,
          lynxConfig.getMaxNumberOfTracesToShow());
      String filter = attributes.getString(R.styleable.lynx_filter);
      lynxConfig.withMaxNumberOfTracesToShow(maxTracesToShow).withFilter(filter);
      attributes.recycle();
    }
  }

  private void initializeView() {
    Context context = getContext();
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    layoutInflater.inflate(R.layout.lynx_view, this);
    mapGui();
    initializeRenderers();
    hookListeners();
  }

  private void mapGui() {
    lv_traces = (ListView) findViewById(R.id.lv_traces);
    et_filter = (EditText) findViewById(R.id.et_filter);
    ib_share = (ImageButton) findViewById(R.id.ib_share);
    configureCursorColor();
    updateFilterText();
  }

  /**
   * Hack to change EditText cursor color even if the API level is lower than 12. Please, don't do
   * this at home.
   */
  private void configureCursorColor() {
    try {
      Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
      f.setAccessible(true);
      f.set(et_filter, R.drawable.edit_text_cursor_color);
    } catch (Exception e) {
      Log.e(LOGTAG, "Error trying to change cursor color text cursor drawable to null.");
    }
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
        lastFirstVisibleItem = firstVisibleItem;
        int lastVisiblePosition = firstVisibleItem + visibleItemCount;
        presenter.onScrollToPosition(lastVisiblePosition);
      }
    });

    et_filter.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //Empty
      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        presenter.onFilterUpdated(s.toString());
      }

      @Override public void afterTextChanged(Editable s) {
        //Empty
      }
    });

    ib_share.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        presenter.onShareButtonClicked();
      }
    });
  }

  private void initializePresenter() {
    Lynx lynx = new Lynx(new Logcat(), new AndroidMainThread(), new TimeProvider());
    presenter = new LynxPresenter(lynx, this, lynxConfig.getMaxNumberOfTracesToShow());
  }

  private void validateLynxConfig(LynxConfig lynxConfig) {
    if (lynxConfig == null) {
      throw new IllegalArgumentException(
          "You can't configure Lynx with a null LynxConfig instance.");
    }
  }

  private void disableTranscriptMode() {
    lv_traces.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);
  }

  private void recoverTranscriptMode() {
    lv_traces.setTranscriptMode(transcriptMode);
  }

  private void updateScrollPosition(int removedTraces) {
    if (shouldUpdateScrollPosition(removedTraces)) {
      int scrollPosition = lastFirstVisibleItem + removedTraces - 1;
      boolean isTranscriptModeDisabled =
          lv_traces.getTranscriptMode() == ListView.TRANSCRIPT_MODE_DISABLED;
      if (isTranscriptModeDisabled) {
        lv_traces.setSelection(scrollPosition);
        lastFirstVisibleItem = scrollPosition;
      }
    }
  }

  private boolean shouldUpdateScrollPosition(int removedTraces) {
    return removedTraces > 0 && lastFirstVisibleItem >= 0;
  }

  private void updateFilterText() {
    if (lynxConfig.hasFilter()) {
      et_filter.append(lynxConfig.getFilter());
    }
  }
}
