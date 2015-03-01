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
