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
import com.github.pedrovgs.lynx.LynxActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * @author Pedro Vicente Gómez Sánchez.
 */
public class LynxActivityDefaultValues extends ActivityInstrumentationTestCase2<LynxActivity> {

  private static final String VERBOSE_TRACE_LEVEL = "VERBOSE";

  public LynxActivityDefaultValues() {
    super(LynxActivity.class);
  }

  @Override protected void setUp() throws Exception {
    super.setUp();
    getActivity();
  }

  public void testDoesNotUseFilterByDefault() {
    onView(withId(R.id.et_filter)).check(matches(withText("")));
  }

  public void testUsesVerboseTraceLevelFilterByDefault() {
    onView(withId(R.id.sp_filter)).check(matches(withSpinnerText(VERBOSE_TRACE_LEVEL)));
  }
}
