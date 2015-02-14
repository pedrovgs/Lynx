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

package com.github.pedrovgs.lynx.model;

/**
 * Class created to add testability in terms of time usage. Using this wrapper of
 * System.currentTimeMillis instead of use the System call directly we improve our code
 * testability and provide mocked implementations of TimeProvider with pre-configured results.
 *
 * @author Pedro Vicente Gomez Sanchez.
 */
public class TimeProvider {

  public long getCurrentTimeMillis() {
    return System.currentTimeMillis();
  }
}
