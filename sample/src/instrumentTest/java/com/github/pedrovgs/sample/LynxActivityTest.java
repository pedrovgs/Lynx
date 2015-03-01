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
import com.github.pedrovgs.lynx.model.Trace;
import com.github.pedrovgs.lynx.model.TraceLevel;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * @author Pedro Vicente Gómez Sánchez.
 */
public class LynxActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

  private static final String ANY_TRACE_FILTER = "Lynx";

  public LynxActivityTest() {
    super(MainActivity.class);
  }

  @Override protected void setUp() throws Exception {
    super.setUp();
    getActivity();
    onView(withId(R.id.bt_show_lynx_activity)).perform(click());
    onView(withId(R.id.et_filter)).perform(clearText());
  }

  public void testAppliesTraceFilterToShowsJustTracesMatchingFilter() {
    onView(withId(R.id.et_filter)).perform(typeText(ANY_TRACE_FILTER), closeSoftKeyboard());

    waitForSomeTraces();

    onData(allOf(is(instanceOf(Trace.class)),
        traceMatcherWithMessage(ANY_TRACE_FILTER))).inAdapterView(withId(R.id.lv_traces))
        .check(matches(isDisplayed()));
  }

  public void testShowsTracesEqualsOrGreaterThanVerboseTraceLevelOnTraceLevelSelected() {
    selectFilterByTraceLevel(TraceLevel.VERBOSE);

    waitForSomeTraces();

    assertShowsTraceMatchingTraceLevel(TraceLevel.VERBOSE);
    assertShowsTraceMatchingTraceLevel(TraceLevel.DEBUG);
    assertShowsTraceMatchingTraceLevel(TraceLevel.INFO);
    assertShowsTraceMatchingTraceLevel(TraceLevel.WARNING);
    assertShowsTraceMatchingTraceLevel(TraceLevel.ERROR);
    assertShowsTraceMatchingTraceLevel(TraceLevel.WTF);
  }

  public void testShowsTracesEqualsOrGreaterThanDebugTraceLevelOnTraceLevelSelected() {
    selectFilterByTraceLevel(TraceLevel.DEBUG);

    waitForSomeTraces();

    assertShowsTraceMatchingTraceLevel(TraceLevel.DEBUG);
    assertShowsTraceMatchingTraceLevel(TraceLevel.INFO);
    assertShowsTraceMatchingTraceLevel(TraceLevel.WARNING);
    assertShowsTraceMatchingTraceLevel(TraceLevel.ERROR);
    assertShowsTraceMatchingTraceLevel(TraceLevel.WTF);
  }

  public void testShowsTracesEqualsOrGreaterThanInfoTraceLevelOnTraceLevelSelected() {
    selectFilterByTraceLevel(TraceLevel.INFO);

    waitForSomeTraces();

    assertShowsTraceMatchingTraceLevel(TraceLevel.INFO);
    assertShowsTraceMatchingTraceLevel(TraceLevel.WARNING);
    assertShowsTraceMatchingTraceLevel(TraceLevel.ERROR);
    assertShowsTraceMatchingTraceLevel(TraceLevel.WTF);
  }

  public void testShowsTracesEqualsOrGreaterThanWARNINGTraceLevelOnTraceLevelSelected() {
    selectFilterByTraceLevel(TraceLevel.WARNING);

    waitForSomeTraces();

    assertShowsTraceMatchingTraceLevel(TraceLevel.WARNING);
    assertShowsTraceMatchingTraceLevel(TraceLevel.ERROR);
    assertShowsTraceMatchingTraceLevel(TraceLevel.WTF);
  }

  public void testShowsTracesEqualsOrGreaterThanErrorTraceLevelOnTraceLevelSelected() {
    selectFilterByTraceLevel(TraceLevel.ERROR);

    waitForSomeTraces();

    assertShowsTraceMatchingTraceLevel(TraceLevel.ERROR);
    assertShowsTraceMatchingTraceLevel(TraceLevel.WTF);
  }

  public void testShowsTracesEqualsOrGreaterThanWtfRTraceLevelOnTraceLevelSelected() {
    selectFilterByTraceLevel(TraceLevel.WTF);

    waitForSomeTraces();

    assertShowsTraceMatchingTraceLevel(TraceLevel.WTF);
  }

  private void selectFilterByTraceLevel(TraceLevel traceLevel) {
    onView(withId(R.id.sp_filter)).perform(click());
    onData(allOf(is(instanceOf(TraceLevel.class)), is(equalTo(traceLevel)))).perform(click());
  }

  private void assertShowsTraceMatchingTraceLevel(TraceLevel traceLevel) {
    onData(allOf(is(instanceOf(Trace.class)), traceMatcherWithLevel(traceLevel))).inAdapterView(
        withId(R.id.lv_traces)).check(matches(isDisplayed()));
  }

  private Matcher<Object> traceMatcherWithLevel(final TraceLevel traceLevel) {
    return new BaseMatcher<Object>() {
      private boolean hasPreviousMatch;

      @Override public boolean matches(Object o) {
        if (o instanceof Trace && !hasPreviousMatch) {
          Trace trace = (Trace) o;
          if (trace.getLevel().equals(traceLevel)) {
            hasPreviousMatch = true;
            return true;
          }
        }
        return false;
      }

      @Override public void describeTo(Description description) {
        description.appendText("Trace has '")
            .appendText(traceLevel.name())
            .appendText("' as TraceLevel.");
      }
    };
  }

  private Matcher<Object> traceMatcherWithMessage(final String message) {
    return new BaseMatcher<Object>() {
      private boolean hasPreviousMatch;

      @Override public boolean matches(Object o) {
        if (o instanceof Trace && !hasPreviousMatch) {
          Trace trace = (Trace) o;
          if (trace.getMessage().contains(message) && !hasPreviousMatch) {
            hasPreviousMatch = true;
            return true;
          }
        }
        return false;
      }

      @Override public void describeTo(Description description) {
        description.appendText("Trace contains '").appendText(message + "' in trace message.");
      }
    };
  }

  /**
   * Ugly sleep used for some of this tests. This method is needed because we can't provide Log
   * traces from the test application process and the default traces generation is implemented in
   * MainActivity. onData method doesn't wait until our traces are displayed with the filter
   * provided. That's why I need this Thread.Sleep. Please, don't do this at home.
   */
  private void waitForSomeTraces() {
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
