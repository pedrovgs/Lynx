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

package com.github.pedrovgs.sample;

import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Pedro Vicente Gomez Sanchez.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

  private static final String LYNX_FILTER = "Lynx";
  private static final String VERBOSE_TRACE_LEVEL = "VERBOSE";
  private static final String DEBUG_TRACE_LEVEL = "DEBUG";

  public MainActivityTest() {
    super(MainActivity.class);
  }

  @Override protected void setUp() throws Exception {
    super.setUp();
    getActivity();
  }

  public void testShowsLynxViewAsGoneByDefault() {
    onView(withId(R.id.lynx_view)).check(matches(not(isDisplayed())));
  }

  public void testShowsLynxViewOnShowLynxButtonClicked() {
    onView(withId(R.id.bt_show_lynx_view)).perform(click());

    onView(withId(R.id.lynx_view)).check(matches(isDisplayed()));
  }

  public void testOpensLynxActivityOnOpenLynxActivityButtonClicked() {
    onView(withId(R.id.bt_show_lynx_activity)).perform(click());

    onView(withId(R.id.lynx_view)).check(matches(isDisplayed()));
  }

  public void testShowsLynxViewWithLynxAsFilterOnShowLynxViewButtonClicked() {
    onView(withId(R.id.bt_show_lynx_view)).perform(click());

    onView(withId(R.id.et_filter)).check(matches(withText(LYNX_FILTER)));
  }

  public void testUsesVerboseAsDefaultTraceLevelFilterInLynxViewOnShowLynxViewButtonClicked() {
    onView(withId(R.id.bt_show_lynx_view)).perform(click());

    onView(withId(R.id.tv_spinner_trace_level)).check(matches(withText(VERBOSE_TRACE_LEVEL)));
  }

  public void testOpensLynxActivityUsingLynxAsDefaultFilterOnOpenLynxActivityButtonClicked() {
    onView(withId(R.id.bt_show_lynx_activity)).perform(click());

    onView(withId(R.id.et_filter)).check(matches(withText(LYNX_FILTER)));
  }

  public void testOpensLynxActivityUsingDebugAsDefaultTraceLevelOnOpenLynxActivityButtonClicked() {
    onView(withId(R.id.bt_show_lynx_activity)).perform(click());

    onView(withId(R.id.sp_filter)).check(matches(withSpinnerText(DEBUG_TRACE_LEVEL)));
  }
}
