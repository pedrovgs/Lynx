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
 * Main library view. Custom view based on a RelativeLayout used to show all the information
 * printed by the Android Logcat. Add this view to your layouts if you want to show your Logcat
 * traces and configure it using styleable attributes.
 *
 * @author Pedro Vicente Gomez Sanchez.
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
  private int lastScrollPosition;

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

  /**
   * Initializes LynxPresenter if LynxView is visible when is attached to the window.
   */
  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (isVisible()) {
      resumePresenter();
    }
  }

  /**
   * Stops LynxPresenter when LynxView is detached from the window.
   */
  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    pausePresenter();
  }

  /**
   * Initializes or stops LynxPresenter based on visibility changes. Doing this Lynx is not going
   * to
   * read your application Logcat if LynxView is not visible or attached.
   */
  @Override protected void onVisibilityChanged(View changedView, int visibility) {
    super.onVisibilityChanged(changedView, visibility);
    if (changedView != this) {
      return;
    }

    if (visibility == View.VISIBLE) {
      resumePresenter();
    } else {
      pausePresenter();
    }
  }

  /**
   * Given a valid LynxConfig object update all the dependencies to apply this new configuration.
   */
  public void setLynxConfig(LynxConfig lynxConfig) {
    validateLynxConfig(lynxConfig);
    boolean hasChangedLynxConfig = !this.lynxConfig.equals(lynxConfig);
    if (hasChangedLynxConfig) {
      this.lynxConfig = (LynxConfig) lynxConfig.clone();
      updateFilterText();
      updateAdapter();
      presenter.setLynxConfig(lynxConfig);
    }
  }

  /**
   * Returns the current LynxConfig object used.
   */
  public LynxConfig getLynxConfig() {
    return lynxConfig;
  }

  /**
   * Given a List<Trace> updates the ListView adapter with this information and keeps the scroll
   * position if needed.
   */
  @Override public void showTraces(List<Trace> traces, int removedTraces) {
    if (lastScrollPosition == 0) {
      lastScrollPosition = lv_traces.getFirstVisiblePosition();
    }
    adapter.clear();
    adapter.addAll(traces);
    adapter.notifyDataSetChanged();
    updateScrollPosition(removedTraces);
  }

  /**
   * Removes all the traces rendered in the ListView.
   */
  @Override public void clear() {
    adapter.clear();
    adapter.notifyDataSetChanged();
  }

  /**
   * Uses an intent to share content and given one String with all the information related to the
   * List of traces shares this information with other applications.
   */
  @Override public void shareTraces(String plainTraces) {
    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
    sharingIntent.setType(SHARE_INTENT_TYPE);
    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, plainTraces);
    getContext().startActivity(Intent.createChooser(sharingIntent, SHARE_INTENT_TITLE));
  }

  @Override public void disableAutoScroll() {
    lv_traces.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);
  }

  @Override public void enableAutoScroll() {
    lv_traces.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
  }

  private boolean isPresenterReady() {
    return presenter != null;
  }

  private void resumePresenter() {
    if (isPresenterReady()) {
      presenter.resume();
      int lastPosition = adapter.getCount() - 1;
      lv_traces.setSelection(lastPosition);
    }
  }

  private void pausePresenter() {
    if (isPresenterReady()) {
      presenter.pause();
    }
  }

  private boolean isVisible() {
    return getVisibility() == View.VISIBLE;
  }

  private void initializeConfiguration(AttributeSet attrs) {
    lynxConfig = new LynxConfig();
    if (attrs != null) {
      TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.lynx);

      int maxTracesToShow = attributes.getInteger(R.styleable.lynx_max_traces_to_show,
          lynxConfig.getMaxNumberOfTracesToShow());
      String filter = attributes.getString(R.styleable.lynx_filter);
      float fontSizeInPx = attributes.getDimension(R.styleable.lynx_text_size, -1);
      if (fontSizeInPx != -1) {
        fontSizeInPx = pixelsToSp(fontSizeInPx);
        lynxConfig.setTextSizeInPx(fontSizeInPx);
      }
      int samplingRate =
          attributes.getInteger(R.styleable.lynx_sampling_rate, lynxConfig.getSamplingRate());

      lynxConfig.setMaxNumberOfTracesToShow(maxTracesToShow)
          .setFilter(filter)
          .setSamplingRate(samplingRate);

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
    lv_traces.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
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
    RendererBuilder<Trace> tracesRendererBuilder = new TraceRendererBuilder(lynxConfig);
    Context context = getContext();
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    adapter = new RendererAdapter<Trace>(layoutInflater, tracesRendererBuilder,
        new ListAdapteeCollection<Trace>());
    adapter.addAll(presenter.getCurrentTraces());
    if (adapter.getCount() > 0) {
      adapter.notifyDataSetChanged();
    }
    lv_traces.setAdapter(adapter);
  }

  private void hookListeners() {
    lv_traces.setOnScrollListener(new AbsListView.OnScrollListener() {
      @Override public void onScrollStateChanged(AbsListView view, int scrollState) {
        //Empty
      }

      @Override public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
          int totalItemCount) {
        //Hack to avoid problems with the scroll position when auto scroll is disabled. This hack
        // is needed because Android notify a firstVisibleItem one position before it should be.
        if (lastScrollPosition - firstVisibleItem != 1) {
          lastScrollPosition = firstVisibleItem;
        }
        int lastVisiblePositionInTheList = firstVisibleItem + visibleItemCount;
        presenter.onScrollToPosition(lastVisiblePositionInTheList);
      }
    });
    et_filter.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //Empty
      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        presenter.updateFilter(s.toString());
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
    lynx.setConfig(lynxConfig);
    presenter = new LynxPresenter(lynx, this, lynxConfig.getMaxNumberOfTracesToShow());
  }

  private void validateLynxConfig(LynxConfig lynxConfig) {
    if (lynxConfig == null) {
      throw new IllegalArgumentException(
          "You can't configure Lynx with a null LynxConfig instance.");
    }
  }

  private void updateFilterText() {
    if (lynxConfig.hasFilter()) {
      et_filter.append(lynxConfig.getFilter());
    }
  }

  private void updateAdapter() {
    if (lynxConfig.hasTextSizeInPx()
        && this.lynxConfig.getTextSizeInPx() != lynxConfig.getTextSizeInPx()) {
      initializeRenderers();
    }
  }

  private float pixelsToSp(float px) {
    float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
    return px / scaledDensity;
  }

  private void updateScrollPosition(int removedTraces) {
    boolean shouldUpdateScrollPosition = removedTraces > 0;
    if (shouldUpdateScrollPosition) {
      int newScrollPosition = lastScrollPosition - removedTraces;
      lastScrollPosition = newScrollPosition;
      lv_traces.setSelectionFromTop(newScrollPosition, 0);
    }
  }

  /**
   * Backdoor used to replace the presenter used in this view. This method should be used just for
   * testing purposes.
   */
  void setPresenter(LynxPresenter presenter) {
    this.presenter = presenter;
  }
}
