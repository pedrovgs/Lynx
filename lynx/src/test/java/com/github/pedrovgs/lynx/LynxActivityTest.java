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

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

/**
 * @author Pedro Vicente Gómez Sánchez.
 */
@Config(constants = BuildConfig.class, sdk = 18)
@RunWith(RobolectricTestRunner.class)
public class LynxActivityTest {

  private static final int ANY_MAX_NUMBER_OF_TRACES = 1000;
  private static final String EXTRA_LYNX_CONFIG = "extra_lynx_config";
  private static final String ANY_FILTER = "AnyFilter";

  private LynxActivity lynxActivity;

  @Before public void setUp() {
    lynxActivity = Robolectric.buildActivity(LynxActivity.class).create().resume().get();
  }

  @Test public void shouldShowLynxViewAsVisible() {
    LynxView lynxView = getLynxView();

    int lynxViewVisibility = lynxView.getVisibility();

    assertEquals(View.VISIBLE, lynxViewVisibility);
  }

  @Test public void shouldShowLynxViewUsingMatchParentAsConfiguration() {
    LynxView lynxView = getLynxView();

    ViewGroup.LayoutParams layoutParams = lynxView.getLayoutParams();
    int lynxViewHeight = layoutParams.height;
    int lynxViewWidth = layoutParams.width;

    assertEquals(ViewGroup.LayoutParams.MATCH_PARENT, lynxViewHeight);
    assertEquals(ViewGroup.LayoutParams.MATCH_PARENT, lynxViewWidth);
  }

  @Test
  public void shouldConfigureLynxViewWithTheDefaultLynxConfigObjectIfThereIsNoOtherConfiguration() {
    LynxView lynxView = getLynxView();

    LynxConfig lynxConfig = lynxView.getLynxConfig();

    LynxConfig defaultLynxConfig = new LynxConfig();
    assertEquals(defaultLynxConfig, lynxConfig);
  }

  @Test public void shouldConfigureLynxViewWithTheDefaultLynxConfigIfUsesGetIntentMethod() {
    Intent intent = LynxActivity.getIntent(RuntimeEnvironment.application);

    LynxActivity lynxActivity =
        Robolectric.buildActivity(LynxActivity.class).withIntent(intent).create().resume().get();

    LynxView lynxView = (LynxView) lynxActivity.findViewById(R.id.lynx_view);
    assertEquals(new LynxConfig(), lynxView.getLynxConfig());
  }

  @Test public void shouldPassLynxConfigurationToLynxView() {
    LynxConfig lynxConfig =
        new LynxConfig().setMaxNumberOfTracesToShow(ANY_MAX_NUMBER_OF_TRACES).setFilter(ANY_FILTER);
    Intent intent = LynxActivity.getIntent(RuntimeEnvironment.application, lynxConfig);

    LynxActivity lynxActivity =
        Robolectric.buildActivity(LynxActivity.class).withIntent(intent).create().resume().get();

    LynxView lynxView = (LynxView) lynxActivity.findViewById(R.id.lynx_view);
    assertEquals(lynxConfig, lynxView.getLynxConfig());
  }

  private LynxView getLynxView() {
    return (LynxView) lynxActivity.findViewById(R.id.lynx_view);
  }
}
