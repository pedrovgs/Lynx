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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Pedro Vicente Gómez Sánchez.
 */
public class LynxConfigTest {

  private static final String ANY_FILTER = "AnyFilter";

  @Test public void shouldUse2500AsMaxNumberOfTracesByDefault() {
    LynxConfig lynxConfig = new LynxConfig();

    assertEquals(2500, lynxConfig.getMaxNumberOfTracesToShow());
  }

  @Test public void shouldUseNullFilterByDefault() {
    LynxConfig lynxConfig = new LynxConfig();

    assertNull(lynxConfig.getFilter());
  }

  @Test public void shouldHasNoFilterByDefault() {
    LynxConfig lynxConfig = new LynxConfig();

    assertFalse(lynxConfig.hasFilter());
  }

  @Test public void shouldReturnTrueIfHasAnyFilterConfiguredDifferentOfNull() {
    LynxConfig lynxConfig = new LynxConfig().setFilter(ANY_FILTER);

    assertTrue(lynxConfig.hasFilter());
  }

  @Test public void shouldUse36pxAsTextSizeByDefault() {
    LynxConfig lynxConfig = new LynxConfig();

    assertEquals(36, lynxConfig.getTextSizeInPx(), 0.1f);
  }

  @Test public void shouldReturnFalseIfHasNoConfiguredTraceTextSize() {
    LynxConfig lynxConfig = new LynxConfig();

    assertFalse(lynxConfig.hasTextSizeInPx());
  }

  @Test public void shouldReturnTrueIfHasConfiguredTraceTextSize() {
    LynxConfig lynxConfig = new LynxConfig().setTextSizeInPx(10);

    assertTrue(lynxConfig.hasTextSizeInPx());
  }

  @Test public void shouldUse150AsDefaultSamplingRate() {
    LynxConfig lynxConfig = new LynxConfig();

    assertEquals(150, lynxConfig.getSamplingRate());
  }
}
